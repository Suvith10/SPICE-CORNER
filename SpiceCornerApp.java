import java.awt.*;
import javax.swing.*;

public class SpiceCornerApp extends JFrame {

    String[] itemNames = {"Pizza", "Burger", "Coke", "Fries", "Pasta", "Sandwich", "Ice Cream", "Noodles", "Coffee"};
    int[] itemPrices = {150, 100, 40, 60, 120, 80, 90, 110, 50};
    String[] itemImages = {
            "images/pizza.png", "images/burger.png", "images/coke.png",
            "images/fries.png", "images/pasta.png", "images/sandwich.png",
            "images/icecream.png", "images/noodles.png", "images/coffee.png"
    };

    JCheckBox[] checkBoxes;
    JTextField[] qtyFields;
    JTextArea resultArea;
    JTextField totalBox;
    BillingBackend backend = new BillingBackend();

    public SpiceCornerApp() {
        // --- Frame Setup ---
        setTitle("🌶️ Spice Corner Billing System");
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 245, 240));

        // --- Header ---
        JLabel header = new JLabel("🌶️ Spice Corner Billing System", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setOpaque(true);
        header.setBackground(new Color(180, 40, 40));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(header, BorderLayout.NORTH);

        // --- Food Panel (Left with Grid Layout) ---
        JPanel foodGrid = new JPanel(new GridLayout(3, 3, 20, 20));
        foodGrid.setBackground(new Color(250, 245, 240));
        foodGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        checkBoxes = new JCheckBox[itemNames.length];
        qtyFields = new JTextField[itemNames.length];

        for (int i = 0; i < itemNames.length; i++) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            
            // Food Image
            ImageIcon icon = new ImageIcon(itemImages[i]);
            Image scaledImg = icon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
            imgLabel.setHorizontalAlignment(JLabel.CENTER);
            card.add(imgLabel, BorderLayout.NORTH);

            // Item Name + Price
            JLabel namePrice = new JLabel(itemNames[i] + " - ₹" + itemPrices[i], JLabel.CENTER);
            namePrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
            card.add(namePrice, BorderLayout.CENTER);

            // Quantity + Checkbox
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            bottomPanel.setBackground(Color.WHITE);
            qtyFields[i] = new JTextField("1", 3);
            checkBoxes[i] = new JCheckBox("Select");

            bottomPanel.add(new JLabel("Qty:"));
            bottomPanel.add(qtyFields[i]);
            bottomPanel.add(checkBoxes[i]);
            card.add(bottomPanel, BorderLayout.SOUTH);

            foodGrid.add(card);
        }

        JScrollPane foodScroll = new JScrollPane(foodGrid);
        foodScroll.setPreferredSize(new Dimension(500, 0));
        add(foodScroll, BorderLayout.WEST);

        // --- Bill Panel (Right) ---
        JPanel billPanel = new JPanel(new BorderLayout());
        billPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel billLabel = new JLabel("🧾 Bill");
        billLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        billPanel.add(billLabel, BorderLayout.NORTH);

        resultArea = new JTextArea(20, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        billPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        add(billPanel, BorderLayout.CENTER);

        // --- Footer ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        footer.setBackground(new Color(250, 245, 240));

        JButton checkoutBtn = new JButton("Checkout");
        checkoutBtn.setBackground(new Color(34, 139, 34));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton saveBtn = new JButton("Save Bill");
        saveBtn.setBackground(new Color(30, 144, 255));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        totalBox = new JTextField(10);
        totalBox.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalBox.setEditable(false);

        footer.add(checkoutBtn);
        footer.add(saveBtn);
        footer.add(new JLabel("Total:"));
        footer.add(totalBox);

        add(footer, BorderLayout.SOUTH);

        // --- Event Handling ---
        checkoutBtn.addActionListener(e -> generateBill());
        saveBtn.addActionListener(e -> saveBill());
    }

    private void generateBill() {
        backend.clearItems();
        for (int i = 0; i < itemNames.length; i++) {
            if (checkBoxes[i].isSelected()) {
                try {
                    int qty = Integer.parseInt(qtyFields[i].getText().trim());
                    backend.addItem(itemNames[i], itemPrices[i], qty);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity for " + itemNames[i]);
                }
            }
        }
        String bill = backend.generateBill();
        resultArea.setText(bill);

        // extract final total from bill
        String[] lines = bill.split("\n");
        String lastLine = lines[lines.length - 2];
        totalBox.setText(lastLine.replaceAll("[^0-9.]", ""));
    }

    private void saveBill() {
        try {
            backend.saveBillToFile(resultArea.getText());
            JOptionPane.showMessageDialog(this, "Bill saved as SpiceCornerBill.txt");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving bill: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SpiceCornerApp().setVisible(true));
    }
}
