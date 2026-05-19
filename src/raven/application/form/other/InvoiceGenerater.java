/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raven.application.form.other;

import java.io.File;
import net.sf.jasperreports.engine.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import raven.application.form.other.BookDetail;

/**
 *
 * @author 萧修枫
 */
public class InvoiceGenerater {
    

public void generateInvoice(int orderId, String staffName, Date orderDate, String paymentMethod, double subtotal, double vat, double totalAmount, double changeAmount, List<BookDetail> bookDetails) {
    try {
        // Path to .jrxml file
        String jrxmlPath = "src/raven/application/form/other/Invoice.jrxml";

        // Compile the report
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlPath);

        // Set parameters for the report
        Map<String, Object> parameters = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#.00");
        double roundedVat = Double.parseDouble(df.format(vat)); 
        
        parameters.put("InvoiceID", orderId);
        parameters.put("StaffName", staffName);
        parameters.put("InvoiceDate", orderDate);
        parameters.put("PaymentMethod", paymentMethod);
        parameters.put("Subtotal", subtotal);
        parameters.put("VAT", roundedVat);
        parameters.put("TotalAmount", totalAmount);
        parameters.put("ChangeAmount", changeAmount);

        // Use JRBeanCollectionDataSource to pass the list of BookDetail to the report
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(bookDetails);

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Define the output directory and save the PDF
        String outputDir = "D:\\Class ST6\\NetBeansProjects\\Test_BockStore\\src\\raven\\invoiceGenerated";
        new File(outputDir).mkdirs();
        String pdfFileName = outputDir + "\\Invoice_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

        // Export the report to a PDF file
        JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFileName);

        System.out.println("Invoice report saved as PDF at: " + pdfFileName);

    } catch (JRException e) {
        e.printStackTrace();
        System.out.println("Failed to generate the invoice report.");
    }
}
    
}
