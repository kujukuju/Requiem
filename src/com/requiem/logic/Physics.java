package com.requiem.logic;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.requiem.Requiem;
import com.requiem.abstractentities.entities.Level;
import com.requiem.abstractentities.entities.Player;
import com.requiem.listeners.GameInput;
import com.requiem.managers.PlayerManager;
import com.requiem.managers.SettingsManager;
import com.requiem.managers.StateManager;
import com.requiem.utilities.GameTime;
import com.requiem.utilities.PhysicsUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * Created by Trent on 10/27/2014.
 */
public class Physics {
    public static final double GRAVITY = 0.08;
    public static final double MAX_SPEED = .05;
    public static final double ACCEL = 0.004;
    public static final double FRICTION = 0.002;

    public static final double TOLERANCE = 0.00001;

    public static DynamicsWorld dynamicsWorld;
    public static Level currentLevel;

    private static boolean init;

    public static void init() {
        dynamicsWorld = PhysicsUtils.createDynamicsWorld();
        dynamicsWorld.setGravity(new Vector3f(0, (float) -GRAVITY, 0));

        //add rigid bodies
    }

    public static void update() {
        if (!init) {
            init();
        }

        dynamicsWorld.stepSimulation((float) GameTime.getDeltaTime());

        playerAngles();
        playerMovements();

        setCameraPosition();
    }

    public static void setCurrentLevel(Level currentLevel) {
        Physics.currentLevel = currentLevel;

        CollisionObject worldObject = PhysicsUtils.createCollisionObject(currentLevel.collisionShape);
        RigidBody personBody = PhysicsUtils.createRigidBody(PlayerManager.PLAYER.collisionShape, 1);

        dynamicsWorld.addCollisionObject(PhysicsUtils.createCollisionObject(currentLevel.collisionShape));
        dynamicsWorld.addRigidBody(personBody);
    }

    private static void playerAngles() {
        double[] mouseSensitivity = SettingsManager.getMouseSensitivity();
        Player player = PlayerManager.PLAYER;

        player.ang.y += Mouse.getDX() * mouseSensitivity[0];
        player.ang.x -= Mouse.getDY() * mouseSensitivity[1];
        if (player.ang.x > 89) {
            player.ang.x = 89;
        }
        if (player.ang.x < -89) {
            player.ang.x = -89;
        }
    }

    //TODO need to do air friction if youre going faster than max speed
    private static void playerMovements() {
        double timeScale = GameTime.getTimeScale();
        Player player = PlayerManager.PLAYER;

        double yawRadians = Math.toRadians(player.ang.y);
        double originalSpeedSquared = player.vel.lengthSquared();

        Vector3d accelVec = new Vector3d();
        if (GameInput.keysDown[Keyboard.KEY_W]) {
            accelVec.add(new Vector3d(Math.sin(yawRadians), 0, -Math.cos(yawRadians)));
        }
        if (GameInput.keysDown[Keyboard.KEY_S]) {
            accelVec.add(new Vector3d(-Math.sin(yawRadians), 0, Math.cos(yawRadians)));
        }

        if (GameInput.keysDown[Keyboard.KEY_D]) {
            accelVec.add(new Vector3d(Math.sin(yawRadians + Math.PI / 2), 0, -Math.cos(yawRadians + Math.PI / 2)));
        }
        if (GameInput.keysDown[Keyboard.KEY_A]) {
            accelVec.add(new Vector3d(-Math.sin(yawRadians + Math.PI / 2), 0, Math.cos(yawRadians + Math.PI / 2)));
        }
        if (accelVec.lengthSquared() > 0) {
            accelVec.normalize();
            accelVec.scale(ACCEL);
            accelVec.scale(timeScale);
        }
        player.vel.add(accelVec);

        if (player.vel.lengthSquared() > 0) {
            //apply friction
            Vector3d frictionVector = (Vector3d) player.vel.clone();
            frictionVector.normalize();
            frictionVector.scale(FRICTION);
            frictionVector.scale(timeScale);

            if (player.vel.lengthSquared() < frictionVector.lengthSquared()) {
                //will go past 0 speed
                player.vel.set(0, 0, 0);
            } else {
                player.vel.sub(frictionVector);
            }
        }

        double newSpeedSquared = player.vel.lengthSquared();
        if (originalSpeedSquared <= MAX_SPEED * MAX_SPEED + TOLERANCE && newSpeedSquared > MAX_SPEED * MAX_SPEED) {
            //went over max speed due to wasd key acceleration
            player.vel.normalize();
            player.vel.scale(MAX_SPEED);
        }

        Vector3d addVel = (Vector3d) player.vel.clone();
        addVel.scale(timeScale);
        player.pos.add(addVel);
    }

    private static void setCameraPosition() {
        Player player = PlayerManager.PLAYER;
        double pitch = Math.toRadians(player.ang.x);
        double yaw = Math.toRadians(player.ang.y);

        double camDistance = 5;
        Vector3d camPos = new Vector3d();
        camPos.x = (float) (player.pos.x + Math.sin(-yaw) * camDistance * Math.cos(pitch));
        camPos.z = (float) (player.pos.z + Math.cos(-yaw) * camDistance * Math.cos(pitch));
        camPos.y = (float) (player.pos.y + Math.sin(pitch) * camDistance);

        Requiem.GAME_CAMERA.pos.set(camPos);
        Requiem.GAME_CAMERA.lookAt(player.pos);
    }
}
