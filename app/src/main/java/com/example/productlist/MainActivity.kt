package com.example.productlist
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var nextButton: Button
    private lateinit var categoryContainer: LinearLayout
    private lateinit var productAdapter: ProductAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private var selectedProducts = setOf<Product>()
    private var currentCategory = "All" // Track current filter
    private var allProducts = listOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupDatabase()
        setupCategoryFilter()
        setupRecyclerView()
        setupNextButton()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewProducts)
        nextButton = findViewById(R.id.buttonNext)
        categoryContainer = findViewById(R.id.categoryContainer)
    }

    private fun setupDatabase() {
        databaseHelper = DatabaseHelper(this)
        allProducts = databaseHelper.getAllProducts()
    }

    private fun setupCategoryFilter() {
        val categories = listOf("All") + databaseHelper.getAllCategories()

        categories.forEach { category ->
            val categoryButton = TextView(this).apply {
                text = category
                setPadding(32, 16, 32, 16)
                textSize = 14f
                setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.black))
                background = ContextCompat.getDrawable(this@MainActivity, android.R.drawable.btn_default)
                isClickable = true
                isFocusable = true

                setOnClickListener {
                    filterByCategory(category)
                    updateCategoryButtons(category)
                }
            }

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 8, 0)
            }
            categoryButton.layoutParams = layoutParams

            categoryContainer.addView(categoryButton)
        }

        // Set "All" as selected initially
        updateCategoryButtons("All")
    }

    private fun updateCategoryButtons(selectedCategory: String) {
        currentCategory = selectedCategory
        for (i in 0 until categoryContainer.childCount) {
            val button = categoryContainer.getChildAt(i) as TextView
            if (button.text == selectedCategory) {
                button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light))
                button.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            } else {
                button.background = ContextCompat.getDrawable(this, android.R.drawable.btn_default)
                button.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            }
        }
    }

    private fun filterByCategory(category: String) {
        val filteredProducts = if (category == "All") {
            allProducts
        } else {
            databaseHelper.getByCategory(category)
        }

        // Clear selections when switching categories
        selectedProducts = setOf()
        updateRecyclerView(filteredProducts)
        updateNextButton()
    }

    private fun setupRecyclerView() {
        updateRecyclerView(allProducts)
    }

    private fun updateRecyclerView(products: List<Product>) {
        productAdapter = ProductAdapter(products) { selected ->
            selectedProducts = selected
            updateNextButton()
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productAdapter
        }
    }

    private fun setupNextButton() {
        nextButton.setOnClickListener {
            if (selectedProducts.size >= 3) {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putParcelableArrayListExtra("selectedProducts", ArrayList(selectedProducts))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select at least 3 products", Toast.LENGTH_SHORT).show()
            }
        }
        updateNextButton()
    }

    private fun updateNextButton() {
        nextButton.isEnabled = selectedProducts.size >= 3
        nextButton.text = if (selectedProducts.size >= 3) {
            "Next (${selectedProducts.size} selected)"
        } else {
            "Select at least 3 products (${selectedProducts.size}/3)"
        }
    }
}