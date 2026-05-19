package raven.application.form.other;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import static javax.swing.text.html.HTML.Attribute.ROWS;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import raven.application.Application;
import raven.application.DatabaseConnection;
import raven.application.form.other.BookDetail;


/**
 *
 * @author Raven
 */

public class FormSale extends javax.swing.JPanel {

    private static final int IMAGE_WIDTH = 150;
    private static final int IMAGE_HEIGHT = 190;
    private static final int PRODUCT_PANEL_HEIGHT = 300;
    private static final int PRODUCT_PANEL_WIDTH = 180;
    private static final int ROWS = 3; 
    private static final Font FONT = new Font("Bahnschrift", Font.PLAIN, 14);
    
    
    int userId;  // Add this variable to store the current user ID
    public FormSale(int userId) {
        this.userId = userId;  // Set the userId when the form is initialized
            System.out.println("FormSale initialized with User ID: " + this.userId);

//        if (this.userId == 0) {
//            this.userId=1;
//        }
        initComponents();
        
        btnPay.setEnabled(false);
        btnPreview.setEnabled(false);
        catIdNameMap = new HashMap<>(); 
        loadGenres();
        comboSortCat.addItem("All");  // Add "All" option
        for (String catName : catIdNameMap.keySet()) {
            comboSortCat.addItem(catName);
        }
        
        lb.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");
        
        
        jPanelProduct.setLayout(new GridLayout(0, ROWS, 10, 10));

        // Validate and repaint to ensure changes take effect
        this.revalidate();
        this.repaint();
        
        loadProductsForSale();
        comboSortCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadProductsForSale();
            }
        });
        
        // Listen for changes in the search field to filter products by title
        txtTitleSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loadProductsForSale();  // Reload products when the search term changes
            }
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSale = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        txtVAT = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lblCash = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCash = new javax.swing.JTextField();
        txtChange = new javax.swing.JTextField();
        cboPayment = new javax.swing.JComboBox<>();
        btnAddtocart = new javax.swing.JButton();
        btnPay = new javax.swing.JButton();
        btnPreview = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        comboSortCat = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanelProduct = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtTitleSearch = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(918, 474));

        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb.setText("Book Store");

        jTableSale.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableSale.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Items", "Qty", "Amount"
            }
        ));
        jScrollPane1.setViewportView(jTableSale);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        jLabel3.setText("VAT : ");

        jLabel6.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        jLabel6.setText("Sub Total : ");

        jLabel7.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        jLabel7.setText("Total : ");

        txtSubTotal.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N

        txtVAT.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        txtVAT.setText("10%");
        txtVAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVATActionPerformed(evt);
            }
        });

        txtTotal.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVAT, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVAT, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblCash.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        lblCash.setText("Cash : ");

        jLabel8.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        jLabel8.setText("Payment Method :");

        jLabel9.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        jLabel9.setText("Change : ");

        txtCash.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        txtCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCashActionPerformed(evt);
            }
        });
        txtCash.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashKeyReleased(evt);
            }
        });

        txtChange.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N

        cboPayment.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        cboPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CASH", "ABA", "ACLEDA", " " }));
        cboPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPaymentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCash, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCash, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(txtChange, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(cboPayment, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCash, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCash, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtChange, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        btnAddtocart.setBackground(new java.awt.Color(0, 51, 51));
        btnAddtocart.setFont(new java.awt.Font("Poppins Black", 0, 14)); // NOI18N
        btnAddtocart.setForeground(new java.awt.Color(255, 255, 255));
        btnAddtocart.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\shopping_cart_add_icon_177407.png")); // NOI18N
        btnAddtocart.setText("Add to cart");
        btnAddtocart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddtocartActionPerformed(evt);
            }
        });

        btnPay.setBackground(new java.awt.Color(51, 153, 0));
        btnPay.setFont(new java.awt.Font("Poppins Black", 0, 14)); // NOI18N
        btnPay.setForeground(new java.awt.Color(255, 255, 255));
        btnPay.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\AGTupdatedrivers_11244.png")); // NOI18N
        btnPay.setText("Pay & Print");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        btnPreview.setBackground(new java.awt.Color(0, 51, 102));
        btnPreview.setFont(new java.awt.Font("Poppins Black", 0, 14)); // NOI18N
        btnPreview.setForeground(new java.awt.Color(255, 255, 255));
        btnPreview.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\ghostview_search_document_6161.png")); // NOI18N
        btnPreview.setText("Preview");
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(204, 102, 0));
        btnReset.setFont(new java.awt.Font("Poppins Black", 0, 14)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\systemsoftwareupdate_94333.png")); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        jLabel10.setText("Sort by Genre: ");

        btnLogout.setBackground(new java.awt.Color(153, 153, 153));
        btnLogout.setFont(new java.awt.Font("Poppins Black", 0, 14)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\Logout_37127.png")); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        jPanelProduct.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelProductLayout = new javax.swing.GroupLayout(jPanelProduct);
        jPanelProduct.setLayout(jPanelProductLayout);
        jPanelProductLayout.setHorizontalGroup(
            jPanelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 973, Short.MAX_VALUE)
        );
        jPanelProductLayout.setVerticalGroup(
            jPanelProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 391, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanelProduct);
        jPanelProduct.getAccessibleContext().setAccessibleName("");
        jPanelProduct.getAccessibleContext().setAccessibleDescription("");

        jLabel11.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        jLabel11.setText("Sort by Title : ");

        txtTitleSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTitleSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnLogout))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnPay, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnPreview, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAddtocart)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboSortCat, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTitleSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(36, 36, 36)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddtocart, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboSortCat, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtTitleSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnLogout)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(btnPay, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(32, 32, 32)
                            .addComponent(btnPreview))
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    private void txtVATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVATActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVATActionPerformed

    private void txtCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCashActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCashActionPerformed

    private void txtCashKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCashKeyReleased
        // TODO add your handling code here:
        String cashStr = txtCash.getText();
        double cash;

        try {
            cash = Double.parseDouble(cashStr);
        } catch (NumberFormatException e) {
            txtChange.setText("0.00");
            btnPay.setEnabled(false);
            btnPreview.setEnabled(false);
            return;
        }

        double totalAmount;
        try {
            totalAmount = Double.parseDouble(txtTotal.getText());
        } catch (NumberFormatException e) {
            totalAmount = 0;
        }

        if (cash >= totalAmount) {
            double change = cash - totalAmount;
            txtChange.setText(String.format("%.2f", change));
            btnPay.setEnabled(true);
            btnPreview.setEnabled(true);
        } else {
            txtChange.setText("0.00");
            btnPay.setEnabled(false);
            btnPreview.setEnabled(false);
        }
    }//GEN-LAST:event_txtCashKeyReleased
    private double AddVAT = 0;
    private void cboPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPaymentActionPerformed
        // TODO add your handling code here:
        if(cboPayment.getSelectedItem().equals("ABA")){
            lblCash.setText("ABA");
            txtCash.setText(txtTotal.getText());
            txtCash.setEditable(false);
            btnPay.setEnabled(true);
            btnPreview.setEnabled(true);
            txtChange.setText("0.0");
        }else if(cboPayment.getSelectedItem().equals("ACLEDA")){
            lblCash.setText("Acleda");
            txtCash.setText(txtTotal.getText());
            txtCash.setEditable(false);
            btnPay.setEnabled(true);
            btnPreview.setEnabled(true);
            txtChange.setText("0.0");
        }else if(cboPayment.getSelectedItem().equals("CASH")){
            lblCash.setText("Cash");
            txtCash.setText("");
            txtCash.setEditable(true);
            btnPay.setEnabled(false);
            btnPreview.setEnabled(false);
            txtChange.setText("0.0");
        }
    }//GEN-LAST:event_cboPaymentActionPerformed

    private void btnAddtocartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddtocartActionPerformed
        // TODO add your handling code here:
        addToCart();
    }//GEN-LAST:event_btnAddtocartActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        // TODO add your handling code here:
        showConfirmationDialog();;
    }//GEN-LAST:event_btnPayActionPerformed

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private void showConfirmationDialog() {
        DefaultTableModel model = (DefaultTableModel) jTableSale.getModel();
        int rowCount = model.getRowCount();

        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "No items in the cart to pay for.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder confirmationMessage = new StringBuilder("<html><body>");
        confirmationMessage.append("<h3>Confirm Payment</h3>");
        confirmationMessage.append("<table border='1'><tr><th>Product</th><th>Qty</th><th>Sub Total</th></tr>");

        double totalAmount = 0;
        for (int i = 0; i < rowCount; i++) {
            String product = (String) model.getValueAt(i, 0);
            int qty = (int) model.getValueAt(i, 1);
            double totalPrice = (double) model.getValueAt(i, 2);
            totalAmount += totalPrice;
            confirmationMessage.append("<tr><td>").append(product).append("</td><td>").append(qty).append("</td><td>").append(totalPrice).append("</td></tr>");
        }
        double total=Double.parseDouble(txtTotal.getText());
        df.format(total);
        confirmationMessage.append("</table><br><h4>VAT: %").append("10").append("</h4>");
        confirmationMessage.append("</table><h4>Total: $").append(total).append("</h4>");
        confirmationMessage.append("</body></html>");

        int confirm = JOptionPane.showConfirmDialog(this, confirmationMessage.toString(), "Confirm Payment", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            handleOrderPayment();
        }
    }
    
    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        // TODO add your handling code here:
            try {
                // Use a JasperReports viewer to preview the report
                String reportPath = "src/raven/application/form/other/Invoice.jasper";
                Map<String, Object> parameters = generateReportParameters();

                // Get the list of book details from the table to pass to the report
                List<BookDetail> bookDetails = getBookDetailsFromTable();
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(bookDetails);

                // Show preview (using a JasperViewer if you’re in a Java environment)
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters, dataSource);
                JasperViewer.viewReport(jasperPrint, false);


            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error displaying preview: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

   // String staffName = getStaffName(userId);
    
    private Map<String, Object> generateReportParameters() {            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("InvoiceID", 123);
            parameters.put("StaffName", "Jonh Cena");
            parameters.put("InvoiceDate", java.sql.Timestamp.valueOf(LocalDateTime.now()));
            parameters.put("PaymentMethod", cboPayment.getSelectedItem());
            parameters.put("Subtotal", Double.parseDouble(txtSubTotal.getText()));
            parameters.put("VAT", 0.10 * Double.parseDouble(txtSubTotal.getText()));
            parameters.put("TotalAmount", Double.parseDouble(txtTotal.getText()));
            parameters.put("ChangeAmount", Double.parseDouble(txtChange.getText()));
            return parameters;
    }//GEN-LAST:event_btnPreviewActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTableSale.getModel();
        model.setRowCount(0);
        AddVAT = 0;
        Subtotal(0);
        TotalAmount(0);
        txtCash.setEditable(true);
        lblCash.setText("Cash");
        cboPayment.setSelectedItem("Cash");

        JOptionPane.showMessageDialog(this, "Cart has been reset!", "Message", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtTitleSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitleSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTitleSearchActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        Application.logout();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private Map<String, String> catIdNameMap;
    private void loadProductsForSale() {
        String selectedCatName = (String) comboSortCat.getSelectedItem();
        String selectedCatID = catIdNameMap.get(selectedCatName); // Get genre ID from the map
        
        // Get the search term from the txtTitleSearch field
        String searchTerm = txtTitleSearch.getText().trim();

        // Base SQL query to join the books and sales tables
        String sql = "SELECT b.book_id, b.title, s.qty_sold, s.sale_price, b.image " +
                     "FROM tblsales s " +
                     "JOIN tblbooks b ON s.book_id = b.book_id ";

        // If a specific genre is selected, add a WHERE clause to filter by genre
        if (!"All".equals(selectedCatName)) {
            sql += "WHERE b.book_id IN (SELECT book_id FROM tblbooks_genres WHERE genre_id = ?) ";
        }

        // If there is a search term, add a condition to filter by title
        if (!searchTerm.isEmpty()) {
            if (sql.contains("WHERE")) {
                sql += "AND b.title LIKE ? ";
            } else {
                sql += "WHERE b.title LIKE ? ";
            }
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int paramIndex = 1;

            // Set the genre ID if a specific genre is selected
            if (!"All".equals(selectedCatName)) {
                preparedStatement.setString(paramIndex++, selectedCatID);
            }

            // Set the search term if it exists
            if (!searchTerm.isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + searchTerm + "%");
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            jPanelProduct.removeAll();  // Clear the panel to reload items
            while (resultSet.next()) {
                String id = resultSet.getString("book_id");
                String name = resultSet.getString("title");
                String qty = resultSet.getString("qty_sold");
                String price = resultSet.getString("sale_price");
                String photoPath = resultSet.getString("image");

                JPanel productItemPanel = new JPanel(new GridBagLayout());
                productItemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                productItemPanel.setPreferredSize(new Dimension(PRODUCT_PANEL_WIDTH, PRODUCT_PANEL_HEIGHT));
                productItemPanel.setMaximumSize(new Dimension(PRODUCT_PANEL_WIDTH, PRODUCT_PANEL_HEIGHT));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx=1.0;

                
                // Product Image
                ImageIcon imageIcon = new ImageIcon(photoPath);
                Image image = imageIcon.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                JLabel lblImage = new JLabel(new ImageIcon(image));
                lblImage.setHorizontalAlignment(SwingConstants.CENTER);
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                productItemPanel.add(lblImage, gbc);
                
                // Details panel with tighter spacing
                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

               // Product Name
            JLabel lblName = new JLabel(name);
            lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblName.setFont(FONT);
            detailsPanel.add(lblName);

            // Price
            JLabel lblPrice = new JLabel("Price: $" + price);
            lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
            detailsPanel.add(lblPrice);

            // Quantity Spinner
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.parseInt(qty), 1));
            quantitySpinner.setFont(FONT);
            quantitySpinner.setPreferredSize(new Dimension(120, 25));
            detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            detailsPanel.add(quantitySpinner);

            // Add the details panel using GridBagConstraints
            gbc.gridx = 0;
            gbc.gridy = 1; // Position it below the image
            gbc.gridwidth = 2; // Span across two columns if necessary
            gbc.fill = GridBagConstraints.HORIZONTAL;
            productItemPanel.add(detailsPanel, gbc);

            productItemPanel.putClientProperty("id", id);
            productItemPanel.putClientProperty("name", name);
            productItemPanel.putClientProperty("price", price);
            productItemPanel.putClientProperty("quantitySpinner", quantitySpinner);

            jPanelProduct.add(productItemPanel);
        }
        
        // Adjust the size of salePanel based on content
        int totalRows = (int) Math.ceil(jPanelProduct.getComponentCount() / (double) ROWS);
        jPanelProduct.setPreferredSize(new Dimension(PRODUCT_PANEL_WIDTH * ROWS, PRODUCT_PANEL_HEIGHT * totalRows));

        jPanelProduct.revalidate();
        jPanelProduct.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadGenres() {
    String sql = "SELECT * FROM tblgenres";
    try (java.sql.Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
            String catID = resultSet.getString("genre_id");
            String catName = resultSet.getString("genre_name");
          //  comboSortCat.addItem(resultSet.getString("CatName"));
            catIdNameMap.put(catName, catID);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void addToCart() {
        DefaultTableModel model = (DefaultTableModel) jTableSale.getModel();
        int rowCount = jPanelProduct.getComponentCount();
        double subtotal = 0;

        for (int i = 0; i < rowCount; i++) {
            JPanel productItemPanel = (JPanel) jPanelProduct.getComponent(i);
            JSpinner quantitySpinner = (JSpinner) productItemPanel.getClientProperty("quantitySpinner");
            int quantity = (int) quantitySpinner.getValue();
            String title = (String) productItemPanel.getClientProperty("name");
            String priceStr = (String) productItemPanel.getClientProperty("price");
            double price = Double.parseDouble(priceStr);

            if (quantity > 0) {
                double totalPrice = price * quantity;
                model.addRow(new Object[]{title, quantity, totalPrice});
                subtotal += totalPrice;
            }
            quantitySpinner.setValue(0);
        }

        AddVAT += subtotal;
        Subtotal(AddVAT);
        TotalAmount(AddVAT);
    }
    private void handleOrderPayment() {
        DefaultTableModel model = (DefaultTableModel) jTableSale.getModel();
        int rowCount = model.getRowCount();

        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "No items in the cart to pay for.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (this.userId == 0) {
            this.userId = 1;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            // Insert order into tblorders
            String insertOrderSql = "INSERT INTO tblorders (order_date, user_id, amount, status) VALUES (?, ?, ?, ?)";
            int generatedOrderId = 0;

            //System.out.println("Inserting order with User ID: " + this.userId); // Debug statement
            try (PreparedStatement insertOrderStmt = connection.prepareStatement(insertOrderSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                LocalDateTime now = LocalDateTime.now();
                insertOrderStmt.setTimestamp(1, java.sql.Timestamp.valueOf(now));
                insertOrderStmt.setInt(2, this.userId);
                insertOrderStmt.setDouble(3, Double.parseDouble(txtTotal.getText()));
                insertOrderStmt.setString(4, "Completed");

                insertOrderStmt.executeUpdate();
                try (ResultSet generatedKeys = insertOrderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedOrderId = generatedKeys.getInt(1);
                    }
                }
            }

            // Insert each order detail into tblorderdetails
            String insertOrderDetailsSql = "INSERT INTO tblorderdetails (order_id, book_id, qty, quoted_price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertOrderDetailsStmt = connection.prepareStatement(insertOrderDetailsSql)) {
                for (int i = 0; i < rowCount; i++) {
                    String bookTitle = (String) model.getValueAt(i, 0);
                    int qtySold = (int) model.getValueAt(i, 1);
                    double quotedPrice = (double) model.getValueAt(i, 2);

                    // Get the book ID from title and handle cases where it might not be found
                    int bookId = getBookIdFromTitle(bookTitle);
                    if (bookId == 0) {  // Assuming 0 indicates an invalid ID
                        System.err.println("Error: Book ID not found for title: " + bookTitle);
                        continue; // Optionally, you could throw an exception here
                    }

                    // Validate quantity and quoted price
                    if (qtySold <= 0 || quotedPrice < 0) {
                        System.err.println("Invalid qty or price for book: " + bookTitle);
                        continue; // Optionally, you could throw an exception here
                    }

                    // Set values for the batch insert
                    insertOrderDetailsStmt.setInt(1, generatedOrderId);
                    insertOrderDetailsStmt.setInt(2, bookId);
                    insertOrderDetailsStmt.setInt(3, qtySold);
                    insertOrderDetailsStmt.setDouble(4, quotedPrice);
                    insertOrderDetailsStmt.addBatch();
                }

                // Execute all batched inserts at once
                insertOrderDetailsStmt.executeBatch();
                //System.out.println("Order details successfully inserted.");
            }


            List<BookDetail> bookDetails = getBookDetailsFromTable();

            // Retrieve the full_name of the logged-in user
            String staffName = getStaffName(userId);
            double vat;
            try {
                // Remove any non-numeric characters, like '%', from the VAT field
                vat = Double.parseDouble(txtVAT.getText().replaceAll("[^\\d.]", "")) / 100;
            } catch (NumberFormatException e) {
                vat = 0.10; // Default to 10% if parsing fails
            }

            // Now you can use this `vat` in your calculations
            double subtotal = Math.round(Double.parseDouble(txtSubTotal.getText()));
            double totalVAT = Math.round(subtotal * vat * 100.0) / 100.0;
            double totalAmount = subtotal + totalVAT;

            // Generate the invoice PDF with the list of BookDetail
            generateInvoicePDF(generatedOrderId, staffName, new Date(), cboPayment.getSelectedItem().toString(), subtotal, totalVAT, totalAmount, Double.parseDouble(txtChange.getText()), bookDetails);

            // Update the quantity sold in tblsales
            //updateSalesQuantity(generatedOrderId);
            // Update the quantity sold in tblsales
            String selectQuery = "SELECT book_id, qty FROM tblorderdetails WHERE order_id = ?";
            String updateQuery = "UPDATE tblsales SET qty_sold = qty_sold - ? WHERE book_id = ?";
            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
                 PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {

                selectStmt.setInt(1, generatedOrderId);
                ResultSet rs = selectStmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found in tblorderdetails for orderId: " + generatedOrderId);
                }

                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    int qtyOrdered = rs.getInt("qty");

                    updateStmt.setInt(1, qtyOrdered);
                    updateStmt.setInt(2, bookId);
                    int rowsUpdated = updateStmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        System.out.println("Updated qty_sold for book_id " + bookId + " by subtracting " + qtyOrdered);
                    } else {
                        System.out.println("No matching book_id " + bookId + " found in tblsales to update.");
                    }
                }
            }
            
            
            //automateIncomeEntry(generatedOrderId);
            // Step 3: Create an income entry in tblincomes after successful payment
            //double totalAmount = Double.parseDouble(txtTotal.getText());  // Retrieve final amount from txtTotal
            String incomeType = "Book Order Sale";
            String remarks = "Order of " + rowCount + " books";  // Adjust as needed

            try (PreparedStatement incomeStmt = connection.prepareStatement(
                    "INSERT INTO tblincomes (income_type, amount, income_date, remarks) VALUES (?, ?, ?, ?)")) {
                incomeStmt.setString(1, incomeType);
                incomeStmt.setDouble(2, totalAmount);  // Set the final amount
                LocalDateTime now = LocalDateTime.now();
                incomeStmt.setTimestamp(3, java.sql.Timestamp.valueOf(now));
                incomeStmt.setString(4, remarks);

                int rowsInserted = incomeStmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Income entry created for order: " + generatedOrderId + " with amount $" + totalAmount);
                }
            }

            // Commit the transaction
            connection.commit();
            JOptionPane.showMessageDialog(this, "Payment successful and order recorded.", "Message", JOptionPane.INFORMATION_MESSAGE);

            resetCart();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Payment failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private List<BookDetail> getBookDetailsFromTable() {
        List<BookDetail> bookDetails = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) jTableSale.getModel();
        int rowCount = model.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            String bookTitle = (String) model.getValueAt(i, 0); // Item title
            int quantity = (int) model.getValueAt(i, 1); // Quantity
            double subTotal = (double) model.getValueAt(i, 2); // Amount

            bookDetails.add(new BookDetail(bookTitle, quantity, subTotal));
        }
        return bookDetails;
    }
    private void generateInvoicePDF(int orderId, String staffName, Date orderDate, String paymentMethod, double subtotal, double vat, double totalAmount, double changeAmount, List<BookDetail> bookDetails) {
        InvoiceGenerater invoiceGenerater = new InvoiceGenerater();
        invoiceGenerater.generateInvoice(orderId, staffName, orderDate, paymentMethod, subtotal, vat, totalAmount, changeAmount, bookDetails);
    }
    private String getStaffName(int userId) {
        String staffName = "";
        String query = "SELECT full_name FROM tblusers WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                staffName = rs.getString("full_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve staff name: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return staffName;
    } 
    private int getBookIdFromTitle(String title) {
        int bookId = 0;
        String query = "SELECT book_id FROM tblbooks WHERE title = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bookId = rs.getInt("book_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookId;
    }

    private void resetCart() {
        DefaultTableModel model = (DefaultTableModel) jTableSale.getModel();
        model.setRowCount(0);  // Clear the cart table

        // Reset the subtotal, VAT, and total amount
        AddVAT = 0;
        txtSubTotal.setText("0.00");
        txtTotal.setText("0.00");
        txtCash.setText("");
        txtChange.setText("0.00");
        lblCash.setText("Cash");
        cboPayment.setSelectedItem("CASH");  // Reset payment method

        JOptionPane.showMessageDialog(this, "Cart has been reset!", "Message", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void Subtotal(double amount) {
        txtSubTotal.setText(String.valueOf(amount));
    }
    private void TotalAmount(double amount) {
        double totalAmount = amount + (amount * 0.10); 
        txtTotal.setText(String.valueOf(totalAmount));
    }



    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddtocart;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cboPayment;
    private javax.swing.JComboBox<String> comboSortCat;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelProduct;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableSale;
    private javax.swing.JLabel lb;
    private javax.swing.JLabel lblCash;
    private javax.swing.JTextField txtCash;
    private javax.swing.JTextField txtChange;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTitleSearch;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtVAT;
    // End of variables declaration//GEN-END:variables
}
