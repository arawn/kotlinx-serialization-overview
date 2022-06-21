# kotlinx.serialization overview

코틀린이 제공하는 직렬화 라이브러리인 [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)를 소개하고, 왜 만들었는지 그리고 어떤 특징을 가지고 있는지 공유합니다.  [슬라이드(slide)](https://www.slideshare.net/arawnkr/kotlinxserialization)는 슬라이드셰어(SlideShare)에 공개되어 있습니다.

## kotlinx.serialization

Kotlin serialization consists of a compiler plugin, that generates visitor code for serializable classes, runtime library with core serialization API and support libraries with various serialization formats.

* Supports Kotlin classes marked as @Serializable and standard collections.
* Provides JSON, Protobuf, CBOR, Hocon and Properties formats.
* Complete multiplatform support: JVM, JS and Native.

> 직렬화는 응용 프로그램에서 쓰는 데이터를 네트워크를 통해 전송하거나 DB 또는 파일에 저장 가능한 형식으로 바꾸는 프로세스다. 역직렬화는 외부 소스에서 데이터를 읽고 이를 런타임 객체로 바꾸는 반대 프로세스다. 이들은 제3자와 데이터를 교환하는 대부분의 앱에서 필수적인 부분이다. JSON 및 프로토콜 버퍼와 같은 일부 데이터 직렬화 형식은 특히 일반적이다. 언어 중립적이고 플랫폼 중립적이기 때문에 모든 현대 언어로 작성된 시스템 간에 데이터 교환이 가능하다. 코틀린에서 데이터 직렬화 도구는 별도의 라이브러리인 kotlinx.serialization에서 쓸 수 있다.
