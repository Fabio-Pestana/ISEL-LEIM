class Recipe {

    private val steps = mutableListOf<Pair<String, () -> Unit>>()

    operator fun plusAssign(step: Pair<String, () -> Unit>) {
        steps.add(step)
    }

    /*
    fun addStep(description: String, action: () -> Unit) {
        steps.add(description to action)
    }
    */

    fun startCooking() {
        steps.forEachIndexed { index, (description, action) ->
            println("Step ${index + 1}: $description")
            action()
            println()
        }
    }
}

fun buildRecipe(steps: Recipe.() -> Unit): Recipe {
    val recipe = Recipe()
    recipe.steps()
    return recipe
}

fun main() {
    val pancakeRecipe = buildRecipe {
        this += "Mix ingredients" to { println("Mixing flour, eggs, milk, and sugar...") }
        this += "Heat the pan" to { println("Heating the pan on medium heat...") }
        this += "Cook pancakes" to { println("Pouring batter, cooking, and flipping!") }
        this += "Serve" to { println("Serving hot pancakes with syrup!") }
    }

    pancakeRecipe.startCooking()
}