package com.requiem.abstractentities;

import com.requiem.interfaces.Initializable;
import com.requiem.interfaces.Updateable;
import com.sun.org.apache.xml.internal.security.Init;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * Created by Trent on 10/24/2014.
 */
public interface AbstractEntity extends Initializable, Updateable {
    public Point3f getPos();
    public void setPos(Point3f pos);
    public Vector3f getVel();
    public void setVel(Vector3f vel);
    public Vector3f getAng();
    public void setAng(Vector3f ang);

}
