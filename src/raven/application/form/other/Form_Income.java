package raven.application.form.other;

import com.formdev.flatlaf.FlatClientProperties;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import raven.application.DatabaseConnection;

/**
 *
 * @author Raven
 */
public class Form_Income extends javax.swing.JPanel {

    public Form_Income() {
        initComponents();
        loadDataFromDB();  
        
            // Add listener to jTable to populate text fields when a row is selected
            jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting() && jTable.getSelectedRow() != -1) {
                        
                        int selectedRow = jTable.getSelectedRow();
                        ComboType.setSelectedItem(jTable.getValueAt(selectedRow, 1).toString());
                        txtAmount.setText(jTable.getValueAt(selectedRow, 2).toString());
                        txtDescription.setText(jTable.getValueAt(selectedRow, 4).toString());
                        
                        // Convert the string to a Date object and set it in JDateChooser
                        try {
                            String dateString = jTable.getValueAt(selectedRow, 3).toString();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  // Assuming your date format is "yyyy-MM-dd"
                            Date date = dateFormat.parse(dateString);  // Parse the date string into a Date object
                            jDateChooser.setDate(date);  // Set the date in JDateChooser
                        } catch (ParseException ex) {
                            ex.printStackTrace();  // Handle parsing errors
                        }
                    }
                }
            });
            // Add window listener to refresh data when window is activated
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent e) {
                    loadDataFromDB();
                }
            });  
        
        lb.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");
    }

    private void loadDataFromDB() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM tblincomes")) {

            if (connection == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create Table Model from ResultSet
            DefaultTableModel tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Column Names
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Data Rows
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }
            jTable.setModel(tableModel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnInsert = new javax.swing.JButton();
        ComboType = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        txtDescription = new javax.swing.JTextField();

        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb.setText("Income Management");

        jTable.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable);

        jLabel2.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel2.setText("Income Type");

        jLabel4.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel4.setText("Amount");

        txtAmount.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        btnUpdate.setBackground(new java.awt.Color(51, 153, 255));
        btnUpdate.setFont(new java.awt.Font("Gotham Ultra", 0, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\ios-8-System-Update-icon_43809.png")); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setFont(new java.awt.Font("Gotham Ultra", 0, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\usertrash_92829.png")); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnInsert.setBackground(new java.awt.Color(255, 255, 255));
        btnInsert.setFont(new java.awt.Font("Gotham Ultra", 0, 18)); // NOI18N
        btnInsert.setForeground(new java.awt.Color(51, 204, 0));
        btnInsert.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\add_icon.png")); // NOI18N
        btnInsert.setText("Add");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        ComboType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Book Order Sale", "Service Fee", "Other" }));

        jLabel5.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel5.setText("Date");

        jLabel6.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel6.setText("Description");

        txtDescription.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDescription)
                            .addComponent(txtAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                            .addComponent(ComboType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnInsert)))
                .addContainerGap(48, Short.MAX_VALUE))
            .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lb, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                            .addComponent(ComboType))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                            .addComponent(jDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to update", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int incomeId = Integer.parseInt(jTable.getValueAt(selectedRow, 0).toString());
        String expenseType = ComboType.getSelectedItem().toString();
        String amount = txtAmount.getText();
        String description = txtDescription.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(jDateChooser.getDate());

        if (expenseType.isEmpty() || amount.isEmpty() || description.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Update tblexpenses
            String updateExpenseQuery = "UPDATE tblincomes SET income_type = ?, amount = ?, remarks = ?, income_date = ? WHERE income_id = ?";
            PreparedStatement psUpdateExpense = connection.prepareStatement(updateExpenseQuery);
            psUpdateExpense.setString(1, expenseType);
            psUpdateExpense.setDouble(2, Double.parseDouble(amount));
            psUpdateExpense.setString(3, description);
            psUpdateExpense.setString(4, date);
            psUpdateExpense.setInt(5, incomeId);

            int rowsUpdated = psUpdateExpense.executeUpdate();

            if (rowsUpdated > 0) {
                // Update tblexpense_details
//                String updateExpenseDetailQuery = "UPDATE tblexpense_details SET expense_type = ?, amount = ?, description = ? WHERE expense_id = ?";
//                PreparedStatement psUpdateExpenseDetail = connection.prepareStatement(updateExpenseDetailQuery);
//                psUpdateExpenseDetail.setString(1, expenseType);
//                psUpdateExpenseDetail.setDouble(2, Double.parseDouble(amount));
//                psUpdateExpenseDetail.setString(3, description);
//                psUpdateExpenseDetail.setInt(4, expenseId);
//                psUpdateExpenseDetail.executeUpdate();

                JOptionPane.showMessageDialog(this, "Income updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update income: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
       int selectedRow = jTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an income to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int incomeId = Integer.parseInt(jTable.getValueAt(selectedRow, 0).toString());

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Delete from tblexpense_details
//            String deleteExpenseDetailQuery = "DELETE FROM tblexpense_details WHERE expense_id = ?";
//            PreparedStatement psDeleteExpenseDetail = connection.prepareStatement(deleteExpenseDetailQuery);
//            psDeleteExpenseDetail.setInt(1, expenseId);
//            psDeleteExpenseDetail.executeUpdate();

            // Delete from tblexpenses
            String deleteExpenseQuery = "DELETE FROM tblincomes WHERE income_id = ?";
            PreparedStatement psDeleteExpense = connection.prepareStatement(deleteExpenseQuery);
            psDeleteExpense.setInt(1, incomeId);
            int rowsDeleted = psDeleteExpense.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Income deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete income: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        String expenseType = ComboType.getSelectedItem().toString();
        String amount = txtAmount.getText();
        String description = txtDescription.getText();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(jDateChooser.getDate());

        if (expenseType.isEmpty() || amount.isEmpty() || description.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String insertExpenseQuery = "INSERT INTO tblincomes (income_type, amount, remarks, income_date) VALUES (?, ?, ?, ?)";
            PreparedStatement psInsertExpense = connection.prepareStatement(insertExpenseQuery, Statement.RETURN_GENERATED_KEYS);
            psInsertExpense.setString(1, expenseType);
            psInsertExpense.setDouble(2, Double.parseDouble(amount));
            psInsertExpense.setString(3, description);
            psInsertExpense.setString(4, date);

            int rowsInserted = psInsertExpense.executeUpdate();

            if (rowsInserted > 0) {
//                ResultSet generatedKeys = psInsertExpense.getGeneratedKeys();
//                if (generatedKeys.next()) {
//                    int expenseId = generatedKeys.getInt(1);
//
//                    // Insert into tblexpense_details
//                    String insertExpenseDetailQuery = "INSERT INTO tblexpense_details (expense_id, expense_type, amount, description) VALUES (?, ?, ?, ?)";
//                    PreparedStatement psInsertExpenseDetail = connection.prepareStatement(insertExpenseDetailQuery);
//                    psInsertExpenseDetail.setInt(1, expenseId);
//                    psInsertExpenseDetail.setString(2, expenseType);
//                    psInsertExpenseDetail.setDouble(3, Double.parseDouble(amount));
//                    psInsertExpenseDetail.setString(4, description);
//                    psInsertExpenseDetail.executeUpdate();
//                }
                JOptionPane.showMessageDialog(this, "Income added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add income: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnInsertActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboType;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnUpdate;
    private com.toedter.calendar.JDateChooser jDateChooser;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JLabel lb;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtDescription;
    // End of variables declaration//GEN-END:variables
}
