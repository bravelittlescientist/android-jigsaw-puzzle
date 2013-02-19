package com.bravelittlescientist.android_puzzle_view;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
        puzzleView.run();

        // Prepare Puzzle Image
        showPuzzleSource();
    }

    /**
     * showPuzzleSource
     */
    public void showPuzzleSource () {

        RelativeLayout rL = (RelativeLayout) findViewById(R.id.puzzle_overlay_layout);
        RelativeLayout.LayoutParams lP = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        Bitmap decodedPuzzleResource = decodePuzzleBitmapFromResource(
                getResources(), R.drawable.kitten_large, 100, 100);

        ImageView i = new ImageView(this);
        i.setImageBitmap(decodedPuzzleResource);

        rL.addView(i, lP);
        Toast.makeText(this, decodedPuzzleResource.getWidth() + " " + decodedPuzzleResource.getHeight(),
                Toast.LENGTH_LONG).show();
    }

    /**
     * loadPuzzlePieces
     *
     * Bitmap Loading Code from Android Developer lesson: "Loading Large Bitmaps Efficiently"
     */
    public static Bitmap decodePuzzleBitmapFromResource (
            Resources res, int resId, int targetWidth, int targetHeight) {

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
            BitmapFactory.Options options, int targetWidth, int targetHeight) {

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
