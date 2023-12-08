import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class KnapsackDWOA {
    private final static int maxCapacity = 800;
    private static final Random random = new Random();

    public static int[] GROA(int[] solution, int[] weight, Integer[] indices) {
        int currentWeight = 0;

        for (int i = 0; i < solution.length; i++) {
            currentWeight= currentWeight + solution[i] * weight[i];
        }

        for (int index: indices){
            if(currentWeight > maxCapacity && solution[index] == 1){
                solution[index] = 0;
                currentWeight = currentWeight - weight[index];
            }
        }

        for (int index: indices){
            if(currentWeight + weight[index] <= maxCapacity && solution[index] == 0){
                solution[index] = 1;
                currentWeight = currentWeight + weight[index];
            }
        }
        return solution;
    }

    public static int knapsackValue(int[] items, int[] values, int[] weights) {
        int totalValue = 0;
        int totalWeight = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] == 1) {
                totalValue += values[i];
                totalWeight += weights[i];
            }
        }
        if (totalWeight > maxCapacity) {
            return 0;
        }
        return totalValue;
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
    public static double vNew(double x) {
        return Math.abs(Math.exp(x) - 1) / (Math.exp(x) + 1);
    }
    public static int[] phi(double[] X, int[] m) {
        int n = X.length;
        int[] Y = new int[n];
        boolean allOne = Arrays.stream(m).allMatch(value -> value == 1);

        if (allOne) {
            double r = Math.random();
            for (int i = 0; i < n; i++) {
                Y[i] = vNew(X[i]) <= r ? 0 : 1;
            }
        } else {
            double[] r = new double[n - 1];
            for (int i = 0; i < n - 1; i++) {
                r[i] = Math.random();
            }
            Arrays.sort(r);

            for (int i = 0; i < n; i++) {
                double VnewX = vNew(X[i]);
                if (VnewX < r[0]) {
                    Y[i] = m[i];
                } else if (VnewX >= r[n - 2]) {
                    Y[i] = 0;
                } else {
                    for (int j = 1; j < n - 1; j++) {
                        if (VnewX >= r[j - 1] && VnewX < r[j]) {
                            Y[i] = m[i] - j;
                            break;
                        }
                    }
                }
            }
        }
        return Y;
    }

    public static void discreteWhaleOptimizationAlgorithm(int N, int ub, int lb, int dim, int maxIteration, int[] weight, int[] value) {
        Integer[] indices = new Integer[dim];
        for (int i = 0; i < dim; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                double density1 = (double) value[i1] / weight[i1];
                double density2 = (double) value[i2] / weight[i2];
                return Double.compare(density2, density1);
            }
        });


        double [][] whale = generateWhale(N,ub,lb,dim);
        int[][] phiWhale = new int[N][dim];
        int[] m = new int[dim];
        Arrays.fill(m, 1);
        for (int i = 0; i < N; i++) {
            phiWhale[i] = phi(whale[i], m);
            phiWhale[i] = GROA(phiWhale[i], weight, indices);
        }

        int[] fitness = new int[N];
        for (int i = 0; i < N; i++) {
            fitness[i] = knapsackValue(phiWhale[i], value, weight);
        }
        int bestWhale = fitness[0];
        int[] bestWhalePosition = Arrays.copyOf(phiWhale[0], phiWhale[0].length);
        double[] bestWhalePositionX = Arrays.copyOf(whale[0], whale[0].length);
        for (int i = 1; i < N; i++) {
            if (fitness[i] > bestWhale) {
                bestWhalePosition = Arrays.copyOf(phiWhale[i], phiWhale[i].length);
                bestWhale = fitness[i];
                bestWhalePositionX = Arrays.copyOf(whale[i], whale[i].length);
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
                if (p < 0.5){
                    if (Math.abs(A) < 1){
                        //Search for prey updating position (6)
                        for (int j = 0; j < dim; j++) {
                            whale[i][j] = bestWhalePositionX[j] - A * Math.abs(C * bestWhalePositionX[j] - whale[i][j]);
                        }
                    }
                    else if (Math.abs(A) >= 1){
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
                    for (int j = 0; j < dim; j++) {
                        whale[i][j] = Math.abs(bestWhalePositionX[j] - whale[i][j]) * Math.exp(b * l) * Math.cos(2 * Math.PI * l) + bestWhalePositionX[j];
                    }
                }

                //Boundary checking
                for (int j = 0; j < dim; j++) {
                    if (whale[i][j] > ub){
                        whale[i][j] = ub;
                    }
                    else if (whale[i][j] < lb){
                        whale[i][j] = lb;
                    }
                }
                phiWhale[i] = phi(whale[i], m);
                phiWhale[i] = GROA(phiWhale[i], weight, indices);
                fitness[i] = knapsackValue(phiWhale[i], value, weight);

                if (fitness[i] > bestWhale){
                    bestWhalePosition = Arrays.copyOf(phiWhale[i], phiWhale[i].length);
                    bestWhale = fitness[i];
                    bestWhalePositionX = Arrays.copyOf(whale[i], whale[i].length);
                }
            }
            t++;
        }
        System.out.println("Best whale position: " + Arrays.toString(bestWhalePosition));
        System.out.println("Best whale: " + bestWhale);
    }

    public static void main(String[] args) {
        int minItem = 300; // Minimum number of items to start with
        int maxItem = 1000; // Maximum number of items to go up to
        int step = (maxItem - minItem) / 100; // Calculate the step to generate 500 values
        int N = 50; // Number of whales
        int ub = 1; // Upper bound for the whale generation
        int lb = 0; // Lower bound for the whale generation
        int maxIteration = 100; // Maximum number of iterations
    
        Random random = new Random();
        int maxWeight = 50;
        int maxValue = 100;
    
        // Generate weights and values for the maximum number of items
        int[] weights = new int[maxItem];
        int[] values = new int[maxItem];
        for (int i = 0; i < maxItem; i++) {
            weights[i] = random.nextInt(maxWeight) + 1; // Weights are between 1 and maxWeight
            values[i] = random.nextInt(maxValue) + 1; // Values are between 1 and maxValue
        }

        ArrayList<Integer> inputSizes = new ArrayList<>();
        ArrayList<Long> actualRuntime = new ArrayList<>();
    
        for (int itemCount = minItem; itemCount <= maxItem; itemCount += step) {
            int dim = itemCount;
            System.out.println("Running KnapsackDWOA with itemCount = " + itemCount);
            long startTime = System.currentTimeMillis();
    
            discreteWhaleOptimizationAlgorithm(N, ub, lb, dim, maxIteration, Arrays.copyOf(weights, itemCount), Arrays.copyOf(values, itemCount));
    
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Total execution time for itemCount " + itemCount + ": " + duration + "ms");
            inputSizes.add(itemCount);
            actualRuntime.add(duration);
        }
        System.out.println("Input sizes: " + inputSizes + "\n");
        System.out.println("Actual runtime: " + actualRuntime);
    }    
}
