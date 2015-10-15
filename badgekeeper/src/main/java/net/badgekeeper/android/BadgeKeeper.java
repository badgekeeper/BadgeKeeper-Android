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

import java.util.List;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import net.badgekeeper.android.network.BadgeKeeperApi;
import net.badgekeeper.android.network.BadgeKeeperService;
import net.badgekeeper.android.objects.models.BKProjectAchievement;
import net.badgekeeper.android.objects.models.BKProjectInformation;
import net.badgekeeper.android.objects.models.BKResponse;
import net.badgekeeper.android.objects.models.BKResponseError;

public class BadgeKeeper {
    private static BadgeKeeper instance = new BadgeKeeper();

    public static BadgeKeeper getInstance() {
        return instance;
    }

    private String projectId;
    private String userId;
    private boolean shouldLoadIcons = false;
    private BadgeKeeperCallback callback = null;
    private BadgeKeeperService service = null;

    private BadgeKeeper() {
        service = new BadgeKeeperService();
    }

    /**
     * Setup Project Id for Badge Keeper instance. You can find Project Id in admin panel.
     * @param projectId - Project Id from Badge Keeper admin panel.
     */
    public static void setProjectId(String projectId) {
        getInstance().projectId = projectId;
    }

    /**
     * Setup User Id for Badge Keeper instance. This is client unique id in your system.
     * @param userId - Unique client Id in your system.
     */
    public static void setUserId(String userId) { getInstance().userId = userId; }

    /**
     * When sets to true load achievement icons from Badge Keeper service
     * with base64 encoded images for unloked and locked states.
     * Sets this parameter to false can reduce traffic and you will get response faster.
     * @param shouldLoadIcons - Should we request images from Badge Keeper service or not.
     */
    public static void setShouldLoadIcons(boolean shouldLoadIcons) { getInstance().shouldLoadIcons = shouldLoadIcons; }

    /**
     * Setup callback to notify system with event after Badge Keeper request.
     * @param callback - Callback in application to receive Badge Keeper notifications.
     */
    public static void setCallback(BadgeKeeperCallback callback) { getInstance().callback = callback; }

    /**
     * Requests all project achievements list.
     */
    public static void requestProjectAchievements() {
        BadgeKeeperApi api = getInstance().service.getApi();
        Call<BKResponse<BKProjectInformation>> call = api.getProjectAchievements(getInstance().projectId, false);

        call.enqueue(new Callback<BKResponse<BKProjectInformation>>() {
            @Override
            public void onResponse(Response<BKResponse<BKProjectInformation>> response, Retrofit retrofit) {
                if (getInstance().callback != null) {
                    BKResponse<BKProjectInformation> bkResponse = response.body();
                    BKResponseError error = bkResponse.getError();
                    BKProjectInformation resulsss = bkResponse.getResult();
                    int i = 0;
                    int k = 4;
                    String xa = "asdfsd" + "asdf";
                    //getInstance().callback.didReceiveProjectAchievements(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (getInstance().callback != null) {
                    getInstance().callback.didReceiveErrorProjectAchievements(t.getLocalizedMessage());
                }
            }
        });
    }

    /**
     * Requests all achievements that are unlocked by the specified user ID.
     */
    public static void requestUserAchievements() {

    }

    /**
     * Sets a new value for specified key.
     * @param value - New value to set. Old value will be overwritten.
     * @param key - Target key to validate achievements.
     */
    public void preparePostKeyWithValue(String key, double value) {

    }

    /**
     * Sets a new value for specified key.
     * @param value - Increment old value.
     * @param key - Target key to validate achievements.
     */
    public void prepareIncrementKeyWithValue(String key, double value) {

    }

    /**
     * Sends all prepared values to server to overwrite them and validate achievements completion.
     * Before sending values must be prepared via <tt>preparePostValue:forKey:</tt> method calls.
     */
    public void postPreparedValues() {

    }

    /**
     * Overloaded postPreparedValues for specific user.
     * Before sending values must be prepared via <tt>prepareIncrementValue:forKey:</tt> method calls.
     * @param userId - User ID which prepared values should be sent.
     *               If set to <tt>nil</tt> then current active user ID will be used.
     */
    public void postPreparedValuesForUserId(String userId) {

    }

    /**
     * Sends all prepared values to server to increment them and validate achievements completion.
     * Before sending values must be prepared via <tt>prepareIncrementValue:forKey:</tt> method calls.
     * After successful sending all prepared values will be removed from memory.
     */
    public void incrementPreparedValues() {

    }

    /**
     * Overloaded incrementPreparedValues for specific user.
     * Before sending values must be prepared via <tt>prepareIncrementValue:forKey:</tt> method calls.
     * After successful sending all prepared values will be removed from memory.
     * @param userId - User ID which prepared values should be sent.
     *               If set to <tt>null</tt> then current active user ID will be used.
     */
     public void incrementPreparedValuesForUserId(String userId) {

     }

    /**
     * Try to read reward value from storage.
     * @param name - Which value to read.
     * @param values (as reference) - Output array values for <tt>name</tt> parameter.
     * @return - true (if parameter successfully taken), false (otherwise).
     */
    public boolean readRewardValuesForNameWithValues(String name, List<Double> values) {
        return false;
    }

    /**
     * Overloaded readRewardValuesForName for specific user.
     * @param userId - Which user rewards should read.
     * @param name - Which value to read.
     * @param values (as reference) - Output array values for <tt>name</tt> parameter.
     * @return - true (if parameter successfully taken), false (otherwise).
     */
    public boolean readUserRewardValuesForNameWithValues(String userId, String name, List<Double> values) {
        return false;
    }

    /**
     * Build image with raw icon data.
     * @param iconString - raw icon data from BadgeKeeper service for UnlockedIcon or LockedIcon.
     * @return - image (if data exist), null (otherwise).
     */
    //public Image buildImageWithIconString(String iconString) {
    //    return null;
    //}
}
