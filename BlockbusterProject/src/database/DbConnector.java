package database;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import model.Admin;
import model.Movie;
import model.User;
import scene.rentPopup.RentPopupController;
import scene.userMenu.UserMenuController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static model.Logic.alert;
import static scene.userMenu.UserMenuController.loggedInUser;

public class DbConnector {
    public Connection connection = null;
    public UserMenuController userMenuController;
    public Statement statement;
    public ResultSet resultSet;
    public List<User> users = new ArrayList<>();
    public List<Admin> admins = new ArrayList<>();
    public List<Movie> movies = new ArrayList<>();

    public void connect() {
        try {
            String url = "jdbc:mysql://den1.mysql3.gear.host:3306/bustblockerdb?verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true&user=bustblockerdb&password=bustblocker!&serverTimeZone=UTF-8";
            connection = DriverManager.getConnection(url);
        } catch (SQLException sql) {
            System.err.println("Connection failed" + sql);
        }
    }

    public void addRental(Movie selectedMovie, Date dateRented, Date dateReturned) {
        String SQLQuery = "INSERT INTO `account_has_movie` (account_idUser, movie_idMovie, dateRented, estimatedDateOfReturned, fee, returned) VALUES (?,?,?,?,?,?)";
        connect();
        try {
            PreparedStatement ps = connection.prepareStatement(SQLQuery);
            ps.setInt(1, loggedInUser.getIdUser());
            ps.setInt(2, selectedMovie.getIdMovie());
            ps.setDate(3, dateRented);
            ps.setDate(4, dateReturned);
            ps.setDouble(5, selectedMovie.getPrice());
            ps.setBoolean(6, false);
            ps.executeUpdate();
            alert("Successfully rented movie!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            System.out.println("Error when loading to database");
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public int tableSizeMovie() {
        connect();
        String temp = null;
        try {
            System.out.println("tableSizeMovie tracker1");
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(idMovie) FROM movie");
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                temp = resultSet.getString("COUNT(idMovie)");
            }
        } catch (SQLException e) {
            System.out.println("Table Empty or does not exist.");
            e.printStackTrace();
        }
        System.out.println("tableSizeMovie tracker2");
        disconnect();
        return Integer.parseInt(temp);
    }

    public int tableSizeAccount() {
        String temp = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(idUser) FROM account");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                temp = resultSet.getString("COUNT(idUser)");
            }
        } catch (SQLException e) {
            System.out.println("Wrong insert");
            e.printStackTrace();
        }
        return Integer.parseInt(temp);
    }

