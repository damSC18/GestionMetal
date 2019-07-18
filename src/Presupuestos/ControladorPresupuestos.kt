package Presupuestos


import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene

import javafx.stage.Stage


import javafx.scene.control.*

import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.util.logging.Level
import java.util.logging.Logger
import javafx.scene.control.ComboBox
import javafx.scene.control.TitledPane
import javafx.scene.input.KeyEvent
import javafx.geometry.Insets
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import java.sql.ResultSet
import java.time.LocalDate
import java.util.HashMap


public class ControladorPresupuestos {

    var LineasPresupuesto = HashMap<String, objetosLP>()

    // BuscarCliente
    @FXML
    var idClinete: TextField = TextField()
    @FXML
    var nombreClinete: TextField = TextField()
    @FXML
    var clienteEncontrado: Label = Label()

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

    var bd: PresupuestosBd = PresupuestosBd()
    //////////////////////////////////////////////////////////////////////////////
    var articulo: objetoIdiomas = objetoIdiomas(0, 0, "", 0, "", 0, "", 0)
    val fechaSql = java.sql.Date(0)

    // Campos FXML para crear presupuesto
    var articuloPresupuesto: objetoPresupuesto = objetoPresupuesto(0, 0, fechaSql, "", "")

    @FXML
    var idCliente: TextField = TextField()
    @FXML
    var nombreCliente: TextField = TextField()
    @FXML
    var idPresupuesto: Label = Label()
    @FXML
    var fechaPresupuesto: DatePicker = DatePicker()
    @FXML
    var descripcionPresupuesto: TextArea = TextArea()
    @FXML
    var estadoPresupuesto: ComboBox<String> = ComboBox()



    // Contenedor lineas Presupuesto creados en Ejecucion


    @FXML
    var ContenedorLineasPresupuesto: Accordion = Accordion()
    @FXML
    var descripcionLinea: TextField = TextField()

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

    // Label visualiza articulo Fabricación buscado
    @FXML
    var ArticuloEncontradoParaAñadir: Label = Label()
    @FXML
    var CodigoArticuloEncontradoParaAñadir: Label = Label()

    // Contenedor de Lineas de Fabricacion creadas
    internal var contenedores = HashMap<String, VBox>()

    fun initialize() {
        fechaPresupuesto=DatePicker(LocalDate.now());

        SelecionIdiomas.items.add("Español")
        SelecionIdiomas.items.add("Francés")
        SelecionIdiomas.items.add("Alemán")
        SelecionIdiomas.items.add("Portugues")
        SelecionIdiomas.value="Español"

        estadoPresupuesto.items.add("Diseño")
        estadoPresupuesto.items.add("Presentado")
        estadoPresupuesto.items.add("Admitido")
        estadoPresupuesto.items.add("Taller")
        estadoPresupuesto.items.add("Entregado")
        estadoPresupuesto.value="Diseño"

        bd.conexionBd()

        // Posicionamos el cursor en el primer registro
        articuloPresupuesto = bd.fleerPrimerRegistro()
        visualizaRegistro()
        rellenarListaCamposBuscar()


    }

    @FXML
    fun NuevoRegistro() {


        PanelBusqueda.setVisible(true)
        SelecionIdiomas.setEditable(true)

        nuevo = true

        SelecionIdiomas.value = "Español"

        idPresupuesto.text = "0"
        idCliente.text = ""
        clienteEncontrado.text="Cliente"

        descripcionPresupuesto.text = ""

        idCliente.requestFocus();

        ContenedorLineasPresupuesto.getPanes().clear()

        //Borra toda la informqación que contengaa el HasMap del presupuesto actual para carga los datos leidos de la base dd datos
        LineasPresupuesto.clear()

    }

