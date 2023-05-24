/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.ModeloPersona;
import model.Persona;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import view.ViewPersonas;

public class ControllerPersona {

    private ModeloPersona modelo;
    private ViewPersonas vista;
    private JFileChooser jfc;
    private int i;
    private String id_persona = "", criterio = "";

    public ControllerPersona(ModeloPersona modelo, ViewPersonas vista) {
        this.modelo = modelo;
        this.vista = vista;
        vista.setVisible(true);
        vista.getLblAlerta1().setVisible(false);
        llenarTabla();
    }

    public void iniciaControl() {
        //botones pantalla principal
        vista.getBtnBuscar().addActionListener(l -> buscar());
        vista.getBtnCrear().addActionListener(l -> abrirDialogo(1));
        vista.getBtnEditar().addActionListener(l -> abrirDialogo(2));
        //botones pantalla secundaria
        vista.getBtnAceptar().addActionListener(l -> crearEditarPersona());
        vista.getBtnCancelar().addActionListener(l -> cancelar());
        //abrir examinar java
        vista.getBtnExaminar().addActionListener(l -> examinarFoto());
        //recuperar datos de la tabla para moificar
        vista.getTblPersonas().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                verIdDatos(evt);
            }
        });
        vista.getBtnEliminar().addActionListener(l -> eliminar());

        //busqueda invremental
        vista.getTxtBuscar().addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {

                buscar();
            }
        });

    }

    private void examinarFoto() {

        FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg, png & jpeg", "jpeg", "png", "jpg");

        jfc = new JFileChooser();
        jfc.setFileFilter(filter);
        int estado = jfc.showOpenDialog(vista);

        if (estado == JFileChooser.APPROVE_OPTION) {

            try {
                Image imagen = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(
                        vista.getLbl_foto().getWidth(),
                        vista.getLbl_foto().getHeight(),
                        Image.SCALE_DEFAULT);

                Icon icono = new ImageIcon(imagen);
                vista.getLbl_foto().setIcon(icono);
                vista.getLbl_foto().updateUI();
                vista.getDlgPersona().setVisible(true);

            } catch (IOException ex) {
                Logger.getLogger(ControllerPersona.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

    private void abrirDialogo(int op) {
        String titulo;
        if (op == 1) {
            titulo = "Crear Persona";
            vista.getDlgPersona().setName("C");
            vista.getTxtDni().setEnabled(true);
            activarJdialog(titulo);
        } else {

            if (id_persona.equals("")) {
                JOptionPane.showMessageDialog(vista, "Seleccione una persona");
            } else {
                titulo = "Editar Persona";
                vista.getDlgPersona().setName("E");
                vista.getTxtDni().setEnabled(false);
                activarJdialog(titulo);
                cargarDatos();
                id_persona = "";
            }
        }

    }

    private void eliminar() {

        if (id_persona.equals("")) {
            JOptionPane.showMessageDialog(vista, "Selecciona una persona");

        } else {
            int respuesta = 0;

            respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro?", "Eliminar!", JOptionPane.YES_NO_OPTION);
            if (respuesta == 0) {

                ModeloPersona persona = new ModeloPersona(id_persona, null, null, null, null, null, 0, 0,null);

                if (persona.deletePersona()) {

                    JOptionPane.showMessageDialog(vista, "Registro Eliminado");
                    id_persona = "";
                    llenarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "El registro no se elimino");
                    id_persona = "";
                }

            } else {

                JOptionPane.showMessageDialog(vista, "Cancelado");
                id_persona = "";
            }

        }

    }

    private void crearEditarPersona() {

        //registrar
        if (datosNoVacios()) {

            if (vista.getDlgPersona().getName().contentEquals("C")) {
                ModeloPersona persona = new ModeloPersona();
                persona = recuperarDatos(persona);

                if (persona.setPersonaFoto()) {
                    JOptionPane.showMessageDialog(null,
                            "Persona creada satisfactoriamente.");

                    limpiarDatos();
                    vista.getDlgPersona().dispose();
                    llenarTabla();

                } else {
                    JOptionPane.showMessageDialog(vista,
                            "No se pudo crear persona error id repetido");
                }
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Faltan datos");
        }

        if (vista.getDlgPersona().getName().contentEquals("E")) {

            if (datosNoVacios()) {

                ModeloPersona persona = new ModeloPersona();
                persona = recuperarDatos(persona);

                if (persona.updatePersonaFoto()) {
                    JOptionPane.showMessageDialog(null,
                            "Persona Modificada satisfactoriamente.");
                    limpiarDatos();
                    vista.getDlgPersona().dispose();

                    llenarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista,
                            "No se pudo Modificar persona error base");
                }

            } else {
                JOptionPane.showMessageDialog(vista, "No se puede editar faltan datos");
            }
        }

    }

    private boolean datosNoVacios() {

        return !vista.getTxtDni().getText().equals("") && !vista.getTxtNombre().getText().equals("") && !vista.getTxtApellido().getText().equals("")
                && !vista.getJdcFechaNac().toString().equals("") && !vista.getTxtTelefono().getText().equals("") && !vista.getTxtSexo().getText().equals("") && !vista.getTxtSueldo().getText().equals("") && !vista.getTxtCupo().getText().equals("")&& !vista.getTxtCorreo().getText().equals("");
    }

    //Carga los datos en la pantalla editar
    private void cargarDatos() {
        ModeloPersona persona = new ModeloPersona();
        persona = persona.getPersonaEditar(id_persona);

        vista.getTxtDni().setText(persona.getIdPersona());
        vista.getTxtNombre().setText(persona.getNombre());
        vista.getTxtApellido().setText(persona.getApellido());
        vista.getJdcFechaNac().setDate(persona.getFechanacimiento());
        vista.getTxtTelefono().setText(persona.getTelefono());
        vista.getTxtSexo().setText(persona.getSexo());
        vista.getTxtSueldo().setText(persona.getSueldo() + "");
        vista.getTxtCupo().setText(persona.getCupo() + "");
        Image foto = persona.getFoto();
        vista.getTxtCorreo().setText(persona.getCorreo());
        if (foto != null) {
            foto = foto.getScaledInstance(90, 120, Image.SCALE_SMOOTH);
            ImageIcon icono = new ImageIcon(foto);
            vista.getLbl_foto().setIcon(icono);

        }else{
            vista.getLbl_foto().setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/llama.jpeg")));
            
        }
    }

    //Recupera los datos para ser modificados o creados
    private ModeloPersona recuperarDatos(ModeloPersona persona) {

        //INSERT
        String identificacion = vista.getTxtDni().getText();
        String nombres = vista.getTxtNombre().getText();
        String apellidos = vista.getTxtApellido().getText();
        Date fechaNac = vista.getJdcFechaNac().getDate();
        String telefono = vista.getTxtTelefono().getText();
        String sexo = vista.getTxtSexo().getText();
        int sueldo = Integer.parseInt(vista.getTxtSueldo().getText());
        int cupo = Integer.parseInt(vista.getTxtCupo().getText());
        String correo = vista.getTxtCorreo().getText();
        try {
            FileInputStream img
                    = new FileInputStream(jfc.getSelectedFile());
            int largo = (int) jfc.getSelectedFile().length();
            persona.setImageFile(img);
            persona.setLength(largo);

        } catch (IOException ex) {
            Logger.getLogger(ControllerPersona.class.getName()).log(Level.SEVERE, null, ex);
        }

        persona.setIdPersona(identificacion);
        persona.setNombre(nombres);
        persona.setApellido(apellidos);
        persona.setFechanacimiento(fechaNac);
        persona.setTelefono(telefono);
        persona.setSexo(sexo);
        persona.setSueldo(sueldo);
        persona.setCupo(cupo);
        persona.setCorreo(correo);
        return persona;

    }

    private void buscar() {
        criterio = vista.getTxtBuscar().getText().trim();

        if (!criterio.equals("")) {

            llenarTablaBusqueda();

        } else {
            vista.getLblAlerta1().setVisible(false);
            llenarTabla();
        }

    }

    private void llenarTablaBusqueda() {
        DefaultTableModel estructuraTabla;
        estructuraTabla = (DefaultTableModel) vista.getTblPersonas().getModel();
        estructuraTabla.setNumRows(0);
        List<Persona> listap = modelo.getPersonasBuscar(criterio);
        i = 0;

        if (!listap.isEmpty()) {

            vista.getLblAlerta1().setVisible(false);

            listap.stream().forEach(persona -> {
                estructuraTabla.addRow(new Object[3]);
                vista.getTblPersonas()
                        .setValueAt(persona.getIdPersona(),
                                i, 0);
                vista.getTblPersonas()
                        .setValueAt(persona.getNombre(),
                                i, 1);
                vista.getTblPersonas()
                        .setValueAt(persona.getApellido(),
                                i, 2);
                vista.getTblPersonas()
                        .setValueAt(persona.getFechanacimiento(),
                                i, 3);
                vista.getTblPersonas()
                        .setValueAt(persona.getTelefono(),
                                i, 4);
                vista.getTblPersonas()
                        .setValueAt(persona.getSexo(),
                                i, 5);
                vista.getTblPersonas()
                        .setValueAt(persona.getSueldo(),
                                i, 6);
                vista.getTblPersonas()
                        .setValueAt(persona.getCupo(),
                                i, 7);
               vista.getTblPersonas()
                    .setValueAt(persona.getCorreo(),
                            i, 9);
                //Llenar imagen
                Image foto = persona.getFoto();
                if (foto != null) {
                    foto = foto.getScaledInstance(50, 75, Image.SCALE_SMOOTH);
                    ImageIcon icono = new ImageIcon(foto);
                    DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
                    dtcr.setIcon(icono);
                    vista.getTblPersonas().setValueAt(new JLabel(icono), i, 8);

                } else {
                    vista.getTblPersonas().setValueAt(null, i, 8);
                }

                i = i + 1;
                 
            });
        } else {
            vista.getLblAlerta1().setVisible(true);
        }

    }

    private void llenarTabla() {
        vista.getTblPersonas().setDefaultRenderer(Object.class, new ImagenTabla());
        vista.getTblPersonas().setRowHeight(50);
        DefaultTableModel estructuraTabla;
        estructuraTabla = (DefaultTableModel) vista.getTblPersonas().getModel();
        estructuraTabla.setNumRows(0);
        List<Persona> listap = modelo.getPersonas();
        i = 0;
        listap.stream().forEach(persona -> {
            estructuraTabla.addRow(new Object[3]);
            vista.getTblPersonas()
                    .setValueAt(persona.getIdPersona(),
                            i, 0);
            vista.getTblPersonas()
                    .setValueAt(persona.getNombre(),
                            i, 1);
            vista.getTblPersonas()
                    .setValueAt(persona.getApellido(),
                            i, 2);
            vista.getTblPersonas()
                    .setValueAt(persona.getFechanacimiento(),
                            i, 3);
            vista.getTblPersonas()
                    .setValueAt(persona.getTelefono(),
                            i, 4);
            vista.getTblPersonas()
                    .setValueAt(persona.getSexo(),
                            i, 5);
            vista.getTblPersonas()
                    .setValueAt(persona.getSueldo(),
                            i, 6);
            vista.getTblPersonas()
                    .setValueAt(persona.getCupo(),
                            i, 7);
            
            vista.getTblPersonas()
                    .setValueAt(persona.getCorreo(),
                            i, 9);
            //Llenar imagen
            Image foto = persona.getFoto();
            if (foto != null) {
                foto = foto.getScaledInstance(50, 75, Image.SCALE_SMOOTH);
                ImageIcon icono = new ImageIcon(foto);
                DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
                dtcr.setIcon(icono);
                vista.getTblPersonas().setValueAt(new JLabel(icono), i, 8);

            } else {
                vista.getTblPersonas().setValueAt(null, i, 8);
            }

            i = i + 1;
            
            

        });

    }

    private void verIdDatos(java.awt.event.MouseEvent evt) {
        id_persona = "";
        DefaultTableModel tm = (DefaultTableModel) vista.getTblPersonas().getModel();

        id_persona = String.valueOf(tm.getValueAt(vista.getTblPersonas().getSelectedRow(), 0));

    }

    private void cancelar() {

        vista.getDlgPersona().dispose();
        id_persona = "";
    }

    private void activarJdialog(String titulo) {
        vista.getDlgPersona().setTitle(titulo);
        vista.getDlgPersona().setSize(500, 500);
        vista.getDlgPersona().setLocationRelativeTo(vista);
        vista.getDlgPersona().setVisible(true);
    }

    private void limpiarDatos() {

        vista.getTxtDni().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtApellido().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtSexo().setText("");
        vista.getTxtSueldo().setText("");
        vista.getTxtCupo().setText("");
        vista.getTxtCorreo().setText("");

    }
}
