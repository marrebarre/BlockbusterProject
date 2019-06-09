package model;

public class Movie {

    public enum Genre {
        Action,
        Adventure,
        Drama,
        Horror,
        Scifi,
        Family
    }

    private String title;
    private String director;
    private double price;
    private Genre genre;
    private String releaseYear;
    private int quantity;
    private int idMovie;
    private String imagePath;

    public Movie(int idMovie, String title, String director, double price, Genre genre, String releaseYear, int quantity, String imagePath) {
        this.idMovie = idMovie;
        this.title = title;
        this.director = director;
        this.price = price;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public double getPrice() {
        return price;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getGenreAsString() {
        switch (this.genre){
            case Action: return "Action";
            case Drama: return "Drama";
            case Scifi: return "Sci-fi";
            case Family: return "Family";
            case Horror: return "Horror";
            case Adventure: return "Adventure";
            default: return null;

        }
    }
    public static String getGenreAsString(Genre genre) {
        switch (genre){
            case Action: return "Action";
            case Drama: return "Drama";
            case Scifi: return "Sci-fi";
            case Family: return "Family";
            case Horror: return "Horror";
            case Adventure: return "Adventure";
            default: return null;

        }
    }
    public static Genre getStringAsGenre(String string) {
        switch (string){
            case "Action": return Genre.Action;
            case "Drama": return Genre.Drama;
            case "Sci-fi": return Genre.Scifi;
            case "Family": return Genre.Family;
            case "Horror": return Genre.Horror;
            case "Adventure": return Genre.Adventure;
            default: return null;
        }
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public String toString(){
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
