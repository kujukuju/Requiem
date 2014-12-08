package com.requiem.abstractentities.entities;


import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.DynamicsWorld;
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
    public GhostObject ghostObject;//for stair step testing
    public static final float MASS = 2;
    public static final float FRICTION = 0;
    public static final float RESTITUTION = 0f;

    public static final float HEIGHT = 2;
    public static final float WIDTH = 1;
    public static final float CCD_SPHERE_SWEPT_RADIUS = 0.5f;//should fit inside the person

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

        pos = new Point3d(-5, 0, -12);

        //collisionShape = new BvhTriangleMeshShape(PhysicsUtils.makeTIVA(playerModel), true);
        //collisionShape = new SphereShape(0.1f);
        collisionShape = new CapsuleShape(WIDTH / 2, HEIGHT / 2);
        Vector3f localInertia = new Vector3f(0, 0, 0);
        collisionShape.calculateLocalInertia(MASS, localInertia);
        createRigidBody();
        setPos(pos);

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

        glTranslated(pos.x, pos.y - HEIGHT / 2, pos.z);
        glRotated(-ang.y, 0, 1, 0);

        Batch.renderModel(playerModel);

        glPopMatrix();
    }

    @Override
    public void createRigidBody() {
        Vector3f localInertia = new Vector3f();
        collisionShape.calculateLocalInertia(MASS, localInertia);
        RigidBodyConstructionInfo constructionInfo = PhysicsUtils.createRigidBodyConstructionInfo(MASS, new Point3d(0, 0, 0), collisionShape, localInertia);
        constructionInfo.restitution = RESTITUTION;
        constructionInfo.friction = FRICTION;
        rigidBody = new RigidBody(constructionInfo);
        rigidBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
        rigidBody.setCcdSweptSphereRadius(CCD_SPHERE_SWEPT_RADIUS);

        ghostObject = new GhostObject();
        ghostObject.setCollisionShape(collisionShape);
    }

    public void setPos(Point3d pos) {
        Vector3f vectorPos = new Vector3f((float) pos.x, (float) pos.y + HEIGHT / 2, (float) pos.z);
        Vector3f curPos = rigidBody.getWorldTransform(new Transform()).origin;
        vectorPos.sub(curPos);
        rigidBody.translate(vectorPos);
    }

    @Override
    public void addToDynamicsWorld(DynamicsWorld dynamicsWorld) {
        dynamicsWorld.addRigidBody(rigidBody);
    }
}
