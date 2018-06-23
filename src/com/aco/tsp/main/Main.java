package com.aco.tsp.main;

import com.aco.tsp.algorithms.TravelingSalesman;

public class Main {

	// Dorigo等人建議，螞蟻數量S = 節點總數量 A
	private static int ants = 100; // 螞蟻數量
	private static int iterator = 1000; // 迭代次數
	private static int pheromoneImpactRate = 1; // 費洛蒙對決策的影響
	private static int distanceImpactRate = 5; // 距離對決策的影響
	private static double evaporationRate = 0.5;// 費洛蒙蒸發率

	public static void main(String[] args) {
		System.out.println("------------------ANT COLONY OPTIMIZATION------------------");
		TravelingSalesman travelingSalesman = new TravelingSalesman(ants, iterator, evaporationRate,
				pheromoneImpactRate, distanceImpactRate);
		travelingSalesman.run();
		System.out.println("-------------------------COMPLETE--------------------------");
	}
}
