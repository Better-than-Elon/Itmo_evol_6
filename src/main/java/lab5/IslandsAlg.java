package lab5;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.islands.IslandEvolution;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;
import org.uncommons.watchmaker.framework.islands.Migration;
import org.uncommons.watchmaker.framework.islands.RingMigration;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IslandsAlg {

    static int dimension = 50; // dimension of problem
    static int complexity = 5; // fitness estimation time multiplicator
    static int populationSize = 1000; // size of population
    static int generations = 100; // number of generations
    static int islandCount = 10;
    static int epochLength = 20;

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
        operators.add(new MyMutation(epochLength)); // Mutation
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

        SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

        FitnessEvaluator<double[]> evaluator = new MultiFitnessFunction(dimension, complexity); // Fitness function

        Migration migration = new RingMigration();

        IslandEvolution<double[]> island_model = new IslandEvolution<double[]>(
                islandCount, migration, factory, pipeline, evaluator, selection, random); // your model;

        island_model.addEvolutionObserver(new IslandEvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
                runningBestFit[0] = Math.max(runningBestFit[0], bestFit);
                System.out.println("Epoch " + populationData.getGenerationNumber() + ": " + bestFit);
//                System.out.println("\tEpoch best solution = " + Arrays.toString((double[]) populationData.getBestCandidate()));
            }

            public void islandPopulationUpdate(int i, PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
//                System.out.println("Island " + i);
//                System.out.println("\tGeneration " + populationData.getGenerationNumber() + ": " + bestFit);
//                System.out.println("\tBest solution = " + Arrays.toString((double[]) populationData.getBestCandidate()));
            }
        });
        int term_gen = generations / epochLength;
        TerminationCondition terminate = new GenerationCount(term_gen);
        island_model.evolve(populationSize / islandCount, 5, epochLength, 2, terminate);


        return runningBestFit[0];
    }
}