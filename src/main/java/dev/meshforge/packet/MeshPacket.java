package dev.meshforge.packet;

import dev.meshforge.node.NodeId;

public record MeshPacket(
        NodeId source,
        NodeId destination,
        long id,
        PacketType type,
        String payload,
        Position position,
        int ttl
) {

    public MeshPacket {
        if (ttl < 0) {
            throw new IllegalArgumentException(
                    "TTL cannot be negative"
            );
        }
    }

    public MeshPacket withTtl(int newTtl) {
        return new MeshPacket(
                source,
                destination,
                id,
                type,
                payload,
                position,
                newTtl
        );
    }
}