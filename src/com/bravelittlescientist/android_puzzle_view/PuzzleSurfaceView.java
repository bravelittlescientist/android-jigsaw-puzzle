package com.bravelittlescientist.android_puzzle_view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class PuzzleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Context puzzleContext;
    private PuzzleGameThread gameThread;

    private static final String TAG = "PuzzleSurfaceView";

    public PuzzleSurfaceView (Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // create thread only; it's started in surfaceCreated()
        gameThread = new PuzzleGameThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {

                //statusText.setVisibility(m.getData().getInt("visibility"));
                //statusText.setText(m.getData().getString("text"));
            }
        });

        setFocusable(true);
    }

    public PuzzleGameThread getGameThread () {
        return gameThread;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) gameThread.pause();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        gameThread.setSurfaceSize(width, height);
    }

    public void setStatus (TextView textView) {
        //statusText = textView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.startPuzzle();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {

            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    /*
    TODO OnTouchEvent
    @Override

    public boolean onTouchEvent (MotionEvent event) {

         if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() > getHeight() - 50) {
                gameThread.setRunning(false);
                ((Activity)getContext()).finish();
            } else {
                Log.d(TAG, "Coordinates: x=" + event.getX() + ",y=" + event.    getY());
            }


        return super.onTouchEvent(event);
    }*/
}
