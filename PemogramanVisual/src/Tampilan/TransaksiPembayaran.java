/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tampilan;
import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Calendar;
import java.util.Date;
import javax.swing.SpinnerDateModel;
import javax.swing.JSpinner;
import java.text.SimpleDateFormat;
import javax.swing.ListSelectionModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Component;

/**
 *
 * @author ADVAN
 */
public class TransaksiPembayaran extends javax.swing.JInternalFrame {
private Statement St;
private Connection Con;
private ResultSet Rs;
private String sql="";
    /**
     * Creates new form TransaksiPenerimaan
     */
    public TransaksiPembayaran() {
        initComponents();
        setClosable(true);
        setMaximizable(false);
        setIconifiable(true); 
        setResizable(true);
        tampilTransaksi();
        tampilPembayaran();
        autonumberBayar();
        Kbayar.setEditable(false);
        TotalB.setEditable (false);
        Kembali.setEditable(false);
        
        
       Ttransaksi.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
       Ttransaksi.getColumnModel().getColumn(0).setCellEditor(
    Ttransaksi.getDefaultEditor(Boolean.class)
);

Ttransaksi.getColumnModel().getColumn(0).setCellRenderer(
    Ttransaksi.getDefaultRenderer(Boolean.class)
);
        
    }

    private void autonumberBayar() {

    try {
        Connection con = Koneksi.getConnection();
        Statement st = con.createStatement();

        String sql = "SELECT KodeBayar FROM pembayaran ORDER BY KodeBayar DESC LIMIT 1";
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {

            String kode = rs.getString("KodeBayar");

            // =========================
            // ANTI NULL + TRIM
            // =========================
            if (kode == null || kode.trim().isEmpty()) {
                Kbayar.setText("B01");
                return;
            }

            kode = kode.trim();

            // AMBIL ANGKA SAJA
            String angkaStr = kode.replaceAll("[^0-9]", "");

            int angka = 0;

            if (!angkaStr.isEmpty()) {
                angka = Integer.parseInt(angkaStr);
            }

            angka++;

            // FORMAT 2 DIGIT
            String hasil = String.format("B%02d", angka);

            Kbayar.setText(hasil);

        } else {
            Kbayar.setText("B01");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
    
    //Tampil Tabel Transaksi
  private void tampilTransaksi() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // hanya kolom Pilih (checkbox)
            }
 
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : Object.class;
            }
        };
        model.addColumn("Pilih");
        model.addColumn("NoTransaksi");
        model.addColumn("Tanggal");
        model.addColumn("Nama");
        model.addColumn("KodeBarang");
        model.addColumn("Jenis");
        model.addColumn("Total");
 
        try {
            Connection con = Koneksi.getConnection();
            Statement st = con.createStatement();
 
            // Hanya tampilkan transaksi yang BELUM dibayar
            String sql =
                "SELECT t.NoTransaksi, t.Tanggal, p.NamaP, " +
                "t.KodeBR, b.JenisB, t.Total " +
                "FROM transaksipemesanan t " +
                "JOIN pelanggan p ON t.KodePesanan = p.KodeP " +
                "JOIN databarang b ON t.KodeBR = b.KodeB " +
                "WHERE t.NoTransaksi NOT IN (" +
                "    SELECT NoTransaksi FROM detailpembayaran" +
                ")";
 
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    false,
                    rs.getString("NoTransaksi"),
                    rs.getString("Tanggal"),
                    rs.getString("NamaP"),
                    rs.getString("KodeBR"),
                    rs.getString("JenisB"),
                    rs.getInt("Total")
                });
            }
            Ttransaksi.setModel(model);
 
            // Render checkbox beneran
            Ttransaksi.getColumnModel().getColumn(0)
                .setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value, boolean isSelected,
                            boolean hasFocus, int row, int column) {
                        JCheckBox cb = new JCheckBox();
                        cb.setSelected(value != null && (Boolean) value);
                        cb.setHorizontalAlignment(JCheckBox.CENTER);
                        cb.setBackground(isSelected
                                ? table.getSelectionBackground()
                                : table.getBackground());
                        return cb;
                    }
                });
            Ttransaksi.getColumnModel().getColumn(0)
                .setCellEditor(new DefaultCellEditor(new JCheckBox()));
 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal load transaksi: " + e.getMessage());
        }
    }
    
  
  //Tampil Tabel Pembayaran
