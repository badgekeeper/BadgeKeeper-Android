package net.badgekeeper.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.badgekeeper.android.objects.models.BadgeKeeperAchievement;
import net.badgekeeper.android.objects.models.BadgeKeeperReward;
import net.badgekeeper.android.objects.models.BadgeKeeperUnlockedAchievement;
import net.badgekeeper.android.objects.models.BadgeKeeperUserAchievement;

public class MainActivity extends AppCompatActivity implements BadgeKeeperCallback {

    private Button postButton;
    private Button incrementButton;
    private Button getProjectAchievementsButton;
    private Button getUserAchievementsButton;

    private EditText userEditText;
    private TextView responseTextView;

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
        getProjectAchievementsButton = (Button)findViewById(R.id.getProjectAchievementsButton);
        getUserAchievementsButton = (Button)findViewById(R.id.getUserAchievementsButton);

        userEditText = (EditText)findViewById(R.id.userEditText);
        responseTextView = (TextView)findViewById(R.id.responseEditText);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isLoginValid()) {
                setLoading(true);

                posts++;
                responseTextView.setText("Posting x = " + String.valueOf(posts));

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
                    responseTextView.setText("");

                    increments++;
                    responseTextView.setText("Incrementing x by " + String.valueOf(increments));

                    BadgeKeeper.setUserId(userEditText.getText().toString());
                    BadgeKeeper.prepareIncrementKeyWithValue("x", increments);
                    BadgeKeeper.incrementPreparedValues();
                }
            }
        });

        getProjectAchievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading(true);
                responseTextView.setText("");
                BadgeKeeper.requestProjectAchievements();
            }
        });

        getUserAchievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginValid()) {
                    setLoading(true);
                    responseTextView.setText("");

                    BadgeKeeper.setUserId(userEditText.getText().toString());
                    BadgeKeeper.requestUserAchievements();
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
            this.getProjectAchievementsButton.setEnabled(false);
            this.getUserAchievementsButton.setEnabled(false);
        }
        else {
            this.userEditText.setEnabled(true);
            this.postButton.setEnabled(true);
            this.incrementButton.setEnabled(true);
            this.getProjectAchievementsButton.setEnabled(true);
            this.getUserAchievementsButton.setEnabled(true);
        }
    }

    private void showError(int code, String message) {
        showErrorWithTitleAndMessage("Error code: " + String.valueOf(code), message);
        setLoading(false);
    }

    @Override
    public void onSuccessReceivedProjectAchievements(BadgeKeeperAchievement[] achievements) {
        String result = "";
        for (BadgeKeeperAchievement achievement : achievements) {
            result += "Name: " + achievement.getDisplayName() + ", "
                   + "Description: " + achievement.getDescription() + System.getProperty("line.separator");
        }
        this.responseTextView.setText(result);
        setLoading(false);
    }

    @Override
    public void onSuccessReceivedUserAchievements(BadgeKeeperUserAchievement[] achievements) {
        String result = "";
        for (BadgeKeeperUserAchievement achievement : achievements) {
            result += "Name: " + achievement.getDisplayName() + ", "
                    + "Description: " + achievement.getDescription() + ", "
                    + "Is unlocked: " + achievement.getIsUnlocked() + ". " + System.getProperty("line.separator");
        }
        this.responseTextView.setText(result);
        setLoading(false);
    }

    @Override
    public void onSuccessReceivedUnlockedUserAchievements(BadgeKeeperUnlockedAchievement[] achievements) {
        String result = "";
        for (BadgeKeeperUnlockedAchievement achievement : achievements) {
            result += "Name: " + achievement.getDisplayName() + ", "
                    + "Description: " + achievement.getDescription() + ", "
                    + "Is unlocked: " + achievement.getIsUnlocked() + ", "
                    + "Rewards: [";
            for (BadgeKeeperReward reward : achievement.getRewards()) {
                result += "Name: " + reward.getName() + "Value: " + String.valueOf(reward.getValue()) + ";";
            }
            result += "]. " + System.getProperty("line.separator");
        }
        this.responseTextView.setText(result);
        setLoading(false);
    }

    @Override
    public void onErrorReceived(BadgeKeeperEventType evenType, int code, String message) {
        showError(code, message);
    }
}
