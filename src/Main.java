/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author nikol
 */
public class Main extends Application {


    private List<Warehouse> warehouses;
    private BorderPane root = new BorderPane();
    private Scene scene;
    private TableView<Warehouse> tableView;
    private TextArea navigacijaArea;
    private VBox vBoxCenter = new VBox(10);
    private GridPane gp = new GridPane();
    private GridPane gpUpdate = new GridPane();
    private VBox vBoxRightMain = new VBox(25);
    private Button proveraButton = new Button("load data");
    private Button updateButton = new Button("update");
    private TextField pocetnaField = new TextField();
    private TextField quantity = new TextField();
    private ComboBox<Boolean> visited = new ComboBox<>();
    private Button obilazakButton = new Button("Find");
    private TextField ciljanaField = new TextField();
    private Button putButton = new Button("Help");

    @Override
    public void start(Stage primaryStage) throws SQLException {

        visited.getItems().addAll(true, false);
        TableColumn<Warehouse, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setMaxWidth(40);
        TableColumn<Warehouse, String> cityColumn = new TableColumn<>("City");
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        cityColumn.setMinWidth(100);
        TableColumn<Warehouse, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setMinWidth(170);
        TableColumn<Warehouse, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        addressColumn.setMaxWidth(60);
        TableColumn<Warehouse, Boolean> visitedColumn = new TableColumn<>("Visited");
        visitedColumn.setCellValueFactory(new PropertyValueFactory<>("visited"));

        tableView = new TableView<>();
        tableView.setMaxSize(450, 255);
        tableView.getColumns().addAll(idColumn, cityColumn, addressColumn, quantityColumn, visitedColumn);
        Connector.getInstance().getAllWarehouses();
        warehouses = Connector.getInstance().getWarehouses().stream().collect(Collectors.toList());
        tableView.setItems(Connector.getInstance().getWarehouses());
        navigacijaArea = new TextArea("Navigacija još uvek nije pokrenuta...");
        navigacijaArea.setEditable(false);
        navigacijaArea.setWrapText(true);
        navigacijaArea.setMaxSize(450, 100);
        vBoxCenter.getChildren().addAll(tableView, navigacijaArea);
        vBoxCenter.setAlignment(Pos.CENTER);
        vBoxCenter.setPadding(new Insets(5));
        gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(10));
        gpUpdate.setAlignment(Pos.CENTER);
        gpUpdate.setPadding(new Insets(10));
        vBoxRightMain.setAlignment(Pos.CENTER);
        vBoxRightMain.setPadding(new Insets(10));
        proveraButton.setMinWidth(140);
        obilazakButton.setMinWidth(55);
        putButton.setMinWidth(55);

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        proveraButton.setOnAction(e -> {
            Connector.getInstance().getAllWarehouses();
            tableView.setItems(Connector.getInstance().getWarehouses());
        });

        putButton.setOnAction(e ->{
            Solver solver = new Solver(warehouses);
            if (!pocetnaField.getText().isEmpty() && !ciljanaField.getText().isEmpty() && warehouses.size()>2) {
                int index1 = Integer.parseInt(pocetnaField.getText());
                int index2 = Integer.parseInt(ciljanaField.getText());
                Optimal op = solver.najkraciPut(index1, index2);;
                updatePoruku(op);
            }
        });

        obilazakButton.setOnAction(e -> {
            Solver solver = new Solver(warehouses);
            if (!pocetnaField.getText().isEmpty() && warehouses.size()>2) {
                int index1 = Integer.parseInt(pocetnaField.getText());
                Optimal op = solver.nadjiPutOd(index1);
                updatePoruku(op);
            }
        });



        updateButton.setOnAction(e -> {
            if (!pocetnaField.getText().isEmpty() && !quantity.getText().isEmpty() && visited.getValue() != null) {
                int index = Integer.parseInt(pocetnaField.getText());
                int kolicina = tableView.getItems().get(index-1).getQuantity()+Integer.parseInt(quantity.getText());
                updateWarehouse(index, kolicina);
            }

        });

        vBoxRightMain.getChildren().addAll(proveraButton, new Label("------------------------"), gpUpdate, new Label("------------------------"), gp);
        pocetnaField.setMaxSize(55, 20);
        ciljanaField.setMaxSize(55, 20);
        proveraButton.setMaxSize(70, 0);
        visited.setMaxSize(55, 20);
        gp.setVgap(5);
        gp.setHgap(20);
        gpUpdate.setVgap(5);
        gpUpdate.setHgap(20);
        gpUpdate.add(new Label("Quantity: "), 0, 0);
        quantity.setMaxSize(55, 20);
        gpUpdate.add(quantity, 1, 0);
        gpUpdate.add(new Label("Is visited: "), 0, 1);
        gpUpdate.add(visited, 1, 1);
        gpUpdate.add(updateButton, 1, 2);
        gp.add(new Label("Start: "), 0, 0);
        gp.add(pocetnaField, 1, 0);
        gp.add(obilazakButton, 0, 2);
        gp.add(new Label("Target: "), 0, 1);
        gp.add(ciljanaField, 1, 1);
        gp.add(putButton, 1, 2);
        pocetnaField.setEditable(false);
        ciljanaField.setEditable(false);
        vBoxRightMain.setAlignment(Pos.TOP_CENTER);
        root.setCenter(vBoxCenter);
        root.setRight(vBoxRightMain);
        ciljanaField.setAlignment(Pos.CENTER_RIGHT);
        pocetnaField.setAlignment(Pos.CENTER_RIGHT);

        tableView.setRowFactory(tv -> {
            TableRow<Warehouse> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Warehouse rowData = row.getItem();
                    pocetnaField.setText(rowData.getId() + "");
                } else if (event.getClickCount() == 3 && (!row.isEmpty())) {
                    Warehouse rowData = row.getItem();
                    ciljanaField.setText(rowData.getId() + "");
                }
            });
            return row;
        });

        scene = new Scene(root, 620, 375);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Help system");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public TextArea getNavigacijaArea() {
        return navigacijaArea;
    }

    void updateWarehouse(int id , int quantity) {

        for (Warehouse w : warehouses) {
            if (w.getId() == id) {
                w.setVisited(true);
            }
        }
        Connector.getInstance().updateQuantity(id, quantity);
        Connector.getInstance().getAllWarehouses();
        List<Warehouse> newWarehouses = Connector.getInstance().getWarehouses().stream().collect(Collectors.toList());
        tableView.getItems().clear();

        for (int i = 0; i < warehouses.size(); i++) {
            if (warehouses.get(i).isVisited()) {
                newWarehouses.get(i).setVisited(true);
            }
        }

        for (Warehouse w : newWarehouses) {
            System.out.println(w.isVisited());
        }
        ObservableList<Warehouse> temp = FXCollections.observableArrayList();
        temp.addAll(newWarehouses);

//        System.out.println(Arrays.toString(newWarehouses.toArray()));
        tableView.setItems(temp);
        tableView.refresh();
    }


    void updatePoruku(Optimal optimalniPut) {
        String msg = "";
        for (Warehouse w : optimalniPut.getPutanja()) {
            msg += w.getCity() + " - " +w.getId()+", ";
        }
        navigacijaArea.setText(msg);
    }




    /**
     * @param args the command line arguments
     */



    public static void main(String[] args) {
        launch(args);
    }

    private ArrayList<Warehouse> getWarehouse(List<Warehouse> warehouses) {
        ArrayList<Warehouse> temp = new ArrayList<>();

        for (Warehouse w : warehouses) {
            if (!w.isVisited()) {
                temp.add(w);
            }
        }
        return temp;
    }

}