    @FXML
    fun GrabaroRegistro() {
        try {
            val dato_idPresupuesto: Int = idPresupuesto.text.toInt()
            val dato_idCliente: Int = idCliente.text.toInt()


            val dato_fechaPresupuesto: java.sql.Date = java.sql.Date.valueOf(fechaPresupuesto.getValue())

            val dato_descripcionPresupuesto: String = descripcionPresupuesto.text

            var ePresupuesto:String =estadoPresupuesto.value

            articuloPresupuesto = objetoPresupuesto(dato_idPresupuesto, dato_idCliente, dato_fechaPresupuesto, dato_descripcionPresupuesto, ePresupuesto)




            if (nuevo) {
                bd.ffgrabaRegistro(articuloPresupuesto)
            } else {
                bd.ffmodificaRegistro(articuloPresupuesto)
                bd.borrarLineasPresupuesto(articuloPresupuesto.id_Presupuesto)
            }

            bd.grabarLineasPresupuesto(LineasPresupuesto,articuloPresupuesto.id_Presupuesto,nuevo)
            // Despues de grabar Presupuesto y líneas Presupuesto se cierra el cursor (ResultSet) de Presupuesto.
            // El método actualizaCursor lo vuelve a abrir y se situal en el primero
            bd.actualizaCursor()

        } catch (ex: Exception) {
            ex.printStackTrace()
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Gestión Metal"
            alert.headerText = "Presupuestos"
            alert.contentText = "Error al Grabar Registro"
            alert.showAndWait()
        }
    }


    @FXML
    fun BorraRegistro() {

        //Borra toda la informqación que contengaa el HasMap del presupuesto actual para carga los datos leidos de la base dd datos
        ContenedorLineasPresupuesto.getPanes().clear()


        LineasPresupuesto.clear()
        //  Borrar presupuesto  actual
        bd.ffborraRegistro(idPresupuesto.text.toInt())
        //  Borrar LineasPresupuesto presupuesto  actual
        bd.borrarLineasPresupuesto(articuloPresupuesto.id_Presupuesto)
        bd.actualizaCursor()
        PrimerRegistro()

    }


    @FXML
    fun anteriorRegistro() {


        articuloPresupuesto = bd.fleerAnteriorRegistro()

        visualizaRegistro()

    }

    @FXML
    fun SiguienteRegistro() {


        articuloPresupuesto = bd.fleerSiguienteRegistro()




        visualizaRegistro()


    }

    @FXML
    fun PrimerRegistro() {

        articuloPresupuesto = bd.fleerPrimerRegistro()


        visualizaRegistro()
    }

    @FXML
    fun UltimoRegistro() {


        articuloPresupuesto = bd.fleerUltimoRegistro()


        visualizaRegistro()
    }

    @FXML
    fun BuscarRegistro() {
        PanelBusqueda.setVisible(true)

    }

    var datosArticuloRecibido: ResultSet? = null
    @FXML
    fun SigBuscar() {
        try {
            val snc = listacamposBuscar.value.toString()
            datosArticuloRecibido = bd.fleerSiguienteBuuscar(PalabraBuscador.text, snc)
            //  var codigoArticulo: List<String> = datosArticuloRecibido.split("-")
            CodigoArticuloEncontradoParaAñadir.text = datosArticuloRecibido!!.getString(1)
            ArticuloEncontradoParaAñadir.text = datosArticuloRecibido!!.getString(2)

        } catch (ex: Exception) {
            ex.printStackTrace()
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
        CodigoArticuloEncontradoParaAñadir.text = codigoArticulo[0]
        ArticuloEncontradoParaAñadir.text = codigoArticulo[1]

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


        // PanelBusqueda.setVisible(false)
        // SelecionIdiomas.setEditable(false)




        nuevo = false



        idPresupuesto.text = articuloPresupuesto.id_Presupuesto.toString()
        idCliente.text = articuloPresupuesto.id_Cliente.toString()
        fechaPresupuesto.value = articuloPresupuesto.Fecha.toLocalDate()
        estadoPresupuesto.value=articuloPresupuesto.Estado
        descripcionPresupuesto.text = articuloPresupuesto.Descripcion.toString()

        buscaClientes2( idCliente.text)

        //Visualizar lineasPresupuesto del Presupuesto activo
   var cursorlineasPresupuesto:ResultSet=bd.leerLineasPresupuesto(idPresupuesto.text)






        ContenedorLineasPresupuesto.getPanes().clear()
        //Borra toda la informqación que contengaa el HasMap del presupuesto actual para carga los datos leidos de la base dd datos
        LineasPresupuesto.clear()
        var claveLinea:String=""
          while (cursorlineasPresupuesto.next()) {
              println(cursorlineasPresupuesto.getString("DescripcionLinea"))

              var datosLineas=objetosLP(cursorlineasPresupuesto.getString("DescripcionLinea") ,Button(cursorlineasPresupuesto.getInt("id_Articulo").toString()),TextField(cursorlineasPresupuesto.getFloat("Precio").toString()),TextField(cursorlineasPresupuesto.getInt("Cantidad").toString()),Button(cursorlineasPresupuesto.getString("Estado") ),TextArea(cursorlineasPresupuesto.getString("DatosTecnicos") ),TextArea(cursorlineasPresupuesto.getString("DatosMantenimiento") ),TextArea(cursorlineasPresupuesto.getString("DatosMontaje") ))


              LineasPresupuesto.put(cursorlineasPresupuesto.getString("DescripcionLinea")+"@"+cursorlineasPresupuesto.getInt("id_Articulo").toString(),datosLineas)
              println(LineasPresupuesto.get(cursorlineasPresupuesto.getString("DescripcionLinea")+"@"+cursorlineasPresupuesto.getInt("id_Articulo")))

            if(cursorlineasPresupuesto.getString("DescripcionLinea")!=claveLinea){
                descripcionLinea.text=cursorlineasPresupuesto.getString("DescripcionLinea")
                AñadirLineaPresupuesto()
                claveLinea=cursorlineasPresupuesto.getString("DescripcionLinea")
              }
           AñadirArticuloAlaLineaPresupuestoActualDesdeBaseDeDatos(datosLineas)
          }

    }

    fun AñadirArticuloAlaLineaPresupuestoActualDesdeBaseDeDatos(datosLineas:objetosLP) {

        datosArticuloRecibido = bd.leerArticuloFabricado(datosLineas.codigoArticuloCreado.text)

        var  existe:Boolean
        existe=datosArticuloRecibido!!.first()
        println("Existe"+existe)
        var sc: ScrollPane = ScrollPane()
        sc.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS)
        sc.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS)

