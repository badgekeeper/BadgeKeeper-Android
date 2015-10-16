/**

 Badge Keeper

 The MIT License (MIT)

 Copyright (c) 2015 Alexander Pukhov, Badge Keeper

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 */

package net.badgekeeper.android;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import net.badgekeeper.android.entities.BadgeKeeperEntityStorage;
import net.badgekeeper.android.network.BadgeKeeperApi;
import net.badgekeeper.android.network.BadgeKeeperApiService;
import net.badgekeeper.android.network.BadgeKeeperResponse;
import net.badgekeeper.android.network.BadgeKeeperResponseError;
import net.badgekeeper.android.objects.BadgeKeeperPair;
import net.badgekeeper.android.objects.models.BadgeKeeperProject;
import net.badgekeeper.android.objects.models.BadgeKeeperReward;
import net.badgekeeper.android.objects.models.BadgeKeeperUnlockedAchievement;
import net.badgekeeper.android.objects.models.BadgeKeeperUserAchievement;

public class BadgeKeeper {
    private static BadgeKeeper instance = new BadgeKeeper();

    // Client application context for storage
    private Context context = null;

    // Service instances
    private BadgeKeeperCallback callback = null;
    private BadgeKeeperApiService service = null;
    private BadgeKeeperEntityStorage storage = null;

    // Service properties
    private String projectId = null;
    private String userId = null;
    private boolean shouldLoadIcons = false;

    // Temporary storage
    private Map<String, List<BadgeKeeperPair<String, Double>>> postVariables = new HashMap<>();
    private Map<String, List<BadgeKeeperPair<String, Double>>> incrementVariables = new HashMap<>();

    private interface BKInternalCallback <Type> {
        void onSuccess(Type type);
    }

    private BadgeKeeper() {
        service = new BadgeKeeperApiService();
        storage = new BadgeKeeperEntityStorage();
    }

    /**
     * Setup Client application context (we need it for storage)
     * @param context - Client application context.
     */
    public static void setContext(Context context) {
        instance.context = context;
    }

    /**
     * Setup Project Id for Badge Keeper instance. You can find Project Id in admin panel.
     * @param projectId - Project Id from Badge Keeper admin panel.
     */
    public static void setProjectId(String projectId) {
        instance.projectId = projectId;
    }

    /**
     * Setup User Id for Badge Keeper instance. This is client unique id in your system.
     * @param userId - Unique client Id in your system.
     */
    public static void setUserId(String userId) { instance.userId = userId; }

    /**
     * When sets to true load achievement icons from Badge Keeper service
     * with base64 encoded images for unloked and locked states.
     * Sets this parameter to false can reduce traffic and you will get response faster.
     * @param shouldLoadIcons - Should we request images from Badge Keeper service or not.
     */
    public static void setShouldLoadIcons(boolean shouldLoadIcons) { instance.shouldLoadIcons = shouldLoadIcons; }

    /**
     * Setup callback to notify system with event after Badge Keeper request.
     * @param callback - Callback in application to receive Badge Keeper notifications.
     */
    public static void setCallback(BadgeKeeperCallback callback) { instance.callback = callback; }

    /**
     * Requests all project achievements list.
     * Check that Project Id and callback configured.
     */
    public static void requestProjectAchievements() {
        // Validation
        if (!instance.isProjectParametersValid()) {
            return;
        }

        final BKInternalCallback<BadgeKeeperProject> callback = new BKInternalCallback<BadgeKeeperProject>() {
            @Override
            public void onSuccess(BadgeKeeperProject bkProjectInformation) {
                instance.callback.onSuccessReceivedProjectAchievements(bkProjectInformation.getAchievements());
            }
        };

        // Invoke
        BadgeKeeperApi api = instance.service.getApi();
        Call<BadgeKeeperResponse<BadgeKeeperProject>> call = api.getProjectAchievements(instance.projectId, instance.shouldLoadIcons);
        instance.makeRequest(callback, BadgeKeeperEventType.RequestProjectAchievements, call);
    }

