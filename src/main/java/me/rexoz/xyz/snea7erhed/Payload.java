package me.rexoz.xyz.snea7erhed;

import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Payload {
    private final Player player;

    private final String ch;

    private final String meth;

    public Payload(Player player, String ch, String meth) {
        this.player = player;
        this.ch = ch;
        this.meth = meth;
    }

    public void send() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(this.meth);
        } catch (IOException iOException) {}
        PacketDataSerializer serializer = new PacketDataSerializer(Unpooled.wrappedBuffer(stream.toByteArray()));
        PacketPlayOutCustomPayload pay = new PacketPlayOutCustomPayload(this.ch, serializer);
        (((CraftPlayer)this.player).getHandle()).playerConnection.sendPacket((Packet<?>)pay);
    }
}