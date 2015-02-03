package com.requiem.abstractentities.pathfinding;

import com.requiem.utilities.AssetManager;
import com.trentwdavies.daeloader.Face;
import com.trentwdavies.daeloader.Geometry;
import com.trentwdavies.daeloader.GeometryObject;
import com.trentwdavies.daeloader.Model;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 1/21/2015.
 */
public class PathLevel {
    public List<PathMesh> pathMeshList;
    public List<PathConvexShape>[][] pathGridShapes;
    public Point3f minPoint;
    public Point3f maxPoint;

    //TODO change to 1 eventually after testing
    public static final double GRID_CELL_SIZE = 1.4;

    public PathLevel(String levelPath) {
        pathMeshList = new ArrayList<PathMesh>();
        minPoint = new Point3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        maxPoint = new Point3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);

        Model levelModel = (Model) AssetManager.getAsset(levelPath);
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
                    PathConvexShape curPathConvexShape = new PathConvexShape(curPathMesh);

                    for (int index : curFace.vertexIndexPointer) {
                        curPathConvexShape.addVertexPointer(curPathMesh, index);
                    }

                    curPathConvexShape.createPath2D();

                    curPathMesh.addPathConvexShapes(curPathConvexShape);
                }
            }

            pathMeshList.add(curPathMesh);

            minPoint.x = (float) Math.min(minPoint.x, curPathMesh.minPoint.x);
            minPoint.y = (float) Math.min(minPoint.y, curPathMesh.minPoint.y);
            minPoint.z = (float) Math.min(minPoint.z, curPathMesh.minPoint.z);
            maxPoint.x = (float) Math.max(maxPoint.x, curPathMesh.maxPoint.x);
            maxPoint.y = (float) Math.max(maxPoint.y, curPathMesh.maxPoint.y);
            maxPoint.z = (float) Math.max(maxPoint.z, curPathMesh.maxPoint.z);
        }

        generateShapeData();

        generateGrid();
    }

    public void generateShapeData() {
        for (PathMesh curPathMesh : pathMeshList) {
            curPathMesh.generateShapeData();
        }
    }

    public void generateGrid() {
        double deltaX = maxPoint.x - minPoint.x;
        double deltaZ = maxPoint.z - minPoint.z;

        int gridCountX = (int) Math.ceil(deltaX / GRID_CELL_SIZE);
        int gridCountZ = (int) Math.ceil(deltaZ / GRID_CELL_SIZE);

        pathGridShapes = new ArrayList[gridCountZ][gridCountX];

        for (int z = 0; z < pathGridShapes.length; z++) {
            for (int x = 0; x < pathGridShapes[z].length; x++) {
                double startX = x * GRID_CELL_SIZE + minPoint.x;
                double startZ = z * GRID_CELL_SIZE + minPoint.z;
                Rectangle2D.Double gridBox = new Rectangle2D.Double(startX, startZ, GRID_CELL_SIZE, GRID_CELL_SIZE);

                for (PathMesh curPathMesh : pathMeshList) {
                    for (PathConvexShape curPathConvexShape : curPathMesh.pathConvexShapeList) {
                        if (curPathConvexShape.xzPath.intersects(gridBox)) {
                            if (pathGridShapes[z][x] == null) {
                                pathGridShapes[z][x] = new ArrayList<PathConvexShape>();
                            }
                            pathGridShapes[z][x].add(curPathConvexShape);
                        }
                    }
                }
            }
        }
    }

    //returns a list of all the shapes inside the grid cell corresponding to your position
    public List<PathConvexShape> getPossibleShapes(double x, double z) {
        double deltaX = x - minPoint.x;
        double deltaZ = z - minPoint.z;
        int gridX = (int) (deltaX / GRID_CELL_SIZE);
        int gridZ = (int) (deltaZ / GRID_CELL_SIZE);
        if (gridX >= 0 && gridZ >= 0 && gridZ < pathGridShapes.length && gridX < pathGridShapes[gridZ].length) {
            return pathGridShapes[gridZ][gridX];
        }

        return new ArrayList<PathConvexShape>();
    }
}
