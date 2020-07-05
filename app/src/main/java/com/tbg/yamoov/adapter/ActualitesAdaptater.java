package com.tbg.yamoov.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.telecom.Call;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tbg.yamoov.R;
import com.tbg.yamoov.model.Actualite;
import com.tbg.yamoov.model.CardModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActualitesAdaptater extends ArrayAdapter<Actualite> implements Filterable {

    List<Actualite> mActualiteList;

    public ActualitesAdaptater(Context context, List<Actualite> object){
        super(context,0, object);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView =  ((Activity)getContext()).getLayoutInflater().inflate(R.layout.items,parent,false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.text_title);
        TextView expTextView = (TextView) convertView.findViewById(R.id.text_subtitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.text_date);
        /*TextView descriptionTextView = (TextView) convertView.findViewById(R.id.mission_description);*/

        Actualite actualite = getItem(position);

        titleTextView.setText(actualite.getTitre());
        expTextView.setText(actualite.getDescription().toString());
        dateTextView.setText(actualite.getDate());
        String imageUrl = actualite.getImage();
        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(imageView);

        return convertView;

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
