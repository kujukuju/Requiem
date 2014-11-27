package com.requiem.logic;

import com.bulletphysics.dynamics.DynamicsWorld;
import com.requiem.Requiem;
import com.requiem.abstractentities.entities.MainPlayer;
import com.requiem.listeners.GameInputProcessor;
import com.requiem.managers.PlayerManager;
import com.requiem.utilities.GameTime;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector3d;

/**
 * Created by Trent on 10/27/2014.
 */
public class Physics {
    public static final double GRAVITY = 0.8;
    public static final double MAX_SPEED = 4;

    public static DynamicsWorld dynamicsWorld;

    public static void update() {
        double timeScale = GameTime.getInstantDeltaTime();

        MainPlayer MAIN_PLAYER = PlayerManager.MAIN_PLAYER;
        Vector3d guyAngle = MAIN_PLAYER.ang;
        double yaw = Math.toRadians(guyAngle.y);
        double originalSpeedSquared = PhysicsCalc.getLengthSquared(MAIN_PLAYER.vel);

        //TODO its not actually adding or something
        /*if (GameInputProcessor.keysDown[Input.Keys.W]) {
            MAIN_PLAYER.vel.add(new Vector3d((float) Math.cos(yaw), 0, (float) Math.sin(yaw)));
        }
        if (GameInputProcessor.keysDown[Input.Keys.S]) {
            MAIN_PLAYER.vel.add(new Vector3d(-Math.cos(yaw), 0, -Math.sin(yaw)));
        }

        if (GameInputProcessor.keysDown[Input.Keys.D]) {
            MAIN_PLAYER.vel.add(new Vector3d(Math.cos(yaw + Math.PI / 2), 0, Math.sin(yaw + Math.PI / 2)));
        }
        if (GameInputProcessor.keysDown[Input.Keys.A]) {
            MAIN_PLAYER.vel.add(new Vector3d(-Math.cos(yaw + Math.PI / 2), 0, -Math.sin(yaw + Math.PI / 2)));
        }*/

        MAIN_PLAYER.ang.y += Mouse.getDX();
        MAIN_PLAYER.ang.x -= Mouse.getDY();

        //TODO this is all gonna be wrong when you can move in 3 dimensions
        double newSpeedSquared = PhysicsCalc.getLengthSquared(MAIN_PLAYER.vel);
        if (originalSpeedSquared <= MAX_SPEED * MAX_SPEED && newSpeedSquared > MAX_SPEED * MAX_SPEED) {
            System.out.println("WHAT");
            System.out.println(MAIN_PLAYER.vel);
            MAIN_PLAYER.vel.normalize();
            System.out.println(MAIN_PLAYER.vel);
            MAIN_PLAYER.vel.x *= MAX_SPEED;
            MAIN_PLAYER.vel.y *= MAX_SPEED;
            MAIN_PLAYER.vel.z *= MAX_SPEED;
        }

        MAIN_PLAYER.vel.x *= timeScale;
        MAIN_PLAYER.vel.y *= timeScale;
        MAIN_PLAYER.vel.z *= timeScale;

        MAIN_PLAYER.pos.add(MAIN_PLAYER.vel);

        MAIN_PLAYER.update();

        setCameraPosition();
    }

    private static void setCameraPosition() {
        MainPlayer MAIN_PLAYER = PlayerManager.MAIN_PLAYER;
        double pitch = Math.toRadians(MAIN_PLAYER.ang.x);
        double yaw = Math.toRadians(MAIN_PLAYER.ang.y);

        double camDistance = 5;
        Vector3d camPos = new Vector3d();
        camPos.x = (float) (MAIN_PLAYER.pos.x - Math.cos(yaw) * camDistance * Math.cos(pitch));
        camPos.z = (float) (MAIN_PLAYER.pos.z - Math.sin(yaw) * camDistance * Math.cos(pitch));
        camPos.y = (float) (MAIN_PLAYER.pos.y + Math.sin(pitch) * camDistance);

        Requiem.GAME_CAMERA.pos.set(camPos);
        Requiem.GAME_CAMERA.lookAt(MAIN_PLAYER.pos);
    }
}
