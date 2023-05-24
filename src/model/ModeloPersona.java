/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ModeloPersona extends Persona {

    ModelPgConecction mpgc = new ModelPgConecction();

    public ModeloPersona() {
    }

    public ModeloPersona(String idPersona, String nombre, String apellido, Date fechanacimiento, String telefono, String sexo, int sueldo, int cupo, String correo) {
        super(idPersona, nombre, apellido, fechanacimiento, telefono, sexo, sueldo, cupo, correo);
    }


    //MANEJAR DATOS DE LA BASE DE DATOS.
    public List<Persona> getPersonas() {

        List<Persona> listaPersonas = new ArrayList<Persona>();

        String sql = "select * from persona";
        ResultSet rs = mpgc.consulta(sql);
        byte[] bytea;
        try {
            while (rs.next()) {

                Persona persona = new Persona();
                persona.setIdPersona(rs.getString("idpersona"));
                persona.setNombre(rs.getString("nombres"));
                persona.setApellido(rs.getString("apellidos"));
                persona.setFechanacimiento(rs.getDate("fechanacimiento"));
                persona.setTelefono(rs.getString("telefono"));
                persona.setSexo(rs.getString("sexo"));
                persona.setSueldo(rs.getInt("sueldo"));
                persona.setCupo(rs.getInt("cupo"));
                //si tiene foto
                bytea=rs.getBytes("foto");
                 persona.setCorreo(rs.getString("correo"));
                
                try {
                    if(bytea!=null) persona.setFoto(getImage(bytea));
                } catch (IOException ex) {
                    Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                listaPersonas.add(persona);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();//cierro conexion BD
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaPersonas;
    }

    public List<Persona> getPersonasBuscar(String criterio) {

        List<Persona> listaPersonas = new ArrayList<Persona>();

        String sql = "select * from persona where idpersona like '%" + criterio + "%' OR nombres like '%" + criterio + "%' OR apellidos like'%" + criterio + "%'";
        ResultSet rs = mpgc.consulta(sql);
        byte[] bytea;
        try {
            while (rs.next()) {

                Persona persona = new Persona();
                persona.setIdPersona(rs.getString("idpersona"));
                persona.setNombre(rs.getString("nombres"));
                persona.setApellido(rs.getString("apellidos"));
                persona.setFechanacimiento(rs.getDate("fechanacimiento"));
                persona.setTelefono(rs.getString("telefono"));
                persona.setSexo(rs.getString("sexo"));
                persona.setSueldo(rs.getInt("sueldo"));
                persona.setCupo(rs.getInt("cupo"));
                bytea= rs.getBytes("foto");
                persona.setCorreo(rs.getString("correo"));
                //si tiene foto
                try {
                    if(bytea!=null) persona.setFoto(getImage(bytea));
                } catch (IOException ex) {
                    Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                listaPersonas.add(persona);
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();//cierro conexion BD
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaPersonas;
    }
    
    //insertar persona foto
    public boolean setPersonaFoto() {
        String sql = "INSERT INTO persona (idpersona,nombres,apellidos,fechanacimiento,telefono,sexo,sueldo,cupo,foto,correo) ";
        sql += "VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = mpgc.con.prepareStatement(sql);
            ps.setString(1, getIdPersona());
            ps.setString(2, getNombre());
            ps.setString(3, getApellido());
            ps.setDate(4,new java.sql.Date(((Date) getFechanacimiento()).getTime()));
            ps.setString(5, getTelefono());
            ps.setString(6, getSexo());
            ps.setInt(7, getSueldo());
            ps.setInt(8, getCupo());
            ps.setBinaryStream(9, getImageFile(), getLength());
            ps.setString(10, getCorreo());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    //consultar datos seleccionados de la tabla
    public ModeloPersona getPersonaEditar(String id) {

        String sql = "select * from persona where idpersona='" + id + "'";
        ResultSet rs = mpgc.consulta(sql);
        ModeloPersona persona = new ModeloPersona();
        byte[] bytea;
        try {
            while (rs.next()) {

                persona.setIdPersona(rs.getString("idpersona"));
                persona.setNombre(rs.getString("nombres"));
                persona.setApellido(rs.getString("apellidos"));
                persona.setFechanacimiento(rs.getDate("fechanacimiento"));
                persona.setTelefono(rs.getString("telefono"));
                persona.setSexo(rs.getString("sexo"));
                persona.setSueldo(rs.getInt("sueldo"));
                persona.setCupo(rs.getInt("cupo"));
                bytea= rs.getBytes("foto");
                persona.setCorreo(rs.getString("correo"));
                //si tiene foto
                try {
                    if(bytea!=null) persona.setFoto(getImage(bytea));
                } catch (IOException ex) {
                    Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();//cierro conexion BD
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
        return persona;
    }

  
    //modificar persona foto
    public boolean updatePersonaFoto() {
        String sql = "UPDATE persona SET nombres=?,apellidos=?,fechanacimiento=?,telefono=?,sexo=?,sueldo=?,cupo=?,foto=?,correo=? ";
        sql += "WHERE idpersona='"+getIdPersona()+"';";

        try {
            PreparedStatement ps = mpgc.con.prepareStatement(sql);
            ps.setString(1, getNombre());
            ps.setString(2, getApellido());
            ps.setDate(3,new java.sql.Date(((Date) getFechanacimiento()).getTime()));
            ps.setString(4, getTelefono());
            ps.setString(5, getSexo());
            ps.setInt(6, getSueldo());
            ps.setInt(7, getCupo());
            ps.setBinaryStream(8, getImageFile(), getLength());
            ps.setString(9, getCorreo());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloPersona.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
    

    public boolean deletePersona() {

        String sql = "DELETE FROM persona WHERE idpersona='" + getIdPersona() + "';";
        return mpgc.accion(sql);
    }

    private Image getImage(byte[] bytes) throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Iterator it = ImageIO.getImageReadersByFormatName("jpeg");
        ImageReader imageReader = (ImageReader) it.next();
        Object source = bais;
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        imageReader.setInput(iis, true);
        ImageReadParam param = imageReader.getDefaultReadParam();
        param.setSourceSubsampling(1, 1, 0, 0);

        return imageReader.read(0, param);

    }

}
