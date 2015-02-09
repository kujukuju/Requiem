package com.requiem.states;

import com.requiem.Requiem;
import com.requiem.Level;
import com.requiem.interfaces.State;
import com.requiem.listeners.GameInput;
import com.requiem.managers.FontManager;
import com.requiem.managers.SettingsManager;
import com.requiem.managers.StateManager;
import com.requiem.utilities.GraphicsUtils;

import javax.vecmath.Point3d;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class TitleScreenState implements State {
    public static final String LEVEL_FILE_PATH = "assets/levels/snow-correct.dae";
    public static final Point3d CAM_POS = new Point3d(2, 9, -14);
    public static Level level;

    private static final String[] menuOptions = {
            "Play",
            "Idk",
            "Other stuff",
            "Customize"
    };

    private static float[][] xyWidthHeight = new float[menuOptions.length][4];
    private static final int X = 0;
    private static final int Y = 1;
    private static final int WIDTH = 2;
    private static final int HEIGHT = 3;

    //private static FrameBuffer[] menuFrameBuffers = new FrameBuffer[menuOptions.length];

    private int mouseOverOptionIndex;
    private static final int[] menuOptionStates = {
            StateManager.STATE_PLAYABLE,
            StateManager.STATE_PLAYABLE,
            StateManager.STATE_PLAYABLE,
            StateManager.STATE_PLAYABLE
    };

    private boolean init;

    @Override
    public void init() {
        level = new Level(LEVEL_FILE_PATH);

        Requiem.GAME_CAMERA.pos.set(CAM_POS);
        Requiem.GAME_CAMERA.lookAt(0, 2, 0);

        init = true;
    }

    @Override
    public void update() {
        if (!init)
            init();

        level.update();

        mouseOverOptionIndex = -1;
        double mouseX = GameInput.mouseX;
        double mouseY = GameInput.mouseY;

        for (int i = 0; i < menuOptions.length; i++) {
            float x = xyWidthHeight[i][X];
            float y = xyWidthHeight[i][Y];
            float width = xyWidthHeight[i][WIDTH];
            float height = xyWidthHeight[i][HEIGHT];

            if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
                mouseOverOptionIndex = i;
                break;
            }
        }

        if (GameInput.mouseDeltaUpLeft) {
            if (mouseOverOptionIndex != -1) {
                int state = menuOptionStates[mouseOverOptionIndex];
                StateManager.setState(state);
            }
        }
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

        glPopMatrix();

        renderOrthographic();
    }

    @Override
    public String getModelPath() {
        return LEVEL_FILE_PATH;
    }

    @Override
    public void setModelPath(String path) {
        //final
    }

    public void renderOrthographic() {
        int[] resolution = SettingsManager.getResolution();
        GraphicsUtils.beginOrtho(resolution[0], resolution[1]);

        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);//TODO this is dumb af

        for (int i = 0; i < menuOptions.length; i++) {
            float drawX = xyWidthHeight[i][X];
            float drawY = xyWidthHeight[i][Y];

            //Color textColor;
            if (mouseOverOptionIndex != i) {
            //    textColor = new Color(255, 255, 255, 200);
            } else {
            //    textColor = new Color(255, 255, 255, 240);
            }


            FontManager.titleScreenMenuFont.drawString(menuOptions[i], (int) drawX, (int) drawY);
        }

        //glBlendFunc(GL_ONE, GL_ZERO);
        GraphicsUtils.endOrtho();
    }

    public static void resize() {
        int[] resolution = SettingsManager.getResolution();

        int startX = resolution[0] / 20;
        int startY = resolution[1] / 2;

        double staticHeight = FontManager.titleScreenMenuFont.getStaticFontHeight();

        for (int i = 0; i < menuOptions.length; i++) {
            String currentOption = menuOptions[i];
            xyWidthHeight[i][X] = startX;
            xyWidthHeight[i][Y] = (int) (startY + staticHeight * i);
        }
    }
}
