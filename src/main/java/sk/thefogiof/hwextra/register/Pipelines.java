package sk.thefogiof.hwextra.register;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class Pipelines {
    public static RenderPipeline ROUNDED_RECT_PIPELINE;

    public static void register() {
        ROUNDED_RECT_PIPELINE =
                RenderPipelines.register(
                        RenderPipeline.builder(RenderPipelines.GUI_SNIPPET)
                                .withLocation(Identifier.fromNamespaceAndPath("hwextra", "pipeline/rounded_rect"))
                                .withVertexShader(Identifier.fromNamespaceAndPath("hwextra", "core/rounded_rect"))
                                .withFragmentShader(Identifier.fromNamespaceAndPath("hwextra", "core/rounded_rect"))
                                .withUniform("uSize", UniformType.UNIFORM_BUFFER)
                                .withUniform("uRadius", UniformType.UNIFORM_BUFFER)
                                .withBlend(BlendFunction.TRANSLUCENT)
                                .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                                .build()
                );
    }
}
