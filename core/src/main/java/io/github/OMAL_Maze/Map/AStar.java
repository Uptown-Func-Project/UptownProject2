package io.github.OMAL_Maze.Map;

import java.util.Comparator;
import java.util.PriorityQueue;

public class AStar {

    static class Node {
        int x, y;
        float g, h;
        Node parent;

        Node(int x, int y, float g, float h, Node parent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
            this.parent = parent;
        }

        float f() { return g + h; }
    }

    public static int[] getNextMove(boolean[][] walls, int[] start, int[] goal) {

        int height = walls.length;
        int width  = walls[0].length;

        int sx = clamp(start[0], 0, width - 1);
        int sy = clamp(start[1], 0, height - 1);
        int gx = clamp(goal[0], 0, width - 1);
        int gy = clamp(goal[1], 0, height - 1);

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(Node::f));
        boolean[][] closed = new boolean[height][width];

        Node startNode = new Node(sx, sy, 0, heuristic(sx, sy, gx, gy), null);
        open.add(startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.x == gx && current.y == gy) {
                return reconstructNextStep(current);
            }

            closed[current.y][current.x] = true;

            for (int i = 0; i < DIRS.length; i++) {
                int dx = DIRS[i][0];
                int dy = DIRS[i][1];

                int nx = current.x + dx;
                int ny = current.y + dy;

                if (nx < 0 || ny < 0 || nx >= width || ny >= height) continue;
                if (walls[ny][nx] == true) continue;
                if (closed[ny][nx]) continue;

                // Prevent diagonal corner clipping
                if (i >= 4) {  // diagonal directions are indices 4–7
                    int block1x = current.x + dx;
                    int block1y = current.y;
                    int block2x = current.x;
                    int block2y = current.y + dy;

                    if (walls[block1y][block1x] || walls[block2y][block2x])
                        continue;
                }

                float g = current.g + DIR_COST[i];
                float h = heuristic(nx, ny, gx, gy);

                open.add(new Node(nx, ny, g, h, current));
            }
        }

        return null;
    }

    // 8 directions: 4 cardinal + 4 diagonals
    private static final int[][] DIRS = {
        { 1, 0}, {-1, 0}, { 0, 1}, { 0,-1},
        { 1, 1}, { 1,-1}, {-1, 1}, {-1,-1}
    };

    // matching movement cost (cardinal = 1, diagonal = sqrt(2))
    private static final float[] DIR_COST = {
        1f, 1f, 1f, 1f,
        1.4142f, 1.4142f, 1.4142f, 1.4142f
    };

    private static float heuristic(int x1, int y1, int x2, int y2) {
        // euclid for diagonal
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        return (float)(Math.max(dx, dy) + (Math.sqrt(2) - 1) * Math.min(dx, dy));
    }

    private static int[] reconstructNextStep(Node goal) {
        Node curr = goal;

        while (curr.parent != null && curr.parent.parent != null) {
            curr = curr.parent;
        }

        return new int[]{curr.x, curr.y};
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