        val codigoArticuloCreado: Button = Button(datosLineas.codigoArticuloCreado.text)
        // Borrar Articulo
        codigoArticuloCreado.setOnMouseClicked { event ->

            LineasPresupuesto.remove(descripcionLinea.text+"@"+codigoArticuloCreado.text)
            codigoArticuloCreado.text="Borrado"
        }

        val descripcionArticuloSeleccionado: Label = Label(datosArticuloRecibido!!.getString("Descripcion"))

        var urlFichero: String = "file:///" + datosArticuloRecibido!!.getString(8).replace("\\", "//")

        var imagenLineaPresupuesto: ImageView = ImageView(urlFichero)
        imagenLineaPresupuesto.fitHeight = 100.0
        imagenLineaPresupuesto.fitWidth = 100.0

        var cantidadArticulos: TextField = datosLineas.cantidadArticulos

        cantidadArticulos.maxWidth = 110.0

        var precioArticulos: TextField =datosLineas.precioArticulos
        precioArticulos.maxWidth = 50.0

        val estadoLinea: Button =Button(datosLineas.estadoLinea.text)

        estadoLinea.setOnMouseClicked { event ->

            if(estadoLinea.text.equals("Diseño")){estadoLinea.text="Taller"}
            else if(estadoLinea.text.equals("Taller")){estadoLinea.text="Fabricado"}
            else if(estadoLinea.text.equals("Fabricado")){estadoLinea.text="Diseño"}
            else{
                estadoLinea.text=""
            }

            // Borro del HasMap la línea modificada
            LineasPresupuesto.remove(descripcionLinea.text+"@"+codigoArticuloCreado.text)
            // Cambio el estado del estado linea en el objetoLP datosLineas
            datosLineas.estadoLinea=estadoLinea
            // Grabo de nuevo en el HasMap la linea con la modificación del estadoLinea
            LineasPresupuesto.put(descripcionLinea.text+"@"+codigoArticuloCreado.text,datosLineas)
        }



        estadoLinea.maxWidth = 110.0

        contenedorLineaPresupuesto = contenedores.get(datosLineas.descripcionLineaPresupuesto) as VBox

        contenedorLineaPresupuesto.padding = Insets(20.0, 5.0, 20.0, 5.0)
        // contenedorLineaPresupuesto.hgap = 5.0
        ///  contenedorLineaPresupuesto.vgap = 5.0

