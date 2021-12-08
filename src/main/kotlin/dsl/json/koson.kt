package dsl.json

import com.lectra.koson.arr
import com.lectra.koson.obj

val o = obj {
    "property" to "value"
    "array" to arr[1, 2, 3]
    "null" to null
    "empty" to obj { }
}


fun main() {
    println(o.pretty())
}
