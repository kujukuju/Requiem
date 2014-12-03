package com.requiem.utilities;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.*;
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

import javax.vecmath.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 11/27/2014.
 */
public class PhysicsUtils {
    public static TriangleIndexVertexArray makeTriangleIndexVertexArray(Geometry geometry) {
        IndexedMesh indexedMesh = new IndexedMesh();
        List<GeometryObject> geometryObjectList = geometry.geometryObjectList;


        List<Point3d> vertexList = geometry.vertexList;
        List<Face> faceList = new ArrayList<Face>();

        int numTriangles = 0;
        int triangleIndexStride = 3 * 4;
        int numVertices = vertexList.size();
        int vertexStride = 3 * 4;

        //calculate num triangles and fill facelist
        for (int i = 0; i < geometryObjectList.size(); i++) {
            GeometryObject geometryObject = geometryObjectList.get(i);
            List<Face> curFaceList = geometryObject.faceList;
            for (int a = 0; a < curFaceList.size(); a++) {
                faceList.add(curFaceList.get(a));
            }
            numTriangles += curFaceList.size();
        }

        int[] triangleIndexArray = new int[numTriangles * 3];
        for (int i = 0; i < faceList.size(); i++) {
            Face curFace = faceList.get(i);
            triangleIndexArray[i * 3] = curFace.vertexIndexPointer.get(0);
            triangleIndexArray[i * 3 + 1] = curFace.vertexIndexPointer.get(1);
            triangleIndexArray[i * 3 + 2] = curFace.vertexIndexPointer.get(2);
        }
        float[] vertexArray = new float[numVertices * 3];
        for (int i = 0; i < vertexList.size(); i++) {
            vertexArray[i * 3] = (float) vertexList.get(i).x;
            vertexArray[i * 3 + 1] = (float) vertexList.get(i).y;
            vertexArray[i * 3 + 2] = (float) vertexList.get(i).z;
        }


        ByteBuffer triangleIndexBase = ByteBuffer.allocateDirect(numTriangles * triangleIndexStride).order(ByteOrder.nativeOrder());
        triangleIndexBase.asIntBuffer().put(triangleIndexArray);

        ByteBuffer vertexBase = ByteBuffer.allocateDirect(numVertices * vertexStride).order(ByteOrder.nativeOrder());
        vertexBase.asFloatBuffer().put(vertexArray);


        indexedMesh.numTriangles = numTriangles;
        indexedMesh.triangleIndexBase = triangleIndexBase;
        indexedMesh.triangleIndexStride = triangleIndexStride;
        indexedMesh.numVertices = numVertices;
        indexedMesh.vertexBase = vertexBase;
        indexedMesh.vertexStride = vertexStride;

        TriangleIndexVertexArray triangleIndexVertexArray = new TriangleIndexVertexArray();
        triangleIndexVertexArray.addIndexedMesh(indexedMesh);

        return triangleIndexVertexArray;
    }

    public static BvhTriangleMeshShape[] getBvhTriangleMeshShapes(Model model, boolean useQuantizedAabbCompression) {
        List<Geometry> geometryList = model.geometryList;
        System.out.println("WHAT: " + geometryList.size());
        BvhTriangleMeshShape[] bvhTriangleMeshShapeArray = new BvhTriangleMeshShape[geometryList.size()];

        for (int i = 0; i < geometryList.size(); i++) {
            TriangleIndexVertexArray triangleIndexVertexArray = makeTriangleIndexVertexArray(geometryList.get(i));
            bvhTriangleMeshShapeArray[i] = new BvhTriangleMeshShape(triangleIndexVertexArray, useQuantizedAabbCompression);
        }

        return bvhTriangleMeshShapeArray;
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
