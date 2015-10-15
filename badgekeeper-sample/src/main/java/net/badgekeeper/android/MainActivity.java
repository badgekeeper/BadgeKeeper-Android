package net.badgekeeper.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.badgekeeper.android.objects.models.BKProjectAchievement;
import net.badgekeeper.android.objects.models.BKUnlockedUserAchievement;
import net.badgekeeper.android.objects.models.BKUserAchievement;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BadgeKeeperCallback {

    private Button postButton;
    private Button incrementButton;
    private EditText userEditText;
    private EditText responseEditText;

    private static int posts = 0;
    private static int increments = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BadgeKeeper.setProjectId("a93a3a6d-d5f3-4b5c-b153-538063af6121");
        BadgeKeeper.setCallback(this);

        postButton = (Button)findViewById(R.id.postButton);
        incrementButton = (Button)findViewById(R.id.incrementButton);
        userEditText = (EditText)findViewById(R.id.userEditText);
        responseEditText = (EditText)findViewById(R.id.responseEditText);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isLoginValid()) {
                setLoading(true);
                responseEditText.setText("");

                posts++;

                BadgeKeeper.setUserId(userEditText.getText().toString());
                BadgeKeeper.preparePostKeyWithValue("x", posts);
                BadgeKeeper.postPreparedValues();
            }
            }
        });

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginValid()) {
                    setLoading(true);
                    responseEditText.setText("");

                    increments++;

                    BadgeKeeper.setUserId(userEditText.getText().toString());
                    BadgeKeeper.prepareIncrementKeyWithValue("x", increments);
                    BadgeKeeper.incrementPreparedValues();
                }
            }
        });
    }

    private boolean isLoginValid() {
        String text = this.userEditText.getText().toString();
        if (text == null || text.isEmpty()) {
            showErrorWithTitleAndMessage("Error", "You should specify your User ID first!");
            return false;
        }
        return true;
    }

    private void showErrorWithTitleAndMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            this.userEditText.setEnabled(false);
            this.postButton.setEnabled(false);
            this.incrementButton.setEnabled(false);
            //[self.activityIndicatorView startAnimating];
        }
        else {
            this.userEditText.setEnabled(true);
            this.postButton.setEnabled(true);
            this.incrementButton.setEnabled(true);
            //[self.activityIndicatorView stopAnimating];
        }
    }

    @Override
    public void didReceiveProjectAchievements(BKProjectAchievement[] achievements) {
        setLoading(false);
    }

    @Override
    public void didReceiveErrorProjectAchievements(int code, String description) {
        setLoading(false);
    }

    @Override
    public void didReceiveUserAchievements(BKUserAchievement[] achievements) {
        setLoading(false);
    }

    @Override
    public void didReceiveErrorUserAchievements(int code, String message) {
        setLoading(false);
    }

    @Override
    public void didReceiveUnlockedUserAchievements(BKUnlockedUserAchievement[] achievements) {
        setLoading(false);
    }

    @Override
    public void didReceiveErrorUnlockedUserAchievements(int code, String message) {
        setLoading(false);
    }
}
