package com.requiem;

import com.requiem.abstractentities.GameCamera;
import com.requiem.abstractentities.entities.Player;
import com.requiem.interfaces.Renderable;
import com.requiem.interfaces.State;
import com.requiem.interfaces.Updateable;
import com.requiem.listeners.GameInput;
import com.requiem.managers.FontManager;
import com.requiem.managers.SettingsManager;
import com.requiem.managers.StateManager;
import com.requiem.states.PlayableState;
import com.requiem.states.TitleScreenState;
import com.requiem.utilities.AssetManager;
import com.requiem.utilities.GameTime;
import com.requiem.utilities.GraphicsUtils;
import com.trentwdavies.daeloader.Model;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Requiem {
    public static final GameCamera GAME_CAMERA = new GameCamera();

    public static boolean running;

    public Requiem() {
        SettingsManager.loadSettings();
        int[] resolution = SettingsManager.getResolution();
        updateResolution();
        loadLoadScreen();

        loadStuff();
        init();

        //Gdx.input.setInputProcessor(gameInputProcessor);
        //Gdx.input.setCatchBackKey(true);

        running = true;
        while (running) {
            render();

            Display.update();

            if (Display.isCloseRequested()) {
                running = false;
            }
        }
    }

    private void loadLoadScreen() {
        //Globals.ASSET_MANAGER.queue("images/loading-screen.png", Texture.class);
        //Globals.ASSET_MANAGER.queue("images/white-pixel.png", Texture.class);
        //Globals.ASSET_MANAGER.finishLoading();
    }

    public void loadStuff() {
        AssetManager.queue(TitleScreenState.LEVEL_FILE_PATH, Model.class);
        AssetManager.queue(PlayableState.LEVEL_FILE_PATH, Model.class);
        AssetManager.queue(Player.PLAYER_MODEL_FILE_PATH, Model.class);
        AssetManager.load();
        AssetManager.pauseWhileLoading();
    }

    public void init() {
        int[] resolution = SettingsManager.getResolution();
        //StateManager.setState(StateManager.STATE_TITLE_SCREEN);
        StateManager.setState(StateManager.STATE_PLAYABLE);

        //TODO this shouldnt be in init
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_POLYGON_SMOOTH);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

        glShadeModel(GL_FLAT);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);

        glEnable(GL_NORMALIZE);

        float[] pos = {-20, 20, -14, 1};
        glLight(GL_LIGHT0, GL_POSITION, GraphicsUtils.flippedFloatBuffer(pos));
        float[] diff = {1f, 1f, 1f, 1};
        glLight(GL_LIGHT0, GL_DIFFUSE, GraphicsUtils.flippedFloatBuffer(diff));
        float[] amb = {0.05f, 0.05f, 0.05f, 1};
        glLight(GL_LIGHT0, GL_AMBIENT, GraphicsUtils.flippedFloatBuffer(amb));

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);


        FontManager.init();
    }

    public void update(State currentState) {
        currentState.update();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        /*if (!Globals.ASSET_MANAGER.update()) {
            State currentState = StateManager.STATES[StateManager.STATE_LOADING_SCREEN];

            ((Updateable) currentState).update();
            ((Renderable) currentState).render();

            return;
        }*/

        updateStatics();

        //get the states before you update and render, because in an update call the state would change then it would render the new state before it updated
        //this would cause errors because the init() method was never ran
        State currentState = StateManager.getCurrentState();

        update(currentState);
        currentState.render();
    }

    public void updateStatics() {
        GameTime.update();
        GameInput.update();
    }

    public void updateResolution() {
        int[] resolution = SettingsManager.getResolution();
        System.out.println("resolution changed: " + resolution[0] + ", " + resolution[1]);

        GameCamera.recalculatePerspective();
        FontManager.resize();
        TitleScreenState.resize();
    }

    public void resize(int width, int height) {
        System.out.println("resize: " + width + " - " + height);
        //GameInputProcessor.resize();
    }

    //TODO fix this
    public void dispose() {
        SettingsManager.writeSettings();
    }
}
