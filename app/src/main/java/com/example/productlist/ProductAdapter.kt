package com.example.productlist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val products: List<Product>,
    private val onSelectionChanged: (Set<Product>) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val selectedProducts = mutableSetOf<Product>()

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productDescription: TextView = itemView.findViewById(R.id.productDescription)
        val productSeller: TextView = itemView.findViewById(R.id.productSeller)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productCategory: TextView = itemView.findViewById(R.id.productCategory)
        val productCheckbox: CheckBox = itemView.findViewById(R.id.productCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.productName.text = product.name
        holder.productDescription.text = product.description
        holder.productSeller.text = "Sold by: ${product.seller}"
        holder.productPrice.text = "${String.format("%.2f", product.price)}"
        holder.productCategory.text = product.category

        // Set product image (you'll need to add these drawable resources)
        val imageResId = holder.itemView.context.resources.getIdentifier(
            product.pictureUri, "drawable", holder.itemView.context.packageName
        )
        if (imageResId != 0) {
            holder.productImage.setImageResource(imageResId)
        } else {
            holder.productImage.setImageResource(R.drawable.ic_launcher_foreground) // fallback
        }

        // Handle checkbox state
        holder.productCheckbox.isChecked = selectedProducts.contains(product)

        holder.productCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedProducts.add(product)
            } else {
                selectedProducts.remove(product)
            }
            onSelectionChanged(selectedProducts.toSet())
        }

        // Allow clicking the whole item to toggle checkbox
        holder.itemView.setOnClickListener {
            holder.productCheckbox.isChecked = !holder.productCheckbox.isChecked
        }
    }

    override fun getItemCount() = products.size

    fun getSelectedProducts(): Set<Product> = selectedProducts.toSet()

    fun clearSelections() {
        selectedProducts.clear()
        notifyDataSetChanged()
    }
}