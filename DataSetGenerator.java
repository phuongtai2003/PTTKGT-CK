import java.util.Random;

public class DataSetGenerator {
    private static final Random rand = new Random();
    //Input: itemCount is the number of items
    //       minProfits is the minimum profits of an item
    //       maxProfits is the maximum profits of an item
    //       minWeight is the minimum weight of an item
    //       maxWeight is the maximum weight of an item
    //Output: weights is the weights of the items
    //        profits is the profits of the items
    //Description: Generate the weights and profits of the items
    public static int[][] generateForKnapsack(int itemCount, int minProfits, int maxProfits, int minWeight, int maxWeight) {
        int[] weights = new int[itemCount];
        int[] profits = new int[itemCount];

        for (int i = 0; i < itemCount; i++) {
            weights[i] = rand.nextInt(maxWeight - minWeight + 1) + minWeight;
            profits[i] = rand.nextInt(maxProfits - minProfits + 1) + minProfits;
        }
        int[][] returns = new int[2][itemCount];
        returns[0] = weights;
        returns[1] = profits;
        return returns;
    }

    //Input: groupNum is the number of groups
    //       minProfits is the minimum profits of an item
    //       maxProfits is the maximum profits of an item
    //       minWeight is the minimum weight of an item
    //       maxWeight is the maximum weight of an item
    //Output: weights is the weights of the items
    //        profits is the profits of the items
    //Description: Generate the weights and profits of the items by groups
    public static int[][] generateForDiscountedKnapsack(int groupNum, int minProfits, int maxProfits, int minWeight, int maxWeight) {
        int[] weights = new int[groupNum * 3];
        int[] profits = new int[groupNum * 3];

        for (int i = 0; i < groupNum; i++) {
            int index = i * 3;
            weights[index] = rand.nextInt(maxWeight - minWeight + 1) + minWeight;
            profits[index] = rand.nextInt(maxProfits - minProfits + 1) + minProfits;
            weights[index + 1] = rand.nextInt(maxWeight - minWeight + 1) + minWeight;
            profits[index + 1] = rand.nextInt(maxProfits - minProfits + 1) + minProfits;
        }
        int[][] returns = new int[2][groupNum * 3];
        returns[0] = weights;
        returns[1] = profits;
        return returns;
    }
    //Input: itemCount is the number of items
    //       minSetCosts is the minimum cost of a set
    //       maxSetCosts is the maximum cost of a set
    //Output: setCosts is the cost of each set
    //Description: Generate the cost of each set
    public static int[] generateSetCosts(int itemCount, int minSetCosts, int maxSetCosts) {
        int[] setCosts = new int[itemCount];
        for (int i = 0; i < itemCount; i++) {
            setCosts[i] = rand.nextInt(maxSetCosts - minSetCosts + 1) + minSetCosts;
        }
        return setCosts;
    }
}