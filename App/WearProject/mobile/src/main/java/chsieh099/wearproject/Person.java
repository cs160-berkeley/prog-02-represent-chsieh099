package chsieh099.wearproject;

/**
 * Created by CassidyHsieh on 3/1/16.
 */
public class Person {
    public String title;
    public String party;
    public String name;
    public String tweet;
    public String endyear;
    int imageId;

    public Person(String title, String party, String name, String tweet, String endyear, int imageId) {
        this.title = title;
        this.party = party;
        this.name = name;
        this.tweet = tweet;
        this.endyear = endyear;
        this.imageId = imageId;
    }
}
