/**
 * Copyright 2015 Badge Keeper
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.badgekeeper.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import net.badgekeeper.android.network.callbacks.BadgeKeeperAchievementsUnlockedCallback;
import net.badgekeeper.android.network.callbacks.BadgeKeeperErrorCallback;
import net.badgekeeper.android.network.callbacks.BadgeKeeperProjectAchievementsCallback;
import net.badgekeeper.android.network.callbacks.BadgeKeeperUserAchievementsCallback;
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
     * Requests all project achievements list.
     * Check that Project Id and callback configured.
     * @param callback - Callback with success or failed response
     */
    public static void getProjectAchievements(final BadgeKeeperProjectAchievementsCallback callback) {
        // Validation
        if (!instance.isProjectParametersValid()) {
            return;
        }

        final BKInternalCallback<BadgeKeeperProject> internalCallback = new BKInternalCallback<BadgeKeeperProject>() {
            @Override
            public void onSuccess(BadgeKeeperProject bkProjectInformation) {
                if (callback != null) {
                    callback.onSuccess(bkProjectInformation.getAchievements());
                }
            }
        };

        // Invoke
        BadgeKeeperApi api = instance.service.getApi();
        Call<BadgeKeeperResponse<BadgeKeeperProject>> call = api.getProjectAchievements(instance.projectId, instance.shouldLoadIcons);
        instance.makeRequest(internalCallback, callback, call);
    }

    /**
     * Get all user achievements list.
     * Check that Project Id, User Id and callback configured.
     * @param callback - Callback with success or failed response
     */
    public static void getUserAchievements(final BadgeKeeperUserAchievementsCallback callback) {
        // Validation
        if (!instance.isParametersValid()) {
            return;
        }

        final BKInternalCallback<BadgeKeeperUserAchievement[]> internalCallback = new BKInternalCallback<BadgeKeeperUserAchievement[]>() {
            @Override
            public void onSuccess(BadgeKeeperUserAchievement[] userAchievements) {
                if (callback != null) {
                    callback.onSuccess(userAchievements);
                }
            }
        };

        // Invoke
        BadgeKeeperApi api = instance.service.getApi();
        Call<BadgeKeeperResponse<BadgeKeeperUserAchievement[]>> call = api.getUserAchievements(instance.projectId, instance.userId, instance.shouldLoadIcons);
        instance.makeRequest(internalCallback, callback, call);
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
     * @param callback - Callback with success or failed response
     */
    public static void postPreparedValues(final BadgeKeeperAchievementsUnlockedCallback callback) {
        postPreparedValuesForUserId(instance.userId, callback);
    }

    /**
     * Overloaded postPreparedValues for specific user.
     * Before sending values must be prepared via <tt>prepareIncrementValue:forKey:</tt> method calls.
     * @param userId - User ID which prepared values should be sent.
     *               If set to <tt>null</tt> then current active user ID will be used.
     * @param callback - Callback with success or failed response
     */
    public static void postPreparedValuesForUserId(final String userId, final BadgeKeeperAchievementsUnlockedCallback callback) {
        // Validation
        if (!instance.isParametersValid()) {
            return;
        }

        List<BadgeKeeperPair<String, Double>> values = new ArrayList<>(instance.postVariables.get(userId));
        if (values != null && !values.isEmpty()) {
            final BKInternalCallback<BadgeKeeperUnlockedAchievement[]> internalCallback = new BKInternalCallback<BadgeKeeperUnlockedAchievement[]>() {
                @Override
                public void onSuccess(BadgeKeeperUnlockedAchievement[] unlockedUserAchievements) {
                    instance.saveUnlockedAchievementRewardsForUser(userId, unlockedUserAchievements);
                    if (callback != null) {
                        callback.onSuccess(unlockedUserAchievements);
                    }
                }
            };

            // Invoke
            BadgeKeeperApi api = instance.service.getApi();
            Call<BadgeKeeperResponse<BadgeKeeperUnlockedAchievement[]>> call = api.postUserVariables(instance.projectId, instance.userId, values);
            instance.makeRequest(internalCallback, callback, call);
            instance.postVariables.get(userId).clear();
        }
    }

    /**
     * Sends all prepared values to server to increment them and validate achievements completion.
     * Before sending values must be prepared via <tt>prepareIncrementValue:forKey:</tt> method calls.
     * After successful sending all prepared values will be removed from memory.
     * @param callback - Callback with success or failed response
     */
    public static void incrementPreparedValues(final BadgeKeeperAchievementsUnlockedCallback callback) {
        incrementPreparedValuesForUserId(instance.userId, callback);
    }

    /**
     * Overloaded incrementPreparedValues for specific user.
     * Before sending values must be prepared via <tt>prepareIncrementValue:forKey:</tt> method calls.
     * After successful sending all prepared values will be removed from memory.
     * @param userId - User ID which prepared values should be sent.
     *               If set to <tt>null</tt> then current active user ID will be used.
     * @param callback - Callback with success or failed response
     */
     public static void incrementPreparedValuesForUserId(final String userId, final BadgeKeeperAchievementsUnlockedCallback callback) {
         // Validation
         if (!instance.isParametersValid()) {
             return;
         }

         List<BadgeKeeperPair<String, Double>> values = new ArrayList<>(instance.incrementVariables.get(userId));
         if (values != null && !values.isEmpty()) {
             final BKInternalCallback<BadgeKeeperUnlockedAchievement[]> internalCallback = new BKInternalCallback<BadgeKeeperUnlockedAchievement[]>() {
                 @Override
                 public void onSuccess(BadgeKeeperUnlockedAchievement[] unlockedUserAchievements) {
                     instance.saveUnlockedAchievementRewardsForUser(userId, unlockedUserAchievements);
                     if (callback != null) {
                         callback.onSuccess(unlockedUserAchievements);
                     }
                 }
             };

             // Invoke
             BadgeKeeperApi api = instance.service.getApi();
             Call<BadgeKeeperResponse<BadgeKeeperUnlockedAchievement[]>> call = api.incrementUserVariables(instance.projectId, instance.userId, values);
             instance.makeRequest(internalCallback, callback, call);
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

    /**
     * Build image with raw icon data.
     * @param iconString - raw icon data from BadgeKeeper service for UnlockedIcon or LockedIcon.
     * @return - Bitmap (if data exist), null (otherwise).
     */
    public static Bitmap buildImageWithIconString(String iconString) {
        Bitmap image = null;
        if (iconString != null && iconString.length() > 0) {
            byte[] decodedString = Base64.decode(iconString, Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        return image;
    }

    private <Type> void makeRequest(final BKInternalCallback<Type> callback, final BadgeKeeperErrorCallback errorCallback, Call<BadgeKeeperResponse<Type>> call) {

        call.enqueue(new Callback<BadgeKeeperResponse<Type>>() {
            @Override
            public void onResponse(Response<BadgeKeeperResponse<Type>> response, Retrofit retrofit) {
                BadgeKeeperResponse<Type> bkResponse = response.body();
                BadgeKeeperResponseError error = bkResponse.getError();
                if (error != null) {
                    if (errorCallback != null) {
                        errorCallback.onError(error.getCode(), error.getMessage());
                    }
                }
                else {
                    Type typeResult = bkResponse.getResult();
                    callback.onSuccess(typeResult);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (errorCallback != null) {
                    errorCallback.onError(-1, t.getLocalizedMessage());
                }
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
        assert (this.projectId != null && !this.projectId.isEmpty());
        assert (this.context != null);

        if (this.context == null || this.projectId == null || this.projectId.isEmpty()) {
            Log.e("BadgeKeeper", "Check context or projectId configuration and try again.");
            return false;
        }
        return true;
    }
}
