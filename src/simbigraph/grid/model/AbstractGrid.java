package simbigraph.grid.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import simbigraph.grid.model.SpatialException;
import simbigraph.grid.model.SpatialMath;



import javolution.util.FastMap;

/**
 * Default implementation of an n-dimensional grid.
 */
public abstract class AbstractGrid<T, U> implements Grid<T> {

	private final List<T> EMPTY_LIST = new ArrayList<T>();
	private double[] vectorTmp;

	/**
	 * A little class used for getting rid of some hash table lookups.
	 */
	public static class PointHolder {
		GridPoint point;
	}

	// this holds the objects that have been added, this includes ones that have
	// been
	// added, but not yet placed on the grid (through a move).  If they have not been
	// moved then they are assosciated with a null point
	protected Map<T, PointHolder> agentLocationMap;

	protected U locationStorage;

	protected GridDimensions dimensions;


	protected GridPointTranslator translator;

	protected CellAccessor<T, U> accessor;

	protected boolean ok = true;

	protected int size = 0;

	/**
	 * Constructs this space with the specified name, adder, translator,
	 * accessor and size. The size is the size of the space meaning [3, 3] is a
	 * 3x3 space.
	 * 
	 * @param name
	 *            the name of the space
	 * @param size
	 *            the dimensions of the space
	 */
	public AbstractGrid(   GridPointTranslator translator, CellAccessor<T, U> accessor,
	                    int... size) {
	//	super(name);
		this.translator = translator;
		this.accessor = accessor;

		int[] aSize = new int[size.length];
		int i = 0;
		for (int dim : size) {
			aSize[i++] = dim;
		}
		this.dimensions = new GridDimensions(aSize);
		vectorTmp = new double[this.dimensions.size()];

		this.agentLocationMap = new FastMap<T, PointHolder>();
		this.locationStorage = createLocationStorage();
		this.translator.init(dimensions);
	}

	protected abstract U createLocationStorage();

	/**
	 * Moves the specified object from its current location into the new
	 * location. The object must previously have been introduced into the space.
	 * Objects are introduced into the space by adding them to the context of
	 * which this space is a projection.
	 * 
	 * @param object
	 * @param newLocation
	 * @return true if the move was successful, otherwise false.
	 * @throws repast.space.SpatialException
	 *             if the object is not already in the space or if the number of
	 *             dimensions in the location does not agree with the number in
	 *             the space.
	 */
	public boolean moveTo(T object, int... newLocation) {
		
		
		PointHolder holder = agentLocationMap.get(object);
		if(holder==null)agentLocationMap.put(object, new PointHolder());
		holder = agentLocationMap.get(object);
		if (holder == null) {
			throw new SpatialException("Object '"+ object +
							"' must be added to the space's context before it can be moved");
		}

		if (newLocation.length != dimensions.size()) {
			throw new SpatialException("Number of new location dimensions must match grid dimensions");
		}

		int[] movedCoords = new int[dimensions.size()];
		translator.transform(movedCoords, newLocation);
		return doMove(object, movedCoords, holder) != null;

	}

	
	// assigns the specified object to the specified coordinates.
	// returns null if the object cannot be put at the specified coordinates.
	private GridPoint doMove(T object, int[] movedCoords, PointHolder holder) {
		GridPoint movedPoint = new GridPoint(movedCoords);
		if (accessor.put(object, locationStorage, movedPoint)) {
			if (holder.point == null) {
				// if the object hasn't yet been put in the space
				size++;
			} else {
				accessor.remove(object, locationStorage, holder.point);
			}
			holder.point = movedPoint;
			//fireProjectionEvent(new ProjectionEvent(this, object,
					//ProjectionEvent.OBJECT_MOVED));
			return holder.point;
		} else {
			return null;
		}
	}

	/**
	 * Gets the location of the specified object.
	 * 
	 * @param obj
	 * @return the location of the specified object or null if the object is not
	 *         in the space.
	 */
	public GridPoint getLocation(Object obj) {
		PointHolder loc = agentLocationMap.get(obj);
		if (loc == null) {
			return null;
		}
		return loc.point;
	}

	/**
	 * Gets the number of objects currently in the space. This does NOT include
	 * any objects that may have been added, but have NOT been moved to a space
	 * location.
	 * 
	 * @return the number of objects currently in the space. This does NOT
	 *         include any objects that may have been added, but have NOT been
	 *         moved to a space location.
	 */
	public int size() {
		return size;
	}

	/**
	 * Retrieves the rule being used for controlling what happens at or beyond
	 * the borders of the space.
	 * 
	 * @return the rule for handling out of bounds coordinates
	 */
	public GridPointTranslator getGridPointTranslator() {
		return translator;
	}

