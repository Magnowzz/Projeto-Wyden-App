package com.lanchonete.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lanchonete.app.R
import com.lanchonete.app.database.entity.Produto
import java.util.Locale

class ProdutoAdapter(
    private val onEditClick: (Produto) -> Unit,
    private val onDeleteClick: (Produto) -> Unit
) : ListAdapter<Produto, ProdutoAdapter.ProdutoViewHolder>(ProdutoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textNome: TextView = itemView.findViewById(R.id.textNome)
        private val textPreco: TextView = itemView.findViewById(R.id.textPreco)
        private val textEstoque: TextView = itemView.findViewById(R.id.textEstoque)
        private val textCategoria: TextView = itemView.findViewById(R.id.textCategoria)
        private val btnEditar: TextView = itemView.findViewById(R.id.btnEditar)
        private val btnDeletar: TextView = itemView.findViewById(R.id.btnDeletar)

        fun bind(produto: Produto) {
            textNome.text = produto.nome
            textPreco.text = String.format(Locale.getDefault(), "R$ %.2f", produto.preco)
            textEstoque.text = "Estoque: ${produto.quantidadeEstoque}"
            textCategoria.text = produto.categoria.ifEmpty { "Sem categoria" }
            
            // Mudar cor do estoque se estiver baixo
            if (produto.quantidadeEstoque < 10) {
                textEstoque.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
            } else {
                textEstoque.setTextColor(itemView.context.getColor(android.R.color.black))
            }

            btnEditar.setOnClickListener { onEditClick(produto) }
            btnDeletar.setOnClickListener { onDeleteClick(produto) }
        }
    }

    class ProdutoDiffCallback : DiffUtil.ItemCallback<Produto>() {
        override fun areItemsTheSame(oldItem: Produto, newItem: Produto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Produto, newItem: Produto): Boolean {
            return oldItem == newItem
        }
    }
}



