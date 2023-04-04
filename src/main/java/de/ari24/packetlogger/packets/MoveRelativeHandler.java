package de.ari24.packetlogger.packets;

import com.google.gson.JsonObject;
import de.ari24.packetlogger.utils.ConvertUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;

public class MoveRelativeHandler implements BasePacketHandler<EntityS2CPacket.MoveRelative> {
    @Override
    public String id() {
        return "UpdateEntityPosition";
    }

    @Override
    public JsonObject serialize(EntityS2CPacket.MoveRelative packet) {
        JsonObject jsonObject = new JsonObject();

        ClientWorld world = MinecraftClient.getInstance().world;
        Entity entity = packet.getEntity(world);

        if (entity != null) {
            jsonObject.add("entity", ConvertUtils.serializeEntity(entity));
        } else {
            jsonObject.addProperty("entity", "unknown");
        }

        jsonObject.addProperty("deltaX", packet.getDeltaX());
        jsonObject.addProperty("deltaY", packet.getDeltaY());
        jsonObject.addProperty("deltaZ", packet.getDeltaZ());
        jsonObject.addProperty("onGround", packet.isOnGround());
        return jsonObject;
    }
}
