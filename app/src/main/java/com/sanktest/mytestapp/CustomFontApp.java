package com.sanktest.mytestapp;

import android.app.Application;

import com.sanktest.mytestapp.activities.FontsOverride;

/**
 * Created by Admin on 21-03-2018.
 */

public class CustomFontApp extends Application {
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/SourceSansPro-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/SourceSansPro-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Oswald-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/SourceSansPro-Regular.ttf");
    }
}
