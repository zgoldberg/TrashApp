package edu.upenn.cis350.project;

// Class that gets instantiated for each user
public class Stats {

    int posts; // Times this user has posted
    int tags; // Times this user has been tagged in a post
    int upVotes; // Total upVotes among posts this User was tagged in
    int downVotes; // Total downVotes among posts this User was tagged in

    int commentsPosted;
    int commentsTagged;

    public Stats() {

    }

    public Stats(int posts, int tags, int upVotes, int downVotes, int commentsPosted, int commentsTagged) {
        this.posts = posts;
        this.tags = tags;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.commentsPosted = commentsPosted;
        this.commentsTagged = commentsTagged;
    }

    // Return number of posts User has made
    public int posts() {
        return posts;
    }

    // Return number of times voted on per tag, to document who is "trending" in posted tagged in
    public double trendingVotes() {
        if (tags == 0) {
            return 0.0;
        }
        return (double) (upVotes + downVotes) / tags;
    }

    public String avgVotesString() {
        double val = (double) Math.round(trendingVotes()*10)/10;
        return "" + val;
    }

    // Return number of times user has been tagged, for simpler metric of trending
    public int trendingTags() {
        return tags + commentsTagged;
    }

    // Return percent of activities (tagged in) which were viewed as positive, i.e. upVotes
    public double getPercent() {
        if (upVotes + downVotes == 0) {
            return 0;
        }
        return (double) upVotes / (upVotes + downVotes);
    }

    public String percentString() {
        int val = (int) Math.round((getPercent() * 100));
        return "" + val;
    }

    public int activity() {
        return posts + tags;
    }

    public int commentActivity() {
        return commentsPosted + commentsTagged;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public int getCommentsPosted() {
        return commentsPosted;
    }

    public int getCommentsTagged() {
        return commentsTagged;
    }

    public int getKarma() {
        return upVotes - downVotes;
    }

}
