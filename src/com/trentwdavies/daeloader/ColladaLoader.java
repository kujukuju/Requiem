package com.trentwdavies.daeloader;

import com.requiem.utilities.MatrixUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Trent on 11/4/2014.
 */
public class ColladaLoader {
    public static Model loadFile(String colladaFilePath) throws ParserConfigurationException, IOException, SAXException {
        Model returnModel = new Model();

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new FileInputStream(colladaFilePath));
        document.getDocumentElement().normalize();//TODO is this necessary?

        Map<String, Geometry> idToGeometryMap = new HashMap<String, Geometry>();

        //library_geometries should be unique
        Node libraryGeometriesNode = document.getElementsByTagName("library_geometries").item(0);
        NodeList geometryNodeList = libraryGeometriesNode.getChildNodes();
        for (int i = 0; i < geometryNodeList.getLength(); i++) {
            Node curGeometryNode = geometryNodeList.item(i);

            if (curGeometryNode.getNodeType() == Node.ELEMENT_NODE) {
                Geometry curModelGeometry = retrieveGeometry(curGeometryNode);

                //if it has vertices and stuff
                if (curModelGeometry != null) {
                    returnModel.addGeometries(curModelGeometry);

                    idToGeometryMap.put(curModelGeometry.id, curModelGeometry);
                }
            }
        }

        Node libraryMaterialsNode = document.getElementsByTagName("library_materials").item(0);
        if (libraryMaterialsNode != null) {
            Map<String, String> materialIdEffectIdReferenceMap = retrieveMaterialIdEffectIdMap(libraryMaterialsNode);

            Node libraryEffectsNode = document.getElementsByTagName("library_effects").item(0);
            Map<String, Effect> effectIdEffectMap = retrieveEffectIdEffectMap(libraryEffectsNode);

            Map<String, Effect> materialEffectMap = new HashMap<String, Effect>();
            Set<String> materialIdEffectIdKeySet = materialIdEffectIdReferenceMap.keySet();
            for (String materialId : materialIdEffectIdKeySet) {
                String effectId = materialIdEffectIdReferenceMap.get(materialId);
                Effect effect = effectIdEffectMap.get(effectId);

                materialEffectMap.put(materialId, effect);
            }
            returnModel.setMaterialEffectMap(materialEffectMap);
        }

        //apply transformation matrices and switch y and z
        double radians = -Math.PI / 2;
        Matrix4d rotateRollMatrix = new Matrix4d(
                1, 0, 0, 0,
                0, Math.cos(radians), -Math.sin(radians), 0,
                0, Math.sin(radians), Math.cos(radians), 0,
                0, 0, 0, 1);
        Node libraryVisualScenesNode = document.getElementsByTagName("library_visual_scenes").item(0);
        Element libraryVisualScenesElement = (Element) libraryVisualScenesNode;
        Node visualScene = libraryVisualScenesElement.getElementsByTagName("visual_scene").item(0);
        NodeList nodeList = visualScene.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node curNode = nodeList.item(i);