	/**
	 * Sets the rule to use for controlling what happens at or beyond the
	 * borders of the space.
	 * 
	 * @param rule
	 *            the rule for handling out of bounds coordinates
	 */
	public void setGridPointTranslator(GridPointTranslator rule) {
		this.translator = rule;
	}

	/**
	 * Gets all the object currently in the space. This does NOT include any
	 * objects that may have been added, but have NOT been moved to a space
	 * location.
	 * 
	 * @return an iteratable over all the object currently in the space. This
	 *         does NOT include any objects that may have been added, but have
	 *         NOT been moved to a space location.
	 */
	public Iterable<T> getObjects() {
		return agentLocationMap.keySet();
	}

	/**
	 * Gets the object at the specified location.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	public T getObjectAt(int... location) {
		int[] loc = getTransformedLocation(location);
		if (loc == null) return null;
		return accessor.get(locationStorage, new GridPoint(loc));
	}

	/**
	 * Gets all the objects at the specified location. For a multi occupancy
	 * space this will be all the objects at that location. For a single
	 * occupancy space this will be the single object at that location.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	public Iterable<T> getObjectsAt(int... location) {
		int[] loc = getTransformedLocation(location);
		if (loc == null) return EMPTY_LIST;
		return accessor.getAll(locationStorage, new GridPoint(loc));
	}

	protected int[] getTransformedLocation(int... location) {
		int[] copy = location.clone();
		translator.transform(copy, location);
		return copy;
	}

	/**
	 * Gets a random object from among those at the specified location. If this
	 * is a single occupancy space this will return the single object at that
	 * location, if any.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	public T getRandomObjectAt(int... location) {
		int[] loc = getTransformedLocation(location);
		if (loc == null) return null;
		return accessor.getRandom(locationStorage, new GridPoint(loc));
	}

	/**
	 * Moves the specified object from its current location by the specified
	 * amount. For example <code>moveByDisplacement(object, 3, -2, 1)</code>
	 * will move the object by 3 along the x-axis, -2 along the y and 1 along
	 * the z. The displacement argument can be less than the number of
	 * dimensions in the space in which case the remaining argument will be
	 * set to 0. For example, <code>moveByDisplacement(object, 3)</code> will
	 * move the object 3 along the x-axis and 0 along the y and z axes, assuming
	 * a 3D grid.
	 * 
	 * @param object
	 *            the object to move
	 * @param displacement
	 *            the amount to move the object
	 * @return the new location if the move was successful, otherwise null
	 * @throws repast.space.SpatialException
	 *             if the object is not already in the space or if the number of
	 *             dimensions in the displacement does not agree with the number in
	 *             the grid.
	 */
	public GridPoint moveByDisplacement(T object, int... displacement) {
		if (dimensions.size() < displacement.length) {
			throw new SpatialException(
					"Displacement matrix cannot have more dimensions than space");
		}

		PointHolder holder = agentLocationMap.get(object);
		if (holder == null) {
			throw new SpatialException(
					"Object '"
							+ object
							+ "' must be added to the space's context before it can be moved");
		}

		int[] movedCoords = new int[dimensions.size()];
		// assumes that the first introducing move into a grid is never by displacement
		holder.point.toIntArray(movedCoords);
		translator.translate(movedCoords, displacement);
		return doMove(object, movedCoords, holder);
	}

	
	

	/**
	 * Gets the dimensions of the grid.
	 * 
	 * @return the dimensions of the grid.
	 */
	public GridDimensions getDimensions() {
		return dimensions;
	}



	protected void removeAll() {
		for (T t : agentLocationMap.keySet()) {
			remove(t);
		}
	}

	public void remove(T t) {
		GridPoint location = agentLocationMap.remove(t).point;
		if (location != null) {
			accessor.remove(t, locationStorage, location);
		}
		size--;
	}

	/**
	 * True if this grid is peridoic (in the sense that moving off one border
	 * makes you appear on the other one), otherwise false. A 2D periodic grid is
	 * a torus. This is defined by the grid's border behavior which is determined by
	 * its GridPointTranslator.
	 *
	 * @return true if this grid is periodic, otherwise false.
	 */
	public boolean isPeriodic() {
		return translator.isToroidal();
	}

	/**
	 * Evaluate this Projection against the specified Predicate. This typically
	 * involves a double dispatch where the Projection calls back to the
	 * predicate, passing itself.
	 *
	 * @param predicate
	 * @return true if the predicate evaluates to true, otherwise false. False
	 *         can also mean that the predicate is not applicable to this
	 *         Projection. For example, a linked type predicate evaluated
	 *         against a grid projection.
	 */

}
