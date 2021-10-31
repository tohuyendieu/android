package fptu.ninhtbm.thebookshop.model;

public class Book {
    int id;
    String name;
    String source;
    public Book(int id, String name, String source){
        this.id = id;
        this.name = name;
        this.source = source;
    }
    public String getSource() {
        return source;
    }
}
