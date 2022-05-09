import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerializationException
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

    test("역직렬화시 널을 허용하지 않는 원시타입 속성에 널을 입력되면 예외가 발생해요") {
        val exception = shouldThrow<SerializationException> {
            Json.decodeFromString<Movie>("""{"title":"foo","director":"x","rating":null}""")
        }

        exception::class.simpleName shouldBe "JsonDecodingException"
        exception.message shouldBe """
        Unexpected JSON token at offset 43: Failed to parse type 'double' for input 'null'
        JSON input: {"title":"foo","director":"x","rating":null}
        """.trimIndent()
    }

    test("역직렬화시 널을 허용하지 않는 속성에 널이 입력되면 예외가 발생해요") {
        val exception = shouldThrow<SerializationException> {
            Json.decodeFromString<Movie>("""{"title":"foo","rating":0.1}""")
        }

        exception::class.simpleName shouldBe "MissingFieldException"
        exception.message shouldBe "Field 'director' is required for type with serial name 'Movie', but it was missing"
    }
})
