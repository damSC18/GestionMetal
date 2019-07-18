package escandallos

import baseDeDatos.oopf
import baseDeDatos.operacionesBdEscandallo
import com.google.gson.JsonParser


import java.util.ArrayList
import java.util.HashMap

import javafx.collections.FXCollections

import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader

import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene

import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.effect.BoxBlur

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

import javafx.stage.Screen
import javafx.stage.Stage

import javafx.util.Callback

import java.awt.Desktop

import java.io.*
import java.net.URI



/**
 *
 * @author izquierda
 */
class ControladorArticulos {
    var codigoFamilia: Int=0
    // Se utiliza para codigoFamilia de Famila de Materias Primas a leer.
    // Y para código de familia del Producnto Fabricado que creamos. Antes de Grabar el producto seleccionamos
    // la familia

    var lv = ListView<Any>()

    internal var tab = Tab()
    internal var Contenedor: VBox = VBox()

    internal var contenedores = HashMap<String, VBox>()

    @FXML
    private val pestañas: TabPane = TabPane()
    @FXML
    private val ventanitas: TabPane = TabPane()

    @FXML
    private val listaJugadores = Pagination()
    @FXML
    private var ListaEquipos = Pagination()
    @FXML
    private val lista = ListView<String>()
    @FXML
    private val listaJ = ListView<Image>()
    @FXML
    internal var AñadirJugador: MenuItem? = null

    @FXML
    internal var BorrarPestañaActual: MenuItem = MenuItem()
    @FXML
    internal var rejillaJugadores: GridPane? = null

    @FXML
    var articulosAFabricacion: Menu = Menu()
    @FXML
    var contenedorArticulos: Pane = Pane()
    @FXML
    var panelImagen: HBox = HBox()
    @FXML
    private var tablas: TabPane = TabPane()
    @FXML
    private var MenuTab: Menu = Menu()
    var pestañaArticuloFabricadoActual: Tab = Tab()

    var contenidoPestaña = HashMap<String, HBox>()
    var PestañasCreadasEnEjecucioncontenidoPestaña = HashMap<String, Tab>()

    @FXML
    var articuloFabricacion: TextField = TextField()


    // Gestión de la ayuda

    @FXML
    var TipoAyuda = ComboBox<String>()


    @FXML
    var rejillaDatosCodigo: TableView<objetoArticuloFabricacion> = TableView()
    @FXML
    var  ContenedorPrincipal:GridPane=GridPane()


    @FXML
    fun Finalizar(event: ActionEvent) {

        try {

            val root = FXMLLoader.load<Parent>(javaClass.getResource("Metal.fxml"))
            val scene = Scene(root)
            val appStage = (event.getSource() as Node).getScene().getWindow() as Stage
            appStage.scene = scene
            appStage.toFront()
            appStage.show()

        } catch (e: Exception) {
        }
    }


    @FXML
    private fun generarPdf() {

        var informeSc:  escandallos.informes.informeScandallos=  escandallos.informes.informeScandallos()
        informeSc.informeScandallos((tablas))

/*
        val fileChooser: FileChooser = FileChooser()

        val selectedFile = fileChooser.showOpenDialog(null)
        if (selectedFile != null) {


            try {


                val bufferedReader: BufferedReader = File(selectedFile.absolutePath).bufferedReader()
                try {
                    val inputString = bufferedReader.use { it.readText() }
                    println(inputString)
                } catch (ex: IOException) {
                    println(ex.message)
                }
            } catch (ex: FileNotFoundException) {
                println(ex.message)
            }


        }

*/
    }


    @FXML
    private fun borraPestaña(event: ActionEvent) {
        try {
            var tt = Tab()
            tt = tablas.selectionModelProperty().get().selectedItem
            val nm = tablas.selectionModelProperty().get().selectedIndex
            tablas.tabs.remove(tt)
            MenuTab.items.removeAt(nm)
        } catch (ex: Exception) {
        }

    }

