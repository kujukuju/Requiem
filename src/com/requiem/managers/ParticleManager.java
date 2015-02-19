package com.requiem.managers;

import com.requiem.Requiem;
import com.requiem.abstractentities.GameCamera;
import com.requiem.abstractentities.entities.Player;
import com.requiem.interfaces.Initializable;
import com.requiem.interfaces.Particle;
import com.requiem.interfaces.Updateable;
import com.requiem.particles.GroundExplosionFlame;
import com.requiem.particles.SmokeCloud;
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

    private static List<Particle> toBeAddedParticleList;

    private static boolean init;

    public static void init() {
        particleList = new LinkedList<Particle>();
        toBeAddedParticleList = new LinkedList<Particle>();

        loadParticleSpriteSheets();

        init = true;
    }

    public static void update() {
        if (!init)
            init();

        particleList.addAll(toBeAddedParticleList);
        toBeAddedParticleList.clear();

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
        SmokeCloud.spriteSheet = (Texture) AssetManager.getAsset(SmokeCloud.SPRITE_SHEET_PATH);
    }

    public static void addParticle(Particle particle) {
        toBeAddedParticleList.add(particle);
    }

    public static void renderParticles() {
        glEnable(GL_TEXTURE_2D);
        int[] currentBlendFunc = {GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA};
        Texture currentBoundSpriteSheet = null;

        Point3f camPos = GameCamera.getPos();
        TreeMap<Float, Particle> farthestToClosestParticles = new TreeMap<Float, Particle>();
        for (Particle particle : particleList) {
            float dist = MathUtils.quickLength(camPos, particle.getPos());
            farthestToClosestParticles.put(dist, particle);
        }

        Set<Float> distanceSet = farthestToClosestParticles.descendingKeySet();
        Iterator<Float> distanceIterator = distanceSet.iterator();
        while (distanceIterator.hasNext()) {
            float nextDist = distanceIterator.next();
            Particle curParticle = farthestToClosestParticles.get(nextDist);
            int[] particleBlendFunc = curParticle.getBlendFunc();
            Texture particleTexture = curParticle.getTexture();

            if (!currentBlendFunc.equals(particleBlendFunc)) {
                glBlendFunc(particleBlendFunc[0], particleBlendFunc[1]);
                currentBlendFunc = particleBlendFunc;
            }
            if (currentBoundSpriteSheet == null || !currentBoundSpriteSheet.equals(particleTexture)) {
                particleTexture.bind();
                currentBoundSpriteSheet = particleTexture;
            }

            curParticle.render();
        }

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
    }
}
