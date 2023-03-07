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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.myprojects.trackyourworkout.workoutdetail.WorkoutDetailViewModel
import com.myprojects.trackyourworkout.workoutdetail.WorkoutDetailViewModelFactory
import com.myprojects.trackyourworkout.R
import com.myprojects.trackyourworkout.database.WorkoutDatabase
import com.myprojects.trackyourworkout.databinding.FragmentWorkoutDetailBinding


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [workoutDetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [workoutDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WorkoutDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentWorkoutDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_workout_detail, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = WorkoutDetailFragmentArgs.fromBundle(arguments!!)

        // Create an instance of the ViewModel Factory.
        val dataSource = WorkoutDatabase.getInstance(application).workoutDatabaseDao
        val viewModelFactory = WorkoutDetailViewModelFactory(arguments.workoutDayKey, dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val workoutDetailViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(WorkoutDetailViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.workoutDetailViewModel = workoutDetailViewModel

        binding.lifecycleOwner = this

        // Add an Observer to the state variable for Navigating when a Quality icon is tapped.
        workoutDetailViewModel.navigateToWorkoutTracker.observe(viewLifecycleOwner, Observer {
            if (it == true) { // Observed state is true.
                this.findNavController().navigate(
                        WorkoutDetailFragmentDirections.actionWorkoutDetailFragment2ToWorkoutTrackerFragment())
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                workoutDetailViewModel.doneNavigating()
            }
        })

        return binding.root
    }
}
