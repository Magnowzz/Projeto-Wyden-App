package com.lanchonete.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.lanchonete.app.R
import com.lanchonete.app.database.entity.Pedido
import java.text.SimpleDateFormat
import java.util.Locale

class PedidoListAdapter(
    private val onStatusClick: (Pedido, String) -> Unit,
    private val onFinalizarClick: (Pedido) -> Unit,
    private val onDetalhesClick: (Pedido) -> Unit
) : ListAdapter<Pedido, PedidoListAdapter.PedidoViewHolder>(PedidoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido_lista, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textCliente: TextView = itemView.findViewById(R.id.textCliente)
        private val textTotal: TextView = itemView.findViewById(R.id.textTotal)
        private val textStatus: TextView = itemView.findViewById(R.id.textStatus)
        private val textData: TextView = itemView.findViewById(R.id.textData)
        private val textFormaPagamento: TextView = itemView.findViewById(R.id.textFormaPagamento)
        private val btnStatus: MaterialButton = itemView.findViewById(R.id.btnStatus)
        private val btnFinalizar: MaterialButton = itemView.findViewById(R.id.btnFinalizar)
        private val btnDetalhes: MaterialButton = itemView.findViewById(R.id.btnDetalhes)

        fun bind(pedido: Pedido) {
            textCliente.text = pedido.cliente
            textTotal.text = String.format(Locale.getDefault(), "R$ %.2f", pedido.total)
            textStatus.text = pedido.status
            textFormaPagamento.text = pedido.formaPagamento
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            textData.text = dateFormat.format(pedido.dataCriacao)

            // Cor do status
            val statusColor = when (pedido.status) {
                "Pendente" -> R.color.warning
                "Preparando" -> R.color.info
                "Pronto" -> R.color.success
                "Entregue" -> R.color.gray_medium
                else -> R.color.text_primary
            }
            textStatus.setTextColor(itemView.context.getColor(statusColor))

            btnStatus.setOnClickListener {
                val statuses = arrayOf("Pendente", "Preparando", "Pronto", "Entregue")
                val currentIndex = statuses.indexOf(pedido.status)
                val nextIndex = (currentIndex + 1) % statuses.size
                onStatusClick(pedido, statuses[nextIndex])
            }

            btnFinalizar.setOnClickListener {
                if (pedido.status == "Pronto" || pedido.status == "Entregue") {
                    onFinalizarClick(pedido)
                } else {
                    Toast.makeText(
                        itemView.context,
                        "Pedido deve estar Pronto ou Entregue",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnDetalhes.setOnClickListener {
                onDetalhesClick(pedido)
            }
        }
    }

    class PedidoDiffCallback : DiffUtil.ItemCallback<Pedido>() {
        override fun areItemsTheSame(oldItem: Pedido, newItem: Pedido): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pedido, newItem: Pedido): Boolean {
            return oldItem == newItem
        }
    }
}

