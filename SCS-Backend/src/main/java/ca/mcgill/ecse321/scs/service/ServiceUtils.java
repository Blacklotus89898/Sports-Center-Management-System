package ca.mcgill.ecse321.scs.service;

import java.util.List;
import java.util.ArrayList;

public class ServiceUtils {
    public static <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
