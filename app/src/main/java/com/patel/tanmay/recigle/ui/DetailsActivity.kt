package com.patel.tanmay.recigle.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.patel.tanmay.recigle.R
import com.patel.tanmay.recigle.adapters.PagerAdapter
import com.patel.tanmay.recigle.data.database.entities.FavoritesEntity
import com.patel.tanmay.recigle.ui.fragments.ingredients.IngredientsFragment
import com.patel.tanmay.recigle.ui.fragments.instructions.InstructionsFragment
import com.patel.tanmay.recigle.ui.fragments.overview.OverviewFragment
import com.patel.tanmay.recigle.util.Constants.Companion.RECIPE_RESULT_KEY
import com.patel.tanmay.recigle.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel : MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolBar)
        toolBar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY,args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu,menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipe.observe(this){favoritesEntity ->
            try {
                for(savedRecipe in favoritesEntity){
                    if(savedRecipe.result.id == args.result.id){
                        changeMenuItemColor(menuItem,R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    }else {
                        changeMenuItemColor(menuItem,R.color.white)
                    }
                }
            } catch (e : Exception){
                Log.d("Details",e.message.toString())
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }else if (item.itemId == R.id.save_to_favorites && recipeSaved == false){
            saveToFavorites(item)
        }else if (item.itemId == R.id.save_to_favorites && recipeSaved == true){
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem) {
            val favoritesEntity =
                FavoritesEntity(
                    0,
                    args.result
                )
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item,R.color.yellow)
        showSnackBar("Recipe saved.")
        recipeSaved = true

    }

    private fun removeFromFavorites(item : MenuItem){
        val favoritesEntity =
            FavoritesEntity(
                savedRecipeId,
                args.result
            )

        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item,R.color.white)
        showSnackBar("Removed from Favorites.")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this,color))
    }
}