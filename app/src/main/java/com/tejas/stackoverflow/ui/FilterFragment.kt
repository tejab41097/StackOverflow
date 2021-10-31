package com.tejas.stackoverflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tejas.stackoverflow.databinding.FragmentFilterBinding
import com.tejas.stackoverflow.ui.adapter.FilterAdapter
import com.tejas.stackoverflow.viewmodel.MainViewModel

class FilterFragment : BottomSheetDialogFragment(), FilterAdapter.ItemClickListener {

    private var viewBinding: FragmentFilterBinding? = null
    private val binding get() = viewBinding!!
    private var filterAdapter = FilterAdapter()
    private lateinit var mainViewModel: MainViewModel
    private var selectedFilter = ""
    private var filterList = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFilter.adapter = filterAdapter
        filterAdapter.itemClickListener = this

        mainViewModel = (requireActivity() as MainActivity).mainViewModel
        mainViewModel.getSelectedFilter().observe(viewLifecycleOwner) {
            selectedFilter = it
            filterAdapter.setFilterList(filterList, it)
        }
        
        mainViewModel.filterListLiveData.observe(viewLifecycleOwner) {
            filterList = it
            filterAdapter.setFilterList(it, selectedFilter)
        }

        binding.btnClearFilter.setOnClickListener { onItemClicked("") }
    }

    override fun onItemClicked(selectedOption: String) {
        mainViewModel.setSelectedFilter(selectedOption)
        dismiss()
    }


}