package com.nutriast.ui.main.intakehistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nutriast.data.remote.pojo.IntakeData
import com.nutriast.databinding.ItemRowDailyIntakeBinding
import java.text.SimpleDateFormat
import java.util.Locale

class IntakeHistoryAdapter(
    private val listUserIntake: ArrayList<IntakeData>
) : RecyclerView.Adapter<IntakeHistoryAdapter.ListViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(data: IntakeData)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowDailyIntakeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val userIntake = listUserIntake[position]
        holder.tvUserDailyIntakeDate.text = changeDateFormat(userIntake.createdAt)
        holder.tvUserDailyIntakeStatus.text = userIntake.healthstatus
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUserIntake[holder.adapterPosition])
        }
    }

    private fun changeDateFormat(createdAt: String?): String {
        if (createdAt != null) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = inputFormat.parse(createdAt)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = outputFormat.format(date!!)
            val parts = formattedDate.split("-")
            val year = parts[0]
            val month = parts[1]
            val day = parts[2]
            return "$day/$month/$year"
        }
        return "Date is missing"
    }

    override fun getItemCount(): Int = listUserIntake.size

    class ListViewHolder(binding: ItemRowDailyIntakeBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvUserDailyIntakeDate = binding.tvUserDailyIntakeDate
        val tvUserDailyIntakeStatus = binding.tvUserDailyIntakeStatus
    }

}