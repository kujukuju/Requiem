package com.requiem.listeners;

import com.requiem.managers.SettingsManager;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 9/16/2014.
 */
public class GameInputProcessor {
    //TODO probably shouldnt be this big
    public static boolean[] keysDown = new boolean[Short.MAX_VALUE];
    public static boolean[] keysDeltaDown = new boolean[Short.MAX_VALUE];
    private static List<Integer> keysUndoDeltaDown = new ArrayList<Integer>();
    public static boolean[] keysDeltaUp = new boolean[Short.MAX_VALUE];
    private static List<Integer> keysUndoDeltaUp = new ArrayList<Integer>();

    public static double mouseX = 0;
    public static double mouseY = 0;
    public static boolean mouseDown = false;
    public static boolean mouseDeltaDown = false;
    public static boolean mouseDeltaUp = false;

    private static double scaleX = 0;
    private static double scaleY = 0;
/*
    public boolean keyDown(int keycode) {
        keysDown[keycode] = true;
        keysDeltaDown[keycode] = true;
        keysUndoDeltaDown.add(keycode);

        return true;
    }

    public boolean keyUp(int keycode) {
        keysDown[keycode] = false;
        keysDeltaUp[keycode] = true;
        keysUndoDeltaUp.add(keycode);

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int resolutionY = SettingsManager.getResolution()[1];
        mouseDown = true;
        mouseDeltaDown = true;
        mouseX = screenX * scaleX;
        mouseY = resolutionY - screenY * scaleY;

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int resolutionY = SettingsManager.getResolution()[1];
        mouseDown = false;
        mouseDeltaUp = true;
        mouseX = screenX * scaleX;
        mouseY = resolutionY - screenY * scaleY;

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (true)//update mouse position while dragged
            mouseMoved(screenX, screenY);

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        int resolutionY = SettingsManager.getResolution()[1];
        mouseX = screenX * scaleX;
        mouseY = resolutionY - screenY * scaleY;

        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
*/
    public void update() {
        removeDeltas();

        if (Mouse.isGrabbed()) {
            //GameCamera.perspCam.rotate(Mouse.getDX(), 1f, 1f, 0f);
        }
    }

    private void removeDeltas() {
        for (int i = 0; i < keysUndoDeltaDown.size(); i++) {
            int curKey = keysUndoDeltaDown.get(i);
            keysDeltaDown[curKey] = false;
        }
        keysUndoDeltaDown.clear();

        for (int i = 0; i < keysUndoDeltaUp.size(); i++) {
            int curKey = keysUndoDeltaUp.get(i);
            keysDeltaUp[curKey] = false;
        }
        keysUndoDeltaUp.clear();

        mouseDeltaDown = false;
        mouseDeltaUp = false;
    }
/*
    //method ran when either window or resolution dimensions change
    public static void resize() {
        int[] resolution = SettingsManager.getResolution();
        scaleX = (double) resolution[0] / Gdx.graphics.getWidth();
        scaleY = (double) resolution[1] / Gdx.graphics.getHeight();
    }
*/
}
