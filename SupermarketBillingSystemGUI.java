import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

// Class representing a product
class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}

// Class representing a shopping cart
class ShoppingCart {
    private List<Product> items;
    private double discount;

    public ShoppingCart() {
        items = new ArrayList<>();
        discount = 0.0;
    }

    public void addItem(Product product) {
        items.add(product);
    }

    public void removeItem(int index) {
        items.remove(index);
    }

    public double calculateTotal() {
        double total = 0;
        for (Product item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        total -= discount;
        return total;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getCartSize() {
        return items.size();
    }

    public Product getItem(int i) {
        return items.get(i);
    }

    public void saveInvoiceToFile() {
        try (FileWriter writer = new FileWriter("invoice.txt")) {
            writer.write("----------- INVOICE -----------\n");
            for (int i = 0; i < items.size(); i++) {
                Product item = items.get(i);
                writer.write((i + 1) + ". " + item.getName() + "\t$" + item.getPrice() + "\tQty: " + item.getQuantity() + "\n");
            }
            writer.write("-------------------------------\n");
            writer.write("Total: $" + calculateTotal() + "\n");
            writer.write("Discount: $" + discount + "\n");
            writer.write("-------------------------------\n");
            JOptionPane.showMessageDialog(null, "Invoice saved to file.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save invoice to file: " + e.getMessage());
        }
    }
}

// Main class representing the Supermarket Billing System using Swing
public class SupermarketBillingSystemGUI extends JFrame {
    private ShoppingCart cart = new ShoppingCart();
    private JTextArea cartArea;
    private JTextField nameField, priceField, quantityField, discountField;

    public SupermarketBillingSystemGUI() {
        setTitle("Supermarket Billing System");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Product Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        // Create the cart area
        cartArea = new JTextArea();
        cartArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(cartArea);

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1)); // 7 buttons in total

        JButton addButton = new JButton("Add Item");
        buttonPanel.add(addButton);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addItemToCart();
            }
        });

        JButton removeButton = new JButton("Remove Item");
        buttonPanel.add(removeButton);
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeItemFromCart();
            }
        });

        JButton viewButton = new JButton("View Cart");
        buttonPanel.add(viewButton);
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewCart();
            }
        });

        buttonPanel.add(new JLabel("Discount:"));
        discountField = new JTextField();
        buttonPanel.add(discountField);

        JButton applyDiscountButton = new JButton("Apply Discount");
        buttonPanel.add(applyDiscountButton);
        applyDiscountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyDiscount();
            }
        });

        JButton generateInvoiceButton = new JButton("Generate Invoice");
        buttonPanel.add(generateInvoiceButton);
        generateInvoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateInvoice();
            }
        });

        JButton downloadInvoiceButton = new JButton("Download Invoice");
        buttonPanel.add(downloadInvoiceButton);
        downloadInvoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                downloadInvoice();
            }
        });

        // Add panels to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addItemToCart() {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        Product product = new Product(name, price, quantity);
        cart.addItem(product);
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");

        JOptionPane.showMessageDialog(this, "Item added to cart!");
    }

    private void removeItemFromCart() {
        int index = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter item number to remove:"));
        if (index >= 1 && index <= cart.getCartSize()) {
            cart.removeItem(index - 1);
            JOptionPane.showMessageDialog(this, "Item removed from cart!");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid item number.");
        }
    }

    private void viewCart() {
        if (cart.getCartSize() > 0) {
            cartArea.setText("------ Cart Items ------\n");
            for (int i = 0; i < cart.getCartSize(); i++) {
                Product item = cart.getItem(i);
                cartArea.append((i + 1) + ". " + item.getName() + "\t$" + item.getPrice() + "\tQty: " + item.getQuantity() + "\n");
            }
            cartArea.append("------------------------\n");
            cartArea.append("Total: $" + cart.calculateTotal() + "\n");
            cartArea.append("------------------------\n");
        } else {
            cartArea.setText("Cart is empty.");
        }
    }

    private void applyDiscount() {
        double discount = Double.parseDouble(discountField.getText());
        cart.setDiscount(discount);
        JOptionPane.showMessageDialog(this, "Discount applied!");
    }

    private void generateInvoice() {
        if (cart.getCartSize() > 0) {
            viewCart();
            cart = new ShoppingCart(); // Clear the cart after generating the invoice
        } else {
            JOptionPane.showMessageDialog(this, "Cart is empty. Cannot generate invoice.");
        }
    }

    private void downloadInvoice() {
        if (cart.getCartSize() > 0) {
            cart.saveInvoiceToFile();
            cart = new ShoppingCart(); // Clear the cart after downloading the invoice
        } else {
            JOptionPane.showMessageDialog(this, "Cart is empty. Cannot download invoice.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SupermarketBillingSystemGUI().setVisible(true);
            }
        });
    }
}
