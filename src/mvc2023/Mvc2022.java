/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc2023;

import controller.ControllerPersona;
import model.ModeloPersona;
import view.ViewPersonas;


public class Mvc2022 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Instanciamos Modelo y Vista
        
        ViewPersonas vista = new ViewPersonas();
        ModeloPersona modelo = new ModeloPersona();
        
        ControllerPersona controlador=
                new ControllerPersona(modelo, vista);
        controlador.iniciaControl();
    }
    
}





















