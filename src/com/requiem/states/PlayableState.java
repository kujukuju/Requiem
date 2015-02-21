package com.requiem.states;

import com.requiem.abstractentities.GameCamera;
import com.requiem.abstractentities.entities.enemies.Enemy;
import com.requiem.Level;
import com.requiem.abstractentities.entities.enemies.CuteCrab;
import com.requiem.abstractentities.pathfinding.PathLevel;
import com.requiem.interfaces.State;
import com.requiem.logic.AIProcessor;
import com.requiem.logic.Physics;
import com.requiem.managers.*;
import com.requiem.overlays.ActionBar;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;

import javax.vecmath.Point3f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class PlayableState implements State {
    public static final String MODEL_PATH = "assets/levels/stadium.dae";
    public static final String PATH_MODEL_PATH = "assets/levels/stadium.dae";
    public static Level level;
    public static PathLevel pathLevel;

    private boolean init;

    @Override
    public void init() {
        Mouse.setGrabbed(true);

        changeLevel(new Level(MODEL_PATH), new PathLevel(PATH_MODEL_PATH));
        Physics.updateCurrentLevel();

        EnemyManager.enemyList.add(new CuteCrab());

        PlayerManager.PLAYER.init();
        for (Enemy enemy : EnemyManager.enemyList) {
            enemy.init();
        }

        level.init();

        AbilityManager.init();
        ActionBar.init();

        init = true;
    }

    public void changeLevel(Level newLevel, PathLevel newPathLevel) {
        this.level = newLevel;
        this.pathLevel = newPathLevel;
    }

    @Override
    public void update() {
        if (!init)
            init();

        PlayerManager.PLAYER.update();
        AbilityManager.update();
        ParticleManager.update();
        EnemyManager.update();

        Physics.update();

        ActionBar.update();
    }

    @Override
    public void render() {
        glPushMatrix();

        glRotated(-GameCamera.ang.x, 1, 0, 0);
        glRotated(-GameCamera.ang.y, 0, 1, 0);
        glRotated(-GameCamera.ang.z, 0, 0, 1);
        glTranslated(-GameCamera.pos.x, -GameCamera.pos.y, -GameCamera.pos.z);

        ShaderManager.perFragLightingFlatShader.use();

        int lightModelViewMatrixLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightModelViewMatrix");
        FloatBuffer lightModelViewMatrix = BufferUtils.createFloatBuffer(16);
        glGetFloat(GL_MODELVIEW_MATRIX, lightModelViewMatrix);
        glUniformMatrix4(lightModelViewMatrixLoc, false, lightModelViewMatrix);

        LightManager.addLightsToShader(ShaderManager.perFragLightingFlatShader.shaderProgram);

        /*int lightTypeLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightType");
        glUniform1i(lightTypeLoc, 1);
        int lightModelViewMatrixLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightModelViewMatrix");
        FloatBuffer lightModelViewMatrix = BufferUtils.createFloatBuffer(16);
        glGetFloat(GL_MODELVIEW_MATRIX, lightModelViewMatrix);
        glUniformMatrix4(lightModelViewMatrixLoc, false, lightModelViewMatrix);
        int lightPosLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightPos");
        glUniform3f(lightPosLoc, 1, 2, 1);
        int lightDiffuseLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightDiffuse");
        glUniform4f(lightDiffuseLoc, 0.5f, 0.4f, 0.3f, 1f);
        int lightAmbientLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightAmbient");
        glUniform4f(lightAmbientLoc, 0.05f, 0.05f, 0.05f, 1f);
        int lightSpecularLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightSpecular");
        glUniform4f(lightSpecularLoc, 1f, 1f, 1f, 1f);*/

        /*int lightTypeLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightType");
        glUniform1i(lightTypeLoc, 2);
        int lightModelViewMatrixLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightModelViewMatrix");
        FloatBuffer lightModelViewMatrix = BufferUtils.createFloatBuffer(16);
        glGetFloat(GL_MODELVIEW_MATRIX, lightModelViewMatrix);
        glUniformMatrix4(lightModelViewMatrixLoc, false, lightModelViewMatrix);
        int lightPosLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightPos");
        glUniform3f(lightPosLoc, 1, 8, 1);
        int lightDirLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightDir");
        glUniform3f(lightDirLoc, 0, -1, 0);
        int lightDiffuseLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightDiffuse");
        glUniform4f(lightDiffuseLoc, 1f, 1f, 1f, 1f);
        int lightAmbientLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightAmbient");
        glUniform4f(lightAmbientLoc, 0.05f, 0.05f, 0.05f, 1f);
        int lightSpecularLoc = glGetUniformLocation(ShaderManager.perFragLightingFlatShader.shaderProgram, "lightSpecular");
        glUniform4f(lightSpecularLoc, 1f, 1f, 1f, 1f);*/

        level.render();
        //PlayerManager.PLAYER.render();
        EnemyManager.renderEnemies();
        ShaderManager.useNoShader();
        AbilityManager.renderAbilities();
        ParticleManager.renderParticles();



glLineWidth(0.2f);
glColor4f(1, 0, 0, 1);
if (AIProcessor.spherePoints.size() > 0) {
    glBegin(GL_LINES);
    glVertex3f(EnemyManager.enemyList.get(0).getPos().x, EnemyManager.enemyList.get(0).getPos().y, EnemyManager.enemyList.get(0).getPos().z);
    glVertex3f(AIProcessor.spherePoints.get(0).x, AIProcessor.spherePoints.get(0).y, AIProcessor.spherePoints.get(0).z);
    glEnd();
}
for (int i = 0; i < AIProcessor.spherePoints.size() - 1; i++) {
    Point3f point1 = AIProcessor.spherePoints.get(i);
    Point3f point2 = AIProcessor.spherePoints.get(i + 1);
    glBegin(GL_LINES);
    glVertex3f(point1.x, point1.y, point1.z);
    glVertex3f(point2.x, point2.y, point2.z);
    glEnd();
}

        glPopMatrix();

        renderOrthographic();
    }

    public void renderOrthographic() {
        ActionBar.render();
    }

    @Override
    public String getModelPath() {
        return MODEL_PATH;
    }

    @Override
    public void setModelPath(String path) {
        //final
    }
}