        var hh: HBox = HBox()
        hh.spacing = 20.0
        hh.setAlignment(Pos.BOTTOM_LEFT)
        hh.padding = Insets(20.0, 5.0, 20.0, 5.0)
        var etiquetaID: Label = Label("ID :")
        etiquetaID.setStyle("-fx-text-fill: #9e2780;-fx-font: normal bold 16px 'serif' ")
        var etiquetaArticulo: Label = Label("Articulo:")
        etiquetaArticulo.setStyle("-fx-text-fill: #9e2780;-fx-font: normal bold 16px 'serif' ")
        var etiquetaPrecio: Label = Label("Precio:")
        etiquetaPrecio.setStyle("-fx-text-fill: #9e2780;-fx-font: normal bold 16px 'serif' ")
        var etiquetaCantidad: Label = Label("Cantidad :")
        etiquetaCantidad.setStyle("-fx-text-fill: #9e2780;-fx-font: normal bold 16px 'serif' ")

        var etiquetaEstado: Label = Label("Estado Linea :")
        etiquetaEstado.setStyle("-fx-text-fill: #9e2780;-fx-font: normal bold 16px 'serif' ")


        hh.children.addAll(etiquetaID, codigoArticuloCreado, etiquetaArticulo, descripcionArticuloSeleccionado, etiquetaPrecio, precioArticulos, etiquetaCantidad, cantidadArticulos,etiquetaEstado,estadoLinea)

        contenedorLineaPresupuesto.children.add(hh)


        //   contenedorLineaPresupuesto.children.add(codigDescripcionCreada)
        // contenedorLineaPresupuesto.children.add(precioArticulos)
        //   contenedorLineaPresupuesto.children.add(cantidadArticulos)
        //   contenedorLineaPresupuesto.children.add(imagenLineaPresupuesto)


        // Datos para el idioma
        var codigoIdioma: Int = SelecionIdiomas.selectionModel.selectedIndex

        var codigoArticulo: Int = codigoArticuloCreado.text.toInt()

       // var ObjetoDatosIdioma: objetoIdiomas = bd.leerRegistroDatosIdiomaParaPresupuesto(codigoIdioma, codigoArticulo)

        var TAMontaje: TextArea = datosLineas.TAMontaje
        var TAMantenimiento: TextArea = datosLineas.TAMantenimiento
        var TATecnicos: TextArea = datosLineas.TATecnicos
        //   var TA: TextArea = TextArea(ObjetoDatosIdioma.DatosMontaje)

        var hhh: HBox = HBox()
        hhh.spacing = 10.0
        hhh.setAlignment(Pos.BOTTOM_LEFT)
        hhh.children.addAll(imagenLineaPresupuesto, TAMontaje, TAMantenimiento, TATecnicos)
        hhh.setStyle("-fx-margin:100")
        contenedorLineaPresupuesto.children.add(hhh)
        //  contenedorLineaPresupuesto.children.addAll(imagenLineaPresupuesto,TADM)

        val lineaSeparacin: Separator = Separator()
        contenedorLineaPresupuesto.children.add(lineaSeparacin)
        sc.setContent(contenedorLineaPresupuesto)


       // javax.swing.JOptionPane.showMessageDialog(null,lineaPresupuesto)
        lineaPresupuesto.setContent(sc)



      //  var datosLineas=objetosLP(descripcionLinea.text,codigoArticuloCreado.text,precioArticulos,cantidadArticulos,estadoLinea,TAMontaje,TAMantenimiento,TATecnicos)