    /**
     * Requests all user achievements list.
     * Check that Project Id, User Id and callback configured.
     */
    public static void requestUserAchievements() {
        // Validation
        if (!instance.isParametersValid()) {
            return;
        }

        final BKInternalCallback<BadgeKeeperUserAchievement[]> callback = new BKInternalCallback<BadgeKeeperUserAchievement[]>() {
            @Override
            public void onSuccess(BadgeKeeperUserAchievement[] userAchievements) {
                instance.callback.onSuccessReceivedUserAchievements(userAchievements);
            }
        };

        // Invoke
        BadgeKeeperApi api = instance.service.getApi();
        Call<BadgeKeeperResponse<BadgeKeeperUserAchievement[]>> call = api.getUserAchievements(instance.projectId, instance.userId, instance.shouldLoadIcons);
        instance.makeRequest(callback, BadgeKeeperEventType.RequestUserAchievements, call);
    }

    /**
     * Sets a new value for specified key.
     * @param value - New value to set. Old value will be overwritten.
     * @param key - Target key to validate achievements.
     */
    public static void preparePostKeyWithValue(final String key, final double value) {
        instance.prepareKeyWithValue(key, value, instance.postVariables);
    }

    /**
     * Sets a new value for specified key.
     * @param value - Increment old value.
     * @param key - Target key to validate achievements.
     */
    public static void prepareIncrementKeyWithValue(final String key, final double value) {
        instance.prepareKeyWithValue(key, value, instance.incrementVariables);
    }

    /**
     * Sends all prepared values to server to overwrite them and validate achievements completion.
     * Before sending values must be prepared via <tt>preparePostValue:forKey:</tt> method calls.
     */
    public static void postPreparedValues() {
        postPreparedValuesForUserId(instance.userId);
    }

    /**
     * Overloaded postPreparedValues for specific user.
     * Before sending values must be prepared via <tt>prepareIncrementValue:forKey:</tt> method calls.
     * @param userId - User ID which prepared values should be sent.
     *               If set to <tt>null</tt> then current active user ID will be used.
     */
    public static void postPreparedValuesForUserId(final String userId) {
        // Validation
        if (!instance.isParametersValid()) {
            return;
        }

        List<BadgeKeeperPair<String, Double>> values = new ArrayList<>(instance.postVariables.get(userId));
        if (values != null && !values.isEmpty()) {
            final BKInternalCallback<BadgeKeeperUnlockedAchievement[]> callback = new BKInternalCallback<BadgeKeeperUnlockedAchievement[]>() {
                @Override
                public void onSuccess(BadgeKeeperUnlockedAchievement[] unlockedUserAchievements) {
                    instance.saveUnlockedAchievementRewardsForUser(userId, unlockedUserAchievements);
                    instance.callback.onSuccessReceivedUnlockedUserAchievements(unlockedUserAchievements);
                }
            };

            // Invoke
            BadgeKeeperApi api = instance.service.getApi();
            Call<BadgeKeeperResponse<BadgeKeeperUnlockedAchievement[]>> call = api.postUserVariables(instance.projectId, instance.userId, values);
            instance.makeRequest(callback, BadgeKeeperEventType.RequestPostingUserVariables, call);
            instance.postVariables.get(userId).clear();
        }
    }

    /**
     * Sends all prepared values to server to increment them and validate achievements completion.
     * Before sending values must be prepared via <tt>prepareIncrementValue:forKey:</tt> method calls.
     * After successful sending all prepared values will be removed from memory.
     */
    public static void incrementPreparedValues() {
        incrementPreparedValuesForUserId(instance.userId);
    }

    /**
     * Overloaded incrementPreparedValues for specific user.
     * Before sending values must be prepared via <tt>prepareIncrementValue:forKey:</tt> method calls.
     * After successful sending all prepared values will be removed from memory.
     * @param userId - User ID which prepared values should be sent.
     *               If set to <tt>null</tt> then current active user ID will be used.
     */
     public static void incrementPreparedValuesForUserId(final String userId) {
         // Validation
         if (!instance.isParametersValid()) {
             return;
         }

         List<BadgeKeeperPair<String, Double>> values = new ArrayList<>(instance.incrementVariables.get(userId));
         if (values != null && !values.isEmpty()) {
             final BKInternalCallback<BadgeKeeperUnlockedAchievement[]> callback = new BKInternalCallback<BadgeKeeperUnlockedAchievement[]>() {
                 @Override
                 public void onSuccess(BadgeKeeperUnlockedAchievement[] unlockedUserAchievements) {
                     instance.saveUnlockedAchievementRewardsForUser(userId, unlockedUserAchievements);
                     instance.callback.onSuccessReceivedUnlockedUserAchievements(unlockedUserAchievements);
                 }
             };

             // Invoke
             BadgeKeeperApi api = instance.service.getApi();
             Call<BadgeKeeperResponse<BadgeKeeperUnlockedAchievement[]>> call = api.incrementUserVariables(instance.projectId, instance.userId, values);
             instance.makeRequest(callback, BadgeKeeperEventType.RequestIncrementingUserVariables, call);
             instance.incrementVariables.get(userId).clear();
         }
     }

