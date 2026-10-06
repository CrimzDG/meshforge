package dev.meshforge.network;

import dev.meshforge.node.NodeId;
import dev.meshforge.packet.MeshPacket;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class InMemoryTransport implements Transport {

    private final NodeId owner;
    private final BiConsumer<NodeId, MeshPacket> sender;

    private Consumer<MeshPacket> receiver;

    public InMemoryTransport(
            NodeId owner,
            BiConsumer<NodeId, MeshPacket> sender
    ) {
        this.owner = owner;
        this.sender = sender;
    }

    @Override
    public void send(MeshPacket packet) {
        sender.accept(owner, packet);
    }

    @Override
    public void onReceive(Consumer<MeshPacket> handler) {
        this.receiver = handler;
    }

    public void deliver(MeshPacket packet) {
        if (receiver != null) {
            receiver.accept(packet);
        }
    }
}