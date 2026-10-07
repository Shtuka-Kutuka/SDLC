import controller.ProteinController;
import model.ProteinModel;
import view.InputWindow;
import view.MainWindow;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProteinModel model = new ProteinModel();

            ProteinController controller = new ProteinController(model);

            MainWindow mainWindow = new MainWindow(controller);
            InputWindow inputWindow = new InputWindow(controller);

            controller.setMainWindow(mainWindow);
            controller.setInputWindow(inputWindow);

            model.addObserver(mainWindow);
            model.addObserver(inputWindow);

            mainWindow.showWindow();
        });
    }
}