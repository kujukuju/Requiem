package com.requiem.utilities;

import com.requiem.Requiem;
import com.requiem.interfaces.Asset;
import com.trentwdavies.daeloader.ColladaLoader;
import com.trentwdavies.daeloader.Model;
import com.trentwdavies.textureloader.Texture;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Trent on 11/17/2014.
 */
public class AssetManager {
    private static Map<String, Asset> filePathAssetMap = new ConcurrentHashMap<String, Asset>();
    private static AtomicBoolean loading = new AtomicBoolean();
    private static AtomicLong totalBytes = new AtomicLong();
    private static AtomicLong readBytes = new AtomicLong();

    private static Map<String, Class> queue = new ConcurrentHashMap<String, Class>();
    private static Map<String, Long> fileBytes = new ConcurrentHashMap<String, Long>();

    public static void load() {
        loading.set(true);

        //start thread?
        while (!queue.isEmpty()) {
            Set<String> keySet = queue.keySet();
            for (String path : keySet) {
                Class type = queue.get(path);
                long bytes = fileBytes.get(path);

                Asset asset = loadAsset(path, type);
                filePathAssetMap.put(path, asset);

                readBytes.addAndGet(bytes);
                queue.remove(path);
            }
        }

        readBytes.set(0);
        totalBytes.set(0);
        queue.clear();
        fileBytes.clear();
        loading.set(false);
        //end thread?
    }

    //is this faster if I do it in a thread? if so colladaloader needs to be not static
    public static void queue(String path, Class type) {
        path = path.toLowerCase();
        File file = new File(path);
        long fileBytesSize = file.length();
        totalBytes.addAndGet(fileBytesSize);

        queue.put(path, type);
        fileBytes.put(path, fileBytesSize);
    }

    public static double getPercentLoaded() {
        if (totalBytes.get() == 0) {
            return 1;
        }

        return (double) readBytes.get() / totalBytes.get();
    }

    public static Asset getAsset(String path) {
        if (filePathAssetMap.containsKey(path)) {
            return filePathAssetMap.get(path);
        } else {
            System.err.println("Asset not loaded into assetmanager!");
            System.exit(1);
        }

        return null;
    }

    public static boolean containsAsset(String path) {
        path = path.toLowerCase();
        return filePathAssetMap.containsKey(path);
    }

    public static void dispose(String path) {
        path = path.toLowerCase();

        filePathAssetMap.remove(path);
    }

    public static void pauseWhileLoading() {
        while (loading.get()) {
            //pausing
        }
    }

    public static boolean isLoading() {
        return loading.get();
    }

    private static Asset loadAsset(String path, Class type) {
        if (type == Model.class) {
            try {
                return ColladaLoader.loadFile(path);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } else if (type == Texture.class) {
            try {
                return new Texture(new FileInputStream(path), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.err.println("assetmanager failed to queue something!");
        return null;
    }
}
