package com.requiem.logic;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.requiem.Requiem;
import com.requiem.abstractentities.entities.Level;
import com.requiem.abstractentities.entities.Player;
import com.requiem.interfaces.Collidable;
import com.requiem.listeners.GameInput;
import com.requiem.managers.PlayerManager;
import com.requiem.managers.SettingsManager;
import com.requiem.utilities.GameTime;
import com.requiem.utilities.PhysicsUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.util.List;

/**
 * Created by Trent on 10/27/2014.
 */
public class Physics {
    public static final double GRAVITY = 32;
    public static final double ACCEL = 160;
    public static final double FRICTION = 80;
    public static final double DRAG = 10;//if not on the ground
    public static final double MAX_SPEED = 6;

    public static final double TOLERANCE = 0.00001;

    public static DiscreteDynamicsWorld dynamicsWorld;
    public static Level currentLevel;

    private static List<Collidable> collidables;

    private static boolean init;

    public static void init() {
        dynamicsWorld = PhysicsUtils.createDynamicsWorld();
        dynamicsWorld.setGravity(new Vector3f(0, (float) -GRAVITY, 0));

        //add rigid bodies
        addCollidables();

        init = true;
    }

    public static void addCollidables() {
        Player mainPlayer = PlayerManager.PLAYER;

        System.out.println("adding rigid bodies to dynamicsworld");
        Physics.currentLevel.addToDynamicsWorld(dynamicsWorld);
        mainPlayer.addToDynamicsWorld(dynamicsWorld);
    }

    public static void update() {
        if (!init) {
            init();
        }

        PlayerManager.PLAYER.rigidBody.activate(true);
        PlayerManager.PLAYER.rigidBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
        PlayerManager.PLAYER.rigidBody.activate(true);

        playerAngles();
        playerXZMovements();
        playerFrictionMovements();
        playerJumpMovements();
        playerDragMovements();

        dynamicsWorld.stepSimulation(GameTime.getDeltaSeconds(), 10, 1f / 60);

        cameraPosition();
    }

    public static void setCurrentLevel(Level currentLevel) {
        Physics.currentLevel = currentLevel;

        if (init) {
            //TODO if youre changing the level after its been initialized you have to do a lot more than this probably
            Physics.currentLevel.addToDynamicsWorld(dynamicsWorld);
        }
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
        player.rigidBody.setAngularFactor(0);
        player.rigidBody.setAngularVelocity(new Vector3f(0, 0, 0));
    }

    //TODO need to do air friction if youre going faster than max speed
    private static void playerXZMovements() {
        Player player = PlayerManager.PLAYER;

        double yawRadians = Math.toRadians(player.ang.y);
        double originalSpeedSquaredXZ = new Vector3d(player.vel.x, 0, player.vel.z).lengthSquared();

        Vector3d appliedForce = new Vector3d();
        if (GameInput.keysDown[Keyboard.KEY_W]) {
            appliedForce.add(new Vector3d(Math.sin(yawRadians), 0, -Math.cos(yawRadians)));
        }
        if (GameInput.keysDown[Keyboard.KEY_S]) {
            appliedForce.add(new Vector3d(-Math.sin(yawRadians), 0, Math.cos(yawRadians)));
        }

        if (GameInput.keysDown[Keyboard.KEY_D]) {
            appliedForce.add(new Vector3d(Math.sin(yawRadians + Math.PI / 2), 0, -Math.cos(yawRadians + Math.PI / 2)));
        }
        if (GameInput.keysDown[Keyboard.KEY_A]) {
            appliedForce.add(new Vector3d(-Math.sin(yawRadians + Math.PI / 2), 0, Math.cos(yawRadians + Math.PI / 2)));
        }
        if (appliedForce.lengthSquared() > 0) {
            appliedForce.normalize();
            appliedForce.scale(ACCEL);
        }

        Vector3d appliedForceToVel = (Vector3d) appliedForce.clone();
        appliedForceToVel.scale(1 / (Player.MASS * 60));

        Vector3d finalVel = new Vector3d(player.vel.x, 0, player.vel.z);
        finalVel.add(appliedForceToVel);
        if (originalSpeedSquaredXZ - TOLERANCE < MAX_SPEED * MAX_SPEED && finalVel.lengthSquared() > MAX_SPEED * MAX_SPEED) {
            finalVel.normalize();
            finalVel.scale(MAX_SPEED);
        } else if (finalVel.lengthSquared() > MAX_SPEED * MAX_SPEED) {
            finalVel.normalize();
            finalVel.scale(Math.sqrt(originalSpeedSquaredXZ));
        }

        Vector3d finalVelMinusOldVelForce = (Vector3d) finalVel.clone();
        finalVelMinusOldVelForce.sub(new Vector3d(player.vel.x, 0, player.vel.z));
        finalVelMinusOldVelForce.scale(Player.MASS * 60);

        player.rigidBody.applyCentralForce(new Vector3f(finalVelMinusOldVelForce));

        player.vel = finalVel;
    }

    private static void playerFrictionMovements() {
        Player player = PlayerManager.PLAYER;

        //TODO might need to set the velocity to zero if its near zero
        Vector3d playerVelXZ = new Vector3d(player.vel.x, 0, player.vel.z);
        if (playerVelXZ.lengthSquared() > 0) {//TODO and if on ground
            Vector3d frictionForce = new Vector3d(-player.vel.x, 0, -player.vel.z);
            frictionForce.normalize();
            frictionForce.scale(FRICTION);

            Vector3d frictionForceToVel = (Vector3d) frictionForce.clone();
            frictionForceToVel.scale(1 / (Player.MASS * 60));

            Vector3d applyForce = new Vector3d(frictionForce);
            if (frictionForceToVel.lengthSquared() > player.vel.lengthSquared()) {
                applyForce = new Vector3d(-player.vel.x, 0, -player.vel.z);
                applyForce.scale(Player.MASS * 60);
            }

            player.rigidBody.applyCentralForce(new Vector3f(applyForce));
        }
    }

    private static void playerJumpMovements() {
        Player player = PlayerManager.PLAYER;

        if (GameInput.keysDeltaDown[Keyboard.KEY_SPACE]) {
            player.rigidBody.applyCentralImpulse(new Vector3f(0, 40, 0));
        }
    }

    private static void playerDragMovements() {
        //if not on ground (but it is by default for now)
    }

    private static void cameraPosition() {
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
