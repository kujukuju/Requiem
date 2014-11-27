package com.requiem.states;

import com.requiem.interfaces.State;
import com.requiem.managers.SettingsManager;

/**
 * Created by Trent on 10/25/2014.
 */
public class LoadingScreenState implements State {
    //private Texture loadingScreen;
    //private Texture loadingBar;
    private double percDone;

    private boolean init;

    @Override
    public void init() {
        //loadingScreen = Globals.ASSET_MANAGER.get("images/loading-screen.png", Texture.class);
        //loadingBar = Globals.ASSET_MANAGER.get("images/white-pixel.png", Texture.class);

        init = true;
    }

    @Override
    public void update() {
        if (!init)
            init();

        //percDone = Globals.ASSET_MANAGER.getProgress();
    }

    @Override
    public void render() {
        int[] resolution = SettingsManager.getResolution();

        float screenWidth = resolution[0];
        float screenHeight = resolution[1];
        float gapX = screenWidth / 960;
        float gapY = screenHeight / 540;
        float startX = screenWidth / 5 + gapX * 4;
        float width = 3 * screenWidth / 5 - gapX * 8;
        float heightPerc = 100f / 2160;
        float height = screenHeight * heightPerc - gapY * 4;
        float startY = screenHeight / 2 - height - gapY * 2;

        width *= percDone;

        /*cam.setToOrtho(false, screenWidth, screenHeight);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

            batch.draw(loadingScreen, 0, 0, screenWidth, screenHeight);
            batch.setColor(new Color(0.9f, 0.9f, 0.9f, 1f));
            batch.draw(loadingBar, startX, startY, width, height);
            batch.setColor(Globals.COLOR_TRUE_WHITE);

        batch.end();*/
    }
}
