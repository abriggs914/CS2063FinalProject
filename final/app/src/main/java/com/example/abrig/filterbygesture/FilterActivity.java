package com.example.abrig.filterbygesture;

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
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

public class FilterActivity extends AppCompatActivity {

    private TextView gestureText;
    private GestureDetector myGestureDectector;
    private int count = 0;

    private ImageView selectedImage;
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        gestureText = (TextView) findViewById(R.id.tvGesture);
        AndroidGestureDectector androidGestureDectector = new AndroidGestureDectector();
        myGestureDectector = new GestureDetector(FilterActivity.this, androidGestureDectector);
        selectedImage = findViewById(R.id.displayedImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            adjustPicture(targetUri, bitmap, false);
        }
    }

    public void adjustPicture(Uri targetUri, Bitmap pic, boolean isPreset){
        assert targetUri != null;
        Bitmap bitmap = pic;
        try {
            if(!isPreset) {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
                Cursor cur = getContentResolver().query(targetUri, orientationColumn, null, null, null);
                int orientation = -1;
                if (cur != null && cur.moveToFirst()) {
                    orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
                }
//                tv.setText("Rotate: " + orientation);
                while (orientation >= 0) {
                    Matrix matrix = new Matrix();
                    if (orientation > 0) {
                        matrix.postRotate(90);
                    }
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    orientation -= 90;
                }
            }
                selectedImage.setImageBitmap(bitmap);
            selectedImageBitmap = bitmap;
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
//                e.printStackTrace();

        }
        catch (ArithmeticException e){ // exception is thrown for some random pictures...
//            tv.setText("ERROR setting preset dog");
//            selectedImage.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.dog), 640, 640, false));
        }
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

    public void startTakePicIntent(View view) {
        Intent intent = new Intent(FilterActivity.this, TakePicActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startGalleryIntent(View view) {
        Intent intent = new Intent(FilterActivity.this, GalleryActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startFirstIntent(View view) {
        Intent intent = new Intent(FilterActivity.this, MainActivity.class);
        startActivityForResult(intent, 1);
    }

    public void selectPictureFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    public void saveEditedPicture(View view) {
        if(selectedImageBitmap == null){
            Toast.makeText(FilterActivity.this, "No picture selected yet", Toast.LENGTH_LONG).show();
            return;
        }
        Bitmap finalBitmap = selectedImageBitmap;
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();

        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
            //     Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
// Tell the media scanner about the new file so that it is
// immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
        Toast.makeText(FilterActivity.this, "Picture saved successfully",Toast.LENGTH_LONG).show();
    }

    class AndroidGestureDectector implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            gestureText.setText("onSingleTapConfirmed");
            updateNumTouches();
            Log.d("Gesture ", "onSingleTapConfirmed");
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            gestureText.setText("onDoubleTap");
            updateNumTouches();
            Log.d("Gesture ", "onDoubleTap");
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            gestureText.setText("onDoubleTapEvent");
            updateNumTouches();
            Log.d("Gesture ", "onDoubleTapEvent");
            return false;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            gestureText.setText("onDown");
//            count--;
            updateNumTouches();
            Log.d("Gesture ", "onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            String line = "onShowPress";
            count--;
            updateNumTouches();
            Toast.makeText(FilterActivity.this, line, Toast.LENGTH_LONG).show();
            gestureText.setText(line);
            Log.d("Gesture ", "onShowPress");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            gestureText.setText("onSingleTapUp");
            updateNumTouches();
//            count++;
            Log.d("Gesture ", "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            gestureText.setText("onScroll");
            String line = "";
//            count++;
            updateNumTouches();
            line = "";
            if (motionEvent.getX() < motionEvent1.getX()) {
                line = "GESTURE: Left to Right Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
                Toast.makeText(FilterActivity.this,line,Toast.LENGTH_LONG).show();
                Log.d("onScroll ", line);
            }
            if (motionEvent.getX() > motionEvent1.getX()) {
                line = "GESTURE: Right to Left Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
                Toast.makeText(FilterActivity.this,line,Toast.LENGTH_LONG).show();
                Log.d("onScroll ", line);
            }
            if (motionEvent.getY() < motionEvent1.getY()) {
                line = "GESTURE: Up to Down Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
                Toast.makeText(FilterActivity.this,line,Toast.LENGTH_LONG).show();
                Log.d("onScroll ", line);
            }
            if (motionEvent.getY() > motionEvent1.getY()) {
                line = "GESTURE: Down to Up Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX() + "\nSPEED: " + String.valueOf(v) + " pixels/second";
                Toast.makeText(FilterActivity.this,line,Toast.LENGTH_LONG).show();
                Log.d("onScroll ", line);
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            gestureText.setText("onLongPress");
//            count++;
            updateNumTouches();
            Log.d("Gesture ", "onLongPress");

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            gestureText.setText("onFling");
            count++;
            updateNumTouches();
            if (motionEvent.getX() < motionEvent1.getX()) {
                Log.d("Gesture ", "Left to Right Fling: " + motionEvent.getX() + " - " + motionEvent1.getX());
                Log.d("Speed ", String.valueOf(v) + " pixels/second");
            }
            if (motionEvent.getX() > motionEvent1.getX()) {
                Log.d("Gesture ", "Right to Left Fling: " + motionEvent.getX() + " - " + motionEvent1.getX());
                Log.d("Speed ", String.valueOf(v) + " pixels/second");
            }
            if (motionEvent.getY() < motionEvent1.getY()) {
                Log.d("Gesture ", "Up to Down Fling: " + motionEvent.getY() + " - " + motionEvent1.getY());
                Log.d("Speed ", String.valueOf(v1) + " pixels/second");
            }
            if (motionEvent.getY() > motionEvent1.getY()) {
                Log.d("Gesture ", "Down to Up Fling: " + motionEvent.getY() + " - " + motionEvent1.getY());
                Log.d("Speed ", String.valueOf(v1) + " pixels/second");
            }
            return false;
        }
    }

    public void updateNumTouches(){
        TextView fs = findViewById(R.id.fling_stats);
        String line = "";
        line += "Count: " + count;
        fs.setText(line);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        count++;
        if(selectedImageBitmap != null) {
            myGestureDectector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}