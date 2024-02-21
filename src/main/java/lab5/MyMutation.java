package lab5;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.List;
import java.util.Random;


public class MyMutation implements EvolutionaryOperator<double[]> {
    int iteration = 0;
    int generations;
    MyMutation(int generations) {
        this.generations=generations;
    }
    MyMutation() {
        this.generations=50;
    }

    public List<double[]> apply(List<double[]> population, Random random) {
        // initial population
        // need to change individuals, but not their number!

        // your implementation:

        double minX = -5.0;
        double maxX = 5.0;
        double p = 0.005;//1. / population.get(0).length;
        double alpha = Math.min(Math.max(0d,(iteration*1.) / (generations*1.)), 1d);

        for (double[] sample : population) {
            for (int i = 0; i < sample.length; i++) {
                sample[i] = random.nextDouble() > p ? sample[i] :
                        alpha * sample[i] + (1 - alpha) * (minX + (maxX - minX) * random.nextDouble());
            }
        }
        //result population
        iteration++;
        return population;
    }
}
