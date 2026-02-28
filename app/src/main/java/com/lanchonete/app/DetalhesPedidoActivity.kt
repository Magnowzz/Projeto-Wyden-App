package com.lanchonete.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lanchonete.app.adapter.ItemPedidoListAdapter
import com.lanchonete.app.database.AppDatabase
import com.lanchonete.app.databinding.ActivityDetalhesPedidoBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DetalhesPedidoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalhesPedidoBinding
    private lateinit var database: AppDatabase
    private var pedidoId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        pedidoId = intent.getLongExtra("pedido_id", 0)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detalhes do Pedido"

        carregarDetalhes()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnMarcarPago.setOnClickListener {
            marcarComoPago()
        }
    }

    private fun marcarComoPago() {
        lifecycleScope.launch {
            try {
                val pedido = database.pedidoDao().obterPorId(pedidoId)
                pedido?.let {
                    val pedidoAtualizado = it.copy(pago = true)
                    database.pedidoDao().atualizar(pedidoAtualizado)
                    
                    // Atualizar a UI
                    atualizarStatusPagamento(true)
                    Toast.makeText(
                        this@DetalhesPedidoActivity,
                        "Pedido marcado como pago!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@DetalhesPedidoActivity,
                    "Erro ao marcar como pago: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun atualizarStatusPagamento(pago: Boolean) {
        if (pago) {
            binding.textStatusPagamento.text = "✓ Pago"
            binding.textStatusPagamento.setTextColor(resources.getColor(R.color.success, null))
            binding.btnMarcarPago.isEnabled = false
            binding.btnMarcarPago.text = "✓ Já está marcado como pago"
        } else {
            binding.textStatusPagamento.text = "✗ Não Pago"
            binding.textStatusPagamento.setTextColor(resources.getColor(R.color.error, null))
            binding.btnMarcarPago.isEnabled = true
            binding.btnMarcarPago.text = "✓ Marcar como Pago"
        }
    }

    private fun carregarDetalhes() {
        lifecycleScope.launch {
            val pedido = database.pedidoDao().obterPorId(pedidoId)
            pedido?.let {
                binding.textCliente.text = it.cliente
                binding.textTotal.text = String.format(Locale.getDefault(), "R$ %.2f", it.total)
                binding.textStatus.text = it.status
                binding.textFormaPagamento.text = it.formaPagamento
                
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                binding.textData.text = dateFormat.format(it.dataCriacao)
                
                if (it.observacoes.isNotEmpty()) {
                    binding.textObservacoes.text = "📝 Observações: ${it.observacoes}"
                    binding.textObservacoes.visibility = android.view.View.VISIBLE
                }

                // Atualizar status de pagamento
                atualizarStatusPagamento(it.pago)
            }

            val itens = database.itemPedidoDao().obterPorPedidoSync(pedidoId)
            val produtosMap = itens.mapNotNull { item ->
                val produto = database.produtoDao().obterPorId(item.produtoId)
                produto?.let { item.produtoId to it.nome }
            }.toMap()
            
            val adapter = ItemPedidoListAdapter(produtosMap)
            binding.recyclerViewItens.layoutManager = LinearLayoutManager(this@DetalhesPedidoActivity)
            binding.recyclerViewItens.adapter = adapter
            adapter.submitList(itens)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

