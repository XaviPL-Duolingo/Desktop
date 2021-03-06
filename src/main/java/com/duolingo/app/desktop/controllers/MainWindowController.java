package com.duolingo.app.desktop.controllers;

import com.duolingo.app.interfaces.impl.CategoryImpl;
import com.duolingo.app.interfaces.impl.CourseImpl;
import com.duolingo.app.interfaces.impl.LanguageImpl;
import com.duolingo.app.interfaces.impl.LevelImpl;
import com.duolingo.app.model.Category;
import com.duolingo.app.model.Course;
import com.duolingo.app.model.Language;
import com.duolingo.app.model.Level;
import com.duolingo.app.util.Server;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private static MainWindowController instance;
    private LanguageImpl languageManager = new LanguageImpl();
    private CourseImpl courseManager = new CourseImpl();
    private CategoryImpl categoryManager = new CategoryImpl();
    private LevelImpl levelManager = new LevelImpl();

    public static double x, y;

    @FXML    private Pane mainPane;
    @FXML    private JFXToggleButton btnServer;
    @FXML    private ListView<Course> listCourses;
    @FXML    private ListView<Category> listCategories;
    @FXML    private ListView<Level> listLevels;
    @FXML    private Button btnCreateCourse, btnCreateCategory, btnCreateLevel, btnCreateExercice, btnShowExercice;
    @FXML    private ComboBox<Language> cmbDestLanguage;
    @FXML    private ComboBox<Language> cmbOriginLanguage;
    @FXML    private Label lblOriginLang;

    public MainWindowController(){
        instance = this;
    }

    @FXML
    void windowDrag(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);

    }

    @FXML
    void windowPressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    @FXML
    void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // PREVIOS

        btnCreateCourse.setDisable(true);
        btnCreateCategory.setDisable(true);
        btnCreateLevel.setDisable(true);
        btnCreateExercice.setDisable(true);
        btnShowExercice.setDisable(true);

        // COMBOBOXES Y FILTRO DE CURSOS

        List<Language> languageList = languageManager.getAllLanguages();
        ObservableList<Language> languageMenu = FXCollections.observableArrayList();

        languageMenu.add(new Language(0, "[TODOS LOS IDIOMAS]", "ALL"));
        languageMenu.addAll(languageList);

        cmbOriginLanguage.setItems(languageMenu);
        cmbOriginLanguage.setValue(languageMenu.get(0));

        cmbDestLanguage.setItems(languageMenu);
        cmbDestLanguage.setValue(languageMenu.get(0));

    }

    @FXML
    void checkCourses(MouseEvent event){

        listCourses.getItems().clear();
        listCategories.getItems().clear();
        listLevels.getItems().clear();


        int idOriginLang = cmbOriginLanguage.getValue().getIdLanguage();
        int idDestLang = cmbDestLanguage.getValue().getIdLanguage();

        if (idOriginLang != idDestLang){
            applyFilter(idOriginLang, idDestLang);
        }else{
            if (idOriginLang == 0 && idDestLang == 0){
                applyFilter(idOriginLang, idDestLang);
            }else{
                System.out.println("Filtro no válido...");
            }
        }
    }

    private void applyFilter(int idOriginLang, int idDestLang){

        listCourses.getItems().clear();
        listCategories.getItems().clear();
        listLevels.getItems().clear();

        btnCreateCategory.setDisable(true);
        btnCreateLevel.setDisable(true);
        btnCreateExercice.setDisable(true);
        btnShowExercice.setDisable(true);

        List<Course> courseList = courseManager.getAllCoursesByID(idOriginLang, idDestLang);
        ObservableList<Course> courseMenu = FXCollections.observableArrayList();
        courseMenu.addAll(courseList);
        listCourses.setItems(courseMenu);

        if (courseList.size() != 0){
            btnCreateCourse.setDisable(true);
        }else {
            btnCreateCourse.setDisable(false);
            btnCreateCategory.setDisable(true);
            btnCreateLevel.setDisable(true);
        }
    }

    @FXML
    void createCourse(){

        int idOriginLang = cmbOriginLanguage.getValue().getIdLanguage();
        int idDestLang = cmbDestLanguage.getValue().getIdLanguage();
        courseManager.insertCourse(idOriginLang, idDestLang);
        applyFilter(idOriginLang, idDestLang);

    }

    @FXML
    void checkCategories(MouseEvent event){

        listCategories.getItems().clear();
        listLevels.getItems().clear();

        try {
            int idCourse = listCourses.getSelectionModel().getSelectedItem().getIdCourse();
            List<Category> categoryList = categoryManager.getAllCategoriesByID(idCourse);
            ObservableList<Category> categoryMenu = FXCollections.observableArrayList();
            categoryMenu.addAll(categoryList);
            listCategories.setItems(categoryMenu);
            btnCreateCategory.setDisable(false);
            btnCreateLevel.setDisable(true);
            btnCreateExercice.setDisable(true);
            btnShowExercice.setDisable(true);
        }catch (Exception e){
            System.out.println("[DEBUG] - No hay CURSO seleccionado...");
        }

    }
    @FXML
    void createCategory(MouseEvent event){

        int idCourse = listCourses.getSelectionModel().getSelectedItem().getIdCourse();
        String nameCourse = JOptionPane.showInputDialog("¿Nombre de la categoria?");
        categoryManager.insertCategory(idCourse, nameCourse);
        checkCategories(event);

    }

    @FXML
    void checkLevels(MouseEvent event){

        listLevels.getItems().clear();

        try {
            int idCategory = listCategories.getSelectionModel().getSelectedItem().getIdCategory();
            List<Level> levelList = levelManager.getAllLevelsByID(idCategory);
            ObservableList<Level> levelMenu = FXCollections.observableArrayList();
            levelMenu.addAll(levelList);
            listLevels.setItems(levelMenu);
            btnCreateLevel.setDisable(false);
            btnCreateExercice.setDisable(true);
            btnShowExercice.setDisable(true);
        }catch (Exception e){
            System.out.println("[DEBUG] - No hay CATEGORIA seleccionada...");
        }

    }

    @FXML
    void createLevel(MouseEvent event){

        int idCategory = listCategories.getSelectionModel().getSelectedItem().getIdCategory();
        String codeLevel = JOptionPane.showInputDialog("Identificador del nivel?");
        levelManager.insertLevel(idCategory, codeLevel);
        checkLevels(event);

    }

    @FXML
    void checkExercices(MouseEvent event){

        try{
            btnCreateExercice.setDisable(false);
            btnShowExercice.setDisable(false);
        }catch (Exception e){
            System.out.println("[DEBUG] - No hay LEVEL seleccionado...");
        }

    }

    @FXML
    void createExercice(MouseEvent event) {
        try {

            int idLevel = listLevels.getSelectionModel().getSelectedItem().getIdLevel();
            URL url = new File("src/main/java/com/duolingo/app/desktop/windows/addExerciceWindow.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            AddExerciceWindowController addExerciceWindowController = loader.<AddExerciceWindowController>getController();
            addExerciceWindowController.setIdLevel(idLevel);

            Scene scene = new Scene(root);
            url = new File("src/main/java/com/duolingo/app/desktop/res/addExerciceWindow.css").toURI().toURL();
            Stage stage = new Stage();
            stage.setX(event.getScreenX()-event.getSceneX());
            stage.setY(event.getScreenY()-event.getSceneY());
            stage.setTitle("Buholingo | LISTA EJERCICIOS");

            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(String.valueOf(url));

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showExercice(MouseEvent event){
        try {

            int idLevel = listLevels.getSelectionModel().getSelectedItem().getIdLevel();
            URL url = new File("src/main/java/com/duolingo/app/desktop/windows/viewExerciceWindow.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            ViewExerciceWindowController viewExerciceWindowController = loader.<ViewExerciceWindowController>getController();
            viewExerciceWindowController.listExercices(idLevel);

            Scene scene = new Scene(root);
            url = new File("src/main/java/com/duolingo/app/desktop/res/addExerciceWindow.css").toURI().toURL();
            Stage stage = new Stage();
            stage.setX(event.getScreenX()-event.getSceneX());
            stage.setY(event.getScreenY()-event.getSceneY());
            stage.setTitle("Buholingo | VER EJERCICIOS");

            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(String.valueOf(url));

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showItems(MouseEvent event){
        try {

            URL url = new File("src/main/java/com/duolingo/app/desktop/windows/itemsWindow.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            url = new File("src/main/java/com/duolingo/app/desktop/res/addExerciceWindow.css").toURI().toURL();
            Stage stage = new Stage();
            stage.setX(event.getScreenX()-event.getSceneX());
            stage.setY(event.getScreenY()-event.getSceneY());
            stage.setTitle("Buholingo | ITEMS Y STORE");

            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(String.valueOf(url));

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void serverStatus() throws IOException {
        if (btnServer.isSelected()){
            Thread threadServer = new Server(1);
            threadServer.start();
        }else {
            Thread theadServer = new Server(0);
            theadServer.start();
        }
    }

    public static String popUpWindow(String context, MouseEvent event){


        try {
            URL url = new File("src/main/java/com/duolingo/app/desktop/windows/popUpWindow.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            PopUpWindowController popUpWindowController = loader.<PopUpWindowController>getController();
            popUpWindowController.setContext(context);

            stage.setX(event.getScreenX()-event.getSceneX()+400);
            stage.setY(event.getScreenY()-event.getSceneY()+220);
            stage.setTitle("Buholingo | DIALOGO");
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
