package net.badgekeeper.android;

/**
 * Created by pukhov-as on 08.10.15.
 */
public class BadgeKeeper {
    private static BadgeKeeper ourInstance = new BadgeKeeper();

    public static BadgeKeeper getInstance() {
        return ourInstance;
    }

    private String mProjectId;
    private String mUserId;
    private boolean mShouldLoadIcons;

    private BadgeKeeper() {

    }

    public void setProjectId(String projectId) {
        mProjectId = projectId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setShouldLoadIcons(boolean shouldLoadIcons) {
        mShouldLoadIcons = shouldLoadIcons;
    }
}
