package com.duolingo.app.desktop.controllers.typeExercices;

import com.duolingo.app.interfaces.impl.ExerciceImpl;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.model.WordMatch;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONObject;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class WordMatchWindowController implements Initializable {

    private ExerciceImpl exerciceManager = new ExerciceImpl();
    private int idLevel;

    @FXML    private Button btnCreateExercice;
    @FXML    private Button btnAddAnswer;
    @FXML    private JFXToggleButton btnIsHard;
    @FXML    private Button btnRemoveAnswer;

    @FXML    private TableView<WordMatch> tableWords;
    @FXML    private TableColumn<String, String> columnWord;
    @FXML    private TableColumn<String, String> columnMatch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        columnWord.setCellValueFactory(new PropertyValueFactory<>("word"));
        columnMatch.setCellValueFactory(new PropertyValueFactory<>("match"));

    }

    @FXML
    public void addAnswer(ActionEvent event){

        try {
            String wordCol1 = JOptionPane.showInputDialog("Palabra en idioma a traducir:");
            String wordCol2 = JOptionPane.showInputDialog("Palabra traducida:");
            WordMatch wordMatchObj = new WordMatch(wordCol1, wordCol2);
            tableWords.getItems().add(wordMatchObj);
            System.out.println("[DEBUG] - Pareja añadida correctamente!");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    public void removeAnswer(ActionEvent event) {

        // removeAnswer()
        // Obtiene todos los items de la tabla y los seleccionados y si uno de los items seleccionados
        // esta en la lista de todos los items de la tabla, lo elimina.

        ObservableList<WordMatch> allMatches, selectedMatches;
        allMatches = tableWords.getItems();
        selectedMatches = tableWords.getSelectionModel().getSelectedItems();
        selectedMatches.forEach(allMatches::remove);

    }

    @FXML
    public void checkContent(ActionEvent actionEvent) {

        if (!tableWords.getItems().isEmpty()){
            createExercice();
        }else {
            System.out.println("[DEBUG] - No hay suficientes MATCH WORDS añadidas...");
        }

    }

    private void createExercice(){

        ObservableList<WordMatch> wordMatchesList = tableWords.getItems();
        WordMatch[] contentExercice = wordMatchesList.toArray(new WordMatch[0]);
        boolean isHard = btnIsHard.isSelected();
        try {
            exerciceManager.insertWordMatchExercice(idLevel, contentExercice, isHard);
            System.out.println("[DEBUG] - Ejercicio WORD MATCH creado correctamente!p");
            clear();
        }catch (Exception e){
            System.out.println("[DEBUG] - Error al crear ejercicio WORD MATCH");
        }

    }

    private void clear(){

        tableWords.getItems().clear();

    }

    public void setIdLevel(int idLevel) {
        this.idLevel = idLevel;
    }

    public void showData(int idExercice){

        Exercice exerciceObj = exerciceManager.getExerciceByID(idExercice);
        JSONObject objectJSON = new JSONObject(exerciceObj.getContentExercice());


        btnIsHard.setSelected(exerciceObj.isHard());

    }

}

