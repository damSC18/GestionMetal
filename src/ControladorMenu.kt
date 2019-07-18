import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert

import javafx.stage.Stage


public class ControladorMenu {
    @FXML
    fun PantallaMateriasFabricadas(event: ActionEvent) {
        println("Materias Fabricadas" + event)

        try {

            val root = FXMLLoader.load<Parent>(javaClass.getResource("ProductosFabricados/ProductosFabricados.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage

            appStage.scene = scene
            appStage.toFront()
            appStage.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @FXML
    fun PantallaEscandallos(event: ActionEvent) {
        println("Materias Escandallos")

        try {

            val root = FXMLLoader.load<Parent>(javaClass.getResource("escandallos/Escandallos.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage

            appStage.scene = scene
            appStage.toFront()
            appStage.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @FXML
    fun PantallaMultimedia(event: ActionEvent) {


        try {

            val root = FXMLLoader.load<Parent>(javaClass.getResource("Multimedia/multimedia.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage

            appStage.scene = scene
            appStage.toFront()
            appStage.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @FXML
    fun DatosIdiomasArticulos(event: ActionEvent) {
        println("Idioma")
        try {
            val root = FXMLLoader.load<Parent>(javaClass.getResource("idiomas/idiomas.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage

            appStage.scene = scene
            appStage.toFront()
            appStage.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    @FXML
    fun PresupuestosPresentados(event: ActionEvent) {
        println("Idioma")
        try {
            val root = FXMLLoader.load<Parent>(javaClass.getResource("Presupuestos/presupuesto.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage

            appStage.scene = scene
            appStage.toFront()
            appStage.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @FXML
    fun tallerFabricacion(event: ActionEvent){
        try {
            val root = FXMLLoader.load<Parent>(javaClass.getResource("Taller/taller.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage

            appStage.scene = scene
            appStage.toFront()
            appStage.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @FXML
    fun PantallaMateriasPrimas(event: ActionEvent) {
        println("Materias MateriasPrimas" + event)

        try {

            val root = FXMLLoader.load<Parent>(javaClass.getResource("MateriasPrimas/MateriasPrimas.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage
            appStage.scene = scene
        ///    scene.stylesheets.add("estilos.css")
            appStage.width = 1280.0
            appStage.height = 800.0
            appStage.toFront()
            appStage.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @FXML
    fun GestionClientes(event: ActionEvent) {
        println("Materias MateriasPrimas" + event)

        try {

            val root = FXMLLoader.load<Parent>(javaClass.getResource("Clientes/clientes.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage
            appStage.scene = scene
            ///    scene.stylesheets.add("estilos.css")
            appStage.width = 1280.0
            appStage.height = 800.0
            appStage.toFront()
            appStage.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    @FXML
    fun FinalizarPrincipal() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Getión Metal"
        alert.headerText = "Menú Principal"
        alert.contentText = "Finalizar"

        alert.x = 300.0
        alert.y = 100.0


        var resultado = alert.showAndWait()
        val botonSel: String = resultado.get().text
        if (botonSel.equals("Aceptar")) {
            System.exit(-1)
        }


    }


}