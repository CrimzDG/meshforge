package dev.meshforge.simulation;

import dev.meshforge.network.InMemoryTransport;
import dev.meshforge.node.NodeId;
import dev.meshforge.packet.MeshPacket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SimulatedNetwork {

    private final Map<NodeId, InMemoryTransport> transports =
            new HashMap<>();

    private final Map<NodeId, Set<NodeId>> neighbours =
            new HashMap<>();

    public InMemoryTransport connect(NodeId nodeId) {

        var transport = new InMemoryTransport(
                nodeId,
                this::transmit
        );

        transports.put(nodeId, transport);
        neighbours.put(nodeId, new HashSet<>());

        return transport;
    }

    public void addLink(NodeId a, NodeId b) {

        requireNode(a);
        requireNode(b);

        neighbours.get(a).add(b);
        neighbours.get(b).add(a);
    }

    public void removeLink(NodeId a, NodeId b) {

        neighbours.getOrDefault(a, Set.of()).remove(b);
        neighbours.getOrDefault(b, Set.of()).remove(a);
    }

    private void transmit(
            NodeId sender,
            MeshPacket packet
    ) {

        var connectedNodes = neighbours.get(sender);

        if (connectedNodes == null) {
            return;
        }

        for (NodeId neighbour : connectedNodes) {

            var transport = transports.get(neighbour);

            if (transport != null) {
                transport.deliver(packet);
            }
        }
    }

    private void requireNode(NodeId nodeId) {

        if (!transports.containsKey(nodeId)) {
            throw new IllegalArgumentException(
                    "Node is not connected to network: " + nodeId
            );
        }
    }
}