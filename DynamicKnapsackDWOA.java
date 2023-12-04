import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class DynamicKnapsackDWOA {
    private final static int maxCapacity = 1500;
    private static final Random random = new Random();

    public static int knapsackValue(int[] solution, int[] values, int[] weights, int groupNum) {
        int totalValue = 0;
        int totalWeight = 0;

        for (int i = 0; i < groupNum; i++) {
            if(solution[i] == 1){
                totalValue += values[3*i];
                totalWeight += weights[3*i];
            }
            else if(solution[i] == 2){
                totalValue += values[3*i + 1];
                totalWeight += weights[3*i + 1];
            }
            else if(solution[i] == 3){
                totalValue += values[3*i + 2];
                totalWeight += weights[3*i + 2];
            }
        }
        if (totalWeight > maxCapacity) {
            return 0;
        }
        return totalValue;
    }

    public static double vNew(double x) {
        return Math.abs(Math.exp(x) - 1) / (Math.exp(x) + 1);
    }

    public static int[] NROA(int[] Y, int[] weight, Integer[] indices) {
        int fWeight = 0;

        for(int j = 0; j < Y.length; j++){
            int q = indices[j] /3;
            int r = indices[j] % 3;
            if(fWeight + weight[indices[j]] <= maxCapacity && Y[q] == r+1){
                fWeight = fWeight + weight[indices[j]];
            }
            else if(fWeight + weight[indices[j]] > maxCapacity && Y[q] == r+1){
                Y[q] = 0;
            }
        }

        for(int j = 0; j < Y.length; j++){
            int q = indices[j] /3;
            int r = indices[j] % 3;
            if(fWeight + weight[indices[j]] <= maxCapacity && Y[q] == 0){
                fWeight = fWeight + weight[indices[j]];
                Y[q] = r+1;
            }
        }

        return Y;
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

    public static void createDynamicItems(int[] weights, int[] profits, int n) {
        for (int i = 0; i < n; i++) {
            int idx = 3 * i;
            weights[idx + 2] = Math.min(weights[idx], weights[idx + 1]) - 1;
            profits[idx + 2] = profits[idx] + profits[idx + 1];
        }
    }

    public static void discreteWhaleOptimizationAlgorithm(int N, int ub, int lb, int itemCount, int maxIteration, int[] weight, int[] profits){
        createDynamicItems(weight, profits, itemCount/3);
        Integer[] indices = new Integer[itemCount];
        for (int i = 0; i < itemCount; i++) {
            indices[i] = i;
        }
        Arrays.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                double density1 = (double) profits[i1] / weight[i1];
                double density2 = (double) profits[i2] / weight[i2];
                return Double.compare(density2, density1);
            }
        });

        double[][] whale = generateWhale(N, ub,lb, itemCount/3);
        int[][] phiWhale = new int[N][itemCount/3];
        int[] m = new int[itemCount/3];
        Arrays.fill(m, 3);
        for (int i = 0; i < N; i++) {
            phiWhale[i] = phi(whale[i], m);
            phiWhale[i] = NROA(phiWhale[i], weight, indices);
        }

        int[] fitness = new int[N];
        for (int i = 0; i < N; i++) {
            fitness[i] = knapsackValue(phiWhale[i], profits, weight, itemCount/3);
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
                        for (int j = 0; j < itemCount / 3; j++) {
                            whale[i][j] = bestWhalePositionX[j] - A * Math.abs(C * bestWhalePositionX[j] - whale[i][j]);
                        }
                    }
                    else if (Math.abs(A) >= 1){
                        //Encircling prey updating position (13)
                        int rand = random.nextInt(N);
                        while (rand == i){
                            rand = random.nextInt(N);
                        }
                        for (int j = 0; j < itemCount/3; j++) {
                            whale[i][j] = whale[rand][j] - A * Math.abs(C * whale[rand][j] - whale[i][j]);
                        }
                    }
                }
                else {
                    for (int j = 0; j < itemCount/3; j++) {
                        whale[i][j] = Math.abs(bestWhalePositionX[j] - whale[i][j]) * Math.exp(b * l) * Math.cos(2 * Math.PI * l) + bestWhalePositionX[j];
                    }
                }

                //Boundary checking
                for (int j = 0; j < itemCount/3; j++) {
                    whale[i][j] = Math.min(Math.max(whale[i][j], lb), ub);
                }
                phiWhale[i] = phi(whale[i], m);
                phiWhale[i] = NROA(phiWhale[i], weight, indices);
                fitness[i] = knapsackValue(phiWhale[i], profits, weight, itemCount/3);

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
        int groupNum = 20;
        int itemCount = 3 * groupNum;
        int minWeight = 50;
        int maxWeight = 200;
        int minValue = 200;
        int maxValue = 500;

        int[] weights = new int[itemCount];
        int[] profits = new int[itemCount];
        Random rand = new Random(0);
        for (int i = 0; i < groupNum; i++) {
            int idx = 3 * i;
            weights[idx] = rand.nextInt(maxWeight - minWeight + 1) + minWeight;
            profits[idx] = rand.nextInt(maxValue - minValue) + minValue;
            weights[idx + 1] = rand.nextInt(maxWeight - minWeight + 1) + minWeight;
            profits[idx + 1] = rand.nextInt(maxValue - minValue) + minValue;
        }

        int N = 30;
        int ub = 3;
        int lb = 0;
        int maxIteration = 500;

        discreteWhaleOptimizationAlgorithm(N, ub, lb, itemCount, maxIteration, weights, profits);

    }
}