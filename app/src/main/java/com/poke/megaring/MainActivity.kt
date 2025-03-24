package com.poke.megaring

import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.view.animation.AlphaAnimation
import android.os.Handler
import android.animation.ValueAnimator
class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Oculta la ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val megaButton: ImageView = findViewById(R.id.mega_button)
        val rootLayout: LinearLayout = findViewById(R.id.root_layout)

        // Configura el MediaPlayer con el archivo MP3
        mediaPlayer = MediaPlayer.create(this, R.raw.mega_evolution_sound) // Cambia 'sound_file' por el nombre de tu archivo MP3

        // Crear un efecto de rayo encima de la imagen (se puede personalizar con una animación o imagen)
        val lightningEffect: View = View(this)
        lightningEffect.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        lightningEffect.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light)) // Color de rayo
        lightningEffect.alpha = 0f // Inicialmente invisible
        rootLayout.addView(lightningEffect)

        // Configura el clic en la imagen
        megaButton.setOnClickListener {
            // Reproducir el sonido
            mediaPlayer?.start()

            // Animación de escala (para dar un efecto visual en la imagen)
            val scaleAnimation = ScaleAnimation(
                1f, 0.9f,  // Escala de 1 a 0.9 (reducción)
                1f, 0.9f,  // Escala de 1 a 0.9 (reducción)
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f
            )
            scaleAnimation.duration = 300  // Duración de la animación en milisegundos
            scaleAnimation.repeatCount = 1  // Número de repeticiones
            scaleAnimation.repeatMode = android.view.animation.Animation.REVERSE  // Reverso al final de la animación
            megaButton.startAnimation(scaleAnimation)

            // Cambiar el color de la imagen a morado rosado gradualmente
            val colorAnimator = ValueAnimator.ofArgb(Color.TRANSPARENT, Color.parseColor("#D500F9")) // De transparente a morado rosado
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

            // Mantener el color morado rosado durante 5 segundos
            Handler().postDelayed({
                // Restaurar el color original de la imagen
                val resetAnimator = ValueAnimator.ofArgb(Color.parseColor("#D500F9"), Color.TRANSPARENT)
                resetAnimator.duration = 2000 // 2 segundos para restaurar el color
                resetAnimator.addUpdateListener { valueAnimator ->
                    val color = valueAnimator.animatedValue as Int
                    megaButton.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                }
                resetAnimator.start()
            }, 5000) // Mantener el color por 5 segundos antes de restaurarlo
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Libera los recursos al destruir la actividad
    }
}
