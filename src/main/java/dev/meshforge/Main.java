package dev.meshforge;

import dev.meshforge.node.MeshNode;
import dev.meshforge.node.NodeId;
import dev.meshforge.node.NodeType;
import dev.meshforge.packet.Position;
import dev.meshforge.simulation.SimulatedNetwork;

public class Main {

    public static void main(String[] args) {

        var network = new SimulatedNetwork();

        var soldierId = new NodeId(1);
        var vehicleId = new NodeId(2);
        var commandId = new NodeId(3);

        var soldier = new MeshNode(
                soldierId,
                NodeType.SOLDIER,
                network.connect(soldierId)
        );

        var vehicle = new MeshNode(
                vehicleId,
                NodeType.VEHICLE,
                network.connect(vehicleId)
        );

        var command = new MeshNode(
                commandId,
                NodeType.COMMAND,
                network.connect(commandId)
        );

        network.addLink(soldierId, vehicleId);
        network.addLink(vehicleId, commandId);

        soldier.updatePosition(
                new Position(
                        53.3498,
                        -6.2603,
                        42
                )
        );

        vehicle.updatePosition(
                new Position(
                        53.3505,
                        -6.2580,
                        38
                )
        );

        command.updatePosition(
                new Position(
                        53.3478,
                        -6.2650,
                        25
                )
        );

        System.out.println("=== MeshForge Network ===");

        soldier.sendPosition(commandId);

        soldier.sendText(
                commandId,
                "Situation report: moving north"
        );
    }
}