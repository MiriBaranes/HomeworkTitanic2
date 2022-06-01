import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainPanel extends BasicPanel {
    private static final String[] EQUALS_SEARCH = {"class --->",
            "sex ---> "
            , "embarkation --->"
            , "siblings' number --->"
            , "parch number --->"
            , "ticket number --->"
            , "cabin number --->"};
    private static final int BOX_SIZE = 3;
    private static final String BY_CONTAINS = "name --->";
    private static final String[] MIX_MAX_SEARCH = {"id min number --->", "id max number --->", "ticket price min --->", "ticket price max --->"};
    public static final String FIELD_NAME="waves-730260985.webp";
    public static final String TITLE="Survived passenger's search:";
    private final ArrayList<JComboBox<String>> box;
    private final ArrayList<JTextField> textFromUser;
    private Button search;
    private final Object[] select;
    private final Passengers passengers;
    private JLabel messageToUser;

    public MainPanel() {
        super(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, FIELD_NAME,TITLE );
        this.box = new ArrayList<>();
        this.textFromUser = new ArrayList<>();
        this.select = new Object[EQUALS_SEARCH.length + MIX_MAX_SEARCH.length + 1];
        initAllCoboBox();
        this.passengers = new Passengers();
    }

    public void initMyCoboBox(int y, String[] option, String text) {
        JComboBox<String> toAdd = (this.addComBoxNextJLabel(text, y
                , option));
        box.add(toAdd);
        select[box.indexOf(toAdd)] = null;
        toAdd.addActionListener((e) -> {
            this.select[box.indexOf(toAdd)] = validValueCoboBox(EQUALS_SEARCH[box.indexOf(toAdd)], Objects.requireNonNull(toAdd.getSelectedItem()));
        });
    }

    public void initAllCoboBox() {
        int y = this.getTitle().getY() + this.getTitle().getHeight();
        for (int i = 0; i < BOX_SIZE; i++) {
            initMyCoboBox(y, Constants.COMBO_LIST_OPTION[i], EQUALS_SEARCH[i]);
            y += Constants.LABEL_HEIGHT + 1;
        }
        for (int i = BOX_SIZE; i < EQUALS_SEARCH.length; i++) {
            initTextField(EQUALS_SEARCH[i], y);
            y += Constants.LABEL_HEIGHT + 1;
        }
        initTextField(BY_CONTAINS, y);
        y += Constants.LABEL_HEIGHT + 1;
        initTextFieldByMinAndMax(y);
    }

    public void actTextListener(int index, JTextField jTextField) {
        if (index >= BOX_SIZE) {
            if (jTextField.getText().equals(""))
                this.select[index] = null;
            else
                this.select[index] = jTextField.getText();
        }
        System.out.println(index + " -  " + jTextField.getText().length());
    }

    public void initTextField(String keys, int y) {
        JTextField toAdd = this.addJTextFieldByOneValueSearch(keys, y);
        textFromUser.add(toAdd);
    }

    public void initTextFieldMinAndMax(String min, String max, int y) {
        JTextField[] toAdd = this.minAndMaxTextField(min, max, y);
        textFromUser.add(toAdd[0]);
        textFromUser.add(toAdd[1]);
    }

    public void initTextFieldByMinAndMax(int y) {
        for (int i = 0; i < MIX_MAX_SEARCH.length; i += 2) {
            initTextFieldMinAndMax(MIX_MAX_SEARCH[i], MIX_MAX_SEARCH[i + 1], y);
            y += Constants.LABEL_HEIGHT + 1;
        }
        searchButton(y);
        this.messageToUser = addJLabel("no message yet", 0, y + search.getHeight() + 1, Constants.WINDOW_WIDTH, Constants.LABEL_HEIGHT, 15, Color.black);
        this.messageToUser.setOpaque(true);
    }


    public Object validValueCoboBox(String keys, Object value) {
        if (value.equals("All")) {
            value = null;
        } else if (keys.equals(EQUALS_SEARCH[Constants.GENDER_FROM_USER1])) {
            value = Passenger.initGender((String) value);
        }
        return value;
    }

    public void searchButton(int y) {
        this.search = addButton(0, y, Constants.WINDOW_WIDTH, Constants.BUTTON_H, "For Search Click!");
        this.search.addActionListener(e -> {
            for (int i = BOX_SIZE, j = 0; i < EQUALS_SEARCH.length + 1 + MIX_MAX_SEARCH.length; i++, j++) {
                actTextListener(i, this.textFromUser.get(j));
            }
            String[] finalObject = new String[select.length];
            for (int i = 0; i < select.length; i++) {
                if (select[i] == null) {
                    finalObject[i] = null;
                } else {
                    finalObject[i] = String.valueOf(select[i]);
                }
            }
            Stream<Passenger> list = passengers.searchByAllParameters(
                    new Object[]{
                            makeStringToBeAInteger(finalObject[Constants.CLASS_FROM_USER1]),
                            makeStringToBeAInteger(finalObject[Constants.GENDER_FROM_USER1]),
                            (finalObject[Constants.EMBARKATION_FROM_USER1]),
                            makeStringToBeAInteger(finalObject[Constants.SIBLINGS_FROM_USER1]),
                            makeStringToBeAInteger(finalObject[Constants.PARCH_FROM_USER1]),
                            finalObject[Constants.TICKET_FROM_USER1],
                            finalObject[Constants.CABIN_FROM_USER1]},
                    new Object[]{finalObject[Constants.NAME_FROM_USER1]},
                    new Object[]{makeStringToBeAInteger(finalObject[Constants.ID_MIN_FROM_USER1]),
                            makeStringToBeDouble(finalObject[Constants.FARE_MIN_FROM_USER1])},
                    new Object[]{makeStringToBeAInteger(finalObject[Constants.ID_MAX_FROM_USER1]),
                            makeStringToBeDouble(finalObject[Constants.FARE_MAX_FROM_USER1])}
            ).sorted();
            List<Passenger> list2 = list.collect(Collectors.toList());
            this.messageToUser.setText(passengers.getHowManySurvivedOrNotFromList(list2));
            repaint();
            WriteToFile.writeToFile(Collections.singletonList(list2), Constants.PASSENGER_TYPE);
        });
        statisticButton();
    }

    public void statisticButton() {
        Button statistics = addButton(0, search.getY() + search.getHeight() + Constants.LABEL_HEIGHT + 2, Constants.BUTTON_W, Constants.BUTTON_H, "Statistic, click for make file.");
        statistics.addActionListener(e -> {
            WriteToFile.writeToFile(Collections.singletonList(passengers.allStatisticSearch()), Constants.STATISTICS_TYPE);
        });
    }

    public Integer makeStringToBeAInteger(Object s) {
        Integer result = null;
        if (s != null) {
            try {
                result = Integer.parseInt((String) s);
            } catch (ArithmeticException | NumberFormatException ignored) {
            }
        }
        return result;
    }

    public Double makeStringToBeDouble(Object s) {
        Double result = null;
        if (s != null) {
            try {
                result = Double.parseDouble((String) s);
            } catch (ArithmeticException | NullPointerException | NumberFormatException ignored) {
            }
        }
        return result;
    }


    public static void main(String[] args) {
        Main main = new Main();
    }

}