package com.lanchonete.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lanchonete.app.adapter.VendaAdapter
import com.lanchonete.app.database.AppDatabase
import com.lanchonete.app.databinding.ActivityVendasBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class VendasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVendasBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: VendaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Vendas"

        setupRecyclerView()
        carregarVendas()
        calcularEstatisticas()
    }

    private fun setupRecyclerView() {
        adapter = VendaAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun carregarVendas() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                database.vendaDao().obterTodas().collect { vendas ->
                    adapter.submitList(vendas)
                    binding.progressBar.visibility = View.GONE
                    binding.textViewEmpty.visibility = if (vendas.isEmpty()) View.VISIBLE else View.GONE
                }
            } catch (e: Exception) {
                Toast.makeText(this@VendasActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun calcularEstatisticas() {
        lifecycleScope.launch {
            try {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val inicioHoje = calendar.timeInMillis
                val fimHoje = System.currentTimeMillis()
                
                val totalHoje = database.vendaDao().obterTotalVendas(inicioHoje, fimHoje) ?: 0.0
                
                calendar.add(Calendar.DAY_OF_MONTH, -30)
                val inicioMes = calendar.timeInMillis
                val totalMes = database.vendaDao().obterTotalVendas(inicioMes, fimHoje) ?: 0.0
                
                binding.textVendasHoje.text = String.format("Hoje: R$ %.2f", totalHoje)
                binding.textVendasMes.text = String.format("Mês: R$ %.2f", totalMes)
            } catch (e: Exception) {
                Toast.makeText(this@VendasActivity, "Erro ao calcular: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        carregarVendas()
        calcularEstatisticas()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}



