package com.trentwdavies.particleloader;

import com.trentwdavies.textureloader.Texture;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trent on 12/17/2014.
 */
public class Particle {
    public static Map<String, Point> pathRowColumnMap;

    public Texture texture;

    public Particle() {

    }

    static {
        try {
            constructPathRowColumnMap();
        } catch (Exception e) {
            System.err.println("Error in the Particle static init call");
            e.printStackTrace();
        }
    }

    public static void constructPathRowColumnMap() {
        pathRowColumnMap = new HashMap<String, Point>();
        pathRowColumnMap.put("", new Point(2, 2));
    }
}
