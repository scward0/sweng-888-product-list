package com.example.productlist

// DatabaseHelper.kt
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "products.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_PRODUCTS = "products"

        // Column names
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_SELLER = "seller"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_PICTURE_URI = "picture_uri"
        private const val COLUMN_CATEGORY = "category"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_SELLER TEXT NOT NULL,
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_PICTURE_URI TEXT NOT NULL,
                $COLUMN_CATEGORY TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTable)

        // Pre-populate with sample data
        insertSampleData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    private fun insertSampleData(db: SQLiteDatabase) {
        val sampleProducts = listOf(
            Product(0, "iPhone 14", "Latest Apple smartphone with advanced camera", "Apple Store", 999.99, "ic_phone", "Electronics"),
            Product(0, "Samsung Galaxy Buds", "Wireless earbuds with noise cancellation", "Samsung", 149.99, "ic_headphones", "Electronics"),
            Product(0, "MacBook Pro", "High-performance laptop for professionals", "Apple Store", 1999.99, "ic_laptop", "Computers"),
            Product(0, "Dell Monitor", "27-inch 4K monitor for gaming and work", "Dell", 299.99, "ic_monitor", "Computers"),
            Product(0, "Wireless Mouse", "Ergonomic wireless mouse with precision tracking", "Logitech", 49.99, "ic_mouse", "Accessories"),
            Product(0, "Mechanical Keyboard", "RGB backlit mechanical gaming keyboard", "Corsair", 129.99, "ic_keyboard", "Accessories"),
            Product(0, "iPad Pro", "12.9-inch tablet with Apple Pencil support", "Apple Store", 799.99, "ic_tablet", "Electronics"),
            Product(0, "Gaming Chair", "Ergonomic gaming chair with lumbar support", "Secretlab", 399.99, "ic_chair", "Furniture"),
            Product(0, "Sony Headphones", "Premium noise-canceling over-ear headphones", "Sony", 249.99, "ic_headphones", "Electronics"),
            Product(0, "Standing Desk", "Adjustable height standing desk for health", "IKEA", 299.99, "ic_monitor", "Furniture"),
            Product(0, "Webcam HD", "1080p webcam for video conferencing", "Logitech", 79.99, "ic_monitor", "Accessories"),
            Product(0, "Gaming Laptop", "High-performance gaming laptop with RTX graphics", "ASUS", 1599.99, "ic_laptop", "Computers")
        )

        sampleProducts.forEach { product ->
            insertProduct(db, product)
        }
    }

    fun insertProduct(product: Product): Long {
        val db = writableDatabase
        return insertProduct(db, product)
    }

    private fun insertProduct(db: SQLiteDatabase, product: Product): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_DESCRIPTION, product.description)
            put(COLUMN_SELLER, product.seller)
            put(COLUMN_PRICE, product.price)
            put(COLUMN_PICTURE_URI, product.pictureUri)
            put(COLUMN_CATEGORY, product.category)
        }
        return db.insert(TABLE_PRODUCTS, null, values)
    }

    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val seller = getString(getColumnIndexOrThrow(COLUMN_SELLER))
                val price = getDouble(getColumnIndexOrThrow(COLUMN_PRICE))
                val pictureUri = getString(getColumnIndexOrThrow(COLUMN_PICTURE_URI))
                val category = getString(getColumnIndexOrThrow(COLUMN_CATEGORY))

                products.add(Product(id, name, description, seller, price, pictureUri, category))
            }
        }
        cursor.close()
        return products
    }

    fun getByCategory(category: String): List<Product> {
        val products = mutableListOf<Product>()
        val db = readableDatabase
        val selection = "$COLUMN_CATEGORY = ?"
        val selectionArgs = arrayOf(category)
        val cursor = db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val seller = getString(getColumnIndexOrThrow(COLUMN_SELLER))
                val price = getDouble(getColumnIndexOrThrow(COLUMN_PRICE))
                val pictureUri = getString(getColumnIndexOrThrow(COLUMN_PICTURE_URI))
                val categoryValue = getString(getColumnIndexOrThrow(COLUMN_CATEGORY))

                products.add(Product(id, name, description, seller, price, pictureUri, categoryValue))
            }
        }
        cursor.close()
        return products
    }

    fun getAllCategories(): List<String> {
        val categories = mutableSetOf<String>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCTS, arrayOf(COLUMN_CATEGORY), null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val category = getString(getColumnIndexOrThrow(COLUMN_CATEGORY))
                categories.add(category)
            }
        }
        cursor.close()
        return categories.sorted()
    }
}