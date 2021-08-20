package com.dicoding.picodiploma.store

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.store.data.viewItem.Item
import com.dicoding.picodiploma.store.databinding.ItemViewBinding
import java.text.DecimalFormat

class MainAdapter(
    private val data: ArrayList<Item>,
    var handler: (Int, Item) -> Unit
) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) = with(binding) {
            binding.itemName.text = item.item_name
            val formatter = DecimalFormat("###,###,##0")
            val priceFormat = formatter.format(item.item_price)
            binding.itemPrice.text = "Rp.${priceFormat}"
            binding.itemQty.text = "${item.item_stock}x"
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
        val item = data[position]
        with(holder) {
            bind(item)
            binding.root.setOnClickListener { handler(position, item) }
        }
    }

}