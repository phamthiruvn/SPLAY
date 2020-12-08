import tornadofx.isInt
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane.showMessageDialog
import javax.swing.JPanel
import javax.swing.JTextField

fun main() {
    Main()
}

class Main : JFrame() {
    private val viewer = View(800)

    private fun addGraphic() {
        this.add(viewer)
    }

    private fun addMenu() {
        val panel = JPanel()
        panel.preferredSize = Dimension(250, 35)
        panel.background = Color(253, 215, 228)
        panel.layout = FlowLayout(FlowLayout.CENTER)
        val textField = JTextField(20)
        val insertButton = JButton("Insert")
        insertButton.addActionListener {
            if (!textField.text.isInt()) showMessageDialog(null, "Write a number")
            viewer.insertNode(textField.text.toInt())
            textField.text = ""
            repaint()
        }
        val deleteButton = JButton("Delete")
        deleteButton.addActionListener {
            if (!textField.text.isInt()) showMessageDialog(null, "Write a number")
            viewer.removeNode(textField.text.toInt())
            textField.text = ""
            repaint()
        }

        val resetButton = JButton("Reset")
        resetButton.addActionListener {
            viewer.resetTree()
            textField.text = ""
            repaint()
        }
        panel.add(resetButton)
        panel.add(textField)
        panel.add(insertButton)
        panel.add(deleteButton)
        this.add(panel, BorderLayout.NORTH)
    }

    init {
        addGraphic()
        addMenu()
        setSize(800, 450)
        isResizable = false
        isVisible = true
    }
}