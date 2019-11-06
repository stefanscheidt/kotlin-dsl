theme: Simple, 1
background-color: #FFFFFF
text: #777777, alignment(left), line-height(1), text-scale(1.0), Titillium Web
header: #25c5dc, alignment(left), line-height(1), text-scale(1.0), Titillium Web
link: #25c5dc, alignment(left), line-height(1), text-scale(1.0), Titillium Web
autoscale: true

[.header: #000000, alignment(center), line-height(1), text-scale(1.0), Titillium Web]
![original, fit](img/REWE-Digital-Titelbild.png)

# Domain Specific Languages in Kotlin
## Theory and Practice
### Stefan Scheidt, REWE digital

---

# Domain Specific Languages

> A domain-specific language (DSL) is a computer language specialized to a particular application domain.
-- [Wikipedia](https://en.wikipedia.org/wiki/Domain-specific_language)

---

# Examples

- HTML
- SQL
- Regular Expressions
- ...

---

# Embedded DSLs

> Embedded (or internal) DSLs are implemented as libraries which exploit the syntax of their host general purpose language
-- [Wikipedia](https://en.wikipedia.org/wiki/Domain-specific_language#Usage_patterns)

---

[.text: #777777, alignment(center), line-height(1), text-scale(1.2), Titillium Web]

# Kotlin DSLs

Domain-specific languages embedded in Kotlin

---

# Examples - HTML [^1]

```kotlin
System.out.appendHTML().html {
    body {
        div {
            a("http://kotlinlang.org") {
                target = ATarget.blank
                +"Main site"
            }
        }
    }
}
```

[^1]: kotlinx.html, <https://github.com/Kotlin/kotlinx.html>

---

# Examples - JSON [^2]

```kotlin
import com.github.salomonbrys.kotson.*

val obj: JsonObject = jsonObject(
    "property" to "value",
    "array" to jsonArray(
        21,
        "fourty-two",
        jsonObject("go" to "hell")
    )
)
```

[^2]: Kotson, <https://github.com/SalomonBrys/Kotson>

---

# Examples - Gradle [^3]

```kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
}
repositories {
    mavenCentral()
}
dependencies {
    compile(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
```

[^3]: Kotlin Gradle DSL, <https://docs.gradle.org/current/userguide/kotlin_dsl.html>

---

# Examples - Dependency Injection for Android [^4]

```kotlin
fun MainModule(activity: MainActivity) = Module {

    singleton<ButtonMapper> { ButtonMapperImpl(get(ACTIVITY_CONTEXT)) }

    singleton<ButtonRepository> { ButtonRepositoryImpl() }

    singleton<MainView> { activity }

    singleton<MainPresenter> { MainPresenterImpl(get(), get(), get()) }

    singleton<MainInteractor> { MainInteractorImpl(get(), get(), get()) }

    singleton<MainNavigator> { MainNavigatorImpl(get(ACTIVITY)) }
}
```

[^4]: Katana, <https://github.com/rewe-digital/katana>

---

# Examples - Android Layouts [^5]

```kotlin
verticalLayout {
  val name = editText {
    hint = "Name"
    textSize = 24f
  }
  button("Say Hello") {
    onClick { 
      toast("Hello, ${name.text}!")
    }
  }
}
```

[^5]: Kotlin Anko Layouts, <https://github.com/Kotlin/anko/wiki/Anko-Layouts>

---

# Example - Spring Web Router Config [^6]

```kotlin
@Bean
fun mainRouter(userHandler: UserHandler) = router {
    accept(TEXT_HTML).nest {
        GET("/") { ok().render("index") }
        GET("/sse") { ok().render("sse") }
        GET("/users", userHandler::findAllView)
    }
    "/api".nest {
        accept(APPLICATION_JSON).nest {
            GET("/users", userHandler::findAll)
        }
        accept(TEXT_EVENT_STREAM).nest {
            GET("/users", userHandler::stream)
        }
    }
    resources("/**", ClassPathResource("static/"))
}
```

[^6]: Spring Framework, [https://docs.spring.io/spring-framework](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/languages.html#kotlin-web)

---

# Example - Spring Mock MVC DSL [^7]

```kotlin
mockMvc.get("/person/{name}", "Lee") {
    secure = true
    accept = APPLICATION_JSON
    headers {
        contentLanguage = Locale.FRANCE
    }
    principal = Principal { "foo" }
}.andExpect {
    status { isOk }
    content { contentType(APPLICATION_JSON) }
    jsonPath("$.name") { value("Lee") }
    content { json("""{"someBoolean": false}""", false) }
}.andDo {
    print()
}
```

[^7]: Spring Framework, [https://docs.spring.io/spring-framework](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/languages.html#mockmvc-dsl)

---

[.header: #FFFFFF, alignment(center), line-height(1), text-scale(1.4), Titillium Web]
![original, fit](img/Zutaten.png)

# __Build your own DSL__

## __Ingredients__

---

# Extention functions

Instead of this

```kotlin
fun <T> swap(list: MutableList<T>, index1: Int, index2: Int) {
    val tmp = list[index1]
    list[index1] = list[index2]
    list[index2] = tmp
}
val list = mutableListOf(1, 2, 3)
swap(list, 1, 2)
```

---

# Extention functions

... we can write this

```kotlin
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
}
val list = mutableListOf(1, 2, 3)
list.swap(1,2)
```

---

# Lambda Expressions with Receivers

So what does this do?

```kotlin
    val printMe: String.() -> Unit = { println(this) }

    "Stefan".printMe()
```

---

# Lambda Expressions with Receivers

Now let's assume we have a class `HTML`

```kotlin
class HTML {
    fun head() {}
    fun body() {}
}
```

---

# Lambda Expressions with Receivers

Now let's assume we have a class `HTML`

```kotlin
class HTML {
    fun head() {}
    fun body() {}
}
```

and a function `html`

```kotlin
fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()
    html.init()
    return html
}
```

---

# Lambda Expressions with Receivers

... then we can write

```kotlin
html({
    head()
    body()   
})
```

---

# Lambda Expressions with Receivers

We apply syntactic sugar:

```kotlin
html() {
    head()
    body()   
}
```

---

# Lambda Expressions with Receivers

And again:

```kotlin
html {
    head()
    body()   
}
```

---

# Lambda Expressions with Receivers

Now let's go from here

```kotlin
class HTML {
    fun head() {}
    fun body() {}
}
```

---

# Lambda Expressions with Receivers

... to here:

```kotlin
class HTML {
    fun head(init: Head.() -> Unit): Head {
        val head = Head(); head.init(); return head
    }
    fun body(init: Body.() -> Unit): Body {
        val body = Body(); body.init(); return body
    }
}

class Head { fun title() { } }

class Body { fun p() { } }
```

---

# Lambda Expressions with Receivers

Then we can write

```kotlin
html {
    head {
        title()
    }
    body {
        p()
    }
}
```

... and so on.

---

# Scope control

But ...

```kotlin
html {
    head {
        head {
            title()
        }
    }
    // ...
}
```

will also be OK. :-(

---

# DSL Markers

Thus let's control receiver scope with `@DslMarker`:

```kotlin
@DslMarker
annotation class HtmlTagMarker

@HtmlTagMarker
class HTML { // ...
}

@HtmlTagMarker
class Head { // ...
}

@HtmlTagMarker
class Body { // ...
}
```

---

# DSL Markers

Now we will get

```
$ ./gradlew compileKotlin 
e: .../html.kt: (32, 13): 'fun head(init: Head.() -> Unit):
  Head' can't be called in this context by implicit receiver.
  Use the explicit one if necessary
> Task :compileKotlin FAILED
```

---

# DSL Markers

Well, we still could write

```kotlin
html {
    head {
        this@html.head {
            title()
        }
    }
    // ...
}
```

---

# Operator Overloading

We would like to write

```kotlin
body {
    p { +"Hello, World!" }
}
```

to add a text element to a paragraph.

---

# Operator Overloading

To do this we overload unitary plus:

```kotlin
abstract class TagWithText(name: String): Tag(name) {
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }
}
// surrounding stuff left out 
```

See also [Operator Overloading](https://kotlinlang.org/docs/reference/operator-overloading.html)

---

# The whole thing ...

The whole example could be found here: [Type-Safe builders](https://kotlinlang.org/docs/reference/type-safe-builders.html).

---

# More Ingredients: Invoke

With

```kotlin
class MyClass {
    operator fun invoke() { // ... 
    }
}
```

one can write

```kotlin
val myObj = MyClass()
myObj() // will call invoke
```

---

# Invoke for DSLs

With

```kotlin
class Family {
    operator fun invoke(body: Family.() -> Unit) { body() }
    fun addMember(name: String) {}
}
```

one can write

```kotlin
val family = Family()
family {
    addMember("Mom"); addMember("Dad"); addMember("Kid")
}
```

---

# More Examples for DSLs

*   <https://dzone.com/articles/kotlin-dsl-from-theory-to-practice>
    convert Test Data Builder to Kotlin DSL
*   <https://kotlinexpertise.com/create-dsl-with-kotlin/>
    create a DSL for setting up a TLS connection
*   <https://kotlinexpertise.com/java-builders-kotlin-dsls/>
    convert Java builders for Android Material Drawer to Kotlin DSL
*   <https://blog.codecentric.de/2018/06/kotlin-dsl-apache-kafka/>
    A simple example of Kotlin DSL for Apache Kafka producer and consumer (German)

---

[.header: #000000, alignment(center), line-height(1), text-scale(1.0), Titillium Web]
![original, fit](img/REWE-Digital-Abschlussbild.png)

# Thank you!

### `@stefanscheidt` on [Twitter](https://twitter.com/stefanscheidt) and [GitHub](https://github.com/stefanscheidt)

### This presentation can be found [here](https://github.com/stefanscheidt/kotlin-dsl)