    /* @FXML
     var listaEquipos:Pagination = Pagination()*/
    @FXML
    private fun CargarFamilias(event: ActionEvent) {
        // Cargar Datos de Familia de articulos

        // Obtiene el tamaño de la ventana
        val screen2 = Screen.getScreens().get(0)
       println( screen2.getVisualBounds())
        // Obtiene la resolucion del dispositivo( de la pantalla del ordenador por ejemplo)
        println(Screen.getScreens() )
        println(Screen.getScreens().get(0).bounds.maxX )
        println(Screen.getScreens().get(0).bounds.maxY)
        println(Screen.getScreens().get(0).bounds.minX )
        println(Screen.getScreens().get(0).bounds.minY)


        ListaEquipos.setPrefSize(Pagination.USE_COMPUTED_SIZE,200.0)

        ListaEquipos.setPageCount(7)
        ListaEquipos.setMaxPageIndicatorCount(4)


        ListaEquipos.pageFactory = Callback { pageIndex -> createDatos(pageIndex!!) }


    }

    private fun crearPestañasArticulosPorFamiliaSeleccionada(familia: String) {


        try {


            var ficheroDatosArticulos: String = ControladorArticulos::class.java!!.getResource("Articulos.json").toExternalForm()


            var posFile: Int = ficheroDatosArticulos.indexOf("file:") + 6

            ficheroDatosArticulos = ficheroDatosArticulos.substring(posFile, ficheroDatosArticulos.length)

            var cadena = ficheroDatosArticulos.toString().replace("%20", " ")
            cadena = cadena.replace("/", "//")

            var posOut: Int = cadena.indexOf("out")

            cadena = cadena.substring(0, posOut) + "articulos.json"

            val parser = JsonParser()
            var fr = FileReader(cadena)//"d:\\Para lunes 29\\kEquipos\\src\\articulos.json")
            var datos = parser.parse(fr)
            var js: leerJson =leerJson()

            var listaArticulos: MutableList<objetoArticuloFabricacion> = mutableListOf<objetoArticuloFabricacion>()

            codigoFamilia = familia.substring(1, familia.indexOf("-") - 1).toInt()
            var nombreFamilia: String = familia.substring(familia.indexOf("-"), familia.length)

              println("codigoFamilia " +codigoFamilia)
            listaArticulos = js.LeerArticulosPorFamilia(datos, codigoFamilia)

            Rejilla_visualizar_articulos_Familia(nombreFamilia, listaArticulos)
        } catch (ex: Exception) {
            articuloFabricacion.text = "Error........... " + ex.message
        }
    }

    fun Rejilla_visualizar_articulos_Familia(nombreFamilia: String, listaArticulos: MutableList<objetoArticuloFabricacion>) {

        rejillaDatosCodigo.items.clear()
        rejillaDatosCodigo.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY )

        rejillaDatosCodigo.setEditable(true)

        rejillaDatosCodigo.columns.clear()
        // Añadir articulo familia a una nueva pestaña
        var ColumnaId = TableColumn<objetoArticuloFabricacion, Int>()
        ColumnaId.text = "Id"
        ColumnaId.setMinWidth(20.0);
        ColumnaId.setMaxWidth(20.0);
        ColumnaId.setResizable(false)
        ColumnaId.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricacion, Int>("iDarticulo"))

        ColumnaId.setOnEditStart({ t: TableColumn.CellEditEvent<objetoArticuloFabricacion, Int> ->

            javax.swing.JOptionPane.showMessageDialog(null, t.rowValue.toString() + "--" + t.tableColumn);
            var registroLinea: objetoArticuloFabricacion = t.rowValue
            javax.swing.JOptionPane.showMessageDialog(null, registroLinea.imagen);

        })


        var ColumnaDescripcion = TableColumn<objetoArticuloFabricacion, String>()
        ColumnaDescripcion.text = "Descripcion"
       //  ColumnaDescripcion.setMinWidth(100.0);
       //  ColumnaDescripcion.setMaxWidth(200.0);
        ColumnaDescripcion.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricacion, String>("descripcion"))

        var ColumnaIdFamilia = TableColumn<objetoArticuloFabricacion, Int>()
        ColumnaIdFamilia.text = "Id Familia"
       // ColumnaIdFamilia . setPrefWidth ( 20.0);
        ColumnaIdFamilia.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricacion, Int>("iDfamilia"))

        var ColumnaPrecioCoste = TableColumn<objetoArticuloFabricacion, Float>()
        ColumnaPrecioCoste.text = "Precio Coste"
       // ColumnaPrecioCoste . setPrefWidth ( 50.0);

        ColumnaPrecioCoste.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricacion, Float>("precioCoste"))

        var ColumnaImagen = TableColumn<objetoArticuloFabricacion, String>()
        ColumnaImagen.text = "Imagen"
       // ColumnaImagen . setPrefWidth ( 250.0);
        ColumnaImagen.setCellValueFactory(PropertyValueFactory<objetoArticuloFabricacion, String>("imagen"))
        ColumnaImagen.setEditable(true)



