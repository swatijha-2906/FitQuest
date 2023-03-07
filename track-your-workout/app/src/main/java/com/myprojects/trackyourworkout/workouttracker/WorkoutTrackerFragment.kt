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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.myprojects.trackyourworkout.R
import com.myprojects.trackyourworkout.database.WorkoutDatabase
import com.myprojects.trackyourworkout.databinding.FragmentWorkoutTrackerBinding


/**
 * A fragment with buttons to record start and end times for workout, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class WorkoutTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_workout_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentWorkoutTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_workout_tracker, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = WorkoutDatabase.getInstance(application).workoutDatabaseDao

        // Create an instance of the ViewModel Factory
        val viewModelFactory = WorkoutTrackerViewModelFactory(dataSource, application)
        //Get a reference to the ViewModel associated with this fragment.
        val workoutTrackerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(WorkoutTrackerViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.workoutTrackerViewModel = workoutTrackerViewModel

        val adapter= WorkoutAdapter(WorkoutListener { dayId ->
            workoutTrackerViewModel.onWorkoutItemClicked(dayId)
        })

        workoutTrackerViewModel.navigateToWorkoutDataQuality.observe(viewLifecycleOwner, Observer {day ->
            day?.let {

                if(this.findNavController().currentDestination?.id==R.id.workout_tracker_fragment) {
                    this.findNavController().navigate(
                        WorkoutTrackerFragmentDirections.actionWorkoutTrackerFragmentToWorkoutDetailFragment2(
                            day
                        )
                    )
                    workoutTrackerViewModel.onWorkoutDataQualityNavigated()
                }
            }
        })

        binding.workoutList.adapter= adapter
        workoutTrackerViewModel.days.observe(viewLifecycleOwner, Observer{
            it?.let { adapter.submitList(it) }
        })

        //Grid Layout
        val manager = GridLayoutManager(activity, 3)
        binding.workoutList.layoutManager = manager

        workoutTrackerViewModel.navigateToWorkoutQuality.observe(viewLifecycleOwner, Observer {
                day ->
            day?.let {
                this.findNavController().navigate(
                    WorkoutTrackerFragmentDirections
                        .actionWorkoutTrackerFragmentToWorkoutQualityFragment(day.dayId))
                workoutTrackerViewModel.doneNavigating()
            }
        })

        workoutTrackerViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                workoutTrackerViewModel.doneShowingSnackbar()
            }
        })

        return binding.root
    }
}
