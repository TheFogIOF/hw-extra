package sk.thefogiof.hwextra.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.nio.charset.StandardCharsets;

public class FeatureControlPayload {
    public record FeatureControlPacket(String json) implements CustomPacketPayload {
        public static final Identifier CHANNEL = Identifier.fromNamespaceAndPath("liteapi", "feature-control");

        public static final CustomPacketPayload.Type<FeatureControlPacket> TYPE = new CustomPacketPayload.Type<>(CHANNEL);

        static StreamCodec<ByteBuf, String> STREAM_CODEC = new StreamCodec<ByteBuf, String>() {
            public String decode(ByteBuf byteBuf) {
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                return new String(bytes, StandardCharsets.UTF_8);
            }

            public void encode(ByteBuf byteBuf, String string) {
                byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
                byteBuf.writeBytes(bytes);
            }
        };

        public static final StreamCodec<RegistryFriendlyByteBuf, FeatureControlPacket> CODEC = StreamCodec.composite(STREAM_CODEC, FeatureControlPacket::json, FeatureControlPacket::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
