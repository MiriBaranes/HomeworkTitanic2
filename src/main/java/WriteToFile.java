import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class WriteToFile {
    private static final String NAME_CATEGORIES="List sort: ,Passenger id, Survived,pClass,name,gender,age,sibSp,parch,ticket,cabin,fare,embarked\n,";
    private static final String NAME_FILE = ".csv";
    private static final String PATH_PASSENGERS = "C:\\FILE\\1\\filter";
    private static final String PATH_STATISTICS = "C:\\FILE\\1\\Statistics.txt";
    private static final String PATH_NUMBER = "C:\\FILE\\1\\num.txt";
    private WriteToFile(){}
    public static void writeToFile(List<Object> list, int type) {
        if (type == Constants.PASSENGER_TYPE) {
            writeListToFile(list, PATH_PASSENGERS);
        } else {
            writeListToFile(list, PATH_STATISTICS);
        }
    }

    private static void writeListToFile(List<Object> toAdd, String path) {
        String add = "";
        String num_to_add = "";
        try {
            if (path.equals(PATH_PASSENGERS)) {
                add += NAME_CATEGORIES;
                int num = getNumbersOfSearch();
                num_to_add = num + "." + NAME_FILE;
            }
            FileWriter fileWriter = new FileWriter(path + num_to_add);
            String list = add + toAdd.toString().replace("[", "");
            list = list.replace("]", "");
            System.out.println(list);
            fileWriter.write(list);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Integer getNumbersOfSearch() throws IOException {
        String numFromFile = readeFromFile();
        int sum = 0;
        if (numFromFile != null) {
            sum = Integer.parseInt(numFromFile);
        }
        sum++;
        writeCountToFile(sum);
        return sum;
    }

    private static void writeCountToFile(int text) throws IOException {
        FileWriter fileWriter = new FileWriter(PATH_NUMBER);
        fileWriter.write("" + text);
        fileWriter.close();
    }

    private static String readeFromFile() throws FileNotFoundException {
        String text = null;
        File file = new File(PATH_NUMBER);
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine())
                text = scanner.nextLine();
            scanner.close();
        }
        return text;
    }
}
