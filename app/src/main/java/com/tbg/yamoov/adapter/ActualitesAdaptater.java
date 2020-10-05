package com.tbg.yamoov.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tbg.yamoov.DetailActualiteActivity;
import com.tbg.yamoov.MainActivity;
import com.tbg.yamoov.ProfilActivity;
import com.tbg.yamoov.R;
import com.tbg.yamoov.model.Actualite;
import com.tbg.yamoov.model.CardModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActualitesAdaptater extends ArrayAdapter<Actualite> implements Filterable {

    List<Actualite> mActualiteList;
    Context context;
    public ActualitesAdaptater(Context context, List<Actualite> object){
        super(context,0, object);
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if(convertView == null){
            convertView =  ((Activity)getContext()).getLayoutInflater().inflate(R.layout.items,parent,false);
            holder = new ViewHolder(convertView,position, context);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.text_title);
        TextView expTextView = (TextView) convertView.findViewById(R.id.text_subtitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.text_date);
        /*TextView descriptionTextView = (TextView) convertView.findViewById(R.id.mission_description);*/
        Actualite actualite = getItem(position);
        holder.tvTitle = actualite.getTitre();
        holder.tvSubtitle = actualite.getDescription();
        holder.date = actualite.getDate();
        holder.imageView = actualite.getImage();

        titleTextView.setText(actualite.getTitre());
        expTextView.setText(actualite.getDescription().toString());
        dateTextView.setText(actualite.getDate());
        String imageUrl = actualite.getImage();
        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(imageView);

        return convertView;

    }
    static class ViewHolder {
        String tvTitle;
        String tvSubtitle;
        String date;
        String imageView;

        ViewHolder(View view, int position,Context context) {


            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    Intent intent=new Intent(context, DetailActualiteActivity.class);

                    intent.putExtra("tvTitle", tvTitle);
                    intent.putExtra("tvSubtitle", tvSubtitle);
                    intent.putExtra("date", date);
                    intent.putExtra("image", imageView);

                    context.startActivity(intent);
                }
            });
        }

    }



    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0){
                    filterResults.count = mActualiteList.size();
                    filterResults.values = mActualiteList;
                }else {
                    String searchStr = constraint.toString().toLowerCase();
                    List<Actualite> resultDate = new ArrayList<>();
                    for (Actualite actualite: mActualiteList){
                        if (actualite.getTitre().contains(searchStr)){
                            resultDate.add(actualite);
                        }
                        filterResults.count = resultDate.size();
                        filterResults.values = resultDate;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
        return super.getFilter();
    }
}
