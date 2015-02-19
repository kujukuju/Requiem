package com.requiem.utilities;

import com.requiem.Requiem;
import com.requiem.abstractentities.GameCamera;
import com.requiem.abstractentities.pathfinding.PathConvexShape;
import com.requiem.abstractentities.pathfinding.PathLevel;
import com.requiem.abstractentities.pathfinding.PathVertex;
import org.lwjgl.util.glu.Sphere;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Trent on 1/21/2015.
 */
public class PathFinding {
    public static PathLevel pathLevel;

    //TODO it would make it faster if I made planeVertex extend point3d so I dont have to create new objects
    //finds the shape thats the closest to the guys feet
    public static PathConvexShape findGroundShape(Point3f guyPosition, PathLevel pathLevel) {
        List<PathConvexShape> pathConvexShapeList = pathLevel.getPossibleShapes(guyPosition.x, guyPosition.z);
        if (pathConvexShapeList == null) {
            return null;
        }

        double highestPossibleY = guyPosition.y;
        PathConvexShape closestShape = null;
        double smallestYDiff = Double.MAX_VALUE;

        for (PathConvexShape curPathConvexShape : pathConvexShapeList) {
            if (curPathConvexShape.xzPath.contains(guyPosition.x, guyPosition.z)) {
                Point2f guyPoint2f = new Point2f(guyPosition.x, guyPosition.z);
                int planeVertexIndex = curPathConvexShape.vertexIndexPointer.get(0);
                PathVertex planeVertex = curPathConvexShape.parentMesh.pathVertexList.get(planeVertexIndex);

                double yValue = MathUtils.calculatePlaneY(guyPoint2f, planeVertex, curPathConvexShape.planeGradient);
                double yDiff = highestPossibleY - yValue;
                if (yDiff >= 0 && yDiff < smallestYDiff) {
                    smallestYDiff = yDiff;
                    closestShape = curPathConvexShape;
                }
            }
        }

        return closestShape;
    }

