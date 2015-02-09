package com.requiem.abilities;

import com.requiem.interfaces.Renderable;
import com.requiem.interfaces.Updateable;
import com.trentwdavies.textureloader.Texture;

/**
 * Created by Trent on 2/7/2015.
 */
public interface Ability extends Renderable, Updateable {
    public static final int STAGE_HOLDING = 0;
    public static final int STAGE_CASTING = 1;
    public static final int STAGE_CAST = 2;

    public int getStage();

    public boolean isCasting();
    public int getTotalCastTime();
    public float getCastPercent();

    public int getTotalCharges();
    public int getRemainingCharges();

    public int getTotalCooldown();
    public int getRemainingCooldown();
    public float getRemainingCooldownPercent();
}
