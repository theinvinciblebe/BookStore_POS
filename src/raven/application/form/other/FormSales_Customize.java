package raven.application.form.other;

import com.formdev.flatlaf.FlatClientProperties;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.awt.ComponentOrientation;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableModel;
import raven.application.DatabaseConnection;

/**
 *
 * @author Raven
 */
public class FormSales_Customize extends javax.swing.JPanel {

    public FormSales_Customize() {
        initComponents();
        loadDataFromDB();
        loadTitle();
        //setupComboBoxFilter();
        
        txtStock.setEditable(false);
        txtStock_Price.setEditable(false);
        
        // Add listener to jTable to populate text fields when a row is selected
        jTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting() && jTable.getSelectedRow() != -1) {
                int selectedRow = jTable.getSelectedRow();
                int bookId = Integer.parseInt(jTable.getValueAt(selectedRow, 1).toString());  // Assuming book_id is in the 2nd column

                // Now update the ComboTitle based on the book_id
                updateComboTitleBasedOnBookId(bookId);

                // Update other fields
                txtPrice.setText(jTable.getValueAt(selectedRow, 4).toString());  // sale price
                txtQuantity.setText(jTable.getValueAt(selectedRow, 3).toString()); // sale quantity

                // Now load the stock price, stock quantity, and image based on the book title
                String title = ComboTitle.getSelectedItem().toString();
                loadBookDetails(title);  // Use the title to fetch details from the database
            }
        });
        // Add window listener to refresh data when window is activated
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                loadDataFromDB();
            }
        }); 
        ComboTitle.addActionListener((java.awt.event.ActionEvent evt) -> {
            loadBookImage();
        });
        
            ComboTitle.setMaximumRowCount(10);
            ComboTitle.setEditable(true);
            ComboTitle.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            ComboTitle.setLocale(Locale.ENGLISH);  // or any locale that uses left-to-right text


            lb.putClientProperty(FlatClientProperties.STYLE, ""
                    + "font:$h1.font");
            
        }
    
