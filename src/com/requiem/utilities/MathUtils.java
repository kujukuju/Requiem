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

    public static float calculatePlaneY(Point2f point, Point3f planePoint, Vector3f gradient) {
        if (gradient.y == 0) {
            return Float.NaN;
        }

        //move y to the other side and divide by y coefficient
        gradient.scale(-1.0f / gradient.y);
        float deltaX = point.x - planePoint.x;
        float deltaZ = point.y - planePoint.z;
        float deltaY = gradient.x * deltaX + gradient.z * deltaZ;

        return planePoint.y + deltaY;
    }

    //TODO not quick yet lol
    public static float quickLength(Point3f from, Point3f to) {
        return MathUtils.quickLength(from.x, from.y, from.z, to.x, to.y, to.z);
    }

    //TODO not quick yet lol
    public static float quickLength(float x1, float y1, float z1, float x2, float y2, float z2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
    }

    //TODO walls that are like 160 degrees might be treated as 20
    public static float calculateSteepnessAngle(Point3f from, Point3f to) {
        float xzLength = quickLength(new Point3f(from.x, 0, from.z), new Point3f(to.x, 0, to.z));
        float yLength = to.y - from.y;

        float angle = (float) Math.atan2(yLength, xzLength);
        if (Math.abs(angle) > 90) {
            angle = (float) (Math.signum(angle) * Math.PI - angle);
        }
        return angle;
    }

    public static Vector3f angleToVector(Vector3f angle) {
        Vector3f returnVec = new Vector3f();

        returnVec.x = (float) Math.sin(Math.toRadians(angle.y));
        returnVec.y = (float) -Math.sin(Math.toRadians(angle.x));
        returnVec.z = (float) -Math.cos(Math.toRadians(angle.y));

        return returnVec;
    }

    public static Vector3f vectorToAngle(Vector3f vector) {
        Vector3f returnAng = new Vector3f();

        float xzPlaneDist = MathUtils.quickLength(0, 0, 0, vector.x, 0, vector.z);
        returnAng.x = (float) Math.toDegrees(Math.atan2(vector.y, xzPlaneDist));
        returnAng.y = (float) Math.toDegrees(Math.atan2(-vector.x, -vector.z));
        returnAng.z = 0;
        //TODO idk how vectors can roll lol rofl

        return returnAng;
    }
}
