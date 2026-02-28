package com.lanchonete.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lanchonete.app.database.AppDatabase
import com.lanchonete.app.database.entity.Venda
import com.lanchonete.app.databinding.ActivityFinalizarVendaBinding
import kotlinx.coroutines.launch

class FinalizarVendaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinalizarVendaBinding
    private lateinit var database: AppDatabase
    private var pedidoId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalizarVendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        pedidoId = intent.getLongExtra("pedido_id", 0)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Finalizar Venda"

        carregarDadosPedido()

        binding.btnFinalizar.setOnClickListener {
            finalizarVenda()
        }
    }

    private fun carregarDadosPedido() {
        lifecycleScope.launch {
            val pedido = database.pedidoDao().obterPorId(pedidoId)
            pedido?.let {
                binding.textTotal.text = String.format("Total: R$ %.2f", it.total)
            }
        }
    }

    private fun finalizarVenda() {
        val formaPagamento = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radioDinheiro -> "Dinheiro"
            R.id.radioCartao -> "Cartão"
            R.id.radioPix -> "PIX"
            else -> "Dinheiro"
        }

        lifecycleScope.launch {
            try {
                val pedido = database.pedidoDao().obterPorId(pedidoId)
                pedido?.let {
                    val venda = Venda(0, pedidoId, it.total, formaPagamento)
                    database.vendaDao().inserir(venda)
                    database.pedidoDao().atualizarStatus(pedidoId, "Entregue")
                    Toast.makeText(this@FinalizarVendaActivity, "Venda finalizada!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@FinalizarVendaActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}



