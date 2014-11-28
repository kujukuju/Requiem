package com.requiem.utilities.renderutilities;

import com.requiem.Requiem;
import com.trentwdavies.daeloader.*;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

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

                for (Face curFace : curGeometryObject.faceList) {
                    glBegin(GL_TRIANGLES);

                        for (int i = 0; i < curFace.vertexIndexPointer.size(); i++) {
                            int curVertexPointer = curFace.vertexIndexPointer.get(i);
                            Point3d vertexPoint3d = curGeometry.vertexList.get(curVertexPointer);

                            if (curEffect != null) {
                                glColor4d(curEffect.diffuse[0], curEffect.diffuse[1], curEffect.diffuse[2], curEffect.transparency);
                            }

                            if (curFace.hasNormals()) {
                                int curNormalPointer = curFace.normalIndexPointer.get(i);
                                Vector3d normalPoint3d = curGeometry.normalList.get(curNormalPointer);

                                glNormal3d(normalPoint3d.x, normalPoint3d.y, normalPoint3d.z);
                            }
                            glVertex3d(vertexPoint3d.x, vertexPoint3d.y, vertexPoint3d.z);
                        }

                    glEnd();
                }
            }

            glPopMatrix();
        }
    }
}
