package de.ari24.packetlogger.packets;

import com.google.gson.JsonObject;
import de.ari24.packetlogger.utils.ConvertUtils;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;

public class LoginQueryRequestS2CPacketHandler implements BasePacketHandler<LoginQueryRequestS2CPacket> {
    @Override
    public String name() {
        return "LoginPluginRequest";
    }

    @Override
    public String url() {
        return "https://wiki.vg/Protocol#Login_Plugin_Request";
    }

    @Override
    public JsonObject description() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("general", "Used to implement a custom handshaking flow together with Login Plugin Response packet.");
        jsonObject.addProperty("wikiVgNotes", "Unlike plugin messages in \"play\" mode, these messages follow a lock-step request/response scheme, where the client is expected to respond to a request indicating whether it understood. The notchian client always responds that it hasn't understood, and sends an empty payload.\nIn Notchian client, the maximum data length is 1048576 bytes.");
        jsonObject.addProperty("messageId", "Generated by the server - should be unique to the connection.");
        jsonObject.addProperty("channel", "Name of the plugin channel used to send the data.");
        jsonObject.addProperty("data", "Any data, depending on the channel. The length of this array must be inferred from the packet length.");
        return jsonObject;
    }

    @Override
    public JsonObject serialize(LoginQueryRequestS2CPacket packet) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("messageId", packet.getQueryId());
        jsonObject.addProperty("channel", packet.getChannel().toString());
        jsonObject.add("data", ConvertUtils.GSON_INSTANCE.toJsonTree(packet.getPayload().array()));
        return jsonObject;
    }
}