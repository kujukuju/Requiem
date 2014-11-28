package com.requiem.abstractentities.entities;

import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.requiem.utilities.PhysicsUtils;
import com.requiem.utilities.renderutilities.Batch;
import com.requiem.utilities.AssetManager;
import com.trentwdavies.daeloader.Model;

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
        levelModel = (Model) AssetManager.getAsset(levelPath);

        collisionShape = new BvhTriangleMeshShape(PhysicsUtils.makeTIVA(levelModel), true);
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

            Batch.renderModel(levelModel);

        glPopMatrix();
    }
}
