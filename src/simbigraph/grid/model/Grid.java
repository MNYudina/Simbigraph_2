package simbigraph.grid.model;

import java.util.Map;

import simbigraph.grid.model.AbstractGrid.PointHolder;




/**
 * Interface for classes implementing grid type projection space.
 * 
 * @author Nick Collier
 */
public interface Grid<T>{

	/**
	 * Gets the dimensions of the space.
	 * 
	 * @return the dimensions of the space.
	 */
	GridDimensions getDimensions();

	/**
	 * Moves the specified object from its current location to the new location.
	 * The object must previously have been introduced into the space. Objects
	 * are introduced into the space by adding them to the context of which this
	 * space is a projection.
	 * 
	 * @param object
	 * @param newLocation
	 * @return true if the move was successful, otherwise false.
	 * @throws repast.space.SpatialException
	 *             if the object is not already in the space, if the number of
	 *             dimensions in the location does not agree with the number in
	 *             the space, or if the object is moved outside the grid
	 *             dimensions.
	 */
	boolean moveTo(T object, int... newLocation);

	/**
	 * Gets the location of the specified object.
	 * 
	 * @param obj
	 * @return the location of the specified object or null if the object is not
	 *         in the space.
	 */
	GridPoint getLocation(Object obj);

	/**
	 * Gets the number of objects currently in the space. This does NOT include
	 * any objects that may have been added, but have NOT been moved to a space
	 * location.
	 * 
	 * @return the number of objects currently in the space. This does NOT
	 *         include any objects that may have been added, but have NOT been
	 *         moved to a space location.
	 */
	int size();

	/**
	 * Retrieves the rule being used for controlling what happens at or beyond
	 * the borders of the space.
	 * 
	 * @return the rule for handling out of bounds coordinates
	 */
	GridPointTranslator getGridPointTranslator();

	/**
	 * Sets the rule to use for controlling what happens at or beyond the
	 * borders of the space.
	 * 
	 * @param rule
	 *            the rule for handling out of bounds coordinates
	 */
	void setGridPointTranslator(GridPointTranslator rule);

	/**
	 * Gets all the object currently in the space. This does NOT include any
	 * objects that may have been added, but have NOT been moved to a space
	 * location.
	 * 
	 * @return an iteratable over all the object currently in the space. This
	 *         does NOT include any objects that may have been added, but have
	 *         NOT been moved to a space location.
	 */
	Iterable<T> getObjects();

	/**
	 * Gets the object at the specified location.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	T getObjectAt(int... location);

	/**
	 * Gets all the objects at the specified location. For a multi occupancy
	 * space this will be all the objects at that location. For a single
	 * occupancy space this will be the single object at that location.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	Iterable<T> getObjectsAt(int... location);

	/**
	 * Gets a random object from among those at the specified location. If this
	 * is a single occupancy space this will return the single object at that
	 * location, if any.
	 * 
	 * @param location
	 * @return the object at the specified location.
	 */
	T getRandomObjectAt(int... location);

	/**
	 * Moves the specified object from its current location by the specified
	 * amount. For example <code>moveByDisplacement(object, 3, -2, 1)</code>
	 * will move the object by 3 along the x-axis, -2 along the y and 1 along
	 * the z. The displacement argument can be less than the number of
	 * dimensions in the space in which case the remaining argument will be set
	 * to 0. For example, <code>moveByDisplacement(object, 3)</code> will move
	 * the object 3 along the x-axis and 0 along the y and z axes, assuming a 3D
	 * grid.
	 * 
	 * @param object
	 *            the object to move
	 * @param displacement
	 *            the amount to move the object
	 * @return the new location if the move was successful, otherwise null
	 * @throws repast.space.SpatialException
	 *             if the object is not already in the space or if the number of
	 *             dimensions in the displacement greater than the number of
	 *             grid dimensions.
	 */
	GridPoint moveByDisplacement(T object, int... displacement);

	/**
	 * True if this grid is peridoic (in the sense that moving off one border
	 * makes you appear on the other one), otherwise false. A 2D periodic grid
	 * is a torus. This is defined by the grid's border behavior which is
	 * determined by its GridPointTranslator.
	 * 
	 * @return true if this grid is periodic, otherwise false.
	 */
	boolean isPeriodic();
	public Map<T, PointHolder>getLocationMap();
	public Map<GridPoint, Object> getLocationStorage();
	public void remove(T obj);
}
