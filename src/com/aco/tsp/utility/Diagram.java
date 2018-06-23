package com.aco.tsp.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Diagram implements Iterable<Point> {

	private HashMap<Integer, Point> pointHashMap;
	private ArrayList<Point> pointList;
	private int alpha, beta;
	private double evaporationRate;

	public Diagram(double evaporationRate, int alpha, int beta) {
		this.alpha = alpha;
		this.beta = beta;
		this.evaporationRate = evaporationRate;
		clear();
	}

	public int getAlpha() {
		return alpha;
	}

	public int getBeta() {
		return beta;
	}

	/**
	 * 取得全部城市的數量
	 * 
	 * @return int
	 */
	public int getTotalPoints() {
		return pointHashMap.size();
	}

	/**
	 * 重設資料
	 */
	public void clear() {
		pointHashMap = new HashMap<>();
		pointList = new ArrayList<>();
	}

	/**
	 * 加入一個點到圖形上
	 * 
	 * @param point
	 *            要加入圖形的點.
	 */
	public void addPoint(Point point) {
		pointHashMap.put(point.hashCode(), point);
		pointList.add(point);
	}

	public Point getPoint(City city) {
		return pointHashMap.get(city.hashCode());
	}

	public void addPath(Point point, City city) {
		point.appendPath(city);
	}

	public City[] getPoints() {
		City[] cities = new City[getTotalPoints()];
		int i = 0;
		for (Point p : this) {
			cities[i++] = p;
		}
		return cities;
	}

	/**
	 * 更新費洛蒙
	 * 
	 * @param ant
	 *            螞蟻
	 */
	public void updatePheromone(Ant ant) {
		double eval = ant.distances();
		double probability = (1 - evaporationRate);
		City[] cities = ant.getTour();
		HashSet<Path> hashSet = new HashSet<>();
		for (int i = 1; i < cities.length; i++) {
			Path p1 = getPoint(cities[i - 1]).getPath(cities[i]);
			Path p2 = getPoint(cities[i]).getPath(cities[i - 1]);

			double ph1 = p1.getPheromone();
			double ph2 = p2.getPheromone();

			hashSet.add(p1);
			hashSet.add(p2);

			p1.setPheromone(probability * ph1 + 1.0 / eval);
			p2.setPheromone(probability * ph2 + 1.0 / eval);
		}

		// 費洛蒙蒸發
		for (Point point : this) {
			for (Path path : point) {
				if (!hashSet.contains(path)) {
					double p = path.getPheromone();
					path.setPheromone(probability * p);
				}
			}
		}
	}

	public static double getDistance(City city1, City city2) {
		double xDiff = city1.getX() - city2.getX();
		double yDiff = city1.getY() - city2.getY();
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}

	@Override
	public Iterator<Point> iterator() {
		return pointList.iterator();
	}

}
