package data;

import controller.LoginScreenController;
import model.Movie;

import java.sql.*;

public class DbConnector {
    String url = "jdbc:mysql://den1.mysql3.gear.host:3306/bustblockerdb?verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true&user=bustblockerdb&password=bustblocker!&serverTimeZone=UTF-8";
    Connection connection = null;
    Statement statement;
    ResultSet resultSet;
    PreparedStatement preparedStatement;
    LoginScreenController loginScreenController;
    //ResultSet resultSet;

    public Connection connect() {
        try {
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

    public void addMovieToDB(Movie movie) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `movie`(idMovie, title, director, price, genre) VALUES (?,?,?,?,?)");
            ps.setInt(1, tableSize("movie") + 1);
            ps.setString(2, movie.getTitle());
            ps.setString(3, movie.getDirector());
            ps.setDouble(4, movie.getPrice());
            ps.setString(5, movie.getGenreAsString());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error yall");
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

    public String getEmail(String username) {
        connect();
        String email = "";

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE email =?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                email = resultSet.getString(1);
                resultSet.close();
            }
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("Email: " + email);
        disconnect();
        return email;
    }

    public String getPassword(String password) {
        connect();
        String pw = "";

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE password = ?");
            preparedStatement.setString(1, password);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pw = resultSet.getString(2);
                resultSet.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Password: " + pw);
        disconnect();
        return pw;
    }
}