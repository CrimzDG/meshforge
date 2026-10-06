package dev.meshforge;

import dev.meshforge.network.InMemoryTransport;
import dev.meshforge.node.MeshNode;
import dev.meshforge.node.NodeId;

public class Main {

    public static void main(String[] args) {

        var transport = new InMemoryTransport();

        var node = new MeshNode(
                new NodeId(1),
                transport
        );

        node.send(
                node.id(),
                "Hello, MeshForge!"
        );
    }
}