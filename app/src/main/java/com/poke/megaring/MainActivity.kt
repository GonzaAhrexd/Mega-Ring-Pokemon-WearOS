package com.poke.megaring

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
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

            // Cambiar el color de la imagen (efecto de filtro de color)
            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f) // Hacer la imagen en blanco y negro
            val filter = ColorMatrixColorFilter(colorMatrix)
            megaButton.colorFilter = filter

            // Efecto de rayo (animación de parpadeo)
            val lightningAnimation = AlphaAnimation(0f, 1f) // Transición de invisible a visible
            lightningAnimation.duration = 300 // Duración de un parpadeo
            lightningAnimation.repeatMode = AlphaAnimation.REVERSE
            lightningAnimation.repeatCount = 1
            lightningEffect.startAnimation(lightningAnimation)

            // Después de un corto tiempo, restaurar el color de la imagen
            megaButton.postDelayed({
                megaButton.colorFilter = null // Restaurar el color original
            }, 500)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Libera los recursos al destruir la actividad
    }
}
