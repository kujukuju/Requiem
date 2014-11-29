package com.requiem.utilities;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.trentwdavies.daeloader.Face;
import com.trentwdavies.daeloader.Geometry;
import com.trentwdavies.daeloader.GeometryObject;
import com.trentwdavies.daeloader.Model;
import com.trentwdavies.daeloader.physics.PhysicsFace;
import com.trentwdavies.daeloader.physics.PhysicsGeometry;
import com.trentwdavies.daeloader.physics.PhysicsGeometryObject;
import com.trentwdavies.daeloader.physics.PhysicsModel;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
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


    public static TriangleIndexVertexArray makeTIVA(Model model) {
        PhysicsModel playerPhysicsModel = PhysicsUtils.getPhysicsModel(model);

        PhysicsGeometry geo = playerPhysicsModel.physicsGeometry;
        IndexedMesh im = new IndexedMesh();

        ByteBuffer vertBuf = ByteBuffer.allocateDirect(geo.vertexList.size() * 3 * 8).order(ByteOrder.nativeOrder());
        DoubleBuffer db = vertBuf.asDoubleBuffer();

        im.numVertices = geo.vertexList.size();
        for(Point3d pt : geo.vertexList) {
            db.put(pt.x);
            db.put(pt.y);
            db.put(pt.z);
        }
        im.vertexBase = vertBuf;


        List<PhysicsFace> faces = geo.physicsGeometryObject.faceList;
        ByteBuffer indexBuf = ByteBuffer.allocateDirect(faces.size() * 3 * 4).order(ByteOrder.nativeOrder());
        IntBuffer ib = indexBuf.asIntBuffer();

        im.numTriangles = faces.size();
        for(PhysicsFace face : faces) {
            ib.put(face.vertexIndexPointer.get(0));
            ib.put(face.vertexIndexPointer.get(1));
            ib.put(face.vertexIndexPointer.get(2));
        }
        im.triangleIndexBase = indexBuf;

        im.vertexStride = 24;
        im.triangleIndexStride = 12;

        TriangleIndexVertexArray tiva = new TriangleIndexVertexArray();
        tiva.addIndexedMesh(im);
        return tiva;
    }


    public static DynamicsWorld createDynamicsWorld() {
        DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);

        // the maximum size of the collision world. Make sure objects stay
        // within these boundaries. Don't make the world AABB size too large, it
        // will harm simulation quality and performance
        Vector3f worldAabbMin = new Vector3f(-1000, -1000, -1000);
        Vector3f worldAabbMax = new Vector3f( 1000,  1000,  1000);
        // maximum number of objects
        final int maxProxies = 1024;
        // Broadphase computes an conservative approximate list of colliding pairs
        BroadphaseInterface broadphase = new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);

        // constraint (joint) solver
        ConstraintSolver solver = new SequentialImpulseConstraintSolver();

        // provides discrete rigid body simulation
        return new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
    }

    public static RigidBody createRigidBody(CollisionShape collisionShape, float mass) {
        RigidBodyConstructionInfo rigidBodyConstructionInfo = new RigidBodyConstructionInfo(mass, null, collisionShape);

        return new RigidBody(rigidBodyConstructionInfo);
    }

    public static CollisionObject createCollisionObject(CollisionShape collisionShape) {
        CollisionObject collisionObject = new CollisionObject();
        collisionObject.setCollisionShape(collisionShape);

        return collisionObject;
    }
}
