package io.github.OMAL_Maze.Map;

import java.util.Comparator;
import java.util.PriorityQueue;

public class AStar {

    static class Node {
        int x, y;
        float g, h;
        Node(int x, int y, float g, float h) {
            this.x = x; this.y = y; this.g = g; this.h = h;
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

        // If start or goal are walls, bail early (caller can fallback)
        if (walls[sy][sx] || walls[gy][gx]) return null;

        // fast-path: already at goal
        if (sx == gx && sy == gy) return new int[]{gx, gy};

        // gScore grid init
        float[][] gScore = new float[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                gScore[y][x] = Float.POSITIVE_INFINITY;

        // parent arrays to reconstruct path
        int[][] parentX = new int[height][width];
        int[][] parentY = new int[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) { parentX[y][x] = -1; parentY[y][x] = -1; }

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f()));
        gScore[sy][sx] = 0f;
        open.add(new Node(sx, sy, 0f, heuristic(sx, sy, gx, gy)));

        while (!open.isEmpty()) {
            Node current = open.poll();

            // Skip stale entry (we already have a better g)
            if (current.g > gScore[current.y][current.x]) continue;

            if (current.x == gx && current.y == gy) {
                // reconstruct next step: walk back until parent is the start
                int rx = gx, ry = gy;
                while (!(rx == sx && ry == sy)) {
                    int px = parentX[ry][rx];
                    int py = parentY[ry][rx];
                    if (px == -1 || py == -1) break;
                    if (px == sx && py == sy) break;
                    rx = px; ry = py;
                }
                return new int[]{rx, ry};
            }

            for (int i = 0; i < DIRS.length; i++) {
                int dx = DIRS[i][0];
                int dy = DIRS[i][1];
                int nx = current.x + dx;
                int ny = current.y + dy;

                if (nx < 0 || ny < 0 || nx >= width || ny >= height) continue;
                if (walls[ny][nx]) continue;

                // prevent diagonal corner cutting
                if (i >= 4) {
                    int b1x = current.x + dx;
                    int b1y = current.y;
                    int b2x = current.x;
                    int b2y = current.y + dy;
                    if (b1x < 0 || b1y < 0 || b1x >= width || b1y >= height) continue;
                    if (b2x < 0 || b2y < 0 || b2x >= width || b2y >= height) continue;
                    if (walls[b1y][b1x] || walls[b2y][b2x]) continue;
                }

                float tentativeG = current.g + DIR_COST[i];
                if (tentativeG < gScore[ny][nx]) {
                    gScore[ny][nx] = tentativeG;
                    parentX[ny][nx] = current.x;
                    parentY[ny][nx] = current.y;
                    open.add(new Node(nx, ny, tentativeG, heuristic(nx, ny, gx, gy)));
                }
            }
        }

        return null;
    }

    private static final int[][] DIRS = {
        { 1, 0}, {-1, 0}, { 0, 1}, { 0,-1},
        { 1, 1}, { 1,-1}, {-1, 1}, {-1,-1}
    };

    private static final float[] DIR_COST = {
        1f, 1f, 1f, 1f,
        1.4142f, 1.4142f, 1.4142f, 1.4142f
    };

    private static float heuristic(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        return (float)(Math.max(dx, dy) + (Math.sqrt(2) - 1) * Math.min(dx, dy));
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
