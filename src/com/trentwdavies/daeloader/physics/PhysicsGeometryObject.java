package com.trentwdavies.daeloader.physics;

import com.trentwdavies.daeloader.Face;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 11/27/2014.
 */
public class PhysicsGeometryObject {
    public List<PhysicsFace> faceList;

    public PhysicsGeometryObject() {
        faceList = new ArrayList<PhysicsFace>();
    }

    public void addPhysicsFaces(PhysicsFace... physicsFaces) {
        for (PhysicsFace f : physicsFaces) {
            faceList.add(f);
        }
    }
}
