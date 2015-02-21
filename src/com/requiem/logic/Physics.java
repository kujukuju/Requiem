package com.requiem.logic;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.requiem.Requiem;
import com.requiem.abstractentities.GameCamera;
import com.requiem.abstractentities.entities.Entity;
import com.requiem.abstractentities.entities.enemies.Enemy;
import com.requiem.Level;
import com.requiem.abstractentities.entities.Player;
import com.requiem.interfaces.Collidable;
import com.requiem.listeners.GameInput;
import com.requiem.managers.EnemyManager;
import com.requiem.managers.PlayerManager;
import com.requiem.managers.SettingsManager;
import com.requiem.states.PlayableState;
import com.requiem.utilities.GameTime;
import com.requiem.utilities.PhysicsUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.List;

/**
 * Created by Trent on 10/27/2014.
 */
public class Physics {
    public static final double GRAVITY = 32;
    public static final double DRAG = 10;//if not on the ground

    public static final double TOLERANCE = 0.00001;

    public static DiscreteDynamicsWorld dynamicsWorld;

    private static List<Collidable> collidables;

    private static boolean angleLocked;

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

        PlayableState.level.addToDynamicsWorld(dynamicsWorld);
        mainPlayer.addToDynamicsWorld(dynamicsWorld);
        for (Enemy enemy : EnemyManager.enemyList) {
            enemy.addToDynamicsWorld(dynamicsWorld);
        }
    }

    public static void update() {
        if (!init) {
            init();
        }

        PlayerManager.PLAYER.getRigidBody().activate(true);
        PlayerManager.PLAYER.getRigidBody().setActivationState(CollisionObject.DISABLE_DEACTIVATION);
        PlayerManager.PLAYER.getRigidBody().activate(true);

        playerAngles();
        playerXZMovements();
        playerFrictionMovements();
        playerJumpMovements();
        playerDragMovements();
        angleLocked = false;

        for (Enemy enemy : EnemyManager.enemyList) {
            enemyAngles(enemy);
            enemyXZMovements(enemy);
            enemyFrictionMovements(enemy);
            enemyJumpMovements(enemy);
            enemyDragMovements(enemy);
        }

        dynamicsWorld.stepSimulation(GameTime.getDeltaSeconds(), 10, 1f / 60);

        cameraPosition();
    }

    public static void updateCurrentLevel() {
        if (init) {
            //TODO if youre changing the level after its been initialized you have to do a lot more than this probably
            PlayableState.level.addToDynamicsWorld(dynamicsWorld);
        }
    }

    public static void lockPlayerAngles() {
        angleLocked = true;
    }

    private static void playerAngles() {
        Player player = PlayerManager.PLAYER;

        if (!angleLocked) {
            double[] mouseSensitivity = SettingsManager.getMouseSensitivity();

            player.getAng().y += Mouse.getDX() * mouseSensitivity[0];
            player.getAng().x -= Mouse.getDY() * mouseSensitivity[1];
            if (player.getAng().x > 89) {
                player.getAng().x = 89;
            }
            if (player.getAng().x < -89) {
                player.getAng().x = -89;
            }
        }
        player.getRigidBody().setAngularFactor(0);
        player.getRigidBody().setAngularVelocity(new Vector3f(0, 0, 0));
    }

    //TODO need to do air friction if youre going faster than max speed
    private static void playerXZMovements() {
        Player player = PlayerManager.PLAYER;

        float yawRadians = (float) Math.toRadians(player.getAng().y);
        float originalSpeedSquaredXZ = new Vector3f(player.getVel().x, 0, player.getVel().z).lengthSquared();

        Vector3f appliedForce = new Vector3f();
        if (GameInput.keysDown[Keyboard.KEY_W]) {
            appliedForce.add(new Vector3f((float) Math.sin(yawRadians), 0, (float) -Math.cos(yawRadians)));
        }
        if (GameInput.keysDown[Keyboard.KEY_S]) {
            appliedForce.add(new Vector3f((float) -Math.sin(yawRadians), 0, (float) Math.cos(yawRadians)));
        }

        if (GameInput.keysDown[Keyboard.KEY_D]) {
            appliedForce.add(new Vector3f((float) Math.sin(yawRadians + Math.PI / 2), 0, (float) -Math.cos(yawRadians + Math.PI / 2)));
        }
        if (GameInput.keysDown[Keyboard.KEY_A]) {
            appliedForce.add(new Vector3f((float) -Math.sin(yawRadians + Math.PI / 2), 0, (float) Math.cos(yawRadians + Math.PI / 2)));
        }
        if (appliedForce.lengthSquared() > 0) {
            appliedForce.normalize();
            appliedForce.scale(player.getAccel());
        }

        Vector3f appliedForceToVel = (Vector3f) appliedForce.clone();
        appliedForceToVel.scale(1 / (player.getMass() * 60));

        Vector3f finalVel = new Vector3f(player.getVel().x, 0, player.getVel().z);
        finalVel.add(appliedForceToVel);
        if (originalSpeedSquaredXZ - TOLERANCE < player.getMaxSpeed() * player.getMaxSpeed() && finalVel.lengthSquared() > player.getMaxSpeed() * player.getMaxSpeed()) {
            finalVel.normalize();
            finalVel.scale(player.getMaxSpeed());
        } else if (finalVel.lengthSquared() > player.getMaxSpeed() * player.getMaxSpeed()) {
            finalVel.normalize();
            finalVel.scale((float) Math.sqrt(originalSpeedSquaredXZ));
        }

        Vector3f finalVelMinusOldVelForce = (Vector3f) finalVel.clone();
        finalVelMinusOldVelForce.sub(new Vector3f(player.getVel().x, 0, player.getVel().z));
        finalVelMinusOldVelForce.scale(player.getMass() * 60);

        player.getRigidBody().applyCentralForce(new Vector3f(finalVelMinusOldVelForce));

        player.setVel(finalVel);
    }

    private static void playerFrictionMovements() {
        Player player = PlayerManager.PLAYER;

        //TODO might need to set the velocity to zero if its near zero
        Vector3f playerVelXZ = new Vector3f(player.getVel().x, 0, player.getVel().z);
        if (playerVelXZ.lengthSquared() > 0) {//TODO and if on ground
            Vector3f frictionForce = new Vector3f(-player.getVel().x, 0, -player.getVel().z);
            frictionForce.normalize();
            frictionForce.scale(player.getFriction());

            Vector3f frictionForceToVel = (Vector3f) frictionForce.clone();
            frictionForceToVel.scale(1 / (player.getMass() * 60));

            Vector3f applyForce = new Vector3f(frictionForce);
            if (frictionForceToVel.lengthSquared() > player.getVel().lengthSquared()) {
                applyForce = new Vector3f(-player.getVel().x, 0, -player.getVel().z);
                applyForce.scale(player.getMass() * 60);
            }

            player.getRigidBody().applyCentralForce(new Vector3f(applyForce));
        }
    }

    private static void playerJumpMovements() {
        Player player = PlayerManager.PLAYER;

        if (GameInput.keysDeltaDown[Keyboard.KEY_SPACE]) {
            player.getRigidBody().applyCentralImpulse(new Vector3f(0, 40, 0));
        }
    }

    private static void playerDragMovements() {
        //if not on ground (but it is by default for now)
    }




    private static void enemyAngles(Enemy enemy) {
        enemy.getRigidBody().setAngularFactor(0);
        enemy.getRigidBody().setAngularVelocity(new Vector3f(0, 0, 0));

        List<Point3f> path = enemy.getAIProcessor().getCurrentPath();
        List<Entity> targets = enemy.getAIProcessor().getTargets();
        if (path.size() > 0) {
            Point3f enemyPos = enemy.getPos();
            Point3f pathPos = path.get(0);
            float yawAngleToPath = (float) Math.toDegrees(Math.atan2(pathPos.z - enemyPos.z, pathPos.x - enemyPos.x)) + 90;
            enemy.getAng().y = yawAngleToPath;
        } else if (targets.size() > 0) {
            Point3f enemyPos = enemy.getPos();
            Point3f targetPos = enemy.getAIProcessor().getTargets().get(0).getPos();
            float yawAngleToTarget = (float) Math.toDegrees(Math.atan2(targetPos.z - enemyPos.z, targetPos.x - enemyPos.x)) + 90;
            enemy.getAng().y = yawAngleToTarget;
        }
    }

    //TODO need to do air friction if youre going faster than max speed
    private static void enemyXZMovements(Enemy enemy) {
        float yawRadians = (float) Math.toRadians(enemy.getAng().y);
        float originalSpeedSquaredXZ = new Vector3f(enemy.getVel().x, 0, enemy.getVel().z).lengthSquared();

        Vector3f appliedForce = new Vector3f();
        /*if (true) {
            appliedForce.add(new Vector3f((float) Math.sin(yawRadians), 0, (float) -Math.cos(yawRadians)));
        }
        if (GameInput.keysDown[Keyboard.KEY_S]) {
            appliedForce.add(new Vector3f((float) -Math.sin(yawRadians), 0, (float) Math.cos(yawRadians)));
        }

        if (GameInput.keysDown[Keyboard.KEY_D]) {
            appliedForce.add(new Vector3f((float) Math.sin(yawRadians + Math.PI / 2), 0, (float) -Math.cos(yawRadians + Math.PI / 2)));
        }
        if (GameInput.keysDown[Keyboard.KEY_A]) {
            appliedForce.add(new Vector3f((float) -Math.sin(yawRadians + Math.PI / 2), 0, (float) Math.cos(yawRadians + Math.PI / 2)));
        }*/
        if (appliedForce.lengthSquared() > 0) {
            appliedForce.normalize();
            appliedForce.scale(enemy.getAccel());
        }

        Vector3f appliedForceToVel = (Vector3f) appliedForce.clone();
        appliedForceToVel.scale(1 / (enemy.getMass() * 60));

        Vector3f finalVel = new Vector3f(enemy.getVel().x, 0, enemy.getVel().z);
        finalVel.add(appliedForceToVel);
        if (originalSpeedSquaredXZ - TOLERANCE < enemy.getMaxSpeed() * enemy.getMaxSpeed() && finalVel.lengthSquared() > enemy.getMaxSpeed() * enemy.getMaxSpeed()) {
            finalVel.normalize();
            finalVel.scale(enemy.getMaxSpeed());
        } else if (finalVel.lengthSquared() > enemy.getMaxSpeed() * enemy.getMaxSpeed()) {
            finalVel.normalize();
            finalVel.scale((float) Math.sqrt(originalSpeedSquaredXZ));
        }

        Vector3f finalVelMinusOldVelForce = (Vector3f) finalVel.clone();
        finalVelMinusOldVelForce.sub(new Vector3f(enemy.getVel().x, 0, enemy.getVel().z));
        finalVelMinusOldVelForce.scale(enemy.getMass() * 60);

        enemy.getRigidBody().applyCentralForce(new Vector3f(finalVelMinusOldVelForce));

        enemy.setVel(finalVel);
    }

    private static void enemyFrictionMovements(Enemy enemy) {
        //TODO might need to set the velocity to zero if its near zero
        Vector3f enemyVelXZ = new Vector3f(enemy.getVel().x, 0, enemy.getVel().z);
        if (enemyVelXZ.lengthSquared() > 0) {//TODO and if on ground
            Vector3f frictionForce = new Vector3f(-enemy.getVel().x, 0, -enemy.getVel().z);
            frictionForce.normalize();
            frictionForce.scale(enemy.getFriction());

            Vector3f frictionForceToVel = (Vector3f) frictionForce.clone();
            frictionForceToVel.scale(1 / (enemy.getMass() * 60));

            Vector3f applyForce = new Vector3f(frictionForce);
            if (frictionForceToVel.lengthSquared() > enemy.getVel().lengthSquared()) {
                applyForce = new Vector3f(-enemy.getVel().x, 0, -enemy.getVel().z);
                applyForce.scale(enemy.getMass() * 60);
            }

            enemy.getRigidBody().applyCentralForce(new Vector3f(applyForce));
        }
    }

    private static void enemyJumpMovements(Enemy enemy) {
        if (false) {
            enemy.getRigidBody().applyCentralImpulse(new Vector3f(0, 40, 0));
        }
    }

    private static void enemyDragMovements(Enemy enemy) {
        //if not on ground (but it is by default for now)
    }




    private static void cameraPosition() {
        Player player = PlayerManager.PLAYER;
        float pitch = (float) Math.toRadians(player.getAng().x);
        float yaw = (float) Math.toRadians(player.getAng().y);

        float camDistance = 5;
        Vector3f camPos = new Vector3f();
        //camPos.x = (float) (player.getPos().x + Math.sin(yaw + 90) * camDistance * Math.cos(pitch));
        //camPos.z = (float) (player.getPos().z + Math.cos(yaw + 90) * camDistance * Math.cos(pitch));
        //camPos.y = (float) (player.getPos().y + Math.sin(pitch) * camDistance);
        camPos.x = (float) (player.getPos().x + Math.sin(-yaw + Math.PI / 2) * 1.5 * 0 + Math.sin(-yaw) * 2.0 * 0);
        camPos.y = (float) (player.getPos().y + 1.3);
        camPos.z = (float) (player.getPos().z + Math.cos(-yaw + Math.PI / 2) * 1.5 * 0 + Math.cos(-yaw) * 2.0 * 0);

        Vector3f camAng = new Vector3f(player.getAng());
        camAng.scale(-1);

        GameCamera.pos.set(camPos);
        GameCamera.setAng(camAng);
        //GameCamera.lookAt(player.getPos());
    }
}
