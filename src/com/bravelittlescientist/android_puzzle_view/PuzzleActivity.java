package com.bravelittlescientist.android_puzzle_view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class PuzzleActivity extends Activity {

    private PuzzleSurfaceView puzzleSurfaceView;
    private PuzzleGameThread puzzleThread;

    private static final String TAG = "PuzzleActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.puzzle_layout);

        puzzleSurfaceView = (PuzzleSurfaceView) findViewById(R.id.puzzle_surface);
        puzzleThread = puzzleSurfaceView.getGameThread();

        puzzleSurfaceView.setTextView((TextView) findViewById(R.id.text));

        if (savedInstanceState == null) {
            // On new Puzzle
            puzzleThread.setState(PuzzleGameThread.STATE_READY);
            Log.w(this.getClass().getName(), "SIS is null");
        } else {
            // Restoring previous puzzle
            puzzleThread.restorePuzzleState(savedInstanceState);
            Log.w(this.getClass().getName(), "SIS is not null");
        }

        /** The following is temporary display code **/
        /*Bitmap[] p = puzzleThread.getPuzzlePiecesArray();
        int[] pDimensions = puzzleThread.getPuzzleDimensions();

        GridLayout gL = (GridLayout) findViewById(R.id.puzzle_overlay_layout);
        gL.setColumnCount(pDimensions[3]);
        gL.setRowCount(pDimensions[2]);

        for (int i = 0; i < p.length; i++) {
            ImageView iV = new ImageView(this);
            BitmapDrawable bMP = new BitmapDrawable(p[i]);
            iV.setImageDrawable(bMP);
            gL.addView(iV);
        } */

        /** End temporary display code **/
        puzzleThread.startPuzzle();

    }

    @Override
    protected void onPause() {
        super.onPause();
        puzzleSurfaceView.getGameThread().pause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        puzzleThread.savePuzzleState(outState);
        Log.w(this.getClass().getName(), "SIS called");
    }
}
