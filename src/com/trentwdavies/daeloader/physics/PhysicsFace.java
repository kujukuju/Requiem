package com.trentwdavies.daeloader.physics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 11/27/2014.
 */
public class PhysicsFace {
    public List<Integer> vertexIndexPointer;

    public PhysicsFace() {
        vertexIndexPointer = new ArrayList<Integer>();
    }

    public void addVertexPointer(int... vertexIndices) {
        for (int i : vertexIndices) {
            this.vertexIndexPointer.add(i);
        }
    }
}
