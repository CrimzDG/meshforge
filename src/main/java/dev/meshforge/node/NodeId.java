package dev.meshforge.node;

public record NodeId(long value) {

    public NodeId {
        if (value < 0) {
            throw new IllegalArgumentException("Node ID cannot be negative");
        }
    }

    @Override
    public String toString() {
        return Long.toUnsignedString(value);
    }
}