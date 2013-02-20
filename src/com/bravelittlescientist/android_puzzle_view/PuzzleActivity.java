package com.bravelittlescientist.android_puzzle_view;

import android.app.Activity;
import android.os.Bundle;

public class PuzzleActivity extends Activity {

    private PuzzleCompactSurface puzzleSurface;

    private static final String TAG = "PuzzleActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        puzzleSurface = new PuzzleCompactSurface(this);
        JigsawPuzzle jigsawPuzzle = new JigsawPuzzle(getResources(), R.drawable.kitten_large);
        puzzleSurface.setPuzzle(jigsawPuzzle);

        setContentView(puzzleSurface);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
