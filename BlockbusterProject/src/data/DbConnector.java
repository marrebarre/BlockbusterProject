package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Size;
import model.Admin;
import model.Movie;
import model.User;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnector {
    public Connection connection = null;
    private Statement statement;
    public ResultSet resultSet;
    public List<User> users = new ArrayList<>();
    public List<Admin> admins = new ArrayList<>();

    public Connection connect() {
        try {
            String url = "jdbc:mysql://den1.mysql3.gear.host:3306/bustblockerdb?verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true&user=bustblockerdb&password=bustblocker!&serverTimeZone=UTF-8";
            connection = DriverManager.getConnection(url);
        } catch (SQLException sql) {
            System.err.println("Connection failed" + sql);
        }
        return connection;
    }

    private int tableSize(String tableName) {
        String temp = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(idMovie) FROM movie");
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                temp = resultSet.getString("COUNT(idMovie)");
            }

        } catch (SQLException e) {
            System.out.println("Table Empty or does not exist.");
            e.printStackTrace();
        }

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

    public void addMovieToDB(Movie movie) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `movie`(idMovie, title, director, price, genre, releaseYear, quantity) VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, tableSize("movie") + 1);
            ps.setString(2, movie.getTitle());
            ps.setString(3, movie.getDirector());
            ps.setDouble(4, movie.getPrice());
            ps.setString(5, movie.getGenreAsString());
            ps.setString(6, movie.getReleaseYear());
            ps.setInt(7, movie.getQuantity());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error");
        }
    }

    public void findMovieInDB(String title) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT movie.title FROM movie");

            while (resultSet.next()) {
                System.out.println("Title of movie: " + resultSet.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Statement couldn't be executed.");
            e.printStackTrace();
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
        String query = "SELECT * FROM account WHERE email =? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, pw);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                admin = resultSet.getBoolean(9);
                if (admin) {
                    adminList(resultSet);
                } else {
                    userList(resultSet);
                }
                resultSet.close();
            }
        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private List<User> userList(ResultSet resultSet) {
        try {
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
            users.add(user);
            //System.out.println("Namn: " + user.getFirstName() + " " + user.getLastName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private List<Admin> adminList(ResultSet resultSet) {
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
            //System.out.println("Email: " + admin.getEmail() + "Lösen: " + admin.getPassword());
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

    //Krillepille
    public void updateFirstName(int idUser, User user) throws SQLException {

        connect();

        String query = "UPDATE account SET firstName = ? WHERE idUser = ?";

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, user.getFirstName()); //needs a setter in the user class?
            preparedStmt.setInt(2, idUser);

            preparedStmt.executeUpdate();
            //  System.out.println("Firstname updated!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //Krillepille
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

    //Krillepille
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

    //Krillepille
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

    //Krillepille
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

    //krillepille
    public String getFirstName(int idUser) {
        connect();
        String firstName = "";
        String query = "SELECT firstName FROM account WHERE idUser =?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, idUser);
            resultSet = preparedStmt.executeQuery();

            if (resultSet.next()) {
                firstName = resultSet.getString(1);
                resultSet.close();
            }
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        // System.out.println("First name: " + firstName);
        return firstName;
    }

    //krillepille
    public String getLastname(int idUser) {
        connect();
        String lastName = "";
        String query = "SELECT lastName FROM account WHERE idUser =?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, idUser);
            resultSet = preparedStmt.executeQuery();

            if (resultSet.next()) {
                lastName = resultSet.getString(1);
                resultSet.close();
            }
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        //  System.out.println("Last name: " + lastName);
        return lastName;
    }

    //krillepille
    public String getEmail(int idUser) {
        connect();
        String email = "";
        String query = "SELECT email FROM account WHERE idUser =?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, idUser);
            resultSet = preparedStmt.executeQuery();

            if (resultSet.next()) {
                email = resultSet.getString(1);
                resultSet.close();
            }
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        // System.out.println("Email : " + email);
        return email;
    }

    //krillepille
    public String getAddress(int idUser) {
        connect();
        String address = "";
        String query = "SELECT address FROM account WHERE idUser =?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, idUser);
            resultSet = preparedStmt.executeQuery();

            if (resultSet.next()) {
                address = resultSet.getString(1);
                resultSet.close();
            }
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        // System.out.println("Address: " + address);
        return address;
    }

    //krillepille
    public String getPhoneNumber(int idUser) {
        connect();
        String phoneNr = "";
        String query = "SELECT phoneNr FROM account WHERE idUser =?";
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, idUser);
            resultSet = preparedStmt.executeQuery();

            if (resultSet.next()) {
                phoneNr = resultSet.getString(1);
                resultSet.close();
            }
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        // System.out.println("Phone number: " + phoneNr);
        disconnect(); //do for all!
        return phoneNr;
    }
/*
        //Krillepille (all images put into a list from database, to later be displayed)

    public ObservableList<Movie> familyMovieImage() throws FileNotFoundException {

        connect();
        ObservableList<Movie> movieList = FXCollections.observableArrayList();
        String genre = "Family";
        String query = "SELECT image FROM movie WHERE genre = '" + genre + "'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Movie movie = new Movie("","",0, Movie.Genre.Action,"",0);
              //  movie.setImage(resultSet.getBlob(1));
                    InputStream is = resultSet.getBinaryStream("pimg");
                    OutputStream os= new FileOutputStream(new File("pic.jpg"));
                    byte[] content= new byte[1024];
                    int size=0;
                    while((size = is.read(content))!=-1){

                        os.write(content, 0,size);
                    }
                    os.close();
                    is.close();
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return movieList;
    }
    */
}


    /*public String userEmail(String username) {
        connect();
        String email = "";
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE email = ? AND admin = 0");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                email = resultSet.getString(1);
                resultSet.close();
            }
            //preparedStatement.setMaxRows(10);

        } catch (SQLException e) {
            System.out.println("Something went wrong!");
            e.printStackTrace();
        }
    }
    */
