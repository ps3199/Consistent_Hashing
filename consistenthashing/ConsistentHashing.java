package consistenthashing;

import consistenthashing.Node;
import consistenthashing.Request;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class ConsistentHashing {
    private Map<Node, List<Long>> nodeToVirtualNodesMappings;
    private ConcurrentSkipListMap<Long, Node> requestToNodeMappings;
    private Function<String, Long> hashFunction;
    private int noOfReplicas;


    public ConsistentHashing(final Function<String, Long> hashFunction,
                             final int noOfReplicas) {
        if (noOfReplicas == 0) {
            throw new IllegalArgumentException();
        }
        this.noOfReplicas = noOfReplicas;
        this.hashFunction = hashFunction;
        this.nodeToVirtualNodesMappings = new ConcurrentHashMap<>();
        this.requestToNodeMappings = new ConcurrentSkipListMap<>();
    }

    public void addNode(Node node) {
        nodeToVirtualNodesMappings.put(node, new CopyOnWriteArrayList<>());
        for (int i = 0; i < noOfReplicas * node.getWeight() ; i++) {
            final var point = hashFunction.apply(node.getId().toString() +"-" + String.valueOf(i));
            nodeToVirtualNodesMappings.get(node).add(point);
            requestToNodeMappings.put(point, node);
        }
    }

    public void removeNode(Node node) {
        for (final Long point : nodeToVirtualNodesMappings.remove(node)) {
            requestToNodeMappings.remove(point);
        }
    }

    public Node getAssignedNode(Request request) {
        final var key = hashFunction.apply(request.getId());
        final var entry = requestToNodeMappings.higherEntry(key);
        if (entry == null) {
            return requestToNodeMappings.firstEntry().getValue();
        } else {
            return entry.getValue();
        }
    }
}
