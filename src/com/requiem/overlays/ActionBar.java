package com.requiem.overlays;

import com.requiem.abilities.Ability;
import com.requiem.abilities.AbilityPressed;
import com.requiem.abilities.AbilitySelected;
import com.requiem.abilities.GroundExplosion;
import com.requiem.interfaces.Initializable;
import com.requiem.interfaces.Overlay;
import com.requiem.listeners.GameInput;
import com.requiem.managers.AbilityManager;
import com.requiem.utilities.GraphicsUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import javax.vecmath.Point2f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class ActionBar {
    private static int selectedAbility;
    private static int pressedAbility;

    private static Class[] abilities;

    private static float x;//where it starts as a percentage of the screen width
    private static float y;
    private static float anchorX;//the anchor on the graphic in terms of percent graphic width
    private static float anchorY;

    private static boolean init;

    public static void init() {
        anchorX = 0.5f;
        anchorY = 1f;
        x = 0.5f;
        y = 1f;

        selectedAbility = -1;
        pressedAbility = -1;
        abilities = new Class[12];
        abilities[0] = GroundExplosion.class;

        init = true;
    }

    public static void update() {
        if (!init)
            init();

        if (GameInput.keysDown[Keyboard.KEY_1]) {
            pressedAbility = 0;
        } else if (GameInput.keysDown[Keyboard.KEY_2]) {
            pressedAbility = 1;
        } else if (GameInput.keysDown[Keyboard.KEY_3]) {
            pressedAbility = 2;
        } else if (GameInput.keysDown[Keyboard.KEY_4]) {
            pressedAbility = 3;
        } else if (GameInput.keysDown[Keyboard.KEY_5]) {
            pressedAbility = 4;
        } else if (GameInput.keysDown[Keyboard.KEY_6]) {
            pressedAbility = 5;
        } else if (GameInput.keysDown[Keyboard.KEY_7]) {
            pressedAbility = 6;
        } else if (GameInput.keysDown[Keyboard.KEY_8]) {
            pressedAbility = 7;
        } else if (GameInput.keysDown[Keyboard.KEY_9]) {
            pressedAbility = 8;
        } else if (GameInput.keysDown[Keyboard.KEY_0]) {
            pressedAbility = 9;
        } else if (GameInput.keysDown[Keyboard.KEY_MINUS]) {
            pressedAbility = 10;
        } else if (GameInput.keysDown[Keyboard.KEY_EQUALS]) {
            pressedAbility = 11;
        } else {
            pressedAbility = -1;
        }

        if (GameInput.keysDeltaUp[Keyboard.KEY_1]) {
            selectedAbility = 0;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_2]) {
            selectedAbility = 1;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_3]) {
            selectedAbility = 2;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_4]) {
            selectedAbility = 3;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_5]) {
            selectedAbility = 4;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_6]) {
            selectedAbility = 5;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_7]) {
            selectedAbility = 6;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_8]) {
            selectedAbility = 7;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_9]) {
            selectedAbility = 8;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_0]) {
            selectedAbility = 9;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_MINUS]) {
            selectedAbility = 10;
        } else if (GameInput.keysDeltaUp[Keyboard.KEY_EQUALS]) {
            selectedAbility = 11;
        }
    }

    public static Class getSelectedAbilityClass() {
        if (selectedAbility == -1) {
            return null;
        }

        return abilities[selectedAbility];
    }

    public static void render() {
        //TODO might be laggy
        int[] resolution = {Display.getWidth(), Display.getHeight()};

        float iconWidth = resolution[1] * 0.05f;

        float width = iconWidth * 12f + 13;
        float height = iconWidth + 1;

        float startX = resolution[0] * x - width * anchorX;
        float startY = resolution[1] * y - height * anchorY;

        GraphicsUtils.beginOrtho(resolution[0], resolution[1]);

        glColor4f(0, 0, 0, 1);

        glEnable(GL_TEXTURE_2D);
        AbilityManager.bindIconTexture();
        //icons and background
        for (int i = 0; i < abilities.length; i++) {
            float iconStartX = startX + i * iconWidth + i + 1;
            float iconStartY = startY + 1;

            Point2f[] texCoords = null;
            if (abilities[i] != null) {
                texCoords = AbilityManager.getAbilityTexCoords(abilities[i]);
            } else {
                texCoords = AbilityManager.getAbilityTexCoords(null);
            }

            glBegin(GL_QUADS);
                //top left
                glTexCoord2f(texCoords[0].x, texCoords[0].y);
                glVertex2f(iconStartX, iconStartY);
                //top right
                glTexCoord2f(texCoords[1].x, texCoords[0].y);
                glVertex2f(iconStartX + iconWidth, iconStartY);
                //bottom right
                glTexCoord2f(texCoords[1].x, texCoords[1].y);
                glVertex2f(iconStartX + iconWidth, iconStartY + iconWidth);
                //bottom left
                glTexCoord2f(texCoords[0].x, texCoords[1].y);
                glVertex2f(iconStartX, iconStartY + iconWidth);
            glEnd();

            if (i == selectedAbility) {
                texCoords = AbilityManager.getAbilityTexCoords(AbilitySelected.class);
                glBegin(GL_QUADS);
                    //top left
                    glTexCoord2f(texCoords[0].x, texCoords[0].y);
                    glVertex2f(iconStartX, iconStartY);
                    //top right
                    glTexCoord2f(texCoords[1].x, texCoords[0].y);
                    glVertex2f(iconStartX + iconWidth, iconStartY);
                    //bottom right
                    glTexCoord2f(texCoords[1].x, texCoords[1].y);
                    glVertex2f(iconStartX + iconWidth, iconStartY + iconWidth);
                    //bottom left
                    glTexCoord2f(texCoords[0].x, texCoords[1].y);
                    glVertex2f(iconStartX, iconStartY + iconWidth);
                glEnd();
            } else if (i == pressedAbility) {
                texCoords = AbilityManager.getAbilityTexCoords(AbilityPressed.class);
                glBegin(GL_QUADS);
                    //top left
                    glTexCoord2f(texCoords[0].x, texCoords[0].y);
                    glVertex2f(iconStartX, iconStartY);
                    //top right
                    glTexCoord2f(texCoords[1].x, texCoords[0].y);
                    glVertex2f(iconStartX + iconWidth, iconStartY);
                    //bottom right
                    glTexCoord2f(texCoords[1].x, texCoords[1].y);
                    glVertex2f(iconStartX + iconWidth, iconStartY + iconWidth);
                    //bottom left
                    glTexCoord2f(texCoords[0].x, texCoords[1].y);
                    glVertex2f(iconStartX, iconStartY + iconWidth);
                glEnd();
            }
        }
        glDisable(GL_TEXTURE_2D);

        glBegin(GL_QUADS);
            //left line
            glVertex2f(startX, startY);
            glVertex2f(startX + 1, startY);
            glVertex2f(startX + 1, startY + height);
            glVertex2f(startX, startY + height);

            //top line
            glVertex2f(startX, startY);
            glVertex2f(startX + width, startY);
            glVertex2f(startX + width, startY + 1);
            glVertex2f(startX, startY + 1);

            //right line
            glVertex2f(startX + width - 1, startY);
            glVertex2f(startX + width, startY);
            glVertex2f(startX + width, startY + height);
            glVertex2f(startX + width - 1, startY + height);

            //the lines in the bar
            for (int i = 1; i < 12; i++) {
                float lineStartX = startX + iconWidth * i + i;
                glVertex2f(lineStartX, startY);
                glVertex2f(lineStartX + 1, startY);
                glVertex2f(lineStartX + 1, startY + height);
                glVertex2f(lineStartX, startY + height);
            }
        glEnd();

        GraphicsUtils.endOrtho();
    }
}
