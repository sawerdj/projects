/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appagenda2;

import entidades.Persona;
import entidades.Provincia;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author SAWER
 */
public class PersonaDetalleViewController implements Initializable {

    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TextField textFieldTelefono;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private DatePicker datePickerFechaNacimiento;
    @FXML
    private RadioButton radioButtonSoltero;
    @FXML
    private RadioButton radioButtonCasado;
    @FXML
    private RadioButton radioButtonViudo;
    @FXML
    private TextField textFieldNumHijos;
    @FXML
    private CheckBox checkBoxJubilado;
    @FXML
    private ComboBox<Provincia> comboBoxProvincia;
    
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private TextField textFieldSalario;
    @FXML
    private Button buttonCancelar;
    private Pane rootAgendaView;
    @FXML
    private Button buttonGuardar;
    @FXML
    
    private Pane rootPersonaDetalleView;
    
    private TableView tableViewPrevio;
    private Persona persona;
    private EntityManager entityManager;
    private boolean nuevaPersona;
    
    public static final char CASADO='C';
    public static final char SOLTERO='S';
    public static final char VIUDO='V';
    
    public void mostrarDatos(){
        textFieldNombre.setText(persona.getNombre());
        textFieldApellidos.setText(persona.getApellidos());
        textFieldTelefono.setText(persona.getTelefono());
        textFieldEmail.setText(persona.getEmail());
        // Falta implementar el código para el resto de controles
        }
    
    public void setPersona(EntityManager entityManager, Persona persona, Boolean nuevaPersona){
        this.entityManager = entityManager;
        entityManager.getTransaction().begin();
        
            if (!nuevaPersona){
                this.persona=entityManager.find(Persona.class,persona.getId());
            }else {
                this.persona=persona;}
                this.nuevaPersona=nuevaPersona;
                
                if (persona.getNumHijos() != null){
                    textFieldNumHijos.setText(persona.getNumHijos().toString());
                }
                if (persona.getSalario() != null){
                    textFieldSalario.setText(persona.getSalario().toString());
            }
                if (persona.getJubilado() != null){
                    checkBoxJubilado.setSelected(persona.getJubilado());
            }
                if (persona.getEstadoCivil() != null){
                    
                switch(persona.getEstadoCivil()){
                case CASADO:
                    radioButtonCasado.setSelected(true);
                break;
                case SOLTERO:
                    radioButtonSoltero.setSelected(true);
                break;
                case VIUDO:
                    radioButtonViudo.setSelected(true);
                break;
                    }
                }
                if (persona.getFechaNacimiento() != null){
                    Date date=persona.getFechaNacimiento();
                    Instant instant=date.toInstant();
                    ZonedDateTime zdt=instant.atZone(ZoneId.systemDefault());
                    LocalDate localDate=zdt.toLocalDate();
                    datePickerFechaNacimiento.setValue(localDate);
                }
                
                Query queryProvinciaFindAll=
                entityManager.createNamedQuery("Provincia.findAll");
                List listProvincia = queryProvinciaFindAll.getResultList();
                comboBoxProvincia.setItems(FXCollections.observableList(listProvincia));
                
                if (persona.getProvincia() != null){
                    comboBoxProvincia.setValue(persona.getProvincia());
                    comboBoxProvincia.setCellFactory(
                            
                    (ListView<Provincia> l)-> new ListCell<Provincia>(){
                    protected void updateItem(Provincia provincia, Boolean empty){
                    super.updateItem(provincia, empty);
                    if (provincia == null || empty){
                    setText("");
                    } else {
                    setText(provincia.getCodigo()+"-"+provincia.getNombre());
                    }
                }
            });

        }
          comboBoxProvincia.setConverter(new StringConverter<Provincia>(){
            @Override
            public String toString(Provincia provincia){
                if (provincia == null){
                    return null;
            } else {
                    return provincia.getCodigo()+"-"+provincia.getNombre();
                }
            }
            @Override
            public Provincia fromString(String userId){
                return null;
                }
            });

    }
      
    public void setTableViewPrevio(TableView tableViewPrevio){
        this.tableViewPrevio=tableViewPrevio;
    }

    public void setRootAgendaView(Pane rootAgendaView){
        this.rootAgendaView = rootAgendaView;
    }   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {       
        StackPane rootMain = (StackPane)rootPersonaDetalleView.getScene().getRoot();
        rootMain.getChildren().remove(rootPersonaDetalleView);
        rootAgendaView.setVisible(true);
        
        persona.setNombre(textFieldNombre.getText());
        persona.setApellidos(textFieldApellidos.getText());
        persona.setTelefono(textFieldTelefono.getText());
        persona.setEmail(textFieldEmail.getText());
        
            if (nuevaPersona){
                entityManager.persist(persona);
            }else {
                entityManager.merge(persona);
            }
            
        entityManager.getTransaction().commit();
         
        int numFilaSeleccionada;
            if (nuevaPersona){
                tableViewPrevio.getItems().add(persona);
                numFilaSeleccionada = tableViewPrevio.getItems().size()- 1;
                tableViewPrevio.getSelectionModel().select(numFilaSeleccionada);
                tableViewPrevio.scrollTo(numFilaSeleccionada);
            }else {
                numFilaSeleccionada=
                tableViewPrevio.getSelectionModel().getSelectedIndex();
                tableViewPrevio.getItems().set(numFilaSeleccionada,persona);
            }
            TablePosition pos = new TablePosition(tableViewPrevio,
            numFilaSeleccionada,null);
            tableViewPrevio.getFocusModel().focus(pos);
            tableViewPrevio.requestFocus();
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        StackPane rootMain = (StackPane)rootPersonaDetalleView.getScene().getRoot();
        rootMain.getChildren().remove(rootPersonaDetalleView);
        rootAgendaView.setVisible(true);
        
        entityManager.getTransaction().rollback();
        int numFilaSeleccionada =
        tableViewPrevio.getSelectionModel().getSelectedIndex();
        TablePosition pos = new TablePosition(tableViewPrevio,
        numFilaSeleccionada,null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();
        
    }
    
}
