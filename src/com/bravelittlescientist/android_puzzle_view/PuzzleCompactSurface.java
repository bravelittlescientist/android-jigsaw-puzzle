package com.bravelittlescientist.android_puzzle_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PuzzleCompactSurface extends SurfaceView implements Runnable {

    /** Surface Components **/
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = false;

    /** Puzzle and Canvas **/
    private JigsawPuzzle puzzle;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    BitmapDrawable sample;

    public PuzzleCompactSurface(Context context) {
        super(context);
        surfaceHolder = getHolder();
    }

    public void resume () {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause () {
        boolean retry = true;
        running = false;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException iE) {
                iE.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            if (surfaceHolder.getSurface().isValid()) {
                Canvas c = surfaceHolder.lockCanvas();

                sample.draw(c);

                surfaceHolder.unlockCanvasAndPost(c);
            }
        }
    }

    public void setPuzzle(JigsawPuzzle jigsawPuzzle) {
        puzzle = jigsawPuzzle;
        Bitmap[] m = puzzle.getPuzzlePiecesArray();
        sample = new BitmapDrawable(m[0]);
        sample.setBounds(0, 0, m[0].getWidth(), m[0].getHeight());
    }
}
