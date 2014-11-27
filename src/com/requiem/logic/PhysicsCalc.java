package com.requiem.logic;

import javax.vecmath.Vector3d;

/**
 * Created by Trent on 10/27/2014.
 */
public class PhysicsCalc {
    public static double getLengthSquared(Vector3d vec) {
        return vec.x * vec.x + vec.y * vec.y + vec.z * vec.z;
    }
}
