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

package net.badgekeeper.android.objects.achievements;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import net.badgekeeper.android.objects.BKObject;

public class BKUnlockedUserAchievement extends BKObject {

    private List<BKAchievementReward> rewards = new ArrayList<BKAchievementReward>();
    private BKUserAchievement achievement;

    public boolean initWithJson(JSONObject json) {
        this.achievement = new BKUserAchievement();
        this.achievement.initWithJson(json);
        this.rewards.clear();

        try {
            JSONArray jsonRewards = json.getJSONArray("Rewards");

            for (int i = 0; i < jsonRewards.length(); ++i) {
                JSONObject jsonReward = jsonRewards.getJSONObject(i);
                BKAchievementReward reward = new BKAchievementReward();
                if (reward.initWithJson(jsonReward)) {
                    this.rewards.add(reward);
                }
            }
        }
        catch (JSONException e) {
            return false;
        }
        return true;
    }
}
