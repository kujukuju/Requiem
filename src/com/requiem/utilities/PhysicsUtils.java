package com.requiem.utilities;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.collision.shapes.TriangleMeshShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.requiem.logic.Physics;
import com.trentwdavies.daeloader.Face;
import com.trentwdavies.daeloader.Geometry;
import com.trentwdavies.daeloader.GeometryObject;
import com.trentwdavies.daeloader.Model;
import com.trentwdavies.daeloader.physics.PhysicsFace;
import com.trentwdavies.daeloader.physics.PhysicsGeometry;
import com.trentwdavies.daeloader.physics.PhysicsGeometryObject;
import com.trentwdavies.daeloader.physics.PhysicsModel;

import javax.vecmath.*;
import java.nio.*;
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
        PhysicsModel physicsModel = PhysicsUtils.getPhysicsModel(model);

        PhysicsGeometry geo = physicsModel.physicsGeometry;
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


    public static TriangleIndexVertexArray makeTriangleIndexVertexArray(Model model) {
        PhysicsModel physicsModel = PhysicsUtils.getPhysicsModel(model);

        PhysicsGeometry physicsGeometry = physicsModel.physicsGeometry;
        PhysicsGeometryObject physicsGeometryObject = physicsGeometry.physicsGeometryObject;
        List<Point3d> vertexList = physicsGeometry.vertexList;
        List<PhysicsFace> faceList = physicsGeometryObject.faceList;

        int numTriangles = physicsGeometryObject.faceList.size();
        int triangleIndexStride = 3 * 4;
        int numVertices = vertexList.size();
        int vertexStride = 3 * 4;

        ByteBuffer triangleIndexBase = ByteBuffer.allocateDirect(numTriangles * 3 * 4).order(ByteOrder.nativeOrder());
        for (int i = 0; i < faceList.size(); i++) {
            int stride = 4;
            int index = i * 3 * stride;
            PhysicsFace physicsFace = faceList.get(i);
            triangleIndexBase.putInt(index, physicsFace.vertexIndexPointer.get(0) * 3 * 4);
            triangleIndexBase.putInt(index + stride, physicsFace.vertexIndexPointer.get(1) * 3 * 4);
            triangleIndexBase.putInt(index + stride * 2, physicsFace.vertexIndexPointer.get(2) * 3 * 4);
        }


        ByteBuffer vertexBase = ByteBuffer.allocateDirect(numVertices * 3 * 4).order(ByteOrder.nativeOrder());
        for (int i = 0; i < vertexList.size(); i++) {
            int stride = 4;
            int index = i * 3 * stride;
            Point3d vertex = vertexList.get(i);
            vertexBase.putFloat(index, (float) vertex.x);
            vertexBase.putFloat(index + stride, (float) vertex.y);
            vertexBase.putFloat(index + stride * 2, (float) vertex.z);
        }

        return new TriangleIndexVertexArray(numTriangles, triangleIndexBase, triangleIndexStride, numVertices, vertexBase, vertexStride);
    }


    public static DiscreteDynamicsWorld createDynamicsWorld() {
        DiscreteDynamicsWorld dynamicsWorld = null;

        //checks collisions on the planes to see if an object may be colliding
        BroadphaseInterface broadphaseInterface = new DbvtBroadphase();
        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        //takes objects that may be colliding and calculates whether or not theyre colliding
        CollisionDispatcher collisionDispatcher = new CollisionDispatcher(collisionConfiguration);
        ConstraintSolver constraintSolver = new SequentialImpulseConstraintSolver();

        //the world in which the physics calculations are done
        dynamicsWorld = new DiscreteDynamicsWorld(collisionDispatcher, broadphaseInterface, constraintSolver, collisionConfiguration);

        return dynamicsWorld;
    }

    public static RigidBodyConstructionInfo createRigidBodyConstructionInfo(float mass, CollisionShape collisionShape) {
        Point3d position = new Point3d(0, 0, 0);

        return createRigidBodyConstructionInfo(mass, position, collisionShape);
    }

    public static RigidBodyConstructionInfo createRigidBodyConstructionInfo(float mass, Point3d position, CollisionShape collisionShape) {
        Vector3f localInertia = new Vector3f(0, 0, 0);

        return createRigidBodyConstructionInfo(mass, position, collisionShape, localInertia);
    }

    public static RigidBodyConstructionInfo createRigidBodyConstructionInfo(float mass, Point3d position, CollisionShape collisionShape, Vector3f localInertia) {
        Vector3f positionVector = new Vector3f((float) position.x, (float) position.y, (float) position.z);
        Quat4f angles = new Quat4f(0, 0, 0, 1);
        Matrix4f transformMatrix = new Matrix4f(angles, positionVector, 1);
        MotionState motionState = new DefaultMotionState(new Transform(transformMatrix));
        RigidBodyConstructionInfo rigidBodyConstructionInfo = new RigidBodyConstructionInfo(mass, motionState, collisionShape, localInertia);

        return rigidBodyConstructionInfo;
    }

    public static CollisionObject createCollisionObject(CollisionShape collisionShape) {
        CollisionObject collisionObject = new CollisionObject();
        collisionObject.setCollisionShape(collisionShape);

        return collisionObject;
    }
}
