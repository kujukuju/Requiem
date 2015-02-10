package com.requiem.abilities;

import com.requiem.interfaces.Particle;
import com.requiem.listeners.GameInput;
import com.requiem.logic.Physics;
import com.requiem.managers.ParticleManager;
import com.requiem.managers.PlayerManager;
import com.requiem.particles.GroundExplosionFlame;
import com.requiem.states.PlayableState;
import com.requiem.utilities.GameTime;
import com.requiem.utilities.MathUtils;
import com.requiem.utilities.PhysicsUtils;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Trent on 2/7/2015.
 */
public class GroundExplosion implements Ability {
    private int stage;

    private long abilityCreationTime;
    private Random randomSeededGenerator;
    private int particleCount;
    private static final float RADIUS = 1;

    private Point3f targetPoint = new Point3f();

    private static final int TOTAL_CAST_TIME = 1500;
    private long startCastTime;

    private static final int TOTAL_CHARGES = 1;
    private static int remainingCharges = TOTAL_CHARGES;

    private static final int TOTAL_COOLDOWN = 0;
    private static long cooldownTimerStart;

    public GroundExplosion() {
        setAbilityCreationTime(GameTime.getCurrentMillis());
    }

    @Override
    public int getStage() {
        return stage;
    }

    @Override
    public boolean isCasting() {
        return stage == STAGE_CASTING;
    }

    @Override
    public boolean isControlling() {
        return stage == STAGE_CONTROLLING;
    }

    @Override
    public boolean isIndependent() {
        return stage == STAGE_INDEPENDENT;
    }

    @Override
    public long getAbilityCreationTime() {
        return abilityCreationTime;
    }

    @Override
    public void setAbilityCreationTime(long abilityCreationTime) {
        this.abilityCreationTime = abilityCreationTime;
        randomSeededGenerator = new Random(abilityCreationTime);
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
    public int getRemainingCastTime() {
        return (int) Math.max(0, TOTAL_CAST_TIME - (GameTime.getCurrentMillis() - startCastTime));
    }

    @Override
    public int getDeltaCastTime() {
        return (int) (GameTime.getCurrentMillis() - startCastTime);
    }

    @Override
    public long getStartCastTime() {
        return startCastTime;
    }

    @Override
    public void setStartCastTime(long startCastTime) {
        this.startCastTime = startCastTime;
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
        return (float) getRemainingCooldown() / getTotalCooldown();
    }

    @Override
    public void render() {
        if (stage == STAGE_HOLDING) {
            // draw flames on hands idk
        } else if (stage == STAGE_CASTING) {
            // draw bigger flames on hands idk


        }
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
            if (stage == STAGE_HOLDING) {
                Point3f castFromPoint = PlayerManager.getCastFromPoint();
                Vector3f forwardVec = MathUtils.angleToForwardVector(PlayerManager.PLAYER.getAng());
                Point3f castToPoint = new Point3f(castFromPoint.x + forwardVec.x * 10000, castFromPoint.y + forwardVec.y * 10000, castFromPoint.z + forwardVec.z * 10000);
                targetPoint = PhysicsUtils.rayTestLevelForPoint3f(PlayableState.level, castFromPoint, castToPoint);

                if (!targetPoint.equals(castToPoint)) {
                    stage = STAGE_CASTING;
                    if (startCastTime == 0) {
                        startCastTime = GameTime.getCurrentMillis();
                    }
                }
            }

            if (stage == STAGE_CASTING) {
                Point3f castFromPoint = PlayerManager.getCastFromPoint();
                Vector3f vectorToTarget = new Vector3f(targetPoint.x - castFromPoint.x, targetPoint.y - castFromPoint.y, targetPoint.z - castFromPoint.z);
                Vector3f angleToTarget = MathUtils.vectorToAngle(vectorToTarget);
                angleToTarget.x = PlayerManager.PLAYER.getAng().x;
                PlayerManager.PLAYER.getAng().y = angleToTarget.y;
                Physics.lockPlayerAngles();

                int desiredListSize = (int) (GameTime.getCurrentMillis() - startCastTime) / 10;
                for (; particleCount < desiredListSize; particleCount++) {
                    Particle currentParticle = new GroundExplosionFlame();
                    currentParticle.init();
                    currentParticle.getPos().x = targetPoint.x + randomSeededGenerator.nextFloat() * RADIUS - RADIUS / 2;
                    currentParticle.getPos().y = targetPoint.y;
                    currentParticle.getPos().z = targetPoint.z + randomSeededGenerator.nextFloat() * RADIUS - RADIUS / 2;
                    ParticleManager.addParticle(currentParticle);
                }

                if (getRemainingCastTime() == 0) {
                    stage = STAGE_INDEPENDENT;
                }
            }
        } else {
            if (stage == STAGE_CASTING) {
                stage = STAGE_HOLDING;
                startCastTime = 0;
                particleCount = 0;
            }
        }
    }
}
