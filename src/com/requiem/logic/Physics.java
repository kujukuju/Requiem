package com.requiem.logic;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
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
import java.util.Dictionary;
import java.util.List;

/**
 * Created by Trent on 10/27/2014.
 */
public class Physics {
    public static final double GRAVITY = 8;
    //public static final double ACCEL = 120;
    public static final double ACCEL = 40;
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

        playerAngles();
        playerMovements();

        dynamicsWorld.stepSimulation(GameTime.getDeltaTime() / 1000f, 10);

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
    private static void playerMovements() {
        Player player = PlayerManager.PLAYER;
        //Transform playerTransform = new Transform();
        //player.rigidBody.getMotionState().getWorldTransform(playerTransform);

        double yawRadians = Math.toRadians(player.ang.y);
        double originalSpeedSquaredXZ = new Vector3d(player.vel.x, 0, player.vel.z).lengthSquared();

        Vector3d applyVec = new Vector3d();
        if (GameInput.keysDown[Keyboard.KEY_W]) {
            applyVec.add(new Vector3d(Math.sin(yawRadians), 0, -Math.cos(yawRadians)));
        }
        if (GameInput.keysDown[Keyboard.KEY_S]) {
            applyVec.add(new Vector3d(-Math.sin(yawRadians), 0, Math.cos(yawRadians)));
        }

        if (GameInput.keysDown[Keyboard.KEY_D]) {
            applyVec.add(new Vector3d(Math.sin(yawRadians + Math.PI / 2), 0, -Math.cos(yawRadians + Math.PI / 2)));
        }
        if (GameInput.keysDown[Keyboard.KEY_A]) {
            applyVec.add(new Vector3d(-Math.sin(yawRadians + Math.PI / 2), 0, Math.cos(yawRadians + Math.PI / 2)));
        }
        if (applyVec.lengthSquared() > 0/* && originalSpeedSquaredXZ < MAX_SPEED * MAX_SPEED*/) {
            applyVec.normalize();
            applyVec.scale(ACCEL);
            Vector3f applyVec3f = new Vector3f((float) applyVec.x, (float) applyVec.y, (float) applyVec.z);
            player.rigidBody.applyCentralForce(applyVec3f);
        }
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
