package net.badgekeeper.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.badgekeeper.android.network.callbacks.BadgeKeeperAchievementsUnlockedCallback;
import net.badgekeeper.android.network.callbacks.BadgeKeeperProjectAchievementsCallback;
import net.badgekeeper.android.network.callbacks.BadgeKeeperUserAchievementsCallback;
import net.badgekeeper.android.objects.models.BadgeKeeperAchievement;
import net.badgekeeper.android.objects.models.BadgeKeeperReward;
import net.badgekeeper.android.objects.models.BadgeKeeperUnlockedAchievement;
import net.badgekeeper.android.objects.models.BadgeKeeperUserAchievement;

public class MainActivity extends Activity {

    private Button postButton;
    private Button incrementButton;
    private Button getProjectAchievementsButton;
    private Button getUserAchievementsButton;

    private EditText userEditText;
    private TextView responseTextView;

    private static int posts = 0;
    private static int increments = 0;

    private BadgeKeeperProjectAchievementsCallback projectCallback = null;
    private BadgeKeeperUserAchievementsCallback userCallback = null;
    private BadgeKeeperAchievementsUnlockedCallback unlockedCallback = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BadgeKeeper.setContext(this);
        BadgeKeeper.setProjectId("a93a3a6d-d5f3-4b5c-b153-538063af6121");

        postButton = (Button)findViewById(R.id.postButton);
        incrementButton = (Button)findViewById(R.id.incrementButton);
        getProjectAchievementsButton = (Button)findViewById(R.id.getProjectAchievementsButton);
        getUserAchievementsButton = (Button)findViewById(R.id.getUserAchievementsButton);

        userEditText = (EditText)findViewById(R.id.userEditText);
        responseTextView = (TextView)findViewById(R.id.responseEditText);

        unlockedCallback = new BadgeKeeperAchievementsUnlockedCallback() {
            @Override
            public void onSuccess(BadgeKeeperUnlockedAchievement[] achievements) {
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
                responseTextView.setText(result);
                setLoading(false);
            }

            @Override
            public void onError(int code, String message) {
                showError(code, message);
            }
        };

        projectCallback = new BadgeKeeperProjectAchievementsCallback() {
            @Override
            public void onSuccess(BadgeKeeperAchievement[] achievements) {
                String result = "";
                for (BadgeKeeperAchievement achievement : achievements) {
                    result += "Name: " + achievement.getDisplayName() + ", "
                            + "Description: " + achievement.getDescription() + System.getProperty("line.separator");
                }
                responseTextView.setText(result);
                setLoading(false);
            }

            @Override
            public void onError(int code, String message) {
                showError(code, message);
            }
        };

        userCallback = new BadgeKeeperUserAchievementsCallback() {
            @Override
            public void onSuccess(BadgeKeeperUserAchievement[] achievements) {
                String result = "";
                for (BadgeKeeperUserAchievement achievement : achievements) {
                    result += "Name: " + achievement.getDisplayName() + ", "
                            + "Description: " + achievement.getDescription() + ", "
                            + "Is unlocked: " + achievement.getIsUnlocked() + ". " + System.getProperty("line.separator");
                }
                responseTextView.setText(result);
                setLoading(false);
            }

            @Override
            public void onError(int code, String message) {
                showError(code, message);
            }
        };

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isLoginValid()) {
                setLoading(true);

                posts++;
                responseTextView.setText("Posting x = " + String.valueOf(posts));

                BadgeKeeper.setUserId(userEditText.getText().toString());
                BadgeKeeper.preparePostKeyWithValue("x", posts);
                BadgeKeeper.postPreparedValues(unlockedCallback);
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
                    BadgeKeeper.incrementPreparedValues(unlockedCallback);
                }
            }
        });

        getProjectAchievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading(true);
                responseTextView.setText("");
                BadgeKeeper.getProjectAchievements(projectCallback);
            }
        });

        getUserAchievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginValid()) {
                    setLoading(true);
                    responseTextView.setText("");

                    BadgeKeeper.setUserId(userEditText.getText().toString());
                    BadgeKeeper.getUserAchievements(userCallback);
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
}