    public Movie findMovieById(int id) {
        connect();
        Movie movie = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM movie WHERE idMovie = ?");
            ps.setInt(1, id);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                movie = new Movie(
                        resultSet.getInt("idMovie"),
                        resultSet.getString("title"),
                        resultSet.getString("director"),
                        resultSet.getDouble("price"),
                        Movie.getStringAsGenre(resultSet.getString("genre")),
                        resultSet.getString("releaseYear"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("imagePath")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return movie;
    }

    public void addMovieToDB(Movie movie) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `movie`(idMovie, title, director, price, genre, releaseYear, quantity, imagePath) VALUES (?,?,?,?,?,?,?,?)");
            ps.setInt(1, movie.getIdMovie());
            ps.setString(2, movie.getTitle());
            ps.setString(3, movie.getDirector());
            ps.setDouble(4, movie.getPrice());
            ps.setString(5, movie.getGenreAsString());
            ps.setString(6, movie.getReleaseYear());
            ps.setInt(7, movie.getQuantity());
            ps.setString(8, movie.getImagePath());

            ps.executeUpdate();
            alert("Successfully added movie!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            System.out.println("Error when loading to database");
        }
    }

    public void disconnect() {
        try {
            if (connection != null)
                connection.close();
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
        } catch (SQLException ex) {
            System.out.println("Failed to disconnect");
        }
    }

    public boolean verifyAccount(String username, String pw) {
        boolean admin;
        User tempUser;
        String query = "SELECT * FROM account WHERE email =? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, pw);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                admin = resultSet.getBoolean(9);
                if (admin) {
                    adminHandler(resultSet);
                } else {
                    tempUser = userHandler(resultSet);
                    loggedInUser = tempUser;
                }
                resultSet.close();
            }
        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public <T> void updateTableColumnById(String table, String column, String idNameInTable, int id, T newData) {
        connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE " + table + " SET " + column + " = ? WHERE " + idNameInTable + " = " + id);
            if (newData instanceof String) {
                String temp = (String) newData;
                ps.setString(1, temp);
            } else if (newData instanceof Integer) {
                int temp = (Integer) newData;
                ps.setInt(1, temp);
            } else if (newData instanceof Double) {
                Double temp = (Double) newData;
                ps.setDouble(1, temp);
            } else if (newData instanceof Boolean) {
                Boolean temp = (Boolean) newData;
                ps.setBoolean(1, temp);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Could not update " + column);
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private User userHandler(ResultSet resultSet) throws SQLException {
        User user = new User(
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getBoolean("admin"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getDouble("balance"),
                resultSet.getString("address"),
                resultSet.getString("phoneNr"),
                resultSet.getInt("idUser"));
        System.out.println("Namn: " + user.getFirstName() + " " + user.getLastName());
        return user;
    }

    private List<Admin> adminHandler(ResultSet resultSet) {
        try {
            Admin admin = new Admin(
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getBoolean("admin"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getDouble("balance"),
                    resultSet.getString("address"),
                    resultSet.getString("phoneNr"),
                    resultSet.getInt("idUser"));
            admins.add(admin);
            System.out.println("Email: " + admin.getEmail() + "Lösen: " + admin.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    public void addUserToDb(User user) {
        String query = "INSERT INTO `account` (idUser, email, password, balance, firstName, lastName, address, phoneNr, admin) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(1, user.getIdUser());
            preparedStatement.setDouble(4, user.getBalance());
            preparedStatement.setString(5, user.getFirstName());
            preparedStatement.setString(6, user.getLastName());
            preparedStatement.setString(7, user.getAddress());
            preparedStatement.setString(8, user.getPhoneNr());
            preparedStatement.setBoolean(9, user.isAdmin());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Something went wrong!");
            e.printStackTrace();
        }
    }

    public User findUser(int i) {
        connect();
        User user = null;
        String query = "SELECT * FROM account WHERE idUser = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, i);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                user = new User(
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("admin"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getDouble("balance"),
                        resultSet.getString("address"),
                        resultSet.getString("PhoneNr"),
                        resultSet.getInt("idUser")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return user;
    }

    public void loadRentals(TilePane tilePane){
        connect();
        String SQLQuery = "SELECT * from movie INNER JOIN account_has_movie ON movie.idMovie = account_has_movie.movie_idMovie WHERE account_has_movie.account_idUser = ?";
        ResultSet resultSetRental;
        try {
            PreparedStatement ps = connection.prepareStatement(SQLQuery);
            ps.setInt(1, loggedInUser.getIdUser());
            resultSetRental = ps.executeQuery();
            while (resultSetRental.next()) {
                String imagePath;
                Movie movie = new Movie(
                        resultSetRental.getInt("movie_idMovie"),
                        resultSetRental.getString("title"),
                        resultSetRental.getString("director"),
                        resultSetRental.getDouble("price"),
                        Movie.getStringAsGenre(resultSetRental.getString("genre")),
                        resultSetRental.getString("releaseYear"),
                        resultSetRental.getInt("quantity"),
                        imagePath = resultSetRental.getString("imagePath")
                );
                TilePane tempTilePane = new TilePane();
                tempTilePane.setPrefColumns(1);
                tempTilePane.setPrefRows(5);
                tempTilePane.setPadding(new Insets(30));
                ImageView tempImageView = new ImageView();
                tempImageView.getStyleClass().add("image-view-user-menu");
                tempImageView.setFitHeight(200);
                tempImageView.setFitWidth(133);
                Image image = new Image(imagePath);
                tempImageView.setImage(image);
                tempImageView.setOnMouseClicked(e -> {
                    RentPopupController.setMovieToRent(movie);
                    /*logic.openSceneInNewWindow("/scene/rentPopup/rentPopup.fxml", "Rent Movie");*/
                });
                tempTilePane.getChildren().addAll(tempImageView);
                tilePane.getChildren().add(tempTilePane);
                tilePane.setPrefColumns(10);
            }
        } catch (Exception e) {
            System.out.println("ohShit");
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public <T> void updateUserInfo(String table, String column, String idNameTable, int iduser, T data) {
        connect();
        try {
            String query = "UPDATE " + table + " SET " + column + " = ? WHERE " + idNameTable + " = " + iduser;

            PreparedStatement ps = connection.prepareStatement(query);
            if (data instanceof String) {
                String temp = (String) data;
                ps.setString(1, temp);
            } else if (data instanceof Integer) {
                int temp = (Integer) data;
                ps.setInt(1, temp);
            } else if (data instanceof Double) {
                Double temp = (Double) data;
                ps.setDouble(1, temp);
            } else if (data instanceof Boolean) {
                Boolean temp = (Boolean) data;
                ps.setBoolean(1, temp);
            }
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Something went wrong...");
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public void updateFirstName(int idUser, User user) throws SQLException {
        connect();
        String query = "UPDATE account SET firstName = ? WHERE idUser = ?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, user.getFirstName()); //needs a setter in the user class?
            System.out.println(user.getFirstName());
            preparedStmt.setInt(2, idUser);
            System.out.println(idUser);
            preparedStmt.executeUpdate();
            System.out.println("First name updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateLastName(int idUser, User user) throws SQLException {
        connect();
        String query = "UPDATE account SET lastName = ? WHERE idUser = ?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, user.getLastName());
            preparedStmt.setInt(2, idUser);
            preparedStmt.executeUpdate();
            System.out.println("Last name updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateEmail(int idUser, User user) throws SQLException {
        connect();
        String query = "UPDATE account SET email = ? WHERE idUser = ?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, user.getEmail()); //needs a setter in the user class?
            preparedStmt.setInt(2, idUser);
            preparedStmt.executeUpdate();
            System.out.println("Email updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateAddress(int idUser, User user) throws SQLException {
        connect();
        String query = "UPDATE account SET address = ? WHERE idUser = ?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, user.getAddress()); //needs a setter in the user class?
            preparedStmt.setInt(2, idUser);
            preparedStmt.executeUpdate();
            System.out.println("Address updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePhoneNumber(int idUser, User user) throws SQLException {
        connect();
        String query = "UPDATE account SET phoneNr = ? WHERE idUser = ?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, user.getPhoneNr()); //needs a setter in the user class?
            preparedStmt.setInt(2, idUser);
            preparedStmt.executeUpdate();
            System.out.println("Phone number updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Movie> searchMovieByGenre(String genre) {
        connect();
        movies.clear();
        String query = "SELECT title FROM movie WHERE genre = '" + genre + "'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Movie movie = new Movie(0, "", "", 0, Movie.Genre.Action, "", 0, "");
                movie.setTitle(resultSet.getString(1));
                movies.add(movie);

            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
        System.out.println(movies);
        return movies;
    }

    //krillepille
    public List<Movie> getMovieTitle(String title) {
        connect();
        movies.clear();
        String query = "SELECT title FROM movie WHERE title LIKE '" + title + "%' ";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                Movie movie = new Movie(0, "", "", 0, Movie.Genre.Action, "", 0, "");
                movie.setTitle(resultSet.getString(1));
                movies.add(movie);

            }
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println(movies);
        disconnect(); //do for all!
        return movies;
    }
}