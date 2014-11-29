package com.requiem.abstractentities.entities;


import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.requiem.utilities.PhysicsUtils;
import com.requiem.utilities.renderutilities.Batch;
import com.trentwdavies.daeloader.ColladaLoader;
import com.trentwdavies.daeloader.Model;
import org.xml.sax.SAXException;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class Player extends Entity {
    public static final String PLAYER_MODEL_FILE_PATH = "assets/models/test-character.dae";
    public static final float MASS = 1;
    private Model playerModel;

    private boolean init;

    public Player() {
        pos = new Point3d();
        vel = new Vector3d();
        ang = new Vector3d();
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

        collisionShape = new GImpactMeshShape(PhysicsUtils.makeTIVA(playerModel));
        Vector3f localInertia = new Vector3f(0, 0, 0);
        collisionShape.calculateLocalInertia(MASS, localInertia);
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

        glTranslated(pos.x, pos.y, pos.z);
        glRotated(-ang.y, 0, 1, 0);

        Batch.renderModel(playerModel);

        glPopMatrix();
    }
}
