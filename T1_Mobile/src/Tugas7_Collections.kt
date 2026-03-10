data class NilaiMahasiswa(
    val nim: String,
    val nama: String,
    val mataKuliah: String,
    val nilai: Int
)

fun main() {
    val daftarNilai = listOf(
        NilaiMahasiswa("2024001", "Budi Santoso", "Pemrograman", 85),
        NilaiMahasiswa("2024002", "Ani Wijaya", "Pemrograman", 92),
        NilaiMahasiswa("2024003", "Citra Dewi", "Pemrograman", 68),
        NilaiMahasiswa("2024004", "Dani Pratama", "Pemrograman", 45),
        NilaiMahasiswa("2024005", "Eko Saputra", "Pemrograman", 73),
        NilaiMahasiswa("2024006", "Fina Lestari", "Pemrograman", 81),
        NilaiMahasiswa("2024007", "Gilang Mahesa", "Pemrograman", 59),
        NilaiMahasiswa("2024008", "Hana Putri", "Pemrograman", 77),
        NilaiMahasiswa("2024009", "Indra Gunawan", "Pemrograman", 88),
        NilaiMahasiswa("2024010", "Joko Prasetyo", "Pemrograman", 60)
    )

    println("===== DATA NILAI MAHASISWA =====")
    println(String.format("%-3s %-8s %-15s %-15s %-5s", "No", "NIM", "Nama", "MataKuliah", "Nilai"))
    daftarNilai.forEachIndexed { index, mhs ->
        println(String.format("%-3d %-8s %-15s %-15s %-5d", index + 1, mhs.nim, mhs.nama, mhs.mataKuliah, mhs.nilai))
    }

    println("\n===== STATISTIK =====")
    val total = daftarNilai.size
    val rataRata = daftarNilai.map { it.nilai }.average()
    val tertinggi = daftarNilai.maxByOrNull { it.nilai }!!
    val terendah = daftarNilai.minByOrNull { it.nilai }!!

    println("Total Mahasiswa : $total")
    println("Rata-rata Nilai : %.1f".format(rataRata))
    println("Nilai Tertinggi : ${tertinggi.nilai} (${tertinggi.nama})")
    println("Nilai Terendah  : ${terendah.nilai} (${terendah.nama})")

    // Fungsi menentukan grade
    fun getGrade(nilai: Int): String {
        return when {
            nilai >= 85 -> "A"
            nilai >= 70 -> "B"
            nilai >= 60 -> "C"
            nilai >= 50 -> "D"
            else -> "E"
        }
    }

    println("\n===== MAHASISWA LULUS =====")
    val lulus = daftarNilai.filter { it.nilai >= 70 }
    lulus.forEachIndexed { i, m ->
        println("${i + 1}. ${m.nama} - ${m.nilai} (${getGrade(m.nilai)})")
    }

    println("\n===== JUMLAH PER GRADE =====")
    val perGrade = daftarNilai.groupBy { getGrade(it.nilai) }
    listOf("A", "B", "C", "D", "E").forEach { g ->
        val jumlah = perGrade[g]?.size ?: 0
        println("Grade $g: $jumlah mahasiswa")
    }
}