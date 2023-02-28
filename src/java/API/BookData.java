package API;

public class BookData {
    private int id;
    private String title;
    private String author;
    private double bookRating;

    public BookData() {
    }

    public BookData(int id, String title, String author, double bookRating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.bookRating = bookRating;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getBookRating() {
        return bookRating;
    }

    public void setBookRating(double bookRating) {
        this.bookRating = bookRating;
    }
}