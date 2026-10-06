package dev.meshforge.network;

import dev.meshforge.packet.MeshPacket;

import java.util.function.Consumer;

public interface Transport {

    void send(MeshPacket packet);

    void onReceive(Consumer<MeshPacket> handler);
}