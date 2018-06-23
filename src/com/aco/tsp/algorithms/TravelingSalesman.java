package com.aco.tsp.algorithms;

import com.aco.tsp.main.TSPDisplayedWindow;
import com.aco.tsp.utility.Ant;
import com.aco.tsp.utility.Diagram;
import com.aco.tsp.utility.Utility;

public class TravelingSalesman {

	private Diagram diagram;
	private int numOfAnts, generations;

	public TravelingSalesman(int ants, int generations, double evaporation, int alpha, int beta) {
		this.numOfAnts = ants;
		this.generations = generations;
		diagram = Utility.getDiagram(evaporation, alpha, beta);
	}

	/**
	 * 開始旅行～
	 */
	public void run() {
		TSPDisplayedWindow window = new TSPDisplayedWindow(diagram.getPoints());
		Ant bestAnt = null;
		int bestEval = 0;
		delay(1000); // Allow WindowTSP to load.
		for (int i = 0; i < generations; i++) {
			Ant[] ants = createAnts(numOfAnts);
			Ant ant = travel(ants);
			updatePheromones(ants);
			if (bestAnt == null) {
				bestAnt = ant;
				bestEval = ant.distances();
			} else if (ant.distances() < bestEval) {
				bestAnt = ant;
				bestEval = ant.distances();
			}
			window.draw(bestAnt.getTour());
		}
		System.out.print("Best Tour: ");
		System.out.println(bestAnt);
		System.out.println("Evaluation: " + bestEval);
	}

	/**
	 * 建立螞蟻
	 * 
	 * @param numOfAnts
	 *            螞蟻要產生的數量
	 * @return Ant[]
	 */
	private Ant[] createAnts(int numOfAnts) {
		Ant[] ants = new Ant[numOfAnts];
		for (int i = 0; i < numOfAnts; i++) {
			ants[i] = new Ant(diagram);
		}
		return ants;
	}

	/**
	 * 讓每隻螞蟻走完全部的城市
	 * 
	 * @param ants
	 *            螞蟻群
	 * @return 路徑最短的螞蟻
	 */
	private Ant travel(Ant[] ants) {
		Ant bestAnt = null;
		int bestEval = 0;
		for (Ant ant : ants) {
			while (!ant.isFinished()) {
				ant.startTravel();
			}
			if (bestAnt == null) {
				bestAnt = ant;
				bestEval = ant.distances();
			} else if (ant.distances() < bestEval) {
				bestAnt = ant;
				bestEval = ant.distances();
			}
		}
		return bestAnt;
	}

	/**
	 * 更新費洛蒙
	 * 
	 * @param ants
	 *            螞蟻群
	 */
	private void updatePheromones(Ant[] ants) {
		for (Ant ant : ants) {
			diagram.updatePheromone(ant);
		}
	}

	private static void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
