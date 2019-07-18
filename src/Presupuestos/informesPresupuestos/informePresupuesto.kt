package Presupuestos.informesPresupuestos

import javafx.application.Application
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import javax.swing.JOptionPane
import net.sf.jasperreports.engine.xml.JRXmlLoader
import net.sf.jasperreports.engine.design.JasperDesign
import java.io.FileNotFoundException
import java.io.FileInputStream
import java.io.InputStream
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuter
import java.util.*
import java.util.Map
import java.util.logging.Level
import java.util.logging.Logger
import javax.security.auth.login.Configuration.setConfiguration
import net.sf.jasperreports.export.SimplePdfExporterConfiguration
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import net.sf.jasperreports.export.OutputStreamExporterOutput
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.ExporterInput
import net.sf.jasperreports.engine.export.JRPdfExporter
import java.awt.Desktop
import java.io.File
import java.net.URI
import java.util.HashMap


import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement;


import java.sql.ResultSetMetaData
public class informePresupuesto() {
    var stmt: Statement? = null
    var connection: Connection? = null

    fun imprimefichero(npresupuesto:String) {
       //  var rutareporte = System.getProperty("user.dir") + "\\InformePresupuesto.jrxml"
       var rutareporte = System.getProperty("user.dir") + "\\InformePresupuesto.jasper"
     //   var rutareporte =  "E:\\MV\\copia\\GestionMetal\\src\\Presupuestos\\informesPresupuestos\\InformePresupuesto.jrxml"
     //   javax.swing.JOptionPane.showMessageDialog(null,rutareporte)

// Se utiliza si lo hacemos con el fichero sin comipla jrxml en lugar de jasper
    //     var jasperReport: JasperReport = JasperCompileManager.compileReport(rutareporte)//l3.jrxml");

        // Parameters for report
        //  Map<String, Object> parameters = new HashMap<String, Object>();


        var parameters = HashMap<String, Any>()
        parameters.put("numeroPresupuesto", npresupuesto)
        // DataSource
        // This is simple example, no database.
        // then using empty datasource.
        var dataSource: JRDataSource = JREmptyDataSource();
        conn()

        // SEsta opcion con fichero sin comoilar
      //   var jasperPrint: JasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
      var jasperPrint: JasperPrint = JasperFillManager.fillReport(rutareporte, parameters, connection);


        // Make sure the output directory exists.
        //var outDir:File  =   File("LitsadoProductosFabricados.jrxml");
        //outDir.mkdirs();

        // Export to PDF.
        //getClass().getProtectionDomain().getCodeSource().getLocation()
        var rutaPdf = System.getProperty("user.dir") + "\\Presupuesto"+npresupuesto+".PDF"
     //    javax.swing.JOptionPane.showMessageDialog(null,rutaPdf)
        JasperExportManager.exportReportToPdfFile(jasperPrint, rutaPdf);

        var desktop: Desktop = Desktop.getDesktop();

        rutaPdf = "file:///" + rutaPdf.replace("\\", "/")
        desktop.browse(URI(rutaPdf));
    }

    fun conn() {
        // Lee los parámetros de conexión al gestor de Base de Datos
        // establecidos en un fichero de texto. C:/GestionMetal/conexion.txt
        var UrlficheroConexion = System.getProperty("user.dir") + "\\conexion.txt"
        val parametrosConexion = FileInputStream(UrlficheroConexion).bufferedReader().use { it.readText() }

        var datosconexion=parametrosConexion.split(",")

        // val connectionString = "jdbc:sqlserver://90d18277a66e\\sqlexpress;database=metal;user=sa;password=sa_sa_2018@2018sa";
        // val connectionString = "jdbc:sqlserver://localhost:1433;database=metal;user=sa;password=dam2";
        val connectionString = "jdbc:sqlserver://"+datosconexion[0]+":"+datosconexion[1]+";database=Metal;user="+datosconexion[2]+";password="+datosconexion[3];
        try {

            //  Class.forName("org.sqlite.JDBC")

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            connection = DriverManager.getConnection(connectionString)


            val SQL = "SELECT * FROM ArticulosFabricados"//where id_ArticuloFabricado >4003"



            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            var rs: ResultSet? = null
            rs = stmt!!.executeQuery(SQL)
            rs.first()
            println(rs.getString(1))
///////////////////////////////////////////////////////////////////


            // Iterate through the data in the result set and display it.


        } catch (e: Exception) {
            e.printStackTrace()


        }


    }
}
fun main(param: Array<String>) {
    print("Estoy Aprendiendo contigo :http://codigoxules.org/java-itext-pdf-creando-pdf-java-itext/\n")
    print("Librerias kotlin   https://kotlin.link/\n")


    //imprimefichero.imprimefichero()

}
