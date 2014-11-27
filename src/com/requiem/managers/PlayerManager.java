package com.requiem.managers;


import com.requiem.abstractentities.entities.MainPlayer;
import com.requiem.abstractentities.entities.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 10/26/2014.
 */
public class PlayerManager {
    public static final MainPlayer MAIN_PLAYER = new MainPlayer();
    public static List<Player> playerList = new ArrayList<Player>();
}
