package alireza.nezami.network.di

import alireza.nezami.network.BuildConfig

object Config {
    const val API_KEY = BuildConfig.PIXABAY_API_KEY
    const val BACKEND_URL = "https://pixabay.com/api/"
    const val HTTP_READ_TIMEOUT_IN_SECONDS = 60
    const val HTTP_CALL_TIMEOUT_IN_SECONDS = 60

}
