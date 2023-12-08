import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;

public class WOA {
    private static final Random random = new Random();

    public static double fitnessFunction(double[] whale){
        double sum = 0;
        for (double j : whale) {
            sum += j * j;
        }
        return sum;
    }
    public static double[][] generateWhale(int N,int ub, int lb, int dim) {
        double[][] whale = new double[N][dim];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < dim; j++) {
                whale[i][j] = (Math.random() * (ub - lb) + lb);
            }
        }
        return whale;
    }
    public static void whaleOptimizationAlgorithm(int N, int ub, int lb, int dim, int maxIteration) {
        double[][] whale = generateWhale(N,ub,lb,dim);
        double[] fitness = new double[N];
        for (int i = 0; i < N; i++) {
            fitness[i] = fitnessFunction(whale[i]);
        }
        double bestWhale = fitness[0];
        double[] bestWhalePosition = Arrays.copyOf(whale[0], whale[0].length);
        for (int i = 1; i < N; i++) {
            if (fitness[i] < bestWhale) {
                bestWhalePosition = Arrays.copyOf(whale[i], whale[i].length);
                bestWhale = fitness[i];
            }
        }
        int t = 0;
        while (t < maxIteration){
            double a = 2 * (1 - (double) t / maxIteration);
            for (int i  = 0; i < N; i++){
                double r = random.nextDouble();
                double A = r * 2 * a - a;
                double C = 2 * r;
                double p = random.nextDouble();
                double b = 0.5;
                double l = (random.nextDouble() * 2) - 1;
                if(p < 0.5){
                    if(Math.abs(A) < 1){
                        //Search for prey updating position (6)
                        for (int j = 0; j < dim; j++) {
                            whale[i][j] = bestWhalePosition[j] - A * Math.abs(C * bestWhalePosition[j] - whale[i][j]);
                        }
                    }
                    else if(Math.abs(A) >= 1){
                        //Encircling prey updating position (13)
                        int rand = random.nextInt(N);
                        while (rand == i){
                            rand = random.nextInt(N);
                        }
                        for (int j = 0; j < dim; j++) {
                            whale[i][j] = whale[rand][j] - A * Math.abs(C * whale[rand][j] - whale[i][j]);
                        }
                    }
                }
                else {
                    //Spiral updating position (10)
                    for (int j = 0; j < dim; j++) {
                        whale[i][j] = Math.abs(bestWhalePosition[j] - whale[i][j]) * Math.exp(b * l) * Math.cos(2 * Math.PI * l) + bestWhalePosition[j];
                    }
                }
                //Clipping if out of bounds of lb and ub
                for (int j = 0; j < dim; j++) {
                    if (whale[i][j] > ub) {
                        whale[i][j] = ub;
                    }
                    if (whale[i][j] < lb) {
                        whale[i][j] = lb;
                    }
                }
                fitness[i] = fitnessFunction(whale[i]);
                if (fitness[i] < bestWhale) {
                    bestWhalePosition = Arrays.copyOf(whale[i], whale[i].length);
                    bestWhale = fitness[i];
                }
            }
            t++;
        }
        System.out.println("Best whale: " + bestWhale);
        System.out.println("Best whale position: " + Arrays.toString(bestWhalePosition));
    }

    public static void main(String[] args) {
        int minDim = 10; // Minimum dimension to start with
        int maxDim = 100; // Maximum dimension to test
        int step = 10; // Step size to increase the dimension in each iteration
        int N = 50; // Number of whales
        int ub = 5; // Upper bound for the whale generation
        int lb = -5; // Lower bound for the whale generation
        int maxIteration = 100; // Maximum number of iterations

        ArrayList<Integer> dimensionsTested = new ArrayList<>();
        ArrayList<Long> executionTimes = new ArrayList<>();

        for (int dim = minDim; dim <= maxDim; dim += step) {
            System.out.println("Running Whale Optimization Algorithm with dimension = " + dim);
            long startTime = System.currentTimeMillis();

            whaleOptimizationAlgorithm(N, ub, lb, dim, maxIteration);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Execution time for dimension " + dim + ": " + duration + "ms");

            dimensionsTested.add(dim);
            executionTimes.add(duration);
        }

        System.out.println("Tested dimensions: " + dimensionsTested);
        System.out.println("Execution times: " + executionTimes);
    }
}