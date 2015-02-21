package com.requiem.interfaces;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * Created by Trent on 2/19/2015.
 */
public interface Light {
    public int getLightType();
    public void setLightType(int lightType);

    public Point4f getLightPos();
    public void setLightPos(Point4f lightPos);

    public Vector4f getLightDir();
    public void setLightDir(Vector4f lightDir);

    public Vector4f getLightDiffuse();
    public void setLightDiffuse(Vector4f lightDiffuse);

    public Vector4f getLightAmbient();
    public void setLightAmbient(Vector4f lightAmbient);

    public Vector4f getLightSpecular();
    public void setLightSpecular(Vector4f lightSpecular);
}
