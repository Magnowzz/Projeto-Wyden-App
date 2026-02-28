package com.lanchonete.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lanchonete.app.R
import com.lanchonete.app.database.entity.ItemPedido
import java.util.Locale

class ItemPedidoListAdapter(
    private val produtosMap: Map<Long, String> = emptyMap()
) : ListAdapter<ItemPedido, ItemPedidoListAdapter.ItemViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_item_pedido_lista, parent, false)
        return ItemViewHolder(view, produtosMap)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(
        itemView: View,
        private val produtosMap: Map<Long, String>
    ) : RecyclerView.ViewHolder(itemView) {
        private val textProduto: TextView = itemView.findViewById(R.id.textProduto)
        private val textQuantidade: TextView = itemView.findViewById(R.id.textQuantidade)
        private val textPreco: TextView = itemView.findViewById(R.id.textPreco)
        private val textSubtotal: TextView = itemView.findViewById(R.id.textSubtotal)

        fun bind(item: ItemPedido) {
            textProduto.text = produtosMap[item.produtoId] ?: "Produto #${item.produtoId}"
            textQuantidade.text = "Qtd: ${item.quantidade}"
            textPreco.text = String.format(Locale.getDefault(), "R$ %.2f", item.precoUnitario)
            textSubtotal.text = String.format(Locale.getDefault(), "Subtotal: R$ %.2f", item.subtotal)
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<ItemPedido>() {
        override fun areItemsTheSame(oldItem: ItemPedido, newItem: ItemPedido): Boolean {
            return oldItem.pedidoId == newItem.pedidoId && oldItem.produtoId == newItem.produtoId
        }

        override fun areContentsTheSame(oldItem: ItemPedido, newItem: ItemPedido): Boolean {
            return oldItem == newItem
        }
    }
}

