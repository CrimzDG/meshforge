package dev.meshforge.packet;

import dev.meshforge.node.NodeId;

public record MeshPacket(
        NodeId source,
        NodeId destination,
        long id,
        String payload
) {
}