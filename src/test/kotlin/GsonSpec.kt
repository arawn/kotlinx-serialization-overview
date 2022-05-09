import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GsonSpec : FunSpec({

    val gson = Gson()

    test("객체를 JSON으로 직렬화 또는 역직렬화하기") {
        val data = Movie("foo", "x", 0.1)

        val serialized = gson.toJson(data)
        serialized shouldBe """{"title":"foo","director":"x","rating":0.1}"""

        // val deserialized = gson.fromJson(serialized, Movie::class.java)
        // val deserialized = gson.fromJson<Movie>(serialized, object : TypeToken<Movie>() {}.type)
        val deserialized = gson.fromJson<Movie>(serialized)
        deserialized shouldBe data
    }

    test("리스트를 JSON으로 직렬화 또는 역직렬화하기") {
        val data = listOf(Movie("foo", "x", 0.1), Movie("bar", "y", 9.9))

        val serialized = gson.toJson(data)
        serialized shouldBe """[{"title":"foo","director":"x","rating":0.1},{"title":"bar","director":"y","rating":9.9}]"""

        // val javaType = object : TypeToken<List<Movie>>() {}.type
        // val deserialized = gson.fromJson<List<Movie>>(serialized, javaType)
        val deserialized = gson.fromJson<List<Movie>>(serialized)
        deserialized shouldBe data
    }

    test("역직렬화시 널을 허용하지 않는 속성에 널을 입력해도 예외가 발생하지 않아요") {
        val deserialized = gson.fromJson<Movie>("""{"title":"foo","rating":null}""")

        deserialized.title shouldBe "foo"
        deserialized.director shouldBe null
        deserialized.rating shouldBe 0.0
    }
})

internal inline fun <reified T> Gson.fromJson(
    jsonString: String
): T = fromJson(jsonString, object : TypeToken<T>() {}.type)
