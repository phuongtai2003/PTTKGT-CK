import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DWOA {
    // Input: setSelection is a possible solution
    //        setCosts is the cost of each set
    // Output: sum of the set cost
    // Description: Calculate the fitness function of the set cover cost problem
    public static int setCoverCost(int[] setSelection, int[] setCosts) {
        int totalCost = 0;
        for (int i = 0; i < setSelection.length; i++) {
            if (setSelection[i] == 1) {
                totalCost += setCosts[i];
            }
        }
        if(totalCost > 200){
            return 0;
        }
        return totalCost;
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
                            Y[i] = Math.abs((m[i] - j) % (m[i] + 1));
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
    //        setCosts is the cost of each set
    // Output: best whale and best whale position
    // Description: Finding the best whale and best whale position based on the set cover cost problem
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
                Random random = new Random();
                double r = random.nextDouble();
                double A = r * 2 * a - a;
                double C = 2 * r;
                double p = random.nextDouble();
                double b = 1;
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
        int N = 1000;
        int ub = 10;
        int lb = 0;
        int minDim = 1000;
        int maxDim = 10000;
        int maxIteration = 100;
        ArrayList<Integer> setSizesTested = new ArrayList<>();
        ArrayList<Long> executionTimes = new ArrayList<>();

        for(int dim = minDim; dim <= maxDim; dim += 100){
            int[] setCosts = DataSetGenerator.generateSetCosts(dim, 1, 10);
            long startTime = System.currentTimeMillis();
            discreteWhaleOptimizationAlgorithm(N,ub,lb,dim,maxIteration, setCosts);
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            setSizesTested.add(dim);
            executionTimes.add(executionTime);
        }
        System.out.println("Set sizes tested: " + setSizesTested);
        System.out.println("Execution times: " + executionTimes);
    }
}
