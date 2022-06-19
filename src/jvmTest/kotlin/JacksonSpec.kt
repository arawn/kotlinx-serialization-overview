import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

@Suppress("BlockingMethodInNonBlockingContext", "NAME_SHADOWING")
class JacksonSpec : FunSpec({

    val mapper = JsonMapper.builder().addModule(kotlinModule()).build()

    test("데이터 클래스를 JSON으로 직렬화 또는 역직렬화하기") {
        data class Project @JsonCreator constructor(
            @JsonProperty("name") val name: String,
            @JsonProperty("version") val version: Double
        )

        val mapper = JsonMapper.builder().build()
        val data = Project("foo", 1.0)

        val serialized = mapper.writeValueAsString(data)
        serialized shouldBe """{"name":"foo","version":1.0}"""

        val deserialized = mapper.readValue(serialized, Project::class.java)
        deserialized.name shouldBe data.name
        deserialized.version shouldBe data.version
    }

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

    test("역직렬화시 널을 허용하지 않는 원시타입 속성에 널을 입력해도 예외가 발생하지 않아요") {
        val deserialized = mapper.readValue<Movie>("""{"title":"foo","director":"x","rating":null}""")

        deserialized.rating shouldBe 0.0
    }

    test("역직렬화시 널을 허용하지 않는 속성에 널이 입력되면 예외가 발생해요") {
        val exception = shouldThrowExactly<MissingKotlinParameterException> {
            mapper.readValue<Movie>("""{"title":"foo","rating":0.1}""")
        }

        exception.parameter.name shouldBe "director"
        exception.parameter.type.classifier shouldBe String::class
    }

    test("역직렬화시 기본 인자가 설정된 속성을 지원해요") {
        val deserialized = mapper.readValue<Movie>("""{"title":"foo","director":"x"}""")

        deserialized.rating shouldBe 1.0
    }
})
