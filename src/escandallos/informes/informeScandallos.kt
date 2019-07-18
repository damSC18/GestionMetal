package  escandallos.informes

import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox

import javafx.scene.control.TabPane
import javafx.scene.layout.HBox
import com.lowagie.text.*
import com.lowagie.text.pdf.PdfAction
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import javafx.scene.image.Image
import java.awt.Color
import java.awt.Desktop

import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import java.text.DecimalFormat

class informeScandallos {

    internal var rutaimagen = System.getProperty("user.dir") + "\\EscandalloPf.PDF"
    private  var ruta:String=rutaimagen.toString()
    private val fdoble = DecimalFormat("###,###,###.##")
    private val fsimple = DecimalFormat("###,###,###")
    val PageSize = com.lowagie.text.Rectangle(700f, 1024f)
    var cont = 0

    val document = Document(PageSize)
    var writer=PdfWriter.getInstance(document, FileOutputStream(rutaimagen))
    fun cabecera() {
        try {



            val footer = HeaderFooter(Phrase("Curso 2018-2019  ...   DAM                                                                                 nº Pagn.: "), true)

            document.setFooter(footer)
            val header = HeaderFooter(Phrase("Escandallo Articulos a Fabricar", FontFactory.getFont(FontFactory.COURIER_BOLD, 12f)), false)
            document.setHeader(header)

            document.open()

            //com.lowagie.text.Image.getInstance( "O:\\_2010_2011_ImpresionConJava\\ImpresionJR\\ImpresionJR\\imagenes\\caballo.jpg");
            //      getClass().getResource("imagenes/Bienvenida.png"));~


            val gif = com.lowagie.text.Image.getInstance(javaClass.getResource("Primero.PNG"))
            val gif1 = com.lowagie.text.Image.getInstance(javaClass.getResource("Siguiente.PNG"))
            val gif2 = com.lowagie.text.Image.getInstance(javaClass.getResource("Anterior.PNG"))
            val gif3 = com.lowagie.text.Image.getInstance(javaClass.getResource("Ultimo.PNG"))
            gif.scaleAbsoluteWidth(25f)
            gif.scaleAbsoluteHeight(25f)
            gif1.scaleAbsoluteWidth(25f)
            gif1.scaleAbsoluteHeight(25f)
            gif2.scaleAbsoluteWidth(25f)
            gif2.scaleAbsoluteHeight(25f)
            gif3.scaleAbsoluteWidth(25f)
            gif3.scaleAbsoluteHeight(25f)

            val table0 = PdfPTable(4)
            // table0.defaultCell.fixedHeight=30f
            table0.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
            table0.defaultCell.setBorderColor(Color(255, 255, 255))
            table0.defaultCell.setBackgroundColor(Color(233, 249, 236))
            //Creamos los botones de posicionamiento entre paginas
            val a = Chunk(gif, 10f, 1f).setAction(PdfAction(PdfAction.FIRSTPAGE))

            val b = Chunk(gif1, 10f, 2f).setAction(PdfAction(PdfAction.NEXTPAGE))

            val c = Chunk(gif2, 10f, 3f).setAction(PdfAction(PdfAction.PREVPAGE))

            val d = Chunk(gif3, 10f, 0f).setAction(PdfAction(PdfAction.LASTPAGE))

            table0.addCell(Phrase(a))

            table0.addCell(Phrase(b))
            table0.addCell(Phrase(c))
            table0.addCell(Phrase(d))

            document.add(table0)


            // Datos de la Empresa
            //Empresa
            val pro = PdfPTable(1)
            pro.defaultCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER)
            val lempresa1 = Phrase(18f, Chunk("CIFP Santa Catalina", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18f, Font.BOLD, Color(255, 174, 63))))
            pro.addCell(lempresa1)
            val lempresa2 = Phrase(18f, Chunk("C/ Montelatorre 11", FontFactory.getFont(FontFactory.HELVETICA, 14f, Font.NORMAL)))
            pro.addCell(lempresa2)
            val lempresa3 = Phrase(18f, Chunk("Telefono : 947 54 63 51", FontFactory.getFont(FontFactory.HELVETICA, 14f, Font.ITALIC)))
            pro.addCell(lempresa3)
            val lempresa4 = Phrase(18f, Chunk("FAX      : 947 00 24 68", FontFactory.getFont(FontFactory.HELVETICA, 14f, Font.ITALIC)))
            pro.addCell(lempresa4)
            val lempresa5 = Phrase(18f, Chunk("09492 ARANDA DE DUERO (Burgos)", FontFactory.getFont(FontFactory.HELVETICA, 14f, Font.BOLD)))
            pro.addCell(lempresa5)
            val lweb = Phrase(18f, Chunk("www.fpsantacatalina.com", FontFactory.getFont(FontFactory.HELVETICA, 8f, Font.BOLD, Color(52, 73, 255))))
            pro.addCell(lweb)

