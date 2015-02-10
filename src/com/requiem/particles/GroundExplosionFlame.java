package com.requiem.particles;

import com.requiem.interfaces.Particle;
import com.requiem.utilities.GameTime;
import com.trentwdavies.textureloader.Texture;
import org.lwjgl.util.vector.Vector3f;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

/**
 * Created by Trent on 2/9/2015.
 */
public class GroundExplosionFlame implements Particle {
    public static final String SPRITE_SHEET_PATH = "assets/images/abilities/ground-explosion/fire-particles.png";
    public static Texture spriteSheet;

    private Point3f pos;
    private Vector3f vel;

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
        return 0;
    }

    @Override
    public void setSpawnTime(long spawnTime) {

    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public void render() {

    }

    @Override
    public void init() {
        pos = new Point3f();
        vel = new Vector3f();
        spawnTime = GameTime.getCurrentMillis();

        init = true;
    }

    @Override
    public void update() {
        if (!init)
            init();


    }
}
