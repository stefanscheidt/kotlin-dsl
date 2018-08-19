package dsl.html


@DslMarker
annotation class HtmlTagMarker

@HtmlTagMarker
class HTML {
    fun head(init: Head.() -> Unit): Head {
        val head = Head()
        head.init()
        return head
    }

    fun body(init: Body.() -> Unit): Body {
        val body = Body()
        body.init()
        return body
    }
}

@HtmlTagMarker
class Head {
    fun title() {
        println("title")
    }
}

@HtmlTagMarker
class Body {
    fun p() {
        println("p")
    }
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()
    html.init()
    return html
}

fun main(args: Array<String>) {
    html {
        head {
            this@html.head {
                title()
            }
        }
        body {
            p()
        }
    }
}
