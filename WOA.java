import java.util.Arrays;
import java.util.Random;

public class WOA {
    // Input: whale is a possible solution
    // Output: sum of the square of the whale
    // Description: Calculate the fitness function of the whale
    public static double fitnessFunction(double[] whale){
        double sum = 0;
        for (double j : whale) {
            sum += j * j;
        }
        return sum;
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
    // Input: N is the number of whales
    //        ub is the upper bound
    //        lb is the lower bound
    //        dim is the dimension
    //        maxIteration is the maximum iteration
    //Output: best whale and best whale position
    //Description: Finding the best whale and best whale position based on the fitness function
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
                Random random = new Random();
                double r = random.nextDouble();
                double A = r * 2 * a - a;
                double C = 2 * r;
                double p = random.nextDouble();
                double b = 0.5;
                double l = (random.nextDouble() * 2) - 1;
                if(p < 0.5){
                    if(Math.abs(A) < 1){
                        //Encircling for prey updating position (6)
                        for (int j = 0; j < dim; j++) {
                            whale[i][j] = bestWhalePosition[j] - A * Math.abs(C * bestWhalePosition[j] - whale[i][j]);
                        }
                    }
                    else if(Math.abs(A) >= 1){
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
        int N = 10000000;
        int ub = 20;
        int lb = -20;
        int dim = 2;
        int maxIteration = 200;
        long startTime = System.currentTimeMillis();
        whaleOptimizationAlgorithm(N,ub,lb,dim,maxIteration);
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}