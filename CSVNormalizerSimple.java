real-file

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


// Solution by soshal yadav
public class CSVNormalizerSimple {

    public static void standardizeStatement(String inputFile, String outputFile) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    parts[0] = formatDate(parts[0]);
                    parts[2] = formatAmount(parts[2]);
                }

                data.add(parts);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        writeCSV(outputFile, data);
    }

    private static String formatDate(String date) {
        String[] formats = {"dd-MM-yyyy", "MM-dd-yyyy", "dd-MM-yy"};
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (String f : formats) {
            try {
                SimpleDateFormat oldFormat = new SimpleDateFormat(f);
                Date d = oldFormat.parse(date);
                return newFormat.format(d);
            } catch (ParseException ignored) {}
        }
        return date;
    }

    private static String formatAmount(String amount) {
        try {
            return String.format("%.2f", Double.parseDouble(amount.replaceAll("[^0-9.]", "")));
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }

    private static void writeCSV(String outputFile, List<String[]> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public static void processFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null) {
            for (File file : files) {
                if (file.getName().contains("-Input-")) {
                    String outputFile = folderPath + File.separator + file.getName().replace("-Input-", "-Output-");
                    standardizeStatement(file.getAbsolutePath(), outputFile);
                    System.out.println("Done: " + file.getName());
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("enter file path");
        String folder = scan.nextLine();
        scan.close();

        processFolder(folder);
        System.out.println("All files processed!");
    }
}
