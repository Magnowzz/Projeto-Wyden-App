package com.lanchonete.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lanchonete.app.R
import com.lanchonete.app.database.entity.Venda
import java.text.SimpleDateFormat
import java.util.Locale

class VendaAdapter : ListAdapter<Venda, VendaAdapter.VendaViewHolder>(VendaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venda, parent, false)
        return VendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textId: TextView = itemView.findViewById(R.id.textId)
        private val textTotal: TextView = itemView.findViewById(R.id.textTotal)
        private val textPagamento: TextView = itemView.findViewById(R.id.textPagamento)
        private val textData: TextView = itemView.findViewById(R.id.textData)

        fun bind(venda: Venda) {
            textId.text = "Venda #${venda.id}"
            textTotal.text = String.format(Locale.getDefault(), "R$ %.2f", venda.total)
            textPagamento.text = "Pagamento: ${venda.formaPagamento}"
            
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            textData.text = dateFormat.format(venda.dataVenda)
        }
    }

    class VendaDiffCallback : DiffUtil.ItemCallback<Venda>() {
        override fun areItemsTheSame(oldItem: Venda, newItem: Venda): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Venda, newItem: Venda): Boolean {
            return oldItem == newItem
        }
    }
}



