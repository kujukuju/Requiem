package com.requiem.particles;

import com.requiem.Requiem;
import com.requiem.abstractentities.GameCamera;
import com.requiem.interfaces.Particle;
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
    public static final String SPRITE_SHEET_PATH = "assets/images/abilities/ground-explosion/fire-particles.png";
    public static Texture spriteSheet;

    private static final float WIDTH = 0.5f;

    private Point3f pos;
    private Vector3f vel;

    private boolean isDead;

    private Point2f[] texCoords;

    private long spawnTime;

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
    public boolean isDead() {
        return isDead;
    }

    @Override
    public void render() {
        renderFacingCamera(pos, texCoords);
    }

    public void renderFacingCamera(Point3f pos, Point2f[] texCoords) {
        Point3f camPos = Requiem.GAME_CAMERA.pos;
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

        texCoords = new Point2f[2];
        texCoords[0] = new Point2f(0, 0);
        texCoords[1] = new Point2f(1f, 1f);

        init = true;
    }

    @Override
    public void update() {
        if (!init)
            init();

        vel.y += 0.000005f * GameTime.getDeltaTime();
        pos.y += vel.y;

        if (GameTime.getCurrentMillis() - spawnTime > 1000) {
            isDead = true;
        }
    }
}
