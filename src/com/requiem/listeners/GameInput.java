package com.requiem.listeners;

import com.requiem.Requiem;
import com.requiem.abstractentities.GameCamera;
import com.requiem.abstractentities.entities.Player;
import com.requiem.managers.PlayerManager;
import com.requiem.managers.SettingsManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 9/16/2014.
 */
public class GameInput {
    public static boolean[] keysDown = new boolean[Keyboard.KEYBOARD_SIZE];
    public static boolean[] keysDeltaDown = new boolean[Keyboard.KEYBOARD_SIZE];
    public static boolean[] keysDeltaUp = new boolean[Keyboard.KEYBOARD_SIZE];

    public static double mouseX = 0;
    public static double mouseY = 0;
    public static boolean mouseDownLeft = false;
    public static boolean mouseDeltaDownLeft = false;
    public static boolean mouseDeltaUpLeft = false;
    public static boolean mouseDownRight = false;
    public static boolean mouseDeltaDownRight = false;
    public static boolean mouseDeltaUpRight = false;

    public static void update() {
        clearDeltas();
        checkKeys();

        if (!Mouse.isGrabbed()) {
            mouseX = Mouse.getX();
            mouseY = Mouse.getY();
        }
    }

    public static void clearDeltas() {
        for (int i = 0; i < Keyboard.KEYBOARD_SIZE; i++) {
            keysDeltaDown[i] = false;
            keysDeltaUp[i] = false;
        }
        mouseDeltaDownLeft = false;
        mouseDeltaUpLeft = false;
        mouseDeltaDownRight = false;
        mouseDeltaUpRight = false;
    }

    public static void checkKeys() {
        while (Keyboard.next()) {
            int curKey = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
                //keys pressed
                if (!keysDown[curKey]) {
                    keysDown[curKey] = true;
                    keysDeltaDown[curKey] = true;
                }
            } else {
                //keys released
                if (keysDown[curKey]) {
                    keysDown[curKey] = false;
                    keysDeltaUp[curKey] = true;
                }
            }
        }

        if (Mouse.isButtonDown(0)) {
            if (!mouseDownLeft) {
                mouseDownLeft = true;
                mouseDeltaDownLeft = true;
            }
        } else {
            if (mouseDownLeft) {
                mouseDownLeft = false;
                mouseDeltaUpLeft = true;
            }
        }
        if (Mouse.isButtonDown(1)) {
            if (!mouseDownRight) {
                mouseDownRight = true;
                mouseDeltaDownRight = true;
            }
        } else {
            if (mouseDownRight) {
                mouseDownRight = false;
                mouseDeltaUpRight = true;
            }
        }
    }
}
