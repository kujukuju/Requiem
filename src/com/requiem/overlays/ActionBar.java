package com.requiem.overlays;

import com.requiem.abilities.Ability;
import com.requiem.abilities.AbilityPressed;
import com.requiem.abilities.AbilitySelected;
import com.requiem.abilities.GroundExplosion;
import com.requiem.interfaces.Initializable;
import com.requiem.interfaces.Overlay;
import com.requiem.managers.AbilityManager;
import com.requiem.utilities.GraphicsUtils;
import org.lwjgl.opengl.Display;

import javax.vecmath.Point2f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class ActionBar implements Overlay, Initializable {
    private int selectedAbility;
    private int pressedAbility;

    private Class[] abilities;

    private float x;//where it starts as a percentage of the screen width
    private float y;
    private float anchorX;//the anchor on the graphic in terms of percent graphic width
    private float anchorY;
    private boolean init;

    @Override
    public void init() {
        anchorX = 0.5f;
        anchorY = 1f;
        x = 0.5f;
        y = 1f;

        selectedAbility = -1;
        pressedAbility = -1;
        abilities = new Class[12];
        abilities[0] = GroundExplosion.class;
    }

    @Override
    public void update() {
        if (!init)
            init();


    }

    @Override
    public void render() {
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

    @Override
    public String getModelPath() {
        return null;
        //TODO renderable does not equal model
    }

    @Override
    public void setModelPath(String path) {
        //no path yet
    }
}
