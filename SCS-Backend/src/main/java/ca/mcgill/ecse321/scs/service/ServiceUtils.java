package ca.mcgill.ecse321.scs.service;

import java.util.List;
import java.util.ArrayList;

/**
 * Utility class for services
 */
public class ServiceUtils {
    /**
     * Convert an iterable to a list
     * @param iterable
     * @return List of elements in the iterable object
     */
    public static <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
