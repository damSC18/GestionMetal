package escandallos
import javafx.application.Application;
import javafx.event.Event
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image
import javafx.stage.Screen
import javafx.stage.Stage;

public class Metal: Application() {


    override fun start(stage: Stage) {

    /*   val primaryScreenBounds = Screen.getPrimary().visualBounds

        //set Stage boundaries to visible bounds of the main screen
        stage.setX(primaryScreenBounds.minX)
        stage.setY(primaryScreenBounds.minY)
        stage.setWidth(primaryScreenBounds.width)
        stage.setHeight(primaryScreenBounds.height)*/



        val root: Parent = FXMLLoader.load(javaClass.getResource("Escandallos.fxml"))


        val scene: Scene = Scene(root)

        stage.scene = scene
        stage.title = "Gestión del Metal"

        val ico = Image("imagenes/icono.jpg")
        stage.icons.add(ico)

        // Desactiva cerrar ventana desde la X
        stage.setOnCloseRequest { e: Event ->
           e.consume()
        }

        stage.show()

    }
    fun initialize() {
        println("Initialize")


    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Metal::class.java)
        }

    }
}