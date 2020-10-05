package sample;

import javafx.application.Application;


import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;



public class Main extends Application {

    private Stage currentStage;
    private Parent root;
    private static String data_path = System.getProperty("user.dir")+"/Data";
    private static File setup_settings = new File(data_path +"/Settings/Pos_sys_settings.dat");
    private String window_title = "Setup POS System";

    @Override
    public void start(Stage primaryStage) throws Exception{
        setCurrentStage(primaryStage);
        if(setup_settings.exists()){
            load_pos_system();
        }else{
            load_setup();
        }
        getCurrentStage().show();
    }

    public void load_pos_system() throws Exception{
        setRoot(FXMLLoader.load(getClass().getResource("Menu.fxml")));
        // reads the name from settings
        currentStage.setTitle(new BufferedReader(new FileReader(setup_settings)).readLine()+"'s POS System");
        currentStage.setX(0);
        currentStage.setY(0);
        currentStage.setFullScreen(true);
        currentStage.setResizable(true);
        currentStage.setMinHeight(600);
        currentStage.setMinWidth(800);
        currentStage.setScene(new Scene(getRoot(), 800, 800));
    }

    // this function loads the setup window and once its done it will open a POS System window
    public void load_setup() throws Exception{
        setRoot(FXMLLoader.load(getClass().getResource("pos_setup.fxml")));
        Parent root = getRoot();
        TextField textField = (TextField) root.getChildrenUnmodifiable().get(1);
        root.getChildrenUnmodifiable().get(3).setOnMouseClicked(mouseEvent -> {
                try {
                    if(setup_settings.createNewFile()){
                        FileWriter writer = new FileWriter(setup_settings);
                        writer.write(textField.getText());
                        writer.write("\n14");
                        writer.write("\nwhite");
                        writer.write("\nblack");
                        writer.close();
                        load_pos_system();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });

        root.getChildrenUnmodifiable().get(2).setOnMouseClicked(mouseEvent -> getCurrentStage().close());

        currentStage.setTitle(window_title);
        currentStage.setResizable(false);
        currentStage.setFullScreen(false);
        currentStage.setScene(new Scene(root, 300, 200));
    }

    public  Stage getCurrentStage() {
        return currentStage;
    }

    public  void setCurrentStage(Stage stage) {
        currentStage = stage;
    }

    public  Parent getRoot() {
        return root;
    }

    public  void setRoot(Parent r) {
        root = r;
    }

    public static File getSetup_settings() {
        return setup_settings;
    }

    public static String getData_path() {
        return data_path;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
