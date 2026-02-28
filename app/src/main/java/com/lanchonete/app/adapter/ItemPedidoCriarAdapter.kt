package com.lanchonete.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.lanchonete.app.R
import java.util.Locale

class ItemPedidoCriarAdapter(
    private val onRemoverClick: (ItemPedidoCriar) -> Unit
) : ListAdapter<ItemPedidoCriarAdapter.ItemPedidoCriar, ItemPedidoCriarAdapter.ItemViewHolder>(ItemDiffCallback()) {

    data class ItemPedidoCriar(
        val produtoId: Long,
        val nome: String,
        val quantidade: Int,
        val precoUnitario: Double,
        val subtotal: Double
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_item_pedido_criar, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textNome: TextView = itemView.findViewById(R.id.textNome)
        private val textQuantidade: TextView = itemView.findViewById(R.id.textQuantidade)
        private val textPreco: TextView = itemView.findViewById(R.id.textPreco)
        private val textSubtotal: TextView = itemView.findViewById(R.id.textSubtotal)
        private val btnRemover: MaterialButton = itemView.findViewById(R.id.btnRemover)

        fun bind(item: ItemPedidoCriar) {
            textNome.text = item.nome
            textQuantidade.text = "${item.quantidade}"
            textPreco.text = String.format(Locale.getDefault(), "%.2f", item.precoUnitario)
            textSubtotal.text = String.format(Locale.getDefault(), "R$ %.2f", item.subtotal)

            btnRemover.setOnClickListener {
                onRemoverClick(item)
            }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<ItemPedidoCriar>() {
        override fun areItemsTheSame(oldItem: ItemPedidoCriar, newItem: ItemPedidoCriar): Boolean {
            return oldItem.produtoId == newItem.produtoId
        }

        override fun areContentsTheSame(oldItem: ItemPedidoCriar, newItem: ItemPedidoCriar): Boolean {
            return oldItem == newItem
        }
    }
}


