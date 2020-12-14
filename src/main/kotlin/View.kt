import SplayTree.SplayNode
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.lang.Integer.max
import javax.swing.JPanel
import kotlin.math.pow

internal class View(width: Int) : JPanel() {
    private val tree: SplayTree<Int> = SplayTree()
    private var h = heightTree(tree.root)
    private val tableWidth = width
    private val diameter = 3
    private val fontNumber = Font("Time New Roman", Font.BOLD, 20)
    private val color = Color(254, 127, 203)

    fun resetTree() {
        tree.clear()
        this.repaint()
    }

    fun insertNode(value: Int) {
        tree.add(value)
        h = heightTree(tree.root)
        this.repaint()
    }

    fun removeNode(value: Int) {
        tree.remove(value)
        h = heightTree(tree.root)
        this.repaint()
    }

    private fun heightTree(root: SplayNode<Int>?): Int {
        return if (root == null) 0 else max(heightTree(root.left), heightTree(root.right)) + 1
    }

    private fun dualNode(a: Int, b: Int, x1: Int, y1: Int, x2: Int, y2: Int, g: Graphics) {
        g.color = Color.black
        g.drawLine(x1, y1, x2, y2)
        g.fillOval(x1, y1, diameter, diameter)
        g.fillOval(x2, y2, diameter, diameter)
        g.drawOval(x1, y1, diameter, diameter)
        g.drawOval(x2, y2, diameter, diameter)
        g.color = color
        g.font = fontNumber
        g.drawString(a.toString(), x1 + 5, y1 + 10)
        g.drawString(b.toString(), x2 + 5, y2 + 10)
    }

    private fun drawTree(root: SplayNode<Int>?, depth: Int, x: Int, y: Int, g: Graphics) {
        val d = 20 * 2.0.pow(depth.toDouble()).toInt()
        if (root != null) {
            if (root.right != null) {
                dualNode(
                    root.value, root.right!!.value, x, y,
                    x + d, y + 40, g
                )
                drawTree(root.right, depth - 1, x + d, y + 40, g)
            }
            if (root.left != null) {
                dualNode(
                    root.value, root.left!!.value, x, y,
                    x - d, y + 40, g
                )
                drawTree(root.left, depth - 1, x - d, y + 40, g)
            }
        }
    }

    public override fun paintComponent(g: Graphics) {
        if (tree.root != null) {
            g.color = Color.black
            g.fillOval(tableWidth / 2, 10, diameter, diameter)
            g.drawOval(tableWidth / 2, 10, diameter, diameter)
            g.color = color
            g.font = fontNumber
            g.drawString(tree.root!!.value.toString(), tableWidth / 2 + 5, 10 + 10)
            if (tree.size > 1) drawTree(tree.root, h - 2, tableWidth / 2, 10, g)
        }
    }

}