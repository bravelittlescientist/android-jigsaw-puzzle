package com.bravelittlescientist.android_puzzle_view;

import android.os.Bundle;

public final class ExampleJigsawConfigurations {

    public static Bundle getKittenExample() {
        // Initialization
        Bundle config = new Bundle();
        Bundle pieces = new Bundle();
        Bundle grid = new Bundle();
        Bundle image = new Bundle();

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

    private ExampleJigsawConfigurations () {
        throw new AssertionError();
    }
}
