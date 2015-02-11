package com.requiem.managers;


import com.requiem.abstractentities.entities.Player;

import javax.vecmath.Point3f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 10/26/2014.
 */
public class PlayerManager {
    public static final Player PLAYER = new Player();
    public static List<Player> playerList = new ArrayList<Player>();

    public static Point3f getCastFromPoint() {
        return new Point3f(PLAYER.getPos().x, PLAYER.getPos().y + 2f, PLAYER.getPos().z);
    }
}
