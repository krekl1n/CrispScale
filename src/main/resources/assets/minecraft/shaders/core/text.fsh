#version 330

#if !defined(IS_GUI) && !defined(IS_SEE_THROUGH)
#moj_import <minecraft:fog.glsl>
#endif

#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

#if !defined(IS_GUI) && !defined(IS_SEE_THROUGH)
in float sphericalVertexDistance;
in float cylindricalVertexDistance;
#endif

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
#ifdef IS_GRAYSCALE
    vec4 texColor = texture(Sampler0, texCoord0).rrrr;
#else
    vec4 texColor;
    if (BetterUIScaleSmoothness == 0.0f) {
        vec2 texSize = textureSize(Sampler0, 0);
        vec2 tx = texCoord0.xy * texSize;
        vec2 uv = (floor(tx) + 0.5) / texSize;
        texColor = texture(Sampler0, uv);
    } else {
        // Based on https://youtu.be/d6tp43wZqps?t=625 by t3ssel8r
        vec2 texSize = textureSize(Sampler0, 0);
        vec2 boxSize = clamp(fwidth(texCoord0.xy) * BetterUIScaleSmoothness * texSize, 1e-5, 1.0);
        vec2 tx = texCoord0.xy * texSize - 0.5 * boxSize;
        vec2 txOffset = smoothstep(vec2(1.0) - boxSize, vec2(1.0), fract(tx));
        vec2 uv = (floor(tx) + 0.5 + txOffset) / texSize;
        texColor = textureGrad(Sampler0, uv, dFdx(texCoord0.xy), dFdy(texCoord0.xy));
    }
#endif

#ifdef IS_SEE_THROUGH
    vec4 color = texColor * vertexColor;
#else
    vec4 color = texColor * vertexColor * ColorModulator;
#endif
    if (color.a < 0.1) {
        discard;
    }

#ifdef IS_SEE_THROUGH
    fragColor = color * ColorModulator;
#elif defined(IS_GUI)
    fragColor = color;
#else
    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
#endif
}
