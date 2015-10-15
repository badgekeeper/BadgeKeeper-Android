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
import net.badgekeeper.android.objects.models.BKProjectAchievement;
import net.badgekeeper.android.objects.models.BKUnlockedUserAchievement;
import net.badgekeeper.android.objects.models.BKUserAchievement;

/**
 * Interface for notifying your app when response received from Badge Keeper service.
 */
public interface BadgeKeeperCallback {
    /**
     * Project achievements successfully received.
     * @param achievements - received achievements array.
     */
    void didReceiveProjectAchievements(BKProjectAchievement[] achievements);

    /**
     * Something goes wrong with receiving achievements by project.
     * @param code - error code.
     * @param message - error message.
     */
    void didReceiveErrorProjectAchievements(int code, String message);

    /**
     * User achievements successfully received.
     * @param achievements - received achievements array.
     */
    void didReceiveUserAchievements(BKUserAchievement[] achievements);

    /**
     * Something goes wrong with receiving achievements by user.
     * @param code - error code.
     * @param message - error message.
     */
    void didReceiveErrorUserAchievements(int code, String message);

    /**
     * Unlocked user achievements successfully received (if exist).
     * @param achievements - received achievements array.
     */
    void didReceiveUnlockedUserAchievements(BKUnlockedUserAchievement[] achievements);

    /**
     * Something goes wrong with posting variables for user.
     * @param code - error code.
     * @param message - error message.
     */
    void didReceiveErrorUnlockedUserAchievements(int code, String message);
}
