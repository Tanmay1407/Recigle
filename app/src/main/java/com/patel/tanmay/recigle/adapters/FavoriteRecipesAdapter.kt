package com.patel.tanmay.recigle.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.patel.tanmay.recigle.R
import com.patel.tanmay.recigle.data.database.entities.FavoritesEntity
import com.patel.tanmay.recigle.databinding.FavoriteRecipesRowLayoutBinding
import com.patel.tanmay.recigle.ui.fragments.favorites.FavoriteFragmentDirections
import com.patel.tanmay.recigle.util.RecipesDiffUtil
import com.patel.tanmay.recigle.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*

class FavoriteRecipesAdapter(
    private val requireActivity : FragmentActivity,
    private val mainViewModel : MainViewModel
) : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {


    private lateinit var mActionMode: ActionMode
    private lateinit var rootView : View
    private var multiSelection = false
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var selectedRecipes = arrayListOf<FavoritesEntity>()

    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class MyViewHolder(private val binding : FavoriteRecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        // binding the favoritesEntity variable from the layout to the current favoritesEntity object
        fun bind(favoritesEntity: FavoritesEntity){
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
       return favoriteRecipes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)

        holder.itemView.favoriteRecipesRowLayout.setOnClickListener {
            if(multiSelection){
                applySelection(holder,currentRecipe)
            } else {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailsActivity(currentRecipe.result)
                holder.itemView.findNavController().navigate(action)
            }

        }

        holder.itemView.favoriteRecipesRowLayout.setOnLongClickListener {
            if(!multiSelection){
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder,currentRecipe)
                true
            }else {
                multiSelection = false
                false
            }


        }


    }

    fun setData(newFavoriteRecipes : List<FavoritesEntity>){
        val favRecipesDiffUtil =
            RecipesDiffUtil(favoriteRecipes,newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favRecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe : FavoritesEntity){
       if(selectedRecipes.contains(currentRecipe)){
           selectedRecipes.remove(currentRecipe)
           changeRecipeStyle(holder,R.color.card_background_color,R.color.strokeColor)
           applyActionModeTile()
       }else {
           selectedRecipes.add(currentRecipe)
           changeRecipeStyle(holder,R.color.cardBackgroundLightColor,R.color.colorPrimary)
           applyActionModeTile()

       }
    }

    private fun changeRecipeStyle(holder : MyViewHolder,bgColor : Int, strokeColor : Int){
        holder.itemView.fav_recipe_container.setBackgroundColor(ContextCompat.getColor(requireActivity,bgColor))
        holder.itemView.fav_row_cardView.strokeColor = ContextCompat.getColor(requireActivity,strokeColor)
    }

    private fun applyActionModeTile(){
        when(selectedRecipes.size){
            0 ->{
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"

            }
        }
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu,menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {

        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if(menu?.itemId == R.id.delete_favorite_recipe_menu){
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed.")
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }

        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach{ holder ->
            changeRecipeStyle(holder,R.color.card_background_color, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int){
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity,color)
    }
    private fun showSnackBar(mess : String){
        Snackbar.make(
            rootView,
            mess,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}