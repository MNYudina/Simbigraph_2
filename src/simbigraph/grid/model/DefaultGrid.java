package simbigraph.grid.model;


import java.util.Map;

import simbigraph.grid.model.AbstractGrid.PointHolder;


import javolution.util.FastMap;

/**
 * A default implementation of a grid backed by a map.
 * 
 * @author Jerry Vos
 */
public class DefaultGrid<T> extends AbstractGrid<T, Map<GridPoint, Object>> {

	/**
	 * 
	 * @param name
	 *            the name of the grid
	 * @param adder
	 *            the adder for adding new objects to the grid
	 * @param translator
	 * 
	 * @param accessor
	 *            the accessor used for accessing grid cells
	 * @param size
	 *            the size of the space
	 * 
	 */
	public DefaultGrid(GridPointTranslator translator,
			CellAccessor<T, Map<GridPoint, Object>> accessor, int... size) {
		super(translator, accessor, size);
	}

	/**
	 * 
	 * @param name
	 *            the name of the grid
	 * @param size
	 *            the size of the space
	 * 
	 */
	public DefaultGrid(int... size) {
		super( new StrictBorders(), new MultiOccupancyCellAccessor<T>(),
				size);

	}

	@Override
	protected Map<GridPoint, Object> createLocationStorage() {
		return new FastMap<GridPoint, Object>();
	}
	
	public Map<T, PointHolder>getLocationMap(){
		return agentLocationMap;
	}

	@Override
	public Map<GridPoint, Object> getLocationStorage() {
		// TODO Auto-generated method stub
		return locationStorage;
	}
	
}
