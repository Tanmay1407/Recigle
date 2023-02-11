package com.patel.tanmay.recigle.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*

import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.patel.tanmay.recigle.viewmodels.MainViewModel
import com.patel.tanmay.recigle.R
import com.patel.tanmay.recigle.adapters.RecipesAdapter
import com.patel.tanmay.recigle.databinding.FragmentRecipesBinding
import com.patel.tanmay.recigle.util.Constants.Companion.API_KEY
import com.patel.tanmay.recigle.util.NetworkListener
import com.patel.tanmay.recigle.util.NetworkResult
import com.patel.tanmay.recigle.util.observeOnce
import com.patel.tanmay.recigle.viewmodels.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(),  SearchView.OnQueryTextListener {

    private lateinit var networkListener : NetworkListener
    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding : FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipeViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mView: View
    private val TAG = "RECIPE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipeViewModel = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        setupRecyclerView()

        recipeViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipeViewModel.backOnline = it
        }

        lifecycleScope.launchWhenStarted{
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect {status ->
                    Log.d(TAG,status.toString())
                    recipeViewModel.networkStatus = status
                    recipeViewModel.showNetworkStatus()
                    readDatabase()
                }
        }


        binding.recipesFab.setOnClickListener{
            if(recipeViewModel.networkStatus){
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            }else recipeViewModel.showNetworkStatus()
        }

        return binding.root
    }

    private fun readDatabase() {
        lifecycleScope.launch{
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d(TAG,"-- DATABASE DATA REQUESTED --")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                }else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData(){
        Log.d(TAG,"-- API DATA REQUESTED --")
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    loadDataFromCache()
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
                else -> {}
            }

        }

    }

    private fun searchApiData(searchQuery : String){
        showShimmerEffect()
        mainViewModel.searchRecipes(recipeViewModel.applySearchQueries(searchQuery))
        mainViewModel.searchRecipesResponse.observe(viewLifecycleOwner){response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    loadDataFromCache()
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
                else -> {}
            }

        }

    }

    private fun loadDataFromCache(){
            lifecycleScope.launch { mainViewModel.readRecipes.observe(viewLifecycleOwner) {database ->
                if(database.isNotEmpty()){
                    mAdapter.setData(database[0].foodRecipe)
                }
            } }
        }



    private fun setupRecyclerView(){
        binding.root.recipe_recyclerView.adapter = mAdapter
        binding.root.recipe_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu,menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchApiData(query)
        }

        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    private fun showShimmerEffect(){
        binding.root.recipe_recyclerView.showShimmer()
    }

    private fun hideShimmerEffect(){
        binding.root.recipe_recyclerView.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}