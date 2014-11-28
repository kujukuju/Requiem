package com.requiem.jsonmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trent on 7/27/2014.
 */
public class Settings {
    @SerializedName("monitor-resolution")
    private int[] monitorResolution;
    @SerializedName("resolution")
    private int[] resolution;
    @SerializedName("mouse-sensitivity")
    private double[] mouseSensitivity;

    public Settings() {
    }

    public Settings(int[] monitorResolution, int[] resolution, double[] mouseSensitivity) {
        this.monitorResolution = monitorResolution;
        this.resolution = resolution;
        this.mouseSensitivity = mouseSensitivity;
    }

    public double[] getMouseSensitivity() {
        return mouseSensitivity;
    }

    public void setMouseSensitivity(double[] mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }

    public int[] getResolution() {
        return resolution;
    }

    public void setResolution(int[] resolution) {
        this.resolution = resolution;
    }

    public int[] getMonitorResolution() {
        return monitorResolution;
    }

    public void setMonitorResolution(int[] monitorResolution) {
        this.monitorResolution = monitorResolution;
    }
}
