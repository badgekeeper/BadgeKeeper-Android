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

package net.badgekeeper.android.objects.models;

import com.google.gson.annotations.SerializedName;

/**
 * Contains all information about Project in Badge Keeper service.
 */
public class BadgeKeeperProject {
    private @SerializedName("Title") String title;
    private @SerializedName("Description") String description;
    private @SerializedName("Icon") String icon;
    private @SerializedName("Achievements") BadgeKeeperAchievement[] achievements;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
    public BadgeKeeperAchievement[] getAchievements() { return achievements; }
}
