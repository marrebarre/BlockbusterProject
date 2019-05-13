package data;

import controller.AdminMenuController;
import javafx.scene.control.Alert;
import model.Admin;
import model.Movie;
import model.User;

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

    public int tableSize(String tableName) {
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
        String temp = null ;
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
                        resultSet.getInt("quantity")
                );
            }
        } catch (SQLException e) {

        } finally {
            disconnect();
        }
        return movie;
    }

    public void addMovieToDB(Movie movie) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `movie`(idMovie, title, director, price, genre, releaseYear, quantity) VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, movie.getIdMovie());
            ps.setString(2, movie.getTitle());
            ps.setString(3, movie.getDirector());
            ps.setDouble(4, movie.getPrice());
            ps.setString(5, movie.getGenreAsString());
            ps.setString(6, movie.getReleaseYear());
            ps.setInt(7, movie.getQuantity());

            ps.executeUpdate();
            AdminMenuController.alert("Successfully added movie!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            System.out.println("Error when loading to database");
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

    //UPDATE TABLE
    public <T> void updateTableColumnById(String table, String column,String idNameInTable, int id, T newData) {
        connect();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE "+table+" SET "+column+" = ? WHERE "+idNameInTable+" = "+id);
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
}