package com.patel.tanmay.recigle.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.patel.tanmay.recigle.R
import com.patel.tanmay.recigle.util.Constants
import com.patel.tanmay.recigle.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.patel.tanmay.recigle.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.patel.tanmay.recigle.viewmodels.RecipeViewModel
import kotlinx.android.synthetic.main.recipes_bottom_sheet.view.*
import java.util.*


class RecipesBottomSheet : BottomSheetDialogFragment() {
    private lateinit var recipesViewModel: RecipeViewModel

    private var mealTypeChip  = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.recipes_bottom_sheet, container, false)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType

            updateChip(value.selectedMealTypeId,mView.mealType_chipGroup)
            updateChip(value.selectedDietTypeId,mView.dietType_chipGroup)
        }

        mView.mealType_chipGroup.setOnCheckedChangeListener{group, selectedChipId ->
            var chip = group.findViewById<Chip>(selectedChipId)
            var selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId


        }

        mView.dietType_chipGroup.setOnCheckedChangeListener{group, selectedChipId ->
            var chip = group.findViewById<Chip>(selectedChipId)
            var selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }

        mView.apply_btn.setOnClickListener{
            recipesViewModel.saveMealAndDietType(mealTypeChip,mealTypeChipId,dietTypeChip,dietTypeChipId)
            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

        return mView
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
            if(chipId != 0){
                try {
                    chipGroup.findViewById<Chip>(chipId).isChecked = true
                }catch (e : Exception) {
                    Log.d("RecipesBottomSheet",e.message.toString())
                }
            }
    }

}