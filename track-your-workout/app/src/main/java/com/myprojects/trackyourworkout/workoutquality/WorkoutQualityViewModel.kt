

package com.myprojects.trackyourworkout.workoutquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myprojects.trackyourworkout.database.WorkoutDatabaseDao
import kotlinx.coroutines.*

class WorkoutQualityViewModel(
    private val dayKey: Long = 0L,
    val database: WorkoutDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope =  CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }
    private val _navigateToWorkoutTracker =  MutableLiveData<Boolean?>()

    val navigateToWorkoutTracker: LiveData<Boolean?>
        get() = _navigateToWorkoutTracker

    fun doneNavigating() {
        _navigateToWorkoutTracker.value = null
    }

    fun onSetWorkoutQuality(quality: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val today = database.get(dayKey) ?: return@withContext
                today.workoutQuality = quality
                database.update(today)
            }
            _navigateToWorkoutTracker.value = true
        }
    }


}
