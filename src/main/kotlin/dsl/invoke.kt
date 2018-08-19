package dsl


class MyClass {
    operator fun invoke() {
        println("invoked")
    }
}

class Family {
    fun addMember(name: String) {
        println("Added $name to the family.")
    }

    operator fun invoke(body: Family.() -> Unit) {
        body()
    }
}

fun main(args: Array<String>) {
    val myObj = MyClass()
    myObj()

    val family = Family()
    family {
        addMember("Mom")
        addMember("Dad")
        addMember("Kid")
    }
}


