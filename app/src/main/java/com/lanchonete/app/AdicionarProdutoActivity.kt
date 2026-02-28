package com.lanchonete.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lanchonete.app.database.AppDatabase
import com.lanchonete.app.database.entity.Produto
import com.lanchonete.app.databinding.ActivityAdicionarProdutoBinding
import kotlinx.coroutines.launch

class AdicionarProdutoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdicionarProdutoBinding
    private lateinit var database: AppDatabase
    private var produtoId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdicionarProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        produtoId = intent.getLongExtra("produto_id", 0)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (produtoId > 0) "Editar Produto" else "Adicionar Produto"

        if (produtoId > 0) {
            carregarProduto()
        }

        binding.btnSalvar.setOnClickListener {
            salvarProduto()
        }
    }

    private fun carregarProduto() {
        lifecycleScope.launch {
            val produto = database.produtoDao().obterPorId(produtoId)
            produto?.let {
                binding.editNome.setText(it.nome)
                binding.editDescricao.setText(it.descricao)
                binding.editPreco.setText(it.preco.toString())
                binding.editEstoque.setText(it.quantidadeEstoque.toString())
                binding.editCategoria.setText(it.categoria)
            }
        }
    }

    private fun salvarProduto() {
        val nome = binding.editNome.text?.toString()?.trim() ?: ""
        val descricao = binding.editDescricao.text?.toString()?.trim() ?: ""
        val precoStr = binding.editPreco.text?.toString()?.trim() ?: ""
        val estoqueStr = binding.editEstoque.text?.toString()?.trim() ?: ""
        val categoria = binding.editCategoria.text?.toString()?.trim() ?: ""

        if (nome.isEmpty() || precoStr.isEmpty() || estoqueStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val preco = precoStr.replace(",", ".").toDouble()
            val estoque = estoqueStr.toInt()

            val produto = if (produtoId > 0) {
                Produto(produtoId, nome, descricao, preco, estoque, categoria)
            } else {
                Produto(0, nome, descricao, preco, estoque, categoria)
            }

            lifecycleScope.launch {
                if (produtoId > 0) {
                    database.produtoDao().atualizar(produto)
                } else {
                    database.produtoDao().inserir(produto)
                }
                Toast.makeText(this@AdicionarProdutoActivity, "Produto salvo!", Toast.LENGTH_SHORT).show()
                finish()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}



