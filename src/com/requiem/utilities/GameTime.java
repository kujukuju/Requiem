package com.requiem.utilities;

/**
 * Created by Trent on 11/19/2014.
 */
public class GameTime {
    private static long currentTime = System.currentTimeMillis();
    private static int deltaTime = 1;

    public static void update() {
        long actualCurrentTime = System.currentTimeMillis();
        deltaTime = (int) (actualCurrentTime - currentTime);
        currentTime = actualCurrentTime;
    }

    public static double getTimeScale() {
        return deltaTime / 10.0;
    }

    public static double getInstantFPS() {
        return 1000.0 / deltaTime;
    }

    public static int getDeltaTime() {
        return deltaTime;
    }

    public static long getCurrentMillis() {
        return currentTime;
    }

    public static float getDeltaSeconds() {
        return deltaTime / 1000f;
    }
}
