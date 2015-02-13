package com.requiem.managers;

import com.requiem.Requiem;
import com.requiem.abstractentities.entities.Player;
import com.requiem.interfaces.Initializable;
import com.requiem.interfaces.Particle;
import com.requiem.interfaces.Updateable;
import com.requiem.particles.GroundExplosionFlame;
import com.requiem.utilities.AssetManager;
import com.requiem.utilities.MathUtils;
import com.trentwdavies.textureloader.Texture;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;

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
        glEnable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        Point3f camPos = Requiem.GAME_CAMERA.getPos();
        TreeMap<Float, Particle> farthestToClosestParticles = new TreeMap<Float, Particle>();
        for (Particle particle : particleList) {
            float dist = MathUtils.quickLength(camPos, particle.getPos());
            farthestToClosestParticles.put(dist, particle);
        }

        GroundExplosionFlame.spriteSheet.bind();//TODO do this correctly
        Set<Float> distanceSet = farthestToClosestParticles.descendingKeySet();
        Iterator<Float> distanceIterator = distanceSet.iterator();
        while (distanceIterator.hasNext()) {
            float nextDist = distanceIterator.next();
            Particle curParticle = farthestToClosestParticles.get(nextDist);
            curParticle.render();
        }

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
    }
}
