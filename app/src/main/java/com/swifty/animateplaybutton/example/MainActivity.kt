package com.swifty.animateplaybutton.example

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.swifty.animateplaybutton.AnimatePlayButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        first.setPlayListener(object : AnimatePlayButton.PlayListener {
            override fun onPlay(playButton: View) {
                Toast.makeText(this@MainActivity, "button 1 play, trigger 2 play", Toast.LENGTH_SHORT).show()
                second.updateStatus(AnimatePlayButton.Status.PLAYED)
            }

            override fun onPause(pause: View) {
                Toast.makeText(this@MainActivity, "button 1 pause, trigger 2 pause", Toast.LENGTH_SHORT).show()
                second.updateStatus(AnimatePlayButton.Status.PAUSED)
            }

            override fun onResume(pause: View) {
                Toast.makeText(this@MainActivity, "button 1 resume, trigger 2 resume", Toast.LENGTH_SHORT).show()
                second.updateStatus(AnimatePlayButton.Status.PLAYED)
            }

            override fun onStop(stop: View) {
                Toast.makeText(this@MainActivity, "button 1 stop, trigger 2 stop", Toast.LENGTH_SHORT).show()
                second.updateStatus(AnimatePlayButton.Status.STOPPED)
            }
        })
        second.setPlayListener(object : AnimatePlayButton.PlayListener {
            override fun onPlay(playButton: View) {
                Toast.makeText(this@MainActivity, "button 2 play", Toast.LENGTH_SHORT).show()
            }

            override fun onPause(pause: View) {
                Toast.makeText(this@MainActivity, "button 2 pause", Toast.LENGTH_SHORT).show()
            }

            override fun onResume(pause: View) {
                Toast.makeText(this@MainActivity, "button 2 resume", Toast.LENGTH_SHORT).show()
            }

            override fun onStop(stop: View) {
                Toast.makeText(this@MainActivity, "button 2 stop", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