private void tampilPembayaran() {
    DefaultTableModel model = new DefaultTableModel(
        new Object[][]{},
        new String[]{"Kode Bayar", "Tanggal", "Nama Pemesan", "Total", "Status"}
    ) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };

    try {
        Connection con = Koneksi.getConnection();
        Statement st = con.createStatement();
        String sql = "SELECT KodeBayar, TGL, NamaPemesan, TotalB, Status " +
                     "FROM pembayaran ORDER BY TGL DESC";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("KodeBayar"),
                rs.getString("TGL"),
                rs.getString("NamaPemesan"),
                rs.getInt("TotalB"),
                rs.getString("Status")
            });
        }
        Tbayar.setModel(model);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal: " + e.getMessage());
    }
}
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        tp = new javax.swing.JLabel();
        kp = new javax.swing.JLabel();
        Kbayar = new javax.swing.JTextField();
        eml = new javax.swing.JLabel();
        TotalB = new javax.swing.JTextField();
        tgl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tbayar = new javax.swing.JTable();
        Tanggal = new de.wannawork.jcalendar.JCalendarComboBox();
        kp2 = new javax.swing.JLabel();
        Bayar = new javax.swing.JTextField();
        Kembali = new javax.swing.JTextField();
        kp5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Ttransaksi = new javax.swing.JTable();
        smpn = new javax.swing.JButton();
        bayar = new javax.swing.JButton();
        Total = new javax.swing.JButton();
        Batal = new javax.swing.JButton();
        Hapus = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        Hapus1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        tp.setBackground(new java.awt.Color(255, 255, 255));
        tp.setFont(new java.awt.Font("Tahoma", 1, 55)); // NOI18N
        tp.setForeground(new java.awt.Color(255, 255, 255));
        tp.setText("TRANSAKSI PEMBAYARAN");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(326, 326, 326)
                .addComponent(tp)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
        );

        kp.setText("Kode Pembayaran");

        Kbayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KbayarActionPerformed(evt);
            }
        });

        eml.setText("Total Bayar");

        TotalB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalBActionPerformed(evt);
            }
        });

        tgl.setText("Tanggal");

        Tbayar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Pembayaran", "Tanggal", "Nama", "Total", "Status"
            }
        ));
        Tbayar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TbayarMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tbayar);

        kp2.setText("Bayar");

        Bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BayarActionPerformed(evt);
            }
        });

        Kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KembaliActionPerformed(evt);
            }
        });

        kp5.setText("Kembalian");

        Ttransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Checklist", "NoTransaksi", "Tanggal", "Nama", "Kode Barang", "Jenis", "Total"
            }
        ));
        Ttransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TtransaksiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(Ttransaksi);

        smpn.setText("SIMPAN");
        smpn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smpnActionPerformed(evt);
            }
        });

        bayar.setText("BAYAR");
        bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarActionPerformed(evt);
            }
        });

        Total.setText("Total");
        Total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalActionPerformed(evt);
            }
        });

        Batal.setText("BATAL");
        Batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BatalActionPerformed(evt);
            }
        });

        Hapus.setText("HAPUS");
        Hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HapusActionPerformed(evt);
            }
        });

        Edit.setText("EDIT");
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });

        Hapus1.setText("Refresh");
        Hapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Hapus1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(kp)
                                    .addComponent(tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(Kbayar)
                                    .addComponent(Tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1388, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(smpn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Batal, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Hapus1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1388, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(eml)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(kp2)
                                .addComponent(kp5)))
                        .addGap(77, 77, 77)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Kembali, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Bayar)
                            .addComponent(TotalB))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Total, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(861, 861, 861))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kp)
                    .addComponent(Kbayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tgl)
                    .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(eml)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TotalB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Total)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kp2)
                    .addComponent(Bayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bayar))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Kembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kp5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(smpn)
                    .addComponent(Batal)
                    .addComponent(Hapus)
                    .addComponent(Edit)
                    .addComponent(Hapus1))
                .addGap(75, 75, 75)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TotalBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalBActionPerformed

    private void KbayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KbayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KbayarActionPerformed

    private void BayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BayarActionPerformed

    private void KembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KembaliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KembaliActionPerformed

    private void smpnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smpnActionPerformed
