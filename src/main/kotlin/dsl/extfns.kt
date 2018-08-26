package dsl


fun <T> swap(list: MutableList<T>, index1: Int, index2: Int) {
    val tmp = list[index1]
    list[index1] = list[index2]
    list[index2] = tmp
}

fun <T> MutableList<T>.swapExt(index1: Int, index2: Int) {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
}

fun main(args: Array<String>) {
    val list = mutableListOf(1, 2, 3)
    swap(list, 1, 2)
    println(list)
    list.swapExt(1, 2)
    println(list)

    val printMe: String.() -> Unit = { println(this) }
    "Stefan".printMe()

}
