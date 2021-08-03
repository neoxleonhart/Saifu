package cl.neoxcore.saifu.ui.transaction.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cl.neoxcore.saifu.databinding.ViewItemTransactionBinding
import cl.neoxcore.saifu.presentation.model.UiTransaction
import javax.inject.Inject

class TransactionAdapter @Inject constructor() : RecyclerView.Adapter<ViewHolder>() {
    private var items: List<UiTransaction> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewItemTransactionBinding.inflate(from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = items[position]
        (holder as TransactionViewHolder).bind(transaction)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun addTransactions(items: List<UiTransaction>) {
        this.items = items
        notifyDataSetChanged()
    }
}