String kodeBayar    = Kbayar.getText().trim();
String totalStr     = TotalB.getText().trim();
String bayarStr     = Bayar.getText().trim();
String kembalianStr = Kembali.getText().trim();

if (kodeBayar.isEmpty() || totalStr.isEmpty()
        || bayarStr.isEmpty() || kembalianStr.isEmpty()) {
    JOptionPane.showMessageDialog(null, "Lengkapi semua field!");
    return;
}

DefaultTableModel model = (DefaultTableModel) Ttransaksi.getModel();
List<String> listNoTrx = new ArrayList<>();
String namaPemesan = "";

for (int i = 0; i < model.getRowCount(); i++) {
    Boolean dipilih = (Boolean) model.getValueAt(i, 0);
    if (dipilih != null && dipilih) {
        listNoTrx.add((String) model.getValueAt(i, 1));
        if (namaPemesan.isEmpty()) {
            namaPemesan = (String) model.getValueAt(i, 3);
        }
    }
}

if (listNoTrx.isEmpty()) {
    JOptionPane.showMessageDialog(null, "Pilih minimal 1 transaksi!");
    return;
}

int total = Integer.parseInt(totalStr);
int bayar = Integer.parseInt(bayarStr);

// Tentukan status otomatis berdasarkan bayar
String status;
if (bayar >= total) {
    status = "Lunas";
} else {
    status = "BELUM LUNAS";
}

Connection con = null;
try {
    con = Koneksi.getConnection();
    con.setAutoCommit(false);

    java.util.Date tanggal = Tanggal.getDate();
    java.sql.Date sqlTgl = new java.sql.Date(tanggal.getTime());

    // 1. Insert ke tabel pembayaran
    PreparedStatement psHeader = con.prepareStatement(
        "INSERT INTO pembayaran " +
        "(KodeBayar, TGL, NamaPemesan, TotalB, Bayar, Kembalian, Status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)"
    );
    psHeader.setString(1, kodeBayar);
    psHeader.setDate(2, sqlTgl);
    psHeader.setString(3, namaPemesan);
    psHeader.setInt(4, total);
    psHeader.setInt(5, bayar);
    psHeader.setInt(6, Integer.parseInt(kembalianStr));
    psHeader.setString(7, status); // ← status dinamis
    psHeader.executeUpdate();

    // 2. Insert ke tabel detailpembayaran
    PreparedStatement psDetail = con.prepareStatement(
        "INSERT INTO detailpembayaran (KPembayaran, noTransaksi) VALUES (?, ?)"
    );
    for (String noTrx : listNoTrx) {
        psDetail.setString(1, kodeBayar);
        psDetail.setString(2, noTrx);
        psDetail.addBatch();
    }
    psDetail.executeBatch();

    con.commit();
    JOptionPane.showMessageDialog(null, "Pembayaran berhasil disimpan! Status: " + status);
    tampilTransaksi();
    tampilPembayaran();
    BatalActionPerformed(evt);
    autonumberBayar();

} catch (Exception e) {
    try { if (con != null) con.rollback(); } catch (SQLException ex) {}
    JOptionPane.showMessageDialog(null, "Gagal simpan: " + e.getMessage());
} finally {
    try { if (con != null) con.setAutoCommit(true); } catch (SQLException e) {}
}// TODO add your handling code here:
    }//GEN-LAST:event_smpnActionPerformed

    private void bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarActionPerformed
       int total = Integer.parseInt(TotalB.getText());
    int bayar = Integer.parseInt(Bayar.getText());

    Kembali.setText(String.valueOf(bayar - total));
 // TODO add your handling code here:
    }//GEN-LAST:event_bayarActionPerformed

    private void TotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalActionPerformed
  int total = 0;

    for (int i = 0; i < Ttransaksi.getRowCount(); i++) {

        Boolean pilih = (Boolean) Ttransaksi.getValueAt(i, 0);

        if (pilih != null && pilih) {
            total += Integer.parseInt(
                Ttransaksi.getValueAt(i, 6).toString()
            );
        }
    }

    TotalB.setText(String.valueOf(total));
