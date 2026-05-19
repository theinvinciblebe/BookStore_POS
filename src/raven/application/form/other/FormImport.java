package raven.application.form.other;

import com.formdev.flatlaf.FlatClientProperties;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.awt.Image;
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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import raven.application.DatabaseConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author Raven
 */
public class FormImport extends javax.swing.JPanel {
    

    public FormImport() {
        initComponents();
        
        loadDataFromDB();
        loadSupplier();
        loadAuthor();
        loadGenre();
        
        
        // Set the current date as the default in JDateChooser
        txtDate.setDate(new Date());
        
        lb.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");
        
        // Add listener to jTable to populate text fields when a row is selected
            jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting() && jTable.getSelectedRow() != -1) {
                        int selectedRow = jTable.getSelectedRow();
                        String supp = jTable.getValueAt(selectedRow, 1).toString();
                        ComboSupplier.setSelectedItem(supp);
                        txttTitle.setText(jTable.getValueAt(selectedRow, 2).toString());
                        String author = jTable.getValueAt(selectedRow, 3).toString();
                        ComboAuthor.setSelectedItem(author);
                        txtAmount.setText(jTable.getValueAt(selectedRow, 4).toString());
                        String genre = jTable.getValueAt(selectedRow, 5).toString();
                        ComboGenre.setSelectedItem(genre);
                        
                        // Convert the string to a Date object and set it in JDateChooser
                        try {
                            String dateString = jTable.getValueAt(selectedRow, 6).toString();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  // Assuming your date format is "yyyy-MM-dd"
                            Date date = dateFormat.parse(dateString);  // Parse the date string into a Date object
                            txtDate.setDate(date);  // Set the date in JDateChooser
                        } catch (ParseException ex) {
                            ex.printStackTrace();  // Handle parsing errors
                        }
                        txtImportPrice.setText(jTable.getValueAt(selectedRow, 7).toString());
                        txtQuantity.setText(jTable.getValueAt(selectedRow, 8).toString());
           
                        txtPhoto.setText(jTable.getValueAt(selectedRow, 9).toString());
                      
                        ImageIcon icon = resizeImage(txtPhoto.getText(), label_photo.getWidth(), label_photo.getHeight());
                        if (icon != null) {
                            label_photo.setIcon(icon);
                        } else {
                            label_photo.setIcon(null);
                        }
                    }
                }
            }   
        );
            
                // Add Key Listeners to Quantity and Import Price fields for auto-calculation
            txtQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    calculateAmount();
                }
            });

            txtImportPrice.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    calculateAmount();
                }
            });
            
             // Add window listener to refresh data when window is activated
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent e) {
                    loadDataFromDB();
                    loadSupplier();
                    loadAuthor();
                    loadGenre();
                }
            });
    }
    
    private void loadDataFromDB() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement != null ? statement.executeQuery(
                "SELECT b.book_id, s.company_name AS Supplier_Name, b.title, " +
                "CONCAT(a.first_name, ' ', a.last_name) AS Author_Name, " + 
                "i.total_cost, g.genre_name, i.import_date, id.import_price, id.qty_import, b.image " +
                "FROM tblbooks b " +
                "LEFT JOIN tblbooks_genres bg ON b.book_id = bg.book_id " +
                "LEFT JOIN tblgenres g ON bg.genre_id = g.genre_id " +
                "LEFT JOIN tblbooks_authors ba ON b.book_id = ba.book_id " +
                "LEFT JOIN tblauthors a ON ba.author_id = a.author_id " +
                "LEFT JOIN tblimports_details id ON b.book_id = id.book_id " +
                "LEFT JOIN tblimports i ON id.import_id = i.import_id " +
                "LEFT JOIN tblsuppliers s ON i.supplier_id = s.supplier_id;"
            ): null) {

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
                jTable.repaint();
            }

            jTable.setModel(tableModel);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadSupplier() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM tblsuppliers")) {

            ComboSupplier.removeAllItems();  // Clear existing items
            while (resultSet.next()) {
                ComboSupplier.addItem(resultSet.getString("company_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadAuthor() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT CONCAT(first_name,' ',last_name) As author_name FROM tblauthors;")) {

            ComboAuthor.removeAllItems(); 
            while (resultSet.next()) {
                ComboAuthor.addItem(resultSet.getString("author_name"));
            }
            
                    // Add "Unknown" as the last item
            ComboAuthor.addItem("Unknown");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadGenre() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM tblgenres")) {

             ComboGenre.removeAllItems(); 
            while (resultSet.next()) {
                ComboGenre.addItem(resultSet.getString("genre_name"));
            }
                    // Add "Unknown" as the last item
            ComboGenre.addItem("Unknown");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } 
    // Method to calculate the amount based on quantity and import price
private void calculateAmount() {
    try {
        int quantity = Integer.parseInt(txtQuantity.getText());
        double importPrice = Double.parseDouble(txtImportPrice.getText());
        double amount = quantity * importPrice;
        txtAmount.setText(String.valueOf(amount));
    } catch (NumberFormatException e) {
        // Handle invalid input
        txtAmount.setText("0");
    }
}
    // Helper method to insert author for a book
private void insertAuthorForBook(Connection connection, int bookId, String authorName) throws SQLException {
    String getAuthorIdQuery = "SELECT author_id FROM tblauthors WHERE CONCAT(first_name, ' ', last_name) = ?";
    PreparedStatement psGetAuthorId = connection.prepareStatement(getAuthorIdQuery);
    psGetAuthorId.setString(1, authorName);
    ResultSet rsAuthor = psGetAuthorId.executeQuery();
    if (rsAuthor.next()) {
        int authorId = rsAuthor.getInt("author_id");
        String insertAuthorQuery = "INSERT INTO tblbooks_authors (book_id, author_id) VALUES (?, ?)";
        PreparedStatement psAuthor = connection.prepareStatement(insertAuthorQuery);
        psAuthor.setInt(1, bookId);
        psAuthor.setInt(2, authorId);
        psAuthor.executeUpdate();
    }
}

// Helper method to insert genre for a book
private void insertGenreForBook(Connection connection, int bookId, String genreName) throws SQLException {
    String getGenreIdQuery = "SELECT genre_id FROM tblgenres WHERE genre_name = ?";
    PreparedStatement psGetGenreId = connection.prepareStatement(getGenreIdQuery);
    psGetGenreId.setString(1, genreName);
    ResultSet rsGenre = psGetGenreId.executeQuery();
    if (rsGenre.next()) {
        int genreId = rsGenre.getInt("genre_id");
        String insertGenreQuery = "INSERT INTO tblbooks_genres (book_id, genre_id) VALUES (?, ?)";
        PreparedStatement psGenre = connection.prepareStatement(insertGenreQuery);
        psGenre.setInt(1, bookId);
        psGenre.setInt(2, genreId);
        psGenre.executeUpdate();
    }
}

// Helper method to get supplier ID
private int getSupplierId(Connection connection, String supplierName) throws SQLException {
    String getSupplierIdQuery = "SELECT supplier_id FROM tblsuppliers WHERE company_name = ?";
    PreparedStatement psGetSupplierId = connection.prepareStatement(getSupplierIdQuery);
    psGetSupplierId.setString(1, supplierName);
    ResultSet rsSupplier = psGetSupplierId.executeQuery();
    if (rsSupplier.next()) {
        return rsSupplier.getInt("supplier_id");
    } else {
        throw new SQLException("Supplier not found");
    }
}

// Helper method to insert expense
private void insertExpense(Connection connection, String expenseType, java.sql.Date expenseDate, double amount, String description) throws SQLException {
    String insertExpenseQuery = "INSERT INTO tblexpenses (expense_type, expense_date, amount, description) VALUES (?, ?, ?, ?)";
    PreparedStatement psExpense = connection.prepareStatement(insertExpenseQuery);
    psExpense.setString(1, expenseType);
    psExpense.setDate(2, expenseDate);
    psExpense.setDouble(3, amount);
    psExpense.setString(4, description);
    psExpense.executeUpdate();
}
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        label_photo = new javax.swing.JLabel();
        btnBrowse = new javax.swing.JButton();
        txtPhoto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txttTitle = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        ComboSupplier = new javax.swing.JComboBox<>();
        ComboAuthor = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        ComboGenre = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtImportPrice = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtDate = new com.toedter.calendar.JDateChooser();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\Inventory-maintenance_25374.png")); // NOI18N
        lb.setText("Import Stock");

        jTable.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable);

        label_photo.setBackground(new java.awt.Color(204, 204, 204));
        label_photo.setForeground(new java.awt.Color(204, 204, 204));
        label_photo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_photo.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image\\Add_Image_icon.png")); // NOI18N
        label_photo.setBorder(javax.swing.BorderFactory.createTitledBorder("Photo"));
        label_photo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        label_photo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_photoMouseClicked(evt);
            }
        });

        btnBrowse.setBackground(new java.awt.Color(204, 204, 204));
        btnBrowse.setFont(new java.awt.Font("Gotham Thin", 3, 11)); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        txtPhoto.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel5.setText("Photo");

        jLabel6.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Supplier");

        jLabel7.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Title");

        txttTitle.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Author");

        ComboSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        ComboAuthor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Genre");

        ComboGenre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Amount");

        txtAmount.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        txtQuantity.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Quantity");

        txtImportPrice.setFont(new java.awt.Font("Gotham", 0, 12)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Import Pice");

        jLabel13.setFont(new java.awt.Font("Gotham Black", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Import Date");

        btnInsert.setBackground(new java.awt.Color(255, 255, 255));
        btnInsert.setFont(new java.awt.Font("Gotham Ultra", 0, 12)); // NOI18N
        btnInsert.setForeground(new java.awt.Color(255, 255, 255));
        btnInsert.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\add_icon.png")); // NOI18N
        btnInsert.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(51, 153, 255));
        btnUpdate.setFont(new java.awt.Font("Gotham Ultra", 0, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\icon\\png\\systemsoftwareupdate_94333.png")); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(label_photo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txttTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtImportPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(ComboGenre, 0, 169, Short.MAX_VALUE)
                                            .addComponent(ComboSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ComboAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 34, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(17, 17, 17)))))
                .addContainerGap())
            .addComponent(lb, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lb)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txttTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtImportPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ComboAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ComboGenre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ComboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)))
                        .addGap(6, 6, 6))
                    .addComponent(label_photo, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBrowse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPhoto)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void label_photoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_photoMouseClicked
        // TODO add your handling code here
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image"));                                                           
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtPhoto.setText(selectedFile.getAbsolutePath());

            ImageIcon icon = resizeImage(selectedFile.getAbsolutePath(), label_photo.getWidth(), label_photo.getHeight());
            if (icon != null) {
                label_photo.setIcon(icon);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load image", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_label_photoMouseClicked

        private ImageIcon resizeImage(String imagePath, int width, int height) {
            try {
                BufferedImage originalImage = ImageIO.read(new File(imagePath));
                Image resizedImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                return new ImageIcon(resizedImage);
            } catch (IOException e) {
                return null;
        }
}
    
    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File("D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\product_image"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtPhoto.setText(selectedFile.getAbsolutePath());

            ImageIcon icon = resizeImage(selectedFile.getAbsolutePath(), label_photo.getWidth(), label_photo.getHeight());
            if (icon != null) {
                label_photo.setIcon(icon);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load image", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
                                         
    // Get data from form fields
    String title = txttTitle.getText();
    String supplier = ComboSupplier.getSelectedItem().toString();
    String author1 = ComboAuthor.getSelectedItem().toString();
    String genre1 = ComboGenre.getSelectedItem().toString();
    String amount = txtAmount.getText();
    String quantity = txtQuantity.getText();
    String importPrice = txtImportPrice.getText();
    String imagePath = txtPhoto.getText(); // Image path from file chooser
    java.util.Date importDateUtil = txtDate.getDate();

    if (importDateUtil == null) {
        JOptionPane.showMessageDialog(this, "Please select a date", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    java.sql.Date importDate = new java.sql.Date(importDateUtil.getTime());

    // Validate input
    if (title.isEmpty() || supplier.isEmpty() || author1.isEmpty() || genre1.isEmpty() || 
        amount.isEmpty() || quantity.isEmpty() || importPrice.isEmpty() || imagePath.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try (Connection connection = DatabaseConnection.getConnection()) {
        connection.setAutoCommit(false); // Start transaction

        try {
            // Step 1: Insert into tblbooks
            String insertBookQuery = "INSERT INTO tblbooks (title, image) VALUES (?, ?)";
            PreparedStatement psBook = connection.prepareStatement(insertBookQuery, Statement.RETURN_GENERATED_KEYS);
            psBook.setString(1, title);
            psBook.setString(2, imagePath);
            psBook.executeUpdate();

            // Get the generated book_id
            ResultSet generatedKeys = psBook.getGeneratedKeys();
            if (generatedKeys.next()) {
                int bookId = generatedKeys.getInt(1);

                // Step 2: Insert authors into tblbooks_authors
                insertAuthorForBook(connection, bookId, author1);

                // Step 3: Insert genres into tblbooks_genres
                insertGenreForBook(connection, bookId, genre1);

                // Step 4: Insert into tblimports
                int supplierId = getSupplierId(connection, supplier);
                String insertImportQuery = "INSERT INTO tblimports (supplier_id, import_date, total_cost) VALUES (?, ?, ?)";
                PreparedStatement psImport = connection.prepareStatement(insertImportQuery, Statement.RETURN_GENERATED_KEYS);
                psImport.setInt(1, supplierId);
                psImport.setDate(2, importDate);
                psImport.setDouble(3, Double.parseDouble(amount));
                psImport.executeUpdate();

                // Get generated import_id
                ResultSet generatedImportKeys = psImport.getGeneratedKeys();
                if (generatedImportKeys.next()) {
                    int importId = generatedImportKeys.getInt(1);

                    // Step 5: Insert into tblimports_details
                    String insertImportDetailsQuery = "INSERT INTO tblimports_details (import_id, book_id, qty_import, import_price) VALUES (?, ?, ?, ?)";
                    PreparedStatement psImportDetails = connection.prepareStatement(insertImportDetailsQuery);
                    psImportDetails.setInt(1, importId);
                    psImportDetails.setInt(2, bookId);
                    psImportDetails.setInt(3, Integer.parseInt(quantity));
                    psImportDetails.setDouble(4, Double.parseDouble(importPrice));
                    psImportDetails.executeUpdate();

                    // Step 6: Insert expense record into tblexpense
                    String expenseType = "Inventory Expenses";
                    String description = "Supplier: " + supplier + ", Title: " + title;
                    double expenseAmount = Double.parseDouble(amount);
                    insertExpense(connection, expenseType, importDate, expenseAmount, description);
                }
            }

            connection.commit(); // Commit transaction
            JOptionPane.showMessageDialog(this, "Book with authors and genres added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadDataFromDB();
            clearFormFields();

        } catch (SQLException e) {
            connection.rollback(); // Rollback transaction if error occurs
            JOptionPane.showMessageDialog(this, "Failed to insert book: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}                                         

// Helper method to clear the form fields
private void clearFormFields() {
    txttTitle.setText("");
    txtAmount.setText("");
    txtQuantity.setText("");
    txtImportPrice.setText("");
    txtPhoto.setText("");
    label_photo.setIcon(null);
    ComboAuthor.setSelectedIndex(0);
    ComboGenre.setSelectedIndex(0);
    ComboSupplier.setSelectedIndex(0);
    txtDate.setDate(null);


    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
                                         
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String title = txttTitle.getText();
        String supplier = ComboSupplier.getSelectedItem().toString();
        String author = ComboAuthor.getSelectedItem().toString();
        String genre = ComboGenre.getSelectedItem().toString();
        String amount = txtAmount.getText();
        String quantity = txtQuantity.getText();
        String importPrice = txtImportPrice.getText();
        String imagePath = txtPhoto.getText();
        java.util.Date importDateUtil = txtDate.getDate();
        java.sql.Date importDate = new java.sql.Date(importDateUtil.getTime());

        // Validate input
        if (title.isEmpty() || supplier.isEmpty() || author.isEmpty() || genre.isEmpty() || 
            amount.isEmpty() || quantity.isEmpty() || importPrice.isEmpty() || imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the selected book_id from the table (assuming the book_id is in column 0)
        int bookId = (int) jTable.getValueAt(selectedRow, 0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Update the book record
            String updateBookQuery = "UPDATE tblbooks SET title = ?, image = ? WHERE book_id = ?";
            PreparedStatement psBook = connection.prepareStatement(updateBookQuery);
            psBook.setString(1, title);
            psBook.setString(2, imagePath);
            psBook.setInt(3, bookId);

            int rowsUpdated = psBook.executeUpdate();

            if (rowsUpdated > 0) {
                // Update author, genre, and supplier (assuming author, genre, and supplier names are unique)
                String updateAuthorQuery = "UPDATE tblbooks_authors SET author_id = (SELECT author_id FROM tblauthors WHERE CONCAT(first_name, ' ', last_name) = ?) WHERE book_id = ?";
                PreparedStatement psAuthor = connection.prepareStatement(updateAuthorQuery);
                psAuthor.setString(1, author);
                psAuthor.setInt(2, bookId);
                psAuthor.executeUpdate();

                String updateGenreQuery = "UPDATE tblbooks_genres SET genre_id = (SELECT genre_id FROM tblgenres WHERE genre_name = ?) WHERE book_id = ?";
                PreparedStatement psGenre = connection.prepareStatement(updateGenreQuery);
                psGenre.setString(1, genre);
                psGenre.setInt(2, bookId);
                psGenre.executeUpdate();

                String updateImportQuery = "UPDATE tblimports SET supplier_id = (SELECT supplier_id FROM tblsuppliers WHERE company_name = ?), import_date = ?, total_cost = ? WHERE import_id = (SELECT import_id FROM tblimports_details WHERE book_id = ?)";
                PreparedStatement psImport = connection.prepareStatement(updateImportQuery);
                psImport.setString(1, supplier);
                psImport.setDate(2, importDate);
                psImport.setDouble(3, Double.parseDouble(amount));
                psImport.setInt(4, bookId);
                psImport.executeUpdate();
                
                String updateImportDetailsQuery = "UPDATE tblimports_details SET import_price = ?, qty_import = ? WHERE book_id = ?";
                PreparedStatement psImportDetails = connection.prepareStatement(updateImportDetailsQuery);
                psImportDetails.setDouble(1, Double.parseDouble(importPrice));
                psImportDetails.setInt(2, Integer.parseInt(quantity));
                psImportDetails.setInt(3, bookId);
                psImportDetails.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDataFromDB();  // Refresh table
                clearFormFields();  // Clear the form fields
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update book", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
      // TODO add your handling code here:                                         
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the selected book_id from the table (assuming the book_id is in column 0)
        int bookId = (int) jTable.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                // Delete from tblimports_details
                String deleteImportDetailsQuery = "DELETE FROM tblimports_details WHERE book_id = ?";
                PreparedStatement psImportDetails = connection.prepareStatement(deleteImportDetailsQuery);
                psImportDetails.setInt(1, bookId);
                psImportDetails.executeUpdate();

                // Delete from tblbooks_authors
                String deleteAuthorQuery = "DELETE FROM tblbooks_authors WHERE book_id = ?";
                PreparedStatement psAuthor = connection.prepareStatement(deleteAuthorQuery);
                psAuthor.setInt(1, bookId);
                psAuthor.executeUpdate();

                // Delete from tblbooks_genres
                String deleteGenreQuery = "DELETE FROM tblbooks_genres WHERE book_id = ?";
                PreparedStatement psGenre = connection.prepareStatement(deleteGenreQuery);
                psGenre.setInt(1, bookId);
                psGenre.executeUpdate();

                // Delete from tblbooks
                String deleteBookQuery = "DELETE FROM tblbooks WHERE book_id = ?";
                PreparedStatement psBook = connection.prepareStatement(deleteBookQuery);
                psBook.setInt(1, bookId);

                int rowsDeleted = psBook.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadDataFromDB();  // Refresh table
                    clearFormFields();  // Clear form fields after deletion
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete book", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }                                         

    }//GEN-LAST:event_btnDeleteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboAuthor;
    private javax.swing.JComboBox<String> ComboGenre;
    private javax.swing.JComboBox<String> ComboSupplier;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JLabel label_photo;
    private javax.swing.JLabel lb;
    private javax.swing.JTextField txtAmount;
    private com.toedter.calendar.JDateChooser txtDate;
    private javax.swing.JTextField txtImportPrice;
    private javax.swing.JTextField txtPhoto;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txttTitle;
    // End of variables declaration//GEN-END:variables
}
