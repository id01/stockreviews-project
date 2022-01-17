package one.id0.stockreviews;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String,Object> toMap() {
        Map<String,Object> out = new HashMap<>();
        out.put("owner", owner);
        out.put("content", content);
        out.put("rating", rating);
        out.put("date", date);
        return out;
    }

    public Bundle toBundle() {
        Bundle out = new Bundle();
        out.putString("owner", owner);
        out.putString("content", content);
        out.putFloat("rating", rating);
        out.putLong("date", date);
        return out;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s, %f, %d}", owner, content, rating, date);
    }
}
