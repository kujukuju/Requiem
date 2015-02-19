package com.requiem.utilities.renderutilities;

import com.requiem.utilities.GraphicsUtils;
import com.trentwdavies.daeloader.*;
import org.lwjgl.BufferUtils;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 11/7/2014.
 */
public class Batch {
    public static void renderModel(Model model) {
        for (Geometry curGeometry : model.geometryList) {
            glPushMatrix();

            for (GeometryObject curGeometryObject : curGeometry.geometryObjectList) {
                Effect curEffect = model.materialEffectMap.get(curGeometryObject.materialReference);
                if (curEffect != null) {//TODO is floatbuffer .get O(1)?
                    glColor4f(curEffect.diffuse.get(0), curEffect.diffuse.get(1), curEffect.diffuse.get(2), curEffect.transparency);
                    glMaterial(GL_FRONT, GL_DIFFUSE, curEffect.diffuse);
                    glMaterial(GL_FRONT, GL_AMBIENT, curEffect.ambient);
                    glMaterial(GL_FRONT, GL_SPECULAR, curEffect.specular);
                    glMaterialf(GL_FRONT, GL_SHININESS, curEffect.shininess);
                } else {
                    glColor4f(1, 1, 1, 1);
                    FloatBuffer fb = BufferUtils.createFloatBuffer(4);
                    float[] arr = {1, 1, 1, 1};
                    fb.put(arr).flip();
                    glMaterial(GL_FRONT, GL_DIFFUSE, fb);
                }

                for (Face curFace : curGeometryObject.faceList) {
                    glBegin(GL_TRIANGLES);

                        for (int i = 0; i < curFace.vertexIndexPointer.size(); i++) {
                            int curVertexPointer = curFace.vertexIndexPointer.get(i);
                            Point3d vertexPoint3d = curGeometry.vertexList.get(curVertexPointer);

                            if (curFace.hasNormals()) {
                                int curNormalPointer = curFace.normalIndexPointer.get(i);
                                Vector3d normalPoint3d = curGeometry.normalList.get(curNormalPointer);

                                glNormal3d(normalPoint3d.x, normalPoint3d.y, normalPoint3d.z);
                            }
                            glVertex3f((float) vertexPoint3d.x, (float) vertexPoint3d.y, (float) vertexPoint3d.z);
                        }

                    glEnd();
                }
            }

            glPopMatrix();
        }
    }
}
