package com.trentwdavies.daeloader.physics;

import com.trentwdavies.daeloader.GeometryObject;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 11/27/2014.
 */
public class PhysicsGeometry {
    public PhysicsGeometryObject physicsGeometryObject;
    public List<Point3d> vertexList;

    public PhysicsGeometry() {
        vertexList = new ArrayList<Point3d>();
    }

    public void addVertices(Point3d... vertices) {
        for (Point3d vector : vertices) {
            vertexList.add(vector);
        }
    }

    public void setPhysicsGeometryObject(PhysicsGeometryObject physicsGeometryObject) {
        this.physicsGeometryObject = physicsGeometryObject;
    }
}
