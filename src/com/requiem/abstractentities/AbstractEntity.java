package com.requiem.abstractentities;

import com.requiem.interfaces.Updateable;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Created by Trent on 10/24/2014.
 */
public class AbstractEntity implements Updateable {
    public Point3d pos;
    public Vector3d vel;
    public Vector3d ang;

    @Override
    public void update() {

    }
}
