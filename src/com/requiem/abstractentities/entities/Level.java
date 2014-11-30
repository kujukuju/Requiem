package com.requiem.abstractentities.entities;

import com.bulletphysics.collision.shapes.*;
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

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

/**
 * Created by Trent on 10/26/2014.
 */
public class Level extends Entity implements Collidable {
    private String levelPath;
    public Model levelModel;
    public CollisionShape collisionShape;
    public RigidBody rigidBody;
    public static final float FRICTION = 1f;
    public static final float RESTITUTION = 0.1f;

    private boolean init;

    public Level(String levelPath) {
        this.levelPath = levelPath;
    }

    @Override
    public void init() {
        levelModel = (Model) AssetManager.getAsset(levelPath);

        //collisionShape = new BvhTriangleMeshShape(PhysicsUtils.makeTIVA(levelModel), true);
        collisionShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 0f);
        System.out.println("creating level rigid body");
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
    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    @Override
    public void createRigidBody() {
        //TODO collisionobject might be better
        //CollisionObject worldObject = PhysicsUtils.createCollisionObject(currentLevel.collisionShape);
        RigidBodyConstructionInfo levelConstructionInfo = PhysicsUtils.createRigidBodyConstructionInfo(0, collisionShape);
        //bounciness
        levelConstructionInfo.restitution = RESTITUTION;
        levelConstructionInfo.friction = FRICTION;
        rigidBody = new RigidBody(levelConstructionInfo);

        //TODO collisionobject might be faster
        //dynamicsWorld.addCollisionObject(PhysicsUtils.createCollisionObject(currentLevel.collisionShape));
    }

    @Override
    public void setRigidBody(RigidBody rigidBody) {
        this.rigidBody = rigidBody;
    }

    @Override
    public RigidBody getRigidBody() {
        return rigidBody;
    }
}
