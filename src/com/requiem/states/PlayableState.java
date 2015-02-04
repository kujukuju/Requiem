package com.requiem.states;

import com.requiem.Requiem;
import com.requiem.abstractentities.entities.enemies.Enemy;
import com.requiem.Level;
import com.requiem.abstractentities.entities.enemies.CuteCrab;
import com.requiem.abstractentities.pathfinding.PathLevel;
import com.requiem.interfaces.State;
import com.requiem.logic.AIProcessor;
import com.requiem.logic.Physics;
import com.requiem.managers.EnemyManager;
import com.requiem.managers.PlayerManager;
import com.requiem.utilities.GraphicsUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.Sphere;

import javax.vecmath.Point3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class PlayableState implements State {
    public static final String MODEL_PATH = "assets/levels/pathfinding/pathtest.dae";
    public static final String PATH_MODEL_PATH = "assets/levels/pathfinding/pathtest-path.dae";
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
        for (Enemy enemy : EnemyManager.enemyList) {
            enemy.update();
        }

        Physics.update();
    }

    @Override
    public void render() {
        glPushMatrix();

        glRotated(-Requiem.GAME_CAMERA.ang.x, 1, 0, 0);
        glRotated(-Requiem.GAME_CAMERA.ang.y, 0, 1, 0);
        glRotated(-Requiem.GAME_CAMERA.ang.z, 0, 0, 1);
        glTranslated(-Requiem.GAME_CAMERA.pos.x, -Requiem.GAME_CAMERA.pos.y, -Requiem.GAME_CAMERA.pos.z);

        float[] pos = {-20, 20, -14, 1};
        glLight(GL_LIGHT0, GL_POSITION, GraphicsUtils.flippedFloatBuffer(pos));
        float[] diff = {1f, 1f, 1f, 1};
        glLight(GL_LIGHT0, GL_DIFFUSE, GraphicsUtils.flippedFloatBuffer(diff));
        float[] amb = {0.05f, 0.05f, 0.05f, 1};
        glLight(GL_LIGHT0, GL_AMBIENT, GraphicsUtils.flippedFloatBuffer(amb));

        level.render();
        PlayerManager.PLAYER.render();
        for (Enemy enemy : EnemyManager.enemyList) {
            enemy.render();
        }

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
