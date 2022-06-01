import javax.swing.*;
import java.io.FileNotFoundException;

class Main extends JFrame {

    public Main() {
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.add(new MainPanel());
        this.setVisible(true);

    }
}