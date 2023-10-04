// Thanks gadget for this wonderful mixin
package de.ari24.packetlogger.mixin;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.IndexedIterable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets="net/minecraft/network/packet/s2c/play/ChunkData$BlockEntityData")
public class ChunkDataBlockEntityDataMixin {
    @Unique
    private int originalBlockEntityTypeId = -1;

    @Inject(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;readRegistryValue(Lnet/minecraft/util/collection/IndexedIterable;)Ljava/lang/Object;"))
    private void packetLogger$saveId(PacketByteBuf buf, CallbackInfo ci) {
        int readerIdx = buf.readerIndex();

        try {
            originalBlockEntityTypeId = buf.readVarInt();
        } finally {
            buf.readerIndex(readerIdx);
        }
    }

    @Redirect(method="write", at=@At(value="INVOKE", target="Lnet/minecraft/network/PacketByteBuf;writeRegistryValue(Lnet/minecraft/util/collection/IndexedIterable;Ljava/lang/Object;)V"))
    private void packetLogger$writeId(PacketByteBuf instance, IndexedIterable<BlockEntityType<?>> registry, Object value) {
        if (value == null) {
            instance.writeVarInt(originalBlockEntityTypeId);
        } else {
            instance.writeRegistryValue(registry, (BlockEntityType<?>) value);
        }
    }
}
