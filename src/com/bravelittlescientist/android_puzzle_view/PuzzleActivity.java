package com.bravelittlescientist.android_puzzle_view;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import java.math.BigInteger;

public class PuzzleActivity extends Activity {

    private PuzzleView puzzleView;

    private static final String TAG = "PuzzleActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.puzzle_layout);

        // Initialize puzzle view and create new puzzle game
        puzzleView = (PuzzleView) findViewById(R.id.puzzle_base);

        // Prepare Puzzle View
        Bitmap puzzle = loadPuzzleResources();
        puzzleView.setPuzzleResource(puzzle);

        /** The following is temporary display code **/
        Bitmap[] p = puzzleView.getPuzzlePiecesArray();
        LinearLayout l = (LinearLayout) findViewById(R.id.puzzle_overlay_layout);

        for (int i = 0; i < p.length; i++) {
            ImageView iV = new ImageView(this);
            BitmapDrawable bMP = new BitmapDrawable(p[i]);
            iV.setImageDrawable(bMP);
            l.addView(iV);
        }

        /** End temporary display code **/

    }

    /**
     * showPuzzleSource
     */
    public Bitmap loadPuzzleResources () {

        long targetWidth =  Math.round(0.75 * getWindowManager().getDefaultDisplay().getWidth());
        long targetHeight = Math.round(0.75 * getWindowManager().getDefaultDisplay().getHeight());

        Bitmap decodedPuzzleResource = decodePuzzleBitmapFromResource(
                getResources(), R.drawable.kitten_large, targetWidth, targetHeight);

        return decodedPuzzleResource;
    }

    /**
     * loadPuzzlePieces
     *
     * Bitmap Loading Code from Android Developer lesson: "Loading Large Bitmaps Efficiently"
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

    @Override
    protected void onPause() {
        super.onPause();

        puzzleView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        puzzleView.resume();
    }
}
