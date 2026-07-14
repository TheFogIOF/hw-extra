struct VertexInput {
    @location(0) position: vec2<f32>,
    @location(1) color: vec4<f32>,
};
struct VertexOutput {
    @builtin(position) position: vec4<f32>,
    @location(0) color: vec4<f32>,
    @location(1) size: vec2<f32>,
    @location(2) radius: f32,
};

// Униформы (порядок биндингов важен)
@group(0) @binding(0) var<uniform> uTransform: mat4x4<f32>;
@group(0) @binding(1) var<uniform> uSize: vec2<f32>;
@group(0) @binding(2) var<uniform> uRadius: f32;

@vertex
fn main(input: VertexInput) -> VertexOutput {
    var output: VertexOutput;
    output.position = uTransform * vec4<f32>(input.position, 0.0, 1.0);
    output.color = input.color;
    output.size = uSize;
    output.radius = uRadius;
    return output;
}

@fragment
fn main(input: VertexOutput) -> @location(0) vec4<f32> {
    return input.color;
}