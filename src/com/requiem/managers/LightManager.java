package com.requiem.managers;

import com.requiem.interfaces.Light;
import org.lwjgl.BufferUtils;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;

/**
 * Created by Trent on 2/19/2015.
 */
public class LightManager {
    public static List<Light> lightList = new LinkedList<Light>();

    public static void addLight(Light light) {
        lightList.add(light);
    }

    public static void addLightsToShader(int shaderProgram) {
        int lightCountLoc = glGetUniformLocation(shaderProgram, "lightCount");
        glUniform1i(lightCountLoc, lightList.size());

        Iterator<Light> lightIterator = lightList.listIterator();
        int lightIndex = 0;
        while (lightIterator.hasNext()) {
            Light curLight = lightIterator.next();

            int lightTypeLoc = glGetUniformLocation(shaderProgram, "lights[" + lightIndex + "].lightType");
            glUniform1i(lightTypeLoc, curLight.getLightType());

            Point4f lightPos = curLight.getLightPos();
            if (lightPos != null) {
                int lightPosLoc = glGetUniformLocation(shaderProgram, "lights[" + lightIndex + "].lightPos");
                glUniform4f(lightPosLoc, lightPos.x, lightPos.y, lightPos.z, lightPos.w);
            }

            Vector4f lightDir = curLight.getLightDir();
            if (lightDir != null) {
                int lightDirLoc = glGetUniformLocation(shaderProgram, "lights[" + lightIndex + "].lightDir");
                glUniform4f(lightDirLoc, lightDir.x, lightDir.y, lightDir.z, lightDir.w);
            }

            Vector4f lightDiffuse = curLight.getLightDiffuse();
            int lightDiffuseLoc = glGetUniformLocation(shaderProgram, "lights[" + lightIndex + "].lightDiffuse");
            glUniform4f(lightDiffuseLoc, lightDiffuse.x, lightDiffuse.y, lightDiffuse.z, lightDiffuse.w);

            Vector4f lightAmbient = curLight.getLightAmbient();
            int lightAmbientLoc = glGetUniformLocation(shaderProgram, "lights[" + lightIndex + "].lightAmbient");
            glUniform4f(lightAmbientLoc, lightAmbient.x, lightAmbient.y, lightAmbient.z, lightAmbient.w);

            Vector4f lightSpecular = curLight.getLightSpecular();
            int lightSpecularLoc = glGetUniformLocation(shaderProgram, "lights[" + lightIndex + "].lightSpecular");
            glUniform4f(lightSpecularLoc, lightSpecular.x, lightSpecular.y, lightSpecular.z, lightSpecular.w);

            lightIndex++;
        }
    }

    public static void removeLight(Light light) {
        lightList.remove(light);
    }
}
