package controller;

import data.DbConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import model.Movie;
import model.User;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    @FXML
    ComboBox<Movie.Genre> genreComboBox = new ComboBox<>();
    @FXML
    TextField titleTxt, directorTxt, priceTxt, releaseYearTxt, quantityTxt;
    @FXML
    HBox hBox;

    //UserTableView ----------------
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

    //MovieTableView ----------------------
    @FXML
    TableView<Movie> movieTable;
    @FXML
    TableColumn<Movie, Integer> colMovieId;
    @FXML
    TableColumn<Movie, String> colMovieTitle;
    @FXML
    TableColumn<Movie, String> colMovieDirector;
    @FXML
    TableColumn<Movie, Double> colMoviePrice;
    @FXML
    TableColumn<Movie, String> colMovieGenre;
    @FXML
    TableColumn<Movie, String> colMovieYear;
    @FXML
    TableColumn<Movie, Integer> colMovieQuantity;
    // ------------------------------------

    private DbConnector dbConnector = new DbConnector();
    private ObservableList<User> observableListUser = FXCollections.observableArrayList();
    private ObservableList<Movie> observableListMovie = FXCollections.observableArrayList();


    public void addMoviePressed() {

        try {
            Movie movie = new Movie(dbConnector.tableSize("movie")+1,titleTxt.getText(), directorTxt.getText(), Double.parseDouble(priceTxt.getText()), genreComboBox.getValue(), releaseYearTxt.getText(),Integer.parseInt(quantityTxt.getText()));

            dbConnector.connect();
            dbConnector.addMovieToDB(movie);

        }catch (IllegalArgumentException e){
            System.out.println("Wrong datatype");
          
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("DataTypeMismatch");
            alert.setContentText("Please double-check the format");
            alert.showAndWait();
        } finally {
            dbConnector.disconnect();
        }
    }

    public void loadMovieTable(){
        dbConnector.connect();
        try {
            PreparedStatement ps = dbConnector.connection.prepareStatement("SELECT * FROM movie");
            dbConnector.resultSet = ps.executeQuery();
            observableListMovie.clear();
            while (dbConnector.resultSet.next()){

                Movie movie = new Movie(
                        dbConnector.resultSet.getInt("idMovie"),
                        dbConnector.resultSet.getString("title"),
                        dbConnector.resultSet.getString("director"),
                        dbConnector.resultSet.getDouble("price"),
                        Movie.getStringAsGenre(dbConnector.resultSet.getString("genre")),
                        dbConnector.resultSet.getString("releaseYear"),
                        dbConnector.resultSet.getInt("quantity")
                        );

                observableListMovie.add(movie);
            }
        }catch (Exception e){

        }finally {
            dbConnector.disconnect();
        }
        colMovieId.setCellValueFactory(new PropertyValueFactory<>("idMovie"));
        colMovieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colMovieDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        colMoviePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colMovieGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colMovieYear.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        colMovieQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        movieTable.setItems(observableListMovie);
    }

    public void viewListbtn() {

        dbConnector.connect();
        try {
            PreparedStatement ps = dbConnector.connection.prepareStatement("SELECT * FROM account");
            dbConnector.resultSet = ps.executeQuery();
            observableListUser.clear();
            while (dbConnector.resultSet.next()) {

                observableListUser.add(new User(dbConnector.resultSet.getString("email"),
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

        }finally {
            dbConnector.disconnect();
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

        accountTable.setItems(observableListUser);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genreComboBox.getItems().addAll(Movie.Genre.Action, Movie.Genre.Family, Movie.Genre.Adventure, Movie.Genre.Drama, Movie.Genre.Horror, Movie.Genre.Scifi);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        hBox.setLayoutX(bounds.getWidth());
        hBox.setLayoutY(bounds.getHeight());
    }
}
