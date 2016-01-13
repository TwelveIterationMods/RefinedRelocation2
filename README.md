# Refined Relocation 2

Minecraft Mod. Adds sorting networks, filter systems, block extenders and more.

## Useful Links
* [Latest Builds](http://jenkins.blay09.net/) on my Jenkins
* [@BlayTheNinth](https://twitter.com/BlayTheNinth) on Twitter

## API

Refined Relocation 2 is still in very early development, so implementing the API in your mod would be a pretty dumb idea.
However, if you'd like to put what's there already into your dev environment, you can do so by following these steps.

1. Register Refined Relocation's maven repository by adding the following lines to your _build.gradle_:

```
repositories {
    maven {
        url = "http://repo.blay09.net"
    }
}
```

2. Then, add a dependency to either just the <s>Refined Relocation API (api)</s> or, if you want Refined Relocation to be available while testing as well, the deobfuscated version (dev):

```
dependencies {
    compile 'net.blay09.mods:refinedrelocation2:major.minor.build:dev'
}
```

Make sure you enter the correct version number for the Minecraft version you're developing for. The major version is the important part here, it is increased for every Minecraft update. See the [Jenkins](http://jenkins.blay09.net/) to find out the latest version number.

3. Done! Run `gradlew` to update your project and you'll be good to go.

The latest <s>Refined Relocation API</s> and an unobfuscated version of the mod can also be downloaded from my [Jenkins](http://jenkins.blay09.net/), if you're not into all that Maven stuff.