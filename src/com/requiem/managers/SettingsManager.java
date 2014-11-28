package com.requiem.managers;


import com.google.gson.JsonSyntaxException;
import com.requiem.Globals;
import com.requiem.jsonmodels.Settings;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;

/**
 * Created by Trent on 10/24/2014.
 */
public class SettingsManager {
    private static Settings settings;

    private static final String SETTINGS_FILE_PATH = "assets/config/settings.json";
    private static final double MAX_ASPECT_RATIO = 1920.0 / 1080;

    public static double[] getMouseSensitivity() {
        return settings.getMouseSensitivity();
    }

    public static void setMouseSensitivity(double[] mouseSensitivity) {
        settings.setMouseSensitivity(mouseSensitivity);
    }

    public static int[] getResolution() {
        return settings.getResolution();
    }

    public static void setResolution(int[] resolution) {
        settings.setResolution(resolution);
    }

    private static int[] getMonitorResolution() {
        return settings.getMonitorResolution();
    }

    private static void setMonitorResolution(int[] monitorResolution) {
        settings.setMonitorResolution(monitorResolution);
    }

    public static void loadSettings() {
        //this weirdness is needed because its null if settings exists but is empty
        File settingsFile = new File(SETTINGS_FILE_PATH);
        if (settingsFile.exists()) {
            Type settingsType = Settings.class;
            try {
                settings = Globals.GSON.fromJson(new FileReader(settingsFile), settingsType);
            } catch (JsonSyntaxException e) {
                //if the settings file was corrupted by an idiot
                System.err.println("Warning! The settings file was unreadable due to a syntax exception.");
                settings = null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            settings = null;
        }

        if (settings == null) {
            setDefaults();
            writeSettings();
        } else {
            checkFields();
        }
    }

    private static void checkFields() {
        if (settings.getMonitorResolution() == null) {
            setDefaultMonitorResolution();
        }
        if (settings.getResolution() == null) {
            setDefaultResolution();
        }
        if (settings.getMouseSensitivity() == null) {
            setDefaultMouseSensitivity();
        }

        writeSettings();
    }

    public static void writeSettings() {
        String jsonString = Globals.GSON.toJson(settings);
        File settingsFile = new File(SETTINGS_FILE_PATH);
        try {
            FileWriter fw = new FileWriter(settingsFile);
            BufferedWriter br = new BufferedWriter(fw);
            br.write(jsonString);
            br.flush();
            fw.flush();
            br.close();
            fw.close();
        } catch (IOException e) {
            System.err.println("Warning! Was unable to write to the settings file.");
            e.printStackTrace();
        }
    }

    public static void setDefaults() {
        settings = new Settings();
        setDefaultMonitorResolution();
        setDefaultResolution();
        setDefaultMouseSensitivity();
    }

    private static void setDefaultMonitorResolution() {
        Dimension monitorResolutionDimension = Globals.TOOLKIT.getScreenSize();

        int[] monitorResolution
                = {(int) monitorResolutionDimension.getWidth(), (int) monitorResolutionDimension.getHeight()};
        setMonitorResolution(monitorResolution);
    }

    private static void setDefaultResolution() {
        Dimension resolutionDimension = Globals.TOOLKIT.getScreenSize();
        //to ensure that its not wider than 16:9
        double aspectRatio = resolutionDimension.getWidth() / resolutionDimension.getHeight();
        int resolutionWidth = (int) resolutionDimension.getWidth();
        int resolutionHeight = (int) resolutionDimension.getHeight();
        if (aspectRatio > MAX_ASPECT_RATIO)
            resolutionWidth = (int) (resolutionHeight * MAX_ASPECT_RATIO);

        int[] resolution = {resolutionWidth, resolutionHeight};
        setResolution(resolution);
    }

    private static void setDefaultMouseSensitivity() {
        double[] mouseSensitivity = {0.25, 0.5};
        setMouseSensitivity(mouseSensitivity);
    }
}
