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

package net.badgekeeper.android.entities;

import android.content.Context;
import io.realm.Realm;
import io.realm.RealmResults;
import net.badgekeeper.android.objects.models.BadgeKeeperReward;

/**
 * NoSQL Storage wrapper for Badge Keeper client library.
 */
public class BadgeKeeperEntityStorage {

    public void saveRewardForUser(Context context, String userId, String rewardName, double rewardValue) {
        if (userId == null || rewardName == null || userId.isEmpty() || rewardName.isEmpty()) {
            return;
        }

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        BadgeKeeperEntityReward user = realm.createObject(BadgeKeeperEntityReward.class);
        user.setUser(userId);
        user.setName(rewardName);
        user.setValue(rewardValue);

        realm.commitTransaction();
    }

    public BadgeKeeperReward[] readRewardForUserWithName(Context context, String userId, String rewardName) {
        if (userId == null || rewardName == null || userId.isEmpty() || rewardName.isEmpty()) {
            return null;
        }

        Realm realm = Realm.getInstance(context);
        RealmResults<BadgeKeeperEntityReward> userData = realm.where(BadgeKeeperEntityReward.class).equalTo("user", userId).findAll();
        RealmResults<BadgeKeeperEntityReward> rewards = userData.where().equalTo("name", rewardName).findAll();

        BadgeKeeperReward result[] = new BadgeKeeperReward[rewards.size()];

        for (int i = 0; i < rewards.size(); i++) {
            BadgeKeeperEntityReward reward = rewards.get(i);
            result[i].setName(reward.getName());
            result[i].setValue(reward.getValue());
        }
        return result;
    }
}
