theme: Simple, 1
background-color: #FFFFFF
text: #777777, alignment(left), line-height(1), text-scale(1.0), Titillium Web
header: #25c5dc, alignment(left), line-height(1), text-scale(1.0), Titillium Web

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

> Embedded (or internal) domain-specific languages are implemented as libraries which exploit the syntax of their host general purpose language
-- [Wikipedia](https://en.wikipedia.org/wiki/Domain-specific_language#Usage_patterns)

---

[.text: #777777, alignment(center), line-height(1), text-scale(1.2), Titillium Web]

# Kotlin DSLs

A domain-specific language embedded in Kotlin

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
    kotlin("jvm") version "1.2.60"
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

[^3]: Kotlin Gradle DSL, <https://github.com/gradle/kotlin-dsl>

---

# Examples - Android Layouts [^4]

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

[^4]: Kotlin Anko Layouts, <https://github.com/Kotlin/anko/wiki/Anko-Layouts>

---

[.header: #FFFFFF, alignment(center), line-height(1), text-scale(1.4), Titillium Web]
![original, fit](img/Zutaten.png)

# __Build your own DSL__

## __Ingredients__

---

# Lambda Expressions with Receivers

Let's assume we have a class

```kotlin
class HTML {
    fun head() {}
    fun body() {}
}
```

and a function

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
html {
    head()
    body()   
}
```

---

# Lambda Expressions with Receivers

OK, let's go on ...

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

Now we can write

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

... and so on.[^5]

[^5]: See <https://kotlinlang.org/docs/reference/type-safe-builders.html> for the complete example

---

# Scope control

```kotlin
html {
    head {
        head {
            title()
        }
    }
    body{
        p()
    }
}
```

will also be OK. :-(

---

# DSL Markers

Control receiver scope with `@DslMarker`:

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
    body{
        p()
    }
}
```

---

# Invoke

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


