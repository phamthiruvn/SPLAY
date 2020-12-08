import javax.swing.*;
import java.awt.*;

class xx extends JPanel {
    private SplayTree<Integer> tree;
    private int h;
    private int CANVAS_WIDTH;
    static private int diameter = 40;

    xx(int width, int height) {
        new JPanel(null) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(5000, 5000);
            }
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        tree = new SplayTree<>();
        setLayout(null);
        h = heightTree(tree.getRoot());
        this.CANVAS_WIDTH = width;
    }

    void insert(int value) {
        tree.add(value);
        h = heightTree(tree.getRoot());
        this.repaint();
    }

    void clearTree() {
        tree.clear();
        this.repaint();
    }

    void removeNode(int value) {
        tree.remove(value);
        h = heightTree(tree.getRoot());
        this.repaint();
    }

    private int heightTree(SplayNode root) {
        if (root == null)
            return 0;
        return Math.max(heightTree(root.getLeft()), heightTree(root.getRight())) + 1;
    }

    private void dualNode(int a, int b, int x1, int y1, int x2, int y2, Graphics g) {
        g.drawLine(x1 + diameter / 2, y1 + diameter / 2, x2 + diameter / 2, y2 + diameter / 2);
        g.setColor(new Color(0x4995BB));
        g.fillOval(x1, y1, diameter, diameter);
        g.fillOval(x2, y2, diameter, diameter);
        g.setColor(Color.black);
        g.drawOval(x1, y1, diameter, diameter);
        g.drawOval(x2, y2, diameter, diameter);
        g.setFont(new Font("Courier New", Font.ITALIC, 20));
        g.drawString(String.valueOf(a), x1 + 10, y1 + 30);
        g.drawString(String.valueOf(b), x2 + 10, y2 + 30);
    }

    private void drawTree(SplayNode root, int depth, int x, int y, Graphics g) {
        if (root == null) {
            return;
        }
        if (root.getRight() != null) {
            dualNode((int) root.getValue(), (int) root.getRight().getValue(), x, y, (int) (x + 20 * Math.pow(2, depth)), y + 60, g);
            drawTree(root.getRight(), depth - 1, (int) (x + 20 * Math.pow(2, depth)), y + 60, g);
        }
        if (root.getLeft() != null) {
            dualNode((int) root.getValue(), (int) root.getLeft().getValue(), x, y, (int) (x - 20 * Math.pow(2, depth)), y + 60, g);
            drawTree(root.getLeft(), depth - 1, (int) (x - 20 * Math.pow(2, depth)), y + 60, g);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (tree.getRoot() != null) {
            g.setColor(new Color(0x4995BB));
            g.fillOval(CANVAS_WIDTH / 2, 10, diameter, diameter);
            g.setColor(Color.black);
            g.drawOval(CANVAS_WIDTH / 2, 10, diameter, diameter);
            g.setFont(new Font("Courier New", Font.ITALIC, 20));
            g.drawString(String.valueOf(tree.getRoot().getValue()), CANVAS_WIDTH / 2 + 10, 10 + 30);
            if (tree.size() > 1)
                drawTree(tree.getRoot(), h - 2, CANVAS_WIDTH / 2, 10, g);
        }
    }
}
