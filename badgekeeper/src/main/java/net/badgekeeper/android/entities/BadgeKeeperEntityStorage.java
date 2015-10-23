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
