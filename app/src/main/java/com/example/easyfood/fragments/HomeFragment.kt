package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapters.MostPopularMealsAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.datasource.MealsByCategory
import com.example.easyfood.datasource.Meal
import com.example.easyfood.viewModel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularMealsAdapter

    companion object {
        const val MEAL_ID = "com.example.easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.example.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.fragments.thumbMeal"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        popularItemsAdapter = MostPopularMealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        homeViewModel.getRandomMeals()
        observerRandomMeal()
        onRandomMealClicked()

        homeViewModel.getPopularItems()
        observerPopularItemsLiveData()

        onPopularItemClicked()

        homeViewModel.getMealsCategories()
        //observerCategoriesLiveData()
    }

//    private fun observerCategoriesLiveData() {
//        homeViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, { categories ->
//            categories.forEach(
//                categories ->
//            )
//        })
//    }

    private fun onPopularItemClicked() {
        popularItemsAdapter.onItemClick = {
            val intent = Intent(activity, MealActivity::class.java)
                .putExtra(MEAL_ID, it.idMeal)
                .putExtra(MEAL_NAME, it.strMeal)
                .putExtra(MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recyclerViewPopularItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observerPopularItemsLiveData() {
        homeViewModel.observePopularItemsLiveData()
            .observe(
                viewLifecycleOwner
            ) { t ->
                popularItemsAdapter.setMeals(mealsList = t as ArrayList<MealsByCategory>)

            }
    }

    private fun onRandomMealClicked() {
        binding.cardViewRandomMeals.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
                .putExtra(MEAL_ID, randomMeal.idMeal)
                .putExtra(MEAL_NAME, randomMeal.strMeal)
                .putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        homeViewModel.observeRandomMealLiveData()
            .observe(
                viewLifecycleOwner
            ) { t ->
                Glide.with(this@HomeFragment)
                    .load(t!!.strMealThumb)
                    .into(binding.imageRandomMeals)

                this.randomMeal = t
            }
    }
}