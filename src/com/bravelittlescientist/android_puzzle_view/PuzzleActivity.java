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
        setContentView(puzzleSurface);

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

    }

    @Override
    protected void onPause() {
        super.onPause();
        puzzleSurface.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        puzzleSurface.resume();
    }
}
