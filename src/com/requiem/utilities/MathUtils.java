package com.requiem.utilities;

import javax.vecmath.*;

/**
 * Created by Trent on 1/31/2015.
 */
public class MathUtils {
    //TODO to optomize just set line12 to .cross(line12, lin32);
    //TODO calculate the plane of best fit instead of using only 3 points!
    public static Vector3f calculatePlaneGradient(Point3f point1, Point3f point2, Point3f point3) {
        Vector3f line12 = new Vector3f(point1.x - point2.x, point1.y - point2.y, point1.z - point2.z);
        Vector3f line32 = new Vector3f(point3.x - point2.x, point3.y - point2.y, point3.z - point2.z);

        Vector3f crossProduct = new Vector3f();
        crossProduct.cross(line12, line32);

        return crossProduct;
    }

    public static double calculatePlaneY(Point2f point, Point3f planePoint, Vector3f gradient) {
        if (gradient.y == 0) {
            return Double.NaN;
        }

        //move y to the other side and divide by y coefficient
        gradient.scale(-1.0f / gradient.y);
        double deltaX = point.x - planePoint.x;
        double deltaZ = point.y - planePoint.z;
        double deltaY = gradient.x * deltaX + gradient.z * deltaZ;

        return planePoint.y + deltaY;
    }

    //TODO not quick yet lol
    public static double quickLength(Point3f from, Point3f to) {
        return Math.sqrt((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y) + (to.z - from.z) * (to.z - from.z));
    }

    public static double calculateSteepnessAngle(Point3f from, Point3f to) {
        double xzLength = quickLength(new Point3f(from.x, 0, from.z), new Point3f(to.x, 0, to.z));
        double yLength = to.y - from.y;

        double angle = Math.atan2(yLength, xzLength);
        if (Math.abs(angle) > 90) {
            angle = Math.signum(angle) * Math.PI - angle;
        }
        return angle;
    }
}
