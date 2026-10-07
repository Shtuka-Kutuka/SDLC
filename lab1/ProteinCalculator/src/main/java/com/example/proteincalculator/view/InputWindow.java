package view;

import controller.ProteinController;
import model.ModelObserver;
import model.ProteinModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class InputWindow extends JDialog implements ModelObserver {

    private final ProteinController controller;

    private final JTextField weightField;
    private final JComboBox<String> unitComboBox;

    public InputWindow(ProteinController controller) {
        super(controllerWindow(controller),
            "Ввод данных",
            false);

        this.controller = controller;

        setSize(390, 250);
        setResizable(false);
        setLocationRelativeTo(getOwner());

        JPanelBuilder panelBuilder = new JPanelBuilder();

        JLabel weightLabel = new JLabel("Вес:");

        weightField = new JTextField(15);

        JLabel unitLabel = new JLabel("Единица:");

        unitComboBox = new JComboBox<>(
            new String[]{"кг", "фунты"}
        );

        JButton calculateButton = new JButton("Рассчитать");
        JButton cancelButton = new JButton("Отмена");

        GridBagConstraints constraints;

        constraints = panelBuilder.constraints(0, 0);
        panelBuilder.panel.add(weightLabel, constraints);

        constraints = panelBuilder.constraints(1, 0);
        panelBuilder.panel.add(weightField, constraints);

        constraints = panelBuilder.constraints(0, 1);
        panelBuilder.panel.add(unitLabel, constraints);

        constraints = panelBuilder.constraints(1, 1);
        panelBuilder.panel.add(unitComboBox, constraints);

        constraints = panelBuilder.constraints(0, 2);
        panelBuilder.panel.add(calculateButton, constraints);

        constraints = panelBuilder.constraints(1, 2);
        panelBuilder.panel.add(cancelButton, constraints);

        add(panelBuilder.panel);

        calculateButton.addActionListener(e -> saveAndCalculate());

        cancelButton.addActionListener(e -> setVisible(false));

        getRootPane().setDefaultButton(calculateButton);

        loadLastData();
    }

    private static java.awt.Frame controllerWindow(
        ProteinController controller
    ) {
        return null;
    }

    public void showWindow() {
        loadLastData();

        setLocationRelativeTo(getOwner());
        setVisible(true);

        weightField.requestFocusInWindow();
        weightField.selectAll();
    }

    private void loadLastData() {
        ProteinModel model = controller.getModel();

        if (!model.hasData()) {
            weightField.setText("");
            unitComboBox.setSelectedIndex(0);
            return;
        }

        DecimalFormat format = new DecimalFormat(
            "0.##",
            DecimalFormatSymbols.getInstance(Locale.US)
        );

        weightField.setText(
            format.format(model.getWeight())
        );

        unitComboBox.setSelectedIndex(
            model.isPounds() ? 1 : 0
        );
    }

    private void saveAndCalculate() {
        String value = weightField.getText().trim();

        if (value.isEmpty()) {
            controller.showError("Введите вес.");
            weightField.requestFocusInWindow();
            return;
        }

        double weight;

        try {
            value = value.replace(',', '.');
            weight = Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            controller.showError(
                "Введите корректное число.\n" +
                    "Например: 70 или 70.5"
            );

            weightField.requestFocusInWindow();
            weightField.selectAll();
            return;
        }

        if (!Double.isFinite(weight)) {
            controller.showError(
                "Введите корректное конечное значение веса."
            );

            weightField.requestFocusInWindow();
            weightField.selectAll();
            return;
        }

        if (weight <= 0.0) {
            controller.showError(
                "Вес должен быть больше нуля."
            );

            weightField.requestFocusInWindow();
            weightField.selectAll();
            return;
        }

        if (weight > 1000.0) {
            controller.showError(
                "Введите реалистичное значение веса (не более 1000)."
            );

            weightField.requestFocusInWindow();
            weightField.selectAll();
            return;
        }

        boolean pounds =
            unitComboBox.getSelectedIndex() == 1;

        controller.calculate(weight, pounds);

        setVisible(false);
    }

    @Override
    public void modelChanged() {
        loadLastData();
    }

    private static class JPanelBuilder {

        private final javax.swing.JPanel panel =
            new javax.swing.JPanel(new GridBagLayout());

        private JPanelBuilder() {
            panel.setBorder(
                BorderFactory.createEmptyBorder(
                    20, 20, 20, 20
                )
            );
        }

        private GridBagConstraints constraints(
            int column,
            int row
        ) {
            GridBagConstraints constraints =
                new GridBagConstraints();

            constraints.gridx = column;
            constraints.gridy = row;

            constraints.weightx = 1.0;
            constraints.fill = GridBagConstraints.HORIZONTAL;

            constraints.insets =
                new Insets(8, 8, 8, 8);

            if (column == 0) {
                constraints.weightx = 0.0;
            }

            return constraints;
        }
    }
}