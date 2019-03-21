package com.example.abrig.gesture_app_5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView gestureText;
    private GestureDetector myGestureDectector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureText = (TextView) findViewById(R.id.tvGesture);
        AndroidGestureDectector androidGestureDectector = new AndroidGestureDectector();
        myGestureDectector = new GestureDetector(MainActivity.this, androidGestureDectector);

    }

    public void startSecondIntent(View view) {
        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
        startActivityForResult(intent,1);
    }

    public void startThirdIntent(View view) {
        Intent intent = new Intent(MainActivity.this,Main3Activity.class);
        startActivityForResult(intent,1);
    }

    public void startFourthIntent(View view) {
        Intent intent = new Intent(MainActivity.this,Main4Activity.class);
        startActivityForResult(intent,1);
    }

    class AndroidGestureDectector implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            gestureText.setText("onSingleTapConfirmed");
            Log.d("Gesture ", "onSingleTapConfirmed");
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            gestureText.setText("onDoubleTap");
            Log.d("Gesture ", "onDoubleTap");
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            gestureText.setText("onDoubleTapEvent");
            Log.d("Gesture ", "onDoubleTapEvent");
            return false;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            gestureText.setText("onDown");
            Log.d("Gesture ", "onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            gestureText.setText("onShowPress");
            Log.d("Gesture ", "onShowPress");

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            gestureText.setText("onSingleTapUp");
            Log.d("Gesture ", "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            gestureText.setText("onScroll");
            if (motionEvent.getX() < motionEvent1.getX()) {
                Log.d("Gesture ", "Left to Right Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX());
                Log.d("Speed ", String.valueOf(v) + " pixels/second");
            }
            if (motionEvent.getX() > motionEvent1.getX()) {
                Log.d("Gesture ", "Right to Left Scroll: " + motionEvent.getX() + " - " + motionEvent1.getX());
                Log.d("Speed ", String.valueOf(v) + " pixels/second");
            }
            if (motionEvent.getY() < motionEvent1.getY()) {
                Log.d("Gesture ", "Up to Down Scroll: " + motionEvent.getY() + " - " + motionEvent1.getY());
                Log.d("Speed ", String.valueOf(v1) + " pixels/second");
            }
            if (motionEvent.getY() > motionEvent1.getY()) {
                Log.d("Gesture ", "Down to Up Scroll: " + motionEvent.getY() + " - " + motionEvent1.getY());
                Log.d("Speed ", String.valueOf(v1) + " pixels/second");
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            gestureText.setText("onLongPress");
            Log.d("Gesture ", "onLongPress");

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            gestureText.setText("onFling");
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        myGestureDectector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
