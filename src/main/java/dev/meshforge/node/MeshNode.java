package dev.meshforge.node;

import dev.meshforge.network.Transport;
import dev.meshforge.packet.MeshPacket;

public final class MeshNode {

    private final NodeId id;
    private final Transport transport;

    public MeshNode(NodeId id, Transport transport) {
        this.id = id;
        this.transport = transport;

        transport.onReceive(this::receive);
    }

    public NodeId id() {
        return id;
    }

    public void send(NodeId destination, String message) {
        MeshPacket packet = new MeshPacket(
                id,
                destination,
                System.nanoTime(),
                message
        );

        System.out.println(
                "[" + id + "] Sending: " + message
        );

        transport.send(packet);
    }

    private void receive(MeshPacket packet) {
        if (!packet.destination().equals(id)) {
            return;
        }

        System.out.println(
                "[" + id + "] Received from "
                        + packet.source()
                        + ": "
                        + packet.payload()
        );
    }
}