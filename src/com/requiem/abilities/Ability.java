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
    public static final int STAGE_CONTROLLING = 2;
    public static final int STAGE_INDEPENDENT = 3;

    public int getStage();

    public boolean isCasting();
    public boolean isControlling();
    public boolean isIndependent();

    public long getAbilityCreationTime();
    public void setAbilityCreationTime(long abilityCreationTime);

    public int getTotalCastTime();
    public float getCastPercent();
    public int getRemainingCastTime();
    public int getDeltaCastTime();
    public long getStartCastTime();
    public void setStartCastTime(long startCastTime);

    public int getTotalCharges();
    public int getRemainingCharges();

    public float getMaxRange();

    public int getTotalCooldown();
    public int getRemainingCooldown();
    public float getRemainingCooldownPercent();
}
