package com.bravelittlescientist.android_puzzle_view;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class PuzzleCompactSurface extends SurfaceView implements SurfaceHolder.Callback {

    public class PuzzleThread extends Thread {

        private SurfaceHolder surfaceHolder;
        private PuzzleCompactSurface surface;
        private boolean running = false;

        public PuzzleThread(SurfaceHolder holder, PuzzleCompactSurface puzzleSurface) {

            surfaceHolder = getHolder();
            surface = puzzleSurface;
        }

        public void setRunning(boolean run) {
            running = run;
        }

        @Override
        public void run() {
            super.run();

            Canvas canvas;
            while (running) {
                canvas=null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        surface.onDraw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    /** Surface Components **/
    private PuzzleThread gameThread;
    private volatile boolean running = false;
    private int found = -1;

    /** Puzzle and Canvas **/
    private int MAX_PUZZLE_PIECE_SIZE = 100;
    private int LOCK_ZONE_LEFT = 200;
    private int LOCK_ZONE_TOP = 100;

    private JigsawPuzzle puzzle;
    private BitmapDrawable[] scaledSurfacePuzzlePieces;
    private Rect[] scaledSurfaceTargetBounds;

    public PuzzleCompactSurface(Context context) {
        super(context);

        gameThread = new PuzzleThread(getHolder(), this);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    /*public void resume () {
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
    }*/

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) { }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.setRunning(false);
        gameThread.stop();
    }


    public void setPuzzle(JigsawPuzzle jigsawPuzzle) {
        puzzle = jigsawPuzzle;

        /** Initialize drawables from puzzle pieces **/
        Bitmap[] originalPieces = puzzle.getPuzzlePiecesArray();
        int[][] positions = puzzle.getPuzzlePieceTargetPositions();
        int[] dimensions = puzzle.getPuzzleDimensions();

        scaledSurfacePuzzlePieces = new BitmapDrawable[originalPieces.length];
        scaledSurfaceTargetBounds = new Rect[originalPieces.length];

        for (int i = 0; i < originalPieces.length; i++) {

            scaledSurfacePuzzlePieces[i] = new BitmapDrawable(originalPieces[i]);
            scaledSurfacePuzzlePieces[i].setBounds(i*MAX_PUZZLE_PIECE_SIZE, 0,
                    i*MAX_PUZZLE_PIECE_SIZE + MAX_PUZZLE_PIECE_SIZE, MAX_PUZZLE_PIECE_SIZE);
        }

        for (int w = 0; w < dimensions[2]; w++) {
            for (int h = 0; h < dimensions[3]; h++) {
                int targetPiece = positions[w][h];

                scaledSurfaceTargetBounds[targetPiece] = new Rect(
                        LOCK_ZONE_LEFT + w*MAX_PUZZLE_PIECE_SIZE,
                        LOCK_ZONE_TOP + h*MAX_PUZZLE_PIECE_SIZE,
                        LOCK_ZONE_LEFT + w*MAX_PUZZLE_PIECE_SIZE + MAX_PUZZLE_PIECE_SIZE,
                        LOCK_ZONE_TOP + h*MAX_PUZZLE_PIECE_SIZE + MAX_PUZZLE_PIECE_SIZE);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);
        for (int bmd = 0; bmd < scaledSurfacePuzzlePieces.length; bmd++) {
            scaledSurfacePuzzlePieces[bmd].draw(canvas);
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
                if (found >= 0 && found < scaledSurfacePuzzlePieces.length && !puzzle.isPieceLocked(found)) {
                    // Lock into position...
                    if (scaledSurfaceTargetBounds[found].contains(xPos, yPos) ) {
                        scaledSurfacePuzzlePieces[found].setBounds(scaledSurfaceTargetBounds[found]);
                        puzzle.setPieceLocked(found, true);
                    } else {
                        Rect rect = scaledSurfacePuzzlePieces[found].copyBounds();

                        rect.left = xPos - MAX_PUZZLE_PIECE_SIZE/2;
                        rect.top = yPos - MAX_PUZZLE_PIECE_SIZE/2;
                        rect.right = xPos + MAX_PUZZLE_PIECE_SIZE/2;
                        rect.bottom = yPos + MAX_PUZZLE_PIECE_SIZE/2;
                        scaledSurfacePuzzlePieces[found].setBounds(rect);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                found = -1;
                break;

        }


        return true;
    }
}
