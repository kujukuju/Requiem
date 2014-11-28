package com.requiem.states;

import com.requiem.Requiem;
import com.requiem.abstractentities.entities.Level;
import com.requiem.interfaces.State;
import com.requiem.logic.Physics;
import com.requiem.managers.PlayerManager;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class PlayableState implements State {
    public static final String LEVEL_FILE_PATH = "assets/levels/snow-correct.dae";
    public static Level level;

    private boolean init;

    @Override
    public void init() {
        Mouse.setGrabbed(true);

        level = new Level(LEVEL_FILE_PATH);

        init = true;
    }

    @Override
    public void update() {
        if (!init)
            init();

        level.update();
        PlayerManager.PLAYER.update();
        Physics.update();
    }

    @Override
    public void render() {
        glPushMatrix();

        glRotated(-Requiem.GAME_CAMERA.ang.x, 1, 0, 0);
        glRotated(-Requiem.GAME_CAMERA.ang.y, 0, 1, 0);
        glRotated(-Requiem.GAME_CAMERA.ang.z, 0, 0, 1);
        glTranslated(-Requiem.GAME_CAMERA.pos.x, -Requiem.GAME_CAMERA.pos.y, -Requiem.GAME_CAMERA.pos.z);

        level.render();
        PlayerManager.PLAYER.render();

        glPopMatrix();
    }
}