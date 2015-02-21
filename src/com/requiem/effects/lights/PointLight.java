package com.requiem.effects.lights;

import com.requiem.interfaces.Light;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

/**
 * Created by Trent on 2/19/2015.
 */
public class PointLight implements Light {
    private int lightType;
    private Point4f lightPos;
    private Vector4f lightDir;
    private Vector4f lightDiffuse;
    private Vector4f lightAmbient;
    private Vector4f lightSpecular;

    public PointLight(Point4f lightPos) {
        this(lightPos, new Vector4f(1f, 1f, 1f, 1f), new Vector4f(0.05f, 0.05f, 0.05f, 1f), new Vector4f(1f, 1f, 1f, 1f));
    }

    public PointLight(Point4f lightPos, Vector4f lightDiffuse) {
        this(lightPos, lightDiffuse, new Vector4f(0.05f, 0.05f, 0.05f, 1f), lightDiffuse);
    }

    public PointLight(Point4f lightPos, Vector4f lightDiffuse, Vector4f lightAmbient, Vector4f lightSpecular) {
        lightType = 1;
        this.lightPos = lightPos;
        this.lightDiffuse = lightDiffuse;
        this.lightAmbient = lightAmbient;
        this.lightSpecular = lightSpecular;
    }

    @Override
    public int getLightType() {
        return lightType;
    }

    @Override
    public void setLightType(int lightType) {
        this.lightType = lightType;
    }

    @Override
    public Point4f getLightPos() {
        return lightPos;
    }

    @Override
    public void setLightPos(Point4f lightPos) {
        this.lightPos = lightPos;
    }

    @Override
    public Vector4f getLightDir() {
        return lightDir;
    }

    @Override
    public void setLightDir(Vector4f lightDir) {
        this.lightDir = lightDir;
    }

    @Override
    public Vector4f getLightDiffuse() {
        return lightDiffuse;
    }

    @Override
    public void setLightDiffuse(Vector4f lightDiffuse) {
        this.lightDiffuse = lightDiffuse;
    }

    @Override
    public Vector4f getLightAmbient() {
        return lightAmbient;
    }

    @Override
    public void setLightAmbient(Vector4f lightAmbient) {
        this.lightAmbient = lightAmbient;
    }

    @Override
    public Vector4f getLightSpecular() {
        return lightSpecular;
    }

    @Override
    public void setLightSpecular(Vector4f lightSpecular) {
        this.lightSpecular = lightSpecular;
    }
}
