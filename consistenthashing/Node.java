package consistenthashing;

import java.util.Objects;

public class Node {
    private final String id;
    private final int weight;
    private final String ipAddress;

    public Node(String id, String ipAddress) {
        this(id, ipAddress, 1);
    }

    public Node(String id, String ipAddress, int weight) {
        this.id = id;
        this.weight = weight;
        this.ipAddress = ipAddress;
    }

    public String getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
