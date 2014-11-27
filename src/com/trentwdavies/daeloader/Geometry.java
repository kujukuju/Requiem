package com.trentwdavies.daeloader;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 11/4/2014.
 */
public class Geometry {
    public String id;
    public List<Point3d> vertexList;
    public List<Vector3d> normalList;
    public List<Point3d> colorList;
    public List<GeometryObject> geometryObjectList;

    public Geometry() {
        this.id = id;
        vertexList = new ArrayList<Point3d>();
        normalList = new ArrayList<Vector3d>();
        colorList = new ArrayList<Point3d>();
        geometryObjectList = new ArrayList<GeometryObject>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addVertices(Point3d... vertices) {
        for (Point3d vector : vertices) {
            vertexList.add(vector);
        }
    }

    public void addNormals(Vector3d... normals) {
        for (Vector3d vector : normals) {
            normalList.add(vector);
        }
    }

    public void addColors(Point3d... colors) {
        for (Point3d vector : colors) {
            colorList.add(vector);
        }
    }

    public void addGeometryObjects(GeometryObject... geometryObjects) {
        for (GeometryObject geometryObject : geometryObjects) {
            geometryObjectList.add(geometryObject);
        }
    }
}
