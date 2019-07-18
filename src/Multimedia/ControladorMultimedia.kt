package Multimedia


import javafx.beans.binding.Bindings
import javafx.beans.property.DoubleProperty
import javafx.collections.FXCollections

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene

import javafx.scene.control.cell.PropertyValueFactory

import javafx.stage.Stage

import javafx.event.Event
import javafx.geometry.Pos

import javafx.scene.SceneAntialiasing
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.shape.Rectangle
import javafx.stage.FileChooser
import javafx.util.Duration
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.sql.Connection

import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.filechooser.FileNameExtensionFilter


public class ControladorMultimedia {

    // Buscar en el cursos actula
    @FXML
    internal var listacamposBuscar: ComboBox<String> = ComboBox()

    @FXML
    internal var PalabraBuscador: TextField = TextField()
    private var lc: ResultSetMetaData? = null
    @FXML
    private var PanelBusqueda: Pane = Pane()

    var escenaActual: Scene? = null


    var nuevo: Boolean = true
    var posicion: Int = 0;

    var listaArticulos: MutableList<objetoMultimedia> = mutableListOf<objetoMultimedia>()

    // Clase en la  que se programan las operaciones contra la BD.

    var bd: MultimediaBd = MultimediaBd()
    //////////////////////////////////////////////////////////////////////////////
    var articulo: objetoMultimedia = objetoMultimedia(0, "", 0, "")
    //   var articulo: objetoArticuloFabricado = objetoArticuloFabricado(1, "", 0, 0f, 0f, 0f, 0f)


    @FXML
    var id_multimedia: TextField = TextField()
    @FXML
    var descripcion: TextField = TextField()
    @FXML
    var tipo: ComboBox<String> = ComboBox()
    @FXML
    var url: TextField = TextField()

    // Media View reproducir fichero Multimedia del registro
    @FXML
    var contenedorMultimedia: Pane = Pane()


    // Trabajo Escandaloos por articulo actual se visualiza en el ListView
    @FXML
    var ListaEscandallos: ListView<String> = ListView()

    fun initialize() {
        tipo.items.add("Imagen")
        tipo.items.add("Sonido")
        tipo.items.add("Video")
        bd.conexionBd()

        // Posicionamos el cursor en el primer registro
        articulo = bd.fleerPrimerRegistro()
        visualizaRegistro()
        rellenarListaCamposBuscar()
    }

    @FXML
    fun NuevoRegistro() {
        nuevo = true
        var posicion: Int = 0
        try {
            //    articulo = listaArticulos.last()
            //   posicion = articulo.id + 1


        } catch (ex: Exception) {
            posicion = 0
        }
        id_multimedia.text = posicion.toString()
        descripcion.text = ""
        tipo.value = "Selecciona Tipo"
        descripcion.requestFocus();
    }

