package edu.upenn.cis350.project;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminStatsActivity extends AppCompatActivity {

    private Stats stats;
    private int users; // Number of user
    private String[] leaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stats);

        updateStats();

        header1();
        values1();
        values2();
        values3();
        header2();
        values4();
        values5();

    }

    public void updateStats() {
        stats = ParseStats.getAdminStats();
        users = ParseStats.numUsers();
        leaders = ParseStats.getLeaders();
    }

    public void header1() {
        TextView header = (TextView) findViewById(R.id.admin_label);
        // Update this image below:
        ImageView pictureField = (ImageView) findViewById(R.id.admin_picture);
    }

    public void labels1() {

    }

    public void values1() {
        TextView users = (TextView) findViewById(R.id.total_users_stats);
        users.setText("" + this.users);
        TextView posts = (TextView) findViewById(R.id.total_posts_stats);
        posts.setText("" + stats.posts());
        TextView comments = (TextView) findViewById(R.id.total_comments_stats);
        comments.setText("" + stats.commentActivity());
    }

    public void labels2() {

    }

    public void values2() {
        TextView ppu = (TextView) findViewById(R.id.ppu_stats);
        double ppuNum = 0;
        if (users != 0) {
            ppuNum = (double) Math.round(stats.posts() / users * 10) / 10;
        }
        ppu.setText(""+ ppuNum);
        TextView cpp = (TextView) findViewById(R.id.cpp_stats);
        double cppNum = 0;
        double tppNum = 0;
        if (stats.posts() != 0) {
            cppNum = (double) Math.round(stats.commentActivity() / stats.posts() * 10) / 10;
            tppNum = (double) Math.round(stats.trendingTags() / stats.posts() * 10) / 10;
        }
        cpp.setText(""+ cppNum);
        TextView tpp = (TextView) findViewById(R.id.tpp_stats);
        tpp.setText(""+ tppNum);
    }

    public void labels3() {

    }

    public void values3() {
        TextView uv = (TextView) findViewById(R.id.a_upvotes);
        uv.setText(""+ stats.getUpVotes());
        TextView dv = (TextView) findViewById(R.id.a_downvotes);
        dv.setText(""+ stats.getDownVotes());
        TextView uvp = (TextView) findViewById(R.id.uvp);
        uvp.setText(stats.percentString() + "%");
    }

    public void header2() {

    }

    public void labels4() {

    }

    public void values4() {
        TextView posts = (TextView) findViewById(R.id.lead_posts);
        posts.setText(leaders[0]);
        TextView tags = (TextView) findViewById(R.id.lead_tags);
        tags.setText(leaders[1]);
    }

    public void labels5() {

    }

    public void values5() {
        TextView high = (TextView) findViewById(R.id.lead_uvp_high);
        high.setText(leaders[2]);
        TextView low = (TextView) findViewById(R.id.lead_uvp_low);
        low.setText(leaders[3]);
    }

}
