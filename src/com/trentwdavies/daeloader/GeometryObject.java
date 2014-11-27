package com.trentwdavies.daeloader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 11/14/2014.
 */
//A GeometryObject is a polylist element
public class GeometryObject {
    public List<Face> faceList;
    public String materialReference;

    public GeometryObject() {
        faceList = new ArrayList<Face>();
    }

    public void addFaces(Face... faces) {
        for (Face f : faces) {
            faceList.add(f);
        }
    }

    public void setMaterialReference(String materialReference) {
        this.materialReference = materialReference;
    }
}
