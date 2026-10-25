
public class BoardSetup {
    private final BoardGUI board;

    private int misses;
    private int strikes;
    private int totalMisses;
    private int totalHits;

    public BoardSetup() {
        board = new BoardGUI();
        misses = 0;
        strikes = 0;
        totalMisses = 0;
        totalHits = 0;
    }

    public ShotResult fire(int row, int col) {
        if (isWon() || isLost()) {
            return ShotResult.ALREADY_FIRED;
        }

        ShotResult result = board.fire(row, col);

        if (result == ShotResult.ALREADY_FIRED) {
            return result;
        }

        if (result == ShotResult.MISS) {
            misses++;
            totalMisses++;
            if (misses == 5) {
                strikes++;
                misses = 0;
            }
        } else {
            totalHits++;
            misses = 0;
        }
        return result;
    }

    public boolean isWon() {
        return totalHits == 17;
    }

    public boolean isLost() {
        return strikes >= 3;
    }

    public BoardGUI getBoard() {
        return board;
    }

    public int getMisses() {
        return misses;
    }

    public int getStrikes() {
        return strikes;
    }

    public int getTotalMisses() {
        return totalMisses;
    }

    public int getTotalHits() {
        return totalHits;
    }
}