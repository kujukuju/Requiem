package com.requiem.abstractentities.entities;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.requiem.interfaces.Collidable;
import com.requiem.utilities.PhysicsUtils;
import com.requiem.utilities.renderutilities.Batch;
import com.requiem.utilities.AssetManager;
import com.trentwdavies.daeloader.Model;

import javax.vecmath.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

/**
 * Created by Trent on 10/26/2014.
 */
public class Level extends Entity implements Collidable {
    private String levelPath;
    public Model levelModel;
    public CollisionShape[] collisionShapes;
    public RigidBody[] rigidBodies;
    public static final float FRICTION = 0.8f;
    public static final float RESTITUTION = 0.1f;

    private boolean init;

    public Level(String levelPath) {
        this.levelPath = levelPath;
    }

    @Override
    public void init() {
        levelModel = (Model) AssetManager.getAsset(levelPath);

        collisionShapes = PhysicsUtils.getBvhTriangleMeshShapes(levelModel, true);
        createRigidBody();

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

    @Override
    public void createRigidBody() {
        //TODO collisionobject might be better
        rigidBodies = new RigidBody[collisionShapes.length];

        for (int i = 0; i < collisionShapes.length; i++) {
            CollisionShape curCollisionShape = collisionShapes[i];
            RigidBodyConstructionInfo levelConstructionInfo = PhysicsUtils.createRigidBodyConstructionInfo(0, curCollisionShape);
            //bounciness
            levelConstructionInfo.restitution = RESTITUTION;
            levelConstructionInfo.friction = FRICTION;
            rigidBodies[i] = new RigidBody(levelConstructionInfo);
        }
    }

    @Override
    public void addToDynamicsWorld(DynamicsWorld dynamicsWorld) {
        for (RigidBody curRigidBody : rigidBodies) {
            dynamicsWorld.addRigidBody(curRigidBody);
        }
    }
}
