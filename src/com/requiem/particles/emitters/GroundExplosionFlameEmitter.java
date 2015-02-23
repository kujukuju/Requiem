package com.requiem.particles.emitters;

import com.requiem.abilities.Ability;
import com.requiem.effects.lights.PointLight;
import com.requiem.interfaces.Light;
import com.requiem.interfaces.Particle;
import com.requiem.interfaces.ParticleEmitter;
import com.requiem.managers.LightManager;
import com.requiem.utilities.FastRandom;
import com.requiem.utilities.GameTime;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector4f;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Trent on 2/22/2015.
 */
public class GroundExplosionFlameEmitter implements ParticleEmitter {
    public List<Particle> particleList = new LinkedList<Particle>();

    private Ability parentAbility;

    private static final Vector4f LIGHT_COLOR = new Vector4f(0.1f, 0.04f, 0f, 1f);
    private Light fireLight;

    public GroundExplosionFlameEmitter(Ability parentAbility, Point3f pos) {
        this.parentAbility = parentAbility;
        fireLight = new PointLight(new Point4f(pos.x, pos.y + 2.5f, pos.z, 1), LIGHT_COLOR);
        LightManager.addLight(fireLight);
    }

    @Override
    public List<Particle> getParticles() {
        return particleList;
    }

    @Override
    public void addParticle(Particle particle) {
        particleList.add(particle);
    }

    @Override
    public boolean isDead() {
        boolean isDead = particleList.size() == 0 && !parentAbility.isCasting();
        if (isDead) {
            LightManager.removeLight(fireLight);
        }

        return isDead;
    }

    @Override
    public void update() {
        float r = LIGHT_COLOR.x + (FastRandom.random.nextFloat() * LIGHT_COLOR.x - LIGHT_COLOR.x / 2) / 4;
        float g = LIGHT_COLOR.y + (FastRandom.random.nextFloat() * LIGHT_COLOR.y - LIGHT_COLOR.y / 2) / 4;
        r *= particleList.size() / 40f;
        g *= particleList.size() / 40f;
        fireLight.setLightDiffuse(new Vector4f(r, g, 0, 1));

        Iterator<Particle> particleIterator = particleList.listIterator();
        while (particleIterator.hasNext()) {
            Particle curParticle = particleIterator.next();
            curParticle.update();

            if (curParticle.isDead()) {
                particleIterator.remove();
            }
        }
    }
}
