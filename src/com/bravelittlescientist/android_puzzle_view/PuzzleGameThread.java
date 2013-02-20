package com.bravelittlescientist.android_puzzle_view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;

public class PuzzleGameThread extends Thread {

    // Thread and Surface Management
    private Handler messageHandler;
    private SurfaceHolder holder;
    private Context puzzleContext;
    private boolean running = false;

    // TODO Puzzle Dimensions

    // Game state
    private int puzzleState;
    public static final int STATE_READY = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_RUNNING = 3;
    public static final int STATE_COMPLETE = 4;

    private int puzzleCanvasWidth = 1;
    private int puzzleCanvasHeight = 1;

    // Drawing Variables
    private Rect puzzleFrame;
    private Paint puzzleFrameLinePaint;

    public PuzzleGameThread (SurfaceHolder surfaceHolder, Context context,
                             Handler handler) {

        // Handle puzzle surface objects
        holder = surfaceHolder;
        messageHandler = handler;
        puzzleContext = context;

        // Initialize puzzle here
        //Resources res = context.getResources();

        // Initialize puzzle holder
        puzzleFrame = new Rect(0, 0, 0, 0);
        puzzleFrameLinePaint = new Paint();
        puzzleFrameLinePaint.setAntiAlias(true);
        puzzleFrameLinePaint.setARGB(255, 120, 180, 0);
    }

    public void startPuzzle () {
        synchronized (holder) {
            // TODO: Set Puzzle initialization parameters here, if needed

            setState(STATE_RUNNING);
        }
    }

    public void pause() {
        synchronized (holder) {
            if (puzzleState == STATE_RUNNING) setState(STATE_PAUSE);
        }
    }

    public void unpause() {
        setState(STATE_RUNNING);
    }

    public synchronized void restorePuzzleState (Bundle savedState) {
        synchronized (holder) {
            setState(STATE_PAUSE);

            // TODO Restore Puzzle settings from Bundle
            // savedState.getInt(KEY)
        }
    }

    public Bundle savePuzzleState(Bundle map) {
        synchronized (holder) {
            if (map != null) {
                // TODO Add puzzle state information to Map
                // map.putInt(KEY, VALUE);
            }
        }
        return map;
    }

    @Override
    public void run() {
        while (running) {
            Canvas c = null;
            try {
                c = holder.lockCanvas(null);
                synchronized (holder) {
                    if (puzzleState == STATE_RUNNING) updatePuzzleFrame();
                    drawPuzzleCanvas(c);
                }
            } finally {
                if (c != null) {
                    holder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void setRunning(boolean r) {
        running = r;
    }

    public void setState(int mode) {
        synchronized (holder) {
            puzzleState = mode;
        }
    }

    public void setSurfaceSize(int width, int height) {
        synchronized (holder) {
            puzzleCanvasWidth = width;
            puzzleCanvasHeight = height;

            // TODO Scale puzzle background if one exists
            // Bitmap Background = Bitmap.createScaledBitmap(currentBackgroundImage, width, height, true);
        }
    }

    private void drawPuzzleCanvas(Canvas canvas) {
        // Draw background image if it exists
        // canvas.drawBitmap(backgroundImage, 0, 0, null);

        // Draw Puzzle result frame
        puzzleFrame.set(20, 20, 40, 60);
        canvas.drawRect(puzzleFrame, puzzleFrameLinePaint);

        // TODO Draw puzzle pieces
        // BitmapDrawable bm
        // bm.draw(canvas)
    }

    private void updatePuzzleFrame () {
       // TODO Check if puzzle is complete...
    }

}