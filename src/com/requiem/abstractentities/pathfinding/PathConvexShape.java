package com.requiem.abstractentities.pathfinding;

import com.requiem.utilities.MathUtils;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 1/21/2015.
 */
public class PathConvexShape {
    public List<Integer> vertexIndexPointer;
    public Path2D xzPath;
    public Vector3f planeGradient;
    public PathMesh parentMesh;

    public PathConvexShape(PathMesh parentMesh) {
        this.parentMesh = parentMesh;
        vertexIndexPointer = new ArrayList<Integer>();
    }

    public void addVertexPointer(PathMesh pathMesh, int... vertexIndices) {
        for (int i : vertexIndices) {
            this.vertexIndexPointer.add(i);
            pathMesh.pathVertexList.get(i).connectedShapes.add(this);
        }
    }

    public void createPath2D() {
        xzPath = new Path2D.Double();
        for (int i = 0; i < vertexIndexPointer.size(); i++) {
            PathVertex curPathVertex = parentMesh.pathVertexList.get(vertexIndexPointer.get(i));

            if (i == 0) {
                xzPath.moveTo(curPathVertex.x, curPathVertex.z);
            } else {
                xzPath.lineTo(curPathVertex.x, curPathVertex.z);
            }
        }

        xzPath.closePath();
    }

    public void createPlane() {
        PathVertex pathVertex1 = parentMesh.pathVertexList.get(vertexIndexPointer.get(0));
        PathVertex pathVertex2 = parentMesh.pathVertexList.get(vertexIndexPointer.get(1));
        PathVertex pathVertex3 = parentMesh.pathVertexList.get(vertexIndexPointer.get(2));
        Point3f point1 = new Point3f(pathVertex1.x, pathVertex1.y, pathVertex1.z);
        Point3f point2 = new Point3f(pathVertex2.x, pathVertex2.y, pathVertex2.z);
        Point3f point3 = new Point3f(pathVertex3.x, pathVertex3.y, pathVertex3.z);
        planeGradient = MathUtils.calculatePlaneGradient(point1, point2, point3);
    }
}
