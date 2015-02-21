package com.requiem.particles;

import com.requiem.Requiem;
import com.requiem.abstractentities.GameCamera;
import com.requiem.interfaces.Particle;
import com.requiem.managers.ParticleManager;
import com.requiem.utilities.FastRandom;
import com.requiem.utilities.GameTime;
import com.requiem.utilities.MathUtils;
import com.trentwdavies.textureloader.Texture;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 2/9/2015.
 */
public class GroundExplosionFlame implements Particle {
    public static final String SPRITE_SHEET_PATH = "assets/images/abilities/ground-explosion/fire-particles-2.png";
    public static Texture spriteSheet;

    private static final int[] BLEND_FUNC = {GL_SRC_ALPHA, GL_ONE};

    private static final float WIDTH = 0.4f;
    private static final int ANIM_LENGTH = 16;
    private static final int ANIM_HEIGHT = 4;

    private int animHeight;

    private Point3f pos;
    private Vector3f vel;

    private boolean isDead;

    private boolean willMakeSmoke;

    private Point2f[] texCoords;

    private long spawnTime;

    private static final int DEFAULT_LIFE_SPAN = 600;
    private static final int DEFAULT_LIFE_SPAN_VARIANCE = 200;
    private int lifeSpan;

    private boolean init;

    @Override
    public Point3f getPos() {
        return pos;
    }

    @Override
    public void setPos(Point3f pos) {
        this.pos = pos;
    }

    @Override
    public Vector3f getVel() {
        return vel;
    }

    @Override
    public void setVel(Vector3f vel) {
        this.vel = vel;
    }

    @Override
    public Point2f[] getTexCoords() {
        return texCoords;
    }

    @Override
    public void setTexCoords(Point2f[] texCoords) {
        this.texCoords = texCoords;
    }

    @Override
    public long getSpawnTime() {
        return spawnTime;
    }

    @Override
    public void setSpawnTime(long spawnTime) {
        this.spawnTime = spawnTime;
    }

    @Override
    public int getDefaultLifeSpan() {
        return DEFAULT_LIFE_SPAN;
    }

    @Override
    public int getDefaultLifeSpanVariance() {
        return DEFAULT_LIFE_SPAN_VARIANCE;
    }

    @Override
    public int getLifeSpan() {
        return lifeSpan;
    }

    @Override
    public void setLifeSpan(int lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public Texture getTexture() {
        return spriteSheet;
    }

    @Override
    public int[] getBlendFunc() {
        return BLEND_FUNC;
    }

    @Override
    public void render() {
        renderFacingCamera(pos, texCoords);
    }

    public void renderFacingCamera(Point3f pos, Point2f[] texCoords) {
        Point3f camPos = GameCamera.pos;
        Vector3f angleToCamera = MathUtils.vectorToAngle(new Vector3f(camPos.x - pos.x, camPos.y - pos.y, camPos.z - pos.z));
        Vector3f upVector = MathUtils.angleToUpVector(angleToCamera);
        upVector.scale(WIDTH);
        Vector3f rightVector = MathUtils.angleToRightVector(angleToCamera);
        rightVector.scale(WIDTH);

        glBegin(GL_QUADS);
            //top right
            glTexCoord2f(texCoords[1].x, texCoords[0].y);
            glVertex3f(pos.x + upVector.x + rightVector.x, pos.y + upVector.y + rightVector.y, pos.z + upVector.z + rightVector.z);
            //top left
            glTexCoord2f(texCoords[0].x, texCoords[0].y);
            glVertex3f(pos.x + upVector.x - rightVector.x, pos.y + upVector.y - rightVector.y, pos.z + upVector.z - rightVector.z);
            //bottom left
            glTexCoord2f(texCoords[0].x, texCoords[1].y);
            glVertex3f(pos.x - upVector.x - rightVector.x, pos.y - upVector.y - rightVector.y, pos.z - upVector.z - rightVector.z);
            //bottom right
            glTexCoord2f(texCoords[1].x, texCoords[1].y);
            glVertex3f(pos.x - upVector.x + rightVector.x, pos.y - upVector.y + rightVector.y, pos.z - upVector.z + rightVector.z);
        glEnd();
    }

    @Override
    public void init() {
        pos = new Point3f();
        vel = new Vector3f();
        spawnTime = GameTime.getCurrentMillis();
        lifeSpan = (int) (DEFAULT_LIFE_SPAN + DEFAULT_LIFE_SPAN_VARIANCE * (FastRandom.random.nextFloat() * 2 - 1));

        willMakeSmoke = FastRandom.random.nextFloat() < 0.05;
        animHeight = FastRandom.random.nextInt(ANIM_HEIGHT);

        texCoords = new Point2f[2];
        texCoords[0] = new Point2f(0, 0);
        texCoords[1] = new Point2f(1f / 16, 1);

        init = true;
    }

    @Override
    public void update() {
        if (!init)
            init();

        int lifeStage = (int) ((GameTime.getCurrentMillis() - spawnTime) * ANIM_LENGTH / lifeSpan);
        texCoords[0] = new Point2f(lifeStage * 1f / ANIM_LENGTH, (float) animHeight / ANIM_HEIGHT);
        texCoords[1] = new Point2f((lifeStage + 1) * 1f / ANIM_LENGTH, (float) (animHeight + 1) / ANIM_HEIGHT);

        /*if (willMakeSmoke && lifeStage >= 2 && FastRandom.random.nextFloat() < 0.002 * GameTime.getDeltaTime()) {
            Particle currentParticle = new SmokeCloud();
            currentParticle.init();
            currentParticle.getPos().x = pos.x;
            currentParticle.getPos().y = pos.y + 0.5f;
            currentParticle.getPos().z = pos.z;
            currentParticle.getVel().x = vel.x;
            currentParticle.getVel().y = vel.y + 0.0025f;
            currentParticle.getVel().z = vel.z;
            ParticleManager.addParticle(currentParticle);

            willMakeSmoke = false;
        }*/

        vel.y += 0.000009f * GameTime.getDeltaTime();
        pos.y += vel.y;

        if (lifeStage == 16) {
            isDead = true;
        }
    }
}
