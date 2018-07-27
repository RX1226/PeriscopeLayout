<h1 align="center">AppUpdater </h1>

<p align="center">
  <a target="_blank" href="https://www.paypal.me/RX1226" title="Donate using PayPal"><img src="https://img.shields.io/badge/paypal-donate-yellow.svg" /></a>
</p>

This module is reference
<a href="https://github.com/javiersantos/AppUpdater">javiersantos's AppUpdater</a>

## How to use
1. Add the JitPack repository to your build file:
```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
2. Add the dependency:
```
    dependencies {
        implementation 'com.github.RX1226:PeriscopeLayout:1.0.0'
    }
```
## Usage
Add layout in xml


            <com.github.rx1226.periscopelayout.PeriscopeLayout
                android:id="@+id/periscope"
                android:layout_width="match_parent"
                android:layout_height="match_parent" />

## Usage
Use addHeart to create fly icon

        final PeriscopeLayout periscopeLayout = findViewById(R.id.periscope);
        periscopeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periscopeLayout.addHeart();
            }
        });

Use setDrawables to set different icons

        // Set customer icon
        periscopeLayout.setDrawables(getResources().getDrawable(R.mipmap.ic_launcher),
                getResources().getDrawable(R.mipmap.ic_launcher_round));

Use setPosition to set start position

        periscopeLayout.setPosition(Position.LEFT);

Use startAuto to auto start

        // auto play
        periscopeLayout.startAuto();

## License
	Copyright 2018 RX1226

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
