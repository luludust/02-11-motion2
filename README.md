# 02-11 Motion 2 - Electric Boogaloo

Code demoing motion sensors and property animation.

## Lab Instructions
The goal of this lab is to learn how to play sounds through Android using the [`SoundPool`](http://developer.android.com/reference/android/media/SoundPool.html) class, which represents a pre-loaded "pool" of sounds that can be played on demand. Specifically, you'll be implementing a "lighsaber soundboard" (as the first step towards making a motion-controlled laser-sword simulator), where you can tap on the screen in different places in order to play different sound effects.

### 1. Check out the code
Make sure you fork and clone this repository! It builds on the code we developed in lecture yesterday. You'll be working in the `SaberActivity`; read through the code to get a sense of what is provided for you!
- Note the touch event handling (like we used yesterday), as well as the Google-provided code for a full-screen app.

### 2. Create the SoundPool object
The first step is to instantiate a new `SoundPool` object. You should do this in the provided `initializeSoundPool()` helper method to keep things neat.
- You'll need to access the `SoundPool` from mutiple methods, so you should make it an instance variable.

There are two different ways of instantiating a `SoundPool` using a <a href="http://developer.android.com/reference/android/media/SoundPool.html#SoundPool(int, int, int)">constructor</a> (for API < 21) and using a [`SoundPool.Builder`](http://developer.android.com/reference/android/media/SoundPool.Builder.html) (for API >= 21, when it was introduced). Since we're targeting API 21 but supporting API 15, we need to check the system's version number to determine which method to use. You can do this with a simple if statement:
```java
if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
  //API >= 21
}
else {
  //API < 21
}
```
(Android Studio will actually produce this code as a "fix" for using the newer methods.)

Let's start with the more modern `SoundPool.Builder` class. This will work similarly to the Alert and Notification builders we've used in the past:
1. First instantiate the `Builder`. It's constructor needs no parameters
- Use `.setMaxStreams(int)` to specify the maximum number of sounds that should be played at once. 2 or 3 is a fine number for this application.
- Use `.setAudioAttributes(AudioAttributes)` to specify what kind of sounds this pool plays.
  - Of course you need an `AudioAttributes` object... which you can create with an [`AudioAttributes.Builder`](https://developer.android.com/reference/android/media/AudioAttributes.Builder.html)! The documentation has a code example you can work from.
  - When specifying the `AudioAttributes`, you should set the `contentType` as `AudioAttributes.CONTENT_TYPE_SONIFICATION` (e.g., a sound effect), and the `usage` as `AudioAttributes.USAGE_GAME` (since we're doing game-like stuff; this is used by the platform for routing or volume decisions).
  - Remember to `.build()` the attributes!
- Use `.build()` to actually build the `SoundPool`

For an API < 21, there's a lot less work to do: simply call the `SoundPool` <a href="http://developer.android.com/reference/android/media/SoundPool.html#SoundPool(int, int, int)">constructor</a>. Look at the method documentation for details on the parameters.

### 3. Load Sounds
After you've instantiated your `SoundPool`, you'll need to load the sounds into it to make them available (this is still in your `initializeSoundPool()` method). You can load sounds by calling the <a href="http://developer.android.com/reference/android/media/SoundPool.html#load(android.content.Context, int, int)">`.load()`</a> method on the `SoundPool` object.
- The version of the `.load()` method you should use takes in a Context, a resource id, and a priority (`1` works fine as a priority).
- The sound resources are stored in the `raw` resource folder. This is a folder for resources that are not processed (compiled) by Android, but are still assigned `ids` (based on the filename) so you can access them easily from the code. They are reference as `R.id.filename`
- The `.load()` method will return a "sound id" (an `int`) for the loaded sound. You'll want to save these ids, like as instance variables.

In order to play sounds, we'll need to know if they have been successfully loaded. But since loading is _asynchronous_, you'll need to specify a listener and callback to react to when "load events" occur.
- Use the `setOnLoadCompleteListener()` method to give the `SoundPool` a callback. This method can take in a `new` `SoundPool.OnLoadCompleteListener`.
- Override the listener's `onLoadComplete` to check if the sound has been successfully loaded. A successfully loaded sound has a `status` of `0`.
- You can figure out _which_ sound was loaded by comparing the given `sampleId` with the "sound ids" returned by the `.load()` calls. A 5-way `if` statement is the easiest way to handle each different sound id.
- When a sound has been loaded successfully, you'll want to save that information (e.g., in a `boolean` which needs to be accessible to other methods).

### 4. Play Sounds
Finally, you can play the sounds! You should do this in response to screen taps (e.g., in `onTouchEvent()`). You play sounds with the <a href="http://developer.android.com/reference/android/media/SoundPool.html#play(int, float, float, int, int, float)">`.play()`</a> method.
- Notice that I'm already calculating what "quadrant" of the screen is touched; you should play a different sound for each quadrant.
- Make sure to check that a particular sound is loaded (its `boolean` is `true`) before you try and play it!
- The <a href="http://developer.android.com/reference/android/media/SoundPool.html#play(int, float, float, int, int, float)">`.play()`</a> method takes a bunch of parameters: you can customize the speaker balance, the priority, whether it loops, and how fast the sound plays back. Pick sensible defaults (e.g., `1` or `0`) while you test that things work.


And that's it! You should be able to play different sounds by tapping on different parts of the screen.

#### Extensions
- Can you play the "lightsaber igniting" sound when your app first starts up?
- Can you play different sounds depending on the gesture (e.g., swipes vs. taps vs. double-taps)?
- For a fun related demo that will melt your computer, check out [https://lightsaber.withgoogle.com/](https://lightsaber.withgoogle.com/)


*Background Image from: http://wallpapercave.com/wp/xzm2LRC.jpg*.
*Sounds from: http://theforce.net/fanfilms/postproduction/soundfx/saberfx_fergo.asp*
