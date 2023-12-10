import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class KnapsackDWOA {
    private final static int maxCapacity = 800;

    // Input: solution is a possible solution
    //        weight is the weights of the items
    //        indices is the indices of the items, sorted descendingly by density
    // Output: a feasible solution
    // Description: Turn a potential solution into a feasible solution
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

    // Input: items is an array of possible solution
    //        values is the profits of the items
    //        weights is the weights of the items
    // Output: sum of the profits of the items
    // Description: Calculate the total profits for the knapsack if weight does not exceed the capacity
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
    // Input: N is the number of whales
    //        ub is the upper bound
    //        lb is the lower bound
    //        dim is the dimension
    // Output: The whale population
    // Description: Generate the whale based on the upper bound and lower bound of the dimension
    public static double[][] generateWhale(int N,int ub, int lb, int dim) {
        double[][] whale = new double[N][dim];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < dim; j++) {
                whale[i][j] = (Math.random() * (ub - lb) + lb);
            }
        }
        return whale;
    }
    // Input: x is an element in a whale
    // Output: vNew
    // Description: Calculate the vNew
    public static double vNew(double x) {
        return Math.abs(Math.exp(x) - 1) / (Math.exp(x) + 1);
    }
    // Input: X is a continuous whale
    //        m is the maximum value in each whale
    // Output: Y is the new discrete whale
    // Description: Transfer from a continuous whale to a new discrete whale
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

    // Input: N is the number of whales
    //        ub is the upper bound
    //        lb is the lower bound
    //        dim is the dimension
    //        maxIteration is the maximum iteration
    //        weight is the weights of the items
    //        value is the profits of the items
    // Output: best whale and best whale position
    // Description: Finding the best whale and best whale position based on the 0 - 1 knapsack problem
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
                Random random = new Random();
                double r = random.nextDouble();
                double A = r * 2 * a - a;
                double C = 2 * r;
                double p = random.nextDouble();
                double b = 0.5;
                double l = (random.nextDouble() * 2) - 1;
                if (p < 0.5){
                    if (Math.abs(A) < 1){
                        //Encircling prey updating position (6)
                        for (int j = 0; j < dim; j++) {
                            whale[i][j] = bestWhalePositionX[j] - A * Math.abs(C * bestWhalePositionX[j] - whale[i][j]);
                        }
                    }
                    else if (Math.abs(A) >= 1){
                        //Searching for prey updating position (13)
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
        int itemCount = 10;
        int minWeight = 50;
        int maxWeight = 200;
        int minValue = 200;
        int maxValue = 500;

        int[] weights = new int[itemCount];
        int[] values = new int[itemCount];

        Random random = new Random(0);

        for (int i = 0; i < itemCount; i++) {
            weights[i] = random.nextInt(maxWeight - minWeight + 1) + minWeight;
            values[i] = random.nextInt(maxValue - minValue + 1) + minValue;
        }
        int N = 50;
        int ub = 1;
        int lb = 0;
        int dim = values.length;
        int maxIteration = 300;
        for (int i = 0; i < 10; i++){
            discreteWhaleOptimizationAlgorithm(N, ub, lb, dim, maxIteration, weights, values);
        }
    }
}
