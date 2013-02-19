package com.bravelittlescientist.android_puzzle_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class PuzzleView extends View {

    protected static int puzzleXDimension;
    protected static int puzzleYDimension;

    protected static int puzzlePieceHeight;
    protected static int puzzlePieceWidth;

    protected static int puzzleGridX;
    protected static int puzzleGridY;

    private Bitmap puzzleResult;
    private Bitmap[] puzzlePiecesArray;
    private int[][] puzzlePieceTargetPositions;

    private static final String TAG = "PuzzleView";

    public PuzzleView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Puzzle Mechanics **/

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
     * initializePuzzleGrid
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

    /** State and Execution **/
    public void run() {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void clear() {

    }

    public void addPuzzlePiece () {

    }

    /** Drawing and Animation **/
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
