package com.requiem.utilities;

import javax.vecmath.Matrix4d;

/**
 * Created by Trent on 11/24/2014.
 */
public class MatrixUtils {
    public static Matrix4d mult(Matrix4d baseMatrix, Matrix4d multMatrix) {
        Matrix4d returnMatrix = new Matrix4d();

        returnMatrix.m00 = baseMatrix.m00 * multMatrix.m03;
        returnMatrix.m01 = baseMatrix.m01 * multMatrix.m13;
        returnMatrix.m02 = baseMatrix.m02 * multMatrix.m23;
        returnMatrix.m03 = baseMatrix.m03 * multMatrix.m33;

        returnMatrix.m10 = baseMatrix.m10 * multMatrix.m02;
        returnMatrix.m11 = baseMatrix.m11 * multMatrix.m12;
        returnMatrix.m12 = baseMatrix.m12 * multMatrix.m22;
        returnMatrix.m13 = baseMatrix.m13 * multMatrix.m32;

        returnMatrix.m20 = baseMatrix.m20 * multMatrix.m01;
        returnMatrix.m21 = baseMatrix.m21 * multMatrix.m11;
        returnMatrix.m22 = baseMatrix.m22 * multMatrix.m21;
        returnMatrix.m23 = baseMatrix.m23 * multMatrix.m31;

        returnMatrix.m30 = baseMatrix.m30 * multMatrix.m00;
        returnMatrix.m31 = baseMatrix.m31 * multMatrix.m10;
        returnMatrix.m32 = baseMatrix.m32 * multMatrix.m20;
        returnMatrix.m33 = baseMatrix.m33 * multMatrix.m30;

        return returnMatrix;
    }

    public static double[] mult(Matrix4d baseMatrix, double[] tempMultMatrix) {
        if (tempMultMatrix.length != 4 && tempMultMatrix.length != 3) {
            throw new ArrayIndexOutOfBoundsException("The matrix mult (double[]) methods input does not have a length of 4.");
        }

        double[] multMatrix = new double[4];
        if (tempMultMatrix.length == 3) {
            multMatrix[0] = tempMultMatrix[0];
            multMatrix[1] = tempMultMatrix[1];
            multMatrix[2] = tempMultMatrix[2];
            multMatrix[3] = 1;
        } else {
            multMatrix = tempMultMatrix;
        }

        double[] returnArray = new double[4];

        returnArray[0] = baseMatrix.m00 * multMatrix[0] + baseMatrix.m01 * multMatrix[1] + baseMatrix.m02 * multMatrix[2] + baseMatrix.m03 * 1;//x
        returnArray[1] = baseMatrix.m10 * multMatrix[0] + baseMatrix.m11 * multMatrix[1] + baseMatrix.m12 * multMatrix[2] + baseMatrix.m13 * 1;//y
        returnArray[2] = baseMatrix.m20 * multMatrix[0] + baseMatrix.m21 * multMatrix[1] + baseMatrix.m22 * multMatrix[2] + baseMatrix.m23 * 1;//z
        returnArray[3] = 1;

        return returnArray;
    }
}
