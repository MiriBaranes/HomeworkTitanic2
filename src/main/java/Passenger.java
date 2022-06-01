import java.util.*;

public class Passenger implements Comparable<Passenger> {
    private static final int PASSENGER_ID = 0;
    private static final int SURVIVED = 1;
    private static final int P_CLASS = 2;
    private static final int SURNAME = 3;
    private static final int NAME = 4;
    private static final int GENDER = 5;
    private static final int AGE = 6;
    private static final int SIB_SP = 7;
    private static final int PARCH = 8;
    private static final int TICKET = 9;
    private static final int FARE = 10;
    private static final int CABIN = 11;
    private static final int EMBARKED = 12;

    public static final Integer MALE = 0;
    public static final Integer FEMALE = 1;
    public static final Integer UN_KNOW = null;
    public static final Integer THERE_IS_RELATIVES = 1;
    public static final Integer THERE_IS_NO_RELATIVES = 0;


    private final Boolean _survived;
    private final Integer _pClass;
    private final Integer _gender;
    private final Integer _sibSp;
    private final Integer _parch;
    private final String _ticket;
    private final String _cabin;
    private final Double _fare;
    private String _embarked;
    private final String _formatName;
    private final Integer passengerId;
    private final String name;
    private final Double age;

    public Passenger(String line) {
        String[] split = line.split(",");
        this.passengerId = Integer.parseInt(split[PASSENGER_ID]);
        this._survived = split[SURVIVED].equals("1");
        _pClass = makeStringToBeAInteger(split[P_CLASS]);
        name = split[SURNAME] + split[NAME];
        _gender = initGender(split[GENDER]);
        age = makeStringToBeDouble(split[AGE]);
        _sibSp = makeStringToBeAInteger(split[SIB_SP]);
        _parch = makeStringToBeAInteger(split[PARCH]);
        _ticket = split[TICKET];
        _cabin = split[CABIN];
        _fare = makeStringToBeDouble(split[FARE]);
        _formatName =getFormatName();
        _embarked = null;
        if (split.length > 12) {
            _embarked = split[EMBARKED];
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passenger)) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(passengerId, passenger.passengerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passengerId);
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public Boolean getSurvived() {
        return _survived;
    }

    public Integer getpClass() {
        return _pClass;
    }

    public Integer getGender() {
        return _gender;
    }

    public Double getAge() {
        return age;
    }

    public Integer getSibSp() {
        return _sibSp;
    }

    public Integer getParch() {
        return _parch;
    }

    public String getTicket() {
        return _ticket;
    }

    public String getCabin() {
        return _cabin;
    }

    public Double getFare() {
        return _fare;
    }

    public String getEmbarked() {
        return _embarked;
    }

    public Integer thereIsRelatives() {
        int result = THERE_IS_NO_RELATIVES;
        if (getParch() > 0 || getSibSp() > 0) {
            result = THERE_IS_RELATIVES;
        }
        return result;
    }

    public String getFormatName() {
        int index = this.name.indexOf(".");
        int start = this.name.indexOf(" ");
        String result = name.substring(index + 1, name.length() - 1) + " " + name.substring(1, start);
        if (result.contains("(") || result.equals(")")) {
            result = result.replace("(", "");
            result = result.replace(")", "");
        }
        return result;
    }

    public static Integer initGender(String gender) {
        Integer gender_ = UN_KNOW;
        gender = gender.toLowerCase(Locale.ROOT);
        if (gender.equals("male")) {
            gender_ = MALE;
        } else if (gender.equals("female")) {
            gender_ = FEMALE;
        }
        return gender_;
    }

    public Integer makeStringToBeAInteger(String s) {
        Integer result = null;
        try {
            result = Integer.parseInt(s);
        } catch (ArithmeticException | NumberFormatException ignored) {
        }
        return result;
    }

    public Double makeStringToBeDouble(String s) {
        Double result = null;
        try {
            result = Double.parseDouble(s);
        } catch (ArithmeticException | NumberFormatException ignored) {
        }
        return result;
    }

    public String line() {
        return passengerId + "," + _survived + "," + _pClass + "," + getFormatName() + "," + _gender + "," + age + "," + _sibSp + "," + _parch + "," + _ticket + "," + _cabin
                + "," + _fare + "," + _embarked;
    }

    @Override
    public String toString() {
        Integer survived = 0;
        if (this._survived) {
            survived = SURVIVED;
        }
        String gender = null;
        if (MALE.equals(this._gender)) {
            gender = "male";
        } else if (FEMALE.equals(this._gender)) {
            gender = "female";
        }
        return passengerId + "," + survived + "," + _pClass + "," + getFormatName() + "," + gender + "," + age + "," + _sibSp + "," + _parch + "," + _ticket + "," + _cabin
                + "," + _fare + "," + _embarked + "\n";
    }

    @Override
    public int compareTo(Passenger o) {
        return this.getFormatName().compareTo(o.getFormatName());
    }
}
