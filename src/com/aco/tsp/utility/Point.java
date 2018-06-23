package com.aco.tsp.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Point extends City implements Iterable<Path> {

	private HashMap<Integer, Path> pathHashMap = new HashMap<>();
	private ArrayList<Path> pathList;

	public Point(String name, int x, int y) {
		super(name, x, y);
		pathHashMap = new HashMap<>();
		pathList = new ArrayList<>();
	}

	/**
	 * 在HashMap上加上走過的路徑.
	 * 
	 * @param city
	 *            要加入城市
	 * @return void
	 */
	public void appendPath(City city) {
		if (city instanceof Point) {
			Path path = new Path(city.getName(), city.getX(), city.getY());
			pathHashMap.put(path.hashCode(), path);
			pathList.add(path);
		} else {
			pathHashMap.put(city.hashCode(), (Path) city);
			pathList.add((Path) city);
		}
	}

	public Path getPath(City city) {
		return pathHashMap.get(city.hashCode());
	}

	/**
	 * 判斷這城市是否走過.
	 * 
	 * @param city
	 *            城市
	 * @return boolean
	 */
	public boolean contain(City city) {
		return pathHashMap.containsValue(city.hashCode());
	}

	/**
	 * 全部走過的數量.
	 * 
	 * @return int
	 */
	public int getTotalEdges() {
		return pathHashMap.size();
	}

	@Override
	public Iterator<Path> iterator() {
		return pathList.iterator();
	}

}
