package com.patel.tanmay.recigle.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.patel.tanmay.recigle.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.patel.tanmay.recigle.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.patel.tanmay.recigle.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.patel.tanmay.recigle.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.patel.tanmay.recigle.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.patel.tanmay.recigle.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.patel.tanmay.recigle.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.patel.tanmay.recigle.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

   suspend fun saveMealAndDietType(mealType : String, mealTypeId : Int,dietType : String, dietTypeId : Int){
       context.dataStore.edit { preferences ->
           preferences[PreferenceKeys.selectedMealType] = mealType
           preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
           preferences[PreferenceKeys.selectedDietType] = dietType
           preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
       }
   }

    suspend fun saveBackOnline(backOnline: Boolean){
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }

    val readMealAndDietType : Flow<MealAndDietType> = context.dataStore.data
        .catch { e ->
            if(e is IOException) {
                emit(emptyPreferences())
            }else {
                throw e
            }
        }
        .map { pref ->
            val sMT = pref[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val sMTID = pref[PreferenceKeys.selectedMealTypeId] ?: 0
            val sDT = pref[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val sDTID = pref[PreferenceKeys.selectedDietTypeId] ?: 0

            MealAndDietType(sMT,sMTID,sDT,sDTID)
        }


    val readBackOnline : Flow<Boolean> = context.dataStore.data
        .catch { e ->
            if(e is IOException) {
                emit(emptyPreferences())
            }else {
                throw e
            }
        }
        .map { pref ->
            val backOnline = pref[PreferenceKeys.backOnline] ?: false
            backOnline
        }
}

data class MealAndDietType (
    val selectedMealType : String,
    val selectedMealTypeId : Int,
    val selectedDietType : String,
    val selectedDietTypeId : Int
        )