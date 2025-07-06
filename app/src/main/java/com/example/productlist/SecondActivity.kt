package com.example.productlist
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SecondActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sendEmailButton: Button
    private lateinit var selectedProductAdapter: SelectedProductAdapter
    private var selectedProducts = mutableListOf<Product>()

    companion object {
        private const val EMAIL_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // call helper methods
        initializeViews()
        getSelectedProducts()
        setupRecyclerView()
        setupEmailButton()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewSelectedProducts)
        sendEmailButton = findViewById(R.id.buttonSendEmail)
    }

    // get products selected in main actiivty
    private fun getSelectedProducts() {
        selectedProducts = intent.getParcelableArrayListExtra<Product>("selectedProducts")?.toMutableList() ?: mutableListOf()
    }

    // set up recycler view to hold selected products
    private fun setupRecyclerView() {
        selectedProductAdapter = SelectedProductAdapter(selectedProducts)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SecondActivity)
            adapter = selectedProductAdapter
        }
    }

    // set up button to email
    private fun setupEmailButton() {
        sendEmailButton.setOnClickListener {
            sendEmailWithProducts()
        }
    }

    // method to send email of selected products
    private fun sendEmailWithProducts() {

        // validate
        if (selectedProducts.isEmpty()) {
            Toast.makeText(this, "No products to send", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if email app is available
        if (!isEmailClientAvailable()) {
            Toast.makeText(this, "No email client found. Please install an email app.", Toast.LENGTH_LONG).show()
            return
        }

        // init email body
        val emailContent = buildEmailContent()

        // create email intent
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("sweng888mobileapps@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Selected Products Information | Sean Ward | svw5927@psu.edu | Practice III")
            putExtra(Intent.EXTRA_TEXT, emailContent)
        }

        try {
            // launch email with chooser
            startActivityForResult(Intent.createChooser(emailIntent, "Send email via:"), EMAIL_REQUEST_CODE)

        } catch (e: Exception) {
            Toast.makeText(this, "Failed to open email client", Toast.LENGTH_SHORT).show()
        }
    }

    // find and use an email app
    private fun isEmailClientAvailable(): Boolean {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
        }
        return emailIntent.resolveActivity(packageManager) != null
    }

    // handle result of email app
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EMAIL_REQUEST_CODE) {
            // Email intent was launched successfully
            onEmailSentSuccessfully()
        }
    }

    // builds the email
    private fun buildEmailContent(): String {
        // start string builder
        val stringBuilder = StringBuilder()

        // make some email headers
        stringBuilder.append("Sean Ward | svw5927@psu.edu | Practice III\n")
        stringBuilder.append("Selected Products Information\n")
        stringBuilder.append("==============================\n\n")


        // add selected products to email body
        selectedProducts.forEachIndexed { index, product ->
            stringBuilder.append("${index + 1}. ${product.name}\n")
            stringBuilder.append("   Category: ${product.category}\n")
            stringBuilder.append("   Description: ${product.description}\n")
            stringBuilder.append("   Seller: ${product.seller}\n")
            stringBuilder.append("   Price: ${String.format("%.2f", product.price)}\n\n")
        }


        // make an email footer and calculate total price
        stringBuilder.append("==============================\n")
        stringBuilder.append("ORDER SUMMARY\n")
        stringBuilder.append("==============================\n")
        stringBuilder.append("Total Products: ${selectedProducts.size}\n")
        val totalPrice = selectedProducts.sumOf { it.price }
        stringBuilder.append("Total Value: ${String.format("%.2f", totalPrice)}\n\n")
        stringBuilder.append("Generated from ProductList App")

        return stringBuilder.toString()
    }

    // once email has been sent
    private fun onEmailSentSuccessfully() {
        // Show toast indicating completion
        Toast.makeText(this, "Email sent successfully! Products list cleared.", Toast.LENGTH_LONG).show()

        // Clear the products list
        selectedProducts.clear()
        selectedProductAdapter.notifyDataSetChanged()

        // Update button state
        sendEmailButton.isEnabled = false
        sendEmailButton.text = "No Products to Send"
    }
}