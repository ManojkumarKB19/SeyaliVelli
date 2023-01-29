package com.example.myapplication.ui.designList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.dao.DesignDao
import com.example.myapplication.databinding.FragmentDesignListBinding
import com.example.myapplication.dm.DesignListDataModel
import com.example.myapplication.ui.home.HomeViewModel
import com.example.myapplication.utils.Constant
import com.example.myapplication.utils.Constant.ARG_DESIGN_ITEM
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson


class DesignListFragment : Fragment(),DesignListAdapter.DesignItemClickListener {

    private var _binding: FragmentDesignListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var adapter: DesignListAdapter? = null
    var designLst:ArrayList<DesignListDataModel>?= ArrayList()
    var isLoading = false

    lateinit  var database: DatabaseReference
    lateinit var homeViewModel: HomeViewModel
    private var designCategoryStr = ""
    private var key = ""
    private var numberOfDesigns = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).supportActionBar?.title = DesignListFragmentArgs.fromBundle(arguments!!).designCategory

        homeViewModel =  ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        database = Firebase.database.reference

        Log.d("DesignListFragment","on Create view called")
        _binding = FragmentDesignListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        designCategoryStr = DesignListFragmentArgs.fromBundle(arguments!!).designCategory

        binding.txtTest.setText(designCategoryStr)

        getDesignsCount()
        setAdapter()

        binding.swipeRefresh.setOnRefreshListener {
            setAdapter()
        }
    }

    private fun getDesignsCount() {
        val  database = Firebase.database.reference
        database.child(Constant.PARAM_ITEMS).child(Constant.PARAM_ANKLETS).child("S Design").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                numberOfDesigns = dataSnapshot.childrenCount
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setAdapter() {
        Log.d("DesignList","setting the adapter")
        adapter = DesignListAdapter(requireContext(), ArrayList(),this)
        var mgr = LinearLayoutManager(requireContext())
        binding.recyclerVwDesignItems.layoutManager = mgr
        binding.recyclerVwDesignItems.adapter = adapter
        loadData()
        binding.recyclerVwDesignItems.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var mgr =  binding.recyclerVwDesignItems.layoutManager as LinearLayoutManager
                var totalItem = mgr?.itemCount
                Log.d("DesignList","totalItem "+totalItem)
                var lastVisible = mgr.findLastCompletelyVisibleItemPosition()
                Log.d("DesignList","lastVisible "+lastVisible)
                if(numberOfDesigns>designLst?.size!!){
                    if(totalItem<lastVisible+3){
                        if(!isLoading) {
                            isLoading = true
                            Log.d("DesignList","Loadind data from key "+key)
                            loadData()
                        }
                    }
                }
            }
        })
        //getFromDesign16()
    }


    private fun loadData() {
        Log.d("DesignList","before function call")
        binding.swipeRefresh.isRefreshing = true
        DesignDao.get(key).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    //  val parent: String = postSnapshot.getKey().let { it }?:""
                    //  Log.i("DesignCategoriesFragment", parent)
                    var mdl = postSnapshot.getValue<DesignListDataModel>()!!
                    mdl.Key = postSnapshot.key!!
                    designLst?.add(mdl)
                    Log.d("DesignList",postSnapshot.getValue<DesignListDataModel>()?.Name.toString())
                    key  = postSnapshot.key!!
                    Log.d("DesignList","key after iterate "+key)
                }
                adapter?.setItems(designLst!!)
                adapter?.notifyDataSetChanged()
                isLoading= false
                binding.swipeRefresh.isRefreshing = false

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DesignList","on cancelled "+error)
                binding.swipeRefresh.isRefreshing = false
            }

        })
        Log.d("DesignList","after the function call")
    }

   /* private fun loadData() {
        Log.d("DesignList","before function call")
        binding.swipeRefresh.isRefreshing = true
        DesignDao.get(key).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val designItemModel: DesignListDataModel? = postSnapshot.getValue(DesignListDataModel::class.java)
                    Log.d("DesignList","key after iterate "+designItemModel.toString())
                  /*  postSnapshot.children.forEach {


                    }*/

                }

              /*  for (firstLevelSnapshot in  snapshot.children) {
                  //var mdl = firstLevelSnapshot.getValue<DesignListDataModel>()!!
                   // mdl.Key = firstLevelSnapshot.key!!
                   // designLst?.add(mdl)
                   Log.d("DesignList","firstLevelSnapshot"+ firstLevelSnapshot.value.toString())

                    Log.d("DesignList","firstLevelSnapshot children"+ firstLevelSnapshot.children.toList().toString())

                    //val designItemModel: DesignListDataModel? = firstLevelSnapshot.getValue(DesignListDataModel::class.java)
                   // Log.d("DesignList",firstLevelSnapshot.getValue<DesignListDataModel>()?.Name.toString())
                    key  = firstLevelSnapshot.key!!
                    //Log.d("DesignList","key after iterate "+key)
                    var sizeList = ArrayList<SizeItem>()
                    for (secondLevelSnapshot in firstLevelSnapshot.children) {
                        val key = secondLevelSnapshot.key
                        //val designItemModel: DesignListDataModel? = secondLevelSnapshot.getValue(DesignListDataModel::class.java)
                        //designLst?.add(key)

                        /*val sizeItem: SizeItem? = secondLevelSnapshot.getValue(SizeItem::class.java)
                        sizeList.add(sizeItem!!)*/

                        /*databaseHelper.addJoueur(
                            joueur.getNom_joueur(),
                            joueur.getPrenom_joueur(),
                            joueur.getNum_licence_joueur(),
                            joueur.getId_equipe_joueur()
                        )*/
                       // Log.d("DesignItem"," inside second level " +secondLevelSnapshot.value)
                        Log.d("DesignItem"," inside second level " +designItemModel.toString())
                    }

                    //designLst?.add(DesignListDataModel(Name = designItemModel?.Name!!, ImageUrl = designItemModel?.ImageUrl,Size = sizeList,designItemModel?.weight,key))
                }
               // Log.d("DesignList",designLst.toString())*/
                binding.swipeRefresh.isRefreshing = false
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("DesignList","on cancelled "+error)
                binding.swipeRefresh.isRefreshing = false
            }

        })
        Log.d("DesignList","after the function call")
    }*/

    private fun getFromDesign16(){
        val  database = Firebase.database.reference

        database.child(Constant.PARAM_ITEMS).child(Constant.PARAM_ANKLETS).child("S Design").orderByChild("Name").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    //  val parent: String = postSnapshot.getKey().let { it }?:""
                    //  Log.i("DesignCategoriesFragment", parent)

                    Log.d("DesignList",postSnapshot.children.toString())

                   /* designLst?.add(postSnapshot.getValue(DesignListDataModel::class.java)!!)
                    Log.d("DesignList",postSnapshot.getValue<DesignListDataModel>()?.Name.toString())
                    key  = postSnapshot.key!!
                    Log.d("DesignList","key after iterate "+key)*/

                }
                adapter?.setItems(designLst!!)
                adapter?.notifyDataSetChanged()
                isLoading= false
                binding.swipeRefresh.isRefreshing = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DesignList","on cancelled "+error)
                binding.swipeRefresh.isRefreshing = false
            }

        })
    }

    override fun onDesignItemClick(view: View?, position: Int) {
        //Toast.makeText(requireContext(),designLst?.get(position)?.Name,Toast.LENGTH_LONG).show()
       /* val bundle = Bundle()
        bundle.putString(Constant.ARG_IMAGE_URL, designLst?.get(position)?.ImageUrl?.let { it }?:"")
        bundle.putString(Constant.ARG_IMAGE_NAME, designLst?.get(position)?.Key?.let { it }?:"")
        Navigation.findNavController(requireView())
            .navigate(R.id.action_navigation_design_list_to_image_view,bundle)*/

        // Toast.makeText(requireContext(), "Position = " + position + "\n Item = " + data.getCompanyName(), Toast.LENGTH_SHORT).show();
        val gson = Gson()
        val str: String = gson.toJson(designLst?.get(position))
        val bundle = Bundle()
        bundle.putString(ARG_DESIGN_ITEM, str)
        findNavController(requireView()).navigate(R.id.action_navigation_design_list_to_design_detail, bundle)
    }
}