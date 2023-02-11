package com.patel.tanmay.recigle.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.patel.tanmay.recigle.RecipeEntity
import com.patel.tanmay.recigle.data.network.FoodRecipesApi
import com.patel.tanmay.recigle.models.FoodRecipe
import com.patel.tanmay.recigle.util.NetworkResult
import retrofit2.Response

class RecipesBinding {

    companion object {

        @BindingAdapter("apiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database : List<RecipeEntity>?
        ){
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty() ){
                imageView.visibility = View.VISIBLE
            }else if(apiResponse is NetworkResult.Loading){
                imageView.visibility = View.INVISIBLE
            }else if (apiResponse is NetworkResult.Success){
                imageView.visibility = View.INVISIBLE

            }

        }

        @BindingAdapter("apiResponse_2", "readDatabase_2", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database : List<RecipeEntity>?
        ){
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty() ){
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            }else if(apiResponse is NetworkResult.Loading){
                textView.visibility = View.INVISIBLE
            }else if (apiResponse is NetworkResult.Success){
                textView.visibility = View.INVISIBLE

            }

        }


    }
}