package com.example.myapplication.ui.designDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.LayoutDesignDetailFragmentBinding
import com.example.myapplication.dm.DesignListDataModel
import com.example.myapplication.dm.SizeItem
import com.example.myapplication.util.LocalHelper
import com.example.myapplication.utils.Constant
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DesignDetailFragment:Fragment(),DesignSizeListAdapter.DesignSizeItemClickListener {

    private var _binding: LayoutDesignDetailFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var designItem: DesignListDataModel

    var adapter: DesignSizeListAdapter? = null
    //var designLst:Hash<DesignListDataModel>?= ArrayList()
    var designSizeValueList:ArrayList<SizeItem>? = ArrayList()

    var wishSelected = false

    @Inject
    lateinit var localHelper: LocalHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var str = DesignDetailFragmentArgs.fromBundle(arguments!!).designItem

        val gson = Gson()
        designItem = gson.fromJson(str, DesignListDataModel::class.java)

       (activity as MainActivity).supportActionBar?.title = designItem.Key

        _binding = LayoutDesignDetailFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try{

            binding.imgWish.setOnClickListener {
                wishSelected = !wishSelected

                if(wishSelected){
                    binding.imgWish.setImageResource(R.drawable.ic_favorite_filled)
                }else{
                    binding.imgWish.setImageResource(R.drawable.ic_favorite_outline)
                }


            }
            if(!designItem.ImageUrl.isEmpty()){
                Picasso.get()
                    .load(designItem.ImageUrl)
                    .resize(1500, 0)
                    .rotate(270f)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.imgView);
            }

            binding.nameValue.setText(designItem.Name)

            binding.imgView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(Constant.ARG_IMAGE_URL, designItem.ImageUrl)
                bundle.putString(Constant.ARG_IMAGE_NAME,designItem.Key)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_design_detail_to_image_view,bundle)
            }

            // designValueList = designItem.Size?.values

            designSizeValueList?.clear()
            designItem.Size?.let{
                for ((k, v) in designItem.Size!!) {
                    println(" Subject Name -> $k and its preference -> $v")

                    var gson = Gson()
                    val jsonObject: JsonObject = gson.toJsonTree(v).getAsJsonObject()

                    var designSizeItem  = gson.fromJson(jsonObject, SizeItem::class.java)

                    designSizeValueList?.add(designSizeItem)
                }
            }


            if((designSizeValueList!=null)&&(!designSizeValueList?.isEmpty()!!)){
                setGridAdapter(designSizeValueList!!)
            }

            Log.d("DesignDetailFragment",designSizeValueList.toString())

            binding.txtOrder.setOnClickListener{
                Log.d("DesignDetail",localHelper.KEY_USER_ID)
                progressBarVisibility(View.VISIBLE)
                placeOrder(designItem,localHelper.KEY_USER_ID)
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
        //imageUrl = ImageViewFragmentArgs.fromBundle(arguments!!).imageUrl


    }

    fun setGridAdapter(designSizeList:ArrayList<SizeItem>){
        val numberOfColumns = 1
       // designCategoryLst?.clear()
        //designCategoryLst = designCatgryLst
        Log.d("DesignCategoriesFragment","setting to adapter")
        binding.sizeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = DesignSizeListAdapter(requireContext(),designSizeList,this)
        binding.sizeRecyclerView.adapter = adapter
        binding.sizeRecyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onDesignSizeItemClick(view: View?, position: Int) {
        Log.d("DesignDetailFrag",designSizeValueList?.get(position).toString())

        designSizeValueList?.get(position)?.isSelected = !designSizeValueList?.get(position)?.isSelected!!
        adapter?.setItems(designSizeValueList!!)
        //adapter?.setThisItem(position,designSizeValueList?.get(position).toString())
    }

    override fun onAddClick(view: View?, position: Int) {
        designSizeValueList?.get(position)?.quantities = designSizeValueList?.get(position)?.quantities?.inc()!!
        adapter?.setItems(designSizeValueList!!)
    }

    override fun onSubClick(view: View?, position: Int) {
        if(designSizeValueList?.get(position)?.quantities!=0){
            designSizeValueList?.get(position)?.quantities = designSizeValueList?.get(position)?.quantities?.dec()!!
            adapter?.setItems(designSizeValueList!!)
        }
    }

    fun progressBarVisibility(view:Int){
        _binding?.progressBar?.visibility = view
    }

    fun placeOrder(designItem: DesignListDataModel, userId:String) {
        val  database = Firebase.database.reference
        Log.d("DesignDao",userId)
        database.child(Constant.USER).child(userId).child(Constant.ORDER).push().setValue(
            designItem, object : DatabaseReference.CompletionListener {
                override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                    progressBarVisibility(View.GONE)
                    if (error != null) {
                        System.out.println("Data could not be saved. " + error.getMessage())
                        showToast(getString(R.string.txt_something_went_wrong_please_contact_administrator))
                    } else {
                        showToast(getString(R.string.txt_data_saved_successfully))
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_navigation_design_detail_to_order)
                    }
                }
            }
        )
    }

    fun showToast(msg:String){
        Toast.makeText(requireContext(),msg,Toast.LENGTH_LONG).show()
    }
}