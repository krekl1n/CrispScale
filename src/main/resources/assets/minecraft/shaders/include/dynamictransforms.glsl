#version 330

layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
    vec4 ColorModulator;
    vec3 ModelOffset;
    float BetterUIScaleSmoothness;
    mat4 TextureMat;
};