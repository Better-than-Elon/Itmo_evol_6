package lab5;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;

public class MyFactory extends AbstractCandidateFactory<double[]> {

    private int dimension;

    public MyFactory(int dimension) {
        this.dimension = dimension;
    }

    public double[] generateRandomCandidate(Random random) {
        double[] solution = new double[dimension];
        // x from -5.0 to 5.0

        // your implementation:
        double minX = -5.0;
        double maxX = 5.0;

        for(int i = 0; i < dimension; i++) {
            solution[i] = minX + (maxX - minX) * random.nextDouble();
        }

        return solution;
    }
}

