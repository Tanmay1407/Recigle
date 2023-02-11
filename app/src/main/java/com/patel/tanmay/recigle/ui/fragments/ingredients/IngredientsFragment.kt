package com.patel.tanmay.recigle.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.patel.tanmay.recigle.R
import com.patel.tanmay.recigle.adapters.IngredientsAdapter
import com.patel.tanmay.recigle.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredients.view.*


class IngredientsFragment : Fragment() {

    private val mAdapter : IngredientsAdapter by lazy { IngredientsAdapter()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)
        val args = arguments
        val myBundle : com.patel.tanmay.recigle.models.Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setUpRecyclerView(view)
        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }

        return view
    }

    fun setUpRecyclerView(view : View){
        view.ingredients_recyclerView.adapter = mAdapter
        view.ingredients_recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}