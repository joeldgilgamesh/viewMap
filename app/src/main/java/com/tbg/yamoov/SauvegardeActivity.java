package com.tbg.yamoov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tbg.yamoov.adapter.GalleryAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SauvegardeActivity extends AppCompatActivity {

    private Button btn,btnIm;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;
    String Storage_Path = "Sauvegarde_Image/";
    // Creating ImageView.
    EditText titreSv, descriptionSv ;


    private static final int PICK_IMAGE = 1;
    Button chooserBtn, uploaderBtn;
    TextView alert;
    private Uri ImageUri;
    ArrayList ImageList = new ArrayList();
    private int upload_count = 0;
    private ProgressDialog progressDialog;
    ArrayList urlStrings;
    ArrayList mArrayUri = new ArrayList();
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sauvegarde);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn = findViewById(R.id.ButtonChooseImage);
        btnIm = findViewById(R.id.ButtonUploadImage);

        gvGallery = (GridView)findViewById(R.id.gv);
        titreSv = (EditText)findViewById(R.id.titre);
        descriptionSv = (EditText)findViewById(R.id.description);


        progressDialog = new ProgressDialog(SauvegardeActivity.this);
        progressDialog.setMessage("Uploading Images please Wait.........!!!!!!");



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);

            }
        });
        btnIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlStrings = new ArrayList<>();
                progressDialog.show();

                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageSauvegarde");

                for (upload_count = 0; upload_count < mArrayUri.size(); upload_count++) {

                    Uri IndividualImage = (Uri) mArrayUri.get(upload_count);
                    final StorageReference ImageName = ImageFolder.child("Images" + IndividualImage.getLastPathSegment());

                    ImageName.putFile(IndividualImage).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    ImageName.getDownloadUrl().addOnSuccessListener(
                                            new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    urlStrings.add(String.valueOf(uri));



                                                    if (urlStrings.size() == mArrayUri.size()){
                                                        storeLink(urlStrings);
                                                    }

                                                }
                                            }
                                    );
                                }
                            }
                    );


                }



            }
        });


    }


    private void storeLink(ArrayList<String> urlStrings) {

       // HashMap<String, String> hashMap = new HashMap<>();
        Map<String, Object> sauvegarde = new HashMap<>();


        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String titreSauvegarde = titreSv.getText().toString().trim();
        String descriptionSauvegarde = descriptionSv.getText().toString().trim();

        // Hiding the progressDialog after done uploading.

        sauvegarde.put("titre", titreSauvegarde);
        sauvegarde.put("description", descriptionSauvegarde);
        sauvegarde.put("date", currentDate);
        for (int i = 0; i <urlStrings.size() ; i++) {
            sauvegarde.put("image"+i, urlStrings.get(i));
        }

        FirebaseFirestore.getInstance().collection("Sauvegarde")
                .add(sauvegarde)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(SauvegardeActivity.this, "Successfully Uplosded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SauvegardeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        progressDialog.dismiss();
        //alert.setText("Uploaded Successfully");
        //uploaderBtn.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Uploaded Successfully ", Toast.LENGTH_LONG).show();

        mArrayUri.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();


                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                    Toast.makeText(this, mArrayUri.toString(),
                            Toast.LENGTH_LONG).show();
                    //UploadImageFileToFirebaseStorage(uri);


                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);



                           /*Toast.makeText(this, mArrayUri.toString(),
                                    Toast.LENGTH_LONG).show();*/

                        }

                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


}