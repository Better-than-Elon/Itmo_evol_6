package lab5;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MasterSlaveAlg {
//    OptionalDouble[7.627611424673124]
//    OptionalDouble[401.4]
    static int dimension = 50; // dimension of problem
    static int complexity = 5; // fitness estimation time multiplicator
    static int populationSize = 1000; // size of population
    static int generations = 100; // number of generations
    static boolean inSingleThread = false;

    public static void main(String[] args) {
        ArrayList<Double> scores = new ArrayList<>();
        List<Long> time = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final long startTime = System.currentTimeMillis();
            scores.add(run());
            time.add(System.currentTimeMillis() - startTime);
        }
        System.out.println(scores);
        System.out.println(time);
        System.out.println(scores.stream().mapToDouble(a -> a).average());
        System.out.println(time.stream().mapToDouble(a -> a).average());
//        System.out.println(scores.stream().max(Double::compare));
    }

    public static double run() {
        final double[] runningBestFit = {0};
        Random random = new Random(); // random

        CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

        ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
        operators.add(new MyCrossover()); // Crossover
        operators.add(new MyMutation(generations)); // Mutation
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

        SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

        FitnessEvaluator<double[]> evaluator = new MultiFitnessFunction(dimension, complexity); // Fitness function

        AbstractEvolutionEngine<double[]> algorithm = new SteadyStateEvolutionEngine<double[]>(
                factory, pipeline, evaluator, selection, populationSize, false, random);

        algorithm.setSingleThreaded(inSingleThread);
//        algorithm.setSingleThreaded(true);

        algorithm.addEvolutionObserver(new EvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
                runningBestFit[0] = Math.max(runningBestFit[0], bestFit);
                System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit);
                System.out.println("\tBest solution = " + Arrays.toString((double[])populationData.getBestCandidate()));
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);
        algorithm.evolve(populationSize, 5, terminate);

        return runningBestFit[0];
    }
}
