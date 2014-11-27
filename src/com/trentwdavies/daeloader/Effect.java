package com.trentwdavies.daeloader;

/**
 * Created by Trent on 11/16/2014.
 */
public class Effect {
    public double[] emission;
    public double[] ambient;
    public double[] diffuse;
    public double[] specular;
    public double shininess;
    public double transparency;

    public Effect() {
        emission = new double[4];
        ambient = new double[4];
        diffuse = new double[4];
        specular = new double[4];
        transparency = 1;
    }

    public void setEmission(double[] emission) {
        this.emission = emission;
    }

    public void setAmbient(double[] ambient) {
        this.ambient = ambient;
    }

    public void setDiffuse(double[] diffuse) {
        this.diffuse = diffuse;
    }

    public void setSpecular(double[] specular) {
        this.specular = specular;
    }

    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    public void setTransparency(double transparency) {
        this.transparency = transparency;
    }

    public Effect clone() {
        Effect newEffect = new Effect();

        newEffect.setEmission(emission);
        newEffect.setAmbient(ambient);
        newEffect.setDiffuse(diffuse);
        newEffect.setSpecular(specular);
        newEffect.setShininess(shininess);
        newEffect.setTransparency(transparency);

        return newEffect;
    }

    public String toString() {
        return "emission: " + emission[0] + " " + emission[1] + " " + emission[2] + " " + emission[3]
                + ", specular: " + specular[0] + " " + specular[1] + " " + specular[2] + " " + specular[3];
    }
}
