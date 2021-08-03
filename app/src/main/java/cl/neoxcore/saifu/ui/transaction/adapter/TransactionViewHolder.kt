package cl.neoxcore.saifu.ui.transaction.adapter

import androidx.recyclerview.widget.RecyclerView
import cl.neoxcore.saifu.databinding.ViewItemTransactionBinding
import cl.neoxcore.saifu.presentation.model.UiTransaction

class TransactionViewHolder(private val binding: ViewItemTransactionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(attrs: UiTransaction) {
        binding.apply {
            dateText.text = attrs.date
            totalText.text = attrs.total.toString()
        }
    }
}
