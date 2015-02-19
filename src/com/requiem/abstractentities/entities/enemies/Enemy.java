package com.requiem.abstractentities.entities.enemies;

import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import com.requiem.abstractentities.entities.Entity;
import com.requiem.abstractentities.entities.Player;
import com.requiem.interfaces.Collidable;
import com.requiem.interfaces.Initializable;
import com.requiem.interfaces.Moveable;
import com.requiem.interfaces.Updateable;
import com.requiem.logic.AIProcessor;
import com.requiem.utilities.AssetManager;
import com.trentwdavies.daeloader.ColladaLoader;
import com.trentwdavies.daeloader.Model;
import org.xml.sax.SAXException;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Trent on 1/31/2015.
 */
public interface Enemy extends Collidable, Moveable {
    public AIProcessor getAIProcessor();
    public void setAIProcessor(AIProcessor aiProcessor);
}
