package com.requiem.managers;

import com.requiem.interfaces.Initializable;
import com.requiem.interfaces.Particle;
import com.requiem.interfaces.Updateable;
import com.requiem.particles.GroundExplosionFlame;
import com.requiem.utilities.AssetManager;
import com.trentwdavies.textureloader.Texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 2/9/2015.
 */
public class ParticleManager {
    public static List<Particle> particleList;

    private static boolean init;

    public static void init() {
        particleList = new LinkedList<Particle>();

        loadParticleSpriteSheets();

        init = true;
    }

    public static void update() {
        if (!init)
            init();

        Iterator<Particle> particleIterator = particleList.listIterator();
        while (particleIterator.hasNext()) {
            Particle currentParticle = particleIterator.next();
            currentParticle.update();

            if (currentParticle.isDead()) {
                particleIterator.remove();
            }
        }
    }

    public static void loadParticleSpriteSheets() {
        GroundExplosionFlame.spriteSheet = (Texture) AssetManager.getAsset(GroundExplosionFlame.SPRITE_SHEET_PATH);
    }

    public static void addParticle(Particle particle) {
        particleList.add(particle);
    }

    public static void renderParticles() {
        glPushMatrix();

        /*float[] projMatrix = new float[16];
        ByteBuffer tempBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder());
        glGetFloat(GL_MODELVIEW_MATRIX, tempBuffer.asFloatBuffer());
        tempBuffer.asFloatBuffer().get(projMatrix);

        float d = (float) Math.sqrt(projMatrix[0] * projMatrix[0] + projMatrix[4] * projMatrix[4] + projMatrix[8] * projMatrix[8]);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    projMatrix[i * 4 + j] = d;
                } else {
                    projMatrix[i * 4 + j] = 0;
                }
            }
        }

        ByteBuffer projMatrixBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder());
        projMatrixBuffer.asFloatBuffer().put(projMatrix);

        glLoadMatrix(projMatrixBuffer.asFloatBuffer());*/

        for (Particle particle : particleList) {
            particle.render();
        }

        glPopMatrix();
    }
}
