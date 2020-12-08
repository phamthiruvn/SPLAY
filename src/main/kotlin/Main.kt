import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.App


class Main : App(Table::class) {
    override fun start(stage: Stage) {
        stage.icons += Image("file:src/main/resources/Icon.png")
        super.start(stage)
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)}