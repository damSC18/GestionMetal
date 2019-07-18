package Clientes

//Use the JDBC driver


import ProductosFabricados.objetoArticuloFabricado

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.io.FileInputStream

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

import java.sql.Statement;
import java.sql.ResultSetMetaData
import java.util.ArrayList


class operacionesBd {

    // Buscar en el curso  actual
    private var posicion: Int = 0
    private var lc: ResultSetMetaData? = null


    var clienteLeido =  objetoCliente(0,"","" ,"","","","","","","","","","")
    var stmt: Statement? = null
    var connection: Connection? = null
    var rs: ResultSet? = null

    //  var metaData: ResultSetMetaData? = null
    var columnNames = arrayOf<String>()

    fun conexionBd() {
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


            val SQL = "SELECT * FROM Clientes"



            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
///////////////////////////////////////////////////////////////////


            // Iterate through the data in the result set and display it.


        } catch (e: Exception) {
            e.printStackTrace()


        }
    }

    fun listado(): MutableList<objetoCliente> {
        // Declare the JDBC objects.

        var listaArticulos: MutableList<objetoCliente> = mutableListOf<objetoCliente>()
        val SQL = "SELECT * FROM clientes"

        try {

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)

            val SQL = "SELECT * FROM clientes"



            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
            // Iterate through the data in the result set and display it.
            while (rs!!.next()) {
                println("Dato " + rs!!.getString(1) + " " + rs!!.getString(2))
                var registroLeiodo = objetoCliente(rs!!.getInt(1), rs!!.getString(2), rs!!.getString(3), rs!!.getString(4), rs!!.getString(5), rs!!.getString(6), rs!!.getString(7), rs!!.getString(8), rs!!.getString(9), rs!!.getString(10), rs!!.getString(11), rs!!.getString(12), rs!!.getString(13))
                listaArticulos.add(registroLeiodo)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } /*finally {
                if (connection != null) try {
                    connection!!.close()
                } catch (e: Exception) {
                }
            }*/

        return listaArticulos
    }

    // Actualiza el cursos en memoria con los datos que leemos de la BD
    fun actualizaCursor() {
        val SQL = "SELECT * FROM clientes"

        try {

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun ffgrabaRegistro(articulo:  objetoCliente) {
        val SQL = " insert into clientes (Nombre,Direccion,Poblacion,Provincia,CodigoPostal,CifNif,Telefono1,Telefono2,Email,Web,PresonaContacto,SectorComercial) values(" + "'" + articulo.nombre + "','" + articulo.direccion + "','" + articulo.poblacion + "','" + articulo.provincia + "','" + articulo.codigoPostal+ "','" + articulo.cif_Nif+ "','" + articulo.telefono1+"','" + articulo.telefono2+"','" + articulo.email+"','" + articulo.web+"','" + articulo.personaContacto+"','" + articulo.sectorComercial+ "')"
        println(SQL)
        try {
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            var cr: Boolean = stmt!!.execute(SQL)

            // Actualiza el cursos en memoria con los datos que leemos de la BD
            actualizaCursor()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun ffmodificaRegistro(cliente:  objetoCliente){
        val SQL = " update   clientes set nombre='"+cliente.nombre+"',Direccion='"+cliente.direccion+"',Poblacion='"+cliente.poblacion+"',Provincia='"+cliente.provincia+"',CodigoPostal='"+cliente.codigoPostal+"',CifNif='"+cliente.cif_Nif+"',Telefono1='"+cliente.telefono1+"',Telefono2='"+cliente.telefono2+"',Email='"+cliente.email+"',Web='"+cliente.web+"',PresonaContacto='"+cliente.personaContacto+"',SectorComercial='"+cliente.sectorComercial+"' where id="+cliente.id
        println(SQL)
        try {
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            var cr: Boolean = stmt!!.execute(SQL)

            // Actualiza el cursos en memoria con los datos que leemos de la BD
            actualizaCursor()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun ffborraRegistro(idCliente: Int) {

        val SQL = "delete  from  clientes where id =" + idCliente
        println(SQL)
        try {
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            var cr: Boolean = stmt!!.execute(SQL)

            // Actualiza el cursos en memoria con los datos que leemos de la BD
            actualizaCursor()

        } catch (e: Exception) {
            e.printStackTrace()
        }


        actualizaCursor()
    }

    fun fleerPrimerRegistro():  objetoCliente{


        try {

            rs!!.first()

            clienteLeido = crearRegistroArticuloVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return clienteLeido
    }


    fun fleerUltimoRegistro():  objetoCliente {


        try {

            rs!!.last()

            clienteLeido = crearRegistroArticuloVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return clienteLeido
    }

    fun fleerSiguienteRegistro():  objetoCliente {

        try {

            var existe: Boolean = rs!!.next()

            if (!existe) {

                rs!!.last()
            }
            clienteLeido = crearRegistroArticuloVisualizag()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return clienteLeido
    }

    fun fleerAnteriorRegistro():  objetoCliente {


        try {

            var existe: Boolean = rs!!.previous()

            if (!existe) {

                rs!!.first()
            }
            clienteLeido = crearRegistroArticuloVisualizag()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return clienteLeido
    }

    fun crearRegistroArticuloVisualizag():objetoCliente {

        var idLeido: Int = rs!!.getInt("id")
        var nombreLeido: String = rs!!.getString("nombre").toString()
        var direccionLeido: String = rs!!.getString(3)
        var poblacionLedio: String = rs!!.getString(4)
        var provinciaLeido: String = rs!!.getString(5)
        var codigoPostalLeido: String = rs!!.getString(6)
        var cifNifLeido: String = rs!!.getString(7)
        var telefono1Leido: String = rs!!.getString(8).toString()
        var telefono2Leido: String = rs!!.getString(9).toString()
        var email1Leido: String = rs!!.getString(10).toString()
        var webLeido: String = rs!!.getString(11).toString()
        var personaContactoLeido: String = rs!!.getString(12).toString()
        var sectorComercialLeido: String = rs!!.getString(13).toString()



        var clienteLeido= objetoCliente(idLeido, nombreLeido, direccionLeido, poblacionLedio
                ,provinciaLeido, codigoPostalLeido, cifNifLeido, telefono1Leido
                ,telefono2Leido,email1Leido,webLeido,personaContactoLeido,sectorComercialLeido)

        return clienteLeido


    }


    // Funciones para buscar registros en el cursor activo
    fun fleerSiguienteBuuscar(datoAbuscar: String, snc: String): objetoCliente {

        try {
            var en = false
            if (rs!!.isAfterLast()) {
                rs!!.last()
            }
            if (rs!!.isBeforeFirst()) {
                rs!!.first()
            }
            posicion = rs!!.getRow()
//javax.swing.JOptionPane.showMessageDialog(null,posicion)
            while (rs!!.next()) {
                if (rs!!.getObject(snc) != null) {

                    //javax.swing.JOptionPane.showMessageDialog(null,rs!!.getRow())
                    var cadena: String = rs!!.getObject(snc).toString().trim() + " "

                    var subcadena = datoAbuscar.trim().toString();


                    val enc:Int = cadena.indexOf(subcadena)

                    if (enc != -1) {
                        clienteLeido = crearRegistroArticuloVisualizag()

                        en = true
                        break
                    }
                }
            }
            if (en == false) {

                rs!!.absolute(posicion)
                //   return articuloLeido
            }
        } catch (ex: Exception) {
        }
        return clienteLeido
    }

    fun fleerAnteriorBuuscar(datoAbuscar: String, snc: String): objetoCliente {

        try {
            var en = false
            if (rs!!.isAfterLast()) {
                rs!!.last()
            }
            if (rs!!.isBeforeFirst()) {
                rs!!.first()
            }
            posicion = rs!!.getRow()

            while (rs!!.previous()) {
                if (rs!!.getObject(snc) != null) {
                    var cadena: String = rs!!.getObject(snc).toString().trim() + " "

                    var subcadena = datoAbuscar.trim().toString();


                    val enc = cadena.indexOf(subcadena)

                    if (enc != -1) {
                        clienteLeido = crearRegistroArticuloVisualizag()

                        en = true
                        break
                    }
                }
            }
            if (en == false) {

                rs!!.absolute(posicion)
                //   return articuloLeido
            }
        } catch (ex: Exception) {
        }
        return clienteLeido
    }



    fun leerCamposTabla(): ResultSetMetaData {

        lc = rs!!.getMetaData()

        return lc!!
    }


    // Leer el Scandallo, materias primas, de un Producto fabricado.
// SELECT * FROM  ArticulosFabricados
//  INNER JOIN EscandallosArticulosFabricados ON EscandallosArticulosFabricados.id_ArticuloFabricado = ArticulosFabricados.id_ArticuloFabricado
// INNER JOIN "MateriaPrimas" ON MateriaPrimas.id_MateriaPrima = EscandallosArticulosFabricados.id_MateriaPrima
// where ArticulosFabricados.id_ArticuloFabricado =5013
fun escandalloProductoFabricado(  codigoArticulo:Int):ArrayList<String>
    {
        var SQL:String="SELECT MateriaPrimas.* , EscandallosArticulosFabricados.*\n" +
                "       FROM  ArticulosFabricados as AF\n" +
                "       INNER JOIN EscandallosArticulosFabricados ON\n" +
                "          EscandallosArticulosFabricados.id_ArticuloFabricado = AF.id_ArticuloFabricado\n" +
                "          INNER JOIN  MateriaPrimas  ON\n" +
                "         MateriaPrimas.id_MateriaPrima = EscandallosArticulosFabricados.id_MateriaPrima\n" +
                "         where AF.id_ArticuloFabricado = "+codigoArticulo

        val data = ArrayList<String>()
        try {

             stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);



            var  rssss:ResultSet = stmt!!.executeQuery(SQL)
            // Iterate through the data in the result set and display it.


            data.add("Materia Prima - Cantidad")
            try{
            while (rssss!!.next() ) {
           //     println("Dato " + rs!!.getString(1) + " " + rssss!!.getString(2))

                data.add( rssss!!.getString("Descripcion")+" - "+ rssss!!.getInt("cantidad"));//+" - "+rssss!!.getInt(1))
            }
            }catch(e:Exception){}

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return data
    }
}