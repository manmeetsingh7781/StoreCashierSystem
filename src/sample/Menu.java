package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;



public class Menu {
    private String background_color, text_color;
    private int font_size;

    @FXML
    private ListView<String> items_display, transcition_list;
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPane, keyPadGrid;

    @FXML private TilePane left_itemBtns_pane;
    @FXML private TextField keyPadText;

    @FXML
    private VBox mainPanel;
    @FXML
    private TilePane items_panel;

    @FXML private Button cancel_trans, hold_trans, bkSpaceBtn;

    public void initialize() {
        load_settings(); // loads the variables
        ObservableList<Node> arr = gridPane.getChildrenUnmodifiable();
        arr.get(0).setStyle("-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        gridPane.setStyle("-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        mainPanel.setStyle("-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        borderPane.setStyle("-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);

        String[] labels = {"Items: ","Taxes: ", "Fee: ", "Total: "};
        int number_of_labels = 6;
        int start_labels = 2;
        // settings tile pane buttons
        for (Node n: left_itemBtns_pane.getChildren()){
            n.setStyle("-fx-font-size: " + font_size + ";-fx-cursor: hand;");
        }

        disable_trans_buttons();
        showTransLabel();
        bkSpaceBtn.setOnAction(actionEvent -> backSpaceText());
        // also style keyPad buttons here
        ObservableList<Node> key_pad_btns = keyPadGrid.getChildrenUnmodifiable();
        // i=2 where keypad btns starts from
        for (int i = 2; i < key_pad_btns.size(); i++) {
                Button each_btn = (Button) key_pad_btns.get(i);
                each_btn.setStyle("-fx-cursor:hand; -fx-font-size: " + font_size);
                // attaching keyPad events to the text field
                each_btn.setOnAction(actionEvent -> {
                    try {
                        keyPadText.setText(getFormatedValue(keyPadText.getText() + each_btn.getText()));
                    }catch (Exception err){
                        // catch errors if the data type in textField is NAN
                    }
                });
        }

        for (int i = start_labels; i < number_of_labels; i++) {
            Label label = (Label) arr.get(i);
            label.setText(labels[i-start_labels]);
            label.setStyle("-fx-padding: 0 0 0 20; -fx-font-size: " + font_size + ";-fx-background-color: " + background_color + "; -fx-text-fill: " + text_color);
        }

        // selects the first item which is 'All departments'
        items_display.getSelectionModel().selectFirst();
        load_item_panel(items_panel, Main.getData_path() + "/Settings/all_departments.dat");

        items_display.setOnMouseClicked(mouseEvent -> {
            int selected_item = items_display.getSelectionModel().getSelectedIndex();
            String filePath;
            switch (selected_item) {
                case 0:
                    filePath = Main.getData_path() + "/Settings/all_departments.dat";
                    break;
                case 1:
                    filePath = Main.getData_path() + "/Settings/in_stores.dat";
                    break;
                case 2:
                    filePath = Main.getData_path() + "/Settings/other_items.dat";
                    break;
                default:
                    filePath = null;
                    break;
            }

            if (filePath != null) {
                load_item_panel(items_panel, filePath);
            } else {
                // show error messesge
                System.out.println("File is NULL");
            }
        });

    }

    private void load_settings() {
        if (Main.getSetup_settings().exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(Main.getSetup_settings()));
                reader.readLine(); // just a window title that we don't need here
                font_size = Integer.parseInt(reader.readLine());
                background_color = reader.readLine();
                text_color = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void load_item_panel(TilePane pane, String file_path) {
        try {
            pane.getChildren().clear();

            BufferedReader reader = new BufferedReader(new FileReader(file_path));
            ArrayList<String> items = new ArrayList<>();
            while (reader.ready()) {
                items.add(reader.readLine());
            }

            for (String i : items) {
                Button each_btn = new Button();
                String[] dis = i.split("\t");

                if (dis.length == 1) {
                    each_btn = new Button(dis[0]);
                }

                if (dis.length == 2) {
                    each_btn = new Button(dis[0]);
                    each_btn.setAccessibleText(dis[1]);
                }

                Button finalEach_btn = each_btn;
                each_btn.setOnAction(actionEvent -> action(finalEach_btn));
                finalEach_btn.setWrapText(true);

                // settings width
                finalEach_btn.setPrefWidth(120);
                finalEach_btn.setMinWidth(120);
                finalEach_btn.setMaxWidth(120);

                // setting height
                finalEach_btn.setMinHeight(60);
                finalEach_btn.setPrefHeight(60);
                finalEach_btn.setMaxHeight(60);

                // settings style
                String style = "-fx-cursor: hand;";
                finalEach_btn.setStyle(style);
                pane.getChildren().add(finalEach_btn);
            }

        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    @FXML private void action(Button btn){
        if(transcition_list.getItems().size() < 1){
            gridPane.getChildren().remove(gridPane.getChildrenUnmodifiable().get(gridPane.getChildrenUnmodifiable().size()-1));
        }
        if(transcition_list.getChildrenUnmodifiable().size() >= 1){
            enable_trans_buttons();
        }else{
            showTransLabel();
            disable_trans_buttons();
        }
        System.out.println(font_size);
        transcition_list.setStyle("-fx-font-size: "+ font_size);
        transcition_list.getItems().add(btn.getText());

    }


    @FXML private void clearKeyPadText(){
       keyPadText.clear();
    }

    @FXML private void backSpaceText(){
       int keyPadVal = Integer.parseInt(keyPadText.getText().replace(".",""));
       keyPadVal/=10;
       keyPadText.setText(""+  getFormatedValue(String.valueOf(keyPadVal)));
    }

    private String getFormatedValue(String number){
            int original = Integer.parseInt(number.replace(".",""));
            String final_text;
            int cents, dollars;
            if(original < 100000) {
                cents = original % 100;
                dollars = original / 100;
                if(cents < 10) {
                    final_text = dollars + ".0" + cents;
                }else {
                    final_text = dollars + "." + cents;
                }
            }else{
                int leading_cents;
                cents =  original%100000;
                dollars =  original/100000;
                leading_cents = cents%100;
                if(leading_cents < 10) {
                    final_text = dollars + ","+cents/100+".0"+leading_cents;
                }else {
                    final_text = dollars + ","+cents/100+"."+leading_cents;
                }
            }
            return  final_text;

    }
    @FXML private void clearTrans(){
        transcition_list.getItems().clear();
        showTransLabel();
        disable_trans_buttons();
    }

    private void showTransLabel(){
        String previous = ((Label)gridPane.getChildrenUnmodifiable().get(gridPane.getChildrenUnmodifiable().size()-1)).getText();
        if(transcition_list.getItems().size() < 1 && !previous.equals(" ")){
            Node label = new Label("No Items");
            label.setStyle("-fx-font-size: 36;-fx-padding: 0 0 0 50;");
            gridPane.add(label, 0, 0);
        }
    }

    private void disable_trans_buttons(){
        cancel_trans.setDisable(true);
        hold_trans.setDisable(true);
    }

    private void enable_trans_buttons(){
        cancel_trans.setDisable(false);
        hold_trans.setDisable(false);
    }
}

