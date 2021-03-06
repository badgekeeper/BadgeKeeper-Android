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

/**
 * Interfaces for notifying your app when error response received from Badge Keeper service.
 * Just for internal use.
 */
public interface BadgeKeeperErrorCallback {
    /**
     * Something goes wrong with request.
     * @param code - error code.
     * @param message - error message.
     */
    public void onError(int code, String message);
}
