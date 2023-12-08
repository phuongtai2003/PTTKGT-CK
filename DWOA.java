import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;

public class DWOA {
    private final static int[] setCosts = {50, 100, 20, 50, 30, 40, 60, 100, 80, 90};
    private static final Random random = new Random();

    public static int setCoverCost(int[] setSelection, int[] setCosts) {
        int totalCost = 0;
        for (int i = 0; i < setSelection.length; i++) {
            if (i >= setCosts.length) {
                break;  // Avoid accessing an index out of bounds
            }
            if (setSelection[i] == 1) {
                totalCost += setCosts[i];
                if (totalCost > 200) {
                    return 0;
                }
            }
        }
        return (totalCost >= 100) ? totalCost : 0;
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
                            Y[i] = Math.abs((m[i] - j) % (m[i] + 1));
                            break;
                        }
                    }
                }
            }
        }
        return Y;
    }

    public static void discreteWhaleOptimizationAlgorithm(int N, int ub, int lb, int dim, int maxIteration, int[] setCosts) {
        double [][] whale = generateWhale(N,ub,lb,dim);
        int[][] phiWhale = new int[N][dim];
        int[] m = new int[dim];
        Arrays.fill(m, 1);

        for (int i = 0; i < N; i++) {
            phiWhale[i] = phi(whale[i], m);
        }
        int[] fitness = new int[N];
        for (int i = 0; i < N; i++) {
            fitness[i] = setCoverCost(phiWhale[i], setCosts);
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
                double b = 1;
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
                fitness[i] = setCoverCost(phiWhale[i], setCosts);

                if (fitness[i] > bestWhale){
                    bestWhalePosition = Arrays.copyOf(phiWhale[i], phiWhale[i].length);
                    bestWhalePositionX = Arrays.copyOf(whale[i], whale[i].length);
                    bestWhale = fitness[i];
                }
            }
            t++;
        }
        System.out.println("Best whale position: " + Arrays.toString(bestWhalePosition));
        System.out.println("Best whale: " + bestWhale);
    }

    public static void main(String[] args) {
        int N = 50; // Number of whales
        int ub = 1; // Upper bound for the whale generation
        int lb = 0; // Lower bound for the whale generation
        int maxIteration = 100; // Maximum number of iterations

        int minSetSize = 300; // Minimum set size to start with
        int maxSetSize = 1000; // Maximum set size to test
        int stepSize = (maxSetSize - minSetSize) / 100; // Step size for varying set sizes

        ArrayList<Integer> setSizesTested = new ArrayList<>();
        ArrayList<Long> executionTimes = new ArrayList<>();

        for (int setSize = minSetSize; setSize <= maxSetSize; setSize += stepSize) {
            System.out.println("Running DWOA with set size = " + setSize);
            long startTime = System.currentTimeMillis();

            discreteWhaleOptimizationAlgorithm(N, ub, lb, setSize, maxIteration, Arrays.copyOf(setCosts, setSize));

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Execution time for set size " + setSize + ": " + duration + "ms");

            setSizesTested.add(setSize);
            executionTimes.add(duration);
        }

        System.out.println("Tested set sizes: " + setSizesTested);
        System.out.println("Execution times: " + executionTimes);
    }
}
