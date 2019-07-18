package  Taller.ParteFabricacionTaller

import Presupuestos.objetoPresupuesto
import Taller.TallerBd
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
import java.sql.ResultSet
import java.text.DecimalFormat

class informeParteTallerItext {

    internal var rutaimagen = System.getProperty("user.dir") + "\\ParteTallerItext.PDF"
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
            val header = HeaderFooter(Phrase("Gestión Metal", FontFactory.getFont(FontFactory.COURIER_BOLD, 16f)), false)
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
            val lempresa1 = Phrase(18f, Chunk("Hoja Diario Fabricación Taller ", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18f, Font.BOLD, Color(255, 174, 63))))

            pro.addCell(lempresa1)
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

    fun informePresupuestos(bd: TallerBd, contenedor:  TableView<objetoPresupuesto>) {
         cabecera()
        var cont:Int=0

        var tablaDatosPresupuesto = PdfPTable(5)

        val tc = floatArrayOf(50f,60f, 226f, 76f, 386f)
        // Recorre los presupuestos selecionados en ControladorTaller
        for (presupuesto in contenedor.items.toList()) {

            println(presupuesto.id_Cliente)
            // Leer Cliente
            var sql:String="Select * from clientes where id="+presupuesto.id_Cliente
            var rs: ResultSet = bd.leeCliente(sql)
            var existe:Boolean=rs.first()
            var NombreCliente:String="Inexistente"
            if(existe){NombreCliente=rs.getString("Nombre")
            println(rs.getString("Nombre"))
            }
            tablaDatosPresupuesto.addCell(presupuesto.id_Presupuesto.toString())
            tablaDatosPresupuesto.addCell(presupuesto.Fecha.toLocalDate().toString())
            tablaDatosPresupuesto.addCell(NombreCliente)
            tablaDatosPresupuesto.addCell(presupuesto.Estado)
            tablaDatosPresupuesto.addCell(presupuesto.Descripcion)

            cont = cont + 1
            if (cont == 2) {
                tablaDatosPresupuesto.setTotalWidth(tc)
           //     tablaDatosPresupuesto.writeSelectedRows(0, -1, 10f, 720f, writer.directContent)
                document.add(tablaDatosPresupuesto)
                document.newPage()
                tablaDatosPresupuesto = PdfPTable(5)

                cont=0
                cabecera()
            }
        }

        tablaDatosPresupuesto.setTotalWidth(tc)
        tablaDatosPresupuesto.writeSelectedRows(0, -1, 10f, 720f, writer.directContent)
        document.close()
        // Visualizar PDF
        var desktop: Desktop = Desktop.getDesktop();
        ruta ="file:///"+ ruta.replace("\\", "/")
        //ruta="file:///"+"G:/ParaMañana/compiladoPdf/Factura.PDF"

        desktop.browse( URI(ruta));
        //  desktop.browse( URI("file:///"+G:/ParaMañana/compiladoPdf/Factura.PDF"));
    }
}