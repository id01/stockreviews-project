package one.id0.stockreviews;

public class Review {
    public String owner, content;
    public float rating;
    public long date;

    public Review(String owner, String content, float rating, long date) {
        this.owner = owner;
        this.content = content;
        this.rating = rating;
        this.date = date;
    }
}
