package com.patel.tanmay.recigle.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.patel.tanmay.recigle.R

class RecipesRowBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic // make fun static
        fun loadImageFromUrl(imageView: ImageView, imageUrl : String){
                imageView.load(imageUrl){
                    crossfade(600)
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
    }
}