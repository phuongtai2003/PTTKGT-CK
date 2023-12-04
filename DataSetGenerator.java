import java.util.Random;
import java.util.stream.IntStream;

public class DataSetGenerator {
    private static final Random random = new Random();

    // Parameters for the continuous and discrete whale optimization algorithms
    private static final int DIMENSION = 30; // Number of dimensions or features
    private static final int UPPER_BOUND = 100; // Upper bound for generating continuous values
    private static final int LOWER_BOUND = -100; // Lower bound for generating continuous values

    // Parameters for the knapsack problems
    private static final int ITEM_COUNT = 100; // Number of items for knapsack problems
    private static final int MAX_WEIGHT = 50; // Max weight for knapsack items
    private static final int MAX_VALUE = 100; // Max value for knapsack items

    public static void main(String[] args) {
        // Generate data for WOA (continuous space)
        double[] continuousData = IntStream.range(0, DIMENSION)
                .mapToDouble(i -> LOWER_BOUND + random.nextDouble() * (UPPER_BOUND - LOWER_BOUND))
                .toArray();

        // Generate data for DWOA (discrete space)
        int[] discreteData = IntStream.range(0, DIMENSION)
                .map(i -> random.nextInt(2)) // For binary decision variables
                .toArray();

        // Generate items for the knapsack problems with weights and values
        int[] weights = IntStream.range(0, ITEM_COUNT)
                .map(i -> random.nextInt(MAX_WEIGHT) + 1)
                .toArray();
        int[] values = IntStream.range(0, ITEM_COUNT)
                .map(i -> random.nextInt(MAX_VALUE) + 1)
                .toArray();

        // Output the generated data
        System.out.println("Continuous Data for WOA:");
        printArray(continuousData);

        System.out.println("\nDiscrete Data for DWOA:");
        printArray(discreteData);

        System.out.println("\nKnapsack Items (Weight, Value):");
        for (int i = 0; i < ITEM_COUNT; i++) {
            System.out.println("(" + weights[i] + ", " + values[i] + ")");
        }
    }

    private static void printArray(double[] array) {
        for (double v : array) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

    private static void printArray(int[] array) {
        for (int v : array) {
            System.out.print(v + " ");
        }
        System.out.println();
    }
}
