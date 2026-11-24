package es.fplumara.dam.ad.autoevaluacion;

import es.fplumara.dam.ad.autoevaluacion.modelo.Pelicula;
import es.fplumara.dam.ad.autoevaluacion.modelo.Persona;
import es.fplumara.dam.ad.autoevaluacion.modelo.Puesto;
import es.fplumara.dam.ad.autoevaluacion.util.Conexion;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Autoevaluación de los temas 1 y 2: el catálogo del cine.
 */
public class Principal {

    // Ficheros de datos, relativos a la carpeta del proyecto
    private static final String RUTA_PERSONAS = "datos/personas.json";
    private static final String RUTA_PELICULAS = "datos/peliculas.json";
    private static final String RUTA_PUESTOS = "datos/puestos.json";
    private static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) {
        // Comprobación inicial: borra estas líneas cuando empieces el apartado 4
        try (Connection conexion = Conexion.abrir()) {
            System.out.println("Conexión correcta con " + conexion.getCatalog());
            // Apartado 1: leer los tres ficheros JSON
            // Paso 2
            List<Pelicula> peliculas = leerPeliculas(RUTA_PELICULAS);
            System.out.println("Peliculas leidas: " + peliculas.size());
            for (Pelicula p : peliculas) {
                insertarPelicula(conexion,p.getId(),p.getTitulo(),p.getEstreno(),p.getPuntuacion(),p.getDescripcion());
            }

            List<Persona> personas = leerPersonas(RUTA_PERSONAS);
            System.out.println("Personas leidas: " + personas.size());
            for (Persona p : personas) {
                insertarPersona(conexion,p.getId(),p.getNombre());
            }

            List<Puesto> puestos = leerPuestos(RUTA_PUESTOS);
            System.out.println("Puestos leidos: " + puestos.size());
            for (Puesto p : puestos) {
                insertarPuesto(conexion, p.getIdPelicula(),p.getIdPersona(),p.getPuesto());
            }



            // Apartado 2: guardar los datos en MariaDB con JDBC



            // Apartado 3: pedir el id de una película y mostrar sus datos y sus personas

            // Apartado 4: hacer las conexiones con un pool
        } catch (IOException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }


    }

    public static int insertarPelicula(Connection conexion, int id, String titulo,
                                    String estreno, double puntuacion,String descripcion) throws SQLException {
        String sql = "INSERT INTO peliculas (id_pelicula, titulo, estreno,puntuacion,descripcion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.setString(2, titulo);
            sentencia.setString(3, estreno);
            sentencia.setDouble(4, puntuacion);
            sentencia.setString(5, descripcion);
            return sentencia.executeUpdate();
        }
    }

    public static int insertarPersona(Connection conexion, int id, String nombre) throws SQLException {
        String sql = "INSERT INTO personas (id_persona, nombre) VALUES (?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.setString(2, nombre);
            return sentencia.executeUpdate();
        }
    }


    public static int insertarPuesto(Connection conexion, int idPelicula,int idPersona, String puesto) throws SQLException {
        String sql = "INSERT INTO puestos (fk_pelicula,fk_persona, puesto) VALUES (?, ?,?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idPelicula);
            sentencia.setInt(2, idPersona);
            sentencia.setString(3,puesto);
            return sentencia.executeUpdate();
        }
    }

    public static List<Pelicula> leerPeliculas(String ruta) {
        return mapper.readValue(new File(ruta), new TypeReference<List<Pelicula>>() {
        });
    }
    // Paso 2
    public static List<Persona> leerPersonas(String ruta) {
        return mapper.readValue(new File(ruta), new TypeReference<List<Persona>>() {
        });
    }

    // Paso 2
    public static List<Puesto> leerPuestos(String ruta) {
        return mapper.readValue(new File(ruta), new TypeReference<List<Puesto>>() {
        });
    }




}
