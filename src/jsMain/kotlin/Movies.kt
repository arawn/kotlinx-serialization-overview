@file:JsExport

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

object Movies {

    private val json = Json

    fun fromJson(content: String) =
        json.decodeFromString(Movie.serializer(), content).let {
            MovieDetail.of(it)
        }

    fun fromJsonArray(content: String) =
        json.decodeFromString(ListSerializer(Movie.serializer()), content).map {
            MovieDetail.of(it)
        }.toTypedArray()

    data class MovieDetail(
        val title: String,
        val director: String,
        val rating: Double = 1.0
    ) {

        companion object {

            internal fun of(source: Movie) = MovieDetail(
                title = source.title,
                director = source.director,
                rating = source.rating
            )
        }
    }
}
