package com.requiem;

import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.requiem.interfaces.Collidable;
import com.requiem.interfaces.Initializable;
import com.requiem.interfaces.Renderable;
import com.requiem.interfaces.Updateable;
import com.requiem.logic.Physics;
import com.requiem.states.PlayableState;
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
public class Level implements Renderable, Collidable, Initializable, Updateable {
    private String levelPath;

    public Model model;

    public CollisionWorld levelCollisionWorld;
    public CollisionShape[] collisionShapes;
    public RigidBody[] rigidBodies;

    public static final float FRICTION = 0.8f;
    public static final float RESTITUTION = 0.1f;

    public Level(String levelPath) {
        this.levelPath = levelPath;
    }

    @Override
    public void init() {
        model = (Model) AssetManager.getAsset(levelPath);

        collisionShapes = PhysicsUtils.getBvhTriangleMeshShapes(model, true);
        createRigidBody();

        DiscreteDynamicsWorld dynamicsWorld = PhysicsUtils.createDynamicsWorld();
        dynamicsWorld.setGravity(new Vector3f(0, (float) -Physics.GRAVITY, 0));
        addToDynamicsWorld(dynamicsWorld);
        levelCollisionWorld = dynamicsWorld.getCollisionWorld();
        System.out.println(levelCollisionWorld.getCollisionObjectArray().size());
    }

    @Override
    public void update() {

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
    public Model getModel() {
        return model;
    }

    @Override
    public void setModel(Model model) {

    }

    @Override
    public CollisionShape getCollisionShape() {
        return null;
    }

    @Override
    public void setCollisionShape(CollisionShape collisionShape) {
        //doesnt work
    }

    @Override
    public RigidBody getRigidBody() {
        return null;
    }

    @Override
    public void setRigidBody(RigidBody rigidBody) {
        //doesnt work
    }

    @Override
    public void render() {
        glPushMatrix();

        Batch.renderModel(model);

        glPopMatrix();
    }

    @Override
    public String getModelPath() {
        return null;
    }

    @Override
    public void setModelPath(String path) {
        //doesnt work
    }

    @Override
    public void addToDynamicsWorld(DynamicsWorld dynamicsWorld) {
        for (RigidBody curRigidBody : rigidBodies) {
            dynamicsWorld.addRigidBody(curRigidBody);
        }
    }
}
