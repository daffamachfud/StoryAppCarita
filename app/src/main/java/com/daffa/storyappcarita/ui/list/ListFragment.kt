package com.daffa.storyappcarita.ui.list

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.daffa.storyappcarita.databinding.FragmentListBinding
import com.daffa.storyappcarita.ui.main.MainViewModel
import com.daffa.storyappcarita.ui.main.StoriesAdapter

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val storiesAdapter = StoriesAdapter()
    private var tokenIntent = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        initView()
        initViewModel()
        return binding.root
    }

    private fun initViewModel() {

    }

    private fun initView() {

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}