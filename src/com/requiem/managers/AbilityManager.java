package com.requiem.managers;

import com.requiem.abilities.Ability;
import com.requiem.abilities.AbilityPressed;
import com.requiem.abilities.AbilitySelected;
import com.requiem.abilities.GroundExplosion;
import com.requiem.listeners.GameInput;
import com.requiem.overlays.ActionBar;
import com.requiem.utilities.AssetManager;
import com.trentwdavies.textureloader.Texture;

import javax.vecmath.Point2f;
import javax.vecmath.Point2i;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Trent on 2/9/2015.
 */
public class AbilityManager {
    public static final String ABILITY_ICON_TEXTURES_PATH = "assets/icons/abilities/ability-icons.png";
    private static Texture abilityIconTextures;
    private static Map<Class, Point2i> abilityToTextureCoordinates;

    private static Ability activeAbility;
    private static List<Ability> currentAbilities;

    private static final int ICON_WIDTH = 128;

    private static boolean init;

    public static void init() {
        currentAbilities = new ArrayList<Ability>();

        abilityToTextureCoordinates = new HashMap<Class, Point2i>();//x, y
        abilityToTextureCoordinates.put(AbilityPressed.class, new Point2i(1, 0));
        abilityToTextureCoordinates.put(AbilitySelected.class, new Point2i(2, 0));
        abilityToTextureCoordinates.put(GroundExplosion.class, new Point2i(3, 0));

        abilityIconTextures = (Texture) AssetManager.getAsset(ABILITY_ICON_TEXTURES_PATH);

        init = true;
    }

    public static void update() {
        if (!init)
            init();

        if (activeAbility != null) {
            activeAbility.update();
        }

        for (Ability ability : currentAbilities) {
            ability.update();
        }

        if (activeAbility != null && activeAbility.isIndependent()) {
            currentAbilities.add(activeAbility);
            activeAbility = null;
        }

        if (ActionBar.getSelectedAbilityClass() != null && (activeAbility == null || !ActionBar.getSelectedAbilityClass().equals(activeAbility.getClass()))) {
            Ability newAbility = null;
            try {
                newAbility = (Ability) ActionBar.getSelectedAbilityClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            //if youre currently casting or controlling an ability, give the new ability the old abilities start time
            if (activeAbility != null) {
                if (activeAbility.isCasting() || activeAbility.isControlling()) {
                    newAbility.setStartCastTime(activeAbility.getStartCastTime());
                }
            }

            activeAbility = newAbility;
        }
    }

    public static void renderAbilities() {
        for (Ability ability : currentAbilities) {
            ability.render();
        }
    }

    public static void bindIconTexture() {
        abilityIconTextures.bind();
    }

    public static Point2f[] getAbilityTexCoords(Class abilityClass) {
        int x = 0;
        int y = 0;

        if (abilityClass != null) {
            x = abilityToTextureCoordinates.get(abilityClass).x;
            y = abilityToTextureCoordinates.get(abilityClass).y;
        }

        int startX = x * ICON_WIDTH;
        int startY = y * ICON_WIDTH;
        int endX = startX + ICON_WIDTH;
        int endY = startY + ICON_WIDTH;

        Point2f[] returnPoints = new Point2f[2];
        returnPoints[0] = new Point2f();
        returnPoints[1] = new Point2f();
        returnPoints[0].x = (float) startX / abilityIconTextures.getWidth();
        returnPoints[0].y = (float) startY / abilityIconTextures.getHeight();
        returnPoints[1].x = (float) endX / abilityIconTextures.getWidth();
        returnPoints[1].y = (float) endY / abilityIconTextures.getHeight();

        return returnPoints;
    }
}
