package com.requiem.abstractentities;

/**
 * Created by Trent on 10/25/2014.
 */

import com.requiem.interfaces.Initializable;
import com.requiem.interfaces.Updateable;
import com.requiem.managers.SettingsManager;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class GameCamera {
    public static Point3f pos;
    public static Point3f targetPos;
    public static Vector3f ang;
    public static Vector3f vel;

    public static float fieldOfView = 65f;
    public static float zNear = 1f;
    public static float zFar = 1000f;

    public static void init() {
        pos = new Point3f();
        targetPos = new Point3f();
        ang = new Vector3f();
        vel = new Vector3f();
    }

    public static void update() {

    }

    public static void lookAt(Point3f point3f) {
        //TODO can I do this without a square root???
        //TODO use mathutils angle thing
        double xDist = point3f.x - pos.x;
        double yDist = point3f.y - pos.y;
        double zDist = point3f.z - pos.z;

        //TODO quick square root
        double xzPlaneDist = Math.sqrt(xDist * xDist + zDist * zDist);

        ang.x = (float) Math.toDegrees(Math.atan2(yDist, xzPlaneDist));
        ang.y = (float) Math.toDegrees(Math.atan2(-xDist, -zDist));
    }

    public static void lookAt(float x, float y, float z) {
        lookAt(new Point3f(x, y, z));
    }

    public static void recalculatePerspective() {
        int[] resolution = SettingsManager.getResolution();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(fieldOfView, (float) resolution[0] / resolution[1], zNear, zFar);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public static Point3f getPos() {
        return pos;
    }

    public static void setPos(Point3f pos) {
        GameCamera.pos = pos;
    }

    public static Vector3f getVel() {
        return vel;
    }

    public static void setVel(Vector3f vel) {
        GameCamera.vel = vel;
    }

    public static Vector3f getAng() {
        return ang;
    }

    public static void setAng(Vector3f ang) {
        GameCamera.ang = ang;
    }
}
