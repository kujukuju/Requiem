package com.requiem.utilities;

import com.trentwdavies.daeloader.Face;
import com.trentwdavies.daeloader.Geometry;
import com.trentwdavies.daeloader.GeometryObject;
import com.trentwdavies.daeloader.Model;
import com.trentwdavies.daeloader.physics.PhysicsFace;
import com.trentwdavies.daeloader.physics.PhysicsGeometry;
import com.trentwdavies.daeloader.physics.PhysicsGeometryObject;
import com.trentwdavies.daeloader.physics.PhysicsModel;

import javax.vecmath.Point3d;
import java.util.List;

/**
 * Created by Trent on 11/27/2014.
 */
public class PhysicsUtils {
    public static PhysicsModel getPhysicsModel(Model graphicsModel) {
        PhysicsModel physicsModel = new PhysicsModel();
        PhysicsGeometry physicsGeometry = new PhysicsGeometry();
        PhysicsGeometryObject physicsGeometryObject = new PhysicsGeometryObject();

        List<Geometry> graphicsGeometryList = graphicsModel.geometryList;
        int offset = 0;
        for (int i = 0; i < graphicsGeometryList.size(); i++) {
            Geometry curGraphicsGeometry = graphicsGeometryList.get(i);
            List<Point3d> curVertexList = curGraphicsGeometry.vertexList;

            Point3d[] vertexListArray = new Point3d[curVertexList.size()];
            physicsGeometry.addVertices(curVertexList.toArray(vertexListArray));

            for (int a = 0; a < curGraphicsGeometry.geometryObjectList.size(); a++) {
                GeometryObject curGraphicsGeometryObject = curGraphicsGeometry.geometryObjectList.get(a);

                for (int b = 0; b < curGraphicsGeometryObject.faceList.size(); b++) {
                    Face curGraphicsFace = curGraphicsGeometryObject.faceList.get(b);
                    PhysicsFace physicsFace = new PhysicsFace();
                    List<Integer> vertexIndexPointerList = curGraphicsFace.vertexIndexPointer;

                    for (int c = 0; c < vertexIndexPointerList.size(); c++) {
                        physicsFace.addVertexPointer(vertexIndexPointerList.get(c) + offset);
                    }

                    physicsGeometryObject.addPhysicsFaces(physicsFace);
                }
            }

            offset += curGraphicsGeometry.vertexList.size();
        }

        physicsGeometry.setPhysicsGeometryObject(physicsGeometryObject);
        physicsModel.setPhysicsGeometry(physicsGeometry);

        return physicsModel;
    }
}