//    private void setupComboBoxFilter() {
//        ComboTitle.setEditable(true);
//        JTextField editor = (JTextField) ComboTitle.getEditor().getEditorComponent();
//        
//        editor.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyReleased(KeyEvent e) {
//                String input = editor.getText().toLowerCase();
//                ComboTitle.hidePopup();
//                
//                // Save all items temporarily
//                List<String> allItems = new ArrayList<>();
//                for (int i = 0; i < ComboTitle.getItemCount(); i++) {
//                    allItems.add(ComboTitle.getItemAt(i));
//                }
//                
//                // Clear items and re-add only matching items
//                ComboTitle.removeAllItems();
//                for (String item : allItems) {
//                    if (item.toLowerCase().contains(input)) {
//                        ComboTitle.addItem(item);
//                    }
//                }
//                
//                editor.setText(input);
//                ComboTitle.showPopup();
//            }
//        });
//    }
    
    
    private void loadDataFromDB() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("Select * from tblsales")) {

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
    private void loadBookImage() {
        String selectedTitle = ComboTitle.getSelectedItem().toString();

        // Validate the selection
        if (selectedTitle.isEmpty()) {
            label_photo.setIcon(null); // Clear the photo if no title is selected
            txtStock.setText(""); // Clear the stock quantity if no title is selected
            return;
        }else{
            txtQuantity.setText("");
            txtPrice.setText("");
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Query to get the image path and stock from tblbooks and tblimport_details based on the selected title
            String getBookDetailsQuery = "SELECT b.image, d.qty_import, d.import_price FROM tblbooks b " +
                                         "LEFT JOIN tblimports_details d ON b.book_id = d.book_id " +
                                         "WHERE b.title = ?";
            PreparedStatement psGetDetails = connection.prepareStatement(getBookDetailsQuery);
            psGetDetails.setString(1, selectedTitle);
            ResultSet rsDetails = psGetDetails.executeQuery();

            if (rsDetails.next()) {
                // Load and display the image
                String imagePath = rsDetails.getString("image");
                ImageIcon icon = resizeImage(imagePath, label_photo.getWidth(), label_photo.getHeight());
                if (icon != null) {
                    label_photo.setIcon(icon);
                } else {
                    label_photo.setIcon(null); // Clear the photo if the image couldn't be loaded
                    JOptionPane.showMessageDialog(this, "Failed to load image", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Load and display the stock quantity
                int stockQuantity = rsDetails.getInt("qty_import");
                txtStock.setText(String.valueOf(stockQuantity));
                
                double stockPrice = rsDetails.getDouble("import_price");
                txtStock_Price.setText(String.valueOf(stockPrice));
            } else {
                label_photo.setIcon(null); // Clear the photo if no image is found
                txtStock.setText("");  // Clear stock field if no record is found
                txtStock_Price.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load book details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    private List<String> originalTitles = new ArrayList<>();
    private void loadTitle() {
            try (Connection connection = DatabaseConnection.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM tblbooks;")) {
                    originalTitles.clear();  // Clear the list to avoid duplicates
                    ComboTitle.removeAllItems();  // Clear existing items in the ComboBox
                 while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    ComboTitle.addItem(title);
                    originalTitles.add(title);  // Store each title in the list
                }
                 
                // After populating ComboBox, manually trigger loading the image for the first title
                if (ComboTitle.getItemCount() > 0) {
                    ComboTitle.setSelectedIndex(0);  // Ensure the first item is selected
                    loadBookImage();  // Load and display the image for the first selected title
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    }
    private void loadBookDetails(String title) {
    // Validate the selection
    if (title.isEmpty()) {
        label_photo.setIcon(null); // Clear the photo if no title is selected
        txtStock.setText(""); // Clear stock quantity
        txtStock_Price.setText(""); // Clear stock price
        return;
    }

    try (Connection connection = DatabaseConnection.getConnection()) {
        // Query to get the image, stock quantity, and stock price from tblbooks and tblimports_details
        String getBookDetailsQuery = "SELECT b.image, d.qty_import, d.import_price FROM tblbooks b " +
                                     "LEFT JOIN tblimports_details d ON b.book_id = d.book_id " +
                                     "WHERE b.title = ?";
        PreparedStatement psGetDetails = connection.prepareStatement(getBookDetailsQuery);
        psGetDetails.setString(1, title);
        ResultSet rsDetails = psGetDetails.executeQuery();

        if (rsDetails.next()) {
            // Load and display the image
            String imagePath = rsDetails.getString("image");
            ImageIcon icon = resizeImage(imagePath, label_photo.getWidth(), label_photo.getHeight());
            if (icon != null) {
                label_photo.setIcon(icon);
            } else {
                label_photo.setIcon(null); // Clear the photo if the image couldn't be loaded
                JOptionPane.showMessageDialog(this, "Failed to load image", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Load and display the stock quantity
            int stockQuantity = rsDetails.getInt("qty_import");
            txtStock.setText(String.valueOf(stockQuantity));

            // Load and display the stock price
            double stockPrice = rsDetails.getDouble("import_price");
            txtStock_Price.setText(String.valueOf(stockPrice));
        } else {
            // Clear the fields if no matching data is found
            label_photo.setIcon(null);
            txtStock.setText("");
            txtStock_Price.setText("");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Failed to load book details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void updateComboTitleBasedOnBookId(int bookId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Query to get the title from tblbooks based on the book_id
            String getTitleQuery = "SELECT title FROM tblbooks WHERE book_id = ?";
            PreparedStatement psGetTitle = connection.prepareStatement(getTitleQuery);
            psGetTitle.setInt(1, bookId);
            ResultSet rsTitle = psGetTitle.executeQuery();

            if (rsTitle.next()) {
                String title = rsTitle.getString("title");
                ComboTitle.setSelectedItem(title);  // Set the title in ComboTitle
            } else {
                JOptionPane.showMessageDialog(this, "Book not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load book title: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        txtQuantity = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        ComboTitle = new javax.swing.JComboBox<>();
        label_photo = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtStock_Price = new javax.swing.JTextField();

        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\shop_icon-icons.com_51010.png")); // NOI18N
        lb.setText("Sale Customize");

        jTable.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable);

        txtQuantity.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel6.setText("Sale_Quantity");

        jLabel2.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel2.setText("Title");

        jLabel4.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel4.setText("Price");

        txtPrice.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        btnInsert.setBackground(new java.awt.Color(0, 204, 102));
        btnInsert.setFont(new java.awt.Font("Gotham Ultra", 0, 12)); // NOI18N
        btnInsert.setForeground(new java.awt.Color(255, 255, 255));
        btnInsert.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\add_icon.png")); // NOI18N
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

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

        btnDelete.setBackground(new java.awt.Color(102, 0, 0));
        btnDelete.setFont(new java.awt.Font("Gotham Ultra", 0, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\trash-can_115312.png")); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        ComboTitle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        label_photo.setBackground(new java.awt.Color(204, 204, 204));
        label_photo.setForeground(new java.awt.Color(204, 204, 204));
        label_photo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_photo.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image\\Add_Image_icon.png")); // NOI18N
        label_photo.setBorder(javax.swing.BorderFactory.createTitledBorder("Photo"));
        label_photo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txtStock.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel7.setText("Stock_Quantity");

        jLabel8.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel8.setText("Stock_Price");

        txtStock_Price.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8))
                                .addGap(28, 28, 28)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPrice)
                            .addComponent(ComboTitle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtQuantity)
                            .addComponent(txtStock)
                            .addComponent(txtStock_Price, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label_photo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(127, 127, 127)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnInsert, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lb, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ComboTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStock_Price, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnInsert)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate)
                                .addGap(31, 31, 31)
                                .addComponent(btnDelete))
                            .addComponent(label_photo, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private ImageIcon resizeImage(String imagePath, int width, int height) {
       try {
           BufferedImage originalImage = ImageIO.read(new File(imagePath));
           Image resizedImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
           return new ImageIcon(resizedImage);
       } catch (IOException e) {
           return null;
       }
    }
    
    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // Get data from form fields
        String title = ComboTitle.getSelectedItem().toString();  // Assuming ComboBox for book titles
        String price = txtPrice.getText();
        String saleQuantity = txtQuantity.getText();

        // Validate input
        if (title.isEmpty() || price.isEmpty() || saleQuantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Get book_id and qty_import from title (assuming book titles are unique)
            String getBookDetailsQuery = "SELECT b.book_id, d.qty_import FROM tblbooks b " +
                                         "LEFT JOIN tblimports_details d ON b.book_id = d.book_id " +
                                         "WHERE b.title = ?";
            PreparedStatement psGetBookDetails = connection.prepareStatement(getBookDetailsQuery);
            psGetBookDetails.setString(1, title);
            ResultSet rsBookDetails = psGetBookDetails.executeQuery();

            int bookId = -1;
            int currentStock = -1;

            if (rsBookDetails.next()) {
                bookId = rsBookDetails.getInt("b.book_id");
                currentStock = rsBookDetails.getInt("d.qty_import");
            } else {
                JOptionPane.showMessageDialog(this, "Book not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if the sale quantity is greater than the stock quantity
            int saleQty = Integer.parseInt(saleQuantity);
            if (saleQty > currentStock) {
                JOptionPane.showMessageDialog(this, "Insufficient stock for this sale", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Check if a sale record for this book already exists
            String checkExistingSaleQuery = "SELECT sale_id, qty_sold FROM tblsales WHERE book_id = ?";
            PreparedStatement psCheckExistingSale = connection.prepareStatement(checkExistingSaleQuery);
            psCheckExistingSale.setInt(1, bookId);
            ResultSet rsExistingSale = psCheckExistingSale.executeQuery();
        
            if (rsExistingSale.next()){
                 // Sale record exists, so update the quantity and price
                int existingQty = rsExistingSale.getInt("qty_sold");
                int newQty = existingQty + saleQty;

                String updateSaleQuery = "UPDATE tblsales SET qty_sold = ?, sale_price = ? WHERE book_id = ?";
                PreparedStatement psUpdateSale = connection.prepareStatement(updateSaleQuery);
                psUpdateSale.setInt(1, newQty);  // Add to the existing quantity
                psUpdateSale.setDouble(2, Double.parseDouble(price)); // Update with the new price
                psUpdateSale.setInt(3, bookId);
                psUpdateSale.executeUpdate();

                JOptionPane.showMessageDialog(this, "Sale record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                // Sale record does not exist, so insert a new record
                String insertSaleQuery = "INSERT INTO tblsales (book_id, qty_sold, sale_price) VALUES (?, ?, ?)";
                PreparedStatement psInsertSale = connection.prepareStatement(insertSaleQuery);
                psInsertSale.setInt(1, bookId);
                psInsertSale.setInt(2, saleQty);
                psInsertSale.setDouble(3, Double.parseDouble(price));
                psInsertSale.executeUpdate();

                JOptionPane.showMessageDialog(this, "New sale added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
                // Update the qty_import in tblimports_details
                String updateStockQuery = "UPDATE tblimports_details SET qty_import = qty_import - ? WHERE book_id = ?";
                PreparedStatement psUpdateStock = connection.prepareStatement(updateStockQuery);
                psUpdateStock.setInt(1, saleQty);
                psUpdateStock.setInt(2, bookId);
                psUpdateStock.executeUpdate();

                loadDataFromDB();  // Reload table data
                loadBookImage();  // Reload book image and stock details
                clearFormFields();  // Clear form after successful insertion
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add/update sale: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // Get data from form fields
        String title = ComboTitle.getSelectedItem().toString();
        String price = txtPrice.getText();
        String quantity = txtQuantity.getText();
        int selectedRow = jTable.getSelectedRow();
        int saleId = Integer.parseInt(jTable.getValueAt(selectedRow, 0).toString());

        // Validate input
        if (title.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Get book_id from title
            String getBookIdQuery = "SELECT book_id FROM tblbooks WHERE title = ?";
            PreparedStatement psGetBookId = connection.prepareStatement(getBookIdQuery);
            psGetBookId.setString(1, title);
            ResultSet rsBookId = psGetBookId.executeQuery();
            int bookId = -1;
            if (rsBookId.next()) {
                bookId = rsBookId.getInt("book_id");
            } else {
                JOptionPane.showMessageDialog(this, "Book not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update sale record
            String updateSaleQuery = "UPDATE tblsales SET book_id = ?, qty_sold = ?, sale_price = ? WHERE sale_id = ?";
            PreparedStatement psUpdateSale = connection.prepareStatement(updateSaleQuery);
            psUpdateSale.setInt(1, bookId);
            psUpdateSale.setInt(2, Integer.parseInt(quantity));
            psUpdateSale.setDouble(3, Double.parseDouble(price));
            psUpdateSale.setInt(4, saleId);
            int rowsUpdated = psUpdateSale.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Sale updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();  // Reload table data
                clearFormFields();  // Clear form after successful update
            }

            // Update stock quantity in tblimports_details
            String updateStockQuery = "UPDATE tblimports_details SET qty_import = qty_import - ? WHERE book_id = ?";
            PreparedStatement psUpdateStock = connection.prepareStatement(updateStockQuery);
            psUpdateStock.setInt(1, Integer.parseInt(quantity));  // Assuming qty_sold is deducted from qty_import
            psUpdateStock.setInt(2, bookId);
            psUpdateStock.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update sale: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a sale to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int saleId = Integer.parseInt(jTable.getValueAt(selectedRow, 0).toString());
        int qtySold = Integer.parseInt(jTable.getValueAt(selectedRow, 3).toString());
        int bookId = Integer.parseInt(jTable.getValueAt(selectedRow, 1).toString());

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Delete sale record
            String deleteSaleQuery = "DELETE FROM tblsales WHERE sale_id = ?";
            PreparedStatement psDeleteSale = connection.prepareStatement(deleteSaleQuery);
            psDeleteSale.setInt(1, saleId);
            int rowsDeleted = psDeleteSale.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Sale deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();  // Reload table data
                clearFormFields();  // Clear form after successful deletion
            }

            // Update stock quantity in tblimports_details (add back the qty_sold)
            String updateStockQuery = "UPDATE tblimports_details SET qty_import = qty_import + ? WHERE book_id = ?";
            PreparedStatement psUpdateStock = connection.prepareStatement(updateStockQuery);
            psUpdateStock.setInt(1, qtySold);  // Add back the sold quantity to stock
            psUpdateStock.setInt(2, bookId);
            psUpdateStock.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete sale: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void clearFormFields() {
        ComboTitle.setSelectedIndex(0);
        txtPrice.setText("");
        txtQuantity.setText("");
        txtStock.setText("");  
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboTitle;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JLabel label_photo;
    private javax.swing.JLabel lb;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtStock_Price;
    // End of variables declaration//GEN-END:variables
}