            if (curNode.getNodeType() == Node.ELEMENT_NODE) {
                Element curNodeElement = (Element) curNode;
                NodeList instanceGeometryList = curNodeElement.getElementsByTagName("instance_geometry");
                if (instanceGeometryList.getLength() > 0) {
                    String nodeId = ((Element) instanceGeometryList.item(0)).getAttribute("url");
                    //to go from #Cube_001-mesh to Cube_001-mesh
                    nodeId = nodeId.substring(1, nodeId.length());

                    if (idToGeometryMap.containsKey(nodeId)) {
                        Geometry curGeometry = idToGeometryMap.get(nodeId);

                        Node matrixNode = curNodeElement.getElementsByTagName("matrix").item(0);
                        String[] matrixParts = matrixNode.getTextContent().split(" ");
                        double[] matrixPartsDouble = new double[matrixParts.length];
                        for (int a = 0; a < matrixParts.length; a++) {
                            matrixPartsDouble[a] = Double.parseDouble(matrixParts[a]);
                        }
                        Matrix4d transformMatrixZUp = new Matrix4d(matrixPartsDouble);

                        for (int a = 0; a < curGeometry.vertexList.size(); a++) {
                            Point3d curVertex = curGeometry.vertexList.get(a);
                            double[] curVertexMatrix = {curVertex.x, curVertex.y, curVertex.z};
                            double[] newVertexZUpMatrix = MatrixUtils.mult(transformMatrixZUp, curVertexMatrix);
                            double[] newVertexYUpMatrix = MatrixUtils.mult(rotateRollMatrix, newVertexZUpMatrix);

                            Point3d newVertexYUp = new Point3d(newVertexYUpMatrix[0], newVertexYUpMatrix[1], newVertexYUpMatrix[2]);
                            curGeometry.vertexList.set(a, newVertexYUp);
                        }
                        for (int a = 0; a < curGeometry.normalList.size(); a++) {
                            Vector3d curNormal = curGeometry.normalList.get(a);

                            Vector3d newNormal = new Vector3d(curNormal.x, curNormal.z, curNormal.y);
                            curGeometry.normalList.set(a, newNormal);
                        }
                    }
                }
            }
        }

        return returnModel;
    }

    public static Map<String, Effect> retrieveEffectIdEffectMap(Node libraryEffectsNode) {
        Map<String, Effect> effectIdEffectMap = new HashMap<String, Effect>();

        Element libraryEffectsElement = (Element) libraryEffectsNode;
        NodeList effectNodeList = libraryEffectsElement.getElementsByTagName("effect");

        for (int i = 0; i < effectNodeList.getLength(); i++) {
            Node curEffectNode = effectNodeList.item(i);
            Element curEffectElement = (Element) curEffectNode;

            String curEffectId = curEffectElement.getAttribute("id");

            Node profileCommonNode = curEffectElement.getElementsByTagName("profile_COMMON").item(0);
            Element profileCommonElement = (Element) profileCommonNode;

            Node techniqueNode = profileCommonElement.getElementsByTagName("technique").item(0);
            Element techniqueElement = (Element) techniqueNode;

            Effect curEffect = getEffect(techniqueElement);

            effectIdEffectMap.put(curEffectId, curEffect);
        }

        return effectIdEffectMap;
    }

    private static Effect getEffect(Element techniqueElement) {
        Effect curEffect = new Effect();

        Node phongNode = techniqueElement.getElementsByTagName("phong").item(0);
        Node lambertNode = techniqueElement.getElementsByTagName("lambert").item(0);
        if (phongNode != null) {
            Element phongElement = (Element) phongNode;

            Node emissionNode = phongElement.getElementsByTagName("emission").item(0);
            double[] emissionData = getEffectValueArray(emissionNode);
            curEffect.setEmission(emissionData);

            Node ambientNode = phongElement.getElementsByTagName("ambient").item(0);
            double[] ambientData = getEffectValueArray(ambientNode);
            curEffect.setAmbient(ambientData);

            Node diffuseNode = phongElement.getElementsByTagName("diffuse").item(0);
            double[] diffuseData = getEffectValueArray(diffuseNode);
            curEffect.setDiffuse(diffuseData);

            Node specularNode = phongElement.getElementsByTagName("specular").item(0);
            double[] specularData = getEffectValueArray(specularNode);
            curEffect.setSpecular(specularData);

            Node shininessNode = phongElement.getElementsByTagName("shininess").item(0);
            Element shininessElement = (Element) shininessNode;
            Node shininessDataNode = shininessElement.getElementsByTagName("float").item(0);
            double shininessData = Double.parseDouble(shininessDataNode.getTextContent());
            curEffect.setShininess(shininessData);

            Node transparencyNode = phongElement.getElementsByTagName("transparency").item(0);
            if (transparencyNode != null) {
                Element transparencyElement = (Element) transparencyNode;
                Node transparencyDataNode = transparencyElement.getElementsByTagName("float").item(0);
                double transparencyData = Double.parseDouble(transparencyDataNode.getTextContent());
                curEffect.setTransparency(transparencyData);
            }
        } else if (lambertNode != null) {
            Element lambertElement = (Element) lambertNode;

            Node emissionNode = lambertElement.getElementsByTagName("emission").item(0);
            double[] emissionData = getEffectValueArray(emissionNode);
            curEffect.setEmission(emissionData);

            Node ambientNode = lambertElement.getElementsByTagName("ambient").item(0);
            double[] ambientData = getEffectValueArray(ambientNode);
            curEffect.setAmbient(ambientData);

            Node diffuseNode = lambertElement.getElementsByTagName("diffuse").item(0);
            double[] diffuseData = getEffectValueArray(diffuseNode);
            curEffect.setDiffuse(diffuseData);

            double[] specularData = {1, 1, 1, 1};
            curEffect.setSpecular(specularData);
            curEffect.setShininess(0);

            Node transparencyNode = lambertElement.getElementsByTagName("transparency").item(0);
            if (transparencyNode != null) {
                Element transparencyElement = (Element) transparencyNode;
                Node transparencyDataNode = transparencyElement.getElementsByTagName("float").item(0);
                double transparencyData = Double.parseDouble(transparencyDataNode.getTextContent());
                curEffect.setTransparency(transparencyData);
            }
        } else {
            throw new RuntimeException("No Shading Exists");
        }

        return curEffect;
    }

    private static double[] getEffectValueArray(Node effectPartNode) {
        Element effectPartElement = (Element) effectPartNode;
        Node effectColorNode = effectPartElement.getElementsByTagName("color").item(0);
        String[] colorStringArray = effectColorNode.getTextContent().split(" ");
        double[] colorValueArray = new double[colorStringArray.length];
        for (int a = 0; a < colorStringArray.length; a++) {
            colorValueArray[a] = Double.parseDouble(colorStringArray[a]);
        }

        return colorValueArray;
    }

    private static Map<String, String> retrieveMaterialIdEffectIdMap(Node libraryMaterialsNode) {
        Map<String, String> materialIdEffectIdMap = new HashMap<String, String>();

        Element libraryMaterialsElement = (Element) libraryMaterialsNode;
        NodeList materialReferenceNodeList = libraryMaterialsElement.getElementsByTagName("material");

        for (int i = 0; i < materialReferenceNodeList.getLength(); i++) {
            Node curMaterialReferenceNode = materialReferenceNodeList.item(i);
            Element curMaterialReferenceElement = (Element) curMaterialReferenceNode;

            String referenceMaterialId = curMaterialReferenceElement.getAttribute("id");

            Node instanceEffectNode = curMaterialReferenceElement.getElementsByTagName("instance_effect").item(0);
            String referenceEffectId = ((Element) instanceEffectNode).getAttribute("url");
            referenceEffectId = referenceEffectId.substring(1, referenceEffectId.length());

            materialIdEffectIdMap.put(referenceMaterialId, referenceEffectId);
        }

        return materialIdEffectIdMap;
    }

    private static Geometry retrieveGeometry(Node curGeometry) {
        Geometry returnGeometry = new Geometry();

        Element curGeometryElement = (Element) curGeometry;
        String curGeometryId = ((Element) curGeometry).getAttribute("id");
        Node mesh = curGeometryElement.getElementsByTagName("mesh").item(0);

        returnGeometry.setId(curGeometryId);

        if (mesh.getNodeType() == Node.ELEMENT_NODE) {
            Element meshElement = (Element) mesh;
            NodeList sourceList = meshElement.getElementsByTagName("source");
            List<Point3d> meshPositions = new ArrayList<Point3d>();
            List<Vector3d> meshNormals = new ArrayList<Vector3d>();
            List<Point3d> meshColors = new ArrayList<Point3d>();

            for (int i = 0; i < sourceList.getLength(); i++) {
                Node curSource = sourceList.item(i);

                if (curSource.getNodeType() == Node.ELEMENT_NODE) {
                    Element curSourceElement = (Element) curSource;
                    String idAttribute = curSourceElement.getAttribute("id");
                    if (idAttribute.equals(curGeometryId + "-positions")) {
                        Node floatArrayNode = curSourceElement.getElementsByTagName("float_array").item(0);
                        Element floatArrayElement = (Element) floatArrayNode;
                        if (floatArrayElement.getAttribute("count").equals("0")) {
                            return null;
                        }
                        String[] floatArraySplit = floatArrayNode.getTextContent().split(" ");

                        for (int a = 0; a < floatArraySplit.length; a += 3) {
                            double xValue = Double.parseDouble(floatArraySplit[a]);
                            double yValue = Double.parseDouble(floatArraySplit[a + 1]);
                            double zValue = Double.parseDouble(floatArraySplit[a + 2]);
                            Point3d curMeshPoint = new Point3d(xValue, yValue, zValue);

                            meshPositions.add(curMeshPoint);
                        }
                    } else if (idAttribute.equals(curGeometryId + "-normals")) {
                        Node floatArrayNode = curSourceElement.getElementsByTagName("float_array").item(0);
                        String[] floatArraySplit = floatArrayNode.getTextContent().split(" ");

                        for (int a = 0; a < floatArraySplit.length; a += 3) {
                            double xValue = Double.parseDouble(floatArraySplit[a]);
                            double yValue = Double.parseDouble(floatArraySplit[a + 1]);
                            double zValue = Double.parseDouble(floatArraySplit[a + 2]);
                            Vector3d curNormalVector = new Vector3d(xValue, yValue, zValue);
                            curNormalVector.normalize();

                            meshNormals.add(curNormalVector);
                        }
                    } else if (idAttribute.equals(curGeometryId + "-colors") || idAttribute.equals(curGeometryId + "-colors-Col")) {
                                Node floatArrayNode = curSourceElement.getElementsByTagName("float_array").item(0);
                                String[] floatArraySplit = floatArrayNode.getTextContent().split(" ");

                                for (int a = 0; a < floatArraySplit.length; a += 3) {
                                    double xValue = Double.parseDouble(floatArraySplit[a]);
                                    double yValue = Double.parseDouble(floatArraySplit[a + 1]);
                                    double zValue = Double.parseDouble(floatArraySplit[a + 2]);
                            Point3d curColorPoint = new Point3d(xValue, yValue, zValue);

                            meshColors.add(curColorPoint);
                        }
                    }
                }
            }

            Point3d[] castingPoint3dPositionsArray = new Point3d[meshPositions.size()];
            returnGeometry.addVertices(meshPositions.toArray(castingPoint3dPositionsArray));

            Vector3d[] castingVector3dNormalsArray = new Vector3d[meshNormals.size()];
            returnGeometry.addNormals(meshNormals.toArray(castingVector3dNormalsArray));

            Point3d[] castingPoint3dColorsArray = new Point3d[meshColors.size()];
            returnGeometry.addColors(meshColors.toArray(castingPoint3dColorsArray));

            NodeList polylistList = meshElement.getElementsByTagName("polylist");

            for (int i = 0; i < polylistList.getLength(); i++) {
                Node polylist = polylistList.item(i);

                GeometryObject returnGeometryObject = retrieveGeometryObject(polylist);

                returnGeometry.addGeometryObjects(returnGeometryObject);
            }
        }

        return returnGeometry;
    }

    public static GeometryObject retrieveGeometryObject(Node polylist) {
        GeometryObject returnGeometryObject = new GeometryObject();

        Element polylistElement = (Element) polylist;

        String materialReferenceId = polylistElement.getAttribute("material");
        returnGeometryObject.setMaterialReference(materialReferenceId);

        //TODO these better always be in this order
        boolean normalsExist = false;
        boolean colorsExist = false;
        NodeList inputNodeList = polylistElement.getElementsByTagName("input");
        for (int b = 0; b < inputNodeList.getLength(); b++) {
            Node curInputNode = inputNodeList.item(b);
            Element curInputElement = (Element) curInputNode;

            String curInputSemantic = curInputElement.getAttribute("semantic");
            if (curInputSemantic.equals("NORMAL")) {
                normalsExist = true;
            } else if (curInputSemantic.equals("COLOR")) {
                colorsExist = true;
            }
        }

        Node vcountNode = polylistElement.getElementsByTagName("vcount").item(0);
        String[] vcountData = vcountNode.getTextContent().split(" ");
        int[] vcountIntegerData = new int[vcountData.length];
        for (int i = 0; i < vcountData.length; i++) {
            vcountIntegerData[i] = Integer.parseInt(vcountData[i]);
        }

        Node pNode = polylistElement.getElementsByTagName("p").item(0);
        String[] pData = pNode.getTextContent().split(" ");
        int[] pIntegerData = new int[pData.length];
        for (int b = 0; b < pData.length; b++) {
            pIntegerData[b] = Integer.parseInt(pData[b]);
        }

        int curPIndex = 0;
        for (int i = 0; i < vcountIntegerData.length; i++) {
            Face returnFace = new Face();
            int vertexCount = vcountIntegerData[i];

            for (int a = 0; a < vertexCount; a++) {
                int vertexIndexPosition = pIntegerData[curPIndex];
                returnFace.addVertexPointer(vertexIndexPosition);
                curPIndex++;

                if (normalsExist) {
                    int normalIndexPosition = pIntegerData[curPIndex];
                    returnFace.addNormalPointer(normalIndexPosition);
                    curPIndex++;
                }

                if (colorsExist) {
                    int colorIndexPosition = pIntegerData[curPIndex];
                    returnFace.addColorPointer(colorIndexPosition);
                    curPIndex++;
                }
            }

            returnGeometryObject.addFaces(returnFace);
        }

        return returnGeometryObject;
    }
}