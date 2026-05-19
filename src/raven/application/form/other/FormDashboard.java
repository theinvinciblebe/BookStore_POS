package raven.application.form.other;


import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.table.TableRowSorter;
import raven.application.DatabaseConnection;
import raven.toast.Notifications;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


/**
 *
 * @author Raven
 */
public class FormDashboard extends javax.swing.JPanel {
    private JLabel totalIncomeLabel;
    private JLabel monthlyIncomeLabel;
    private JLabel highestIncomeDayLabel;
    private JTable incomeTable;
    private DefaultTableModel incomeTableModel;
    private JTable breakdownTable;
    private DefaultTableModel breakdownTableModel;
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;



    public FormDashboard() {
        initComponents();
                
        lb.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");


        JPanel summaryPanel = createSummaryPanel();
        JPanel incomeTypePanel = createIncomeTypePanel(); //Breakdown
        JPanel incomeTablePanel = createIncomeTablePanel(); //Entries
        JPanel inventoryTablePanel = createInventoryTablePanel();

         //Add panels to the main layout
        setLayout(new BorderLayout());
        add(summaryPanel, BorderLayout.NORTH);
        add(incomeTypePanel, BorderLayout.CENTER);
        add(inventoryTablePanel, BorderLayout.EAST);
        add(incomeTablePanel, BorderLayout.SOUTH);
        
        summaryPanel.setPreferredSize(new Dimension(0, 100)); // Set height to 100 pixels
        incomeTypePanel.setPreferredSize(new Dimension(100, 0)); // Set width to 300 pixels for CENTER panel
        inventoryTablePanel.setPreferredSize(new Dimension(600, 0)); // Set width to 400 pixels for EAST panel
        incomeTablePanel.setPreferredSize(new Dimension(0, 350)); // Set height to 200 pixels for SOUTH panel

        loadIncomeData();
        loadInventoryData();
        loadIncomeBreakdown();
    }
    
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        totalIncomeLabel = new JLabel("Total Income: $0.00", JLabel.CENTER);
        totalIncomeLabel.setFont(new Font("Arial", Font.BOLD, 15));
        totalIncomeLabel.setHorizontalAlignment(JLabel.CENTER);
        
        monthlyIncomeLabel = new JLabel("Monthly Income: $0.00", JLabel.CENTER);
        monthlyIncomeLabel.setFont(new Font("Arial", Font.BOLD, 15));
        
        highestIncomeDayLabel = new JLabel("Highest Income Day: $0.00", JLabel.CENTER);
        highestIncomeDayLabel.setFont(new Font("Arial", Font.BOLD, 15));
        
        panel.add(createSummaryCard("Total Income", totalIncomeLabel));
        panel.add(createSummaryCard("Monthly Income", monthlyIncomeLabel));
        panel.add(createSummaryCard("Highest Income Day", highestIncomeDayLabel));
        
        return panel;
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createTitledBorder(title));
        card.add(valueLabel);
        return card;
    }

    private JPanel createIncomeTypePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Income Breakdown by Type"));

        String[] columnNames = {"Income Type", "Total Amount"};
        breakdownTableModel = new DefaultTableModel(columnNames, 0);
        breakdownTable = new JTable(breakdownTableModel);

        // Customize table header
        JTableHeader header = breakdownTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(Color.BLUE);

        JScrollPane scrollPane = new JScrollPane(breakdownTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createIncomeTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Date", "Type", "Amount", "Remarks"};
        incomeTableModel = new DefaultTableModel(columnNames, 0);
        incomeTable = new JTable(incomeTableModel);

        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(incomeTableModel);
        incomeTable.setRowSorter(sorter);

        // Customize table header
        JTableHeader header = incomeTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(Color.BLUE);

        JScrollPane scrollPane = new JScrollPane(incomeTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createTitledBorder("Income Entries"));

        return panel;
    }
    
    private JPanel createInventoryTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Inventory Overview"));

        // Define the column names
        String[] columnNames = {"Book Title", "Qty in Stock", "Qty on Sale", "Qty Sold"};
        inventoryTableModel = new DefaultTableModel(columnNames, 0);
        inventoryTable = new JTable(inventoryTableModel);

        // Customize the table header
        JTableHeader header = inventoryTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(Color.BLUE);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb = new javax.swing.JLabel();

        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb.setText("Dashboard");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, 1161, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb)
                .addContainerGap(615, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    private void loadIncomeData() {
        double totalIncome = 0.0;
        double monthlyIncome = 0.0;
        double highestIncome = 0.0;
        String highestIncomeDay = "";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT income_date, income_type, amount, remarks FROM tblincomes")) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String date = rs.getString("income_date");
                String type = rs.getString("income_type");
                double amount = rs.getDouble("amount");
                String remarks = rs.getString("remarks");

                totalIncome += amount;
                incomeTableModel.addRow(new Object[]{date, type, amount, remarks});

                // Calculate monthly income and highest income day (assuming all records are from the current month)
                monthlyIncome += amount;
                if (amount > highestIncome) {
                    highestIncome = amount;
                    highestIncomeDay = date;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        totalIncomeLabel.setText("Total Income: $" + String.format("%.2f", totalIncome));
        monthlyIncomeLabel.setText("Monthly Income: $" + String.format("%.2f", monthlyIncome));
        highestIncomeDayLabel.setText("Highest Income Day: $" + String.format("%.2f", highestIncome) + " on " + highestIncomeDay);
    }

    private void loadIncomeBreakdown() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT income_type, SUM(amount) AS total_amount FROM tblincomes GROUP BY income_type")) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("income_type");
                double totalAmount = rs.getDouble("total_amount");

                breakdownTableModel.addRow(new Object[]{type, totalAmount});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadInventoryData() {

    String query = "SELECT " +
                   "    b.book_id, " +
                   "    b.title, " +
                   "    COALESCE(total_imported, 0) AS qty_in_stock, " +
                   "    COALESCE(total_on_sale, 0) AS qty_on_sale, " +
                   "    COALESCE(total_sold, 0) AS qty_sold " +
                   "FROM " +
                   "    tblbooks b " +
                   "LEFT JOIN ( " +
                   "    SELECT book_id, SUM(qty_import) AS total_imported " +
                   "    FROM tblimports_details " +
                   "    GROUP BY book_id " +
                   ") AS imports ON b.book_id = imports.book_id " +
                   "LEFT JOIN ( " +
                   "    SELECT book_id, SUM(qty_sold) AS total_on_sale " +
                   "    FROM tblsales " +
                   "    GROUP BY book_id " +
                   ") AS sales ON b.book_id = sales.book_id " +
                   "LEFT JOIN ( " +
                   "    SELECT book_id, SUM(qty) AS total_sold " +
                   "    FROM tblorderdetails " +
                   "    GROUP BY book_id " +
                   ") AS orders ON b.book_id = orders.book_id;";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        // Clear any existing rows
        inventoryTableModel.setRowCount(0);

        // Populate data from the query
        while (rs.next()) {
            String title = rs.getString("title");
            int qtyInStock = rs.getInt("qty_in_stock");
            int qtyOnSale = rs.getInt("qty_on_sale");
            int qtySold = rs.getInt("qty_sold");

            inventoryTableModel.addRow(new Object[]{ title, qtyInStock, qtyOnSale, qtySold});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lb;
    // End of variables declaration//GEN-END:variables
}
