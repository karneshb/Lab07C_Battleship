import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardGUI {
    public static final int SIZE = 10;
    private final Ships[][] ships;
    private final boolean[][] fired;
    private final List<Ships> fleet;
    private final Random rand;

    public BoardGUI() {
        ships = new Ships[SIZE][SIZE];
        fired = new boolean[SIZE][SIZE];
        fleet = new ArrayList<>();
        rand = new Random();
        placeShips();
    }

    private void placeShips() {
        int[] shipSizes = {5, 4, 3, 3, 2};
        for (int size : shipSizes) {
            Ships ship = new Ships(size);
            boolean placed = false;
            while (!placed) {
                boolean horizontal = rand.nextBoolean();
                int row = rand.nextInt(SIZE);
                int col = rand.nextInt(SIZE);

                if (canPlace(row, col, size, horizontal)) {
                    for (int i = 0; i < size; i++) {
                        int r = row + (horizontal ? 0 : i);
                        int c = col + (horizontal ? i : 0);
                        ships[r][c] = ship;
                    }
                    fleet.add(ship);
                    placed = true;
                }
            }
        }
    }

    private boolean canPlace(int row, int col, int size, boolean horizontal) {
        if (horizontal && col + size > SIZE) {
            return false;
        }

        if (!horizontal && row + size > SIZE) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            if (ships[r][c] != null) {
                return false;
            }
        }
        return true;
    }

    public ShotResult fire(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Shot is outside the board");
        }

        if (fired[row][col]) {
            return ShotResult.ALREADY_FIRED;
        }

        fired[row][col] = true;
        Ships ship = ships[row][col];

        if (ship == null) {
            return ShotResult.MISS;
        }

        ship.registerHit();

        if (ship.isSunk()) {
            return ShotResult.SUNK;
        }
        return ShotResult.HIT;
    }

    public boolean wasFiredAt(int row, int col) {
        return fired[row][col];
    }

    public boolean hasShip(int row, int col) {
        return ships[row][col] != null;
    }

    public int getShipCount() {
        return fleet.size();
    }
}
