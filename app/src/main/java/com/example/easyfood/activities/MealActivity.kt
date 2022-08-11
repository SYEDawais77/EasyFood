package com.example.easyfood.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.viewModel.MealViewModel

class MealActivity : AppCompatActivity() {
    private lateinit var mealViewModel: MealViewModel
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var binding: ActivityMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mealViewModel = ViewModelProvider(this)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationInViewsMealActivity()

        loadingResponseFromAPI()

        mealViewModel.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onYoutubeIconClicked()

    }

    private fun onYoutubeIconClicked() {
        binding.imageViewYoutubeIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private fun observeMealDetailsLiveData() {
        mealViewModel.observeMealDetailsLiveData().observe(
            this
        ) {
            responseReceivedFromAPI()
            binding.textViewCategory.text = "Category: ${it!!.strCategory}"
            binding.textViewArea.text = "Area: ${it.strArea}"
            binding.textViwMealDetailInformation.text = it.strInstructions

            youtubeLink = it.strYoutube
        }
    }

    private fun setInformationInViewsMealActivity() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imageViewMealDetail)

        binding.collapsingToolbarLayout.title = mealName
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbarLayout.setExpandedTitleColor(resources.getColor(R.color.white))

    }


    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }


    private fun loadingResponseFromAPI() {
        binding.linearProgressIndicator.visibility = View.VISIBLE
        binding.floatingActionButtonAddToFav.visibility = View.INVISIBLE
        binding.textViwMealIntroduction.visibility = View.INVISIBLE
        binding.textViewCategory.visibility = View.INVISIBLE
        binding.textViewArea.visibility = View.INVISIBLE
        binding.imageViewYoutubeIcon.visibility = View.INVISIBLE
        binding.textViwMealDetailInformation.visibility = View.INVISIBLE

    }

    private fun responseReceivedFromAPI() {
        binding.linearProgressIndicator.visibility = View.INVISIBLE
        binding.floatingActionButtonAddToFav.visibility = View.VISIBLE
        binding.textViwMealIntroduction.visibility = View.VISIBLE
        binding.textViewCategory.visibility = View.VISIBLE
        binding.textViewArea.visibility = View.VISIBLE
        binding.imageViewYoutubeIcon.visibility = View.VISIBLE
        binding.textViwMealDetailInformation.visibility = View.VISIBLE

    }

}