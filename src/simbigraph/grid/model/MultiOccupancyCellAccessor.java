package simbigraph.grid.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import simbigraph.math.RandomHelper;



/**
 * Coordinate accessor where a location can hold multiple individual objects. This uses a list to
 * store the objects in each cell.
 * 
 * @author Nick Collier
 */
public class MultiOccupancyCellAccessor<T> implements CellAccessor<T, Map<GridPoint, Object>> {

	private static Iterable EMPTY_ITERABLE = new ArrayList();

	/**
	 * Gets the item at the specified location in the map.
	 * 
	 * @param locationMap
	 *            the location map to put the object in
	 * @param location
	 *            the location in the map
	 * @return the item at the specified location
	 */
	@SuppressWarnings("unchecked")
	public T get(Map<GridPoint, Object> locationMap, GridPoint location) {
		List<T> list = (List<T>) locationMap.get(location);
		if (list == null)
			return null;
		return list.get(0);
	}

	/**
	 * Gets all the items at the specified location in the map.
	 * 
	 * @param locationMap
	 *            the location map to retrieve from
	 * @param location
	 *            the location in the map
	 * @return an iterator over all the items at the specified location
	 */
	@SuppressWarnings("unchecked")
	public Iterable<T> getAll(Map<GridPoint, Object> locationMap, GridPoint location) {
		List<T> list = (List<T>) locationMap.get(location);
		if (list == null) {
			return MultiOccupancyCellAccessor.EMPTY_ITERABLE;
		}

		return list;
	}

	/**
	 * Gets a random item from those at the specified location in the map.
	 * 
	 * @param locationMap
	 *            the location map to retrieve from
	 * @param location
	 *            the location in the map
	 * @return a random item at the specified location
	 */
	@SuppressWarnings("unchecked")
	public T getRandom(Map<GridPoint, Object> locationMap, GridPoint location) {
		List<T> list = (List<T>) locationMap.get(location);
		if (list == null) {
			return null;
		}
		return list.get(RandomHelper.nextIntFromTo(0, list.size() - 1));
	}

	/**
	 * Attempts to put the specified object at the specified location. Returns true if the put was
	 * successful. The semantics of the result will be determined by implementing classes.
	 * 
	 * @param obj
	 *            the object to put at the location
	 * @param locationMap
	 *            the location map to put the object in
	 * @param location
	 *            the location in the map
	 * @return true if the put was successful, otherwise false.
	 */
	@SuppressWarnings("unchecked")
	public boolean put(T obj, Map<GridPoint, Object> locationMap, GridPoint location) {
		List<T> list = (List<T>) locationMap.get(location);
		if (list == null) {
			list = new ArrayList<T>();
			locationMap.put(location, list);
		}
		list.add(obj);
		return true;
	}

	/**
	 * Removes the specified object from the location in the map.
	 * 
	 * @param obj
	 *            the object to remove
	 * @param locationMap
	 *            the location map to remove the object from
	 * @param location
	 *            the location in the map
	 */
	@SuppressWarnings("unchecked")
	public void remove(T obj, Map<GridPoint, Object> locationMap, GridPoint location) {
		List<T> list = (List<T>) locationMap.get(location);
		if (list != null) {
			list.remove(obj);
			if (list.isEmpty()) {
				// privledge memory over speed in this case
				locationMap.remove(location);
			}
		}
	}
}
