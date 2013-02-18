package com.bravelittlescientist.android_puzzle_view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PuzzleActivity extends Activity {

    private PuzzleView puzzleView;

    private static final String TAG = "PuzzleActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

        ImageView i = new ImageView(this);
        i.setImageResource(R.drawable.kitten_large);

        rL.addView(i, lP);
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
