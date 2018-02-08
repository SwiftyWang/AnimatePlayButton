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
        first.setPlayListener(object : AnimatePlayButton.OnButtonsListener {
            override fun onPlayClick(playButton: View): Boolean {
                Toast.makeText(this@MainActivity, "button 1 play", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onPauseClick(pause: View): Boolean {
                Toast.makeText(this@MainActivity, "button 1 pause", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onResumeClick(pause: View): Boolean {
                Toast.makeText(this@MainActivity, "button 1 resume", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onStopClick(stop: View): Boolean {
                Toast.makeText(this@MainActivity, "button 1 stop", Toast.LENGTH_SHORT).show()
                return true
            }
        })
        second.setPlayListener(object : AnimatePlayButton.OnButtonsListener {
            override fun onPlayClick(playButton: View): Boolean {
                Toast.makeText(this@MainActivity, "button 2 play", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onPauseClick(pause: View): Boolean {
                Toast.makeText(this@MainActivity, "button 2 pause", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onResumeClick(pause: View): Boolean {
                Toast.makeText(this@MainActivity, "button 2 resume", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onStopClick(stop: View): Boolean {
                Toast.makeText(this@MainActivity, "button 2 stop", Toast.LENGTH_SHORT).show()
                return true
            }
        })
    }
}
