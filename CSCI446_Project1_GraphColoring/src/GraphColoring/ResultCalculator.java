package GraphColoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Statistical behavior analyzer. Interacts with a ConstraintSolver class and
 * provides metrics based on its number of decisions made, valid colorings,
 * vertices visited, and vertices recolored
 *
 * @version 09/19/16
 */
public class ResultCalculator {

    private Map<Class<?>, int[]> solverMap = new HashMap<Class<?>, int[]>();
    ConstraintSolver solver;

    public ResultCalculator() {
        this.solver = solver;
        for (Class<?> solverClass : Driver.solverList) {
            solverMap.put(solverClass, new int[2]);
        }
    }

    /**
     * Work in progress. May take 1 to 4 inputs depending how we want to
     * calculate metrics
     *
     * @return
     */
    public void calculateRunMetrics(ArrayList<ConstraintSolver> solvers, int numVertices) {
        for (ConstraintSolver s : solvers) {
            solverMap.get(s.getClass())[0] += s.decisionsMade;
            solverMap.get(s.getClass())[1] += s.validColorings;
            System.out.println("Solver: " + s.getClass().getSimpleName() + " Graph size: " + numVertices);
            System.out.println(solverMap.get(s.getClass())[0] + "," + solverMap.get(s.getClass())[1]);
        }
    }
}
