package com.requiem.interfaces;

import javax.vecmath.Point3f;
import java.util.List;

/**
 * Created by Trent on 2/22/2015.
 */
public interface ParticleEmitter extends Updateable {
    public List<Particle> getParticles();
    public void addParticle(Particle particle);
    public boolean isDead();
}
