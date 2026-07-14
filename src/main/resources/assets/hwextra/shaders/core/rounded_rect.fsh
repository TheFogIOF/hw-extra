#version 150

in vec4 vColor;
in vec2 vUv;

out vec4 fragColor;

uniform vec2 uSize;    // размер прямоугольника (w,h)
uniform float uRadius; // радиус скругления

float sdRoundedRect(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + vec2(r);
    return length(max(q, 0.0)) - r;
}

void main() {
    // UV0 в Minecraft обычно 0..1 по X/Y
    vec2 p = (vUv * uSize) - (uSize * 0.5);
    float d = sdRoundedRect(p, uSize * 0.5, uRadius);

    float alpha = smoothstep(0.5, -0.5, d); // антиалиасинг

    fragColor = vec4(vColor.rgb, vColor.a * alpha);
}