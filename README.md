# Badge Keeper

Badge Keeper Android library will help add achievement system to your app easily.
More information you can find here: [Badge Keeper official site](https://badgekeeper.net)

[![Build Status](https://travis-ci.org/badgekeeper/BadgeKeeper-Android.svg?branch=master)](https://travis-ci.org/badgekeeper/BadgeKeeper-iOS)
[![Version](https://img.shields.io/cocoapods/v/BadgeKeeper.svg)](http://cocoadocs.org/docsets/BadgeKeeper)
[![Platform](https://img.shields.io/cocoapods/p/BadgeKeeper.svg)](http://cocoadocs.org/docsets/BadgeKeeper)
[![License](https://img.shields.io/cocoapods/l/BadgeKeeper.svg)](http://cocoadocs.org/docsets/BadgeKeeper)

## Getting Started

To integrate Badge Keeper library make sure your project uses jCenter as a dependency repository.
Add config below to the dependencies of your project and sync project.
```
compile 'net.badgekeeper.android:badgekeeper:0.1.4'
```

## Usage

### Basic Initialization

```java
BadgeKeeper.setContext(this); // this = Context object
BadgeKeeper.setProjectId("Project Id from admin panel");
BadgeKeeper.setUserId("Your client id");
BadgeKeeper.setShouldLoadIcons(true); // default is false
```

That's all settings that need to be configured.

### Callbacks

There are four callbacks that we will use to receive results from Badge Keeper service:

```java
// 1 - Returns error code and error message if something goes wrong
public interface BadgeKeeperErrorCallback {
    public void onError(int code, String message);
}

// 2 - Returns array of BadgeKeeperAchievement elements
public interface BadgeKeeperProjectAchievementsCallback extends BadgeKeeperErrorCallback {
    public void onSuccess(BadgeKeeperAchievement[] achievements);
}

// 3 - Returns array of BadgeKeeperUserAchievement elements
public interface BadgeKeeperUserAchievementsCallback extends BadgeKeeperErrorCallback {
    public void onSuccess(BadgeKeeperUserAchievement[] achievements);
}

// 4 - Returns array of BadgeKeeperUnlockedAchievement elements
public interface BadgeKeeperAchievementsUnlockedCallback extends BadgeKeeperErrorCallback {
    public void onSuccess(BadgeKeeperUnlockedAchievement[] achievements);
}
```

### Get project achievements (no userId required)

```java
BadgeKeeper.getProjectAchievements(new BadgeKeeperProjectAchievementsCallback() {
	@Override
    public void onSuccess(BadgeKeeperAchievement[] achievements) {
		// Put logic here
	}
	
	@Override
    public void onError(int code, String message) {
    	// Put logic here
	}
});
```

### Get user achievements

```java
BadgeKeeper.getUserAchievements(new BadgeKeeperUserAchievementsCallback() {
    @Override
    public void onSuccess(BadgeKeeperUserAchievement[] achievements) {
    	// Put logic here
    }
	
	@Override
	public void onError(int code, String message) {
		// Put logic here
	}
});
```

### Post user variables and validate completed achievements

```java
BadgeKeeper.preparePostKeyWithValue("x", 0);

BadgeKeeper.postPreparedValues(new BadgeKeeperAchievementsUnlockedCallback() {
	@Override
	public void onSuccess(BadgeKeeperUnlockedAchievement[] achievements) {
		// Put logic here
	}

	@Override
	public void onError(int code, String message) {
		// Put logic here
    }
});
```

### Increment user variables and validate completed achievements

```java
BadgeKeeper.prepareIncrementKeyWithValue("x", 1);

BadgeKeeper.incrementPreparedValues(new BadgeKeeperAchievementsUnlockedCallback() {
	@Override
	public void onSuccess(BadgeKeeperUnlockedAchievement[] achievements) {
		// Put logic here
	}

	@Override
	public void onError(int code, String message) {
		// Put logic here
    }
});
```

## Requirements

* JDK version >=7.
* We support all Android versions since API Level 14 (Android 4.0, 4.0.1, 4.0.2 Ice Cream Sandwich & above).

## License

	Copyright 2015 Badge Keeper

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

    	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.