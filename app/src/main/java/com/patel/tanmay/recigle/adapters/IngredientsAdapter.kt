package com.patel.tanmay.recigle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.patel.tanmay.recigle.R
import com.patel.tanmay.recigle.models.ExtendedIngredient
import com.patel.tanmay.recigle.util.Constants.Companion.BASE_IMAGE_URL
import com.patel.tanmay.recigle.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*
import java.util.*

class IngredientsAdapter  : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>(){

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout,parent,false))

    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.ingredient_imageView.load(BASE_IMAGE_URL + ingredientsList[position].image){
            crossfade(600)
            error(R.drawable.ic_error_image)
        }
        holder.itemView.ingredient_name_textView.text = ingredientsList[position].name.capitalize(
            Locale.ROOT)
        holder.itemView.ingredient_amt_textView.text = ingredientsList[position].amount.toString()
        holder.itemView.ingredient_unit_textView.text = ingredientsList[position].unit
        holder.itemView.ingredient_consist_textView.text = ingredientsList[position].consistency
//        holder.itemView.ingredient_original_textView.text = ingredientsList[position].original

    }

    fun setData(newIngredients : List<ExtendedIngredient>){
        val ingredientsDiffUtil =
            RecipesDiffUtil(ingredientsList,newIngredients)

        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)


    }
}