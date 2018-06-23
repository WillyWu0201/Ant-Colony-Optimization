package com.aco.tsp.utility;

import java.io.InputStream;

public class Utility {

	public static Diagram getDiagram(double evaporationRate, int alpha, int beta) {

		String dataSetName;
		int startingLine;

		dataSetName = "eli51.tsp";
		startingLine = 6;

		String[] lines = read(dataSetName).split("\n");
		String[] words = lines[3].split(" ");
		int numOfCities = Integer.parseInt(words[words.length - 1]);

		Point[] points = new Point[numOfCities];

		for (int i = startingLine; i < startingLine + numOfCities; i++) {
			String[] line = removeWhiteSpace(lines[i]).trim().split(" ");
			int x = (int) Double.parseDouble(line[1].trim());
			int y = (int) Double.parseDouble(line[2].trim());
			Point point = new Point(line[0], x, y);
			points[i - startingLine] = point;
		}

		Diagram diagram = new Diagram(evaporationRate, alpha, beta);
		for (int i = 0; i < numOfCities; i++) {
			diagram.addPoint(points[i]);
		}
		for (Point point : diagram) {
			for (int i = 0; i < numOfCities; i++) {
				if (points[i] != point) {
					diagram.addPath(point, points[i]);
				}
			}
		}
		return diagram;
	}

	private static String read(String fileName) {
		InputStream stream = Utility.class.getResourceAsStream(fileName);
		java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static String removeWhiteSpace(String s) {
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) == ' ' && s.charAt(i - 1) == ' ') {
				if (i != s.length()) {
					s = s.substring(0, i) + s.substring(i + 1, s.length());
					i--;
				} else {
					s = s.substring(0, i);
					i--;
				}
			}
		}
		return s;
	}
}