// TODO add your handling code here:
    }//GEN-LAST:event_TotalActionPerformed

    private void BatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BatalActionPerformed
  autonumberBayar(); // tetap lanjut nomor terakhir dari DB
TotalB.setText("");
    Bayar.setText("");
    Kembali.setText("");

    // Uncheck semua checkbox di tabel atas
    DefaultTableModel model = (DefaultTableModel) Ttransaksi.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
        model.setValueAt(false, i, 0);
    }
// TODO add your handling code here:
    }//GEN-LAST:event_BatalActionPerformed

    private void TtransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TtransaksiMouseClicked
 int row = Ttransaksi.getSelectedRow();

    if (row != -1) {

        Boolean cek = (Boolean) Ttransaksi.getValueAt(row, 0);

        // kalau null dianggap false
        if (cek == null) {
            cek = false;
        }

        // toggle (true jadi false, false jadi true)
        Ttransaksi.setValueAt(!cek, row, 0);
    }
        // TODO add your handling code here:
    }//GEN-LAST:event_TtransaksiMouseClicked

    private void HapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HapusActionPerformed
// Cek apakah ada baris yang dipilih di tabel bawah (history pembayaran)
    int baris = Tbayar.getSelectedRow();
    if (baris == -1) {
        JOptionPane.showMessageDialog(null, "Pilih data pembayaran yang ingin dihapus!");
        return;
    }

    // Ambil kode bayar dari baris yang dipilih
    String kodeBayar = Tbayar.getValueAt(baris, 0).toString();

    // Konfirmasi hapus
    int konfirmasi = JOptionPane.showConfirmDialog(null,
        "Hapus pembayaran " + kodeBayar + "?\n" +
        "Data transaksi akan kembali ke daftar pemesanan.",
        "Konfirmasi Hapus",
        JOptionPane.YES_NO_OPTION
    );

    if (konfirmasi != JOptionPane.YES_OPTION) return;

    Connection con = null;
    try {
        con = Koneksi.getConnection();
        con.setAutoCommit(false);

        // 1. Hapus detail dulu (anak) baru header (induk) — urutan penting!
        PreparedStatement psDetail = con.prepareStatement(
            "DELETE FROM detailpembayaran WHERE KPembayaran = ?"
        );
        psDetail.setString(1, kodeBayar);
        psDetail.executeUpdate();

        // 2. Hapus dari tabel pembayaran (induk)
        PreparedStatement psHeader = con.prepareStatement(
            "DELETE FROM pembayaran WHERE KodeBayar = ?"
        );
        psHeader.setString(1, kodeBayar);
        psHeader.executeUpdate();

        con.commit();
        JOptionPane.showMessageDialog(null, 
            "Pembayaran " + kodeBayar + " berhasil dihapus!\n" +
            "Transaksi kembali ke daftar pemesanan."
        );

        // Refresh kedua tabel
        tampilTransaksi();  // transaksi muncul lagi di tabel atas
        tampilPembayaran(); // hilang dari history bawah

    } catch (Exception e) {
        try { if (con != null) con.rollback(); } catch (SQLException ex) {}
        JOptionPane.showMessageDialog(null, "Gagal hapus: " + e.getMessage());
    } finally {
        try { if (con != null) con.setAutoCommit(true); } catch (SQLException e) {}
    }
        // TODO add your handling code here:
    }//GEN-LAST:event_HapusActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
 int baris = Tbayar.getSelectedRow();

if (baris == -1) {
    JOptionPane.showMessageDialog(null, "Pilih data terlebih dahulu!");
    return;
}

