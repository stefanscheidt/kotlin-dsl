package dsl.html.cont

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class HtmlTest {

    @Test
    fun `produce html`() {
        val document = html {
            head {
                title { +"Intro to Kotlin DSLs" }
            }
            body {
                p { +"Kotlin DSLs are awesome!" }
            }
        }

        val markup =
            """<html>
            |<head>
            |<title>
            |Intro to Kotlin DSLs
            |</title>
            |</head>
            |<body>
            |<p>
            |Kotlin DSLs are awesome!
            |</p>
            |</body>
            |</html>
            |""".trimMargin()

        assertEquals(markup, document.toString())
    }

}