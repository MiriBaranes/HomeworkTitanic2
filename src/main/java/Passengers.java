import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Passengers implements Serializable {
    public static final PassengerFieldGetter[] EQUALS_FIELDS = {Passenger::getpClass,
            Passenger::getGender,
            Passenger::getEmbarked,
            Passenger::getSibSp,
            Passenger::getParch,
            Passenger::getTicket,
            Passenger::getCabin};
    public static final PassengerFieldGetter[] CONTAINS_FIELDS = {Passenger::getFormatName};
    public static final PassengerFieldGetter[] MIN_MAX_FIELD = {Passenger::getPassengerId, Passenger::getFare};
    private final File titanic;

    public Passengers() {
        this.titanic = new File(Constants.PATH_TO_DATA_FILE);
    }

    public Stream<Passenger> allPassengers() {

        String text;
        List<Passenger> passengers = new LinkedList<>();
        try {
            if (this.titanic.exists()) {
                Scanner scanner = new Scanner(new File(Constants.PATH_TO_DATA_FILE));
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
                while (scanner.hasNextLine()) {
                    text = scanner.nextLine();
                    passengers.add(new Passenger(text));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return passengers.stream();
    }

    private Stream<Passenger> filter_nulls(Stream<Passenger> copy, PassengerFieldGetter fieldGetter) {
        return copy.filter(passenger -> fieldGetter.getField(passenger) != null);
    }

    public Stream<Passenger> getPassengersByDoubleMaxAndMinFieldSearch(PassengerFieldGetter fieldGetter, Double min, Double max) {
        Stream<Passenger> copy = allPassengers();
        copy = filter_nulls(copy, fieldGetter);
        return copy.filter(passenger -> ((max == null || (Double) fieldGetter.getField(passenger) < max) && (min == null || (Double) fieldGetter.getField(passenger) >= min)));
    }


    public Stream<Passenger> filterArrayObjectWithFields(Stream<Passenger> workOn, Object[] objects, PassengerFieldGetter[] passengerFieldGetters, PassengersPred pred) {
        if (objects.length == passengerFieldGetters.length) {
            for (int i = 0; i < passengerFieldGetters.length; i++) {
                workOn = filterByPred(workOn, passengerFieldGetters[i], objects[i], pred);
            }
        }
        return workOn;
    }


    public Stream<Passenger> searchByAllParameters(Object[] equalsObject,
                                                   Object[] containObject,
                                                   Object[] minObject,
                                                   Object[] maxObject) {
        Stream<Passenger> result = allPassengers().distinct();
        result = filterArrayObjectWithFields(result, equalsObject, EQUALS_FIELDS, Passengers::predEquals);
        result = filterArrayObjectWithFields(result, containObject, CONTAINS_FIELDS, Passengers::predContains);
        result = filterArrayObjectWithFields(result, minObject, MIN_MAX_FIELD, Passengers::predGE);
        result = filterArrayObjectWithFields(result, maxObject, MIN_MAX_FIELD, Passengers::predLT);
        return result;
    }

    private double percentagesOf(int part, int whole) {
        double percents = part * StatisticsConstants.HUNDRED_PERCENT / whole;
        double tens = Math.pow(10, Constants.NUMBER_OF_DIGITS_AFTER_FLOATING_POINT);
        percents = Math.round(percents * tens) / tens;
        return percents;
    }

    private Stream<Passenger> filterByPred(Stream<Passenger> result, PassengerFieldGetter getter, Object value, PassengersPred predicate) {
        if (value == null)
            return result;
        return result.filter(passenger -> predicate.pred(passenger, getter, value));
    }

    private static boolean predEquals(Passenger passenger, PassengerFieldGetter getter, Object value) {
        Object fieldValue = getter.getField(passenger);
        if (value == null)
            return true;
        return value.equals(fieldValue);
    }

    private static boolean predLT(Passenger passenger, PassengerFieldGetter getter, Object value) {
        Object fieldValue = getter.getField(passenger);
        if (value == null)
            return true;
        return Double.parseDouble("" + fieldValue) < Double.parseDouble("" + value);
    }

    private static boolean predGE(Passenger passenger, PassengerFieldGetter getter, Object value) {
        Object fieldValue = getter.getField(passenger);
        if (value == null)
            return true;
        return Double.parseDouble("" + value) <= Double.parseDouble("" + fieldValue);
    }

    private static boolean predContains(Passenger passenger, PassengerFieldGetter getter, Object value) {
        Object fieldValue = getter.getField(passenger);
        if (value == null)
            return true;
        return ((String) (fieldValue)).contains((String) value);
    }

    public String getHowManySurvivedOrNotFromList(List<Passenger> list) {
        long survived = list.stream().filter(Passenger::getSurvived).count();
        long count = list.size();
        return "Total Rows: " + count + "(" + survived +
                " Survived ," + (count - survived) + " did not)";
    }


    public double getPercentageOfSurvivedByEquals(PassengerFieldGetter fieldGetter, Object value) {
        Stream<Passenger> passengers = allPassengers();
        passengers = filter_nulls(passengers, fieldGetter);
        passengers = filterByPred(passengers, fieldGetter, value, Passengers::predEquals);
        return survivedPercentages(passengers);
    }

    public double getPercentageOfSurvivedByMinAndMax(PassengerFieldGetter fieldGetter, Double min, Double max) {
        Stream<Passenger> passengers = getPassengersByDoubleMaxAndMinFieldSearch(fieldGetter, min, max);
        return survivedPercentages(passengers);
    }

    private double survivedPercentages(Stream<Passenger> passengers) {
        AtomicInteger all = new AtomicInteger(),
                byValue = new AtomicInteger();
        passengers.forEach(p -> {
            all.incrementAndGet();
            if (p.getSurvived()) {
                byValue.incrementAndGet();
            }
        });
        return percentagesOf(byValue.get(), all.get());
    }

    public String getPercentageOfSurvivedByPClass() {
        return getPercentageString(getPercentageOfSurvivedMap1(Passenger::getpClass, StatisticsConstants.P_CLASS), StatisticsConstants.P_CLASS_TYPE);
    }

    public String getPercentageOfSurvivedByAge() {
        return getPercentageString(getPercentageMinMaxOfSurvivedMap1(Passenger::getAge, StatisticsConstants.MIN_AGE, StatisticsConstants.MAX_AGE),
                StatisticsConstants.AGE_TYPE);
    }

    public String getPercentageOfSurvivedByGender() {
        return getPercentageString(getPercentageOfSurvivedMap1(Passenger::getGender, StatisticsConstants.GENDER), StatisticsConstants.GENDER_TYPE).replace(Passenger.MALE + "=", "Male=").replace(Passenger.FEMALE + "=", "Female=");
    }

    public TreeMap<Object, String> getPercentageOfSurvivedMap1(PassengerFieldGetter fieldGetter, Object[] keys) {
        TreeMap<Object, String> myMap = new TreeMap<>();
        for (Object key : keys) {
            double value = getPercentageOfSurvivedByEquals(fieldGetter, key);
            myMap.put(key, value + "%");
        }
        return myMap;
    }

    public TreeMap<Object, String> getPercentageMinMaxOfSurvivedMap1(PassengerFieldGetter fieldGetter, Object[] min, Object[] max) {
        TreeMap<Object, String> myMap = new TreeMap<>();
        IntStream.range(0, max.length).mapToObj(key -> new AbstractMap.SimpleEntry<Object, Object>(min[key] + "-" + max[key],
                getPercentageOfSurvivedByMinAndMax(fieldGetter,
                        (Double) min[key], (Double) max[key]))).forEach(e -> myMap.put(((AbstractMap.SimpleEntry<?, ?>) e).getKey() + " ", ((AbstractMap.SimpleEntry<?, ?>) e).getValue() + "%"));
        return myMap;
    }


    public String getPercentageOfSurvivedByFare() {
        return getPercentageString(getPercentageMinMaxOfSurvivedMap1(Passenger::getFare,
                StatisticsConstants.MIN_PRICE, StatisticsConstants.MAX_PRICE), StatisticsConstants.FARE_TYPE).replace("30.0-null", "30+");
    }

    public String getPercentageOfSurvivedByEmbarkation() {
        return getPercentageString(getPercentageOfSurvivedMap1(Passenger::getEmbarked,
                StatisticsConstants.EMBARKATION_PLACE), StatisticsConstants.EMBARKATION_TYPE);
    }


    public String getPercentageOfSurvivedByRelatives() {
        return getPercentageString(getPercentageOfSurvivedMap1(Passenger::thereIsRelatives, StatisticsConstants.RELATIVES), StatisticsConstants.RELATIVE_TYPE).replace(StatisticsConstants.RELATIVES[0] + "=", "No relative=").replace(StatisticsConstants.RELATIVES[1] + "=", "Has relative=");
    }

    public String getPercentageString(TreeMap<Object, String> map, String explanation) {
        return "\nStatistics by- " + explanation + "\n" + map.toString() + "\n";
    }

    public List<String> allStatisticSearch() {
        List<String> result = new LinkedList<>();
        result.add(getPercentageOfSurvivedByPClass());
        result.add(getPercentageOfSurvivedByAge());
        result.add(getPercentageOfSurvivedByGender());
        result.add(getPercentageOfSurvivedByFare());
        result.add(getPercentageOfSurvivedByEmbarkation());
        result.add(getPercentageOfSurvivedByRelatives());
        return result;
    }
}
