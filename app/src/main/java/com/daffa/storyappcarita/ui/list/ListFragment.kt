package com.daffa.storyappcarita.ui.list

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.daffa.storyappcarita.databinding.FragmentListBinding
import com.daffa.storyappcarita.util.UserPreference
import com.daffa.storyappcarita.util.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel
    private val adapter = ListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataStore = requireContext().dataStore
        initViewModel(dataStore)
        initData()
    }

    private fun initData() {
        binding.rvStories.visibility = View.GONE
        binding.loadingMain.visibility = View.VISIBLE
        binding.rvStories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingListAdapter {
                adapter.retry()
            }
        )
        loadData()
    }

    private fun initViewModel(dataStore: DataStore<Preferences>) {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), requireContext())
        )[ListViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        loadData()
        binding.rvStories.smoothScrollToPosition(0)
    }

    private fun loadData(){
        viewModel.getUser().observe(requireActivity()) {
            if (it.token != null) {
                viewModel.getPagingStories("Bearer ${it.token}").observe(requireActivity()) { result ->
                    binding.rvStories.visibility = View.VISIBLE
                    binding.loadingMain.visibility = View.GONE
                    adapter.submitData(lifecycle, result)
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data stories", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

}