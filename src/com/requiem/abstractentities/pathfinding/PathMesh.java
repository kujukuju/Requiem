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
        minPoint = new Point3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        maxPoint = new Point3d(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    public void addVertices(PathVertex... pathVertices) {
        for (PathVertex curPathVertex : pathVertices) {
            pathVertexList.add(curPathVertex);

            minPoint.x = Math.min(minPoint.x, curPathVertex.x);
            minPoint.y = Math.min(minPoint.y, curPathVertex.y);
            minPoint.z = Math.min(minPoint.z, curPathVertex.z);
            maxPoint.x = Math.max(maxPoint.x, curPathVertex.x);
            maxPoint.y = Math.max(maxPoint.y, curPathVertex.y);
            maxPoint.z = Math.max(maxPoint.z, curPathVertex.z);
        }
    }

    public void addPathConvexShapes(PathConvexShape... pathConvexShapes) {
        for (PathConvexShape curPathConvexShape : pathConvexShapes) {
            pathConvexShapeList.add(curPathConvexShape);
        }
    }

    public void generateShapeData() {
        for (PathConvexShape curPathConvexShape : pathConvexShapeList) {
            curPathConvexShape.createPath2D();
            curPathConvexShape.createPlane();
        }
    }
}
