package com.tejas.stackoverflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tejas.stackoverflow.databinding.FragmentListBinding
import com.tejas.stackoverflow.ui.adapter.QuestionAdapter
import com.tejas.stackoverflow.viewmodel.MainViewModel

class ListFragment : Fragment() {

    private var viewBinding: FragmentListBinding? = null
    private val binding get() = viewBinding!!
    private val questionAdapter = QuestionAdapter()
    private var mainViewModel: MainViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvQuestions.adapter = questionAdapter

        mainViewModel = (requireActivity() as MainActivity).mainViewModel
        mainViewModel?.getQuestionList()?.observe(viewLifecycleOwner) {
            questionAdapter.setData(it.items)
        }
    }

}