package com.requiem.abstractentities.entities;


import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.requiem.interfaces.Collidable;
import com.requiem.utilities.PhysicsUtils;
import com.requiem.utilities.renderutilities.Batch;
import com.trentwdavies.daeloader.ColladaLoader;
import com.trentwdavies.daeloader.Model;
import org.xml.sax.SAXException;

import javax.vecmath.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class Player extends Entity implements Collidable {
    public static final String PLAYER_MODEL_FILE_PATH = "assets/models/test-character.dae";
    public  Model playerModel;
    public CollisionShape collisionShape;
    public RigidBody rigidBody;
    public static final float MASS = 2;
    public static final float FRICTION = 4;
    public static final float RESTITUTION = 0.1f;

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

        pos = new Point3d(-5, 5, -6);

        //collisionShape = new BvhTriangleMeshShape(PhysicsUtils.makeTIVA(playerModel), true);
        collisionShape = new SphereShape(0.1f);
        //collisionShape = new BoxShape(new Vector3f(0.1f, 0.1f, 0.1f));
        Vector3f localInertia = new Vector3f(0, 0, 0);
        collisionShape.calculateLocalInertia(MASS, localInertia);
        System.out.println("creating player rigid body");
        createRigidBody();

        init = true;
    }

    @Override
    public void update() {
        if (!init)
            init();

        Vector3f newPos = rigidBody.getWorldTransform(new Transform()).origin;
        Vector3f newVel = new Vector3f();
        rigidBody.getLinearVelocity(newVel);
        pos.x = newPos.x;
        pos.y = newPos.y;
        pos.z = newPos.z;
        vel.x = newVel.x;
        vel.y = newVel.y;
        vel.z = newVel.z;
    }

    @Override
    public void render() {
        glPushMatrix();

        glTranslated(pos.x, pos.y, pos.z);
        glRotated(-ang.y, 0, 1, 0);

        Batch.renderModel(playerModel);

        glPopMatrix();
    }

    @Override
    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    @Override
    public void createRigidBody() {
        Vector3f localInertia = new Vector3f();
        collisionShape.calculateLocalInertia(MASS, localInertia);
        RigidBodyConstructionInfo constructionInfo = PhysicsUtils.createRigidBodyConstructionInfo(MASS, pos, collisionShape, localInertia);
        constructionInfo.restitution = RESTITUTION;
        constructionInfo.friction = FRICTION;
        rigidBody = new RigidBody(constructionInfo);
        rigidBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
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
