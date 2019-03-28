package com.example.abrig.filterbygesture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TakePicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);
    }

    public void startGalleryIntent(View view) {
        Intent intent = new Intent(TakePicActivity.this, GalleryActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startFirstIntent(View view) {
        Intent intent = new Intent(TakePicActivity.this, MainActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startFilterIntent(View view) {
        Intent intent = new Intent(TakePicActivity.this, FilterActivity.class);
        startActivityForResult(intent, 1);
    }
}
