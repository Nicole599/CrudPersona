/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ModelPgConecction {
    
    String cadenaConexion = "jdbc:postgresql://localhost:5432/PrograV";
    String pgUsuario = "postgres";
    String pgPassword = "1234";
    Connection con;
    public ModelPgConecction() {
        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ModelPgConecction.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            con=DriverManager.getConnection(cadenaConexion,pgUsuario,pgPassword);
        } catch (SQLException ex) {
            Logger.getLogger(ModelPgConecction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet consulta(String sql){//SELECT
        
        try {
            Statement st=con.createStatement();
            return st.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(ModelPgConecction.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public boolean accion(String sql){
        
        
        boolean correcto;
        try {
        //INSERT-UPDATE-DELETE
        Statement st=con.createStatement();
         st.execute(sql);
         st.close();//Cierro conexion.
         correcto=true;
        } catch (SQLException ex) {
            Logger.getLogger(ModelPgConecction.class.getName()).log(Level.SEVERE, null, ex);
            correcto=false;
        }
        
        return correcto;
    }
    
}
