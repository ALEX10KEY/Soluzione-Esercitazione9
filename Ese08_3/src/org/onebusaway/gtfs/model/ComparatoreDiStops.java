package org.onebusaway.gtfs.model;

import java.util.Comparator;

public class ComparatoreDiStops implements Comparator<Stop> {

	@Override
	public int compare(Stop s1, Stop s2) {
			return s1.getName().compareTo(s2.getName());
	}

}
