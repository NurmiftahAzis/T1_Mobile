open class Produk(
    val id: Int,
    val nama: String,
    val harga: Double,
    val kategori: String,
    var stok: Int
)

data class CartItem(
    val produk: Produk,
    var jumlah: Int
)

data class Customer(
    val id: Int,
    val nama: String,
    val email: String,
    val alamat: String? = null // Null untuk alamat
)

data class Order(
    val id: Int,
    val customer: Customer,
    val items: List<CartItem>,
    var status: OrderStatus,
    var paymentMethod: PaymentMethod,
    val totalHarga: Double
)

// Sealed Classes
sealed class OrderStatus {
    object Pending : OrderStatus()
    object Processing : OrderStatus()
    object Shipped : OrderStatus()
    object Delivered : OrderStatus()
    object Cancelled : OrderStatus()
}

sealed class PaymentMethod {
    object Cash : PaymentMethod()
    object Transfer : PaymentMethod()
    object EWallet : PaymentMethod()
}

// Interface Diskon
interface Discountable {
    fun hitungDiskon(): Double
}

// Implementasi Produk Diskon
class ProdukDiskon(
    id: Int,
    nama: String,
    harga: Double,
    kategori: String,
    stok: Int,
    val persenDiskon: Double
) : Produk(id, nama, harga, kategori, stok), Discountable {
    override fun hitungDiskon(): Double {
        return harga * (persenDiskon / 100)
    }
}

// Kelas ShoppingCart
class ShoppingCart {
    private val cartItems = mutableListOf<CartItem>()

    fun tambahProduk(produk: Produk, jumlah: Int = 1) {
        if (produk.stok >= jumlah) {
            val existingItem = cartItems.find { it.produk.id == produk.id }
            if (existingItem != null) {
                existingItem.jumlah += jumlah
            } else {
                cartItems.add(CartItem(produk, jumlah))
            }
            produk.stok -= jumlah
            println("${produk.nama} x$jumlah ditambahkan ke keranjang.")
        } else {
            println("Stok ${produk.nama} tidak mencukupi!")
        }
    }

    fun hapusProduk(produkId: Int) {
        val item = cartItems.find { it.produk.id == produkId }
        if (item != null) {
            cartItems.remove(item)
            item.produk.stok += item.jumlah
            println("${item.produk.nama} dihapus dari keranjang.")
        } else {
            println("Produk tidak ditemukan di keranjang.")
        }
    }

    fun tampilkanKeranjang() {
        println("\n===== Isi Keranjang =====")
        if (cartItems.isEmpty()) {
            println("Keranjang kosong.")
        } else {
            cartItems.forEachIndexed { index, item ->
                println("${index + 1}. ${item.produk.nama} - ${item.jumlah} x Rp${item.produk.harga}")
            }
        }
    }

    fun hitungTotal(customDiscount: (Double) -> Double = { it }): Double {
        val total = cartItems.sumOf { it.produk.harga * it.jumlah }
        return customDiscount(total)
    }

    fun getItems(): List<CartItem> = cartItems.toList()

    fun kosongkanKeranjang() {
        cartItems.clear()
    }
}

// Kelas TokoOnline
class TokoOnline {
    private val daftarProduk = mutableListOf<Produk>()
    private val daftarOrder = mutableListOf<Order>()
    private var orderIdCounter = 1

    fun tambahProduk(produk: Produk) = daftarProduk.add(produk)

    fun tampilkanProduk() {
        println("\n===== DAFTAR PRODUK =====")
        daftarProduk.forEach {
            println("${it.id}. ${it.nama} - Rp${it.harga} [${it.kategori}] (Stok: ${it.stok})")
        }
    }

    fun filterProdukByKategori(kategori: String) =
        daftarProduk.filter { it.kategori.equals(kategori, ignoreCase = true) }

    fun sortProdukByHarga(ascending: Boolean = true) =
        if (ascending) daftarProduk.sortedBy { it.harga } else daftarProduk.sortedByDescending { it.harga }

    fun groupProdukByKategori() = daftarProduk.groupBy { it.kategori }

    fun checkout(customer: Customer, cart: ShoppingCart, payment: PaymentMethod, diskon: Double = 0.0) {
        val total = cart.hitungTotal { total -> total - diskon }
        val order = Order(
            id = orderIdCounter++,
            customer = customer,
            items = cart.getItems(),
            status = OrderStatus.Pending,
            paymentMethod = payment,
            totalHarga = total
        )
        daftarOrder.add(order)
        cart.kosongkanKeranjang()
        println("\nPesanan #${order.id} berhasil dibuat untuk ${customer.nama}. Total: Rp${total}")
    }

    fun ubahStatusOrder(orderId: Int, statusBaru: OrderStatus) {
        val order = daftarOrder.find { it.id == orderId }
        if (order != null) {
            order.status = statusBaru
            println("Status pesanan #${order.id} diubah menjadi ${statusBaru::class.simpleName}.")
        } else {
            println("Pesanan tidak ditemukan.")
        }
    }

    fun tampilkanRiwayatOrder() {
        println("\n===== RIWAYAT PESANAN =====")
        if (daftarOrder.isEmpty()) {
            println("Belum ada pesanan.")
        } else {
            daftarOrder.forEach {
                println("Order #${it.id} - ${it.customer.nama} - Total: Rp${it.totalHarga} - Status: ${it.status::class.simpleName}")
            }
        }
    }
}

// Fungsi Main (Demo Sistem)
fun main() {
    val toko = TokoOnline()
    val cart = ShoppingCart()

    val p1 = Produk(1, "Kopi Arabica", 35000.0, "Minuman", 10)
    val p2 = ProdukDiskon(2, "Teh Hijau", 25000.0, "Minuman", 8, 10.0)
    val p3 = Produk(3, "Roti Coklat", 15000.0, "Makanan", 15)
    val p4 = ProdukDiskon(4, "Susu Kedelai", 20000.0, "Minuman", 12, 5.0)

    toko.tambahProduk(p1)
    toko.tambahProduk(p2)
    toko.tambahProduk(p3)
    toko.tambahProduk(p4)

    toko.tampilkanProduk()

    val customer = Customer(1, "Miftah", "miftah@example.com", "Mataram")

    // Tambah produk ke keranjang
    cart.tambahProduk(p1, 2)
    cart.tambahProduk(p2, 1)
    cart.tambahProduk(p3, 3)
    cart.tampilkanKeranjang()

    // Hapus salah satu produk
    cart.hapusProduk(3)
    cart.tampilkanKeranjang()

    // Hitung total dengan diskon 10.000 menggunakan Higher-Order Function
    val total = cart.hitungTotal { it - 10000 }
    println("\nTotal setelah diskon Rp10.000: Rp$total")

    // Checkout
    toko.checkout(customer, cart, PaymentMethod.EWallet, 10000.0)

    // Ubah status pesanan
    toko.ubahStatusOrder(1, OrderStatus.Shipped)
    toko.ubahStatusOrder(1, OrderStatus.Delivered)

    // Tampilkan riwayat order
    toko.tampilkanRiwayatOrder()

    // Contoh Collection Operations
    println("\n===== PRODUK DIKELOMPOKKAN BERDASARKAN KATEGORI =====")
    toko.groupProdukByKategori().forEach { (kategori, list) ->
        println("$kategori: ${list.joinToString { it.nama }}")
    }
}