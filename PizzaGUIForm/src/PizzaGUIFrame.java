import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrustRB, regularCrustRB, deepDishCrustRB;
    private JComboBox<String> sizeComboBox;
    private JCheckBox[] toppingCheckBoxes;
    private JTextArea orderTextArea;
    private JButton orderButton, clearButton, quitButton;

    private final String[] SIZES = {"Small", "Medium", "Large", "Super"};
    private final double[] SIZE_PRICES = {8.00, 12.00, 16.00, 20.00};
    private final String[] TOPPINGS = {"Pepperoni", "Mushrooms", "Onions", "Sausage", "Bacon", "Extra Cheese"};
    private final double TOPPING_PRICE = 1.00;
    private final double TAX_RATE = 0.07;

    public PizzaGUIFrame() {
        setTitle("Silas's Pizza Order System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createPanels();
        addListeners();

        setVisible(true);
    }

    private void createPanels() {
        // Crust Panel
        JPanel crustPanel = new JPanel(new GridLayout(3, 1));
        crustPanel.setBorder(BorderFactory.createTitledBorder("Crust Type"));
        ButtonGroup crustGroup = new ButtonGroup();
        thinCrustRB = new JRadioButton("Thin");
        regularCrustRB = new JRadioButton("Regular");
        deepDishCrustRB = new JRadioButton("Deep-dish");
        crustGroup.add(thinCrustRB);
        crustGroup.add(regularCrustRB);
        crustGroup.add(deepDishCrustRB);
        crustPanel.add(thinCrustRB);
        crustPanel.add(regularCrustRB);
        crustPanel.add(deepDishCrustRB);

        // Size Panel
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createTitledBorder("Pizza Size"));
        sizeComboBox = new JComboBox<>(SIZES);
        sizeComboBox.setPreferredSize(new Dimension(100, 25));
        sizePanel.add(sizeComboBox);

        // Toppings Panel
        JPanel toppingsPanel = new JPanel(new GridLayout(3, 2));
        toppingsPanel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        toppingCheckBoxes = new JCheckBox[TOPPINGS.length];
        for (int i = 0; i < TOPPINGS.length; i++) {
            toppingCheckBoxes[i] = new JCheckBox(TOPPINGS[i]);
            toppingsPanel.add(toppingCheckBoxes[i]);
        }

        // Order Display Panel
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        orderTextArea = new JTextArea(10, 40);
        orderTextArea.setEditable(false);
        orderTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(orderTextArea);
        orderPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        // Main layout
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);
        topPanel.add(toppingsPanel);

        add(topPanel, BorderLayout.NORTH);
        add(orderPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        orderButton.addActionListener(e -> displayOrder());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void displayOrder() {
        if (!validateOrder()) {
            JOptionPane.showMessageDialog(this, "Please select a crust type, size, and at least one topping.", "Incomplete Order", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder order = new StringBuilder();
        double subtotal = 0.0;

        // Crust and Size
        String crust = thinCrustRB.isSelected() ? "Thin" : (regularCrustRB.isSelected() ? "Regular" : "Deep-dish");
        String size = (String) sizeComboBox.getSelectedItem();
        double sizePrice = SIZE_PRICES[sizeComboBox.getSelectedIndex()];
        subtotal += sizePrice;

        order.append("=========================================\n");
        order.append(String.format("%-30s $%6.2f\n", "Type of Crust & Size", sizePrice));
        order.append(String.format("%-30s\n", crust + " Crust, " + size));

        // Toppings
        order.append(String.format("%-30s $%6.2f\n", "Ingredient", TOPPING_PRICE));
        for (int i = 0; i < toppingCheckBoxes.length; i++) {
            if (toppingCheckBoxes[i].isSelected()) {
                order.append(String.format("%-30s\n", TOPPINGS[i]));
                subtotal += TOPPING_PRICE;
            }
        }

        // Calculations
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        // Format and display the order
        order.append("\n");
        order.append(String.format("%-30s $%6.2f\n", "Sub-total:", subtotal));
        order.append(String.format("%-30s $%6.2f\n", "Tax:", tax));
        order.append("---------------------------------------------\n");
        order.append(String.format("%-30s $%6.2f\n", "Total:", total));
        order.append("=========================================\n");

        orderTextArea.setText(order.toString());
    }

    private boolean validateOrder() {
        boolean crustSelected = thinCrustRB.isSelected() || regularCrustRB.isSelected() || deepDishCrustRB.isSelected();
        boolean toppingSelected = false;
        for (JCheckBox cb : toppingCheckBoxes) {
            if (cb.isSelected()) {
                toppingSelected = true;
                break;
            }
        }
        return crustSelected && sizeComboBox.getSelectedIndex() != -1 && toppingSelected;
    }

    private void clearForm() {
        thinCrustRB.setSelected(false);
        regularCrustRB.setSelected(false);
        deepDishCrustRB.setSelected(false);
        sizeComboBox.setSelectedIndex(0);
        for (JCheckBox cb : toppingCheckBoxes) {
            cb.setSelected(false);
        }
        orderTextArea.setText("");
    }
}