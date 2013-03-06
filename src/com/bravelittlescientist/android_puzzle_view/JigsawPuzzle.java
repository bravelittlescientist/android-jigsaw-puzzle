package com.bravelittlescientist.android_puzzle_view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

public class JigsawPuzzle {

    protected static int puzzleXDimension;
    protected static int puzzleYDimension;

    protected static int puzzlePieceHeight;
    protected static int puzzlePieceWidth;

    protected static int puzzleGridX;
    protected static int puzzleGridY;

    // Todo
    private Bundle config;

    private Bitmap puzzleResult;
    private Bitmap[] puzzlePiecesArray;
    private int[][] puzzlePieceTargetPositions;
    private boolean[] pieceLocked;

    private Context mContext;

    /**
     * JigsawPuzzle constructor: Dynamic Configuration
     * @param res
     * @param resourceId
     *
     * This jigsaw puzzle will be configured dynamically by partitioning
     * the provided source image.
     */
    public JigsawPuzzle(Resources res, Integer resourceId) {
        loadPuzzleResources(res, resourceId, 450, 300);
        buildDynamicPuzzleGrid();
    }

    /**
     * JigsawPuzzle constructor: Bundle configuration
     * @param context
     * @param configuration
     *
     * A bundle containing bundles of puzzle information as follows:
     *  Key String  | Value(s)
     *  -------------------------------
     *  "pieces"        A Bundle of Bundles containing puzzle piece metadata
     *
     *  "grid"          A Bundle containing grid and dimensions
     *
     *  "image"         Bundle containing an image url, base width, and base height
     *
     */
    public JigsawPuzzle(Context context, Bundle configuration) {
        config = configuration;
        mContext = context;

        loadPuzzleResources(mContext.getResources(),
                config.getBundle("img").getInt("img_local"), 400, 300);
        loadPuzzleConfiguration();
    }

    public void loadPuzzleResources (Resources res, int resourceId, long targetWidth, long targetHeight) {

        Bitmap decodedPuzzleResource = decodePuzzleBitmapFromResource(
                res, resourceId, targetWidth, targetHeight);

        puzzleResult = decodedPuzzleResource;
        puzzleXDimension = decodedPuzzleResource.getWidth();
        puzzleYDimension = decodedPuzzleResource.getHeight();
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
        pieceLocked = new boolean[puzzleGridX * puzzleGridY];

        // Generate array of bitmaps
        int counter = 0;
        for (int w = 0; w < puzzleGridX; w++) {
            for (int h = 0; h < puzzleGridY; h++) {
                puzzlePiecesArray[counter] = Bitmap.createBitmap(puzzleResult,
                        w*puzzlePieceWidth, h*puzzlePieceHeight, puzzlePieceWidth, puzzlePieceHeight);

                pieceLocked[counter] = false;

                puzzlePieceTargetPositions[w][h] = counter;


                counter++;
            }
        }
    }

    /**
     * greatestCommonDivisor
     * @param n1
     * @param n2
     * @return gcd of n1, n2
     *
     * Utility class for computing optimal puzzle bitmap scaling.
     */
    public int greatestCommonDivisor (int n1, int n2) {
        if (n2 == 0) return n1;
        return greatestCommonDivisor(n2, n1 % n2);
    }

    public void loadPuzzleConfiguration() {
        Bundle grid = config.getBundle("grid");
        Bundle image = config.getBundle("img");
        Bundle pieces = config.getBundle("pieces");

        // Puzzle Grid
        puzzlePieceHeight = grid.getInt("cellh");
        puzzlePieceWidth = grid.getInt("cellw");
        puzzleGridX = puzzleXDimension / puzzlePieceWidth;
        puzzleGridY = puzzleYDimension / puzzlePieceHeight;

        // Fill Puzzle
        puzzlePieceTargetPositions = new int[puzzleGridX][puzzleGridY];
        puzzlePiecesArray = new Bitmap[puzzleGridX * puzzleGridY];
        pieceLocked = new boolean[puzzleGridX * puzzleGridY];

        int counter = 0;
        for (int w = 0; w < puzzleGridX; w++) {
            for (int h = 0; h < puzzleGridY; h++) {
                puzzlePiecesArray[counter] = Bitmap.createBitmap(puzzleResult, w*puzzlePieceWidth, h*puzzlePieceHeight,
                        puzzlePieceWidth, puzzlePieceHeight);

                pieceLocked[counter] = false;

                puzzlePieceTargetPositions[w][h] = counter;

                counter++;
            }
        }

    }

    /** Getters and Setters **/

    public Bitmap[] getPuzzlePiecesArray () {
        return puzzlePiecesArray;
    }

    public int[] getPuzzleDimensions () {
        return new int[] { puzzleXDimension, puzzleYDimension, puzzleGridX, puzzleGridY };
    }

    public int[][] getPuzzlePieceTargetPositions () {
        return puzzlePieceTargetPositions;
    }

    public void setPieceLocked (int piece, boolean locked) {
        if (piece >= 0 && piece < pieceLocked.length) {
            pieceLocked[piece] = locked;
        }
    }

    public boolean isPieceLocked(int piece) {
        if (piece >= 0 && piece < pieceLocked.length) {
            return pieceLocked[piece];
        } else {
            return false;

        }
    }
}
