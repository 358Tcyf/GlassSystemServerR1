package project.ys.glass_system.util;

public class RandomUtils {


    public final static int plan = 0;
    public final static int harden = 1;
    public final static int coat = 2;
    public final static int fail = 3;

    public final static int appointment = 0;
    public final static int delivery = 1;

    public final static int minLimit = 100;
    public final static int maxLimit = 500;


    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static double randomInt(double min, double max) {
        return Double.valueOf(String.format("%.2f", (Math.random() * (max - min) + min)));
    }

    public static int[] randomProduce() {
        int[] randomProduce = new int[4];
        randomProduce[plan] = randomInt(minLimit, maxLimit);
        randomProduce[harden] = randomInt((int) (minLimit + (randomProduce[plan] - minLimit) * 0.8), randomProduce[plan]);
        randomProduce[coat] = randomInt((int) (minLimit + (randomProduce[harden] - minLimit) * 0.8), randomProduce[harden]);
        randomProduce[fail] = randomInt(0, randomProduce[plan] - randomProduce[harden]);
        return randomProduce;
    }

    public static int[] randomSale() {
        int[] randomSale = new int[2];
        randomSale[appointment] = randomInt(10, 100);
        randomSale[delivery] = randomInt((int) (10 + (randomSale[appointment] - 10) * 0.9), randomSale[appointment]);
        return randomSale;
    }
}
