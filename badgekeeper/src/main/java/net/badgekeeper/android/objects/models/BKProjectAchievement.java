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

package net.badgekeeper.android.objects.models;

//import org.json.JSONObject;
//import org.json.JSONException;
import com.google.gson.annotations.SerializedName;
//import net.badgekeeper.android.objects.BKObject;
/**
 * Present general element in Badge Keeper service - achievement.
 */
public class BKProjectAchievement {

    private @SerializedName("DisplayName") String displayName;

    /*private String displayName;
    private String description;
    private String iconUnlocked;
    private String iconLocked;

    public boolean initWithJson(JSONObject json) {
        try {
            this.displayName = json.getString("DisplayName");
            this.description = json.getString("Description");
            this.iconUnlocked = json.getString("UnlockedIcon");
            this.iconLocked = json.getString("LockedIcon");
        }
        catch (JSONException e) {
            return false;
        }
        return true;
    }*/

    public String getDisplayName() { return displayName; }
    //public String getDescription() { return description; }
    //public String getIconUnlocked() { return iconUnlocked; }
    //public String getIconLocked() { return iconLocked; }
}