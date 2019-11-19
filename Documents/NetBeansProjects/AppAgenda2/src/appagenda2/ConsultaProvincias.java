/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appagenda2;

import entidades.Provincia;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author SAWER
 */
public class ConsultaProvincias extends Application {
    
    @Override
    public void start(Stage primaryStage) {
    EntityManagerFactory emf=
    Persistence.createEntityManagerFactory("AppAgenda2PU");
    EntityManager em=emf.createEntityManager();
    
    Query queryProvincias = em.createNamedQuery("Provincia.findAll");
    List<Provincia> listProvincias = queryProvincias.getResultList();
        for(Provincia provincia : listProvincias){
        System.out.println(provincia.getNombre());
    }
}
    public static void main(String[] args) {
        launch(args);
    }
    
}
