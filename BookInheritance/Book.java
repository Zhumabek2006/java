package BookInheritance;

public class Book {
    protected String title;
    protected String author;
    protected int yearPublished;

    public Book(String title, String author, int yearPublished) {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
    }

    public String getBookInfo() {
        return "Title: " + title + ", Author: " + author + ", Year: " + yearPublished;
    }

    public void printBookDetails() {
        System.out.println(getBookInfo());
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
    public int getYearPublished() {
        return yearPublished;
    }
    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }
}

class PrintedBook extends Book {
    private int numberOfPages;
    private String publisher;

    public PrintedBook(String title, String author, int yearPublished, int numberOfPages, String publisher) {
        super(title, author, yearPublished);
        this.numberOfPages = numberOfPages;
        this.publisher = publisher;
    }

    public String getBookInfo() {
        return super.getBookInfo() + ", Pages: " + numberOfPages + ", Publisher: " + publisher;
    }

    public void printBookDetails() {
        System.out.println(getBookInfo());
    }

    public void bookType() {
        System.out.println("This is a printed book.");
    }
}

class EBook extends Book {
    private double fileSizeMB;
    private String fileFormat;

    public EBook(String title, String author, int yearPublished, double fileSizeMB, String fileFormat) {
        super(title, author, yearPublished);
        this.fileSizeMB = fileSizeMB;
        this.fileFormat = fileFormat;
    }
    public String getBookInfo() {
        return super.getBookInfo() + ", File Size: " + fileSizeMB + "MB, Format: " + fileFormat;
    }

    public void printBookDetails() {
        System.out.println(getBookInfo());
    }
    public void bookType() {
        System.out.println("This is an ebook.");
    }
}


class Main {
    public static void main(String[] args) {
        PrintedBook pb = new PrintedBook("The Great Gatsby", "F. Scott Fitzgerald", 1925, 218, "Scribner");
        pb.printBookDetails();
        pb.bookType();
        EBook eb = new EBook("1984", "George Orwell", 1949, 2.5, "EPUB");
        eb.printBookDetails();
        eb.bookType();
    }
}
