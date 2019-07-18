package idiomas



import com.itextpdf.text.pdf.fonts.otf.Language


import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene

import javafx.stage.Stage


import javafx.scene.control.*

import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.util.logging.Level
import java.util.logging.Logger
import javafx.scene.layout.Pane
import javafx.scene.control.ComboBox


public class ControladorIdiomas {

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

    var listaArticulos: MutableList<objetoIdiomas> = mutableListOf<objetoIdiomas>()

    // Clase en la  que se programan las operaciones contra la BD.

    var bd: IdiomasBd = IdiomasBd()
    //////////////////////////////////////////////////////////////////////////////
    var articulo: objetoIdiomas = objetoIdiomas(0, 0, "", 0, "", 0, "", 0)
    //   var articulo: objetoArticuloFabricado = objetoArticuloFabricado(1, "", 0, 0f, 0f, 0f, 0f)

// Controles  Interfaz


    @FXML
    private var id_multimediaDMontaje: TextField = TextField()


    @FXML
    private var Texto_DMantenimiento: TextArea = TextArea()


    @FXML
    private var id_articuloFabricado: TextField = TextField()

    @FXML
    private var Texto_DTecnicos: TextArea = TextArea()


    @FXML
    private var SelecionIdiomas: ComboBox<String> = ComboBox()

    @FXML
    private var id_multimediaDMantenimiento: TextField = TextField()

    @FXML
    private var id_multimediaDTecnicos: TextField = TextField()

    @FXML
    private var etiquetaProductoFabricado: Label = Label()

    @FXML
    private var Texto_DMontaje: TextArea = TextArea()
    // Media View reproducir fichero Multimedia del registro
    @FXML
    var contenedorMultimedia: Pane = Pane()


    // Trabajo Escandaloos por articulo actual se visualiza en el ListView
    @FXML
    var ListaEscandallos: ListView<String> = ListView()

    fun initialize() {

        SelecionIdiomas.items.add("Español")
        SelecionIdiomas.items.add("Francés")
        SelecionIdiomas.items.add("Alemán")
        SelecionIdiomas.items.add("Portugues")
        bd.conexionBd()

        // Posicionamos el cursor en el primer registro
        articulo = bd.fleerPrimerRegistro()
        visualizaRegistro()
        rellenarListaCamposBuscar()


    }

    @FXML
    fun NuevoRegistro() {


        PanelBusqueda.setVisible(true)
        SelecionIdiomas.setEditable(true)

        nuevo = true

        SelecionIdiomas.value = "Español"
        id_articuloFabricado.text = ""
        etiquetaProductoFabricado.text = "Producto Fabricado"
        id_multimediaDTecnicos.text = ""
        Texto_DTecnicos.text = ""
        id_multimediaDMantenimiento.text = ""
        Texto_DMantenimiento.text = ""
        id_multimediaDMontaje.text = ""
        Texto_DMontaje.text = ""
        SelecionIdiomas.requestFocus();

    }

    @FXML
    fun GrabaroRegistro() {
        try {

            val dato_SelecionIdiomas: Int = SelecionIdiomas.selectionModel.selectedIndex
            val dato_id_articuloFabricado: Int = id_articuloFabricado.text.toInt()

            val dato_id_multimediaDTecnicos: Int = id_multimediaDTecnicos.text.toInt()
            val dato_Texto_DTecnicos = Texto_DTecnicos.text

            val dato_id_multimediaDMantenimiento: Int = id_multimediaDMantenimiento.text.toInt()
            val dato_Texto_DMantenimiento = Texto_DMantenimiento.text

            val dato_id_multimediaDMontaje: Int = id_multimediaDMontaje.text.toInt()
            val dato_Texto_DMontaje = Texto_DMontaje.text

            articulo = objetoIdiomas(dato_SelecionIdiomas, dato_id_articuloFabricado, dato_Texto_DTecnicos, dato_id_multimediaDTecnicos, dato_Texto_DMantenimiento, dato_id_multimediaDMantenimiento, dato_Texto_DMontaje, dato_id_multimediaDMontaje)
            if (nuevo) {
                bd.ffgrabaRegistro(articulo)
            } else {
                bd.ffmodificaRegistro(articulo)
            }


        } catch (ex: Exception) {
            ex.printStackTrace()
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Gestión Metal"
            alert.headerText = "Crud Información Idiomas Productos Fabricado"
            alert.contentText = "Error al Grabar Registro"
            alert.showAndWait()
        }
    }


    @FXML
    fun BorraRegistro() {

        //  Borrar registro actul
        bd.ffborraRegistro(SelecionIdiomas.selectionModel.selectedIndex, id_articuloFabricado.text.toInt())
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
        try {
            val snc = listacamposBuscar.value.toString()
            val datosArticuloRecibido: String = bd.fleerSiguienteBuuscar(PalabraBuscador.text, snc)
            var codigoArticulo: List<String> = datosArticuloRecibido.split("-")
            id_articuloFabricado.text = codigoArticulo[0]
            etiquetaProductoFabricado.text = codigoArticulo[1]
        } catch (ex: Exception) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Gestión Metal"
            alert.headerText = "Crud Idiomas Fabricadas"
            alert.contentText = "Error .. :Elija el Campo de Búsqueda"
            alert.showAndWait()
        }


    }

    @FXML
    fun AntBuscar() {
        val snc = listacamposBuscar.value.toString()
        val datosArticuloRecibido: String = bd.fleerAnteriorBuuscar(PalabraBuscador.text, snc)
        var codigoArticulo: List<String> = datosArticuloRecibido.split("-")
        id_articuloFabricado.text = codigoArticulo[0]
        etiquetaProductoFabricado.text = codigoArticulo[1]

        // visualizaRegistro()
    }

    @FXML
    fun FinalizarBuscar() {
        PanelBusqueda.setVisible(false)
    }

    @FXML
    fun Finalizar() {

    }

    fun visualizaRegistro() {


        PanelBusqueda.setVisible(false)
        SelecionIdiomas.setEditable(false)


        nuevo = false


        // SelecionIdiomas.selectionModel.setSelectedItem(articulo.id_idioma.toString())
        SelecionIdiomas.value = SelecionIdiomas.items.get(articulo.id_idioma)
        id_articuloFabricado.text = articulo.id_articuloFabricado.toString()
        etiquetaProductoFabricado.text = ""

        id_multimediaDTecnicos.text = articulo.MDtecnicos.toString()
        Texto_DTecnicos.text = articulo.DatosTecnicos

        id_multimediaDMantenimiento.text = articulo.MDMantenimiento.toString()
        Texto_DMantenimiento.text = articulo.DatosMantenimiento

        id_multimediaDMontaje.text = articulo.MDmontaje.toString()
        Texto_DMontaje.text = articulo.DatosMontaje
        SelecionIdiomas.requestFocus()


        // val datosArticuloRecibido:String = bd.fleerSiguienteBuuscar(articulo.id_articuloFabricado.toString(), "id_ArticuloFabricado")
        val datosArticuloRecibido: String = bd.leerproductoFabricado(articulo.id_articuloFabricado)


        id_articuloFabricado.text = articulo.id_articuloFabricado.toString()
        etiquetaProductoFabricado.text = datosArticuloRecibido


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
            Logger.getLogger(ControladorIdiomas::class.java!!.getName()).log(Level.SEVERE, null, ex)
        }


    }


}

