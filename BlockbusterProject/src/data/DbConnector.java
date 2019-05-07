package data;

import model.Movie;
import model.User;

import java.sql.*;

public class DbConnector {
    private String url = "jdbc:mysql://den1.mysql3.gear.host:3306/bustblockerdb?verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true&user=bustblockerdb&password=bustblocker!&serverTimeZone=UTF-8";
    private Connection connection = null;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

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

    public int tableSizeAccount(String accountTable){
        String temp = null;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(idUser) FROM account");
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                temp = resultSet.getString("COUNT(idUser)");
            }
        }catch (SQLException e){
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
            ps.setInt(7,movie.getQuantity());

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

    public String getEmail(String username) {
        connect();
        String email = "";
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE email =? AND admin = 1");
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
        return email;
    }

   public String getPassword(String password) {
        String pw = "";
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE password = ? AND admin = 1");
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
        return pw;
    }

    public boolean isAdmin(boolean admin){
        boolean isAdmin = false;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE admin = ?");
            preparedStatement.setBoolean(1, admin);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                isAdmin = resultSet.getBoolean(9);
                resultSet.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("Is admin: " + isAdmin);
        disconnect();

        return isAdmin;
    }

    public void addUserToDb(User user){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `account` (email, password,idUser, balance, firstName, lastName," +
                    "address, phoneNr, admin) VALUES (?,?,?,?,?,?,?,?,?)");

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3,tableSizeAccount("account")+ 1);
            preparedStatement.setDouble(4, user.getBalance());
            preparedStatement.setString(5, user.getFirstName());
            preparedStatement.setString(6, user.getLastName());
            preparedStatement.setString(7,user.getAddress());
            preparedStatement.setString(8,user.getPhone());
            preparedStatement.setBoolean(9,false);

            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println("Something went wrong!");
            e.printStackTrace();
        }
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
            e.printStackTrace();
        }
        return email;
    }

        public String userPassword(String pw){
            String password = "";
            try{
                preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE password = ? AND admin = 0");
                preparedStatement.setString(1,pw);
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    password = resultSet.getString(2);
                    resultSet.close();
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
            disconnect();
            return password;
        }*/

    }