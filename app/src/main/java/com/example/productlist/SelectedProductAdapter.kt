package com.example.productlist

// SelectedProductAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SelectedProductAdapter(
    private val products: MutableList<Product>
) : RecyclerView.Adapter<SelectedProductAdapter.SelectedProductViewHolder>() {

    class SelectedProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.selectedProductImage)
        val productName: TextView = itemView.findViewById(R.id.selectedProductName)
        val productDescription: TextView = itemView.findViewById(R.id.selectedProductDescription)
        val productSeller: TextView = itemView.findViewById(R.id.selectedProductSeller)
        val productPrice: TextView = itemView.findViewById(R.id.selectedProductPrice)
        val productCategory: TextView = itemView.findViewById(R.id.selectedProductCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selected_product, parent, false)
        return SelectedProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedProductViewHolder, position: Int) {
        val product = products[position]

        holder.productName.text = product.name
        holder.productDescription.text = product.description
        holder.productSeller.text = "Sold by: ${product.seller}"
        holder.productPrice.text = "$${String.format("%.2f", product.price)}"
        holder.productCategory.text = product.category

        // Set product image
        val imageResId = holder.itemView.context.resources.getIdentifier(
            product.pictureUri, "drawable", holder.itemView.context.packageName
        )
        if (imageResId != 0) {
            holder.productImage.setImageResource(imageResId)
        } else {
            holder.productImage.setImageResource(R.drawable.ic_launcher_foreground) // fallback
        }
    }

    override fun getItemCount() = products.size
}