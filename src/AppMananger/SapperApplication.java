package AppMananger;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import utils.Cell;
import utils.TimerLabel;

public class SapperApplication extends JFrame {

    private final int FIELD_SIZE = 9;
    private final int START_LOCATION = 200;
    private final int MOUSE_BUTTON_LEFT = 1;
    private final int MOUSE_BUTTON_RIGHT = 3;
    private final int NUMBER_OF_MINES = 10;

    private Cell[][] field = new Cell[FIELD_SIZE][FIELD_SIZE];
    private Random random = new Random();
    private int countOpenedCells;
    private static boolean youWon, bangMine;

    public SapperApplication() {
        setTitle("Игра \"Сапёр\"");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, 330, 330);
        setResizable(false);
        final TimerLabel timeLabel = new TimerLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        final Panel panel = new Panel();
        panel.setBackground(Color.white);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int x = e.getX() / 30;
                int y = e.getY() / 30;
                if (!bangMine && !youWon) {
                    if (e.getButton() == MOUSE_BUTTON_LEFT)
                        if (field[y][x].isNotOpen()) {
                            openCells(x, y);
                            youWon = countOpenedCells == FIELD_SIZE * FIELD_SIZE - NUMBER_OF_MINES;
                        }
                    if (e.getButton() == MOUSE_BUTTON_RIGHT) field[y][x].inverseFlag();
                    if (bangMine || youWon) timeLabel.stopTimer();
                    panel.repaint();
                }
            }
        });
        add(BorderLayout.CENTER, panel);
        add(BorderLayout.SOUTH, timeLabel);
        setVisible(true);
        initField();
    }

    void openCells(int x, int y) {
        if (x < 0 || x > FIELD_SIZE - 1 || y < 0 || y > FIELD_SIZE - 1) return;
        if (!field[y][x].isNotOpen()) return;
        field[y][x].open();
        bangMine = field[y][x].isMine();
        if (!field[y][x].isMine()) countOpenedCells++;
        if (field[y][x].getCountMine() > 0 || bangMine) return;
        for (int dx = -1; dx < 2; dx++)
            for (int dy = -1; dy < 2; dy++) openCells(x + dx, y + dy);
    }

    void initField() {
        int x, y, countMines = 0;

        for (x = 0; x < FIELD_SIZE; x++)
            for (y = 0; y < FIELD_SIZE; y++)
                field[y][x] = new Cell();

        while (countMines < NUMBER_OF_MINES) {
            do {
                x = random.nextInt(FIELD_SIZE);
                y = random.nextInt(FIELD_SIZE);
            } while (field[y][x].isMine());
            field[y][x].mine();
            countMines++;
        }

        for (x = 0; x < FIELD_SIZE; x++) {
            for (y = 0; y < FIELD_SIZE; y++) {
                if (!field[y][x].isMine()) {
                    int count = 0;
                    for (int dx = -1; dx < 2; dx++)
                        for (int dy = -1; dy < 2; dy++) {
                            int nX = x + dx;
                            int nY = y + dy;
                            if (nX < 0 || nY < 0 || nX > FIELD_SIZE - 1 || nY > FIELD_SIZE - 1) {
                                nX = x;
                                nY = y;
                            }
                            count += (field[nY][nX].isMine()) ? 1 : 0;
                        }
                    field[y][x].setCountMine(count);
                }
            }
        }
    }

    class Panel extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int x = 0; x < 9; x++)
                for (int y = 0; y < 9; y++) field[y][x].paint(g, x, y);
        }
    }
}