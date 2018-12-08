package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day8 {
    private static final Logger LOGGER = Logger.getLogger(Day8.class.getName());
    private static final Queue<Integer> DATA = new ArrayDeque<>();

    private static class Node {
        private final List<Integer> _metaData;
        private final List<Node> _children;

        public Node(List<Integer> metaData, List<Node> childNodes) {
            _metaData = metaData;
            _children = childNodes;
        }

        public Integer getMetadata() {
            return _metaData.stream().mapToInt(Integer::intValue).sum()
                    + _children.stream().map(n -> n.getMetadata()).mapToInt(Integer::intValue).sum();
        }

        public Integer getValue() {
            if (_children.isEmpty()) {
                return _metaData.stream().mapToInt(Integer::intValue).sum();
            }

            return _metaData.stream() //
                    .filter(d -> d > 0) //
                    .filter(d -> d <= _children.size()) //
                    .map(d -> _children.get(d - 1).getValue()) //
                    .mapToInt(Integer::intValue).sum();
        }
    }

    public static void run() throws IOException {
        String rawData = new String(Files.readAllBytes(Paths.get("inputs/input8.txt"))).trim();
        DATA.addAll(Arrays.stream(rawData.split(" +")) //
                .map(Integer::valueOf) //
                .collect(Collectors.toList()));

        Node topNode = getNode();

        // Results
        LOGGER.info("Part 1 : " + topNode.getMetadata());
        LOGGER.info("Part 2 : " + topNode.getValue());
    }

    private static Node getNode() {
        Integer nofChildren = DATA.poll();
        Integer nofMetadataEntries = DATA.poll();

        List<Node> children = new ArrayList<>();
        for (int i = 0; i < nofChildren; i++) {
            children.add(getNode());
        }

        List<Integer> metaData = new ArrayList<>();
        for (int i = 0; i < nofMetadataEntries; i++) {
            metaData.add(DATA.poll());
        }

        return new Node(metaData, children);
    }

}
