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

package com.myprojects.trackyourworkout.workoutdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myprojects.trackyourworkout.database.Workout
import com.myprojects.trackyourworkout.database.WorkoutDatabaseDao

import kotlinx.coroutines.Job


class WorkoutDetailViewModel(
        private val workoutDayKey: Long = 0L,
        dataSource: WorkoutDatabaseDao
) : ViewModel() {

    val database = dataSource


    /**
     */

    private val day = MediatorLiveData<Workout>()

    fun getDay() = day

    init {
        day.addSource(database.getDayWithId(workoutDayKey), day::setValue)
    }

    /**
     * Variable that tells the fragment whether it should navigate to [workoutTrackerFragment].
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the [Fragment]
     */
    private val _navigateToWorkoutTracker = MutableLiveData<Boolean?>()

    /**
     * When true immediately navigate back to the [workoutTrackerFragment]
     */
    val navigateToWorkoutTracker: LiveData<Boolean?>
        get() = _navigateToWorkoutTracker

    /**
     *
     */


    /**
     * Call this immediately after navigating to [workoutTrackerFragment]
     */
    fun doneNavigating() {
        _navigateToWorkoutTracker.value = null
    }

    fun onClose() {
        _navigateToWorkoutTracker.value = true
    }

}

 
