import Movies.MovieDetail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MoviesSpec : FunSpec({

    test("JSON 데이터로 MovieDetail 객체를 얻을 수 있어요") {
        val movie = Movies.fromJson("""{"title":"foo","director":"x","rating":0.1}""")

        movie shouldBe MovieDetail(
            title = "foo",
            director = "x",
            rating = 0.1
        )
    }

    test("JSON 배열 데이터로 MovieDetail 목록을 얻을 수 있어요") {
        val movies = Movies.fromJsonArray(
            Json.encodeToString(
                listOf(
                    Movie("foo", "x", 0.1),
                    Movie("bar", "y", 9.9)
                )
            )
        )

        movies shouldBe arrayOf(
            MovieDetail("foo", "x", 0.1),
            MovieDetail("bar", "y", 9.9)
        )
    }
})
