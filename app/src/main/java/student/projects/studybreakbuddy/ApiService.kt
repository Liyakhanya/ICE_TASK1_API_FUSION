package student.projects.studybreakbuddy

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AdviceService {
    @GET("advice")
    suspend fun getRandomAdvice(): AdviceResponse

    @GET("advice/search/study")
    suspend fun getStudyAdvice(): SearchResponse
}

interface GiphyService {
    @GET("v1/gifs/random")
    suspend fun getRandomStudyGif(
        @Query("api_key") apiKey: String = "dc6zaTOxFJmzC", // Public beta key
        @Query("tag") tag: String = "study"
    ): GiphyResponse
}

object ApiClient {
    private const val ADVICE_BASE_URL = "https://api.adviceslip.com/"
    private const val GIPHY_BASE_URL = "https://api.giphy.com/"

    val adviceService: AdviceService by lazy {
        Retrofit.Builder()
            .baseUrl(ADVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdviceService::class.java)
    }

    val giphyService: GiphyService by lazy {
        Retrofit.Builder()
            .baseUrl(GIPHY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GiphyService::class.java)
    }
}