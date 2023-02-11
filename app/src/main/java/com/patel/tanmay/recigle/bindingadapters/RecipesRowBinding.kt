package com.patel.tanmay.recigle.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.patel.tanmay.recigle.R
import com.patel.tanmay.recigle.ui.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup

class RecipesRowBinding {

    companion object {

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic // make fun static
        fun onRecipeClickListener(recipeRowLayout : ConstraintLayout, result : com.patel.tanmay.recigle.models.Result){
            recipeRowLayout.setOnClickListener{
                try {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                }catch (e : Exception){
                    Log.d("RecipesRowLayout",e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic // make fun static
        fun loadImageFromUrl(imageView: ImageView, imageUrl : String){
                imageView.load(imageUrl){
                    crossfade(600)
                    error(R.drawable.ic_error_image)
                }
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic // make fun static
        fun setNumberOfLikes(textView: TextView, likes : Int){
            textView.text = likes.toString()
        }

        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic // make fun static
        fun setNumberOfMinutes(textView: TextView, minutes : Int){
            textView.text = minutes.toString()
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic // make fun static
        fun applyVeganColor(view: View, vegan : Boolean){
            if(vegan){
                when(view){
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }

                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }


        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView,description : String?){
            if(description != null){
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }
    }
}