package com.bravelittlescientist.android_puzzle_view;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class PuzzleCompactSurface extends SurfaceView implements Runnable {

    /** Surface Components **/
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = false;
    private int found = -1;

    /** Puzzle and Canvas **/
    private int MAX_PUZZLE_PIECE_SIZE = 100;
    private JigsawPuzzle puzzle;
    private BitmapDrawable[] scaledSurfacePuzzlePieces;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PuzzleCompactSurface(Context context) {
        super(context);
        surfaceHolder = getHolder();
        setFocusable(true);
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
            scaledSurfacePuzzlePieces[i].getPaint().setColor(Color.BLACK);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xPos =(int) event.getX();
        int yPos =(int) event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < scaledSurfacePuzzlePieces.length; i++) {
                    Rect place = scaledSurfacePuzzlePieces[i].getBounds();

                    if (xPos >= place.left &&
                            xPos <= place.right &&
                            yPos <= place.bottom &&
                            yPos >= place.top) {
                        found = i;
                    }
                }
                break;


            case MotionEvent.ACTION_MOVE:
                if (found >= 0 && found < scaledSurfacePuzzlePieces.length) {
                    Rect rect = scaledSurfacePuzzlePieces[found].copyBounds();
                    rect.left = xPos - MAX_PUZZLE_PIECE_SIZE/2;
                    rect.top = yPos - MAX_PUZZLE_PIECE_SIZE/2;
                    rect.right = xPos + MAX_PUZZLE_PIECE_SIZE/2;
                    rect.bottom = yPos + MAX_PUZZLE_PIECE_SIZE/2;
                    scaledSurfacePuzzlePieces[found].setBounds(rect);
                }
                break;

            case MotionEvent.ACTION_UP:
                found = -1;
                break;

        }


        return true;
    }
}
