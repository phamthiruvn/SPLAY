import javafx.geometry.Pos
import javafx.scene.layout.*
import tornadofx.*


class Table : View() {
    private val viewer = SplayTreeViewer(1600)
    override val root = borderpane {
        top = hbox {
            alignment = Pos.TOP_CENTER
            spacing = 5.0
            button("Reset") {
                setPrefSize(50.0, 20.0)
                action { viewer.resetTree() }
            }
            val text = textfield {
                border = Border(BorderStroke(null, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))
                setPrefSize(75.0, 20.0)

            }
            button("Insert") {
                setPrefSize(50.0, 20.0)
                action { viewer.insertNode(text.text.toInt()) }

            }
            button("Delete") {
                setPrefSize(60.0, 20.0)
                action { viewer.removeNode(text.text.toInt()) }
            }
            button("Find") {
                setPrefSize(40.0, 20.0)
                action { viewer.findNode(text.text.toInt()) }
            }
        }
        center = borderpane {
            fitToParentSize()
            this@borderpane.add(viewer)
        }
    }

    init {
        title = "Splay Tree"
        primaryStage.width = 1600.0
        primaryStage.height = 900.0
        primaryStage.isResizable = false
    }
}