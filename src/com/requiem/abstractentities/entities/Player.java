package com.requiem.abstractentities.entities;


import com.requiem.renderutilities.Batch;
import com.trentwdavies.daeloader.ColladaLoader;
import com.trentwdavies.daeloader.Model;
import org.xml.sax.SAXException;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Trent on 10/24/2014.
 */
public class Player extends Entity {
    public static final String PLAYER_MODEL_FILE_PATH = "models/loader_test_1.dae";
    private Model playerModel;

    private boolean init;

    public Player() {
        init();
    }

    @Override
    public void init() {
        try {
            playerModel = ColladaLoader.loadFile(PLAYER_MODEL_FILE_PATH);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        pos = new Point3d();
        vel = new Vector3d();
        ang = new Vector3d();

        init = true;
    }

    @Override
    public void update() {
        if (!init)
            init();
    }

    @Override
    public void render() {
        Batch.renderModel(playerModel);
    }
}
