package view;

import controller.ProteinController;
import model.ModelObserver;
import model.ProteinModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class MainWindow extends JFrame implements ModelObserver {

    private final ProteinController controller;

    private final JLabel resultLabel;

    private final JButton inputButton;

    public MainWindow(ProteinController controller) {
        this.controller = controller;

        setTitle("Калькулятор суточной нормы протеина");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 330);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(8, 8, 8, 8);

        JLabel titleLabel = new JLabel(
            "Расчет суточной нормы употребления протеина",
            SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;

        mainPanel.add(titleLabel, constraints);

        inputButton = new JButton("Ввести данные");
        inputButton.setPreferredSize(new Dimension(190, 40));

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(20, 8, 15, 8);

        mainPanel.add(inputButton, constraints);

        resultLabel = new JLabel(
            "Данные еще не введены.",
            SwingConstants.LEFT
        );

        resultLabel.setFont(new Font("Dialog", Font.PLAIN, 14));

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10, 8, 8, 8);

        mainPanel.add(resultLabel, constraints);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        inputButton.addActionListener(e -> controller.openInputWindow());

        updateResult();
    }

    public void showWindow() {
        setVisible(true);
    }

    @Override
    public void modelChanged() {
        updateResult();
    }

    private void updateResult() {
        ProteinModel model = controller.getModel();

        if (!model.hasData()) {
            resultLabel.setText(
                "<html>Данные еще не введены.</html>"
            );
            return;
        }

        String unit = model.isPounds() ? "фунтов" : "кг";

        String text = String.format(
            "<html>" +
                "<b>Последние введенные данные:</b><br><br>" +
                "Вес: %.2f %s<br>" +
                "Вес в килограммах: %.2f кг<br><br>" +
                "<b>Суточная норма протеина: %.2f г/сутки</b>" +
                "</html>",
            model.getWeight(),
            unit,
            model.getWeightKg(),
            model.getProteinNorm()
        );

        resultLabel.setText(text);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Ошибка",
            JOptionPane.ERROR_MESSAGE
        );
    }
}