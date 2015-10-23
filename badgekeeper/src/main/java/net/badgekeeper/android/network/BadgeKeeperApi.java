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
