package com.requiem.abstractentities.entities;

import com.requiem.renderutilities.Batch;
import com.requiem.utilities.AssetManager;
import com.trentwdavies.daeloader.ColladaLoader;
import com.trentwdavies.daeloader.Model;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

/**
 * Created by Trent on 10/26/2014.
 */
public class Level extends Entity {
    private Model levelModel;

    private String levelPath;

    private boolean init;

    public Level(String levelPath) {
        this.levelPath = levelPath;
    }

    @Override
    public void init() {
        try {
            levelModel = ColladaLoader.loadFile(levelPath);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        init = true;
    }

    @Override
    public void update() {
        if (!init)
            init();
    }

    @Override
    public void render() {
        glPushMatrix();

            glTranslated(-1, 2, -1);

            Batch.renderModel(levelModel);

        glPopMatrix();
    }
}
