package com.trentwdavies.daeloader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 11/4/2014.
 */
public class Face {
    public List<Integer> vertexIndexPointer;
    public List<Integer> normalIndexPointer;
    public List<Integer> colorIndexPointer;

    public Face() {
        vertexIndexPointer = new ArrayList<Integer>();
        normalIndexPointer = new ArrayList<Integer>();
        colorIndexPointer = new ArrayList<Integer>();
    }

    public void addVertexPointer(int... vertexIndices) {
        for (int i : vertexIndices) {
            this.vertexIndexPointer.add(i);
        }
    }

    public void addNormalPointer(int... normalIndices) {
        for (int i : normalIndices) {
            this.normalIndexPointer.add(i);
        }
    }

    public void addColorPointer(int... colorIndices) {
        for (int i : colorIndices) {
            this.colorIndexPointer.add(i);
        }
    }

    public boolean hasNormals() {
        return normalIndexPointer.size() > 0;
    }

    public boolean hasColors() {
        return colorIndexPointer.size() > 0;
    }
}
