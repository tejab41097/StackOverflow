package com.tejas.stackoverflow.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tejas.stackoverflow.R
import com.tejas.stackoverflow.databinding.ItemFilterBinding

class FilterAdapter : RecyclerView.Adapter<FilterAdapter.FilterListViewHolder>() {
    private var selectedFilter = ""
    private var filterList = mutableListOf<String>()
    lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FilterListViewHolder(
        ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = filterList.size

    fun setFilterList(list: MutableList<String>, selectedFilter: String) {
        this.selectedFilter = selectedFilter
        filterList = list
        notifyDataSetChanged()
    }

    inner class FilterListViewHolder(val itemBinding: ItemFilterBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onBindViewHolder(holder: FilterListViewHolder, position: Int) {
        holder.itemBinding.tvFilter.text = filterList[position]
        if (selectedFilter == filterList[position])
            holder.itemBinding.tvFilter.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemBinding.tvFilter.context,
                    R.color.teal_200
                )
            )
        else
            holder.itemBinding.tvFilter.setBackgroundColor(Color.TRANSPARENT)

        holder.itemBinding.root.setOnClickListener {
            selectedFilter = filterList[position]
            itemClickListener.onItemClicked(selectedFilter)
        }
    }

    interface ItemClickListener {
        fun onItemClicked(selectedOption: String)
    }

}
