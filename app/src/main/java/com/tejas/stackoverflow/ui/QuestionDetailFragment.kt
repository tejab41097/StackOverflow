package com.tejas.stackoverflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tejas.stackoverflow.databinding.FragmentQuestionDetailBinding

class QuestionDetailFragment : Fragment() {

    private var viewBinding: FragmentQuestionDetailBinding? = null
    private val binding get() = viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentQuestionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).viewBindingMainActivity.toolbar.title =
            requireArguments().getString("title")

        var url = requireArguments().getString("URL")
        if (url == null)
            url = "https://www.google.com/search?q=advertisement"
        binding.webView.loadUrl(url)
    }

}