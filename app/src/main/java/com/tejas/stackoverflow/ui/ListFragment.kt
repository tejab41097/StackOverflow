package com.tejas.stackoverflow.ui

import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.tejas.stackoverflow.R
import com.tejas.stackoverflow.databinding.FragmentListBinding
import com.tejas.stackoverflow.model.Question
import com.tejas.stackoverflow.ui.adapter.QuestionAdapter
import com.tejas.stackoverflow.viewmodel.MainViewModel

class ListFragment : Fragment(), QuestionAdapter.ItemClickListener {

    private var viewBinding: FragmentListBinding? = null
    private val binding get() = viewBinding!!
    private val questionAdapter = QuestionAdapter()
    private lateinit var mainViewModel: MainViewModel
    private var searchText = ""
    private var listBeforeSearch = mutableListOf<Question?>()
    private var navController: NavController? = null
    private var selectedFilter = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        viewBinding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvQuestions.adapter = questionAdapter
        questionAdapter.itemClickListener = this

        mainViewModel = (requireActivity() as MainActivity).mainViewModel
        navController = (requireActivity() as MainActivity).navController
        mainViewModel.getQuestionList().observe(viewLifecycleOwner) {
            setRecyclerViewOrShowPopup(it)
            listBeforeSearch = it
        }

        mainViewModel.getSelectedFilter().observe(viewLifecycleOwner) {
            selectedFilter = it
            setRecyclerViewOrShowPopup(listBeforeSearch)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        menu.findItem(R.id.menu_search).isVisible = true
        menu.findItem(R.id.menu_filter_inactive).isVisible = true
        val searchItem: MenuItem? = menu.findItem(R.id.menu_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnCloseListener {
            mainViewModel.getQuestionList().value?.let {
                listBeforeSearch = it
                setRecyclerViewOrShowPopup(it)
            }
            true
        }
        searchView.setQuery(searchText, true)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                setList(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                setList(newText)
                return true
            }
        })
    }

    private fun filterSearch(listBeforeSearch: MutableList<Question?>): MutableList<Question?> {
        return if (selectedFilter.isNotEmpty()) {
            val list = mutableListOf<Question?>()
            for (question in listBeforeSearch)
                if (question?.tags?.contains(selectedFilter) == true)
                    list.add(question)
            list
        } else
            listBeforeSearch
    }

    private fun setList(text: String?) {
        if (!text.isNullOrEmpty()) {
            searchText = text
            setRecyclerViewOrShowPopup(searchExercise(listBeforeSearch))
        } else
            setRecyclerViewOrShowPopup(listBeforeSearch)
    }

    private fun searchExercise(
        list2: MutableList<Question?>
    ): MutableList<Question?> {
        val list = mutableListOf<Question?>()
        for (item in list2)
            if (item?.title?.contains(searchText, true) == true)
                list.add(item)
        return list
    }


    private fun setRecyclerViewOrShowPopup(list: MutableList<Question?>) {
        questionAdapter.setData(filterSearch(list))
        if (list.isEmpty()) {
            binding.rvQuestions.isVisible = false
            binding.tvNoResultsFound.isVisible = true
        } else {
            binding.rvQuestions.isVisible = true
            binding.tvNoResultsFound.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter_inactive -> navController?.navigate(R.id.navigation_filter)
        }
        return false
    }

    override fun onItemClicked(question: Question) {
        if (isNetworkConnected())
            navController?.navigate(
                R.id.navigation_question_detail,
                bundleOf("URL" to question.link, "title" to question.title)
            )
        else
            Toast.makeText(
                requireContext(),
                getString(R.string.no_internet_connection),
                Toast.LENGTH_LONG
            ).show()
    }

    private fun isNetworkConnected() =
        (requireActivity().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager).let {
            it.activeNetworkInfo != null && it.activeNetworkInfo!!.isConnected
        }
}