            pro.totalWidth = 300f

            pro.writeSelectedRows(0, -1, 10f, 890f, writer.directContent)

            // Cabecera Datos
            var table1 = PdfPTable(5)
            val tc = floatArrayOf(222f,222f, 76f, 76f, 86f)
            table1.addCell("Art. Fabricación")
            table1.addCell("Mat.Prima")

            table1.addCell("Cantidad")
            table1.addCell("Precio Coste")
            table1.addCell("Imagen")
            cont = 0

            table1.setTotalWidth(tc)
            table1.writeSelectedRows(0, -1, 10f, 750f, writer.directContent)
            //




        } //....................
        catch (ex: IOException) {
            ex.printStackTrace() //  javax.swing.JOptionPane.showMessageDialog(null, "Error"+ex.getMessage()+"-"+rutaimagen);
        }
        catch (ex: DocumentException) {
            ex.printStackTrace()  // javax.swing.JOptionPane.showMessageDialog(null, "documento"+rutaimagen);
        }




    }

    fun informeScandallos(contenedor: TabPane) {
         cabecera()
        var cont:Int=0

        var tablaDatos = PdfPTable(5)

        val tc = floatArrayOf(222f,222f, 76f, 76f, 86f)

        for (objeto in contenedor.tabs.toList()) {


            println("Es Control  objeto" + objeto.styleClass)
            // Recorremos cada tab
            var CpestañaAticulo: Tab = objeto
            println("Es Control ttt " + CpestañaAticulo.styleClass)

            var ArticuloAFabricar:String= CpestañaAticulo.text
            tablaDatos.addCell(ArticuloAFabricar)

            val ContenedorDeLaPestaña = CpestañaAticulo.getContent()
            println("Es Control treeTableView  " + ContenedorDeLaPestaña.styleClass)

            var CScrollPane = ContenedorDeLaPestaña as ScrollPane
            println("Es Control  ssss   " + CScrollPane.styleClass)

            var CHBox = CScrollPane.content
            var CcHBox: HBox = HBox()
            CcHBox = CHBox as HBox
            println("Es Control  CHBox   " + CHBox.children.get(0).styleClass)


            for (i: Int in 0..CHBox.children.size - 1) {
                if(i>0){   tablaDatos.addCell("")}
                var CVBox: VBox = VBox()
                CVBox = CHBox.children.get(i) as VBox

                println("Es Control  CVBox   " + CVBox.children.size)

                var CImageView: ImageView = ImageView()
                CImageView = CVBox.children.get(0) as ImageView

                //javax.swing.JOptionPane.showMessageDialog(null,"Es Control  Fichero Imagen   " + CImageView.id)


                // Las imagenes de Articulos.json tienen que crearse en una carpeta llamad imagenes situada en la misma carpeta donde
                // creemos el ejecutable.

                var rutaImagenes  = System.getProperty("user.dir")+"\\Imagenes\\"+CImageView.id
                rutaImagenes ="file:///" +   rutaImagenes.replace("\\", "/")
                //javax.swing.JOptionPane.showMessageDialog(null,rutaImagenes)

             //   var imagenMateriaFabricacion = com.lowagie.text.Image.getInstance(javaClass.getResource( rutaImagenes ))
                var imagenMateriaFabricacion = com.lowagie.text.Image.getInstance( rutaImagenes )
                println("Es Control  Nombre Maertia Prima  " + CImageView.accessibleText)
                tablaDatos.addCell(CImageView.accessibleText)


                var CTextField1: TextField = TextField()
                CTextField1 = CVBox.children.get(1) as TextField

                println("Es Control  Cantidad   " + CTextField1.text)
                tablaDatos.addCell( CTextField1.text)

                var CTextField2: TextField = TextField()
                CTextField2 = CVBox.children.get(2) as TextField

                println("Es Control  Precio   " + CTextField2.text)
                tablaDatos.addCell(CTextField2.text)


                // Añadir la imagen del articulo a la linea de la tabla
                tablaDatos.addCell(imagenMateriaFabricacion)
            }


            cont = cont + 1
            if (cont == 2) {
                tablaDatos.setTotalWidth(tc)
                tablaDatos.writeSelectedRows(0, -1, 10f, 720f, writer.directContent)
                document.newPage()
                  tablaDatos = PdfPTable(5)

                cont=0
                cabecera()
            }
        }

        tablaDatos.setTotalWidth(tc)
        tablaDatos.writeSelectedRows(0, -1, 10f, 720f, writer.directContent)
        document.close()
        // Visualizar PDF
        var desktop: Desktop = Desktop.getDesktop();
        ruta ="file:///"+ ruta.replace("\\", "/")
        //ruta="file:///"+"G:/ParaMañana/compiladoPdf/Factura.PDF"

        desktop.browse( URI(ruta));
        //  desktop.browse( URI("file:///"+G:/ParaMañana/compiladoPdf/Factura.PDF"));
    }
}