package com.requiem.managers;

import com.requiem.interfaces.Initializable;
import com.requiem.interfaces.Particle;
import com.requiem.interfaces.Updateable;
import com.requiem.particles.GroundExplosionFlame;
import com.requiem.utilities.AssetManager;
import com.trentwdavies.textureloader.Texture;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
        for (Particle particle : particleList) {
            particle.render();
        }
    }
}
