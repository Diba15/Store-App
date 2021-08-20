package com.dicoding.picodiploma.store

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.store.data.item.UpdateItemResponse
import com.dicoding.picodiploma.store.data.viewItem.Item
import com.dicoding.picodiploma.store.databinding.ActivityMainBinding
import com.dicoding.picodiploma.store.network.RetrofitClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newList: ArrayList<Item>
    private lateinit var mainAdapter: MainAdapter
    private var idUser: Int = 0
    private var toko: Int = 0
    private val api = RetrofitClient().getInstance()
    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "LoginPref"
    private val KEY_USERNAME = "key_username"
    private val KEY_TOKO = "key_toko";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        toko = intent.getIntExtra("toko", 1)
        title = getUsername() + " - Toko " + toko

        readItem()
        refresh()
        binding.refreshBtn.setOnClickListener {
            binding.rvItem.adapter = null
            readItem()
        }
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.log_out) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
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
        with(api) {
            readItem(getToko()).enqueue(object : Callback<ArrayList<Item>> {
                override fun onResponse(
                    call: Call<ArrayList<Item>>,
                    response: Response<ArrayList<Item>>
                ) {
                    if (response.isSuccessful) {
                        newList = response.body()!!
                        mainAdapter = MainAdapter(newList) { _, item ->
                            val bundleStatus = Bundle()
                            with(bundleStatus) {
                                putInt("id", item.item_id)
                                putString("itemName", item.item_name)
                                putInt("itemPrice", item.item_price)
                                putInt("itemStock", item.item_stock)
                            }
                            MaterialAlertDialogBuilder(this@MainActivity)
                                .setView(R.layout.dialog_layout)
                                .setTitle(bundleStatus.getString("itemName"))
                                .setMessage(R.string.hint_stok)
                                .setNeutralButton("Cancel") { _, _ ->

                                }
                                .setPositiveButton("Tambah") { dialog, _ ->
                                    val stok =
                                        (dialog as? AlertDialog)?.findViewById<TextInputEditText>(R.id.stok_editText)?.text.toString()
                                            .toInt()
                                    val result = bundleStatus.getInt("itemStock", 0) + stok
                                    updateItem(bundleStatus.getInt("id"), result, "Ditambahkan")
                                    refresh()
                                    dialog.dismiss()
                                }
                                .setNegativeButton("Kurang") { dialog, _ ->
                                    val stok =
                                        (dialog as? AlertDialog)?.findViewById<TextInputEditText>(R.id.stok_editText)?.text.toString()
                                            .toInt()
                                    val result = bundleStatus.getInt("itemStock", 0) - stok
                                    updateItem(bundleStatus.getInt("id"), result, "Dikurangi")
                                    refresh()
                                    dialog.dismiss()
                                }
                                .show()
                        }

                        with(binding.rvItem) {
                            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
                            adapter = mainAdapter
                            setHasFixedSize(true)
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Gagal", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                    t.message?.let { Log.e("ErrorRetro", it) }
                }

            })
        }
    }

    private fun updateItem(itemId: Int, stokItem: Int, status: String) {
        Log.e("e", itemId.toString())
        with(api) {
            updateItem(toko,stokItem, itemId).enqueue(object : Callback<UpdateItemResponse> {
                override fun onResponse(
                    call: Call<UpdateItemResponse>,
                    response: Response<UpdateItemResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.response == true) {
                            Snackbar.make(
                                binding.root,
                                "Stok Berhasil $status",
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                            binding.rvItem.adapter = null
                            readItem()
                        } else {
                            Snackbar.make(binding.root, "Gagal Mengubah Stok", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Gagal", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UpdateItemResponse>, t: Throwable) {
                    t.message?.let { Log.e("ErrorRetro", it) }
                }

            })
        }
    }

    private fun getUsername(): String? = sharedPreferences.getString(KEY_USERNAME, null)
    private fun getToko(): Int = sharedPreferences.getInt(KEY_TOKO, 0)
}