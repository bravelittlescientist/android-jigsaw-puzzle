package com.bravelittlescientist.android_puzzle_view;

import android.os.Bundle;

public final class ExampleJigsawConfigurations {

    public static Bundle getKittenExample() {
        // Initialization
        Bundle config = new Bundle();

        Bundle pieces = new Bundle();
        Bundle grid = new Bundle();
        Bundle image = new Bundle();
        Bundle board = new Bundle();

        // Image
        image.putBoolean("is_local", true);
        image.putInt("img_id", R.drawable.kitten_large);
        image.putString("img_url", null);
        image.putInt("img_w", 1024);
        image.putInt("img_h", 681);

        // Grid
        grid.putInt("nrows", 2);
        grid.putInt("ncols", 3);
        grid.putInt("cellw", 256);
        grid.putInt("cellh", 256);

        // Pieces
        Bundle p;
        String key;
        for (int h = 0; h < 2; h++) {
            for (int w = 0; w < 3; w++) {
                key = "k" + String.valueOf(w) + String.valueOf(h);
                p = new Bundle();
                p.putInt("l", -1);
                p.putString("pid", key);
                p.putBoolean("b", false);
                p.putInt("x", w*(1024/3));
                p.putInt("y", h*(681/2));
                p.putInt("r", h);
                p.putInt("c", w);
                pieces.putBundle(key, p);
            }
        }

        // Add subbundles to config bundle
        config.putBundle("pieces", pieces);
        config.putBundle("grid", grid);
        config.putBundle("image", image);

        return config;
    }

    /**
     * For documentation supporting this example, see:
     * https://github.com/gentimouton/rcat/wiki/Jigsaw-Puzzle
     * @return Bundle configuration
     */
    public static Bundle getRcatKittenExample() {
        Bundle config = new Bundle();

        Bundle board = new Bundle();
        Bundle grid = new Bundle();
        Bundle frus = new Bundle();
        Bundle pieces = new Bundle();
        Bundle img = new Bundle();
        Bundle scores = new Bundle();

        board.putInt("w", 800);
        board.putInt("h", 600);
        board.putInt("minScale", 1);
        board.putInt("maxScale", 10);

        grid.putInt("x", 200);
        grid.putInt("y", 150);
        grid.putInt("ncols", 4);
        grid.putInt("nrows", 3);
        grid.putInt("cellw", 100);
        grid.putInt("cellh", 100);

        frus.putInt("x", 0);
        frus.putInt("y", 0);
        frus.putInt("scale", 1);
        frus.putString("w", null);
        frus.putString("h", null);

        scores.putStringArray("top", new String[]{"Top1", "Top2", "Top3"});
        scores.putInt("numTop", 20);
        Bundle[] connected = new Bundle[2];
        connected[0] = new Bundle();
        connected[1] = new Bundle();
        connected[0].putString("user","Arthur");
        connected[0].putString("uid", "player5678-uid");
        connected[0].putInt("score", 47);
        connected[1].putString("user", "Thomas");
        connected[1].putString("uid", "player9012-uid");
        connected[1].putInt("score", 39);
        scores.putParcelableArray("connected", connected);

        img.putString("img_url", "http://ics.uci.edu/~tdebeauv/rCAT/diablo_1MB.jpg");
        img.putInt("img_local", R.drawable.happy_kitten);
        img.putInt("img_w", 1600);
        img.putInt("img_h", 1200);

        // Pieces
        Bundle p;
        String key;
        for (int h = 0; h < grid.getInt("nrows"); h++) {
            for (int w = 0; w < grid.getInt("ncols"); w++) {
                key = "piece_" + String.valueOf(w) + String.valueOf(h);
                p = new Bundle();
                p.putString("l", "-1");
                p.putString("pid", key);
                p.putBoolean("b", false);
                p.putInt("x", w*(img.getInt("img_w")/grid.getInt("ncols")));
                p.putInt("y", h*(img.getInt("img_h")/grid.getInt("nrows")));
                p.putInt("r", h);
                p.putInt("c", w);
                pieces.putBundle(key, p);
            }
        }

        config.putBundle("board", board);
        config.putBundle("grid", grid);
        config.putBundle("frus", frus);
        config.putBundle("pieces", pieces);
        config.putString("myId", "player1234-uid");
        config.putBundle("img", img);
        config.putBundle("scores", scores);

        return config;
    }

    private ExampleJigsawConfigurations () {
        throw new AssertionError();
    }
}
