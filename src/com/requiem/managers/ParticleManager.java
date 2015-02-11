package com.requiem.managers;

import com.requiem.Requiem;
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
        glAlphaFunc(GL_GREATER, 0.5f);
        glEnable(GL_ALPHA_TEST);

        glEnable(GL_TEXTURE_2D);

        GroundExplosionFlame.spriteSheet.bind();
        for (Particle particle : particleList) {
            particle.render();
        }

        glDisable(GL_TEXTURE_2D);
    }
}
