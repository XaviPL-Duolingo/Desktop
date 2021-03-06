package com.duolingo.app.desktop.controllers.typeExercices;

import com.duolingo.app.interfaces.impl.ExerciceImpl;
import com.duolingo.app.model.Exercice;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class TranslateSortWindowController implements Initializable {

    private ExerciceImpl exerciceManager = new ExerciceImpl();
    private int idLevel;
    private boolean isListen;

    @FXML    private TextField txtQuestion, txtAnswer;
    @FXML    private JFXToggleButton btnIsHard;
    @FXML    private Button btnCreateExercice;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void checkContent(){

        String phrToTranslate = txtQuestion.getText();
        String phrTranslated = txtAnswer.getText();

        if (!phrToTranslate.isEmpty()){
            if (!phrTranslated.isEmpty()){
                createExercice(phrToTranslate, phrTranslated);
            }else {
                System.out.println("[DEBUG] - Falta introducir la frase traducida...");
            }
        }else {
            System.out.println("[DEBUG] - Falta introducir la frase a traducir...");
        }

    }

    private void createExercice(String phrToTranslate, String phrTranslated){

        String[] contentExercice = new String[]{phrToTranslate, phrTranslated};
        boolean isHard = btnIsHard.isSelected();
        try {
            exerciceManager.insertTranslateExercice(idLevel, contentExercice, isHard, isListen);
            System.out.println("[DEBUG] - Ejercicio TRANSLATE SORT creado correctamente!");
            clear();
        }catch (Exception e){
            System.out.println("[DEBUG] - Error al crear ejercicio TRANSLATE SORT");
        }

    }

    private void clear(){

        txtAnswer.clear();
        txtQuestion.clear();
        btnIsHard.setSelected(false);

    }

    public void setIdLevel(int idLevel){
        this.idLevel = idLevel;
    }

    public void showData(int idExercice){

        Exercice exerciceObj = exerciceManager.getExerciceByID(idExercice);
        JSONObject objectJSON = new JSONObject(exerciceObj.getContentExercice());

        txtQuestion.setText((String) objectJSON.get("phrToTranslate"));
        txtAnswer.setText((String) objectJSON.get("answer1"));
        btnIsHard.setSelected(exerciceObj.isHard());

        txtQuestion.setDisable(true);
        txtAnswer.setDisable(true);
        btnIsHard.setDisable(true);
        btnCreateExercice.setDisable(true);

    }

    public void setListen(boolean listen) {
        isListen = listen;
    }
}