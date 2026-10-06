package dev.meshforge.node;

import dev.meshforge.network.Transport;
import dev.meshforge.packet.MeshPacket;
import dev.meshforge.packet.PacketType;
import dev.meshforge.packet.Position;

import java.util.HashSet;
import java.util.Set;

public final class MeshNode {

    private static final int DEFAULT_TTL = 5;

    private final NodeId id;
    private final NodeType type;
    private final Transport transport;

    private final Set<Long> seenPackets = new HashSet<>();

    private Position position;

    public MeshNode(
            NodeId id,
            NodeType type,
            Transport transport
    ) {
        this.id = id;
        this.type = type;
        this.transport = transport;

        transport.onReceive(this::receive);
    }

    public NodeId id() {
        return id;
    }

    public NodeType type() {
        return type;
    }

    public Position position() {
        return position;
    }

    public void updatePosition(Position position) {
        this.position = position;
    }

    public void sendText(
            NodeId destination,
            String message
    ) {

        MeshPacket packet = new MeshPacket(
                id,
                destination,
                System.nanoTime(),
                PacketType.TEXT,
                message,
                null,
                DEFAULT_TTL
        );

        send(packet);
    }

    public void sendPosition(NodeId destination) {

        if (position == null) {
            throw new IllegalStateException(
                    "Node has no position"
            );
        }

        MeshPacket packet = new MeshPacket(
                id,
                destination,
                System.nanoTime(),
                PacketType.POSITION_UPDATE,
                null,
                position,
                DEFAULT_TTL
        );

        send(packet);
    }

    private void send(MeshPacket packet) {

        seenPackets.add(packet.id());

        System.out.println(
                "[" + id + "] Sending packet "
                        + packet.id()
                        + " → "
                        + packet.destination()
                        + " (TTL "
                        + packet.ttl()
                        + ")"
        );

        transport.send(packet);
    }

    private void receive(MeshPacket packet) {

        if (!seenPackets.add(packet.id())) {

            System.out.println(
                    "[" + id + "] Duplicate packet "
                            + packet.id()
                            + " ignored"
            );

            return;
        }

        System.out.println(
                "[" + id + "] Received packet "
                        + packet.id()
        );

        if (packet.destination().equals(id)) {

            handlePacket(packet);
            return;
        }

        if (packet.ttl() <= 0) {

            System.out.println(
                    "[" + id + "] Packet "
                            + packet.id()
                            + " expired"
            );

            return;
        }

        forward(packet);
    }

    private void forward(MeshPacket packet) {

        MeshPacket forwardedPacket =
                packet.withTtl(packet.ttl() - 1);

        System.out.println(
                "[" + id + "] Forwarding packet "
                        + packet.id()
                        + " → "
                        + packet.destination()
                        + " (TTL "
                        + forwardedPacket.ttl()
                        + ")"
        );

        transport.send(forwardedPacket);
    }

    private void handlePacket(MeshPacket packet) {

        switch (packet.type()) {

            case TEXT -> System.out.println(
                    "[" + id + "] Message from "
                            + packet.source()
                            + ": "
                            + packet.payload()
            );

            case POSITION_UPDATE -> System.out.println(
                    "[" + id + "] Position update from "
                            + packet.source()
                            + ": "
                            + packet.position().latitude()
                            + ", "
                            + packet.position().longitude()
            );
        }
    }
}