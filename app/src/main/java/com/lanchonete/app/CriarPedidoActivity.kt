package com.lanchonete.app

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lanchonete.app.adapter.ItemPedidoCriarAdapter
import com.lanchonete.app.database.AppDatabase
import com.lanchonete.app.database.entity.ItemPedido
import com.lanchonete.app.database.entity.Pedido
import com.lanchonete.app.database.entity.Produto
import com.lanchonete.app.databinding.ActivityCriarPedidoBinding
import kotlinx.coroutines.launch
import java.util.Locale

class CriarPedidoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCriarPedidoBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: ItemPedidoCriarAdapter
    private val itens = mutableListOf<ItemPedidoCriarAdapter.ItemPedidoCriar>()
    private var produtos: List<Produto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCriarPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Novo Pedido"

        setupRecyclerView()
        setupListeners()
        carregarProdutos()
    }

    private fun setupRecyclerView() {
        adapter = ItemPedidoCriarAdapter(
            onRemoverClick = { item ->
                itens.remove(item)
                adapter.submitList(itens.toList())
                atualizarTotal()
            }
        )
        binding.recyclerViewItens.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewItens.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnAdicionarItem.setOnClickListener {
            adicionarItem()
        }

        binding.btnSalvarPedido.setOnClickListener {
            salvarPedido()
        }
    }

    private fun carregarProdutos() {
        lifecycleScope.launch {
            database.produtoDao().obterComEstoque().collect { produtosList ->
                produtos = produtosList
                val nomes = produtosList.map { it.nome }
                val adapter = ArrayAdapter(this@CriarPedidoActivity, android.R.layout.simple_dropdown_item_1line, nomes)
                (binding.autoCompleteProduto as? AutoCompleteTextView)?.setAdapter(adapter)
            }
        }
    }

    private fun adicionarItem() {
        val nomeProduto = (binding.autoCompleteProduto as? AutoCompleteTextView)?.text?.toString()?.trim() ?: ""
        val quantidadeStr = binding.editTextQuantidade.text?.toString()?.trim() ?: ""

        if (nomeProduto.isEmpty() || quantidadeStr.isEmpty()) {
            Toast.makeText(this, "Selecione um produto e informe a quantidade", Toast.LENGTH_SHORT).show()
            return
        }

        val produto = produtos.find { it.nome == nomeProduto }
        if (produto == null) {
            Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val quantidade = quantidadeStr.toInt()
            if (quantidade <= 0) {
                Toast.makeText(this, "Quantidade deve ser maior que zero", Toast.LENGTH_SHORT).show()
                return
            }

            if (quantidade > produto.quantidadeEstoque) {
                Toast.makeText(this, "Estoque insuficiente. Disponível: ${produto.quantidadeEstoque}", Toast.LENGTH_SHORT).show()
                return
            }

            val subtotal = produto.preco * quantidade
            val item = ItemPedidoCriarAdapter.ItemPedidoCriar(
                produtoId = produto.id,
                nome = produto.nome,
                quantidade = quantidade,
                precoUnitario = produto.preco,
                subtotal = subtotal
            )

            itens.add(item)
            adapter.submitList(itens.toList())
            atualizarTotal()

            (binding.autoCompleteProduto as? AutoCompleteTextView)?.text?.clear()
            binding.editTextQuantidade.text?.clear()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show()
        }
    }

    private fun atualizarTotal() {
        val total = itens.sumOf { it.subtotal }
        binding.textViewTotal.text = String.format(Locale.getDefault(), "Total: R$ %.2f", total)
    }

    private fun salvarPedido() {
        val cliente = binding.editTextCliente.text?.toString()?.trim() ?: ""
        val observacoes = binding.editTextObservacoes.text?.toString()?.trim() ?: ""

        if (cliente.isEmpty()) {
            Toast.makeText(this, "Informe o nome do cliente", Toast.LENGTH_SHORT).show()
            return
        }

        if (itens.isEmpty()) {
            Toast.makeText(this, "Adicione pelo menos um item ao pedido", Toast.LENGTH_SHORT).show()
            return
        }

        val total = itens.sumOf { it.subtotal }
        // Obter forma de pagamento
        val formaPagamento = when (binding.radioGroupPagamento.checkedRadioButtonId) {
            R.id.radioDinheiro -> "Dinheiro"
            R.id.radioCartao -> "Cartão"
            R.id.radioPix -> "PIX"
            else -> "Dinheiro"
        }
        val pedido = Pedido(0, cliente, total, "Pendente", System.currentTimeMillis(), System.currentTimeMillis(), observacoes, formaPagamento)

        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val pedidoId = database.pedidoDao().inserir(pedido)
                
                val itensPedido = itens.map { item ->
                    ItemPedido(
                        pedidoId = pedidoId,
                        produtoId = item.produtoId,
                        quantidade = item.quantidade,
                        precoUnitario = item.precoUnitario,
                        subtotal = item.subtotal
                    )
                }
                
                database.itemPedidoDao().inserirTodos(itensPedido)
                
                // Reduzir estoque
                itens.forEach { item ->
                    database.produtoDao().reduzirEstoque(item.produtoId, item.quantidade)
                }
                
                Toast.makeText(this@CriarPedidoActivity, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@CriarPedidoActivity, "Erro ao salvar pedido: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