    @FXML
    fun GrabaroRegistro() {
        try {

            val dato_id_multimedia = id_multimedia.text.toInt()
            val dato_descripcion = descripcion.text
            val dato_tipo = tipo.selectionModel.selectedIndex
            //      javax.swing.JOptionPane.showMessageDialog(null,dato_tipo)

            val dato_url = url.text

            articulo = objetoMultimedia(dato_id_multimedia, dato_descripcion, dato_tipo, dato_url)
            if (nuevo) {
                bd.ffgrabaRegistro(articulo)
            } else {
                bd.ffmodificaRegistro(articulo)
            }

            id_multimedia.text.toInt()
        } catch (ex: Exception) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Gestión Metal"
            alert.headerText = "Crud Materias Fabricadas"
            alert.contentText = "Error al grabar artículo"
            alert.showAndWait()
        }
    }


    @FXML
    fun BorraRegistro() {

        //  Borrar registro actul
        bd.ffborraRegistro(id_multimedia.text.toInt())
        PrimerRegistro()

    }


    @FXML
    fun anteriorRegistro() {


        articulo = bd.fleerAnteriorRegistro()

        visualizaRegistro()

    }

    @FXML
    fun SiguienteRegistro() {


        articulo = bd.fleerSiguienteRegistro()




        visualizaRegistro()


    }

    @FXML
    fun PrimerRegistro() {

        articulo = bd.fleerPrimerRegistro()




        visualizaRegistro()
    }

    @FXML
    fun UltimoRegistro() {


        articulo = bd.fleerUltimoRegistro()





        visualizaRegistro()
    }

    @FXML
    fun BuscarRegistro() {
        PanelBusqueda.setVisible(true)

    }

    @FXML
    fun SigBuscar() {
        val snc = listacamposBuscar.value.toString()
        articulo = bd.fleerSiguienteBuuscar(PalabraBuscador.text, snc)

        visualizaRegistro()
    }

    @FXML
    fun AntBuscar() {
        val snc = listacamposBuscar.value.toString()
        articulo = bd.fleerAnteriorBuuscar(PalabraBuscador.text, snc)

        visualizaRegistro()
    }

    @FXML
    fun FinalizarBuscar() {
        PanelBusqueda.setVisible(false)
    }

    @FXML
    fun Finalizar() {

    }

    fun visualizaRegistro() {

        nuevo = false
        id_multimedia.text = articulo.id.toString()
        descripcion.text = articulo.descripcion
        tipo.value = tipo.items.get(articulo.tipo)

        url.text = articulo.url
        println(url.text)
        var urlFichero: String = "file:///" + url.text.replace("\\", "//")


        try {

            // Tipo Video o Sonido
            if (tipo.value == "Video" || tipo.value == "Sonido"  ) {

                var media: Media = Media(urlFichero);


                var mediaPlayer: MediaPlayer = MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);

                var objetoMultimedia: MediaView = MediaView(mediaPlayer);
                objetoMultimedia.fitHeight = 300.0
                objetoMultimedia.fitWidth = 430.0

                objetoMultimedia.viewportProperty()
                mediaPlayer.play()
                var r: Rectangle = Rectangle()
               r.setX(20.1)
                r.setY(30.1)
                r.setWidth(500.1)
                r.setHeight(150.0)
                objetoMultimedia.setClip(r)


                val playButton = Button(">")
                playButton.setOnAction { e ->
                    if (playButton.text == ">") {
                        mediaPlayer.play()
                        playButton.text = "||"
                    } else {
                        mediaPlayer.pause()
                        playButton.text = ">"
                    }
                }
                val rewindButton = Button("<<")
                rewindButton.setOnAction { e -> mediaPlayer.seek(Duration.ZERO) }
                val slVolume = Slider()
                slVolume.prefWidth = 150.0
                slVolume.maxWidth = Region.USE_PREF_SIZE
                slVolume.minWidth = 30.0
                slVolume.value = 50.0
                mediaPlayer.volumeProperty().bind(slVolume.valueProperty().divide(100))


                val hBox = HBox(10.0)
                hBox.alignment = Pos.CENTER
                hBox.children.addAll(Label("  "), playButton, rewindButton,
                        Label("Volume"), slVolume)
                contenedorMultimedia.children.clear()
                contenedorMultimedia.children.addAll(hBox, objetoMultimedia)

            }
            // Tipo imagen
            else
            {
                contenedorMultimedia.children.clear()
                var imagenMultimedia:ImageView=    ImageView(urlFichero)

                imagenMultimedia.setFitWidth(contenedorMultimedia.getWidth())
                imagenMultimedia.setFitHeight(contenedorMultimedia.getHeight())
               // imagenMultimedia.setScaleX(0.9)
                //imagenMultimedia.setScaleY(0.9)
                contenedorMultimedia.children.add(  imagenMultimedia)

            }
        } catch (e: Exception) {
            println("Error....:" + e.message + "\n")
            e.printStackTrace()
        }

    }


    // Listado articulos con JasperReport
    @FXML
    fun ImprimeArticulosFabricados() {

        var listadoJr: reportes.listadoArticulosFabricados = reportes.listadoArticulosFabricados()

        listadoJr.imprimefichero()
    }


    @FXML
    fun Finalizar(event: ActionEvent) {

        try {


            val root = FXMLLoader.load<Parent>(javaClass.getResource("Metal.fxml"))

            // val root = FXMLLoader.load<Parent>(javaClass.getResource("..//Metal.fxml"))
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
    fun SeleccionarImagen() {
        val fileChooser: FileChooser = FileChooser()
        fileChooser.getExtensionFilters().addAll(
                FileChooser.ExtensionFilter("All Files", "*.*"),
                FileChooser.ExtensionFilter("Text Files", "*.txt"),
                FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                FileChooser.ExtensionFilter("Video Files", "*.avi", "*.mp3", "*.mp4"));

        val selectedFile = fileChooser.showOpenDialog(null)
        if (selectedFile != null) {
            try {

                url.text = selectedFile.absolutePath

            } catch (ex: FileNotFoundException) {
                println(ex.message)
            }
        }
    }

    // Combo Box con los campos del resultSet Actual
    private fun rellenarListaCamposBuscar() {


        lc = bd.leerCamposTabla()
        try {
            //   listacamposBuscar.items.clear()
            //  listacamposBuscar.removeAllItems();

            for (i in 1..lc!!.getColumnCount()) {
                //     println("TIpo de campo en la table Producto " + lc!!.getColumnType(i) + lc!!.getColumnTypeName(i))
                if (lc!!.getColumnType(i) == 12 || lc!!.getColumnType(i) == 4) {

                    listacamposBuscar.items.add(lc!!.getColumnName(i).toString())

                }
            }
        } catch (ex: SQLException) {
            Logger.getLogger(ControladorMultimedia::class.java!!.getName()).log(Level.SEVERE, null, ex)
        }


    }
}

