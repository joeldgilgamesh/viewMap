package com.tbg.yamoov.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.tbg.yamoov.R;
import com.tbg.yamoov.model.Actualite;
import com.tbg.yamoov.model.CardModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ActualitesAdaptater extends ArrayAdapter<Actualite> {

    public ActualitesAdaptater(Context context, List<Actualite> object){
        super(context,0, object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView =  ((Activity)getContext()).getLayoutInflater().inflate(R.layout.items,parent,false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.text_title);
        TextView expTextView = (TextView) convertView.findViewById(R.id.text_subtitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        /*TextView dateTextView = (TextView) convertView.findViewById(R.id.mission_date);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.mission_description);*/

        Actualite actualite = getItem(position);

        titleTextView.setText(actualite.getTitre());
        expTextView.setText(actualite.getDescription().toString());

        String imageUrl = actualite.getImage();
        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(imageView);

        //  Picasso.get().load(actualite.getImage()).into(imageView);
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
        /*dateTextView.setText(mission.getDate());
        descriptionTextView.setText(mission.getDescription());*/


        return convertView;
    }
}
