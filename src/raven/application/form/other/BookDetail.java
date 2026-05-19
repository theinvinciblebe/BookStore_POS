    package raven.application.form.other;

/**
 *
 * @author 萧修枫
 */
public class BookDetail {
    private String bookTitle;
    private int quantity;
    private double subTotal;

    public BookDetail(String bookTitle, int quantity, double subTotal) {
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public String getBookTitle() { return bookTitle; }
    public int getQuantity() { return quantity; }
    public double getSubTotal() { return subTotal; }
    
}
