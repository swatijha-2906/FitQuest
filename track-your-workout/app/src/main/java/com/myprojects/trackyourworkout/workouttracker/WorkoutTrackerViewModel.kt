/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myprojects.trackyourworkout.workouttracker

import android.app.Application
import androidx.lifecycle.*
import com.myprojects.trackyourworkout.database.Workout
import com.myprojects.trackyourworkout.database.WorkoutDatabaseDao
import com.myprojects.trackyourworkout.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for WorkoutTrackerFragment.
 */
class WorkoutTrackerViewModel(
        val database: WorkoutDatabaseDao,
        application: Application) : AndroidViewModel(application) {

        private var viewModelJob = Job()
        override fun onCleared() {
                super.onCleared()
                viewModelJob.cancel()
        }

        private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)
        private var today = MutableLiveData<Workout?>()
        val days= database.getAllDays()

        //navigation
        private val _navigateToWorkoutQuality = MutableLiveData<Workout>()

        val navigateToWorkoutQuality: LiveData<Workout>
                get() = _navigateToWorkoutQuality

        private var _showSnackbarEvent = MutableLiveData<Boolean>()

        val showSnackBarEvent: LiveData<Boolean>
                get() = _showSnackbarEvent

        fun doneShowingSnackbar() {
                _showSnackbarEvent.value = false
        }

        val daysString = Transformations.map(days) { days ->
                formatNights(days, application.resources)
        }
        init {
                initializeToday()
        }
        private fun initializeToday() {
                uiScope.launch {
                        today.value = getTodayFromDatabase()
                }
        }

        private suspend fun getTodayFromDatabase():  Workout? {
                return withContext(Dispatchers.IO) {
                        var day = database.getToday()

                        if (day?.endTimeMilli != day?.startTimeMilli) {
                                day = null
                        }
                        day
                }
        }
        fun onStartTracking() {
                uiScope.launch {
                        val newDay = Workout()

                        today.value = getTodayFromDatabase()

                        if(today.value==null) {
                                insert(newDay)
                        }

                        today.value = getTodayFromDatabase()
                }
        }
        private suspend fun insert(day: Workout){
                withContext(Dispatchers.IO) {
                        database.insert(day)
                }
        }
        fun onStopTracking() {
                uiScope.launch {
                        val oldDay = today.value ?: return@launch
                        oldDay.endTimeMilli = System.currentTimeMillis()
                        update(oldDay)
                        _navigateToWorkoutQuality.value = oldDay
                }
        }
        private suspend fun update(day: Workout) {
                withContext(Dispatchers.IO) {
                        database.update(day)
                }
        }
        fun onClear() {
                uiScope.launch {
                        clear()
                        today.value = null
                }
                _showSnackbarEvent.value = true
        }
        suspend fun clear() {
                withContext(Dispatchers.IO) {
                        database.clear()
                }
        }
        fun doneNavigating() {
                _navigateToWorkoutQuality.value = null
        }

        //RecyclerView onclick
        private val _navigateToWorkoutDataQuality = MutableLiveData<Long>()
        val navigateToWorkoutDataQuality
                get() = _navigateToWorkoutDataQuality
        fun onWorkoutItemClicked(id: Long){
                _navigateToWorkoutDataQuality.value = id
        }
        fun onWorkoutDataQualityNavigated() {
                _navigateToWorkoutDataQuality.value = 0
        }
}


