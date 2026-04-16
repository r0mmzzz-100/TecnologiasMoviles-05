package com.ejercicio.ejercicio

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvProducts = findViewById(R.id.rvProducts)
        rvProducts.layoutManager = LinearLayoutManager(this)

        // Cargar datos de SharedPreferences
        loadProducts()

        // Si está vacío, agregar algunos productos de ejemplo para mostrar
        if (productList.isEmpty()) {
            productList.add(Product("Laptop", 5, 1200.0))
            productList.add(Product("Mouse", 20, 25.50))
            productList.add(Product("Teclado", 10, 45.0))
            saveProducts()
        }

        adapter = ProductAdapter(productList)
        rvProducts.adapter = adapter
    }

    private fun saveProducts() {
        val sharedPreferences = getSharedPreferences("ProductCatalog", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        
        val jsonArray = JSONArray()
        for (product in productList) {
            val jsonObject = JSONObject()
            jsonObject.put("name", product.name)
            jsonObject.put("quantity", product.quantity)
            jsonObject.put("price", product.price)
            jsonArray.put(jsonObject)
        }
        
        editor.putString("products_json", jsonArray.toString())
        editor.apply()
    }

    private fun loadProducts() {
        val sharedPreferences = getSharedPreferences("ProductCatalog", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("products_json", null)
        
        if (jsonString != null) {
            val jsonArray = JSONArray(jsonString)
            productList.clear()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val product = Product(
                    jsonObject.getString("name"),
                    jsonObject.getInt("quantity"),
                    jsonObject.getDouble("price")
                )
                productList.add(product)
            }
        }
    }
}