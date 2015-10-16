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

package net.badgekeeper.android.network;

import java.util.List;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

import net.badgekeeper.android.objects.BadgeKeeperPair;
import net.badgekeeper.android.objects.models.BadgeKeeperProject;
import net.badgekeeper.android.objects.models.BadgeKeeperUnlockedAchievement;
import net.badgekeeper.android.objects.models.BadgeKeeperUserAchievement;

public interface BadgeKeeperApi {

    @GET("api/gateway/{projectId}/get")
    Call<BadgeKeeperResponse<BadgeKeeperProject>> getProjectAchievements(@Path("projectId") String projectId,
                                                                           @Query("shouldLoadIcons") boolean shouldLoadIcons);

    @GET("api/gateway/{projectId}/users/get/{userId}")
    Call<BadgeKeeperResponse<BadgeKeeperUserAchievement[]>> getUserAchievements(@Path("projectId") String projectId,
                                                                       @Path("userId") String userId,
                                                                       @Query("shouldLoadIcons") boolean shouldLoadIcons);

    @POST("api/gateway/{projectId}/users/post/{userId}")
    Call<BadgeKeeperResponse<BadgeKeeperUnlockedAchievement[]>> postUserVariables(@Path("projectId") String projectId,
                                                                             @Path("userId") String userId,
                                                                             @Body List<BadgeKeeperPair<String, Double>> values);

    @POST("api/gateway/{projectId}/users/increment/{userId}")
    Call<BadgeKeeperResponse<BadgeKeeperUnlockedAchievement[]>> incrementUserVariables(@Path("projectId") String projectId,
                                                                                  @Path("userId") String userId,
                                                                                  @Body List<BadgeKeeperPair<String, Double>> values);

}
