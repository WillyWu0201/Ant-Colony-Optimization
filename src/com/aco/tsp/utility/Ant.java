package com.aco.tsp.utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Ant {

	private Diagram diagram;
	private City currentCity;
	private HashSet<City> placesTravelled;
	private ArrayList<City> tour;

	public Ant(Diagram diagram) {
		this.diagram = diagram;
		clear();
	}

	/**
	 * 重新設定資料
	 */
	public void clear() {
		currentCity = getRandomPoint();
		placesTravelled = new HashSet<>();
		tour = new ArrayList<>();
	}

	/**
	 * 隨機一個城市
	 * 
	 * @return Point
	 */
	private Point getRandomPoint() {
		int r = new Random().nextInt(diagram.getTotalPoints());
		Iterator<Point> it = diagram.iterator();
		for (int i = 0; i < r; i++) {
			it.next();
		}
		return it.next();
	}

	/**
	 * 產生螞蟻下一個前往的城市
	 */
	public void startTravel() {
		if (isFinished()) {
			return;
		}
		// 最後回到起始點
		if (diagram.getTotalPoints() == tour.size()) {
			tour.add(tour.get(0));
			return;
		}
		Path path = nextPath();
		placesTravelled.add(path);
		tour.add(path);
		currentCity = path;
	}

	/**
	 * 螞蟻是否走完全部的城市.
	 * 
	 * @return boolean
	 */
	public boolean isFinished() {
		return diagram.getTotalPoints() + 1 == tour.size();
	}

	/**
	 * 取得螞蟻走完的城市
	 * 
	 * @return City[]
	 */
	public City[] getTour() {
		City[] cities = new City[tour.size()];
		for (int i = 0; i < tour.size(); i++) {
			cities[i] = tour.get(i);
		}
		return cities;
	}

	/**
	 * 取得全部的距離
	 * 
	 * @return int
	 */
	public int distances() {
		int eval = 0;
		for (int i = 1; i < tour.size(); i++) {
			eval += Diagram.getDistance(tour.get(i), tour.get(i - 1));
		}
		return eval;
	}

	/**
	 * 根據費洛蒙和距離取得下一個城市
	 * 
	 * @return Path
	 */
	public Path nextPath() {
		ArrayList<Pair<Path, Double>> probabilities = probabilities();
		double r = new Random().nextDouble();
		for (Pair<Path, Double> pair : probabilities) {
			if (r <= pair.item2) {
				return pair.item1;
			}
		}
		return null;
	}

	/**
	 * 取得每段路徑可以走的機率 假設如果有4個路徑，每個路徑機率為0.25，則結果為[0.25, 0.50, 0.75, 1.00]
	 * 
	 * @return ArrayList<Pair<Path, Double>>
	 */
	private ArrayList<Pair<Path, Double>> probabilities() {
		double denominator = denominator();
		ArrayList<Pair<Path, Double>> probabilities = new ArrayList<>(validPaths());

		for (Path path : diagram.getPoint(currentCity)) {
			if (placesTravelled.contains(path)) {
				continue;
			}
			Pair<Path, Double> pair = new Pair<>();
			if (probabilities.size() == 0) {
				pair.item2 = desirability(path) / denominator;
			} else {
				int i = probabilities.size() - 1;
				pair.item2 = probabilities.get(i).item2 + desirability(path) / denominator;
			}
			pair.item1 = path;
			probabilities.add(pair);
		}
		return probabilities;
	}

	/**
	 * 取得剩下可以走的城市數量
	 * 
	 * @return int
	 */
	private int validPaths() {
		int i = 0;
		for (Path path : diagram.getPoint(currentCity)) {
			if (!placesTravelled.contains(path)) {
				i++;
			}
		}
		return i;
	}

	/**
	 * 計算下個城市機率的分母
	 * 
	 * @return double
	 */
	private double denominator() {
		double denominator = 0.0;
		for (Path path : diagram.getPoint(currentCity)) {
			if (placesTravelled.contains(path)) {
				continue;
			}
			denominator += desirability(path);
		}
		return denominator;
	}

	/**
	 * 計算費洛蒙與距離相乘的結果
	 * 
	 * @param path
	 *            要計算的路徑
	 * @return double
	 */
	private double desirability(Path path) {
		double pheromone = Math.pow(path.getPheromone(), diagram.getAlpha());
		double distance = Diagram.getDistance(currentCity, path);
		double distanceValue = Math.pow(1 / distance, diagram.getBeta());
		return pheromone * distanceValue;
	}

	/**
	 * 建立一對pair.
	 * 
	 * @param <T>
	 *            城市
	 * @param <E>
	 *            抵達此城市的機率
	 */
	private static class Pair<T, E> {
		T item1;
		E item2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for (City city : tour) {
			if (flag)
				sb.append(" -> ");
			flag = true;
			sb.append(city.getName());
		}

		return new String(sb);
	}
}
