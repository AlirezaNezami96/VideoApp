package alireza.nezami.network.model

enum class VideoLanguage(val code: String) {
    CZECH("cs"),
    DANISH("da"),
    GERMAN("de"),
    ENGLISH("en"),
    SPANISH("es"),
    FRENCH("fr"),
    INDONESIAN("id"),
    ITALIAN("it"),
    HUNGARIAN("hu"),
    DUTCH("nl"),
    NORWEGIAN("no"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    ROMANIAN("ro"),
    SLOVAK("sk"),
    FINNISH("fi"),
    SWEDISH("sv"),
    TURKISH("tr"),
    VIETNAMESE("vi"),
    THAI("th"),
    BULGARIAN("bg"),
    RUSSIAN("ru"),
    GREEK("el"),
    JAPANESE("ja"),
    KOREAN("ko"),
    CHINESE("zh");

    companion object {
        val DEFAULT = ENGLISH
    }
}

enum class VideoType(val value: String) {
    ALL("all"),
    FILM("film"),
    ANIMATION("animation");

    companion object {
        val DEFAULT = ALL
    }
}

enum class VideoCategory(val value: String) {
    BACKGROUNDS("backgrounds"),
    FASHION("fashion"),
    NATURE("nature"),
    SCIENCE("science"),
    EDUCATION("education"),
    FEELINGS("feelings"),
    HEALTH("health"),
    PEOPLE("people"),
    RELIGION("religion"),
    PLACES("places"),
    ANIMALS("animals"),
    INDUSTRY("industry"),
    COMPUTER("computer"),
    FOOD("food"),
    SPORTS("sports"),
    TRANSPORTATION("transportation"),
    TRAVEL("travel"),
    BUILDINGS("buildings"),
    BUSINESS("business"),
    MUSIC("music")
}

enum class VideoOrder(val value: String) {
    POPULAR("popular"),
    LATEST("latest");

    companion object {
        val DEFAULT = POPULAR
    }
}

object ApiPageConfig {
    const val DEFAULT_PAGE = 1
    const val DEFAULT_PER_PAGE = 20
}