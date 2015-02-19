uniform int lightType;
uniform mat4 lightModelViewMatrix;
uniform vec3 lightDirPos;
uniform vec4 lightDiffuse;
uniform vec4 lightAmbient;
uniform vec4 lightSpecular;

varying vec4 diffuse;
varying vec4 ambient;
varying vec4 ambientGlobal; //TODO also I dont understand why this is necessary since it never changes
varying vec4 vertexPos;
varying vec3 normal;
varying vec3 halfVector;

void main() {
    diffuse = gl_FrontMaterial.diffuse * lightDiffuse;
    ambient = gl_FrontMaterial.ambient * lightAmbient;

    //directional
    if (lightType == 0) {
        normal = normalize(gl_ModelViewMatrix * vec4(gl_Normal, 0));

        vec3 newVertToEyeVector = normalize(vec3(-gl_ModelViewMatrix * gl_Vertex));

        vec3 lightDir = normalize(lightModelViewMatrix * vec4(lightDirPos, 0));
        halfVector = vec3(lightDir + newVertToEyeVector);

        ambient += gl_LightModel.ambient * gl_FrontMaterial.ambient;
    //point
    } else if (lightType == 1) {
        vec3 pos = gl_ModelViewMatrix * gl_Vertex;

        //compute the direction to eye vector and normalize it
        vec3 vertToEyeVector = normalize(-pos);

        //transform the normal into eye space and normalize
        normal = normalize(gl_NormalMatrix * gl_Normal);

        //computer the vertex position in camera space
        vertexPos = gl_ModelViewMatrix * gl_Vertex;

        //calculate the light position in camera space
        vec4 lightPos = lightModelViewMatrix * vec4(lightDirPos, 1); //TODO is it necessary to make it vec4? maybe use normal matrix?

        //calculate the vector from vertex to light
        vec4 lightToVertVector = normalize(vertexPos - lightPos); //TODO might be backwards

        //the halfway vector between the eye dir and the light dir
        halfVector = vec3(lightToVertVector.x, lightToVertVector.y, lightToVertVector.z) + vertToEyeVector; //for directional

        ambientGlobal = gl_LightModel.ambient * gl_FrontMaterial.ambient;
    }

    gl_Position = ftransform();
}