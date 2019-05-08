package controller;

import data.DbConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Movie;
import model.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    @FXML
    ComboBox<Movie.Genre> genreComboBox = new ComboBox<>();

    @FXML
    TextField titleTxt, directorTxt, priceTxt;

    @FXML
    TableView<User> accountTable;


    @FXML
    TableColumn<User, String> emailcol;
    @FXML
    TableColumn<User, String> passwordcol;
    @FXML
    TableColumn<User, Integer> idusercol;
    @FXML
    TableColumn<User, Double> balancecol;
    @FXML
    TableColumn<User, String> firstnamecol;
    @FXML
    TableColumn<User, String> lastnamecol;
    @FXML
    TableColumn<User, String> addresscol;
    @FXML
    TableColumn<User, String> phonecol;
    @FXML
    TableColumn<User, Boolean> isadmincol;

    private DbConnector dbConnector = new DbConnector();


    public void addMoviePressed() {

        try {
            Movie movie = new Movie(titleTxt.getText(), directorTxt.getText(), Double.parseDouble(priceTxt.getText()), genreComboBox.getValue());

            dbConnector.connect();
            dbConnector.addMovieToDB(movie);
            dbConnector.disconnect();
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong datatype bruh");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("DataTypeMismatch");
            alert.setContentText("Please double-check the format");
            alert.showAndWait();
        }
    }

    ObservableList<User> observableList = FXCollections.observableArrayList();

    public void viewListbtn() {

        dbConnector.connect();
        try {
            PreparedStatement ps = dbConnector.connection.prepareStatement("SELECT * FROM account");
            dbConnector.resultSet = ps.executeQuery();

            while (dbConnector.resultSet.next()) {
                observableList.add(new User(dbConnector.resultSet.getString("email"),
                        dbConnector.resultSet.getString("password"),
                        dbConnector.resultSet.getInt("idUser"),
                        dbConnector.resultSet.getString("firstName"),
                        dbConnector.resultSet.getString("lastName"),
                        dbConnector.resultSet.getDouble("balance"),
                        dbConnector.resultSet.getString("address"),
                        dbConnector.resultSet.getString("phoneNr"),
                        dbConnector.resultSet.getBoolean("admin")));
            }
        } catch (Exception e) {

        }

        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordcol.setCellValueFactory(new PropertyValueFactory<>("password"));
        idusercol.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        balancecol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        firstnamecol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastnamecol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addresscol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phonecol.setCellValueFactory(new PropertyValueFactory<>("phoneNr"));
        isadmincol.setCellValueFactory(new PropertyValueFactory<>("admin"));

        accountTable.setItems(observableList);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genreComboBox.getItems().addAll(Movie.Genre.Action, Movie.Genre.Family, Movie.Genre.Adventure, Movie.Genre.Drama, Movie.Genre.Horror, Movie.Genre.Scifi);
    }
}
