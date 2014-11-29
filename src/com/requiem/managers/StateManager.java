package com.requiem.managers;

import com.requiem.interfaces.State;
import com.requiem.states.LoadingScreenState;
import com.requiem.states.PlayableState;
import com.requiem.states.TitleScreenState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 10/24/2014.
 */
public class StateManager {
    public static final int STATE_LOADING_SCREEN = 0;
    public static final int STATE_PLAYABLE = 1;
    public static final int STATE_TITLE_SCREEN = 2;

    public static int currentState = STATE_PLAYABLE;

    public static State[] STATES = {
            new LoadingScreenState(),
            new PlayableState(),
            new TitleScreenState()
    };

    public static State getCurrentState() {
        return STATES[currentState];
    }

    public static void setState(int state) {
        currentState = state;
    }
}
