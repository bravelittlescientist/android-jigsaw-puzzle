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

    private Bitmap[] puzzlePiecesArray;
    private int[][] puzzlePieceTargetPositions;

    private static final String TAG = "PuzzleView";

    public PuzzleView (Context context, AttributeSet attrs) {
        super(context, attrs);


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
