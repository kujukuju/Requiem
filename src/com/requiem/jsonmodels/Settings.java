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

    public Settings() {
    }

    public Settings(int[] monitorResolution, int[] resolution) {
        this.monitorResolution = monitorResolution;
        this.resolution = resolution;
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
