package code.codewars.alpinist;

import java.util.*;

public record Alpinist(int width, int... map) {

    public Alpinist(String map) {
        this(width(map), intArray(map));
    }

    public Alpinist {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive");
        }
        if (map == null || map.length == 0) {
            throw new IllegalArgumentException("Heightmap cannot be null or empty");
        }
        if (map.length % width != 0) {
            throw new IllegalArgumentException("Heightmap length must be a multiple of width");
        }
    }

    public List<Point> path() {
        return new Pathfinder().path(this);
    }

    private boolean isDestination(Point point) {
        return point.x() == width - 1 && point.y() == height() - 1;
    }

    private boolean isValid(Point point) {
        return point.x() >= 0 && point.x() < width() &&
                point.y() >= 0 && point.y() < height();
    }

    public int height() {
        return map.length / width;
    }

    public int get(Point p) {
        return get(p.x(), p.y());
    }

    public int get(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException("Coordinates out of bounds");
        }
        return map[y * width + x];
    }

    private static int width(String map) {
        int i = map.indexOf('\n');
        if (i == 0) i = map.indexOf('\n', 1);
        return i;
    }

    static int[] intArray(String map) {
        map = map.replaceAll("[^0-9]", "");
        var result = new int[map.length()];
        for (int i = 0; i < map.length(); i++) {
            result[i] = Character.getNumericValue(map.charAt(i));
        }
        return result;
    }

    private static class Pathfinder {

        private final Map<Point, Waypoint> best = new HashMap<>();
        private final Queue<Waypoint> queue = new PriorityQueue<>(Comparator.comparingInt(Waypoint::cost));

        public Pathfinder() {
            enqueue(new Waypoint());
        }

        public List<Point> path(Alpinist map) {
            while (!queue.isEmpty()) {
                var current = queue.poll();
                if (map.isDestination(current.point)) {
                    return current.getPath();
                }
                for (var direction : Direction.values()) {
                    expand(map, current, direction);
                }
            }
            throw new IllegalStateException("No path found");
        }

        private void expand(Alpinist map, Waypoint current, Direction direction) {
            var nextPoint = current.point.move(direction);
            if (map.isValid(nextPoint)) {
                var next = new Waypoint(nextPoint, current, map);
                enqueue(next);
            }
        }

        private void enqueue(Waypoint nextWaypoint) {
            var current = best.get(nextWaypoint.point);
            if (current != null && nextWaypoint.cost >= current.cost) return;
            best.put(nextWaypoint.point, nextWaypoint);
            queue.add(nextWaypoint);
        }
    }

    private static class Waypoint {

        private final Waypoint previous;
        private final Point point;
        private final int cost;

        public Waypoint() {
            this.previous = null;
            this.point = new Point(0, 0);
            this.cost = 0;
        }

        public Waypoint(Point point, Waypoint previous, Alpinist map) {
            this.point = point;
            this.previous = previous;
            var h1 = map.get(previous.point);
            var h2 = map.get(point);
            this.cost = Math.abs(h1 - h2) + previous.cost;
        }

        public int cost() {
            return cost;
        }

        public List<Point> getPath() {
            var path = new ArrayList<Point>();
            var current = this;
            while (current != null) {
                path.add(current.point);
                current = current.previous;
            }
            return path.reversed();
        }
    }
}
