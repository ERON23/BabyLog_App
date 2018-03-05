package com.example.maceo.babylog;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class BabyInfoActivity extends AppCompatActivity {


    ImageView imageView;
    Button mSaveButton;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_info);

        imageView = (ImageView) findViewById(R.id.imageView);
        mSaveButton = (Button) findViewById(R.id.savebaby_info_button);

        mSaveButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){

                Intent r = new Intent(BabyInfoActivity.this, HomeActivity.class);
                startActivity(r);

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }



    }

