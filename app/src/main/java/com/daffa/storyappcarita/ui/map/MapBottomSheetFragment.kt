package com.daffa.storyappcarita.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.daffa.storyappcarita.databinding.FragmentMapBottomSheetListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MapBottomSheetFragment(private val snippet: String?,private val title: String?) : BottomSheetDialogFragment() {

    private var _binding: FragmentMapBottomSheetListDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBottomSheetListDialogBinding.inflate(inflater, container, false)
        binding.tvUsername.text = title
        Glide.with(requireActivity())
            .load(snippet)
            .apply(RequestOptions().override(500, 500))
            .into(binding.imageView)
        return binding.root
    }


    companion object {
        const val TAG = "BottomSheet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}