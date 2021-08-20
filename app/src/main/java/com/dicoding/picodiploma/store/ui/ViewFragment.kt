package com.dicoding.picodiploma.store.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dicoding.picodiploma.store.MainAdapter
import com.dicoding.picodiploma.store.data.Barang
import com.dicoding.picodiploma.store.data.viewItem.Item
import com.dicoding.picodiploma.store.databinding.FragmentViewBinding
import com.dicoding.picodiploma.store.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewFragment(id: Int) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentViewBinding
    private var id_user: Int = id
    private lateinit var newList: ArrayList<Item>
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewBinding.inflate(layoutInflater, container, false)
        readItem()
        refresh()
        binding.refreshBtn.setOnClickListener {
            binding.rvItem.adapter = null
            readItem()
        }
        Log.e("id", id_user.toString())
        return binding.root
    }

    private fun refresh() {
        binding.swipeRefresh.setOnRefreshListener {
            newList.clear()
            readItem()
            binding.rvItem.adapter?.notifyDataSetChanged()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun readItem() {
        val api = RetrofitClient().getInstance()
        with(api) {
            readItem(id_user).enqueue(object : Callback<ArrayList<Item>> {
                override fun onResponse(
                    call: Call<ArrayList<Item>>,
                    response: Response<ArrayList<Item>>
                ) {
                    if (response.isSuccessful) {
                        newList = response.body()!!
                        mainAdapter = MainAdapter(newList) { _, item ->
                            val bundleStatus = Bundle()
                            with(bundleStatus) {
                                putString("itemName", item.item_name)
                                putInt("itemPrice", item.item_price)
                                putInt("itemStock", item.item_stock)
                            }
                            Toast.makeText(context, bundleStatus.getString("itemName"),Toast.LENGTH_LONG).show()
                        }

                        with(binding.rvItem) {
                            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
                            adapter = mainAdapter
                            setHasFixedSize(true)
                        }
                    } else {
                        Toast.makeText(context, "Gagal", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                    t.message?.let { Log.e("ErrorRetro", it) }
                }

            })
        }
    }
}