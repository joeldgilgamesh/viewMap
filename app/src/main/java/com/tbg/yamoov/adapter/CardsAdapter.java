package com.tbg.yamoov.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbg.yamoov.MainActivity;
import com.tbg.yamoov.R;
import com.tbg.yamoov.model.CardModel;

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */

public class CardsAdapter extends ArrayAdapter<CardModel> {

    public CardsAdapter(Context context) {
        super(context, R.layout.items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.items, parent, false);
            holder = new ViewHolder(convertView,position);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CardModel model = getItem(position);

       // holder.imageView.setImageResource(model.getImageId());
        holder.tvTitle.setText(model.getTitle());
        holder.tvSubtitle.setText(model.getSubtitle());

        return convertView;
    }

    static class ViewHolder {
        //ImageView imageView;
        TextView tvTitle;
        TextView tvSubtitle;
        Context context;
        ViewHolder(View view, int position) {
            //imageView = (ImageView) view.findViewById(R.id.image);
            tvTitle = (TextView) view.findViewById(R.id.text_title);
            tvSubtitle = (TextView) view.findViewById(R.id.text_subtitle);
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    Toast.makeText(context, tvTitle.toString() , Toast.LENGTH_LONG).show();
                   // Log.i("W4K","Click-"+position);
                    //context.startActivity(new Intent(context, MainActivity.class));
                }
            });
        }

    }


}
