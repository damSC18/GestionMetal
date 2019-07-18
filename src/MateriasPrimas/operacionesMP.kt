package MateriasPrimas

import java.io.FileInputStream
import java.sql.*

import java.util.ArrayList


class operacionesMP {


    private var posicion: Int = 0
    private var lc: ResultSetMetaData? = null
    var articuloLeido =  objetoMateriasPrimas(0,"",0,0f,"")
    var stmt: Statement? = null
    var connection: Connection? = null
    var rs: ResultSet? = null

    //FUNCION PARA CONECTARSE A LA BASE DE DATOS
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


            val SQL = "SELECT * FROM MateriaPrimas"



            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
///////////////////////////////////////////////////////////////////


            // Iterate through the data in the result set and display it.


        } catch (e: Exception) {
            e.printStackTrace()


        }
    }
    //CON ESTA FUNCION OBTENEMOS UNA LISTA DE LAS MATERIAS PRIMAS
    fun listado(): MutableList<objetoMateriasPrimas> {

        var listaArticulos: MutableList<objetoMateriasPrimas> = mutableListOf<objetoMateriasPrimas>()
        val SQL = "SELECT * FROM MateriaPrimas"

        try {
            //ASIGNAMOS A NUESTRO OBJERTO STATEMENT DEFINIENDO EL TIPO DE RESULT SET QUE VA A TENER LOS DATOS OBTENIDOS DE NUESTRA CONSULTA
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
            //EJECUTAMOS LA QUERY Y CUARDAMOS EL RESULTADO EN rs QUE ES UN RESULT SET
            rs = stmt!!.executeQuery(SQL)

            val SQL = "SELECT * FROM MateriaPrimas"

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
            //ITERAMOS LOS DATOS DEL RESULT SET Y LES VAMOS DISPONIENDO
            while (rs!!.next()) {
                println("Dato " + rs!!.getString(1) + " " + rs!!.getString(2))
                //GUARDAMOS EN LA VARIABLE REGISTRO LEIDO UN OBJETO MATERIAS PRIMAS CON LOS DATOS QUE REGOGEMOS POR COLUMNAS DEL RESULT SET
                var registroLeiodo = objetoMateriasPrimas(rs!!.getInt(1), rs!!.getString(2), rs!!.getInt(3), rs!!.getFloat(4), rs!!.getString(5))
                //AÑADIMOS EL CONTENIDO DE REGISTRO LEIDO A LA MUTABLE LIST DE objetoMateriasPrimas
                listaArticulos.add(registroLeiodo)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        //DEVOLVEMOS LA LISTA DE ARTICULOS
        return listaArticulos
    }

    //FUNCION PARA ACTUALIZAR LOS DATOS Y PODER VER REFLEJADO EN EL MOMENTOLAS INSERCIONES MODIFICACIONES O BORRADOS QUE HAGAMOS
    fun UpdateData() {
        val SQL = "SELECT * FROM MateriaPrimas"

        try {
            //ASIGNAMOS A NUESTRO OBJERTO STATEMENT DEFINIENDO EL TIPO DE RESULT SET QUE VA A TENER LOS DATOS OBTENIDOS DE NUESTRA CONSULTA
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
            //EJECUTAMOS LA QUERY Y CUARDAMOS EL RESULTADO EN rs QUE ES UN RESULT SET
            rs = stmt!!.executeQuery(SQL)

            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt!!.executeQuery(SQL)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    //FUNCION PARA INSERTAR UNA NUEVA MATERIA PRIMA
    fun grabaRegistroMP(articulo:  objetoMateriasPrimas) {
    //INSERTAMOS LA COSULTA EN EL VALUE SQL EN ESTE CASO COMO ES PARA AÑADIR HACEMOS UN INSERT INTO CON LOS CAMPOS Y LUEGO LOS VALORES QUE SE RECOGEN DE LAS CAJAS DE TEXTO
    val SQL = " insert into MateriaPrimas (Descripcion, id_Familia, PrecioCoste, Imagen) values('" + articulo.descripcion + "'," + articulo.familia + "," + articulo.precioCoste + ",'" + articulo.imagen+ "')"
    println(SQL)

    try {
        stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
        //EJECUTAMOS LA QUERY Y EN ESTE CASO TENEMOS UN BOOLEANO CR QUE TOMARA EL VALOR TRUE O FALSE DEPENDIENDO DE SI LA CONSULTA HA SIDO EXITOSA
        var cr: Boolean = stmt!!.execute(SQL)

        //LLAMAMOS A UPDATEDATA() PARA REFRESCAR LOS CAMBIOS
        UpdateData()

    } catch (e: Exception) {
        e.printStackTrace()
    }
    }
    //FUNCION PARA MODIFICAR UNA  MATERIA PRIMA
    fun modificaRegistroMP(articulo:  objetoMateriasPrimas){
        //INSERTAMOS LA COSULTA EN EL VALUE SQL EN ESTE CASO COMO ES PARA MODIFICAR HACEMOS UN UPDATE CON LOS CAMPOS ASIGNANDOLES SU NUEVO VALOR, QUE SE RECOGE DE LAS CAJAS DE TEXTO
        val SQL = " update   MateriaPrimas set Descripcion='"+articulo.descripcion+"',id_Familia="+articulo.familia+",PrecioCoste="+articulo.precioCoste+",Imagen='"+articulo.imagen+"' where id_MateriaPrima="+articulo.id
        println(SQL)
        try {
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
            //EJECUTAMOS LA QUERY Y EN ESTE CASO TENEMOS UN BOOLEANO CR QUE TOMARA EL VALOR TRUE O FALSE DEPENDIENDO DE SI LA CONSULTA HA SIDO EXITOSA
            var cr: Boolean = stmt!!.execute(SQL)

            //LLAMAMOS A UPDATEDATA() PARA REFRESCAR LOS CAMBIOS
            UpdateData()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //FUNCION PARA BORRAR UNA  MATERIA PRIMA
    fun borraRegistroMP(idMateriasPrimas: Int) {
        //INSERTAMOS LA COSULTA EN EL VALUE SQL EN ESTE CASO COMO ES PARA BORRAR HACEMOS UN DELETE AL QUE PASAMOS LA CLAVE PRIMARIA DE LA TABLA
        val SQL = "delete  from MateriaPrimas  where id_MateriaPrima=" + idMateriasPrimas
        println(SQL)
        try {
            stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
            //EJECUTAMOS LA QUERY Y EN ESTE CASO TENEMOS UN BOOLEANO CR QUE TOMARA EL VALOR TRUE O FALSE DEPENDIENDO DE SI LA CONSULTA HA SIDO EXITOSA
            var cr: Boolean = stmt!!.execute(SQL)

            //LLAMAMOS A UPDATEDATA() PARA REFRESCAR LOS CAMBIOS
            UpdateData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        UpdateData()
    }
    //FUNCION PARA IR AL PRIMER REGISTRO DE MATERIAS PRIMAS EN LA BD
    fun PrimerRegistroMP():  objetoMateriasPrimas {
        try {
            //INDICAMOS QUE QUEREMOS IR AL PRIMER ELEMENTO DEL RESULTSET
            rs!!.first()
            //LEEOMS EL objetoMaterisPrimas EN ESTA POSICION Y LLAMAMOS A LA FUNCION VISUALIZARARTICULO PARA MOSTRARLO
            articuloLeido = VisualizarArticulo()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }

    //FUNCION PARA IR AL ULTIMO REGISTRO DE MATERIAS PRIMAS EN LA BD
    fun UltimoRegistroMP():  objetoMateriasPrimas {
        try {
            //INDICAMOS QUE QUEREMOS IR AL ULTIMO ELEMENTO DEL RESULTSET
            rs!!.last()
            articuloLeido = VisualizarArticulo()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }
    //FUNCION PARA  AVANZAR UN REGISTRO DE MATERIAS PRIMAS EN LA BD
    fun SiguienteRegistroMP():  objetoMateriasPrimas {
        try {
            //DECLARAMOS UNA VARIABLE BOOLEANA PARA COMPROBAR SI AL AVANZAR RECORRIENDO EL RESULT SET LLEGAMOS AL FINAL
            var existe: Boolean = rs!!.next()
            //SI ESTA VARIABLE ES FALSA NOS INDICARA QUE NO HAY MAS DATOS DESPUES Y NO NOS DEJARA AVANZAR MAS
            if (!existe) {

                rs!!.last()
            }
            articuloLeido = VisualizarArticulo()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }
//FUNCION PARA  RETROCEDER UN REGISTRO DE MATERIAS PRIMAS EN LA BD
    fun AnteriorRegistroMP():  objetoMateriasPrimas {
        try {
            //DECLARAMOS UNA VARIABLE BOOLEANA PARA COMPROBAR SI AL RETROCEDER RECORRIENDO EL RESULT SET LLEGAMOS AL INICIO
            var existe: Boolean = rs!!.previous()
            //SI ESTA VARIABLE ES FALSA NOS INDICARA QUE NO HAY MAS DATOS DESPUES Y NO NOS DEJARA RETROCEDER MAS
            if (!existe) {

                rs!!.first()
            }
            articuloLeido = VisualizarArticulo()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articuloLeido
    }
    //FUNCION PARA VISUALIZAR UN ARTICULO
    fun VisualizarArticulo():objetoMateriasPrimas {
        //GUARDAMOS EN VARIABLES LOS DATOS QUE RECOGEMOS DE LAS COLUMNAS DEL RESULT SET
        var idLeido: Int = rs!!.getInt(1)
        var descripcionLeido: String = rs!!.getString(2).toString()
        var familiaLeido: Int = rs!!.getInt(3)
        var precioCoste: Float = rs!!.getFloat(4)
        var ImagenLeido: String = rs!!.getString(5).toString()


        //INSERTAMOS EN ARTICULO LEIDO QUE ES UN OBJETO MATERIAS PRIMAS
        var articuloLeido= objetoMateriasPrimas(idLeido, descripcionLeido, familiaLeido, precioCoste, ImagenLeido)

        return articuloLeido

    }


    //FUNCION PARA BUSCAR REGISTROS EN EL CURSOR ACTIVO
    fun SiguienteBuscarMP(datoAbuscar: String, snc: String): objetoMateriasPrimas {

        try {
            //IF PARA NO PERMITIRNOS SOBREPASAR NI LA PRIMERA NI LA ULTIMA POSICION
            var en = false
            if (rs!!.isAfterLast()) {
                rs!!.last()
            }
            if (rs!!.isBeforeFirst()) {
                rs!!.first()
            }
            posicion = rs!!.getRow()

            while (rs!!.next()) {
                if (rs!!.getObject(snc) != null) {
                    var cadena: String = rs!!.getObject(snc).toString().trim() + " "
                    var subcadena = datoAbuscar.trim().toString();

                    val enc = cadena.indexOf(subcadena)
                    if (enc != -1) {
                        articuloLeido = VisualizarArticulo()

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
        return articuloLeido
    }

    fun AnteriorBuscar(datoAbuscar: String, snc: String): objetoMateriasPrimas {

        try {
            //IF PARA NO PERMITIRNOS SOBREPASAR NI LA PRIMERA NI LA ULTIMA POSICION
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
                        articuloLeido = VisualizarArticulo()

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
        return articuloLeido
    }


    //FUNCION PARA GUARDAR METADATA DEL RESULT SET EN UN RESULT SET METADATA
    fun leerCamposTabla(): ResultSetMetaData {
        lc = rs!!.getMetaData()
        return lc!!
    }

fun escandalloProductoFabricado(  codigoArticulo:Int):ArrayList<String>
    {
        //GUARDAMOS DENTRO DEL VALUE SQL LA CONSULTA QUE QUEREMOS REALIZAR, EN ESTE CASO UN SELECT
    var SQL:String="SELECT *\n" +
            "FROM  ArticulosFabricados\n" +
            "\n" +
            "\tINNER JOIN EscandallosArticulosFabricados ON \n" +
            "\t EscandallosArticulosFabricados.id_ArticuloFabricado = ArticulosFabricados.id_ArticuloFabricado\n" +
            "\t \tINNER JOIN \"MateriaPrimas\" ON \n" +
            "\t MateriaPrimas.id_MateriaPrima = EscandallosArticulosFabricados.id_MateriaPrima\n" +
            "where ArticulosFabricados.id_ArticuloFabricado =  "+codigoArticulo
     //DECLARAMOS UN ARRAY LIST
        val data = ArrayList<String>()
        try {
             stmt = connection!!.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            var  rs:ResultSet = stmt!!.executeQuery(SQL)
            //ITERAMOS LOS DATOS DEL RESULT SET AÑADIMOS MATERIA PRIMA Y CANTIDAD E INSERTAMOS EN DATA EL ARRAY LIST DECLARADO ANTERIORMENTE LOS DATOS DEL RS
            data.add("Materia Prima - Cantidad")
            try{
            while (rs!!.next() ) {
                data.add( rs!!.getString("Descripcion")+" - "+ rs!!.getInt("cantidad"))
            }
            }catch(e:Exception){}

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return data
    }
}