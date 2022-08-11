package com.example.easyfood.retrofit

import com.example.easyfood.datasource.CategoryList
import com.example.easyfood.datasource.MealsByCategoryList
import com.example.easyfood.datasource.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeals(): Call<MealList>

    @GET("lookup.php?")
    fun getInstructionDetail(@Query("i") id: String): Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c") categoryName: String): Call<MealsByCategoryList>

    @GET("categories.php")
    fun getMealsCategories(): Call<CategoryList>
}