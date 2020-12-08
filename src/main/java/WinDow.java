import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class WinDow extends JFrame {
    private SplayTreeViewer viewer = new SplayTreeViewer(1000, 1000);
    private JPanel optionPanel;

    private void setMainPanel() {
        viewer.setBackground(new Color(0xE0E2D1));
        viewer.setBorder(new LineBorder(new Color(0x1F312E), 2));
        this.add(viewer, BorderLayout.CENTER);
    }

    private void label() {
        JPanel headPanel = new JPanel();
        headPanel.setPreferredSize(new Dimension(200, 30));
        headPanel.setBorder(new LineBorder(new Color(0x1F312E)));
        headPanel.setBackground(new Color(0x5FCDE2));
        JLabel headLabel = new JLabel("SPLAY TREE");
        headLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headPanel.add(headLabel);
        this.add(headPanel, BorderLayout.NORTH);
    }

    private void setDialogs() {
        JPanel insertPanel = new JPanel();
        insertPanel.setBorder(new LineBorder(new Color(0x1F312E)));
        insertPanel.setPreferredSize(new Dimension(120, 90));
        insertPanel.setBackground(new Color(0x43E2D9));
        insertPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("INSERT");
        JTextField insertField = new JTextField(9);
        JButton insertButton = new JButton("confirm");
        insertButton.addActionListener(e -> {

            viewer.insert(Integer.parseInt(insertField.getText()));
            repaint();
        });
        insertPanel.add(label);
        insertPanel.add(insertField);
        insertPanel.add(insertButton);
        optionPanel.add(insertPanel);

        JPanel removePanel = new JPanel();
        removePanel.setBorder(new LineBorder(new Color(0x1F312E)));
        removePanel.setPreferredSize(new Dimension(120, 90));
        removePanel.setBackground(new Color(0x43E2D9));
        removePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label1 = new JLabel("REMOVE");
        JTextField removeField = new JTextField(9);
        JButton removeButton = new JButton("confirm");
        removeButton.addActionListener(e -> {
            viewer.removeNode(Integer.parseInt(removeField.getText()));
            repaint();
        });
        removePanel.add(label1);
        removePanel.add(removeField);
        removePanel.add(removeButton);
        optionPanel.add(removePanel);
    }

    private void setOptionPanel() {
        optionPanel = new JPanel();
        optionPanel.setPreferredSize(new Dimension(150, 100));
        optionPanel.setBorder(new LineBorder(new Color(0x1F312E), 2));
        optionPanel.setBackground(new Color(0xA754E2));
        setDialogs();
        JButton resetButton = new JButton("reset");
        resetButton.addActionListener(e -> {
            viewer.clearTree();
            repaint();
        });
        optionPanel.add(resetButton);
        this.add(optionPanel, BorderLayout.NORTH);
    }

    private WinDow() {
        setMainPanel();
        setSize(1200, 1000);
        label();
        setOptionPanel();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        WinDow w = new WinDow();
    }
}
