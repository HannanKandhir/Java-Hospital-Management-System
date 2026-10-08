package hospital.storage;

import hospital.HospitalSystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataStorage {

    public static void save(HospitalSystem system, String fileName) {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))) {
            output.writeObject(system);
            System.out.println("\nSerialization Output: " + fileName + " saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static HospitalSystem load(String fileName) {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))) {
            System.out.println("Data loaded successfully from " + fileName);
            return (HospitalSystem) input.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("File not found. Starting new hospital system.");
            return new HospitalSystem();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return new HospitalSystem();
        }
    }

    public static void exportTextFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
            System.out.println(fileName + " exported successfully.");
        } catch (IOException e) {
            System.out.println("Error exporting text file: " + e.getMessage());
        }
    }

    public static void appendTextFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(content);
            writer.write("\n");
            System.out.println("Data added to " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing text file: " + e.getMessage());
        }
    }
}