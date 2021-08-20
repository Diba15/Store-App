package com.dicoding.picodiploma.store.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dicoding.picodiploma.store.data.item.InsertItemResponse
import com.dicoding.picodiploma.store.databinding.FragmentAddBinding
import com.dicoding.picodiploma.store.network.RetrofitClient
import me.abhinay.input.CurrencySymbols
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddFragment(id: Int) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAddBinding
    private var idUser = id


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
        binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        binding.ETHargaBarang.setCurrency(CurrencySymbols.INDONESIA)
        binding.ETHargaBarang.setDelimiter(true)
        binding.ETHargaBarang.setDecimals(false)
        binding.saveButton.setOnClickListener {
            val itemName = binding.ETNamaBarang.text.toString()
            val itemPrice = binding.ETHargaBarang.text.toString()
            val itemStock = binding.ETStok.text.toString()

            if (itemName == "") {
                binding.ETNamaBarang.error = "Nama tidak boleh kosong!"
            } else if (itemPrice == "") {
                binding.ETHargaBarang.error = "Harga tidak boleh kosong!"
            }else if (itemStock == "") {
                binding.ETStok.error = "Stok tidak boleh kosong!"
            } else {
                addStatistik(itemName, binding.ETHargaBarang.cleanIntValue, itemStock.toInt())
                binding.ETNamaBarang.text = null
                binding.ETHargaBarang.text = null
                binding.ETStok.text = null
            }
        }
        return binding.root
    }

    private fun addStatistik(itemName: String, itemPrice: Int, itemStock: Int) {
        val api = RetrofitClient().getInstance()
        with(api) {
            insertItem(idUser, itemName, itemPrice, itemStock).enqueue(object :
                Callback<InsertItemResponse> {
                override fun onResponse(
                    call: Call<InsertItemResponse>,
                    response: Response<InsertItemResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.response == true) {
                            Toast.makeText(
                                context,
                                "Barang Berhasil ditambahkan",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Gagal menambahkan",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "Gagal", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<InsertItemResponse>, t: Throwable) {
                    t.message?.let { Log.e("ErrorRetro", it) }
                }

            })
        }
    }
}