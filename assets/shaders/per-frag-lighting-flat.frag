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
    vec4 color = vec4(0, 0, 0, 1);
    vec4 normNormal;
    float nDotL;
    float nDotHV;

    normNormal = normal;

    for (int i = 0; i < lightCount; i++) {
        vec4 normHalfVector = normalize(halfVector[i]);

        //directional
        if (lights[i].lightType == 0) {
            //color = ambient; //for directional TODO but maybe for point because idk if ambientglobal is necessary

            vec4 newLightDir = normalize(lightModelViewMatrix * lights[i].lightDir);
            nDotL = max(dot(normNormal, newLightDir), 0.0); //for directional

            if (nDotL > 0.0) {
                color += diffuse[i] * nDotL;
                nDotHV = max(dot(normNormal, normHalfVector), 0.0);
                color += gl_FrontMaterial.specular * lights[i].lightSpecular * pow(nDotHV, gl_FrontMaterial.shininess);
            }
        //point
        } else if (lights[i].lightType == 1) {
            //color = ambientGlobal;

            vec4 newLightDir = lightModelViewMatrix * lights[i].lightPos - vertexPos;
            float dist = length(newLightDir);

            nDotL = max(dot(normNormal, normalize(newLightDir)), 0.0);

            if (nDotL > 0.0) {
                //TODO calculate constant, linear, and quadratic attenuation based on brightness?
                float constantAttenuation = 0.05;
                float linearAttenuation = 0.02;
                float quadraticAttenuation = 0.03;
                float att = 1.0 / (constantAttenuation + linearAttenuation * dist + quadraticAttenuation * dist * dist);
                color += att * (diffuse[i] * nDotL + ambient);

                nDotHV = max(dot(normNormal, normHalfVector), 0.0);
                color += att * gl_FrontMaterial.specular * lights[i].lightSpecular * pow(nDotHV, gl_FrontMaterial.shininess);
            }
        //spotlight
        } else if (lights[0].lightType == 2) {
            //color = ambientGlobal;

            float spotCosCutoff = 0.8;

            vec4 newLightDir = lightModelViewMatrix * lights[i].lightDir;
            vec4 newLightDirToFragment = lightModelViewMatrix * lights[i].lightPos - vertexPos;
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

                    color += att * (diffuse[i] * nDotL + ambient);

                    nDotHV = max(dot(normNormal, normHalfVector), 0.0);
                    color += att * gl_FrontMaterial.specular * lights[i].lightSpecular * pow(nDotHV, gl_FrontMaterial.shininess);
                }
            }
        }
    }

    gl_FragColor = color;
}