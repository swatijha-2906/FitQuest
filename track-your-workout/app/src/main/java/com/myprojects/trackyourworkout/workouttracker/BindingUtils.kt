package com.myprojects.trackyourworkout.workouttracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.myprojects.trackyourworkout.R
import com.myprojects.trackyourworkout.convertDurationToFormatted
import com.myprojects.trackyourworkout.convertNumericQualityToString
import com.myprojects.trackyourworkout.database.Workout


@BindingAdapter("workoutImage")
fun ImageView.setSleepImage(item: Workout?) {
    item?.let {
        setImageResource(
            when (item.workoutQuality) {
                0 -> R.drawable.ic_workout_0
                1 -> R.drawable.ic_workout_1
                2 -> R.drawable.ic_workout_2
                3 -> R.drawable.ic_workout_3
                4 -> R.drawable.ic_workout_4
                5 -> R.drawable.ic_workout_5
                else -> R.drawable.ic_workout_active
            }
        )
    }
}
    @BindingAdapter("workoutDurationFormatted")
    fun TextView.setWorkoutDurationFormatted(item: Workout?) {
        item?.let {
            text = convertDurationToFormatted(
                item.startTimeMilli,
                item.endTimeMilli,
                context.resources
            )
        }
    }

    @BindingAdapter("workoutQualityString")
    fun TextView.setWorkoutQualityString(item: Workout?) {
        item?.let {
            text = convertNumericQualityToString(item.workoutQuality, context.resources)
        }
    }
