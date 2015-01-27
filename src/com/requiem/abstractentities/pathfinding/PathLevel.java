package com.requiem.abstractentities.pathfinding;

import com.requiem.abstractentities.AbstractEntity;
import com.requiem.abstractentities.entities.Level;
import com.trentwdavies.daeloader.Face;
import com.trentwdavies.daeloader.Geometry;
import com.trentwdavies.daeloader.GeometryObject;
import com.trentwdavies.daeloader.Model;

import javax.vecmath.Point3d;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 1/21/2015.
 */
public class PathLevel extends AbstractEntity {
    public List<PathMesh> pathMeshList;
    public PathConvexShape[][] pathGridShapes;

    public static final double gridCellSize = 1;

    public PathLevel(Level level) {
        pathMeshList = new ArrayList<PathMesh>();

        Model levelModel = level.levelModel;
        for (Geometry curGeometry : levelModel.geometryList) {
            PathMesh curPathMesh = new PathMesh();

            //load all vertices
            for (Point3d curPoint : curGeometry.vertexList) {
                PathVertex curPathVertex = new PathVertex((float) curPoint.x, (float) curPoint.y, (float) curPoint.z);
                curPathMesh.addVertices(curPathVertex);
            }

            //load all faces
            for (GeometryObject curGeometryObject : curGeometry.geometryObjectList) {
                for (Face curFace : curGeometryObject.faceList) {
                    PathConvexShape curPathConvexShape = new PathConvexShape();

                    for (int index : curFace.vertexIndexPointer) {
                        curPathConvexShape.addVertexPointer(curPathMesh, index);
                    }

                    curPathConvexShape.createPath2D(curPathMesh);

                    curPathMesh.addPathConvexShapes(curPathConvexShape);
                }
            }

            pathMeshList.add(curPathMesh);
        }
    }

    public void generateGrid() {

    }
}
