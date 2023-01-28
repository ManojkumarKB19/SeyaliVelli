package com.example.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDesignCategoriesBinding
import com.example.myapplication.di.DesignCategoryModel
import com.example.myapplication.utils.Constant
import com.example.myapplication.utils.Constant.ARG_DESIGN_CATEGORY
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class DesignCategoriesFragment:Fragment(),DesignCategoryAdapter.ItemClickListener {

    private var _binding: FragmentDesignCategoriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var adapter: DesignCategoryAdapter? = null
    var designCategoryLst:ArrayList<DesignCategoryModel>?= ArrayList()
    lateinit  var database: DatabaseReference

    lateinit var homeViewModel:HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =  ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        database = Firebase.database.reference

        Log.d("DesignCategoriesFragment","on Create view called")
        _binding = FragmentDesignCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myTopPostsQuery = database.child(Constant.PARAM_ITEMS).child(Constant.PARAM_ANKLETS)
        var designCategoryLst:ArrayList<DesignCategoryModel>  = ArrayList()

        binding.progressBar.visibility = View.VISIBLE
        myTopPostsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                binding.progressBar.visibility = View.GONE
                for (postSnapshot in dataSnapshot.children) {
                    val parent: String = postSnapshot.getKey().let { it }?:""
                    Log.i("DesignCategoriesFragment", parent)
                    designCategoryLst?.add(DesignCategoryModel(parent))
                }
                setGridAdapter(designCategoryLst!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                // Getting Post failed, log a message
                Log.w("DesignCategoriesFragment", "loadPost:onCancelled", databaseError.toException())
                // ...
            }

        })

    }

    fun setGridAdapter(designCatgryLst:ArrayList<DesignCategoryModel>){
        val numberOfColumns = 2
        designCategoryLst?.clear()
        designCategoryLst = designCatgryLst
        Log.d("DesignCategoriesFragment","setting to adapter")
        binding.gridDesignCategories.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        adapter = DesignCategoryAdapter(designCatgryLst,requireContext(),this)
        binding.gridDesignCategories.adapter = adapter
        binding.gridDesignCategories.adapter?.notifyDataSetChanged()
    }

    override fun onItemClick(view: View?, position: Int) {
        //Toast.makeText(requireContext(),designCategoryLst?.get(position)?.designCategoryModel,Toast.LENGTH_LONG).show()

        // Toast.makeText(requireContext(), "Position = " + position + "\n Item = " + data.getCompanyName(), Toast.LENGTH_SHORT).show();
        val bundle = Bundle()
        bundle.putString(ARG_DESIGN_CATEGORY, designCategoryLst?.get(position)?.designCategoryModel?.let { it }?:"")
        findNavController(requireView()).navigate(R.id.action_navigation_design_category_to_navigation_designs,bundle)
    }

}