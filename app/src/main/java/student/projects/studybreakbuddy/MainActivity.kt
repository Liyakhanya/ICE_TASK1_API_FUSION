package student.projects.studybreakbuddy

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var tvAdvice: TextView
    private lateinit var tvFavorites: TextView
    private lateinit var ivGif: ImageView
    private lateinit var btnRandomAdvice: Button
    private lateinit var btnSearchStudy: Button
    private lateinit var btnSaveFavorite: Button

    private val favorites = mutableListOf<String>()
    private var currentAdvice = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvAdvice = findViewById(R.id.tvAdvice)
        tvFavorites = findViewById(R.id.tvFavorites)
        ivGif = findViewById(R.id.ivGif)
        btnRandomAdvice = findViewById(R.id.btnRandomAdvice)
        btnSearchStudy = findViewById(R.id.btnSearchStudy)
        btnSaveFavorite = findViewById(R.id.btnSaveFavorite)
    }

    private fun setupClickListeners() {
        // Feature 1: Get Random Advice
        btnRandomAdvice.setOnClickListener {
            getRandomAdvice()
        }

        // Feature 2: Search Study Advice
        btnSearchStudy.setOnClickListener {
            getStudyAdvice()
        }

        // Feature 3: Save Favorite
        btnSaveFavorite.setOnClickListener {
            saveCurrentAdvice()
        }
    }

    private fun getRandomAdvice() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.adviceService.getRandomAdvice()
                currentAdvice = response.slip.advice

                // Get study GIF
                val gifResponse = ApiClient.giphyService.getRandomStudyGif()
                val gifUrl = gifResponse.data.images.original.url

                withContext(Dispatchers.Main) {
                    tvAdvice.text = currentAdvice
                    Glide.with(this@MainActivity)
                        .load(gifUrl)
                        .into(ivGif)
                    Toast.makeText(this@MainActivity, "New advice loaded!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvAdvice.text = "Failed to load advice. Please check your internet connection."
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getStudyAdvice() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.adviceService.getStudyAdvice()
                val studyAdvices = response.slips

                if (studyAdvices != null && studyAdvices.isNotEmpty()) {
                    val randomAdvice = studyAdvices.random()
                    currentAdvice = randomAdvice.advice

                    // Get study GIF
                    val gifResponse = ApiClient.giphyService.getRandomStudyGif()
                    val gifUrl = gifResponse.data.images.original.url

                    withContext(Dispatchers.Main) {
                        tvAdvice.text = currentAdvice
                        Glide.with(this@MainActivity)
                            .load(gifUrl)
                            .into(ivGif)
                        Toast.makeText(this@MainActivity, "Study advice loaded!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        tvAdvice.text = "No study advice found. Try random advice instead!"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvAdvice.text = "Failed to load study advice. Please try again."
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveCurrentAdvice() {
        if (currentAdvice.isNotEmpty()) {
            favorites.add(currentAdvice)
            updateFavoritesDisplay()
            Toast.makeText(this, "Advice saved to favorites!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No advice to save. Get some advice first!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFavoritesDisplay() {
        if (favorites.isEmpty()) {
            tvFavorites.text = "No saved advice yet"
        } else {
            val favoritesText = favorites.joinToString("\n\n") { "• $it" }
            tvFavorites.text = favoritesText
        }
    }
}