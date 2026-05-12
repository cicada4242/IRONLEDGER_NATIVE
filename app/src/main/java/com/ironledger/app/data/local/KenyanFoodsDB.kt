package com.ironledger.app.data.local

import com.ironledger.app.data.local.entity.FoodEntity

/**
 * IronLedger - Kenyan Foods Database
 * Nutritional values per 100g edible portion unless stated otherwise.
 * Calories calculated from macros: protein*4 + carbs*4 + fat*9
 */
object KenyanFoodsDB {
    val foods = listOf(
        // ============ HIGH PROTEIN ============
        FoodEntity(
            name = "Omena (Silver Cyprinid, dried/cooked)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 250,
            protein = 40f,
            carbs = 0f,
            fat = 10f
        ),
        FoodEntity(
            name = "Kuku Choma (Roasted Chicken)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 163,
            protein = 31f,
            carbs = 0f,
            fat = 3.5f
        ),
        FoodEntity(
            name = "Nyama Choma (Roasted Beef)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 239,
            protein = 26f,
            carbs = 0f,
            fat = 15f
        ),
        FoodEntity(
            name = "Nyama Choma (Roasted Goat)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 239,
            protein = 26f,
            carbs = 0f,
            fat = 15f
        ),
        FoodEntity(
            name = "Ndengu (Green Grams, cooked)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 107,
            protein = 7f,
            carbs = 19f,
            fat = 0.4f
        ),
        FoodEntity(
            name = "Njahi (Black Beans, cooked)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 130,
            protein = 8.5f,
            carbs = 23f,
            fat = 0.5f
        ),
        FoodEntity(
            name = "Kamande (Lentils, cooked)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 120,
            protein = 9f,
            carbs = 20f,
            fat = 0.4f
        ),
        FoodEntity(
            name = "Yellow Beans (cooked)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 125,
            protein = 8f,
            carbs = 22f,
            fat = 0.6f
        ),
        FoodEntity(
            name = "Tilapia (grilled)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 126,
            protein = 26f,
            carbs = 0f,
            fat = 2.5f
        ),
        FoodEntity(
            name = "Mala (Fermented Milk)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "ml",
            calories = 58,
            protein = 3.3f,
            carbs = 5f,
            fat = 3f
        ),
        FoodEntity(
            name = "Mayai (Boiled Eggs)",
            category = "High Protein",
            servingSize = 50f,
            servingUnit = "large egg (50g)",
            calories = 78,
            protein = 6.5f,
            carbs = 0.5f,
            fat = 5.5f
        ),

        // ============ EVERYDAY STAPLES ============
        FoodEntity(
            name = "Ugali (Maize Meal, cooked)",
            category = "Everyday Staple",
            servingSize = 100f,
            servingUnit = "g",
            calories = 138,
            protein = 3f,
            carbs = 31f,
            fat = 0.6f
        ),
        FoodEntity(
            name = "White Rice (cooked)",
            category = "Everyday Staple",
            servingSize = 100f,
            servingUnit = "g",
            calories = 130,
            protein = 2.7f,
            carbs = 28f,
            fat = 0.3f
        ),
        FoodEntity(
            name = "Chapati (Wheat Flatbread)",
            category = "Everyday Staple",
            servingSize = 80f,
            servingUnit = "chapati (80g)",
            calories = 253,
            protein = 7.2f,
            carbs = 36.8f,
            fat = 8f
        ),
        FoodEntity(
            name = "Spaghetti (cooked)",
            category = "Everyday Staple",
            servingSize = 100f,
            servingUnit = "g",
            calories = 157,
            protein = 6f,
            carbs = 31f,
            fat = 1f
        ),
        FoodEntity(
            name = "Githeri (Maize + Beans)",
            category = "Everyday Staple",
            servingSize = 100f,
            servingUnit = "g",
            calories = 141,
            protein = 6f,
            carbs = 27f,
            fat = 1f
        ),
        FoodEntity(
            name = "Sukuma Wiki (Collard Greens, cooked)",
            category = "Everyday Staple",
            servingSize = 100f,
            servingUnit = "g",
            calories = 49,
            protein = 3f,
            carbs = 7f,
            fat = 0.6f
        ),
        FoodEntity(
            name = "Matoke (Cooked Green Bananas)",
            category = "Everyday Staple",
            servingSize = 100f,
            servingUnit = "g",
            calories = 129,
            protein = 1f,
            carbs = 31f,
            fat = 0.3f
        ),
        FoodEntity(
            name = "Chai (Kenyan Milk Tea)",
            category = "Everyday Staple",
            servingSize = 250f,
            servingUnit = "ml",
            calories = 120,
            protein = 5f,
            carbs = 20f,
            fat = 5f
        ),
        FoodEntity(
            name = "Mandazi (Fried Dough)",
            category = "Everyday Staple",
            servingSize = 70f,
            servingUnit = "mandazi (70g)",
            calories = 279,
            protein = 4.9f,
            carbs = 36.4f,
            fat = 10.5f
        ),
        FoodEntity(
            name = "Nduma (Arrowroots, boiled)",
            category = "Everyday Staple",
            servingSize = 100f,
            servingUnit = "g",
            calories = 115,
            protein = 1.5f,
            carbs = 27f,
            fat = 0.1f
        ),
        FoodEntity(
            name = "Ngwaci (Sweet Potatoes, boiled)",
            category = "Everyday Staple",
            servingSize = 100f,
            servingUnit = "g",
            calories = 87,
            protein = 1.6f,
            carbs = 20f,
            fat = 0.1f
        ),
        FoodEntity(
            name = "Brown Rice (cooked)",
            category = "Everyday Staple",
            servingSize = 100f,
            servingUnit = "g",
            calories = 123,
            protein = 2.7f,
            carbs = 26f,
            fat = 1f
        ),
        FoodEntity(
            name = "Avocado",
            category = "Everyday Staple",
            servingSize = 150f,
            servingUnit = "medium avocado (150g)",
            calories = 240,
            protein = 3f,
            carbs = 13.5f,
            fat = 22.5f
        ),
        FoodEntity(
            name = "Whole Milk",
            category = "High Protein",
            servingSize = 250f,
            servingUnit = "ml",
            calories = 150,
            protein = 8f,
            carbs = 12f,
            fat = 8f
        ),
        FoodEntity(
            name = "Groundnuts (Peanuts)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 567,
            protein = 26f,
            carbs = 16f,
            fat = 49f
        ),
        FoodEntity(
            name = "Banana",
            category = "Everyday Staple",
            servingSize = 118f,
            servingUnit = "medium banana (118g)",
            calories = 105,
            protein = 1.3f,
            carbs = 27.1f,
            fat = 0.4f
        ),
        FoodEntity(
            name = "Mango",
            category = "Everyday Staple",
            servingSize = 200f,
            servingUnit = "medium mango (200g)",
            calories = 120,
            protein = 1.6f,
            carbs = 30f,
            fat = 0.8f
        ),
        FoodEntity(
            name = "Orange",
            category = "Everyday Staple",
            servingSize = 130f,
            servingUnit = "medium orange (130g)",
            calories = 61,
            protein = 1.2f,
            carbs = 15.6f,
            fat = 0.1f
        ),
        FoodEntity(
            name = "Cassava (boiled)",
            category = "Everyday Staple",
            servingSize = 100f,
            servingUnit = "g",
            calories = 160,
            protein = 1.4f,
            carbs = 38f,
            fat = 0.3f
        ),
        FoodEntity(
            name = "White Bread (1 slice)",
            category = "Everyday Staple",
            servingSize = 30f,
            servingUnit = "slice (30g)",
            calories = 79,
            protein = 2.7f,
            carbs = 15f,
            fat = 1f
        ),
        FoodEntity(
            name = "Brown Bread (1 slice)",
            category = "Everyday Staple",
            servingSize = 30f,
            servingUnit = "slice (30g)",
            calories = 73,
            protein = 3f,
            carbs = 14f,
            fat = 1f
        ),
        FoodEntity(
            name = "Butter (1 tbsp)",
            category = "Everyday Staple",
            servingSize = 14f,
            servingUnit = "tbsp (14g)",
            calories = 102,
            protein = 0.1f,
            carbs = 0f,
            fat = 11.5f
        ),
        FoodEntity(
            name = "Cooking Oil (1 tbsp)",
            category = "Everyday Staple",
            servingSize = 15f,
            servingUnit = "ml",
            calories = 120,
            protein = 0f,
            carbs = 0f,
            fat = 14f
        ),
        FoodEntity(
            name = "Uji (Porridge)",
            category = "Everyday Staple",
            servingSize = 250f,
            servingUnit = "ml",
            calories = 150,
            protein = 3f,
            carbs = 32f,
            fat = 1f
        ),
        FoodEntity(
            name = "Boiled Beans",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 127,
            protein = 8.7f,
            carbs = 22f,
            fat = 0.5f
        ),
        FoodEntity(
            name = "Chicken Breast (grilled)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 165,
            protein = 31f,
            carbs = 0f,
            fat = 3.6f
        ),
        FoodEntity(
            name = "Matumbo (Tripe, cooked)",
            category = "High Protein",
            servingSize = 100f,
            servingUnit = "g",
            calories = 100,
            protein = 12f,
            carbs = 0f,
            fat = 5f
        )
    )

    fun searchFoods(query: String): List<FoodEntity> {
        val lowerQuery = query.lowercase()
        return foods.filter {
            it.name.lowercase().contains(lowerQuery) ||
            it.category.lowercase().contains(lowerQuery)
        }
    }

    fun getFoodsByCategory(): Map<String, List<FoodEntity>> {
        return foods.groupBy { it.category }
    }
}
