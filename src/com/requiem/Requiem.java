package com.requiem;

import com.requiem.abilities.GroundExplosion;
import com.requiem.abstractentities.GameCamera;
import com.requiem.abstractentities.entities.Player;
import com.requiem.abstractentities.entities.enemies.CuteCrab;
import com.requiem.effects.lights.DirectionalLight;
import com.requiem.effects.lights.PointLight;
import com.requiem.effects.lights.SpotLight;
import com.requiem.interfaces.Light;
import com.requiem.interfaces.State;
import com.requiem.listeners.GameInput;
import com.requiem.managers.*;
import com.requiem.particles.GroundExplosionFlame;
import com.requiem.particles.SmokeCloud;
import com.requiem.states.PlayableState;
import com.requiem.states.TitleScreenState;
import com.requiem.utilities.AssetManager;
import com.requiem.utilities.GameTime;
import com.trentwdavies.daeloader.Model;
import com.trentwdavies.textureloader.Texture;
import org.lwjgl.opengl.Display;

import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Requiem {
    public static boolean running;

    public Requiem() {
        SettingsManager.loadSettings();
        updateResolution();
        loadLoadingScreen();

        init();
        loadStuff();

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

    private void loadLoadingScreen() {
        //Globals.ASSET_MANAGER.queue("images/loading-screen.png", Texture.class);
        //Globals.ASSET_MANAGER.queue("images/white-pixel.png", Texture.class);
        //Globals.ASSET_MANAGER.finishLoading();
    }

    public void loadStuff() {
        AssetManager.queue(TitleScreenState.LEVEL_FILE_PATH, Model.class);
        AssetManager.queue(PlayableState.MODEL_PATH, Model.class);
        AssetManager.queue(PlayableState.PATH_MODEL_PATH, Model.class);
        AssetManager.queue(Player.MODEL_PATH, Model.class);
        AssetManager.queue(CuteCrab.MODEL_PATH, Model.class);

        AssetManager.queue(AbilityManager.ABILITY_ICON_TEXTURES_PATH, Texture.class);
        AssetManager.queue(GroundExplosionFlame.SPRITE_SHEET_PATH, Texture.class);
        AssetManager.queue(SmokeCloud.SPRITE_SHEET_PATH, Texture.class);

        AssetManager.load();
        AssetManager.pauseWhileLoading();

        ShaderManager.loadShaders();
    }

    public void init() {
        //StateManager.setState(StateManager.STATE_TITLE_SCREEN);
        StateManager.setState(StateManager.STATE_PLAYABLE);

        //TODO this shouldnt be in init
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_POLYGON_SMOOTH);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

        glEnable(GL_DEPTH_TEST);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);

        GameCamera.init();
        FontManager.init();

        Light dirLight = new DirectionalLight(new Vector4f(-0.57735f, 0.57735f, -0.57735f, 0f), new Vector4f(1 / 2f, 0.9f / 2f, 0.8f / 2f, 1));
        LightManager.addLight(dirLight);
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