       // println(datosLineas)
        LineasPresupuesto.put(descripcionLinea.text+"@"+codigoArticuloCreado.text,datosLineas)
        println(LineasPresupuesto.get(descripcionLinea.text+"@"+1))

    }




    // Listado articulos con JasperReport
    @FXML
    fun ImprimeArticulosFabricados() {

        var listadoJr: Presupuestos.informesPresupuestos.informePresupuesto = Presupuestos.informesPresupuestos.informePresupuesto()

        listadoJr.imprimefichero(articuloPresupuesto.id_Presupuesto.toString())
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
            Logger.getLogger(ControladorPresupuestos::class.java!!.getName()).log(Level.SEVERE, null, ex)
        }


    }

    // Variable utilizada para guardar el TiledPane, lineadepresupuesto seleccionad y luego poder añadir mas cosas o borrarla.
    var lineaPresupuesto: javafx.scene.control.TitledPane = javafx.scene.control.TitledPane()
    var claveLinea: String = ""
    var contenedorLineaPresupuesto: VBox = VBox()
    @FXML
    fun AñadirLineaPresupuesto() {

        lineaPresupuesto = javafx.scene.control.TitledPane()

        // Evento que selecciona la LineaPresupuesto y la hace activa
        lineaPresupuesto.setOnMouseClicked { event ->
            // Convierte el evento event por medio de la propiedad source en u TitledPane
             lineaPresupuesto  = event.source as TitledPane
            claveLinea = lineaPresupuesto.text
            descripcionLinea.text = lineaPresupuesto.text

        };

        // Añadimos objetos a la linea de presupuesto

        lineaPresupuesto.text = descripcionLinea.text
        claveLinea = lineaPresupuesto.text



        ContenedorLineasPresupuesto.getPanes().add(lineaPresupuesto)
        var contenedorLineaArticulo: VBox = VBox()
        contenedores.put(descripcionLinea.text, contenedorLineaArticulo)

    }

    @FXML
    fun BorrarLineaPresupuesto() {


        ContenedorLineasPresupuesto.panes.removeAll(lineaPresupuesto)


    }

    @FXML
    fun AñadirArticuloAlaLineaPresupuestoActual() {

        var sc: ScrollPane = ScrollPane()
        sc.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS)
        sc.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS)

        val codigoArticuloCreado: Button = Button(CodigoArticuloEncontradoParaAñadir.text)

        // Borrar Articulo
        codigoArticuloCreado.setOnMouseClicked { event ->

            LineasPresupuesto.remove(descripcionLinea.text+"@"+codigoArticuloCreado.text)
            codigoArticuloCreado.text="Borrado"

        }
        val descripcionArticuloSeleccionado: Label = Label(ArticuloEncontradoParaAñadir.text)

        var urlFichero: String = "file:///" + datosArticuloRecibido!!.getString(8).replace("\\", "//")

        var imagenLineaPresupuesto: ImageView = ImageView(urlFichero)
        imagenLineaPresupuesto.fitHeight = 100.0
        imagenLineaPresupuesto.fitWidth = 100.0

        var cantidadArticulos: TextField = TextField("1")

        cantidadArticulos.maxWidth = 110.0

        var precioArticulos: TextField = TextField(datosArticuloRecibido!!.getString(5))
        precioArticulos.maxWidth = 50.0

        var estadoLinea: Button = Button("Diseño")


        estadoLinea.setOnMouseClicked { event ->

            if(estadoLinea.text.equals("Diseño")){estadoLinea.text="Taller"}
            else if(estadoLinea.text.equals("Taller")){estadoLinea.text="Fabricado"}
            else if(estadoLinea.text.equals("Fabricado")){estadoLinea.text="Diseño"}
            else{
                estadoLinea.text=""
            }


        }


        estadoLinea.maxWidth = 110.0

