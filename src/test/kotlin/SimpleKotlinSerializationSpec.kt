import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf

@ExperimentalSerializationApi
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
        Unexpected JSON token at offset 43: Failed to parse type 'double' for input 'null' at path: ${'$'}.rating
        JSON input: {"title":"foo","director":"x","rating":null}
        """.trimIndent()
    }

    test("역직렬화시 널을 허용하지 않는 속성에 널이 입력되면 예외가 발생해요") {
        val exception = shouldThrow<SerializationException> {
            Json.decodeFromString<Movie>("""{"title":"foo","rating":0.1}""")
        }

        exception::class.simpleName shouldBe "MissingFieldException"
        exception.message shouldBe """
        Field 'director' is required for type with serial name 'Movie', but it was missing at path: ${'$'}
        """.trimIndent()
    }

    test("역직렬화시 기본 인자가 설정된 속성을 지원해요") {
        val deserialized = Json.decodeFromString<Movie>("""{"title":"foo","director":"x"}""")

        deserialized.rating shouldBe 1.0
    }

    test("컴파일 타임에 직렬화 지원 여부를 확인해요") {
        // @Serializable 애노테이션을 제거하면 컴파일 오류가 발생해요: Serializer has not been found for type 'User'
        @Serializable
        data class User(val userName: String)

        @Serializable
        data class Project(
            val name: String,
            val owner: User,
            val language: String = "Kotlin"
        )
    }

    test("객체를 CBOR 형식으로 직렬화 또는 역직렬화하기") {
        val data = Movie("foo", "x", 0.1)

        val serialized = Cbor.encodeToByteArray(data)
        serialized.toAsciiHexString() shouldBe "{BF}etitlecfoohdirectoraxfrating{FB}?{B9}{99}{99}{99}{99}{99}{9A}{FF}"

        val deserialized = Cbor.decodeFromByteArray<Movie>(serialized)
        deserialized shouldBe data
    }

    test("객체를 ProtoBuf 형식으로 직렬화 또는 역직렬화하기") {
        val data = Movie("foo", "x", 0.1)

        val serialized = ProtoBuf.encodeToByteArray(data)
        serialized.toAsciiHexString() shouldBe "{0A}{03}foo{12}{01}x{19}{9A}{99}{99}{99}{99}{99}{B9}?"

        val deserialized = ProtoBuf.decodeFromByteArray<Movie>(serialized)
        deserialized shouldBe data
    }
})

fun ByteArray.toAsciiHexString() = joinToString("") {
    if (it in 32..127) it.toInt().toChar().toString()
    else "{${it.toUByte().toString(16).padStart(2, '0').uppercase()}}"
}
