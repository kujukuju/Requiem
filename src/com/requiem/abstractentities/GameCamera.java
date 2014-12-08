package com.requiem.abstractentities;

/**
 * Created by Trent on 10/25/2014.
 */

import com.requiem.managers.SettingsManager;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class GameCamera extends AbstractEntity {
    public Point3d pos = new Point3d();
    public Point3d targetPos = new Point3d();
    public Vector3d ang = new Vector3d();
    public Vector3d vel = new Vector3d();

    public static float fieldOfView = 65f;
    public static float zNear = 1f;
    public static float zFar = 1000f;

    public void lookAt(Point3d point3d) {
        //TODO can I do this without a square root???
        double xDist = point3d.x - pos.x;
        double yDist = point3d.y - pos.y;
        double zDist = point3d.z - pos.z;

        double xzPlaneDist = Math.sqrt(xDist * xDist + zDist * zDist);

        ang.x = Math.toDegrees(Math.atan2(yDist, xzPlaneDist));
        ang.y = Math.toDegrees(Math.atan2(-xDist, -zDist));
    }

    public void lookAt(double x, double y, double z) {
        lookAt(new Point3d(x, y, z));
    }

    public static void recalculatePerspective() {
        int[] resolution = SettingsManager.getResolution();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(fieldOfView, (float) resolution[0] / resolution[1], zNear, zFar);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    @Override
    public void update() {

    }
}
