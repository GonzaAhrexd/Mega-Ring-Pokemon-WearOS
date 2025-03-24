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

        // Crear una vista morada que cubra toda la pantalla
        val purpleOverlay: View = View(this)
        purpleOverlay.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        purpleOverlay.setBackgroundColor(ContextCompat.getColor(this, R.color.purple)) // Color morado
        purpleOverlay.alpha = 0f // Inicialmente invisible
        rootLayout.addView(purpleOverlay)

        // Configura el clic en la imagen
        megaButton.setOnClickListener {
            // Reproducir el sonido
            mediaPlayer?.start()

            // Efecto de animación de escala (para dar un efecto visual en la imagen)
            val scaleAnimation = ScaleAnimation(
                1f, 0.9f,  // Escala de 1 a 0.9 (reducción)
                1f, 0.9f,  // Escala de 1 a 0.9 (reducción)
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,
                android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f
            )
            scaleAnimation.duration = 200  // Duración de la animación en milisegundos
            scaleAnimation.repeatCount = 1  // Número de repeticiones
            scaleAnimation.repeatMode = android.view.animation.Animation.REVERSE  // Reverso al final de la animación
            megaButton.startAnimation(scaleAnimation)

            // Efecto de rayo (animación de parpadeo)
            val lightningAnimation = AlphaAnimation(0f, 1f) // Transición de invisible a visible
            lightningAnimation.duration = 300 // Duración de un parpadeo
            lightningAnimation.repeatMode = AlphaAnimation.REVERSE
            lightningAnimation.repeatCount = 1
            purpleOverlay.startAnimation(lightningAnimation)

            // Animación para cambiar la imagen a morado (más rosado)
            val animator = ValueAnimator.ofArgb(Color.TRANSPARENT, Color.parseColor("#D500F9")) // Morado más rosado
            animator.duration = 3000 // 3 segundos para la transición
            animator.addUpdateListener { valueAnimator ->
                val color = valueAnimator.animatedValue as Int
                megaButton.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
            animator.start()

            // Después de 3 segundos, mantener el color morado rosado por 5 segundos antes de restaurar
            Handler().postDelayed({
                val resetAnimator = ValueAnimator.ofArgb(Color.parseColor("#D500F9"), Color.TRANSPARENT) // Morado rosado a transparente
                resetAnimator.duration = 3000 // 3 segundos para restaurar la imagen
                resetAnimator.addUpdateListener { valueAnimator ->
                    val color = valueAnimator.animatedValue as Int
                    megaButton.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                }
                resetAnimator.start()
            }, 4000) // Esperamos 8 segundos (3 segundos de animación + 5 segundos de color morado)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Libera los recursos al destruir la actividad
    }
}
