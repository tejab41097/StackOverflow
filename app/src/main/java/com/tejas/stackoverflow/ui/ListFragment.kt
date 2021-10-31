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
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.tejas.stackoverflow.R
import com.tejas.stackoverflow.databinding.FragmentListBinding
import com.tejas.stackoverflow.model.Question
import com.tejas.stackoverflow.ui.adapter.QuestionAdapter
import com.tejas.stackoverflow.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFragment : Fragment(), QuestionAdapter.ItemClickListener {

    private var viewBinding: FragmentListBinding? = null
    private val binding get() = viewBinding!!
    private val questionAdapter = QuestionAdapter()
    private lateinit var mainViewModel: MainViewModel
    private var searchText = ""
    private var listBeforeSearch = mutableListOf<Question?>()
    private var navController: NavController? = null
    private var selectedFilter = ""

    // LiveData used to do calculations on IO thread
    private val answerCountLiveData = MutableLiveData<Double>()
    private val viewCountLiveData = MutableLiveData<Double>()

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

        answerCountLiveData.observe(viewLifecycleOwner) {
            binding.tvAvgAnswerCount.text =
                getString(R.string.average_answer_count, it)
        }
        viewCountLiveData.observe(viewLifecycleOwner) {
            binding.tvAvgViewCount.text =
                getString(R.string.average_view_count, it)
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
            binding.tvAvgAnswerCount.isVisible = false
            binding.tvAvgViewCount.isVisible = false
        } else {
            binding.rvQuestions.isVisible = true
            binding.tvNoResultsFound.isVisible = false
            binding.tvAvgAnswerCount.isVisible = true
            binding.tvAvgViewCount.isVisible = true
            countAnswersAndViews(list)
        }
    }

    private fun countAnswersAndViews(list: MutableList<Question?>) {
        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            var answerCount = 0.toDouble()
            var viewCount = 0.toDouble()
            list.forEach {
                it?.let {
                    answerCount += it.answer_count
                    viewCount += it.view_count
                }
            }

            viewCountLiveData.postValue(viewCount / list.size)
            answerCountLiveData.postValue(answerCount / list.size)
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