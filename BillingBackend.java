import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BillingBackend {
    private ArrayList<Item> items;

    public BillingBackend() {
        items = new ArrayList<>();
    }

    public void addItem(String name, int price, int quantity) {
        if (quantity > 0) {
            items.add(new Item(name, price, quantity));
        }
    }

    public String generateBill() {
        StringBuilder bill = new StringBuilder();
        int subtotal = 0;

        bill.append("         🌶️ Spice Corner\n");
        bill.append("     123 Flavor Street, Nilgiris\n");
        bill.append("----------------------------------------\n");
        bill.append(String.format("%-18s %3s %6s %7s\n", "Item", "Qty", "Rate", "Amount"));
        bill.append("----------------------------------------\n");

        for (Item item : items) {
            subtotal += item.getCost();
            bill.append(String.format("%-18s x%-2d  ₹%-5d ₹%-5d\n",
                    item.getName(), item.getQuantity(), item.getPrice(), item.getCost()));
        }

        double gst = subtotal * 0.05;
        double discount = subtotal * 0.10;
        double finalTotal = subtotal + gst - discount;

        bill.append("----------------------------------------\n");
        bill.append(String.format("Subtotal:%28s₹%.2f\n", "", (double) subtotal));
        bill.append(String.format("GST (5%%):%27s₹%.2f\n", "", gst));
        bill.append(String.format("Discount (10%%):%21s₹%.2f\n", "", discount));
        bill.append(String.format("Total:%31s₹%.2f\n", "", finalTotal));
        bill.append("----------------------------------------\n");
        bill.append("Bringing Taste to Life – Thank You! 🍴");

        return bill.toString();
    }

    public void saveBillToFile(String billText) throws IOException {
        FileWriter fw = new FileWriter("SpiceCornerBill.txt");
        fw.write(billText);
        fw.close();
    }

    public void clearItems() {
        items.clear();
    }
}
