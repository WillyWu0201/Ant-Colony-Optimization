package com.aco.tsp.utility;

public class Path extends City {

	private double pheromone;

	public Path(String name, int x, int y) {
		super(name, x, y);
		pheromone = 0.01;
	}

	public void setPheromone(double pheromone) {
		this.pheromone = pheromone;
	}

	public double getPheromone() {
		return pheromone;
	}
}
