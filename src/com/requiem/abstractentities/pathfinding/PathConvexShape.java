package com.requiem.abstractentities.pathfinding;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 1/21/2015.
 */
public class PathConvexShape {
    public List<Integer> vertexIndexPointer;
    Path2D yzPath;
    Path2D xzPath;
    Path2D xyPath;

    public PathConvexShape() {
        vertexIndexPointer = new ArrayList<Integer>();
    }

    public void addVertexPointer(PathMesh pathMesh, int... vertexIndices) {
        for (int i : vertexIndices) {
            this.vertexIndexPointer.add(i);
            pathMesh.pathVertexList.get(i).connectedShapes.add(this);
        }
    }

    public void createPath2D(PathMesh pathMesh) {
        yzPath = new Path2D.Double();
        xzPath = new Path2D.Double();
        xyPath = new Path2D.Double();
        for (int i = 0; i < vertexIndexPointer.size(); i++) {
            PathVertex curPathVertex = pathMesh.pathVertexList.get(i);

            if (i == 0) {
                yzPath.moveTo(curPathVertex.y, curPathVertex.z);
                xzPath.moveTo(curPathVertex.x, curPathVertex.z);
                xyPath.moveTo(curPathVertex.x, curPathVertex.y);
            } else {
                yzPath.lineTo(curPathVertex.y, curPathVertex.z);
                xzPath.lineTo(curPathVertex.x, curPathVertex.z);
                xyPath.lineTo(curPathVertex.x, curPathVertex.y);
            }
        }

        yzPath.closePath();
        xzPath.closePath();
        xyPath.closePath();
    }
}
