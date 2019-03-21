package com.example.abrig.gesture_app_5;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Main4Activity extends AppCompatActivity implements ThumbnailCallback{

    public ImageView targetImage;
    public ImageView currImage; // which ImageButton pic is selected (tiny versions)
    public TextView textTargetUri;
    private RecyclerView thumbListView;
    private Activity activity;
    private FloatingActionButton fab;
    public Filter myFilter;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        targetImage = findViewById(R.id.targetimage);
        currImage = targetImage;//getResources(R.drawable.dog);
        initUIWidgets();
        activity = this;

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        ImageButton dog = findViewById(R.id.imageButton2_dog);
        ImageButton canoe = findViewById(R.id.imageButton_canoe);
        ImageButton london = findViewById(R.id.imageButton_london);
        ImageButton ski = findViewById(R.id.imageButton_ski_resort);

        dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap pic = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.dog), 640, 640, false);
                Uri uri = getImageUri(getApplicationContext(), pic);
//                adjustPicture(uri);
                targetImage.setImageBitmap(pic);
                Snackbar.make(view, "Dog set.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        london.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap pic = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.london_skyline), 640, 640, false);
                Uri uri = getImageUri(getApplicationContext(), pic);
//                adjustPicture(uri);
                targetImage.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.london_skyline), 640, 640, false));
                Snackbar.make(view, "London Skyline set.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        canoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap pic = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.canoe_lake), 640, 640, false);
                Uri uri = getImageUri(getApplicationContext(), pic);
//                adjustPicture(uri);
                targetImage.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.canoe_lake), 640, 640, false));
                Snackbar.make(view, "Canoe in Lake set.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ski.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap pic = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ski_resort), 640, 640, false);
                Uri uri = getImageUri(getApplicationContext(), pic);
//                adjustPicture(uri);
                targetImage.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ski_resort), 640, 640, false));
                Snackbar.make(view, "Ski Resort set.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    public void filterButtonCLicked() {
        // TODO Auto-generated method stub

    }

    public void adjustPicture(Uri targetUri){
        assert targetUri != null;
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
            String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
            Cursor cur = getContentResolver().query(targetUri, orientationColumn, null, null, null);
            int orientation = -1;
            if (cur != null && cur.moveToFirst()) {
                orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
            }
            TextView tv = findViewById(R.id.textView1);
            tv.setText("Rotate: " + orientation);
            while(orientation >= 0){
                Matrix matrix = new Matrix();
                if(orientation > 0) {
                    matrix.postRotate(90);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                orientation -= 90;
            }
//                targetImage.setImageBitmap(bitmap);

            myFilter = new Filter();
            myFilter.addSubFilter(new BrightnessSubFilter(30));
            myFilter.addSubFilter(new ContrastSubFilter(1.1f));
            if(bitmap != null) {
                Bitmap outputImage = myFilter.processFilter(bitmap);
                targetImage.setImageBitmap(outputImage);
            }
            else{
                targetImage.setImageBitmap(bitmap);
            }
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
//                e.printStackTrace();

        }
        catch (Exception e){ // exception is thrown for some random pictures...
            TextView tv = findViewById(R.id.textView1);
            tv.setText("ERROR setting preset");
            targetImage.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.dog), 640, 640, false));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            // do your stuff.
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            adjustPicture(targetUri);
        }
    }

    public void startFirstIntent(View view) {
        Intent intent = new Intent(Main4Activity.this,MainActivity.class);
        startActivityForResult(intent,1);
    }

    private void initUIWidgets() {
        thumbListView = (RecyclerView) findViewById(R.id.thumbnails);
        targetImage = (ImageView) findViewById(R.id.targetimage);
//        if(currImage.equals((android.widget.ImageView) Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.dog), 640, 640, false))){
//
//        }
        targetImage.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.dog), 640, 640, false));
        currImage = targetImage;
        initHorizontalList();
    }

    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        thumbListView.setLayoutManager(layoutManager);
        thumbListView.setHasFixedSize(true);
        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        final Context context = this.getApplication();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                ImageView dog = findViewById(R.id.imageButton2_dog);
                ImageView canoe = findViewById(R.id.imageButton_canoe);
                ImageView london = findViewById(R.id.imageButton_london);
                ImageView ski = findViewById(R.id.imageButton_ski_resort);
                Bitmap thumbImage;
                if(currImage.equals(dog)){
                    thumbImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.dog), 640, 640, false);
                }
                else if(currImage.equals(canoe)){
                    thumbImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.canoe_lake), 640, 640, false);
                }
                else if(currImage.equals(ski)){
                    thumbImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ski_resort), 640, 640, false);
                }
                else{
                    thumbImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.london_skyline), 640, 640, false);
                }
                ThumbnailItem t1 = new ThumbnailItem();
                ThumbnailItem t2 = new ThumbnailItem();
                ThumbnailItem t3 = new ThumbnailItem();
                ThumbnailItem t4 = new ThumbnailItem();
                ThumbnailItem t5 = new ThumbnailItem();
                ThumbnailItem t6 = new ThumbnailItem();

                t1.image = thumbImage;
                t2.image = thumbImage;
                t3.image = thumbImage;
                t4.image = thumbImage;
                t5.image = thumbImage;
                t6.image = thumbImage;
                ThumbnailsManager.clearThumbs();
                ThumbnailsManager.addThumb(t1); // Original Image

                t2.filter = SampleFilters.getStarLitFilter();
                ThumbnailsManager.addThumb(t2);

                t3.filter = SampleFilters.getBlueMessFilter();
                ThumbnailsManager.addThumb(t3);

                t4.filter = SampleFilters.getAweStruckVibeFilter();
                ThumbnailsManager.addThumb(t4);

                t5.filter = SampleFilters.getLimeStutterFilter();
                ThumbnailsManager.addThumb(t5);

                t6.filter = SampleFilters.getNightWhisperFilter();
                ThumbnailsManager.addThumb(t6);

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);

                ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, (ThumbnailCallback) activity);
                thumbListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    @Override
    public void onThumbnailClick(com.zomato.photofilters.imageprocessors.Filter filter) {
        myFilter = new Filter();
        targetImage.setImageBitmap(myFilter.processFilter(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.dog), 640, 640, false)));
        currImage = targetImage;
    }
}
