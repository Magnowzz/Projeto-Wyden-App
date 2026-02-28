package com.lanchonete.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lanchonete.app.adapter.ProdutoAdapter
import com.lanchonete.app.database.AppDatabase
import com.lanchonete.app.database.entity.Produto
import com.lanchonete.app.databinding.ActivityEstoqueBinding
import kotlinx.coroutines.launch

class EstoqueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEstoqueBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: ProdutoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstoqueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Controle de Estoque"

        setupRecyclerView()
        setupListeners()
        carregarProdutos()
    }

    private fun setupRecyclerView() {
        adapter = ProdutoAdapter(
            onEditClick = { produto ->
                abrirEditarProduto(produto)
            },
            onDeleteClick = { produto ->
                deletarProduto(produto)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        binding.fabAdicionar.setOnClickListener {
            val intent = Intent(this, AdicionarProdutoActivity::class.java)
            startActivity(intent)
        }
        
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                buscarProdutos(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    carregarProdutos()
                } else {
                    buscarProdutos(newText)
                }
                return true
            }
        })
    }

    private fun carregarProdutos() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                database.produtoDao().obterTodos().collect { produtos ->
                    adapter.submitList(produtos)
                    atualizarEstatisticas(produtos)
                    binding.progressBar.visibility = View.GONE
                    binding.textViewEmpty.visibility = if (produtos.isEmpty()) View.VISIBLE else View.GONE
                }
            } catch (e: Exception) {
                Toast.makeText(this@EstoqueActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun buscarProdutos(busca: String) {
        lifecycleScope.launch {
            try {
                database.produtoDao().buscar(busca).collect { produtos ->
                    adapter.submitList(produtos)
                    atualizarEstatisticas(produtos)
                }
            } catch (e: Exception) {
                Toast.makeText(this@EstoqueActivity, "Erro na busca: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun atualizarEstatisticas(produtos: List<Produto>) {
        val totalProdutos = produtos.size
        val estoqueBaixo = produtos.count { it.quantidadeEstoque < 10 }
        val semEstoque = produtos.count { it.quantidadeEstoque == 0 }
        
        binding.textTotalProdutos.text = "Total: $totalProdutos"
        binding.textEstoqueBaixo.text = "Estoque Baixo: $estoqueBaixo"
        binding.textSemEstoque.text = "Sem Estoque: $semEstoque"
    }

    private fun abrirEditarProduto(produto: Produto) {
        val intent = Intent(this, AdicionarProdutoActivity::class.java)
        intent.putExtra("produto_id", produto.id)
        startActivity(intent)
    }

    private fun deletarProduto(produto: Produto) {
        lifecycleScope.launch {
            try {
                database.produtoDao().deletar(produto)
                Toast.makeText(this@EstoqueActivity, "Produto deletado", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@EstoqueActivity, "Erro ao deletar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        carregarProdutos()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}



