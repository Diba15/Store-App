package com.dicoding.picodiploma.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.store.data.Barang

class MainViewModel : ViewModel() {

    private val itemLiveData = MutableLiveData<List<Barang?>>()

    fun insertData(name: String, price: Float, qty: Int) {
        val list = listOf(
            Barang(name, price, qty),
        )
        itemLiveData.value = list
    }

    fun getHasil(): LiveData<List<Barang?>> = itemLiveData
}