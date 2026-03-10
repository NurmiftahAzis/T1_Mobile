fun main() {
    println("===== SISTEM PENILAIAN =====")

    println("Masukkan Nama Mahasiswa: ")
    val nama = readLine()

    println("Masukkan Nilai UTS (0-100): ")
    val uts = readLine()!!.toDouble()

    println("Masukkan Nilai UAS (0-100): ")
    val uas = readLine()!!.toDouble()

    println("Masukkan Nilai Tugas (0-100): ")
    val tugas = readLine()!!.toDouble()

    val nilaiAkhir = (uts * 0.3) + (uas * 0.4) + (tugas * 0.3)

    // Konversi nilai ke huruf
    val nilaiHuruf = when (nilaiAkhir) {
        in 85.0..100.0 -> "A"
        in 70.0..<84.0 -> "B"
        in 60.0..<69.0 -> "C"
        in 50.0..<59.0 -> "D"
        else -> "E"
    }

    // Tentukan status kelulusan
    val status = if (nilaiAkhir >= 60) "Lulus" else "Tidak Lulus"

    println("===== HASIL PENILAIAN =====")

    println("Nama           : $nama")
    println("Nilai UTS      : $uts (Bobot 30%)")
    println("Nilai UAS      : $uas (Bobot 40%)")
    println("Nilai Tugas    : $tugas (Bobot 30%)")
    println("-----------------------------------")
    println("Nilai Akhir    : %.2f".format(nilaiAkhir))
    println("Nilai Huruf    : $nilaiHuruf")
    println("Status         : $status")
}