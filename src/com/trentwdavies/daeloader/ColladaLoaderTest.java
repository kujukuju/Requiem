package com.trentwdavies.daeloader;

/**
 * Created by Trent on 11/4/2014.
 */
public class ColladaLoaderTest {
    public static void main(String[] args) {
        try {
            ColladaLoader.loadFile("models/loader_test_1.dae");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
