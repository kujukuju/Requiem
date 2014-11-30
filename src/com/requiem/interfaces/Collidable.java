package com.requiem.interfaces;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.trentwdavies.daeloader.physics.PhysicsModel;

/**
 * Created by Trent on 11/29/2014.
 */
public interface Collidable {
    public CollisionShape getCollisionShape();
    public void createRigidBody();
    public void setRigidBody(RigidBody rigidBody);
    public RigidBody getRigidBody();
}
