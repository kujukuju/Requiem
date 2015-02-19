package com.requiem.interfaces;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.trentwdavies.daeloader.Model;

/**
 * Created by Trent on 11/29/2014.
 */
public interface Collidable extends Initializable {
    public void createRigidBody();
    public void addToDynamicsWorld(DynamicsWorld dynamicsWorld);

    public Model getModel();
    public void setModel(Model model);

    public CollisionShape getCollisionShape();
    public void setCollisionShape(CollisionShape collisionShape);

    public RigidBody getRigidBody();
    public void setRigidBody(RigidBody rigidBody);
}
