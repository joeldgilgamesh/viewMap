package com.tbg.yamoov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.google.firebase.iid.FirebaseInstanceId;

public class DetailActualiteActivity extends AppCompatActivity {

    private TextView TextTitle, TextDescript,TextDate;
    private ImageView imageView;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_actualite);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextTitle = (TextView)findViewById(R.id.title_txt);
        TextDescript = (TextView)findViewById(R.id.description_txt);
        TextDate = (TextView)findViewById(R.id.date_txt);
        imageView = (ImageView)findViewById(R.id.cover_img);
        btn = (Button)findViewById(R.id.btnMessage);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("tvTitle");
        String description = bundle.getString("tvSubtitle");
        String date = bundle.getString("date");
        String image = bundle.getString("image");
        //Toast.makeText(DetailActualiteActivity.this, image, Toast.LENGTH_LONG).show();
        String imageUrl =image;
        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(imageView);

        TextTitle.setText(title);
        TextDescript.setText(description);
        TextDate.setText(date);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                   // Log.w(TAG, "getInstanceId failed", task.getException());
                                    Toast.makeText(DetailActualiteActivity.this, "getInstanceId failed", Toast.LENGTH_SHORT).show();

                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
                               // String msg = getString(R.string.msg_token_fmt, token);
                                //Log.d(TAG, msg);
                                Toast.makeText(DetailActualiteActivity.this, "msg", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}