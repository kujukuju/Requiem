#version 120

struct Light {
    int lightType;
    vec4 lightPos;
    vec4 lightDir;
    vec4 lightDiffuse;
    vec4 lightAmbient;
    vec4 lightSpecular;
};
uniform Light lights[8];
uniform int lightCount;
uniform mat4 lightModelViewMatrix;

varying vec4 diffuse[8];
varying vec4 ambient;
varying vec4 ambientGlobal; //TODO also I dont understand why this is necessary since it never changes
varying vec4 vertexPos;
varying vec4 halfVector[8];
varying vec4 normal;

void main() {
    ambient = vec4(0, 0, 0, 0);

    //transform the normal into eye space and normalize
    normal = normalize(gl_ModelViewMatrix * vec4(gl_Normal, 0));

    for (int i = 0; i < lightCount; i++) {
        diffuse[i] = gl_FrontMaterial.diffuse * lights[i].lightDiffuse;
        ambient += gl_FrontMaterial.ambient * lights[i].lightAmbient;

        //directional
        if (lights[i].lightType == 0) {
            vec4 newVertToEyeVector = normalize(vec4(0, 0, 0, 1) - gl_ModelViewMatrix * gl_Vertex);

            vec4 newLightDir = normalize(lightModelViewMatrix * lights[i].lightDir);
            halfVector[i] = newLightDir + newVertToEyeVector;

            ambient += gl_LightModel.ambient * gl_FrontMaterial.ambient;
        //point or spotlight
        } else if (lights[i].lightType == 1 || lights[i].lightType == 2) {
            vec4 pos = gl_ModelViewMatrix * gl_Vertex;

            //compute the direction to eye vector and normalize it
            vec4 vertToEyeVector = normalize(vec4(0, 0, 0, 1) - pos);

            //computer the vertex position in camera space
            vertexPos = gl_ModelViewMatrix * gl_Vertex;

            //calculate the light position in camera space
            vec4 newLightPos = lightModelViewMatrix * lights[i].lightPos; //TODO is it necessary to make it vec4? maybe use normal matrix?

            //calculate the vector from vertex to light
            vec4 lightToVertVector = normalize(vertexPos - newLightPos); //TODO might be backwards

            //the halfway vector between the eye dir and the light dir
            halfVector[i] = lightToVertVector + vertToEyeVector;
        }
    }

    gl_Position = ftransform();
}