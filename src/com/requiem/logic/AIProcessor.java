package com.requiem.logic;

import com.requiem.abstractentities.entities.Entity;
import com.requiem.abstractentities.entities.enemies.Enemy;
import com.requiem.interfaces.Moveable;
import com.requiem.interfaces.Updateable;
import com.requiem.managers.PlayerManager;
import com.requiem.states.PlayableState;
import com.requiem.utilities.GameTime;
import com.requiem.utilities.PathFinding;
import org.lwjgl.util.glu.Sphere;

import javax.vecmath.Point3f;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

/**
 * Created by Trent on 2/2/2015.
 */
public class AIProcessor implements Updateable {
    private Moveable owner;
    private List<Entity> targets;
    private List<Point3f> currentPath;

    public AIProcessor(Moveable owner) {
        this.owner = owner;
        targets = new ArrayList<Entity>();
        currentPath = new LinkedList<Point3f>();
    }
public static List<Point3f> spherePoints;
    @Override
    public void update() {
        calculatePathToEntity(PlayerManager.PLAYER);
        spherePoints = currentPath;
    }

    public void addTargets(Entity... entities) {
        for (Entity entity : entities) {
            targets.add(entity);
        }
    }

    public void calculatePathToEntity(Entity entity) {
        List<Point3f> newPath = PathFinding.findPath(owner.getPos(), entity.getPos(), owner.getMaxSteepness(), owner.getMaxJumpHeight(), PlayableState.pathLevel);
        System.out.println("Path Size: " + newPath.size());

        currentPath = newPath;
    }

    public List<Point3f> getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(List<Point3f> currentPath) {
        this.currentPath = currentPath;
    }

    public Moveable getOwner() {
        return owner;
    }

    public void setOwner(Moveable owner) {
        this.owner = owner;
    }
}
