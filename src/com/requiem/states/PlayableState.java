package com.requiem.states;

import com.requiem.Requiem;
import com.requiem.abstractentities.entities.enemies.Enemy;
import com.requiem.Level;
import com.requiem.abstractentities.entities.enemies.CuteCrab;
import com.requiem.abstractentities.pathfinding.PathLevel;
import com.requiem.interfaces.State;
import com.requiem.logic.Physics;
import com.requiem.managers.EnemyManager;
import com.requiem.managers.PlayerManager;
import com.requiem.utilities.GraphicsUtils;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;

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

        changeLevel(new Level(MODEL_PATH));
        changePathLevel(new PathLevel(PATH_MODEL_PATH));
        Physics.setCurrentLevel(level);

        EnemyManager.enemyList.add(new CuteCrab());

        PlayerManager.PLAYER.init();
        for (Enemy enemy : EnemyManager.enemyList) {
            enemy.init();
        }

        level.init();

        init = true;
    }

    public void changeLevel(Level newLevel) {
        this.level = newLevel;
    }

    public void changePathLevel(PathLevel newPathlevel) {
        this.pathLevel = newPathlevel;
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
