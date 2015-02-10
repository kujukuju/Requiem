package com.requiem.particles;

import com.requiem.interfaces.Particle;
import com.requiem.utilities.GameTime;
import com.trentwdavies.textureloader.Texture;
import org.lwjgl.util.vector.Vector3f;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 2/9/2015.
 */
public class GroundExplosionFlame implements Particle {
    public static final String SPRITE_SHEET_PATH = "assets/images/abilities/ground-explosion/fire-particles.png";
    public static Texture spriteSheet;

    private static final float WIDTH = 0.1f;

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
        glBegin(GL_QUADS);
            glTexCoord2f(texCoords[0].x, texCoords[0].y);
            glVertex3f(pos.x - WIDTH, pos.y - WIDTH, pos.z);
            glTexCoord2f(texCoords[1].x, texCoords[0].y);
            glVertex3f(pos.x + WIDTH, pos.y - WIDTH, pos.z);
            glTexCoord2f(texCoords[1].x, texCoords[1].y);
            glVertex3f(pos.x + WIDTH, pos.y + WIDTH, pos.z);
            glTexCoord2f(texCoords[0].x, texCoords[1].y);
            glVertex3f(pos.x - WIDTH, pos.y + WIDTH, pos.z);
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
