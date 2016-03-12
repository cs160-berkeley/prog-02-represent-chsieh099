package chsieh099.wearproject;

/**
 * Created by CassidyHsieh on 3/1/16.
 */
public class Person {
    public String title; // Senator or Representative
    public String party; // D, I, or R
    public String name;
    public String tweet;
    public String endyear;
    public int imageId;
    public String website;
    public String email;

    public String memberId;
    public String[] committees;
    public String[] bills;

    public String twitterHandle;

    public Person(String title, String party, String name, String tweet, String endyear, int imageId,
                  String website, String email, String memberId, String twitterHandle) {
        this.title = title;
        this.party = party;
        this.name = name;
        this.tweet = tweet;
        this.endyear = endyear;
        this.imageId = imageId;
        this.website = website;
        this.email = email;
        this.memberId = memberId;
        this.committees = new String[3];
        this.bills = new String[3];
        this.twitterHandle = twitterHandle;
    }

    public Person(String title, String party, String name, String endyear, String website,
                  String email, String memberId, String twitterHandle) {
        this.title = title;
        this.party = party;
        this.name = name;
//        this.tweet = "Today I was pleased to introduce a bill to provide assistance to our crab fishing communities.";
        this.endyear = endyear;
//        this.imageId = R.drawable.defaultprofile;
        this.website = website;
        this.email = email;
        this.memberId = memberId;
        this.committees = new String[3];
        this.bills = new String[3];
        this.twitterHandle = twitterHandle;

        if (name.equals("Barbara Lee")) {
            this.tweet = "ICYMI: Impt work by @SFChronicle highlighting #AIDS survivors. Let’s continue our fight for an AIDS-free generation.";
            this.imageId = R.drawable.barbaralee;
        } else if (name.equals("Barbara Boxer")) {
            this.tweet = ".@SenateDems stood united at the Supreme Court today to tell @Senate_GOPs: #DoYourJob";
            this.imageId = R.drawable.barbaraboxer;
        } else if (name.equals("Dianne Feinstein")) {
            this.tweet = "Chatting w/ Senate pages from Calif. They work so hard! Great intro to our legislative system. @SenateYouth #USSYP";
            this.imageId = R.drawable.diannefeinstein;
        } else if (name.equals("Duncan Hunter")) {
            this.tweet = ".@Rep_Hunter set to introduce the MARTLAND Act in #Congress. Read & RT to learn about latest in #SgtMartland's case: http://aclj.us/1M5Fhk5";
            this.imageId = R.drawable.duncanhunter;
        } else if (name.equals("Ken Calvert")) {
            this.tweet = "Today, I introduce legislation that will double the maximum jail sentence for anyone convicted of mail theft. http://goo.gl/TNZAt1";
            this.imageId = R.drawable.kencalvert;
        } else if (name.equals("Jared Huffman")) {
            this.tweet = "Happy 100th @NatlParkService! Thanks for all the work you to do to protect our beautiful parks in the North Coast";
            this.imageId = R.drawable.jaredhuffman;
        } else if (name.equals("Mike Thompson")) {
            this.tweet = "Toured Eastlake Landfill & thanked employees for their work after the Valley Fire. http://www.record-bee.com/general-news/20160223/getting-his-fill...";
            this.imageId = R.drawable.mikethompson;
        } else {
            this.tweet = "Today I was pleased to introduce a bill to provide assistance to our crab fishing communities.";
            this.imageId = R.drawable.defaultprofile;
        }
    }
}
