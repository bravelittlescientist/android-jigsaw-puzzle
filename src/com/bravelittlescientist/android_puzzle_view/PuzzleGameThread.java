package com.bravelittlescientist.android_puzzle_view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class PuzzleGameThread extends Thread {

    // Thread and Surface Management
    private Handler messageHandler;
    private SurfaceHolder holder;
    private Context puzzleContext;
    private boolean running = false;

    // Puzzle Dimensions
    protected static int puzzleXDimension;
    protected static int puzzleYDimension;

    protected static int puzzlePieceHeight;
    protected static int puzzlePieceWidth;

    protected static int puzzleGridX;
    protected static int puzzleGridY;

    private Bitmap puzzleResult;
    private Bitmap[] puzzlePiecesArray;
    private int[][] puzzlePieceTargetPositions;

    // Game state
    private int puzzleState;
    public static final int STATE_READY = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_RUNNING = 3;
    public static final int STATE_COMPLETE = 4;

    private int puzzleCanvasWidth = 100;
    private int puzzleCanvasHeight = 100;

    // Drawing Variables
    private Rect puzzleFrame;
    private Paint puzzleFrameLinePaint;

    public PuzzleGameThread (SurfaceHolder surfaceHolder, Context context,
                             Handler handler) {

        // Handle puzzle surface objects
        holder = surfaceHolder;
        messageHandler = handler;
        puzzleContext = context;

        // Initialize puzzle holder
        puzzleFrame = new Rect(0, 0, 0, 0);
        puzzleFrameLinePaint = new Paint();
        puzzleFrameLinePaint.setAntiAlias(true);
        puzzleFrameLinePaint.setARGB(120, 120, 180, 0);

        loadPuzzleResources();
    }

    public void startPuzzle () {
        synchronized (holder) {
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
                Toast.makeText(puzzleContext, "Made it somewhere", Toast.LENGTH_SHORT).show();
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

            // TIP: This would be a good place to set the background image.
        }
    }

    private void drawPuzzleCanvas(Canvas canvas) {
        //BitmapDrawable bmd = new BitmapDrawable(puzzleResult);
        //bmd.draw(canvas);

        // Draw background image if it exists
        // canvas.drawBitmap(backgroundImage, 0, 0, null);

        // Draw Puzzle result frame
        //puzzleFrame.set(20, 200, 400, 200);
        //canvas.drawRect(puzzleFrame, puzzleFrameLinePaint);

    }

    private void updatePuzzleFrame () {
       // TODO Check if puzzle is complete...
    }

    /** Managing Puzzle Resources **/

    public void loadPuzzleResources () {

        long targetWidth =  450; // TODO hardcoded for testing
        long targetHeight = 300; // TODO hardcoded for testing
        Bitmap decodedPuzzleResource = decodePuzzleBitmapFromResource(
                puzzleContext.getResources(), R.drawable.kitten_large, targetWidth, targetHeight);

        setPuzzleResource(decodedPuzzleResource);
    }

    /**
     * decodePuzzleBitmapFromResource
     * @param res
     * @param resId
     * @param targetWidth
     * @param targetHeight
     * @return Bitmap
     *
     *  Bitmap Loading Code from Android Developer lesson: "Loading Large Bitmaps Efficiently"
     */
    public static Bitmap decodePuzzleBitmapFromResource (
            Resources res, int resId, long targetWidth, long targetHeight) {

        // Load only the dimensions of the puzzle image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate ratio to scale puzzle bitmap
        options.inSampleSize = calculateScaledPuzzleSize(options, targetWidth, targetHeight);

        // Decode puzzle resource image to bitmap from computed ratio
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * calculateScaledPuzzleSize
     *
     * Adapted from Android Developer lesson: "Loading Large Bitmaps Efficiently"
     */
    public static int calculateScaledPuzzleSize (
            BitmapFactory.Options options, long targetWidth, long targetHeight) {

        // Source Image Dimensions
        final int height = options.outHeight;
        final int width = options.outWidth;
        int imageScaleRatio = 1;

        if (height > targetHeight || width > targetWidth) {
            // Calculate ratios of height and width to target height and width
            final int heightRatio = Math.round((float) height / (float) targetHeight);
            final int widthRatio = Math.round((float) width / (float) targetWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            imageScaleRatio = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return imageScaleRatio;
    }

    /**
     * setPuzzleResource
     * @param bm    Memory-scaled bitmap representing puzzle target.
     *
     * TODO: Make sure input image is valid.
     */
    public void setPuzzleResource(Bitmap bm) {
        puzzleResult = bm;
        puzzleXDimension = bm.getWidth();
        puzzleYDimension = bm.getHeight();

        // TODO: This should only happen here if user did not initialize PuzzleView with a grid
        buildDynamicPuzzleGrid();
    }

    /**
     * buildDynamicPuzzleGrid
     *
     * If not already set, computes optimal puzzle piece size using Greatest Common Divisor.
     * Computes Bitmaps for each piece and sets their target positions.
     *
     * TODO: Handle case where GCD is 1, or if piece should be a rectangle?
     */
    public void buildDynamicPuzzleGrid() {
        // Compute optimal piece size:
        int optimalPieceSize = greatestCommonDivisor(puzzleXDimension, puzzleYDimension);

        // Update puzzle dimension variables
        puzzlePieceHeight = optimalPieceSize;
        puzzlePieceWidth = optimalPieceSize;
        puzzleGridX = puzzleXDimension / puzzlePieceWidth;
        puzzleGridY = puzzleYDimension / puzzlePieceHeight;

        // Initialize and fill puzzle
        puzzlePieceTargetPositions = new int[puzzleGridX][puzzleGridY];
        puzzlePiecesArray = new Bitmap[puzzleGridX * puzzleGridY];

        // Generate array of bitmaps
        int counter = 0;
        for (int w = 0; w < puzzleGridX; w++) {
            for (int h = 0; h < puzzleGridY; h++) {
                puzzlePiecesArray[counter] = Bitmap.createBitmap(puzzleResult,
                        w*puzzlePieceWidth, h*puzzlePieceHeight, puzzlePieceWidth, puzzlePieceHeight);

                puzzlePieceTargetPositions[w][h] = counter;

                counter++;
            }
        }
    }

    public int greatestCommonDivisor (int n1, int n2) {
        if (n2 == 0) return n1;
        return greatestCommonDivisor(n2, n1 % n2);
    }

    public Bitmap[] getPuzzlePiecesArray () {
        return puzzlePiecesArray;
    }

    public int[] getPuzzleDimensions () {
        return new int[] { puzzleXDimension, puzzleYDimension, puzzleGridX, puzzleGridY };
    }

    public int[][] getPuzzlePieceTargetPositions () {
        return puzzlePieceTargetPositions;
    }

}