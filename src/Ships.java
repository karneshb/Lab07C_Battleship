
public class Ships {
    private final int size;
    private int hits;

    public Ships(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Invalid ship size");
        }
        this.size = size;
        this.hits = 0;
    }

    public void registerHit() {
        if (hits < size) {
            hits++;
        }
    }

    public boolean isSunk() {
        return hits == size;
    }

    public int getSize() {
        return size;
    }

    public int getHits() {
        return hits;
    }
}
