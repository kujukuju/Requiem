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
    public static List<Integer> currentStates = new ArrayList<Integer>();

    public static final int STATE_LOADING_SCREEN = 0;
    public static final int STATE_PLAYABLE = 1;
    public static final int STATE_TITLE_SCREEN = 2;

    public static State[] STATES = {
            new LoadingScreenState(),
            new PlayableState(),
            new TitleScreenState()
    };

    public static List<State> getCurrentStates() {
        List<State> returnStates = new ArrayList<State>();
        for (int i = 0; i < currentStates.size(); i++) {
            returnStates.add(STATES[currentStates.get(i)]);
        }

        return returnStates;
    }

    public static void clearStates() {
        currentStates.clear();
    }

    public static void addStates(int... states) {
        for (int s : states) {
            currentStates.add(s);
        }
    }
}
