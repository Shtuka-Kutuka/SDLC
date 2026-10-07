package controller;

import model.ProteinModel;
import view.InputWindow;
import view.MainWindow;

public class ProteinController {

    private final ProteinModel model;

    private MainWindow mainWindow;
    private InputWindow inputWindow;

    public ProteinController(ProteinModel model) {
        this.model = model;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setInputWindow(InputWindow inputWindow) {
        this.inputWindow = inputWindow;
    }

    public void openInputWindow() {
        if (inputWindow != null) {
            inputWindow.showWindow();
        }
    }

    public void calculate(double weight, boolean pounds) {
        model.setData(weight, pounds);
    }

    public void showError(String message) {
        if (mainWindow != null) {
            mainWindow.showError(message);
        }
    }

    public boolean hasData() {
        return model.hasData();
    }

    public ProteinModel getModel() {
        return model;
    }
}