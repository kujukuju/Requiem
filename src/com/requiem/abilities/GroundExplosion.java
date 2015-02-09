package com.requiem.abilities;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.requiem.interfaces.Collidable;
import com.requiem.listeners.GameInput;
import com.requiem.utilities.AssetManager;
import com.requiem.utilities.GameTime;
import com.trentwdavies.daeloader.Model;
import com.trentwdavies.textureloader.Texture;

/**
 * Created by Trent on 2/7/2015.
 */
public class GroundExplosion implements Ability {
    private int stage;

    private static final int TOTAL_CAST_TIME = 1500;
    private long startCastTime;

    private static final int TOTAL_CHARGES = 1;
    private static int remainingCharges = TOTAL_CHARGES;

    private static final int TOTAL_COOLDOWN = 0;
    private static long cooldownTimerStart;

    @Override
    public int getStage() {
        return stage;
    }

    @Override
    public boolean isCasting() {
        return stage == STAGE_CASTING;
    }

    @Override
    public int getTotalCastTime() {
        return TOTAL_CAST_TIME;
    }

    @Override
    public float getCastPercent() {
        return (GameTime.getCurrentMillis() - startCastTime) / TOTAL_CAST_TIME;
    }

    @Override
    public int getTotalCharges() {
        return TOTAL_CHARGES;
    }

    @Override
    public int getRemainingCharges() {
        return remainingCharges;
    }

    @Override
    public int getTotalCooldown() {
        return TOTAL_COOLDOWN;
    }

    @Override
    public int getRemainingCooldown() {
        int remainingCooldown = (int) (TOTAL_COOLDOWN - (GameTime.getCurrentMillis() - cooldownTimerStart));
        return Math.max(0, remainingCooldown);
    }

    @Override
    public float getRemainingCooldownPercent() {
        return getRemainingCooldown() / getTotalCooldown();
    }

    @Override
    public void render() {

    }

    @Override
    public String getModelPath() {
        return null;
    }

    @Override
    public void setModelPath(String path) {
        //no model
    }

    @Override
    public void update() {
        if (GameInput.mouseDownLeft) {
            System.out.println("casting!");
        }
    }
}
