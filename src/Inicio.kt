import javafx.application.Application
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.stage.StageStyle

public class Inicio: Application() {


    override fun start(stage: Stage) {

        val root : Parent = FXMLLoader.load(javaClass.getResource("Metal.fxml"))
        val scene: Scene =   Scene(root)

        stage.scene = scene
        stage.title="Gestión del Metal"

        val ico = Image("Imagenes/icono.jpg")
        stage.icons.add(ico)

        stage.show()


        // Desactiva cerrar ventana desde la X
        stage.setOnCloseRequest { e: Event ->
            e.consume()
        }
    }

    fun initialize() {
        println("Initialize")


    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Inicio::class.java)
        }
    }
}
