package com.dicoding.picodiploma.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.store.data.Barang
import com.dicoding.picodiploma.store.databinding.ItemViewBinding
import java.text.DecimalFormat

class MainAdapter(
    private val data: List<Barang>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: Barang) = with(binding) {
            binding.itemName.text = barang.name
            val formatter = DecimalFormat("###,###,##0.00")
            val priceFormat = formatter.format(barang.price)
            binding.itemPrice.text = "Rp.${priceFormat}"
            binding.itemQty.text = "${barang.quantity}x"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

}