    /**
     * Try to read reward value from storage.
     * @param name - Which value to read.
     * @return - true (if parameter successfully taken), false (otherwise).
     */
    public static BadgeKeeperReward[] readRewardsForName(final String name) {
        return readRewardsForName(instance.userId, name);
    }

    /**
     * Overloaded readRewardValuesForName for specific user.
     * @param userId - Which user rewards should read.
     * @param name - Which value to read.
     * @return - true (if parameter successfully taken), false (otherwise).
     */
    public static BadgeKeeperReward[] readRewardsForName(final String userId, final String name) {
        if (instance.context == null || userId == null || userId.isEmpty() || name == null || name.isEmpty()) {
            return null;
        }

        BadgeKeeperReward[] result = instance.storage.readRewardForUserWithName(instance.context, userId, name);
        return result;
    }

    ///**
    // * Build image with raw icon data.
    // * @param iconString - raw icon data from BadgeKeeper service for UnlockedIcon or LockedIcon.
    // * @return - image (if data exist), null (otherwise).
    // */
    //public Image buildImageWithIconString(String iconString) {
    //    return null;
    //}

    private <Type> void makeRequest(final BKInternalCallback callback, final BadgeKeeperEventType eventType, Call<BadgeKeeperResponse<Type>> call) {

        call.enqueue(new Callback<BadgeKeeperResponse<Type>>() {
            @Override
            public void onResponse(Response<BadgeKeeperResponse<Type>> response, Retrofit retrofit) {
                if (instance.callback != null) {
                    BadgeKeeperResponse<Type> bkResponse = response.body();
                    BadgeKeeperResponseError error = bkResponse.getError();
                    if (error != null) {
                        instance.callback.onErrorReceived(eventType, error.getCode(), error.getMessage());
                    }
                    else {
                        Type typeResult = bkResponse.getResult();
                        callback.onSuccess(typeResult);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                instance.callback.onErrorReceived(eventType, -1, t.getLocalizedMessage());
            }
        });
    }

    private void saveUnlockedAchievementRewardsForUser(String userId, BadgeKeeperUnlockedAchievement[] achievements) {
        if (achievements != null) {
            for (BadgeKeeperUnlockedAchievement achievement : achievements) {
                BadgeKeeperReward[] rewards = achievement.getRewards();
                if (rewards != null) {
                    for (BadgeKeeperReward reward : rewards) {
                        storage.saveRewardForUser(this.context, userId, reward.getName(), reward.getValue());
                    }
                }
            }
        }
    }

    private void prepareKeyWithValue(String key, Double value, Map<String, List<BadgeKeeperPair<String, Double>>> dictionary) {
        if (!dictionary.containsKey(userId)) {
            dictionary.put(userId, new ArrayList<BadgeKeeperPair<String, Double>>());
        }

        BadgeKeeperPair<String, Double> pair = new BadgeKeeperPair<>(key, value);
        dictionary.get(userId).add(pair);
    }

    // Configuration validation
    private boolean isParametersValid() {
        assert (this.userId != null && !this.userId.isEmpty());
        if (!isProjectParametersValid() || this.userId == null || this.userId.isEmpty()) {
            Log.e("BadgeKeeper", "Check userId configuration and try again.");
            return false;
        }
        return true;
    }

    private boolean isProjectParametersValid() {
        assert (this.callback != null);
        assert (this.projectId != null && !this.projectId.isEmpty());
        assert (this.context != null);

        if (this.context == null || this.callback == null || this.projectId == null || this.projectId.isEmpty()) {
            Log.e("BadgeKeeper", "Check context/callback/projectId configuration and try again.");
            return false;
        }
        return true;
    }
}
