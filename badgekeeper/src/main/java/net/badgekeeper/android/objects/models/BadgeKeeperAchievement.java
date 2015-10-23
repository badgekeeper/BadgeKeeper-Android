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
 * Present general element in Badge Keeper service - achievement.
 */
public class BadgeKeeperAchievement {

    private @SerializedName("DisplayName") String displayName;
    private @SerializedName("Description") String description;
    private @SerializedName("UnlockedIcon") String iconUnlocked;
    private @SerializedName("LockedIcon") String iconLocked;

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public String getIconUnlocked() { return iconUnlocked; }
    public String getIconLocked() { return iconLocked; }
}
