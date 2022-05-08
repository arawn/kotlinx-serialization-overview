import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SimpleKotlinSerializationSpec : FunSpec({

    test("객체를 JSON으로 직렬화 또는 역직렬화하기") {
        val data = Movie("foo", "x", 0.1)

        val serialized = Json.encodeToString(data)
        serialized shouldBe """{"title":"foo","director":"x","rating":0.1}"""

        val deserialized = Json.decodeFromString<Movie>(serialized)
        deserialized shouldBe data
    }

    test("리스트를 JSON으로 직렬화 또는 역직렬화하기") {
        val data = listOf(Movie("foo", "x", 0.1), Movie("bar", "y", 9.9))

        val serialized = Json.encodeToString(data)
        serialized shouldBe """[{"title":"foo","director":"x","rating":0.1},{"title":"bar","director":"y","rating":9.9}]"""

        val deserialized = Json.decodeFromString<List<Movie>>(serialized)
        deserialized shouldBe data
    }
})