try {

    Connection con = Koneksi.getConnection();
    con.setAutoCommit(false); // ✔ aman

    String kodeBayar = Tbayar.getValueAt(baris, 0).toString();


    // INPUT BARU
    int bayarBaru = Integer.parseInt(Bayar.getText());

    int total = Integer.parseInt(Tbayar.getValueAt(baris, 3).toString());

    int kembalian = bayarBaru - total;
    String status = (bayarBaru >= total) ? "Lunas" : "BELUM LUNAS";

    // UPDATE PEMBAYARAN
    String sql =
        "UPDATE pembayaran SET Bayar=?, Kembalian=?, Status=? WHERE KodeBayar=?";

    PreparedStatement pst = con.prepareStatement(sql);

    pst.setInt(1, bayarBaru);
    pst.setInt(2, kembalian);
    pst.setString(3, status);
    pst.setString(4, kodeBayar);

    pst.executeUpdate();

    // UPDATE LAPORAN PELANGGAN
    String sqlLaporan =
        "UPDATE laporanpelanggan lp " +
        "JOIN pembayaran pb ON lp.KodeBayar = pb.KodeBayar " +
        "SET lp.Status = pb.Status " +
        "WHERE pb.KodeBayar = ?";

    PreparedStatement pst2 = con.prepareStatement(sqlLaporan);
    pst2.setString(1, kodeBayar);
    pst2.executeUpdate();

    con.commit();

    JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");

    // REFRESH
    tampilPembayaran();
    autonumberBayar();

    TotalB.setText("");
    Bayar.setText("");
    Kembali.setText("");
    Tanggal.setDate(null);

} catch (Exception e) {

    try {
        Connection con = Koneksi.getConnection();
        con.rollback();
    } catch (Exception ex) {}

    JOptionPane.showMessageDialog(null, e.getMessage());
}    // TODO add your handling code here:
    }//GEN-LAST:event_EditActionPerformed

    private void TbayarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TbayarMouseClicked
int baris = Tbayar.getSelectedRow();

    if (baris != -1) {

        try {

            Connection con = Koneksi.getConnection();

            String kodeBayar = Tbayar.getValueAt(baris, 0).toString();

            String sql =
                "SELECT TGL, TotalB, Bayar, Kembalian " +
                "FROM pembayaran WHERE KodeBayar=?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, kodeBayar);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                
                // SET TEXT FIELD
                Kbayar.setText(kodeBayar);
                TotalB.setText(rs.getString("TotalB"));
                Bayar.setText(rs.getString("Bayar"));
                Kembali.setText(rs.getString("Kembalian"));

                // 🔥 FIX JCALENDAR
                java.sql.Date tgl = rs.getDate("TGL");
                Tanggal.setDate(tgl); // INI YANG BENAR

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }   // TODO add your handling code here:
    }//GEN-LAST:event_TbayarMouseClicked

    private void Hapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Hapus1ActionPerformed
tampilPembayaran();        // TODO add your handling code here:
    }//GEN-LAST:event_Hapus1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransaksiPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransaksiPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransaksiPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransaksiPembayaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TransaksiPembayaran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Batal;
    private javax.swing.JTextField Bayar;
    private javax.swing.JButton Edit;
    private javax.swing.JButton Hapus;
    private javax.swing.JButton Hapus1;
    private javax.swing.JTextField Kbayar;
    private javax.swing.JTextField Kembali;
    private de.wannawork.jcalendar.JCalendarComboBox Tanggal;
    private javax.swing.JTable Tbayar;
    private javax.swing.JButton Total;
    private javax.swing.JTextField TotalB;
    private javax.swing.JTable Ttransaksi;
    private javax.swing.JButton bayar;
    private javax.swing.JLabel eml;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel kp;
    private javax.swing.JLabel kp2;
    private javax.swing.JLabel kp5;
    private javax.swing.JButton smpn;
    private javax.swing.JLabel tgl;
    private javax.swing.JLabel tp;
    // End of variables declaration//GEN-END:variables
}
