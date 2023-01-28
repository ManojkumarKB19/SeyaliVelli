package com.example.myapplication.ui.designList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDesignCategoriesBinding
import com.example.myapplication.databinding.FragmentImageViewBinding
import com.example.myapplication.ui.home.HomeViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ImageViewFragment: Fragment() {

    private var _binding: FragmentImageViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).supportActionBar?.title = ImageViewFragmentArgs.fromBundle(arguments!!).imageName
        Log.d("ImageViewFragment","on Create view called")
        _binding = FragmentImageViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icClose.setOnClickListener {
            imageUrl = ""
            findNavController().popBackStack()
        }

        imageUrl = ImageViewFragmentArgs.fromBundle(arguments!!).imageUrl

        if(!imageUrl.isEmpty()){
            Picasso.get()
                .load(imageUrl)
                .resize(1500, 0)
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(R.drawable.ic_launcher_background)
                .into(binding.imgvw);
        }

    }

}