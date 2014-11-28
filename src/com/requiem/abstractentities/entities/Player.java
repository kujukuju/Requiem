package com.requiem.abstractentities.entities;


import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.requiem.utilities.renderutilities.Batch;
import com.trentwdavies.daeloader.ColladaLoader;
import com.trentwdavies.daeloader.Face;
import com.trentwdavies.daeloader.Geometry;
import com.trentwdavies.daeloader.Model;
import org.xml.sax.SAXException;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 10/24/2014.
 */
public class Player extends Entity {
    public static final String PLAYER_MODEL_FILE_PATH = "assets/models/test-character.dae";
    private Model playerModel;

    private boolean init;

    public Player() {
        pos = new Point3d();
        vel = new Vector3d();
        ang = new Vector3d();
    }

    @Override
    public void init() {
        try {
            playerModel = ColladaLoader.loadFile(PLAYER_MODEL_FILE_PATH);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        loadGuyShapeWithVertexAndIndexDataAndAssignToCollisionShapeDataMemberFromEntityClass();
        init = true;
    }

    private void loadGuyShapeWithVertexAndIndexDataAndAssignToCollisionShapeDataMemberFromEntityClass() {
        List<Geometry> geometries = playerModel.geometryList;
        if(geometries.size() == 0) {
            System.err.println("No geometries");
            return;
        }
        Geometry geo = geometries.get(0);
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


        List<Face> faces = geo.geometryObjectList.get(0).faceList;
        ByteBuffer indexBuf = ByteBuffer.allocateDirect(faces.size() * 3 * 4).order(ByteOrder.nativeOrder());
        IntBuffer ib = indexBuf.asIntBuffer();

        im.numTriangles = faces.size();
        for(Face face : faces) {
            ib.put(face.vertexIndexPointer.get(0));
            ib.put(face.vertexIndexPointer.get(1));
            ib.put(face.vertexIndexPointer.get(2));
        }
        im.triangleIndexBase = indexBuf;

        im.vertexStride = 24;
        im.triangleIndexStride = 12;

        TriangleIndexVertexArray tiva = new TriangleIndexVertexArray();
        tiva.addIndexedMesh(im);
        collisionShape = new GImpactMeshShape(tiva);
    }

    @Override
    public void update() {
        if (!init)
            init();
    }

    @Override
    public void render() {
        glPushMatrix();

        glTranslated(pos.x, pos.y, pos.z);
        glRotated(-ang.y, 0, 1, 0);

        Batch.renderModel(playerModel);

        glPopMatrix();
    }
}
