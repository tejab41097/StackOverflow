package com.tejas.stackoverflow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tejas.stackoverflow.R
import com.tejas.stackoverflow.databinding.ItemAdvertisementBinding
import com.tejas.stackoverflow.databinding.ItemQuestionBinding
import com.tejas.stackoverflow.model.Question
import java.text.SimpleDateFormat
import java.util.*

private const val QUESTION = 0
private const val ADVERTISE = 1

class QuestionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = mutableListOf<Question?>()

    override fun getItemViewType(position: Int): Int {
        return if (list[position] == null)
            ADVERTISE
        else
            QUESTION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ADVERTISE) AdvertisementViewHolder(
            ItemAdvertisementBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        else QuestionViewHolder(
            ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AdvertisementViewHolder) {
            holder.itemBinding.ivAdvertise.setImageResource(R.drawable.advertisement)
        } else if (holder is QuestionViewHolder) {
            val questionBinding = holder.itemBinding
            Glide.with(questionBinding.ivQuestion.context)
                .load(list[position]?.owner?.profile_image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(questionBinding.ivQuestion)

            questionBinding.tvDate.text = list[position]?.creation_date?.getDateTime()
            questionBinding.tvTitle.text = list[position]?.title
            questionBinding.tvDescription.text = list[position]?.owner?.display_name


        }

    }

    override fun getItemCount() = list.size

    fun setData(list: MutableList<Question?>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class QuestionViewHolder(val itemBinding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    inner class AdvertisementViewHolder(val itemBinding: ItemAdvertisementBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    private fun String.getDateTime(): String? {
        return try {
            val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
            val netDate = Date(this.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            null
        }
    }

}