    //TODO go from both the from point and the to point and meet in the middle for less path expansion
    public static List<Point3f> findPath(Point3f from, Point3f to, double maxSteepness, double maxLedgeHeight, PathLevel pathLevel) {
        List<Point3f> returnVertices = new ArrayList<Point3f>();

        PathConvexShape startShape = findGroundShape(from, pathLevel);
        PathConvexShape endShape = findGroundShape(to, pathLevel);
        //check if youre in the same polygon as the target
        if (startShape.equals(endShape)) {
            if (!isTooSteep(maxSteepness, from, to)) {
                returnVertices.add(to);

                return returnVertices;
            }
        }
        Set<PathVertex> endShapeVertices = new HashSet<PathVertex>();
        for (int index : endShape.vertexIndexPointer) {
            PathVertex curPathVertex = endShape.parentMesh.pathVertexList.get(index);
            endShapeVertices.add(curPathVertex);
        }

glPushMatrix();
glRotated(-GameCamera.ang.x, 1, 0, 0);
glRotated(-GameCamera.ang.y, 0, 1, 0);
glRotated(-GameCamera.ang.z, 0, 0, 1);
glTranslated(-GameCamera.pos.x, -GameCamera.pos.y, -GameCamera.pos.z);
glColor4f(1, 1, 0, 1);
Sphere sphere = new Sphere();
for (int i : endShape.vertexIndexPointer) {
    PathVertex pv = endShape.parentMesh.pathVertexList.get(i);
    glPushMatrix();
    glTranslatef(pv.x, pv.y, pv.z);
    sphere.draw(0.3f, 16, 16);
    glPopMatrix();
}
glPopMatrix();

        //if theres a max value the little dude can go before he gives up, make this it
        double shortestPathLength = Short.MAX_VALUE;
        List<PathVertex> currentVertexList = new LinkedList<PathVertex>();
        List<ArrayList<Point3f>> currentTraversedVerticesList = new ArrayList<ArrayList<Point3f>>();
        //keeps track of the path length
        List<Float> currentLengthList = new LinkedList<Float>();
        //makes sure the little dude can jump up to the next part of the path
        List<Float> currentTooSteepYHeightList = new LinkedList<Float>();
        List<HashSet<Integer>> alreadyVisitedVertexIndicesList = new LinkedList<HashSet<Integer>>();

        //start the path
        for (int index : startShape.vertexIndexPointer) {
            PathVertex curPathVertex = startShape.parentMesh.pathVertexList.get(index);
            currentVertexList.add(curPathVertex);

            ArrayList<Point3f> currentTraversedVertices = new ArrayList<Point3f>();
            currentTraversedVertices.add(curPathVertex);
            currentTraversedVerticesList.add(currentTraversedVertices);

            //TODO make pathvertex extend point3d and dont recreate every time
            currentLengthList.add(MathUtils.quickLength(from, curPathVertex));

            if (isTooSteep(maxSteepness, from, curPathVertex)) {
                currentTooSteepYHeightList.add(curPathVertex.y - from.y);
            } else {
                currentTooSteepYHeightList.add(0f);
            }

            HashSet<Integer> alreadyVisitedSet = new HashSet<Integer>();
            alreadyVisitedSet.add(index);
            alreadyVisitedVertexIndicesList.add(alreadyVisitedSet);
        }

        while (currentVertexList.size() > 0) {
            //TODO this isnt O(n) is it? since its 0?
            PathVertex currentVertex = currentVertexList.remove(0);
            ArrayList<Point3f> currentTraversedVertices = currentTraversedVerticesList.remove(0);
            float currentLength = currentLengthList.remove(0);
            float currentTooSteepYHeight = currentTooSteepYHeightList.remove(0);
            HashSet<Integer> alreadyVisitedVertexIndices = alreadyVisitedVertexIndicesList.remove(0);

            //check ledge heights
            boolean stillNotTooSteep = maxLedgeHeight == 0 || currentTooSteepYHeight < maxLedgeHeight;
            boolean stillNotTooLong = currentLength < shortestPathLength;
            //TODO mightneed to put this after the last for loop
            if (stillNotTooSteep && stillNotTooLong) {
                //get all connected shapes
                for (PathConvexShape adjacentShape : currentVertex.connectedShapes) {
                    //get all vertices in the current shape
                    for (int nextVertexIndex : adjacentShape.vertexIndexPointer) {
                        //if you can maybe reach the end
                        if (endShapeVertices.contains(currentVertex) && !alreadyVisitedVertexIndices.contains(nextVertexIndex)) {
                            float addLength = MathUtils.quickLength(currentVertex, to);

                            //TODO might have to say or travel length is less than guy width so its not a tiny little ledge
                            float addYHeight = 0;
                            if (isTooSteep(maxSteepness, currentVertex, to)) {
                                addYHeight += to.y - currentVertex.y;
                            } else {
                                //too steep height resets if it finds an okay spot to walk on
                                addYHeight = -currentTooSteepYHeight;
                            }

                            boolean pathToEndNotTooSteep = maxLedgeHeight == 0 || currentTooSteepYHeight + addYHeight < maxLedgeHeight;
                            boolean pathToEndNotTooLong = currentLength + addLength < shortestPathLength;

                            if (pathToEndNotTooSteep && pathToEndNotTooLong) {
                                ArrayList<Point3f> currentTraversedVerticesClone = (ArrayList<Point3f>) currentTraversedVertices.clone();
                                currentTraversedVerticesClone.add(to);

                                shortestPathLength = currentLength + addLength;
                                returnVertices = currentTraversedVerticesClone;
                            }
                        //if you havent already visited it and cant immediately reach the end
                        } else if (!alreadyVisitedVertexIndices.contains(nextVertexIndex)) {
                            //go on to the next vertex
                            //updated the values for the move
                            PathVertex nextVertex = adjacentShape.parentMesh.pathVertexList.get(nextVertexIndex);

                            //TODO make nextvertex extend point3d and then get rid of the object creation
                            ArrayList<Point3f> currentTraversedVerticesClone = (ArrayList<Point3f>) currentTraversedVertices.clone();
                            currentTraversedVerticesClone.add(nextVertex);

                            HashSet<Integer> alreadyVisitedVertexIndicesClone = (HashSet<Integer>) alreadyVisitedVertexIndices.clone();
                            alreadyVisitedVertexIndicesClone.add(nextVertexIndex);

                            float addLength = MathUtils.quickLength(currentVertex, nextVertex);

                            float addYHeight = 0;
                            //TODO might have to say or travel length is less than guy width so its not a tiny little ledge
                            if (isTooSteep(maxSteepness, currentVertex, nextVertex)) {
                                addYHeight += nextVertex.y - currentVertex.y;
                            } else {
                                //too steep height resets if it finds an okay spot
                                addYHeight = -currentTooSteepYHeight;
                            }

                            currentVertexList.add(nextVertex);
                            currentTraversedVerticesList.add(currentTraversedVerticesClone);
                            currentLengthList.add(currentLength + addLength);
                            currentTooSteepYHeightList.add(currentTooSteepYHeight + addYHeight);
                            alreadyVisitedVertexIndicesList.add(alreadyVisitedVertexIndicesClone);
                        }
                    }
                }
            }
        }

        return returnVertices;
    }

    public static boolean isTooSteep(double maxSteepness, Point3f from, Point3f to) {
        double steepness = MathUtils.calculateSteepnessAngle(from, to);
        return steepness > 0 && steepness > maxSteepness;
    }
}
