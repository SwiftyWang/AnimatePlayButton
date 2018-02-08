# AnimatePlayButton
play button animate to pause and stop button

![Demo GIF](demo/demo.gif)

## How to use
Use it in xml
```xml
    <com.swifty.animateplaybutton.AnimatePlayButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:animationDuration="250"
        app:borderColor="@color/colorPrimary"
        app:buttonBackground="@drawable/circle_primary_bg"
        app:buttonColor="@android:color/black"
        app:buttonSize="60dp"/>

```
Add click listener in Java/Kotlin
```kotlin
        animatePlayButton.setPlayListener(object : AnimatePlayButton.OnButtonsListener {
            override fun onPlayClick(playButton: View): Boolean {
                return true
            }

            override fun onPauseClick(pause: View): Boolean {
                return true
            }

            override fun onResumeClick(pause: View): Boolean {
                return true
            }

            override fun onStopClick(stop: View): Boolean {
                return true
            }
        })
```

## Reference
| xml attribute     | description                                  | default value                   |
|-------------------|----------------------------------------------|---------------------------------|
| animationDuration | the transform animation duration             | 300(300ms)                      |
| borderColor       | the outside border color                     | @android:color/holo_blue_bright |
| buttonBackground  | the button's background(need shape drawable) | @drawable/circle_button_bg      |
| buttonColor       | the button's icon color                      | @android:color/white            |
| buttonSize        | the button's size                            | 42dp                            |
