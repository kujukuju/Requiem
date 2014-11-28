package com.requiem;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Created by Trent on 11/19/2014.
 */
public class RequiemLauncher {
    public static Requiem requiem;
    public static void main(String[] args) {
        int width = (int) (1920 * 0.5);
        int height = (int) (1080 * 0.5);
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }

        requiem = new Requiem();
    }
}
