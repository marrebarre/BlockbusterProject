package scene.adminMenu;

import database.DbConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import model.*;
import org.joda.time.Days;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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
    TableColumn<User, String> colUserEmail;
    @FXML
    TableColumn<User, String> colUserPassword;
    @FXML
    TableColumn<User, Integer> colUserId;
    @FXML
    TableColumn<User, Double> colUserBalance;
    @FXML
    TableColumn<User, String> colUserFirstName;
    @FXML
    TableColumn<User, String> colUserLastName;
    @FXML
    TableColumn<User, String> colUserAddress;
    @FXML
    TableColumn<User, String> colUserPhone;
    @FXML
    TableColumn<User, Boolean> colUserIsAdmin;

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
    Button btnAddImage;
    @FXML
    Label verifyImageAdded;
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
    @FXML
    ComboBox<Boolean> isAdminCombo;
    @FXML
    TextField editEmail, editUserIdTxtField, editPassword, editBalance, editFirstName, editLastName, editAddress, editPhoneNr;
    @FXML
    Pane editUserPane;
    @FXML
    TextField searchRentalTxt;
    @FXML
    Label enterLbl;
    @FXML
    TilePane tilePaneRentals;
    @FXML
    ScrollPane scrollPaneRentals;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();
    private ObservableList<User> observableList = FXCollections.observableArrayList();
    private ObservableList<Movie> observableListMovie = FXCollections.observableArrayList();

    public void addMoviePressed() {
        if (titleTxt.getText().equals("") || (directorTxt.getText().equals("")) || priceTxt.getText().equals("") || releaseYearTxt.getText().equals("") || quantityTxt.getText().equals("")) {
            Logic.alert("Make sure to fill all fields before clicking add.", Alert.AlertType.WARNING);
        } else {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("images", "*.png", "*.jpg"));
                File selectedFile = fileChooser.showOpenDialog(null);
                String imagePath = "";
                if (selectedFile != null) {
                    Path movefrom = FileSystems.getDefault().getPath(selectedFile.getPath());
                    Path targetDir = FileSystems.getDefault().getPath("./src/image/" + selectedFile.getName());
                    try {
                        Files.move(movefrom, targetDir, StandardCopyOption.ATOMIC_MOVE);
                        imagePath = targetDir.getFileName().toString();
                        System.out.println(targetDir.getFileName().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("File chosen isn't valid");
                }
                Movie movie = new Movie(dbConnector.tableSizeMovie() + 1, titleTxt.getText(), directorTxt.getText(), Double.parseDouble(priceTxt.getText()), genreComboBox.getValue(), releaseYearTxt.getText(), Integer.parseInt(quantityTxt.getText()), "image/" + imagePath);
                System.out.println("TrackADMIN");
                dbConnector.connect();
                dbConnector.addMovieToDB(movie);
                titleTxt.clear();
                directorTxt.clear();
                priceTxt.clear();
                releaseYearTxt.clear();
                quantityTxt.clear();
                genreComboBox.setValue(null);
                verifyImageAdded.setText("Image successfully added!");
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong database type");
                Logic.alert("Wrong database type entered. Please make sure to use dot's instead of comma's. Careful with special signs.", Alert.AlertType.WARNING);
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
                Logic.alert("Something went wrong..", Alert.AlertType.WARNING);
            }
            editPane.setVisible(true);
            editMovieTxt.setEditable(false);
        } else {
            Logic.alert("Enter ID of the movie you would like to edit.", Alert.AlertType.INFORMATION);
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
            Logic.alert("Something went wrong..", Alert.AlertType.WARNING);
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
                        dbConnector.resultSet.getInt("quantity"),
                        dbConnector.resultSet.getString("imagePath")
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

    public void viewListBtn() {
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
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUserPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        colUserBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colUserFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colUserLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colUserAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colUserPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNr"));
        colUserIsAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        accountTable.setItems(observableList);
    }

    public void btnPressedLogOut(ActionEvent event) {
        String logInFXML = "/scene/loginScreen/loginScreenRedux.fxml";
        logic.changeSceneHandler(event, logInFXML, false);
    }

    public void editUserIdPressed() {

        if (!editUserIdTxtField.getText().equals("")) {
            User user;
            try {
                user = dbConnector.findUser(Integer.parseInt(editUserIdTxtField.getText()));
                editEmail.setText(user.getEmail());
                editPassword.setText(user.getPassword());
                editBalance.setText(String.valueOf(user.getBalance()));
                editFirstName.setText(user.getFirstName());
                editLastName.setText(user.getLastName());
                editAddress.setText(user.getAddress());
                editPhoneNr.setText(user.getPhoneNr());
                isAdminCombo.setValue(user.isAdmin());
            } catch (Exception e) {
                System.out.println("something went wrong...");
                e.printStackTrace();
            }
            editUserPane.setVisible(true);
            editUserIdTxtField.setEditable(false);
        } else {
            Logic.alert("You must choose an User ID", Alert.AlertType.WARNING);
        }
    }

    public void searchRentalsByEmail(){
        LocalDate todayDate = LocalDate.now();
        dbConnector.connect();
        enterLbl.setVisible(false);
        searchRentalTxt.setLayoutY(10);
        scrollPaneRentals.setLayoutY(89);
        tilePaneRentals.setVisible(true);
        tilePaneRentals.getChildren().clear();
        try{
            PreparedStatement ps = dbConnector.connection.prepareStatement("SELECT * FROM account_has_movie JOIN account ON account.idUser = account_has_movie.account_idUser JOIN movie ON movie.idMovie =account_has_movie.movie_idMovie WHERE email = ? AND returned = 0");
            ps.setString(1, searchRentalTxt.getText());
            dbConnector.resultSet = ps.executeQuery();
            while (dbConnector.resultSet.next()){
                Account_Has_Movie rental = new Account_Has_Movie(
                        dbConnector.resultSet.getInt("movie_idMovie"),
                        dbConnector.resultSet.getString("title"),
                        dbConnector.resultSet.getString("director"),
                        dbConnector.resultSet.getDouble("price"),
                        Movie.getStringAsGenre(dbConnector.resultSet.getString("genre")),
                        dbConnector.resultSet.getString("releaseYear"),
                        dbConnector.resultSet.getInt("quantity"),
                        dbConnector.resultSet.getString("imagePath"),
                        dbConnector.resultSet.getInt("rentalID"),
                        dbConnector.resultSet.getInt("account_idUser"),
                        dbConnector.resultSet.getInt("movie_idMovie"),
                        dbConnector.resultSet.getString("dateRented"),
                        dbConnector.resultSet.getString("estimatedDateOfReturned"),
                        dbConnector.resultSet.getDouble("fee"),
                        dbConnector.resultSet.getBoolean("returned"));
                rental.setTitle(dbConnector.resultSet.getString("title"));
                System.out.println(rental);
                Pane pane = new Pane();

                LocalDate returnDate = LocalDate.parse(rental.getEstimatedReturnDate());
                long daysBetween = ChronoUnit.DAYS.between(todayDate,returnDate);
                if (returnDate.isAfter(todayDate)){
                    rental.setFee(daysBetween);
                }

                Label label = new Label(rental.getTitle()+"\nRented: "+rental.getRented()+"\nReturn date: "+rental.getEstimatedReturnDate()+"\nFee: "+rental.getFee()+"$");
                label.setFont(new Font("System",22));
                label.setStyle("-fx-text-fill: #faab04;");
                pane.getChildren().add(label);
                Button button = new Button("Return");
                button.setLayoutY(125);
                button.setOnAction(new EventHandler<ActionEvent>(){

                    @Override
                    public void handle(ActionEvent event) {
                        dbConnector.connect();
                        try {
                            PreparedStatement preparedStatement = dbConnector.connection.prepareStatement("UPDATE account_has_movie SET returned = ? WHERE rentalId = ?");
                            preparedStatement.setInt(1,1);
                            preparedStatement.setInt(2, rental.getRentalID());
                            preparedStatement.executeUpdate();

                            preparedStatement = dbConnector.connection.prepareStatement("SELECT * FROM movie WHERE idMovie = ?");
                            preparedStatement.setInt(1, rental.getMovie_idMovie());
                            dbConnector.resultSet = preparedStatement.executeQuery();
                            int tmp=0;
                            while (dbConnector.resultSet.next()) {
                                tmp = dbConnector.resultSet.getInt("quantity");
                            }
                            preparedStatement = dbConnector.connection.prepareStatement("UPDATE movie SET quantity = ? WHERE idMovie = ?");
                            preparedStatement.setInt(1,tmp+1);
                            preparedStatement.setInt(2,rental.getMovie_idMovie());
                            preparedStatement.executeUpdate();

                            searchRentalsByEmail();
                        }catch (Exception e){
                            System.out.println("error in sql set returned 1");
                            e.printStackTrace();
                        }finally {
                            dbConnector.disconnect();
                        }
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Successfully returned "+rental.getTitle());
                        alert.setTitle("Bustblocker");
                        alert.setHeaderText("Success!");
                        alert.showAndWait();
                    }
                });
                pane.getChildren().add(button);
                pane.setStyle("-fx-background-color: black;");
                tilePaneRentals.getChildren().add(pane);
            }

        }catch (Exception e){

        }finally {
            dbConnector.disconnect();
        }
    }

    public void updateUserInfoPressed() {
        dbConnector.updateUserInfo("account", "email", "idUser", Integer.parseInt(editUserIdTxtField.getText()), editEmail.getText());
        dbConnector.updateUserInfo("account", "password", "idUser", Integer.parseInt(editUserIdTxtField.getText()), editPassword.getText());
        dbConnector.updateUserInfo("account", "balance", "idUser", Integer.parseInt(editUserIdTxtField.getText()), Double.parseDouble(editBalance.getText()));
        dbConnector.updateUserInfo("account", "firstName", "idUser", Integer.parseInt(editUserIdTxtField.getText()), editFirstName.getText());
        dbConnector.updateUserInfo("account", "lastName", "idUser", Integer.parseInt(editUserIdTxtField.getText()), editLastName.getText());
        dbConnector.updateUserInfo("account", "address", "idUser", Integer.parseInt(editUserIdTxtField.getText()), editAddress.getText());
        dbConnector.updateUserInfo("account", "phoneNr", "idUser", Integer.parseInt(editUserIdTxtField.getText()), editPhoneNr.getText());
        dbConnector.updateUserInfo("account", "admin", "idUser", Integer.parseInt(editUserIdTxtField.getText()), isAdminCombo.getValue());
    }

        @Override
        public void initialize (URL location, ResourceBundle resources){
            genreComboBox.getItems().addAll(Movie.Genre.Action, Movie.Genre.Family, Movie.Genre.Adventure, Movie.Genre.Drama, Movie.Genre.Horror, Movie.Genre.Scifi);
            editMovieGenreBox.getItems().addAll(Movie.Genre.Action, Movie.Genre.Family, Movie.Genre.Adventure, Movie.Genre.Drama, Movie.Genre.Horror, Movie.Genre.Scifi);
            isAdminCombo.getItems().addAll(true, false);
            editPane.setVisible(false);
            editUserPane.setVisible(false);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            tilePaneRentals.setVisible(false);
            tilePaneRentals.setTileAlignment(Pos.CENTER);
            tilePaneRentals.setHgap(5);
            tilePaneRentals.setVgap(5);
            scrollPaneRentals.setLayoutY(370);
        }
    }