package com.lanchonete.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lanchonete.app.adapter.PedidoListAdapter
import com.lanchonete.app.database.AppDatabase
import com.lanchonete.app.database.entity.Pedido
import com.lanchonete.app.databinding.ActivityPedidosBinding
import kotlinx.coroutines.launch

class PedidosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPedidosBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: PedidoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pedidos"

        setupRecyclerView()
        setupListeners()
        carregarPedidos()
    }

    private fun setupRecyclerView() {
        adapter = PedidoListAdapter(
            onStatusClick = { pedido, novoStatus ->
                atualizarStatus(pedido, novoStatus)
            },
            onFinalizarClick = { pedido ->
                finalizarPedido(pedido)
            },
            onDetalhesClick = { pedido ->
                verDetalhes(pedido)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        binding.fabNovoPedido.setOnClickListener {
            val intent = Intent(this, CriarPedidoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun carregarPedidos() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                database.pedidoDao().obterTodos().collect { pedidos ->
                    adapter.submitList(pedidos)
                    atualizarEstatisticas(pedidos)
                    binding.progressBar.visibility = View.GONE
                    binding.textViewEmpty.visibility = if (pedidos.isEmpty()) View.VISIBLE else View.GONE
                }
            } catch (e: Exception) {
                Toast.makeText(this@PedidosActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun atualizarEstatisticas(pedidos: List<Pedido>) {
        val total = pedidos.size
        val pendentes = pedidos.count { it.status == "Pendente" }
        val emPreparo = pedidos.count { it.status == "Preparando" }
        val prontos = pedidos.count { it.status == "Pronto" }
        
        binding.textTotalPedidos.text = "Total: $total"
        binding.textPendentes.text = "Pendentes: $pendentes"
        binding.textEmPreparo.text = "Em Preparo: $emPreparo"
        binding.textProntos.text = "Prontos: $prontos"
    }

    private fun atualizarStatus(pedido: Pedido, novoStatus: String) {
        lifecycleScope.launch {
            try {
                database.pedidoDao().atualizarStatus(pedido.id, novoStatus)
                Toast.makeText(this@PedidosActivity, "Status atualizado", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@PedidosActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun finalizarPedido(pedido: Pedido) {
        val intent = Intent(this, FinalizarVendaActivity::class.java)
        intent.putExtra("pedido_id", pedido.id)
        startActivity(intent)
    }

    private fun verDetalhes(pedido: Pedido) {
        val intent = Intent(this, DetalhesPedidoActivity::class.java)
        intent.putExtra("pedido_id", pedido.id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        carregarPedidos()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}



