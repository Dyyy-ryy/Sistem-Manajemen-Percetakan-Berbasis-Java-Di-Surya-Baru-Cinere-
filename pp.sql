-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 11 Jul 2026 pada 11.04
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pp`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `databarang`
--

CREATE TABLE `databarang` (
  `KodeB` varchar(50) NOT NULL,
  `NamaB` varchar(50) NOT NULL,
  `JenisB` varchar(500) NOT NULL,
  `Harga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `databarang`
--

INSERT INTO `databarang` (`KodeB`, `NamaB`, `JenisB`, `Harga`) VALUES
('KB01', 'Buku Tulis Custom', 'Buku', 15000),
('KB02', 'Buku Nota NCR', 'Buku', 25000),
('KB03', 'Brosur Promosi A4', 'Brosur', 500),
('KB04', 'Flyer promosi A5', 'Brosur', 300),
('KB05', 'Kartu nama', 'Kartu', 350),
('KB06', 'Undangan', 'Kartu', 3000),
('KB07', 'Kalender meja', 'LainLain', 25000),
('KB08', 'Poster', 'Poster', 10000),
('KB09', 'Spanduk', 'Spanduk', 35000),
('KB10', 'X-Banner (permeter)', 'Pilih Barang', 40000),
('KB11', 'Kaos sablon', 'Merchandise', 80000),
('KB12', 'Mug custom', 'Merchandise', 45000),
('KB13', 'Kop surat', 'LainLain', 500),
('KB14', 'Amplop custom', 'LainLain', 1000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `detailpembayaran`
--

CREATE TABLE `detailpembayaran` (
  `id` int(100) NOT NULL,
  `KPembayaran` varchar(100) NOT NULL,
  `noTransaksi` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `detailpembayaran`
--

INSERT INTO `detailpembayaran` (`id`, `KPembayaran`, `noTransaksi`) VALUES
(31, 'B01', 'T01'),
(32, 'B02', 'T02'),
(33, 'B03', 'T03'),
(34, 'B04', 'T09'),
(35, 'B04', 'T10'),
(36, 'B05', 'T11'),
(37, 'B05', 'T12'),
(47, 'B07', 'T17');

-- --------------------------------------------------------

--
-- Struktur dari tabel `karyawan`
--

CREATE TABLE `karyawan` (
  `Kode` varchar(20) NOT NULL,
  `Nama` varchar(100) NOT NULL,
  `JenisKelamin` enum('L','P') NOT NULL,
  `Tanggal_Lahir` date NOT NULL,
  `Telepon` varchar(50) NOT NULL,
  `Alamat` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `karyawan`
--

INSERT INTO `karyawan` (`Kode`, `Nama`, `JenisKelamin`, `Tanggal_Lahir`, `Telepon`, `Alamat`) VALUES
('KK01', 'Joko', 'L', '2005-07-07', '9919190', 'Bekasi'),
('KK02', 'Budi', 'L', '1996-08-27', '133231', 'Bogor'),
('KK03', 'Winda', 'P', '2000-09-11', '0198232131', 'Depok'),
('KK04', 'Linda', 'P', '1990-11-21', '89199313', 'Tanggerang Selatan'),
('KK05', 'Suli', 'L', '2000-05-05', '08191992', 'Cianjur'),
('KK06', 'Wanto', 'P', '2002-05-07', '081923113', 'Bekasi'),
('KK07', 'Linda', 'P', '2006-06-15', '09199293', 'Cilengsi'),
('KK08', 'Yuri', 'P', '2001-06-13', '0819921', 'Bekasi'),
('KK09', 'Yuli', 'P', '2004-07-08', '08919313', 'Depok'),
('KK10', 'Dillah', 'L', '1998-08-19', '0891993193', 'Cilengsi'),
('KK11', 'geruu', 'P', '2004-11-04', '08128012', 'rumah'),
('KK12', 'Guraisu', 'P', '2000-06-14', '082838202', 'JAWA'),
('KK13', 'windah', 'P', '2004-06-09', '080200323', 'Bogor'),
('KK14', 'Wiwin', 'P', '1999-06-08', '080830820831', 'Cijantung');

-- --------------------------------------------------------

--
-- Struktur dari tabel `laporanpelanggan`
--

CREATE TABLE `laporanpelanggan` (
  `KodeBayar` varchar(100) NOT NULL,
  `Nama` varchar(100) NOT NULL,
  `Alamat` varchar(100) NOT NULL,
  `Telp` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Status` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pelanggan`
--

CREATE TABLE `pelanggan` (
  `KodeP` varchar(50) NOT NULL,
  `NamaP` varchar(50) NOT NULL,
  `AlamatP` varchar(100) NOT NULL,
  `Telp` varchar(50) NOT NULL,
  `Email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pelanggan`
--

INSERT INTO `pelanggan` (`KodeP`, `NamaP`, `AlamatP`, `Telp`, `Email`) VALUES
('KP01', 'Joko', 'Depok', '081919222', 'Jokokeren@gmail.com'),
('KP02', 'Fabio', 'Margonda', '019199313', 'FabioMitikGlory@gmail.com'),
('KP03', 'Hanafi', 'Bojong', '0883018', 'Hanafiproo@gmail.com'),
('KP04', 'Raihan', 'Rambutan', '018318931', 'RaihanMitikGrading@gmail.com'),
('KP05', 'Abiyyu', 'Cirebon', '018399132313', 'Arsenal000@gmail.com'),
('KP06', 'Pak Fauzan', 'Unindra', '0881820812', 'adjhi@gmail.com'),
('KP07', 'EPQEQ', 'ADADA', '0796', 'EAD@gmail.com'),
('KP08', 'Abdillah', 'Cilengsi', '0892912912', 'DilahGG@gmail.com'),
('KP09', 'Yusuf', 'bogor', '089913131', 'yusuf@gmail.com'),
('KP10', 'Budi', 'Bekasi', '0813913913', 'Budi@gmail.com'),
('KP11', 'Ryu', 'cibubur', '08380183', 'ryuji@gmail.com'),
('KP12', 'daud', 'jatinangor', '018319313', 'Dud@gmail.com'),
('KP13', 'Lala', 'jatiwaringin', '0831803801', 'LALALAL@gmail.com'),
('KP14', 'LILI', 'KECAPI', '0183083', 'LILIA@gmail.com');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pembayaran`
--

CREATE TABLE `pembayaran` (
  `KodeBayar` varchar(50) NOT NULL,
  `TGL` date NOT NULL,
  `NamaPemesan` varchar(100) NOT NULL,
  `TotalB` int(11) NOT NULL,
  `Bayar` int(11) NOT NULL,
  `Kembalian` int(11) NOT NULL,
  `Status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pembayaran`
--

INSERT INTO `pembayaran` (`KodeBayar`, `TGL`, `NamaPemesan`, `TotalB`, `Bayar`, `Kembalian`, `Status`) VALUES
('B01', '2026-06-22', 'Joko', 125000, 200000, 75000, 'Lunas'),
('B02', '2026-06-22', 'Fabio', 17500, 10000, -7500, 'BELUM LUNAS'),
('B03', '2026-06-22', 'Hanafi', 3200000, 400000, -2800000, 'BELUM LUNAS'),
('B04', '2026-06-22', 'daud', 60000, 70000, 10000, 'Lunas'),
('B05', '2026-06-23', 'LILI', 200000, 200000, 0, 'Lunas'),
('B07', '2026-07-07', 'Hanafi', 60000, 60000, 0, 'Lunas');

-- --------------------------------------------------------

--
-- Struktur dari tabel `suplier`
--

CREATE TABLE `suplier` (
  `KodeS` varchar(100) NOT NULL,
  `NamaS` varchar(100) NOT NULL,
  `BarangS` varchar(100) NOT NULL,
  `AlamatS` varchar(300) NOT NULL,
  `TelpS` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `suplier`
--

INSERT INTO `suplier` (`KodeS`, `NamaS`, `BarangS`, `AlamatS`, `TelpS`) VALUES
('KS01', 'PTKERTAS', 'KERTAS', 'DEPOK', '08193913'),
('KS02', 'PTPLASTIK', 'PLASTIK', 'BOGOR', '0193931'),
('KS03', 'PTKEREN', 'CAT WARNA', 'CIKAMPEK', '02019313'),
('KS04', 'PTGG', 'KERTAS NCR', 'BANDUNG', '088318081'),
('KS05', 'PTPTP', 'TINTA', 'PONDOK', '0131313'),
('KS06', 'PT PENSIL', 'PENSIL WARNA', 'JAKARTA', '01838183'),
('KS07', 'PTPPP', 'KERTAS', 'CIBINONG', '9080138081'),
('KS08', 'PT JAYA', 'BAHAN BANNER', 'BOGOR', '90808018'),
('KS09', 'PT YUYU', 'KERTAS KORAN', 'Jatiwarna', '08080810'),
('KS10', 'PTPTPTPTP', 'Buku', 'Jatinegara', '9880808080'),
('KS11', 'PT', 'Cat ', 'bekasi', '08810830'),
('KS12', 'PT UHUY', 'Cat warna', 'Kranggan', '0808308103'),
('KS13', 'PT CIHUY', 'KORAN', 'PEKALONGAN', '08083081038'),
('KS14', 'PTPTTPTP', 'BUKU WARNA', 'CIBUBUR', '080183081038');

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksipemesanan`
--

CREATE TABLE `transaksipemesanan` (
  `NoTransaksi` varchar(100) NOT NULL,
  `KodePesanan` varchar(100) NOT NULL,
  `Tanggal` date NOT NULL,
  `Jam` time NOT NULL,
  `KodeBR` varchar(100) NOT NULL,
  `Jumlah` int(11) NOT NULL,
  `Total` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `transaksipemesanan`
--

INSERT INTO `transaksipemesanan` (`NoTransaksi`, `KodePesanan`, `Tanggal`, `Jam`, `KodeBR`, `Jumlah`, `Total`) VALUES
('T01', 'KP01', '2026-06-22', '15:23:49', 'KB02', 5, 125000),
('T02', 'KP02', '2026-06-22', '15:23:49', 'KB05', 50, 17500),
('T03', 'KP03', '2026-06-22', '15:23:49', 'KB11', 40, 3200000),
('T04', 'KP07', '2026-06-22', '15:23:49', 'KB10', 2, 80000),
('T05', 'KP09', '2026-06-22', '15:23:49', 'KB11', 30, 2400000),
('T06', 'KP09', '2026-06-22', '15:23:49', 'KB12', 30, 1350000),
('T07', 'KP09', '2026-06-22', '15:23:49', 'KB05', 30, 10500),
('T08', 'KP11', '2026-06-22', '15:23:49', 'KB07', 5, 125000),
('T09', 'KP12', '2026-06-22', '15:23:49', 'KB04', 100, 30000),
('T10', 'KP12', '2026-06-22', '15:23:49', 'KB01', 2, 30000),
('T11', 'KP14', '2026-06-22', '15:23:49', 'KB08', 5, 50000),
('T12', 'KP14', '2026-06-22', '15:23:49', 'KB06', 50, 150000),
('T13', 'KP06', '2026-06-22', '15:23:49', 'KB02', 5, 125000),
('T14', 'KP05', '2026-06-22', '15:23:49', 'KB11', 5, 400000),
('T15', 'KP03', '2026-06-23', '14:18:04', 'KB03', 15, 7500),
('T16', 'KP06', '2026-07-07', '09:32:21', 'KB08', 30, 300000),
('T17', 'KP03', '2026-07-07', '10:23:40', 'KB06', 20, 60000);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `databarang`
--
ALTER TABLE `databarang`
  ADD PRIMARY KEY (`KodeB`);

--
-- Indeks untuk tabel `detailpembayaran`
--
ALTER TABLE `detailpembayaran`
  ADD PRIMARY KEY (`id`),
  ADD KEY `KPembayaran` (`KPembayaran`),
  ADD KEY `noTransaksi` (`noTransaksi`);

--
-- Indeks untuk tabel `karyawan`
--
ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`Kode`);

--
-- Indeks untuk tabel `laporanpelanggan`
--
ALTER TABLE `laporanpelanggan`
  ADD PRIMARY KEY (`KodeBayar`);

--
-- Indeks untuk tabel `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`KodeP`);

--
-- Indeks untuk tabel `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD PRIMARY KEY (`KodeBayar`);

--
-- Indeks untuk tabel `suplier`
--
ALTER TABLE `suplier`
  ADD PRIMARY KEY (`KodeS`);

--
-- Indeks untuk tabel `transaksipemesanan`
--
ALTER TABLE `transaksipemesanan`
  ADD PRIMARY KEY (`NoTransaksi`),
  ADD KEY `KodePesanan` (`KodePesanan`),
  ADD KEY `KodeBR` (`KodeBR`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `detailpembayaran`
--
ALTER TABLE `detailpembayaran`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `detailpembayaran`
--
ALTER TABLE `detailpembayaran`
  ADD CONSTRAINT `detailpembayaran_ibfk_1` FOREIGN KEY (`KPembayaran`) REFERENCES `pembayaran` (`KodeBayar`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `detailpembayaran_ibfk_2` FOREIGN KEY (`noTransaksi`) REFERENCES `transaksipemesanan` (`NoTransaksi`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `transaksipemesanan`
--
ALTER TABLE `transaksipemesanan`
  ADD CONSTRAINT `transaksipemesanan_ibfk_1` FOREIGN KEY (`KodePesanan`) REFERENCES `pelanggan` (`KodeP`) ON UPDATE CASCADE,
  ADD CONSTRAINT `transaksipemesanan_ibfk_2` FOREIGN KEY (`KodeBR`) REFERENCES `databarang` (`KodeB`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
