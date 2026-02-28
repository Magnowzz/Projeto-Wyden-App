package com.lanchonete.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.lanchonete.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        binding.navView.setNavigationItemSelectedListener(this)
        
        // Abrir tela de pedidos por padrão
        if (savedInstanceState == null) {
            abrirPedidos()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_estoque -> {
                abrirEstoque()
            }
            R.id.nav_pedidos -> {
                abrirPedidos()
            }
            R.id.nav_vendas -> {
                abrirVendas()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun abrirEstoque() {
        val intent = Intent(this, EstoqueActivity::class.java)
        startActivity(intent)
    }

    private fun abrirPedidos() {
        val intent = Intent(this, PedidosActivity::class.java)
        startActivity(intent)
    }

    private fun abrirVendas() {
        val intent = Intent(this, VendasActivity::class.java)
        startActivity(intent)
    }
}
