package alireza.nezami.network.api_service

import alireza.nezami.model.data.VideoResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayVideoApi {

    @GET("videos/")
    suspend fun getLatestVideos(
        @Query("key") apiKey: String,
        @Query("order") order: String = "latest",
        @Query("lang") lang: String = "en",
        @Query("video_type") videoType: String = "all",
        @Query("editors_choice") editorsChoice: Boolean = false,
        @Query("safesearch") safeSearch: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): VideoResponseDto

    @GET("videos/")
    suspend fun getPopularVideos(
        @Query("key") apiKey: String,
        @Query("order") order: String = "popular",
        @Query("lang") lang: String = "en",
        @Query("video_type") videoType: String = "all",
        @Query("editors_choice") editorsChoice: Boolean = false,
        @Query("safesearch") safeSearch: Boolean = false,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): VideoResponseDto

    @GET("videos/")
    suspend fun getMovieById(
        @Query("key") apiKey: String,
        @Query("id") id: Int,
        @Query("lang") lang: String = "en",
        @Query("video_type") videoType: String = "all",
        @Query("editors_choice") editorsChoice: Boolean = false,
        @Query("safesearch") safeSearch: Boolean = false
    ): VideoResponseDto

    @GET("videos/")
    suspend fun searchMovie(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("lang") lang: String = "en",
        @Query("video_type") videoType: String = "all",
        @Query("editors_choice") editorsChoice: Boolean = false,
        @Query("safesearch") safeSearch: Boolean = false,
        @Query("order") order: String = "popular",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): VideoResponseDto
}