package machine

import kotlin.math.min

fun main() {
    val cm = CoffeeMachine()
    while (cm.currentState != States.EXIT) {
        if (cm.currentState == States.WAIT) {
            println()
            print("Write action (buy, fill, take, remaining, exit): ")
        }
        cm.read(readLine()!!)
    }
}

enum class States { WAIT, BUY, FILL, WATER, MILK, BEAN, CUP, EXIT }

enum class Recipe(val water: Int, val milk: Int, val bean: Int, val cost: Int) {
    Espresso(250, 0, 16, 4),
    Latte(350, 75, 20, 7),
    Cappuccino(200, 100, 12, 6)
}

class CoffeeMachine(
    var waters: Int = 400,
    var milks: Int = 540,
    var beans: Int = 120,
    var cups: Int = 9,
    var sum: Int = 550) {

    var currentState = States.WAIT

    fun read(msg: String) {
        when (currentState) {
            States.WAIT -> readCmd(msg)
            States.BUY -> buy(msg)
            else -> fill(msg)
        }
    }

    fun readCmd(msg: String) {
        when (msg) {
            "buy" -> {
                println()
                print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
                currentState = States.BUY
                return
            }
            "fill" -> { currentState = States.FILL; fill(msg) }
            "take" -> take();
            "remaining" -> state()
            "exit" -> currentState = States.EXIT
            else -> return
        }
    }

    fun fill(msg: String) {
        when (currentState) {
            States.FILL -> {
                println()
                print("Write how many ml of water do you want to add: ")
                currentState = States.WATER
            }
            States.WATER -> {
                waters += msg.toInt()
                print("Write how many ml of milk do you want to add: ")
                currentState = States.MILK
            }
            States.MILK -> {
                milks += msg.toInt()
                print("Write how many grams of coffee beans do you want to add: ")
                currentState = States.BEAN
            }
            States.BEAN -> {
                beans += msg.toInt()
                print("Write how many disposable cups of coffee do you want to add: ")
                currentState = States.CUP
            }
            States.CUP -> {
                cups += msg.toInt()
                currentState = States.WAIT
            }
        }
    }

    fun buy(msg: String) {
        currentState = States.WAIT
        val coffee = when (msg) {
            "1" -> Recipe.Espresso
            "2" -> Recipe.Latte
            "3" -> Recipe.Cappuccino
            else -> return
        }
        val isWater = waters >= coffee.water
        val isMilk = milks >= coffee.milk
        val isBean = beans >= coffee.bean
        val isCup = cups > 0
        var isEnough = false
        println(if (!isWater) "Sorry, not enough water!"
                else if (!isMilk) "Sorry, not enough milk!"
                else if (!isBean) "Sorry, not enough beans!"
                else if (!isCup) "Sorry, not enough cups!"
                else { isEnough = true; "I have enough resources, making you a coffee!" })
        if (isEnough) {
            waters -= coffee.water
            milks -= coffee.milk
            beans -= coffee.bean
            sum += coffee.cost
            cups--
        }
    }

    fun take() {
        println("I gave you \$$sum")
        sum = 0
    }

    fun state() {
        println()
        println("The coffee machine has:")
        println("$waters of water")
        println("$milks of milk")
        println("$beans of coffee beans")
        println("$cups of disposable cups")
        println("\$$sum of money")
    }

}

