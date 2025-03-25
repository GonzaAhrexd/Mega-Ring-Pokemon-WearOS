package com.poke.megaring

import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.view.animation.AlphaAnimation
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null // Audio player

    // Function to create the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        // Call the parent class function
        super.onCreate(savedInstanceState)
        // Hide the action bar
        supportActionBar?.hide()
        // Set the layout of the activity
        setContentView(R.layout.activity_main)
        // Get the image view and the root layout
        val megaButton: ImageView = findViewById(R.id.mega_button)
        val rootLayout: LinearLayout = findViewById(R.id.root_layout)

        // Load the sound file
        mediaPlayer = MediaPlayer.create(this, R.raw.mega_evolution_sound)

        // Crear un efecto de rayo encima de la imagen (se puede personalizar con una animación o imagen)
        val lightningEffect: View = View(this)
        lightningEffect.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lightningEffect.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light)) // Color de rayo
        lightningEffect.alpha = 0f // Inicialmente invisible
        rootLayout.addView(lightningEffect)

        // Configura el touch listener en la imagen para activar el evento al mantener presionado
        megaButton.setOnTouchListener { view, event ->
            when (event.action) {
              MotionEvent.ACTION_DOWN -> {
    // Cuando el usuario empieza a presionar
    // Reproducir el sonido
    mediaPlayer?.start()

    // Animación de escala (para dar un efecto visual en la imagen)
    val scaleAnimation = ScaleAnimation(
        1f, 1.15f,  // Escala de 1 a 1.15 (aumento)
        1f, 1.15f,  // Escala de 1 a 1.15 (aumento)
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f
    )
    scaleAnimation.duration = 300  // Duración de la animación en milisegundos
    scaleAnimation.repeatCount = 0  // Solo se realiza una vez
    megaButton.startAnimation(scaleAnimation)

    // Cambiar el color de la imagen a un rosado brillante gradualmente
    val colorAnimator = ValueAnimator.ofArgb(Color.TRANSPARENT, Color.parseColor("#FF66FF")) // De transparente a rosado brillante
    colorAnimator.duration = 3000 // Transición gradual de 3 segundos
    colorAnimator.addUpdateListener { animator ->
        val color = animator.animatedValue as Int
        megaButton.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
    colorAnimator.start()

    // Efecto de rayo (animación de parpadeo)
    val lightningAnimation = AlphaAnimation(0f, 1f) // Transición de invisible a visible
    lightningAnimation.duration = 300 // Duración de un parpadeo
    lightningAnimation.repeatMode = AlphaAnimation.REVERSE
    lightningAnimation.repeatCount = 1
    lightningEffect.startAnimation(lightningAnimation)

    // Ejecutar después de 4 segundos
    Handler(Looper.getMainLooper()).postDelayed({
        // Restaurar el color al rosado brillante pero más desaturado y gradual
        val resetAnimator = ValueAnimator.ofArgb(Color.parseColor("#FF66FF"), Color.TRANSPARENT)
        resetAnimator.duration = 3000 // 4 segundos para restaurar el color
        resetAnimator.addUpdateListener { valueAnimator ->
            val color = valueAnimator.animatedValue as Int
            megaButton.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
        resetAnimator.start()
    }, 3000) // Retrasar 4 segundos
}

                MotionEvent.ACTION_UP -> {
                    // Cuando el usuario deja de presionar, restaurar el color a su estado normal en 4 segundos
                   

                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Libera los recursos al destruir la actividad
    }
}
