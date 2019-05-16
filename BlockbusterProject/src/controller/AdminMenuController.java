package controller;

import data.DbConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import model.Logic;
import model.Movie;
import model.User;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    @FXML
    ComboBox<Movie.Genre> genreComboBox = new ComboBox<>(), editMovieGenreBox = new ComboBox<>();
    @FXML
    TextField titleTxt, directorTxt, priceTxt, releaseYearTxt, quantityTxt;
    @FXML
    TableView<User> accountTable;
    @FXML
    Button btnLogOut;
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

    //Edit movie feature
    @FXML
    TextField editMovieTxt, editMovieTitle, editMovieDirector, editMoviePrice, editMovieYear, editMovieQuantity;
    @FXML
    Button doneBtn;
    @FXML
    AnchorPane editPane;
    @FXML
    HBox hBox;

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
    private Logic logic = new Logic();
    private ObservableList<User> observableList = FXCollections.observableArrayList();
    private ObservableList<Movie> observableListMovie = FXCollections.observableArrayList();

    public void addMoviePressed() {
        if (titleTxt.getText().equals("") || (directorTxt.getText().equals("")) || priceTxt.getText().equals("") || releaseYearTxt.getText().equals("") || quantityTxt.getText().equals("")) {
            alert("Make sure to fill all fields before clicking add.", Alert.AlertType.WARNING);
        } else {
            try {
                Movie movie = new Movie(dbConnector.tableSize("movie") + 1, titleTxt.getText(), directorTxt.getText(), Double.parseDouble(priceTxt.getText()), genreComboBox.getValue(), releaseYearTxt.getText(), Integer.parseInt(quantityTxt.getText()));
                dbConnector.connect();
                dbConnector.addMovieToDB(movie);
                titleTxt.clear();
                directorTxt.clear();
                priceTxt.clear();
                releaseYearTxt.clear();
                quantityTxt.clear();
                genreComboBox.setValue(null);
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong data type");
                alert("Wrong data type entered. Please make sure to use dot's instead of comma's. Careful with special signs.", Alert.AlertType.WARNING);
            } finally {
                dbConnector.disconnect();
            }
        }
    }

    public void editMovie() {
        if (!editMovieTxt.getText().equals("")) {
            Movie movie;
            try {
                movie = dbConnector.findMovieById(Integer.parseInt(editMovieTxt.getText()));
                editMovieTitle.setText(movie.getTitle());
                editMovieDirector.setText(movie.getDirector());
                editMoviePrice.setText(String.valueOf(movie.getPrice()));
                editMovieGenreBox.setValue(movie.getGenre());
                editMovieYear.setText(movie.getReleaseYear());
                editMovieQuantity.setText(String.valueOf(movie.getQuantity()));
            } catch (Exception e) {
                System.out.println("Something went wrong when loading to textfields.");
                alert("Something went wrong..", Alert.AlertType.WARNING);
            }
            editPane.setVisible(true);
            editMovieTxt.setEditable(false);
        } else {
            alert("Enter ID of the movie you would like to edit.", Alert.AlertType.INFORMATION);
        }
    }

    public void doneEditing() {
        try {
            dbConnector.updateTableColumnById("movie", "title", "idMovie", Integer.parseInt(editMovieTxt.getText()), editMovieTitle.getText());
            dbConnector.updateTableColumnById("movie", "director", "idMovie", Integer.parseInt(editMovieTxt.getText()), editMovieDirector.getText());
            dbConnector.updateTableColumnById("movie", "price", "idMovie", Integer.parseInt(editMovieTxt.getText()), Double.parseDouble(editMoviePrice.getText()));
            dbConnector.updateTableColumnById("movie", "genre", "idMovie", Integer.parseInt(editMovieTxt.getText()), Movie.getGenreAsString(editMovieGenreBox.getValue()));
            dbConnector.updateTableColumnById("movie", "releaseYear", "idMovie", Integer.parseInt(editMovieTxt.getText()), editMovieYear.getText());
            dbConnector.updateTableColumnById("movie", "quantity", "idMovie", Integer.parseInt(editMovieTxt.getText()), Integer.parseInt(editMovieQuantity.getText()));
        } catch (Exception e) {
            System.out.println("Somwthing went wrong when trying to update database information.");
            alert("Something went wrong..", Alert.AlertType.WARNING);
        }
        editMovieTxt.setEditable(true);
        editPane.setVisible(false);
    }

    public void loadMovieTable() {
        dbConnector.connect();
        try {
            PreparedStatement ps = dbConnector.connection.prepareStatement("SELECT * FROM movie");
            dbConnector.resultSet = ps.executeQuery();
            observableListMovie.clear();
            while (dbConnector.resultSet.next()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

    public static void alert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setHeaderText("We got a message for you");
        alert.setTitle("ErrorBuster 9000");
        alert.showAndWait();
    }

    public void viewListbtn() {
        dbConnector.connect();
        try {
            PreparedStatement ps = dbConnector.connection.prepareStatement("SELECT * FROM account");
            dbConnector.resultSet = ps.executeQuery();
            observableList.clear();

            while (dbConnector.resultSet.next()) {
                observableList.add(new User(dbConnector.resultSet.getString("email"),
                        dbConnector.resultSet.getString("password"),
                        dbConnector.resultSet.getBoolean("admin"),
                        dbConnector.resultSet.getString("firstName"),
                        dbConnector.resultSet.getString("lastName"),
                        dbConnector.resultSet.getDouble("balance"),
                        dbConnector.resultSet.getString("address"),
                        dbConnector.resultSet.getString("phoneNr"),
                        dbConnector.resultSet.getInt("idUser")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
        accountTable.setItems(observableList);
    }

    public void btnPressedLogOut(ActionEvent event){
        String logInFXML = "/view/loginScreenRedux.fxml";
        logic.changeSceneHandler(event, logInFXML, false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genreComboBox.getItems().addAll(Movie.Genre.Action, Movie.Genre.Family, Movie.Genre.Adventure, Movie.Genre.Drama, Movie.Genre.Horror, Movie.Genre.Scifi);
        editMovieGenreBox.getItems().addAll(Movie.Genre.Action, Movie.Genre.Family, Movie.Genre.Adventure, Movie.Genre.Drama, Movie.Genre.Horror, Movie.Genre.Scifi);
        editPane.setVisible(false);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        hBox.setLayoutX(bounds.getWidth());
        hBox.setLayoutY(bounds.getHeight());
    }
}