// Añade Imagen a pestaña Actual
        //https://es.stackoverflow.com/questions/103013/un-controlador-por-cada-tab-tabpane-javafx
        ColumnaImagen.setOnEditStart({ t: TableColumn.CellEditEvent<objetoArticuloFabricacion, String> ->
try {
    var registroLinea: objetoArticuloFabricacion = t.rowValue

    // Las imagenes de Articulos.json tienen que crearse en una carpeta llamad imagenes situada en la misma carpeta donde
    // creemos el ejecutable.

    var rutaImagenes = System.getProperty("user.dir") + "\\imagenes\\"
    rutaImagenes = "file:///" + rutaImagenes.replace("\\", "/")
    // javax.swing.JOptionPane.showMessageDialog(null,rutaImagenes)
    var imagenArticulo: Image = Image(rutaImagenes + registroLinea.imagen.toString().trim())
    var iii: ImageView = ImageView(imagenArticulo)
    // Guardamos el nombre del fichro Imagen en el id
    iii.id = registroLinea.imagen.toString().trim()
    iii.accessibleText = registroLinea.descripcion.toString().trim()

    // GUARDO EN EL ImageVies iii toda la información del registro MateriaPrima correspondiente a la imagen
    iii.setUserData(registroLinea)

    //Para recuperar la informacion
    // var    var registroLineaa: objetoArticuloFabricacion= iii.getUserData()
    //    iii.setStyle("-fx-background-size: 10px;-fx-background-color: white;-fx-border-style: solid")


    var panelDeImagenes: HBox = contenidoPestaña.getValue(pestañaArticuloFabricadoActual.text)

    //   panelDeImagenes.setStyle("-fx-border-color:white;-fx-background-color: black;");

    iii.setFitHeight(90.0);
    iii.setFitWidth(90.0);
    var datosMateriaPrima: VBox = VBox()
    datosMateriaPrima.children.add(iii)
    var Cantidad_MP: TextField = TextField()
    Cantidad_MP.text="1"
    Cantidad_MP.setMaxSize(90.0, 90.0)
    Cantidad_MP.setPromptText("Cantidad")
    datosMateriaPrima.children.add(Cantidad_MP)

    var PrecioCosteMP: TextField = TextField(registroLinea.precioCoste.toString())
    PrecioCosteMP.setMaxSize(90.0, 90.0)
    PrecioCosteMP.setPromptText("P. Coste")
    datosMateriaPrima.children.add(PrecioCosteMP)

    panelDeImagenes.children.add(datosMateriaPrima)



    contenidoPestaña.put(pestañaArticuloFabricadoActual.text, panelDeImagenes)

    val scrollPane = ScrollPane()
    scrollPane.setContent(panelDeImagenes);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);


    pestañaArticuloFabricadoActual.setContent(scrollPane)
}catch(e:Exception){e.printStackTrace()}

        })


      //  rejillaDatosCodigo.setPrefSize(780.0, 380.0)

        rejillaDatosCodigo.getColumns().addAll(ColumnaId, ColumnaDescripcion, ColumnaIdFamilia, ColumnaPrecioCoste, ColumnaImagen)
        for (art in listaArticulos) {


            var objetoPF: objetoArticuloFabricacion = objetoArticuloFabricacion(art.iDarticulo, art.descripcion, art.iDfamilia, art.precioCoste, art.imagen)


            rejillaDatosCodigo.items.add(objetoPF)





        }



    }

    @FXML
    fun crearTablaArticuloFabricado() {

        // javax.swing.JOptionPane.showMessageDialog(null, "Crear Pestaña")
        val tab = Tab()
        val np = tablas.tabs.size + 1

        tab.text = articuloFabricacion.text
        tab.content = Rectangle(200.0, 200.0, Color.LIGHTSTEELBLUE)
        // Guardar tabla recien creada como la actual donde añadir las imagenes


        var panelDeImagenes: HBox = HBox()
        panelDeImagenes.spacing = 10.0


        // Crea efectos en el HBox que visualiza las imágenes
        val boxBlur = BoxBlur()

        boxBlur.setWidth(10.37)
        boxBlur.setHeight(10.92)
        boxBlur.setIterations(2)
        //   panelDeImagenes.setEffect(boxBlur);


        tab.setContent(panelDeImagenes)

        pestañaArticuloFabricadoActual = tab
        contenidoPestaña.put(pestañaArticuloFabricadoActual.text, panelDeImagenes)

// Evento de las pestañas
        tab.setOnSelectionChanged(EventHandler<Event>() {
            pestañaArticuloFabricadoActual = tablas.selectionModelProperty().get().selectedItem
        });

        tablas.tabs.add(tab)
        val itemTab = MenuItem(articuloFabricacion.text)
        //http://docs.oracle.com/javafx/2/ui_controls/menu_controls.htm
        // Selecionamos la pesta´ña actual desde el menu
        itemTab.onAction = object : EventHandler<ActionEvent> {
            override fun handle(t: ActionEvent) {


                // Obtener el texto de la opcion de menu
                val tat: MenuItem = (t.getSource() as MenuItem) as MenuItem
                var nombrePestañaSeleccionadaPorMenu = tat.text
                println("Pestaña menu" + nombrePestañaSeleccionadaPorMenu)
                pestañaArticuloFabricadoActual = PestañasCreadasEnEjecucioncontenidoPestaña.getValue(nombrePestañaSeleccionadaPorMenu)

                println("Pestaña menu" + pestañaArticuloFabricadoActual)
                tablas.selectionModel.select(pestañaArticuloFabricadoActual)


            }
        }
        articulosAFabricacion.items.add(itemTab)
        PestañasCreadasEnEjecucioncontenidoPestaña.put(pestañaArticuloFabricadoActual.text, tab)
    }


    @FXML
    private fun AsignarTablaAEquipo(event: MouseEvent) {


    }

    @FXML
    private fun AnadirJugadorAEquipo(event: MouseEvent) {

    }

    fun initialize() {




        var anchoPantalla:Double=Screen.getScreens().get(0).bounds.maxX-150
        var altoPantalla:Double=Screen.getScreens().get(0).bounds.maxY-200
        ContenedorPrincipal.setPrefSize(anchoPantalla,altoPantalla)
        ContenedorPrincipal.setAlignment(Pos.CENTER)
       // ContenedorPrincipal.setLayoutX(300.0)
     //   ContenedorPrincipal.layoutX=10.0//(anchoPantalla/2)+25
      //  ContenedorPrincipal.layoutY=10.0//(altoPantalla/2)+25
        TipoAyuda.getItems().add(".chm");
        TipoAyuda.getItems().add(".pdf");
        TipoAyuda.getItems().add(".docx");
        TipoAyuda.getItems().add(".html");
    }

    private fun createDatos(pageIndex: Int): Node {


        var ficheroFamiliasArticulo: String = ControladorArticulos::class.java!!.getResource("familias.json").toExternalForm()

        var posFile: Int = ficheroFamiliasArticulo.indexOf("file:") + 6


        ficheroFamiliasArticulo = ficheroFamiliasArticulo.substring(posFile, ficheroFamiliasArticulo.length)


        var cadena = ficheroFamiliasArticulo.toString().replace("%20", " ")
        cadena = cadena.replace("/", "//")

        var posOut: Int = ficheroFamiliasArticulo.indexOf("out") + 2

        cadena = cadena.substring(0, posOut) + "familias.json"


        /*  var fff:File=File(cadena)

        val bufferedReader: BufferedReader = File(fff.absolutePath).bufferedReader()

       val inputString = bufferedReader.use { it.readText() }*/


        val parser = JsonParser()
        var fr = FileReader(cadena)//"j:\\Para lunes 29\\kEquipos\\src\\familias.json")
        var datos = parser.parse(fr)

        var js: leerJson = leerJson()

        var stringList: ArrayList<String> = arrayListOf<String>()
        stringList = js.LeerFamilias(datos)




        println("Indice$pageIndex")

        println("createDatos")

        val data = ArrayList<String>(8)
        val inicio = pageIndex * 2
        //  for (familia in stringList) {
        for (i in inicio..inicio + 1) {

            data.add(stringList[i])
            //  print(familia)
        }
        val observableList = FXCollections.observableList<String>(data)

        println(observableList[0])
        lista.setItems(observableList)
        println("LIST ITEM " + lista.items.get(0))

        // Evento click en ListView
        lista.setOnMouseClicked() {
            println(lista.selectionModel.selectedItem + "  psocion :" + lista.selectionModel.selectedIndex)
            crearPestañasArticulosPorFamiliaSeleccionada(lista.selectionModel.selectedItem)
        }
        return BorderPane(lista)

    }


    private fun createData(pageIndex: Int): List<*> {
        println("createData")
        val data = ArrayList<Any>(24)
        val inicio = pageIndex * 8
        for (i in inicio until inicio + 9) {

            data.add("Equipo  $i")
        }

        return data
    }
    // Jugadores

    // Visualiza Ayuda
    @FXML
    fun VisualizarAyuda() {

        var rutaimagen = System.getProperty("user.dir")


        var extension:String = ".chm";
        extension=   TipoAyuda.getValue();
        System.out.println("extension:"+extension);
        if (extension==".chm") {
            extension = "Ayudas/AyudaJavaFX.chm";
        }
        if (extension==".pdf") {
            extension = "Ayudas/AyudaJavaFX.pdf";
        }
        if (extension==".docx") {
            extension = "Ayudas/AyudaJavaFX.docx";
        }
        if (extension==".html") {
            extension = "Ayudas/Html/index.html";

        }


        /*
        var res:String = "ControladorArticulos.class"



        var ruta:String = ControladorArticulos::class.java.getResource(res).path.toString();


        var sobra:Int=  ruta.indexOf("GestionMetal") +16;
        var posFile=ruta.indexOf("file:")
        if( posFile==-1){posFile=1}else{posFile=0}
        ruta = ruta.substring(posFile, sobra);

        ruta = ruta + "/" + extension;

       ruta = ruta.toString().replace("%20", " ")*/

        var desktop: Desktop = Desktop.getDesktop();


        rutaimagen=rutaimagen+"/"+extension
        rutaimagen = rutaimagen.replace("\\", "//")
     //   javax.swing.JOptionPane.showMessageDialog(null,rutaimagen)

        desktop.browse( URI(rutaimagen))

    }
    fun VisualizarAyudas() {
        var extension:String = ".chm";
        extension=   TipoAyuda.getValue();
        System.out.println("extension:"+extension);
        if (extension==".chm") {
            extension = "Ayudas/AyudaJavaFX.chm";
        }
        if (extension==".pdf") {
            extension = "Ayudas/AyudaJavaFX.pdf";
        }
        if (extension==".docx") {
            extension = "Ayudas/AyudaJavaFX.docx";
        }
        if (extension==".html") {
            extension = "Ayudas/Html/index.html";

        }
        // String res = "Ayuda";
        var res:String = "ControladorArticulos.class"
        // Se utiliza para buscando este fichero encontar la rura de la carpeta Ayuda

        var ruta:String = ControladorArticulos::class.java.getResource(res).path.toString();
        //   javax.swing.JOptionPane.showConfirmDialog(null, ruta)

        //   javax.swing.JOptionPane.showConfirmDialog(null, "------------------"+ ( ruta.indexOf("jar")>-1))
        // Obtenemos la rura del fichero Vecinos.class , atnto en la estructura del proyecto fuente como en el .jar
        //  javax.swing.JOptionPane.showMessageDialog(null, "Ruta de la calse Principal  :" + ruta);

        // String sep = File.separator + "";
        // Si la ruta comienza por 'jar:', que es el caso de estar ejecutando la aplicacion.jar
        if (ruta.indexOf("jar")>-1) {

            javax.swing.JOptionPane.showConfirmDialog(null, "Ruta Jar ...:"+ruta)
            var posicion:Int = ruta.indexOf("kMetalScandallo");
            javax.swing.JOptionPane.showConfirmDialog(null, "Ruta Jar posicion kMetal :"+posicion)
            // Buscamos la posición del nombre del ejecutable.jar en la ruta

            if (posicion != -1) {
                // Si ha encontrado la subcadena, quitamos 'jar:' de la ruta y le sumamos la extensión
                // para obtener la ruta de la carpeta Ayudas que contiene los disitintos tipos dee
                // ayuda.
                //Es necesario copiar la carpeta que contiene los ficheros de ayuda en la
                // carpeta Ayuda, en el mismo directorio donde esta el jar ejecutable  de la aplicación
                ruta = ruta.substring(6, posicion+7) + extension;
                javax.swing.JOptionPane.showConfirmDialog(null,"Ruta Jar Cortada :"+ ruta)

            }

        } else {  // Si ejecutamos la ayuda desde el fuente
            // cogemos de la ruta hasta el directorio en el que esta creado el proyecto

            var sobra:Int=  ruta.indexOf("kMetal") +6;

            ruta = ruta.substring(1, sobra);
            // Le sumamos la extensión para obtener la ruta del tipo de ayuda que queremos
            ruta = ruta + "/" + extension;

        }
        var desktop: Desktop = Desktop.getDesktop();
        // Visualiza cualquier tipo de ayuda por medio de la aplicación asociado al tipo del fichero

        ruta = ruta.toString().replace("%20", " ")
        //ruta = ruta.replace("/", "//")
        ruta="file:///"+ruta
        println( ruta)
        javax.swing.JOptionPane.showConfirmDialog(null,"Ultima ...: "+ ruta)
        desktop.browse( URI(ruta));



    }
    @FXML
    fun GrabarArticuloFabricado(){
        println("Grabar articulop fabricados Familia:"+codigoFamilia)
        var bd : operacionesBdEscandallo =  operacionesBdEscandallo()
        bd.conexionBd()


        // Obtiene el ultimo ID autoincrement de la tabal ArticuloFabricado.
        // Sumamos 1 para saber el que vamos a crear a continuación
        // Lo necesitamos para ir creaando los registros en la tabla EscandallosArticulosFabricados
        var id_NuevoArticuloFabricado:Int=0
        id_NuevoArticuloFabricado=bd.ffleerUltimoClaveGrabadaAutoincrement()
        id_NuevoArticuloFabricado=id_NuevoArticuloFabricado+1

        println("Nuevo Id a Generar "+id_NuevoArticuloFabricado)


        var  articulo: oopf = oopf()

        articulo=obtenerDatosArticuloFabricadoEnFabricacion()

        println("ARTICULO "+ articulo)
        // Conexion a la base de datos


       //  var registroLineaa: objetoArticuloFabricacion= iii.getUserData()



        var precioCosteAf:Float=0f

        // Obtenemos la Tab activa en este momento , es decir el diseño del articulo que estamos fabricando

        var pestañaArticuloFabricacion:Tab = tablas.selectionModelProperty().get().selectedItem

        val descripcionAF:String=pestañaArticuloFabricacion.text


        // Obtenemos el ScrollPane que contiene el HBox que a su vez contiene los VBox con las materias primas seleccioandas

        var CScrollPane:ScrollPane = pestañaArticuloFabricacion.getContent() as ScrollPane


        // Obtenemos el HBox

        var CHBox: HBox = CScrollPane.content as HBox


        //Recorremos los VBox del HBox
        for (i: Int in 0..CHBox.children.size - 1) {
            // Optenemos el VBox
            var CVBox: VBox = CHBox.children.get(i) as VBox

            //Obtenemos la ImagenView del VBox que es el elemento 0 del Array de objetos que contiene
             var  CImageView: ImageView  = CVBox.children.get(0) as ImageView

            // Cantidad
            var CTextField1: TextField =  CVBox.children.get(1) as TextField

            var cantidadMP:Int=CTextField1.text.toInt()

            // Precio Coste
            var CTextField2: TextField = CVBox.children.get(2) as TextField
            var precioCosteMP:Float=CTextField2.text.toFloat()

            // Calcula Precio coste ArticuloFabricado
            precioCosteAf=cantidadMP*precioCosteMP
            // Añadir la imagen del articulo a la linea de la tabla

            // Grabar en Escandallo. Por cada Materia prima que forma parte del ArticuloFabricado grabamos un registro
            // en la tabla [EscandallosArticulosFabricados]
            // Leemos del UserData de la imagen la información de la Materia Prima
            var registroLineaa: objetoArticuloFabricacion= CImageView.getUserData() as objetoArticuloFabricacion

            bd.ffgrabaResgsitrosScandalloMateriaPrima(id_NuevoArticuloFabricado,registroLineaa.iDarticulo,cantidadMP)

        }

        // Creamos objeto de tipo Articulo
        articulo.oopf(0,descripcionAF,codigoFamilia,precioCosteAf,precioCosteAf,0,0,"Imagenes\\icono.jpg")
        // LLamamos al méodpo de la base de datos que graba el articuloFabricadoNuevo en la base de datos
        bd.ffgrabaRegistro(articulo)

    }

    fun obtenerDatosArticuloFabricadoEnFabricacion():oopf
    {



        var precioCosteAf:Float=0f

           // Obtenemos la Tab activa en este momento , es decir el diseño del articulo que estamos fabricando
        var objeto = Tab()
        objeto = tablas.selectionModelProperty().get().selectedItem

                val descripcionAF:String=objeto.text
                // Recorremos cada tab
                var CpestañaAticulo: Tab = objeto
                println("Es Control ttt " + CpestañaAticulo.styleClass)

                var ArticuloAFabricar:String= CpestañaAticulo.text


                val ContenedorDeLaPestaña = CpestañaAticulo.getContent()
                println("Es Control treeTableView  " + ContenedorDeLaPestaña.styleClass)

                var CScrollPane = ContenedorDeLaPestaña as ScrollPane
                println("Es Control  ssss   " + CScrollPane.styleClass)

                var CHBox = CScrollPane.content
                var CcHBox: HBox = HBox()
                CcHBox = CHBox as HBox
                println("Es Control  CHBox   " + CHBox.children.get(0).styleClass)


                for (i: Int in 0..CHBox.children.size - 1) {
                    if(i>0){}
                    var CVBox: VBox = VBox()
                    CVBox = CHBox.children.get(i) as VBox

                    println("Es Control  CVBox   " + CVBox.children.size)

                    var CImageView: ImageView = ImageView()
                    CImageView = CVBox.children.get(0) as ImageView

                    println("Es Control  Fichero Imagen   " + CImageView.id)

                    var rutaImagenes  = System.getProperty("user.dir")+"\\Imagenes\\"+CImageView.id
                    rutaImagenes ="file:///" +   rutaImagenes.replace("\\", "/")
                    //javax.swing.JOptionPane.showMessageDialog(null,rutaImagenes)

                    //   var imagenMateriaFabricacion = com.lowagie.text.Image.getInstance(javaClass.getResource( rutaImagenes ))
                    var imagenMateriaFabricacion = com.lowagie.text.Image.getInstance( rutaImagenes )




                    var CTextField1: TextField = TextField()
                    CTextField1 = CVBox.children.get(1) as TextField
                    var cantidadMP:Int=CTextField1.text.toInt()
                            //precioCosteAf
                //    println("Es Control  Cantidad   " + CTextField1.text)


                    var CTextField2: TextField = TextField()
                    CTextField2 = CVBox.children.get(2) as TextField

                    println("Es Control  Precio   " + CTextField2.text)
                    var precioCosteMP:Float=CTextField2.text.toFloat()

                    precioCosteAf=cantidadMP*precioCosteMP
                    // Añadir la imagen del articulo a la linea de la tabla

                 }

                    var  articulo: oopf = oopf()
                    articulo.oopf(0,descripcionAF,codigoFamilia,precioCosteAf,precioCosteAf,0,0,"Imagenes\\icono.jpg")


        return articulo
    }
}