//javax.swing.JOptionPane.showMessageDialog(null,claveLinea)
        contenedorLineaPresupuesto = contenedores.get(claveLinea) as VBox

        contenedorLineaPresupuesto.padding = Insets(20.0, 5.0, 20.0, 5.0)
        // contenedorLineaPresupuesto.hgap = 5.0
        ///  contenedorLineaPresupuesto.vgap = 5.0

        var hh: HBox = HBox()
        hh.spacing = 20.0
        hh.setAlignment(Pos.BOTTOM_LEFT)
        hh.padding = Insets(20.0, 5.0, 20.0, 5.0)
        var etiquetaID: Label = Label("ID :")
        etiquetaID.setStyle("-fx-text-fill: #9e2780;-fx-font: normal bold 16px 'serif' ")
        var etiquetaArticulo: Label = Label("Articulo:")
        etiquetaArticulo.setStyle("-fx-text-fill: #9e2780;-fx-font: normal bold 16px 'serif' ")
        var etiquetaPrecio: Label = Label("Precio:")
        etiquetaPrecio.setStyle("-fx-text-fill: #9e2780;-fx-font: normal bold 16px 'serif' ")
        var etiquetaCantidad: Label = Label("Cantidad :")
        etiquetaCantidad.setStyle("-fx-text-fill: #9e2780;-fx-font: normal bold 16px 'serif' ")

        var etiquetaEstado: Label = Label("Estado Linea :")
        etiquetaEstado.setStyle("-fx-text-fill: #9e2780;-fx-font: normal bold 16px 'serif' ")


        hh.children.addAll(etiquetaID, codigoArticuloCreado, etiquetaArticulo, descripcionArticuloSeleccionado, etiquetaPrecio, precioArticulos, etiquetaCantidad, cantidadArticulos,etiquetaEstado,estadoLinea)

        contenedorLineaPresupuesto.children.add(hh)


        //   contenedorLineaPresupuesto.children.add(codigDescripcionCreada)
        // contenedorLineaPresupuesto.children.add(precioArticulos)
        //   contenedorLineaPresupuesto.children.add(cantidadArticulos)
        //   contenedorLineaPresupuesto.children.add(imagenLineaPresupuesto)


        // Datos para el idioma
        var codigoIdioma: Int = SelecionIdiomas.selectionModel.selectedIndex

        var codigoArticulo: Int = codigoArticuloCreado.text.toInt()
        var ObjetoDatosIdioma: objetoIdiomas = bd.leerRegistroDatosIdiomaParaPresupuesto(codigoIdioma, codigoArticulo)
        var TAMontaje: TextArea = TextArea(ObjetoDatosIdioma.DatosMontaje)
        var TAMantenimiento: TextArea = TextArea(ObjetoDatosIdioma.DatosMantenimiento)
        var TATecnicos: TextArea = TextArea(ObjetoDatosIdioma.DatosTecnicos)
     //   var TA: TextArea = TextArea(ObjetoDatosIdioma.DatosMontaje)

        var hhh: HBox = HBox()
        hhh.spacing = 10.0
        hhh.setAlignment(Pos.BOTTOM_LEFT)
        hhh.children.addAll(imagenLineaPresupuesto, TAMontaje, TAMantenimiento, TATecnicos)
        hhh.setStyle("-fx-margin:100")
        contenedorLineaPresupuesto.children.add(hhh)
        //  contenedorLineaPresupuesto.children.addAll(imagenLineaPresupuesto,TADM)

        val lineaSeparacin: Separator = Separator()
        contenedorLineaPresupuesto.children.add(lineaSeparacin)
        sc.setContent(contenedorLineaPresupuesto)


        lineaPresupuesto.setContent(sc)
        println("estadoLinea: ---------------------"+estadoLinea.text)

        var datosLineas=objetosLP(descripcionLinea.text,codigoArticuloCreado ,precioArticulos,cantidadArticulos,estadoLinea,TAMontaje,TAMantenimiento,TATecnicos)


        LineasPresupuesto.put(descripcionLinea.text+"@"+codigoArticuloCreado.text,datosLineas)
        println(LineasPresupuesto.get(descripcionLinea.text+"@"+1))
    }

    @FXML
    fun buscaClientes(event: KeyEvent) {

        var texto: TextField = event.source as TextField
        var Campo: String = texto.id.trim()
        var rsCliente: ResultSet? = null
        var SQL: String = ""
        if (Campo == "nombreCliente") {
            SQL = "select * from clientes where Nombre like '" + texto.text + "%'"
        } else {
            SQL = "select * from clientes where id like " + texto.text + ""
        }
        try {
            rsCliente = bd.leeCliente(SQL)
            var existe: Boolean = rsCliente.first()
            if (existe) {
                clienteEncontrado.text = rsCliente.getString("Nombre")
                idCliente.text = rsCliente.getString("Id")
            }

        } catch (e: Exception) {
        }
    }

    fun buscaClientes2(clave:String) {


        var rsCliente: ResultSet? = null
        var SQL: String = ""

            SQL = "select * from clientes where id like " + clave + ""

        try {
            rsCliente = bd.leeCliente(SQL)
            var existe: Boolean = rsCliente.first()
            if (existe) {
                clienteEncontrado.text = rsCliente.getString("Nombre")
            }
            idCliente.text = rsCliente.getString("Id")
        } catch (e: Exception) {
        }
    }

}