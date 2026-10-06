package dev.meshforge.network;

import dev.meshforge.packet.MeshPacket;

import java.util.function.Consumer;

public class InMemoryTransport implements Transport {

    private Consumer<MeshPacket> receiver;

    @Override
    public void send(MeshPacket packet) {
        if (receiver != null) {
            receiver.accept(packet);
        }
    }

    @Override
    public void onReceive(Consumer<MeshPacket> handler) {
        this.receiver = handler;
    }
}