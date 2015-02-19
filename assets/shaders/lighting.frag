uniform int lightType;
uniform mat4 lightModelViewMatrix;
uniform vec3 lightPos;
uniform vec3 lightDir;
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
    vec4 color;
    vec3 normNormal;
    vec3 normHalfVector;
    float nDotL;
    float nDotHV;

    normNormal = normalize(normal);
    normHalfVector = normalize(halfVector);

    //directional
    if (lightType == 0) {
        color = ambient; //for directional TODO but maybe for point because idk if ambientglobal is necessary

        vec3 newLightDir = normalize(lightModelViewMatrix * vec4(lightDir, 0));
        nDotL = max(dot(normNormal, newLightDir), 0.0); //for directional

        if (nDotL > 0.0) {
            color += diffuse * nDotL;
            nDotHV = max(dot(normNormal, normHalfVector), 0.0);
            color += gl_FrontMaterial.specular * lightSpecular * pow(nDotHV, gl_FrontMaterial.shininess);
        }
    //point
    } else if (lightType == 1) {
        color = ambientGlobal;

        vec3 newLightDir = vec3(lightModelViewMatrix * vec4(lightPos, 1) - vertexPos);
        float dist = length(newLightDir);

        nDotL = max(dot(normNormal, normalize(newLightDir)), 0.0);

        if (nDotL > 0.0) {
            //TODO calculate constant, linear, and quadratic attenuation based on brightness?
            float constantAttenuation = 0.05;
            float linearAttenuation = 0.02;
            float quadraticAttenuation = 0.03;
            float att = 1.0 / (constantAttenuation + linearAttenuation * dist + quadraticAttenuation * dist * dist);
            color += att * (diffuse * nDotL + ambient);

            nDotHV = max(dot(normNormal, normHalfVector), 0.0);
            color += att * gl_FrontMaterial.specular * lightSpecular * pow(nDotHV, gl_FrontMaterial.shininess);
        }
    //spotlight
    } else if (lightType == 2) {
        color = ambientGlobal;

        float spotCosCutoff = 0.8;

        vec3 newLightDir = vec3(lightModelViewMatrix * vec4(lightDir, 0));
        vec3 newLightDirToFragment = vec3(lightModelViewMatrix * vec4(lightPos, 1) - vertexPos);
        float dist = length(newLightDirToFragment);

        nDotL = max(dot(normNormal, normalize(newLightDirToFragment)), 0.0);

        if (nDotL > 0.0) {
            float spotEffect = dot(normalize(newLightDir), normalize(-newLightDirToFragment));
            if (spotEffect > spotCosCutoff) {
                float spotRateOfDecay = 0.2;
                //TODO calculate constant, linear, and quadratic attenuation based on brightness?
                float constantAttenuation = 0.05;
                float linearAttenuation = 0.02;
                float quadraticAttenuation = 0.01;

                spotEffect = pow(spotEffect, spotRateOfDecay);
                float att = spotEffect / (constantAttenuation + linearAttenuation * dist + quadraticAttenuation * dist * dist);

                color += att * (diffuse * nDotL + ambient);

                nDotHV = max(dot(normNormal, normHalfVector), 0.0);
                color += att * gl_FrontMaterial.specular * lightSpecular * pow(nDotHV, gl_FrontMaterial.shininess);
            }
        }
    }

    gl_FragColor = color;
}