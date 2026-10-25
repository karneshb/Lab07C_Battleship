import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {

    private BoardSetup game;
    private final JButton[][] buttons;

    private final JLabel missLabel;
    private final JLabel strikeLabel;
    private final JLabel totalMissLabel;
    private final JLabel totalHitLabel;

    private static final Color WATER = new Color(43, 155, 251);

    public GameFrame() {

        setTitle("Battleship");
        setSize(750, 800);
        setMinimumSize(new Dimension(600, 650));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        game = new BoardSetup();
        buttons = new JButton[10][10];

        JLabel titleLabel = new JLabel("BATTLESHIP", SwingConstants.CENTER);

        titleLabel.setFont(new Font("Cosmic Sans", Font.BOLD, 28));
        titleLabel.setForeground(Color.DARK_GRAY);
        add(titleLabel, BorderLayout.NORTH);
        JPanel boardPanel = new JPanel(new GridLayout(10, 10, 3, 3));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton button = new JButton(" ");
                button.setFont(new Font("Cosmic Sans", Font.BOLD, 20));

                button.setBackground(WATER);
                button.setForeground(Color.BLUE);
                button.setOpaque(true);
                button.setFocusPainted(false);

                final int r = row;
                final int c = col;

                button.addActionListener(e -> fireAt(r, c));
                buttons[row][col] = button;
                boardPanel.add(button);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        JPanel statusPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        missLabel = new JLabel("MISS: 0 / 5");
        strikeLabel = new JLabel("STRIKES: 0 / 3");
        totalMissLabel = new JLabel("TOTAL MISS: 0");
        totalHitLabel = new JLabel("TOTAL HIT: 0 / 17");

        JLabel[] labels = {
                missLabel,
                strikeLabel,
                totalMissLabel,
                totalHitLabel
        };

        for (JLabel label : labels) {
            label.setFont(new Font("Cosmic Sans", Font.BOLD, 16));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            statusPanel.add(label);
        }

        bottomPanel.add(statusPanel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton playAgainButton = new JButton("Play Again");
        JButton quitButton = new JButton("Quit");
        playAgainButton.setFont(new Font("Cosmic Sans", Font.BOLD, 16));
        quitButton.setFont(new Font("Cosmic Sans", Font.BOLD, 16));

        playAgainButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to start a new game?", "Play Again", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                startNewGame();
            }});

        quitButton.addActionListener(e -> confirmQuit());

        controlPanel.add(playAgainButton);
        controlPanel.add(quitButton);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {confirmQuit();}});

        refreshCounters();
    }

    private void fireAt(int row, int col) {
        if (game.isWon() || game.isLost()) {
            return;
        }

        ShotResult result = game.fire(row, col);
        if (result == ShotResult.ALREADY_FIRED) {
            return;
        }

        JButton button = buttons[row][col];

        if (result == ShotResult.MISS) {
            showShotResult(button, "M", Color.YELLOW, Color.BLACK);
        } else if (result == ShotResult.HIT || result == ShotResult.SUNK) {
            showShotResult(button, "X", Color.RED, Color.WHITE);
        }
        refreshCounters();

        if (result == ShotResult.SUNK) {
            JOptionPane.showMessageDialog(this, "You sunk a ship!", "Ship Sunk", JOptionPane.INFORMATION_MESSAGE);
        }
        if (game.isWon()) {
            finishGame(true);
        } else if (game.isLost()) {
            finishGame(false);
        }
    }

    private void showShotResult(JButton button, String symbol, Color background, Color foreground) {
        button.setText(symbol);
        button.setBackground(background);
        button.setForeground(foreground);

        for (java.awt.event.ActionListener listener : button.getActionListeners()) {
            button.removeActionListener(listener);
        }
    }

    private void refreshCounters() {
        missLabel.setText("MISS: " + game.getMisses() + " / 5");
        strikeLabel.setText("STRIKES: " + game.getStrikes() + " / 3");
        totalMissLabel.setText("TOTAL MISS: " + game.getTotalMisses());
        totalHitLabel.setText(
                "TOTAL HIT: " + game.getTotalHits() + " / 17");
    }

    private void startNewGame() {
        game = new BoardSetup();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton button = buttons[row][col];
                button.setText(" ");
                button.setEnabled(true);
                button.setBackground(WATER);
                button.setForeground(Color.BLUE);

                for (java.awt.event.ActionListener listener : button.getActionListeners()) {
                    button.removeActionListener(listener);
                }
                final int r = row;
                final int c = col;
                button.addActionListener(e -> fireAt(r, c)
                );
            }
        }
        refreshCounters();
    }

    private void finishGame(boolean won) {
        String message;
        if (won) {
            message = "Congratulations! You won!\n" + "You hit all 17 ship cells.\n" + "Would you like to play again?";
        } else {
            message = "Game Over! You lost!\n" + "You reached 3 strikes.\n" + "Would you like to play again?";
        }

        int choice = JOptionPane.showConfirmDialog(this, message, won ? "You Won!" : "You Lost!", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        } else {
            dispose();
        }
    }

    private void confirmQuit() {
        int choice = JOptionPane.showConfirmDialog(this, "Would you like to play again?\n" + "Choose No to quit.", "Quit Game", JOptionPane.YES_NO_CANCEL_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        } else if (choice == JOptionPane.NO_OPTION) {
            dispose();
        }
    }
}