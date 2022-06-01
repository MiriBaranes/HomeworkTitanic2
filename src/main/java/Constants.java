import java.awt.*;
import java.util.List;

public class Constants {
    public static final String PATH_TO_DATA_FILE = "src/main/resources/titanic.csv";
    public static final int NUMBER_OF_DIGITS_AFTER_FLOATING_POINT = 3;
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final String[] PASSENGER_CLASS_OPTIONS = {"All", "1", "2", "3"};
    public static final String[] PASSENGER_SEX_OPTIONS = {"All", "Male", "Female"};
    public static final String[] PASSENGER_EMBARKED_PLACE = {"All", "S", "Q", "C"};
    public static final String[][] COMBO_LIST_OPTION = {PASSENGER_CLASS_OPTIONS, PASSENGER_SEX_OPTIONS, PASSENGER_EMBARKED_PLACE};
    public static final int LABEL_WIDTH = 200;
    public static final int LABEL_HEIGHT = 30;
    public static final int COMBO_BOX_WIDTH = 80;
    public static final int COMBO_BOX_HEIGHT = 30;
    public static final int BUTTON_W = 200;
    public static final int BUTTON_H = 50;
    public static final int PASSENGER_TYPE = 1;
    public static final int STATISTICS_TYPE = 2;
    public static final int CLASS_FROM_USER1 = 0;
    public static final int GENDER_FROM_USER1 = 1;
    public static final int EMBARKATION_FROM_USER1 = 2;
    public static final int NAME_FROM_USER1 = 7;
    public static final int SIBLINGS_FROM_USER1 = 3;
    public static final int PARCH_FROM_USER1 = 4;
    public static final int TICKET_FROM_USER1 = 5;
    public static final int CABIN_FROM_USER1 = 6;
    public static final int ID_MIN_FROM_USER1 = 8;
    public static final int FARE_MIN_FROM_USER1 = 10;
    public static final int ID_MAX_FROM_USER1 = 9;
    public static final int FARE_MAX_FROM_USER1 = 11;
}
