package com.requiem.abstractentities.pathfinding;

import javax.vecmath.Point3d;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 1/21/2015.
 */
public class PathMesh {
    public List<PathVertex> pathVertexList;
    public List<PathConvexShape> pathConvexShapeList;
    public Point3d minPoint;
    public Point3d maxPoint;

    public PathMesh() {
        pathVertexList = new ArrayList<PathVertex>();
        pathConvexShapeList = new ArrayList<PathConvexShape>();
    }

    public void addVertices(PathVertex... pathVertices) {
        for (PathVertex curPathVertex : pathVertices) {
            pathVertexList.add(curPathVertex);
        }
    }

    public void addPathConvexShapes(PathConvexShape... pathConvexShapes) {
        for (PathConvexShape curPathConvexShape : pathConvexShapes) {
            pathConvexShapeList.add(curPathConvexShape);
        }
    }
}
