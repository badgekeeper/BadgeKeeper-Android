package net.badgekeeper.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.badgekeeper.android.objects.models.BKProjectAchievement;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BadgeKeeperCallback {

    private Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BadgeKeeper.setProjectId("a3d549ce-939c-4203-b12f-c28fe7409208");
        BadgeKeeper.setCallback(this);

        postButton = (Button)findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BadgeKeeper.requestProjectAchievements();
            }
        });
    }

    @Override
    public void didReceiveProjectAchievements(List<BKProjectAchievement> achievements) {
        int i = 0;
    }

    @Override
    public void didReceiveErrorProjectAchievements(String description) {
        int i = 0;
    }
}
