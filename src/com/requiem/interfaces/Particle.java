package com.requiem.interfaces;

import com.trentwdavies.textureloader.Texture;
import org.lwjgl.util.vector.Vector3f;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

/**
 * Created by Trent on 2/9/2015.
 */
public interface Particle extends Updateable, Initializable {
    public Point3f getPos();
    public void setPos(Point3f pos);

    public Vector3f getVel();
    public void setVel(Vector3f vel);

    public Point2f[] getTexCoords();
    public void setTexCoords(Point2f[] texCoords);

    public long getSpawnTime();
    public void setSpawnTime(long spawnTime);

    public boolean isDead();

    public void render();
}
