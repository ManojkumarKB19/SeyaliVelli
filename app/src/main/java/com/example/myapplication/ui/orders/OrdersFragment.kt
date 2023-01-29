package com.example.myapplication.ui.orders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.dao.DesignDao
import com.example.myapplication.databinding.FragmentOrdersBinding
import com.example.myapplication.dm.DesignListDataModel
import com.example.myapplication.dm.OrderListDataModel
import com.example.myapplication.util.LocalHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var localHelper: LocalHelper

    var orderList:ArrayList<OrderListDataModel>?= ArrayList()
    private var key = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[OrdersViewModel::class.java]

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* binding.recyclerVwDesignItems.addOnScrollListener(object : RecyclerView.OnScrollListener(){

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
        })*/

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        DesignDao.getOrders("",localHelper.KEY_USER_ID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val mdl = postSnapshot.getValue<OrderListDataModel>()!!
                    mdl.Key = postSnapshot.key!!
                    orderList?.add(mdl)
                    Log.d("OrdersList",postSnapshot.getValue<OrderListDataModel>()?.Name.toString())
                    key  = postSnapshot.key!!
                    Log.d("DesignList","key after iterate "+key)
                }
                /*adapter?.setItems(designLst!!)
                adapter?.notifyDataSetChanged()
                isLoading= false
                binding.swipeRefresh.isRefreshing = false*/

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("OrdersList","on cancelled "+error)
                //binding.swipeRefresh.isRefreshing = false
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}