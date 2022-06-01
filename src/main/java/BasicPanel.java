import javax.swing.*;
import java.awt.*;

public class BasicPanel extends JPanel {
    private ImageIcon backGround;
    private JLabel title;

    public BasicPanel(int x, int y, int w, int h, Color color, String title) {
        this.setBounds(x, y, w, h);
        this.setBackground(color);
        this.backGround = null;
        init();
    }

    public JLabel getTitle() {
        return this.title;
    }

    public BasicPanel(int x, int y, int w, int h, String fieldName, String title) {
        this.setBounds(x, y, w, h);
        this.title = addJLabel(title, 0, 0, this.getWidth(), 100, 50, Color.blue.brighter());
        this.title.setOpaque(true);
        this.backGround = new ImageIcon(fieldName);
        init();
    }

    public BasicPanel(int x, int y, int w, int h, String fieldName) {
        this.setBounds(x, y, w, h);
        this.backGround = new ImageIcon(fieldName);
        init();
    }

    public void init() {
        this.setLayout(null);
        this.setDoubleBuffered(true);
        this.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.backGround != null) {
            this.backGround.paintIcon(this, g, 0, 100);
        }
    }

    public JLabel addJLabel(String title, int x, int y, int w, int h, int size, Color color) {
        JLabel jLabel = new JLabel(title, SwingConstants.CENTER);
        jLabel.setFont(new Font("ariel", Font.BOLD, size));
        jLabel.setForeground(color);
        jLabel.setBounds(x, y, w, h);
        this.add(jLabel);
        return jLabel;
    }

    public void setBackGround(String path) {
        if (this.backGround != null) {
            backGround = new ImageIcon(path);
            repaint();
        }
    }

    public JLabel addLabelBelowAntherLabel(JLabel other, int w, int h, String string, int size) {
        return addJLabel(string, other.getX(), other.getY() + other.getHeight(), w, h, size, other.getForeground());
    }

    public JLabel addLabelNextAntherLabel(JLabel other, String string, int size) {
        return addJLabel(string, other.getX() + other.getWidth(), other.getY(), other.getWidth(), other.getHeight(), size, other
                .getForeground());
    }

    public JTextField addTextField(String title, int x, int y, int w, int h) {
        JTextField text = new JTextField(title);
        text.setBounds(x, y, w, h);
        this.add(text);
        return text;
    }

    public JTextField addTextFieldBelowAntherTextField(JTextField other, String string) {
        return addTextField(string, other.getX(), other.getY() + other.getHeight(), other.getWidth(), other.getHeight());
    }

    public JTextField addTextFieldNextAntherTextField(JTextField other, String string) {
        return addTextField(string, other.getX() + other.getWidth(), other.getY(), other.getWidth(), other.getHeight());
    }

    public JComboBox<String> addComBoxNextJLabel(String title, int y, String[] option) {
        JComboBox<String> jComboBox = new JComboBox<>(option);
        JLabel jLabel = addJLabel(title, 0, y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT, 10, Color.black);
        jComboBox.setBounds(jLabel.getX() + jLabel.getWidth() + 1, jLabel.getY(), Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
        this.add(jComboBox);
//        return new Object[]{jLabel, jComboBox};
        return jComboBox;
    }

    public JTextField[] minAndMaxTextField(String min, String max, int y) {
        JTextField minText = addJTextFieldByOneValueSearch(min, y);
        JLabel maxJLabel = addJLabel(max, minText.getX() + minText.getWidth() + 1, y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT, 10, Color.black);
        JTextField maxText = addTextField("", maxJLabel.getX() + maxJLabel.getWidth() + 1, y, Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
        return new JTextField[]{minText, maxText};
    }

    public JTextField addJTextFieldByOneValueSearch(String title, int y) {
        JLabel jLabel = addJLabel(title, 0, y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT, 10, Color.black);
        return addTextField("", jLabel.getX() + jLabel.getWidth() + 1, y, Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
    }
    public Button addButton(int x, int y, int w, int h,String title){
        Button button=new Button(title);
        button.setBounds(x,y,w,h);
        button.setBackground(Color.cyan);
        this.add(button);
        return button;
    }
}
