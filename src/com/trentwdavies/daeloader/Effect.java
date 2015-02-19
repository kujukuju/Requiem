package com.trentwdavies.daeloader;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created by Trent on 11/16/2014.
 */
public class Effect {
    public FloatBuffer emission;
    public FloatBuffer ambient;
    public FloatBuffer diffuse;
    public FloatBuffer specular;
    public float shininess;
    public float transparency;

    public Effect() {
        emission = BufferUtils.createFloatBuffer(4);
        ambient = BufferUtils.createFloatBuffer(4);
        diffuse = BufferUtils.createFloatBuffer(4);
        specular = BufferUtils.createFloatBuffer(4);
        transparency = 1;
    }

    public void setEmission(float[] emission) {
        this.emission.put(emission);
        this.emission.flip();
    }

    public void setAmbient(float[] ambient) {
        this.ambient.put(ambient);
        this.ambient.flip();
    }

    public void setDiffuse(float[] diffuse) {
        this.diffuse.put(diffuse);
        this.diffuse.flip();
    }

    public void setSpecular(float[] specular) {
        this.specular.put(specular);
        this.specular.flip();
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    public Effect clone() {
        Effect newEffect = new Effect();

        newEffect.setEmission(emission.array());
        newEffect.setAmbient(ambient.array());
        newEffect.setDiffuse(diffuse.array());
        newEffect.setSpecular(specular.array());
        newEffect.setShininess(shininess);
        newEffect.setTransparency(transparency);

        return newEffect;
    }

    public String toString() {
        return "emission: " + emission.array()[0] + " " + emission.array()[1] + " " + emission.array()[2] + " " + emission.array()[3]
                + ", specular: " + specular.array()[0] + " " + specular.array()[1] + " " + specular.array()[2] + " " + specular.array()[3];
    }
}
