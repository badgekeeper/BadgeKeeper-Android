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

package net.badgekeeper.android.network.callbacks;

import net.badgekeeper.android.objects.models.BadgeKeeperAchievement;

/**
 * Interfaces for notifying your app when response received from Badge Keeper service.
 */
public interface BadgeKeeperProjectAchievementsCallback extends BadgeKeeperErrorCallback {
    /**
     * Project achievements successfully received.
     * @param achievements - received achievements array.
     */
    public void onSuccess(BadgeKeeperAchievement[] achievements);
}
