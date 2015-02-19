uniform int lightType;
uniform mat4 lightModelViewMatrix;
uniform vec3 lightDirPos; //for directional and point
uniform vec4 lightDiffuse; //for directional
uniform vec4 lightAmbient; //for directional
uniform vec4 lightSpecular; //for directional

varying vec4 diffuse; //for directional and point
varying vec4 ambient; //for directional and point
varying vec4 ambientGlobal; //for point TODO also I dont understand why this is necessary since it never changes
varying vec4 vertexPos; //for point
varying vec3 normal; //for directional and point
varying vec3 halfVector; //for directional and point

void main() {
    vec4 color; //for directional and point
    vec3 normNormal; //for directional and point
    vec3 normHalfVector; //for directional and point
    float nDotL; //for directional and point
    float nDotHV; //for directional and point

    normNormal = normalize(normal);
    normHalfVector = normalize(halfVector);

    //directional
    if (lightType == 0) {
        color = ambient; //for directional TODO but maybe for point because idk if ambientglobal is necessary

        vec3 lightDir = normalize(lightModelViewMatrix * vec4(lightDirPos, 0));
        nDotL = max(dot(normNormal, lightDir), 0.0); //for directional

        if (nDotL > 0.0) {
            color += diffuse * nDotL;
            nDotHV = max(dot(normNormal, normHalfVector), 0.0);
            color += gl_FrontMaterial.specular * lightSpecular * pow(nDotHV, gl_FrontMaterial.shininess);
        }
    //point
    } else if (lightType == 1) {
        color = ambientGlobal;
        float dist; //for point

        vec3 lightDir = vec3(lightModelViewMatrix * vec4(lightDirPos, 1) - vertexPos);
        dist = length(lightDir);

        nDotL = max(dot(normNormal, normalize(lightDir)), 0.0);

        if (nDotL > 0.0) {
            //TODO calculate constant, linear, and quadratic attenuation based on brightness?
            float constantAttenuation = 0.05;
            float linearAttenuation = 0.02;
            float quadraticAttenuation = 0.01;
            float att = 1.0 / (constantAttenuation + linearAttenuation * dist + quadraticAttenuation * dist * dist);
            color += att * (diffuse * nDotL + ambient);

            nDotHV = max(dot(normNormal, normHalfVector), 0.0);
            color += att * gl_FrontMaterial.specular * lightSpecular * pow(nDotHV, gl_FrontMaterial.shininess);
        }
    }

    gl_FragColor = color;
}