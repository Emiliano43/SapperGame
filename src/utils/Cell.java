package utils;

import java.awt.*;

public class Cell {
    private int countMinesNear;
    private boolean isOpen;
    private boolean isMine;
    private boolean isFlag;
    private boolean bangMine, youWon;
    private int[] colour_of_numbers = {0x0000FF, 0x008000, 0xFF0000, 0x800000, 0x0};

    public boolean isMine() {
        return isMine;
    }

    public int getCountMine() { return countMinesNear; }

    public boolean isNotOpen() { return !isOpen; }

    public void open() {
        isOpen = true;
    }

    public void mine() { isMine = true; }

    public void setCountMine(int count) { countMinesNear = count; }

    public void inverseFlag() { isFlag = !isFlag; }

    public void paintMine(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x*30 + 7, y*30 + 10, 18, 10);
        g.fillRect(x*30 + 11, y*30 + 6, 10, 18);
        g.fillRect(x*30 + 9, y*30 + 8, 14, 14);
        g.setColor(Color.white);
        g.fillRect(x*30 + 11, y*30 + 10, 4, 4);
    }

    public void paintString(Graphics g, String str, int x, int y, Color color) {
        g.setColor(color);
        g.setFont(new Font("", Font.BOLD, 30));
        g.drawString(str, x*30 + 8, y*30 + 26);
    }

    public void paint(Graphics g, int x, int y) {
        g.setColor(Color.lightGray);
        g.drawRect(x*30, y*30, 30, 30);
        if (!isOpen) {
            if ((bangMine || youWon) && isMine) paintMine(g, x, y, Color.black);
            else {
                g.setColor(Color.lightGray);
                g.fill3DRect(x*30, y*30, 30, 30, true);
                if (isFlag) paintString(g, "F", x, y, Color.red);
            }
        } else
        if (isMine) paintMine(g, x, y, bangMine? Color.red : Color.black);
        else
        if (countMinesNear > 0)
            paintString(g, Integer.toString(countMinesNear), x, y, new Color(colour_of_numbers[countMinesNear - 1]));
    }
}