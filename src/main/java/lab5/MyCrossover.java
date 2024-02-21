package lab5;

import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MyCrossover extends AbstractCrossover<double[]> {
    protected MyCrossover() {
        super(1);
    }

    protected List<double[]> mate(double[] p1, double[] p2, int i, Random random) {
        ArrayList children = new ArrayList();

        // your implementation:
        double[][] childs = new double[2][p1.length];

        for(int it = 0; it < p1.length; it++) {
            double alpha = random.nextDouble();
            childs[0][it] = alpha * p1[it] + (1 - alpha) * p2[it];
            childs[1][it] = alpha * p2[it] + (1 - alpha) * p1[it];
        }
        children.addAll(Arrays.asList(childs));

        return children;
    }
}
