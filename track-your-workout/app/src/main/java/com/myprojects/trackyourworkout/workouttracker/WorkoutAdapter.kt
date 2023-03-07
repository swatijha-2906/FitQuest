package com.myprojects.trackyourworkout.workouttracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.trackyourworkout.database.Workout
import com.myprojects.trackyourworkout.databinding.ListItemWorkoutBinding


class WorkoutAdapter(val clickListener: WorkoutListener): ListAdapter<Workout, WorkoutAdapter.ViewHolder>(WorkoutListDiffCallback()){
//    var data= listOf<Workout>()
//
//    set(value){
//        field= value
//        notifyDataSetChanged()
//    }
//
//    override fun getItemCount()= data.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int ) {
       // val item = data[position]
        val item= getItem(position)
        holder.bind(clickListener,getItem(position))

    }




    class ViewHolder private constructor(val binding: ListItemWorkoutBinding): RecyclerView.ViewHolder(binding.root) {


        fun bind(clickListener: WorkoutListener, item: Workout) {
            binding.workout = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding= ListItemWorkoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }



}

class WorkoutListDiffCallback :
    DiffUtil.ItemCallback<Workout>() {
    override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem.dayId== newItem.dayId
    }

    override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem==newItem
    }
}

class WorkoutListener(val clickListener: (dayId: Long) -> Unit) {
    fun onClick(day: Workout) = clickListener(day.dayId)
}