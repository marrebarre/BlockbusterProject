package database;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import model.*;
import javafx.stage.Stage;
import scene.rentPopup.RentPopupController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static model.Logic.alert;
import static scene.rentPopup.RentPopupController.movieToRent;
import static scene.userMenu.UserMenuController.loggedInUser;

public class DbConnector {
    public Connection connection = null;
    public Statement statement;
    public ResultSet resultSet;
    Logic logic;
    public List<User> users = new ArrayList<>();
    public List<Admin> admins = new ArrayList<>();
    private static final String ACCOUNT_SID = "AC0eb02f6d7980e28e685a99eb1e6dfbb3";
    private static final String AUTH_TOKEN = "7196be33b314fe016e8ac0e0ed7495e2";
    public static boolean verify = true;

    private static boolean isVerify() {
        return verify;
    }

    public static void setVerify(boolean verify) {
        DbConnector.verify = verify;
    }

    private Date estDateReturned;

    public List<Movie> movies = new ArrayList<>();
    private List<Account_Has_Movie> accMovies = new ArrayList<>();

    public void connect() {
        try {
            String url = "jdbc:mysql://den1.mysql3.gear.host:3306/bustblockerdb?verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true&user=bustblockerdb&password=bustblocker!&serverTimeZone=UTF-8";
            connection = DriverManager.getConnection(url);
        } catch (SQLException sql) {
            System.err.println("Connection failed" + sql);
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

    public void addRental(Movie chosenMovie, Date dateRented, Date dateReturned, ActionEvent event) {
        String SQLQuery = "INSERT INTO `account_has_movie` (account_idUser, movie_idMovie, dateRented, estimatedDateOfReturned, fee, returned) VALUES (?,?,?,?,?,?)";
        setVerify(true);
        try {
            PreparedStatement ps = connection.prepareStatement(SQLQuery);
            estDateReturned = dateReturned;

            if (isVerify() == true) {
                movieStockHandler();
            }
            if (isVerify() == true) {
                economyHandler();
            }
            if (isVerify() == true) {
                ps.setInt(1, loggedInUser.getIdUser());
                ps.setInt(2, movieToRent.getIdMovie());
                ps.setDate(3, dateRented);
                ps.setDate(4, dateReturned);
                ps.setDouble(5, movieToRent.getPrice());
                ps.setBoolean(6, false);
                ps.executeUpdate();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        } catch (SQLException e) {
            System.out.println("Error when loading to database");
            e.printStackTrace();
        }
    }

    public void textMessageHandler() throws SQLException {
        //System.out.println("Pre textHandler; " + isVerify());
        if (isVerify() == true) {
            String SQLQuery = "SELECT * FROM account_has_movie WHERE account_idUser = ?";
            connect();
            PreparedStatement ps = connection.prepareStatement(SQLQuery);
            ps.setInt(1, loggedInUser.getIdUser());

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message
                    .creator(new PhoneNumber("+46734453860"), // to
                            new PhoneNumber("+12018066425"), // from
                            "Hello " + loggedInUser.getFirstName() + "!" +
                                    "\nYou just rented " + movieToRent.getTitle() + "." +
                                    "\nReturn expected " + estDateReturned.toString() + "." +
                                    "\nBest regards," +
                                    "\nBustblockers")
                    .create();
            System.out.println(message.getSid());
        }
    }

    private void economyHandler() {
        //System.out.println("Pre economyHandler verified: " + isVerify());
        String SQLQuery = "UPDATE account INNER JOIN movie SET balance = ? WHERE idUser = ?";
        //System.out.println("Movie price: " + movieToRent.getPrice());
        //System.out.println("Pre: " + loggedInUser.getBalance());
        try {
            PreparedStatement ps = connection.prepareStatement(SQLQuery);
            if (loggedInUser.getBalance() < movieToRent.getPrice()) {
                System.out.println("Insufficient funds");
                setVerify(false);
            } else if (loggedInUser.getBalance() >= movieToRent.getPrice()) {
                loggedInUser.setBalance(loggedInUser.getBalance() - movieToRent.getPrice());
                //System.out.println("Post: " + loggedInUser.getBalance());
                ps.setDouble(1, loggedInUser.getBalance());
                ps.setInt(2, loggedInUser.getIdUser());
                ps.executeUpdate();
                System.out.println("Sufficient funds on user account");
            }
        } catch (Exception e) {
            System.out.println("Banking error");
            e.printStackTrace();
        }
    }

    private void movieStockHandler() {
        //System.out.println("Pre stockHandler: " + isVerify());
        String SQLQuery = "UPDATE movie SET quantity = ? WHERE idMovie = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQLQuery);
            if (movieToRent.getQuantity() <= 0 || loggedInUser.getBalance() < movieToRent.getPrice()) {
                if (movieToRent.getQuantity() <= 0) {
                    alert("Out of stock", Alert.AlertType.WARNING);
                    setVerify(false);
                } else if (loggedInUser.getBalance() < movieToRent.getPrice()) {
                    alert("Balance insufficient", Alert.AlertType.WARNING);
                    setVerify(false);
                }
            } else if ((loggedInUser.getBalance() >= movieToRent.getPrice()) && movieToRent.getQuantity() > 0) {
                movieToRent.setQuantity(movieToRent.getQuantity() - 1);
                ps.setInt(1, movieToRent.getQuantity());
                ps.setInt(2, movieToRent.getIdMovie());
                ps.executeUpdate();
                alert("Movie successfully subtracted from quantity!", Alert.AlertType.CONFIRMATION);
            }
        } catch (SQLException e) {
            System.out.println("Error when loading to database");
            e.printStackTrace();
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
            dataHandler(newData, ps);
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


    public <T> void updateUserInfo(String table, String column, String idNameTable, int iduser, T data) {
        connect();
        try {
            String query = "UPDATE " + table + " SET " + column + " = ? WHERE " + idNameTable + " = " + iduser;
            PreparedStatement ps = connection.prepareStatement(query);
            dataHandler(data, ps);
        } catch (SQLException e) {
            System.out.println("Something went wrong...");
        } finally {
            disconnect();
        }
    }

    private <T> void dataHandler(T data, PreparedStatement ps) throws SQLException {
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
    }

    public void updateFirstName(int idUser, User user) {
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

    public void updateLastName(int idUser, User user) {
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

    public void updateEmail(int idUser, User user) {
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

    public void updateAddress(int idUser, User user) {
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

    public void updatePhoneNumber(int idUser, User user) {
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


    //krille
    public void updatePassword(String userMail, User user){

        connect();
        String query = "UPDATE account SET password = ? WHERE email = ?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1,user.getPassword()); //needs a setter in the user class?
            preparedStmt.setString(2,userMail );
            preparedStmt.executeUpdate();
            System.out.println("Password updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    //krille
    public List<Account_Has_Movie> showRentals(int idUser) {
        connect();
        accMovies.clear();
        String query = "SELECT * FROM movie INNER JOIN account_has_movie WHERE account_idUser = '" + idUser + "'";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            resultSet = preparedStmt.executeQuery();

            while (resultSet.next()) {
                Account_Has_Movie accountHasMovie = new Account_Has_Movie(
                        resultSet.getInt("movie_idMovie"),
                        resultSet.getString("title"),
                        resultSet.getString("director"),
                        resultSet.getDouble("price"),
                        Movie.getStringAsGenre(resultSet.getString("genre")),
                        resultSet.getString("releaseYear"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("imagePath"),
                        resultSet.getInt("rentalID"),
                        resultSet.getInt("account_idUser"),
                        resultSet.getInt("movie_idMovie"),
                        resultSet.getString("dateRented"),
                        resultSet.getString("estimatedDateOfReturned"),
                        resultSet.getDouble("fee"),
                        resultSet.getBoolean("returned"));

                accMovies.add(accountHasMovie);
                accMovies.toString().replace("[", "").replace("]", "");

            }
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println(accMovies.toString().substring(1, accMovies.toString().length() - 1));
        disconnect(); //do for all!
        return accMovies;
    }
}