package com.poke.megaring

import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.view.animation.AlphaAnimation
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null // Audio player
    private val handler = Handler(Looper.getMainLooper()) // Handler for delayed execution
    private var isLongPressDetected = false // Flag to control execution
    private var longPressRunnable: Runnable? = null // Store the Runnable to cancel it if needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hide the action bar
        setContentView(R.layout.activity_main) // Set the activity layout

        val megaButton: ImageView = findViewById(R.id.mega_button) // Reference to the button image
        val rootLayout: LinearLayout = findViewById(R.id.root_layout) // Reference to the root layout

        mediaPlayer = MediaPlayer.create(this, R.raw.mega_evolution_sound) // Initialize the sound file

        // Create a lightning effect overlay (can be customized with an animation or image)
        val lightningEffect: View = View(this)
        lightningEffect.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lightningEffect.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light)) // Set lightning color
        lightningEffect.alpha = 0f // Initially invisible
        rootLayout.addView(lightningEffect) // Add to the layout

        // Set the touch listener on the image to trigger the event on long press
        megaButton.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Reset the flag and cancel any pending Runnable
                    isLongPressDetected = false
                    longPressRunnable?.let { handler.removeCallbacks(it) } // Cancel if there was one pending

                    // Create the Runnable with the code to execute after 1.2 seconds
                    longPressRunnable = Runnable {
                        isLongPressDetected = true // Mark that the long press was detected

                        // Code to execute after 1.2 seconds
                        mediaPlayer?.start() // Play the sound

                        // Scale animation (visual effect on the image)
                        val scaleAnimation = ScaleAnimation(
                            1f, 1.15f, // Scale from 1 to 1.15 (increase)
                            1f, 1.15f, // Scale from 1 to 1.15 (increase)
                            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,
                            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f
                        )
                        scaleAnimation.duration = 300 // Animation duration in milliseconds
                        scaleAnimation.repeatCount = 0 // Run once
                        megaButton.startAnimation(scaleAnimation)

                        // Gradually change the image color to bright pink
                        val colorAnimator = ValueAnimator.ofArgb(Color.TRANSPARENT, Color.parseColor("#FF66FF")) // From transparent to bright pink
                        colorAnimator.duration = 3000 // 3-second gradual transition
                        colorAnimator.addUpdateListener { animator ->
                            val color = animator.animatedValue as Int
                            megaButton.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                        }
                        colorAnimator.start()

                        // Lightning effect (blinking animation)
                        val lightningAnimation = AlphaAnimation(0f, 1f) // Fade from invisible to visible
                        lightningAnimation.duration = 300 // Duration of one blink
                        lightningAnimation.repeatMode = AlphaAnimation.REVERSE
                        lightningAnimation.repeatCount = 1
                        lightningEffect.startAnimation(lightningAnimation)

                        // Execute after 3 seconds
                        handler.postDelayed({
                            // Gradually restore the color from bright pink to transparent
                            val resetAnimator = ValueAnimator.ofArgb(Color.parseColor("#FF66FF"), Color.TRANSPARENT)
                            resetAnimator.duration = 3000 // 3 seconds to restore color
                            resetAnimator.addUpdateListener { valueAnimator ->
                                val color = valueAnimator.animatedValue as Int
                                megaButton.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                            }
                            resetAnimator.start()
                        }, 3000) // Delay by 3 seconds
                    }

                    // Schedule the execution after 1.2 seconds
                    handler.postDelayed(longPressRunnable!!, 1200)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    // If released before 1.2 seconds, cancel the Runnable
                    if (!isLongPressDetected) {
                        longPressRunnable?.let { handler.removeCallbacks(it) }
                    }
                    true
                }

                MotionEvent.ACTION_CANCEL -> {
                    // Cancel if the event is interrupted
                    longPressRunnable?.let { handler.removeCallbacks(it) }
                    false
                }

                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Release resources when the activity is destroyed
    }
}