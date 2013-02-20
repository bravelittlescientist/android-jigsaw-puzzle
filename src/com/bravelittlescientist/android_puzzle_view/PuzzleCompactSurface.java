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
    private int MAX_PUZZLE_PIECE_SIZE = 100;
    private JigsawPuzzle puzzle;
    private BitmapDrawable[] scaledSurfacePuzzlePieces;
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

                for (int bmd = 0; bmd < scaledSurfacePuzzlePieces.length; bmd++) {
                    scaledSurfacePuzzlePieces[bmd].draw(c);
                }

                surfaceHolder.unlockCanvasAndPost(c);
            }
        }
    }

    public void setPuzzle(JigsawPuzzle jigsawPuzzle) {
        puzzle = jigsawPuzzle;

        /** Initialize drawables from puzzle pieces **/
        Bitmap[] originalPieces = puzzle.getPuzzlePiecesArray();
        scaledSurfacePuzzlePieces = new BitmapDrawable[originalPieces.length];
        for (int i = 0; i < originalPieces.length; i++) {
            scaledSurfacePuzzlePieces[i] = new BitmapDrawable(originalPieces[i]);
            scaledSurfacePuzzlePieces[i].setBounds(i*MAX_PUZZLE_PIECE_SIZE, 0,
                    i*MAX_PUZZLE_PIECE_SIZE + MAX_PUZZLE_PIECE_SIZE, MAX_PUZZLE_PIECE_SIZE);
        }

    }
}
