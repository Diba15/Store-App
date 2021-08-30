package com.dicoding.picodiploma.store

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.store.data.viewItem.Item
import com.dicoding.picodiploma.store.databinding.ItemViewBinding
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter(
    private val data: ArrayList<Item>,
    var handler: (Int, Item) -> Unit
) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>(), Filterable {

    var filterList = ArrayList<Item>()

    init {
        filterList = data
    }

    class ViewHolder(val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Item) = with(binding) {
            binding.itemName.text = item.item_name
            val formatter = DecimalFormat("###,###,##0")
            val priceFormat = formatter.format(item.item_price)
            binding.itemPrice.text = "Rp.${priceFormat}"
            binding.itemQty.text = "${item.item_stock}x"
            binding.minStok.text = "Min Stok: ${item.min_stok}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filterList[position])
        val item = filterList[position]
        with(holder) {
            bind(item)
            binding.root.setOnClickListener { handler(position, item) }
        }
    }

    override fun getFilter(): Filter {
        return object  : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filterList = if (charSearch.isEmpty()) {
                    data
                } else {
                    val resultList = ArrayList<Item>()
                    for (row in data) {
                        if (row.item_name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<Item>
                notifyDataSetChanged()
            }
        }
    }


}