package com.example.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.dm.CategoryModel
import com.example.myapplication.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(),CategoryListAdapter.ItemClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var adapter: CategoryListAdapter? = null
    var categoryLst:ArrayList<CategoryModel>?= ArrayList()
   lateinit  var database: DatabaseReference

   lateinit var homeViewModel:HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =  ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        database = Firebase.database.reference

        Log.d("HomeFragment","on Create view called")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


     /*   val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      /*  homeViewModel.categoryLst.observe(viewLifecycleOwner){
            Log.d("HomeFragment","observing the lifecycle")
            categoryLst = it
            if((categoryLst!=null)&&(!categoryLst?.isEmpty()!!)){
                Log.d("HomeFragment","array list is not empty setting the adapter")
                Toast.makeText(requireContext(),"array list is not empty setting the adapter",Toast.LENGTH_LONG).show()
                setGridAdapter(categoryLst!!)
            }else{
                Log.d("HomeFragment","array list is empty")
                Toast.makeText(requireContext(),"array list is empty",Toast.LENGTH_LONG).show()
            }
        }*/

       // setGridAdapter(categoryLst!!)
        val myTopPostsQuery = database.child(Constant.PARAM_ITEMS)
        var lstCategory:ArrayList<CategoryModel>  = ArrayList()

       /* homeViewModel.getCategoryList().observe(requireActivity(), Observer { it->
            categoryLst = it
            Log.d("HomeFragment"," cat. list "+categoryLst)
            setGridAdapter(categoryLst!!)
        })

        if((categoryLst!=null)&&(!categoryLst?.isEmpty()!!)) {
            setGridAdapter(categoryLst!!)
        }*/

        binding.progressBar.visibility = View.VISIBLE
        myTopPostsQuery.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                binding.progressBar.visibility = View.GONE
                categoryLst?.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val parent: String = postSnapshot.getKey().let { it }?:""
                    Log.i("HomeViewModel", parent)
                    categoryLst?.add(CategoryModel(parent))
                }
                setGridAdapter(categoryLst!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                // Getting Post failed, log a message
                Log.w("HomeViewModel", "loadPost:onCancelled", databaseError.toException())
                // ...
            }

        })

        Log.d("HomeFragment","on view created called")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        /*homeViewModel.categoryLst.observe(viewLifecycleOwner){
            Log.d("HomeFragment","observing the lifecycle")
            categoryLst = it
            if((categoryLst!=null)&&(!categoryLst?.isEmpty()!!)){
                Log.d("HomeFragment","array list is not empty setting the adapter")
                Toast.makeText(requireContext(),"array list is not empty setting the adapter",Toast.LENGTH_LONG).show()
                setGridAdapter(categoryLst!!)
            }else{
                Log.d("HomeFragment","array list is empty")
                Toast.makeText(requireContext(),"array list is empty",Toast.LENGTH_LONG).show()
            }
        }*/
    }

    override fun onItemClick(view: View?, position: Int) {
        //Toast.makeText(requireContext(),"clicked",Toast.LENGTH_LONG).show()
        //startActivity(DesignCategoriesFragment.getCallingIntent(requireContext(),categoryLst?.get(position)?.categoryName?.let { it }?:""))


    // Navigate using the IDs you defined in your Nav Graph
        //NavController.navigate(R.id.blankFragment)
        findNavController(requireView()).navigate(R.id.action_navigation_home_to_navigation_design_categories)
    }

    fun setGridAdapter(categoryLst:ArrayList<CategoryModel>){
        val numberOfColumns = 2
        Log.d("HomeFragment","setting to adapter")
        binding.gridCategoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        adapter = CategoryListAdapter(categoryLst,requireContext(),this)
        binding.gridCategoryRecyclerView.adapter = adapter
        binding.gridCategoryRecyclerView.adapter?.notifyDataSetChanged()
    }
}