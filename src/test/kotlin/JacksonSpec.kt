
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

@Suppress("BlockingMethodInNonBlockingContext")
class JacksonSpec : FunSpec({

    val mapper = jacksonObjectMapper()

    test("객체를 JSON으로 직렬화 또는 역직렬화하기") {
        val data = Movie("foo", "x", 0.1)

        val serialized = mapper.writeValueAsString(data)
        serialized shouldBe """{"title":"foo","director":"x","rating":0.1}"""

        // val deserialized = mapper.readValue(serialized, Movie::class.java)
        // val deserialized = mapper.readValue(serialized, object: TypeReference<Movie>() {})
        val deserialized = mapper.readValue<Movie>(serialized)
        deserialized shouldBe data
    }

    test("리스트를 JSON으로 직렬화 또는 역직렬화하기") {
        val data = listOf(Movie("foo", "x", 0.1), Movie("bar", "y", 9.9))

        val serialized = mapper.writeValueAsString(data)
        serialized shouldBe """[{"title":"foo","director":"x","rating":0.1},{"title":"bar","director":"y","rating":9.9}]"""

        // val javaType = mapper.typeFactory.constructCollectionType(List::class.java, Movie::class.java)
        // val deserialized = mapper.readValue<List<Movie>>(serialized, javaType)
        // val deserialized = mapper.readValue(serialized, object: TypeReference<List<Movie>>() {})
        val deserialized = mapper.readValue<List<Movie>>(serialized)
        deserialized shouldBe data
    }
})
