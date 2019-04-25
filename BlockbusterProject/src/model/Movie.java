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

    public Movie(String title, String director, double price, Genre genre) {
        this.title = title;
        this.director = director;
        this.price = price;
        this.genre = genre;
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

    public void setDirector(String director) {
        this.director = director;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
