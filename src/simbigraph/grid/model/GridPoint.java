package simbigraph.grid.model;


import java.util.Arrays;

/**
 * Represents an n-dimensional point in a space.
 * 
 * @author Nick Collier
 */
public class GridPoint {

	// this is package protected so we can use it directly
	int[] point;

	/**
	 * Creates a NdPoint from the passed in array of points.
	 * 
	 * @param point
	 */
	public GridPoint(int... point) {
		this.point = point;
	}

	/**
		 * Gets the square of the euclidian distance from this point to the
		 * specified point. If the points do not have the same dimension
		 * then this returns Double.NaN
		 *
		 * @param point
		 * @return the square of the euclidian distance from this point to the
		 * specified point. If the points do not have the same dimension
		 * then this returns Double.NaN
		 */
		public double getDistanceSq(GridPoint point) {
			if (point.dimensionCount() != dimensionCount()) return Double.NaN;
			double sum = 0;
			for (int i = 0, n = this.point.length; i < n; i++) {
				double diff = this.point[i] - point.point[i];
				sum += diff * diff;
			}

			return sum;

		}

		/**
		 * Gets the the euclidian distance from this point to the
		 * specified point. If the points do not have the same dimension
		 * then this returns Double.NaN
		 *
		 * @param point
		 * @return the euclidian distance from this point to the
		 * specified point. If the points do not have the same dimension
		 * then this returns Double.NaN
		 */
		public double getDistance(GridPoint point) {
			double distanceSq = getDistanceSq(point);
			return Double.isNaN(distanceSq) ? distanceSq : Math.sqrt(distanceSq);
		}


	/**
	 * Gets the x coordinate.
	 * 
	 * @return the x coordinate.
	 */
	public int getX() {
		return point[0];
	}

	/**
	 * Gets the y coordinate.
	 * 
	 * @return the y coordinate.
	 * @throws java.lang.ArrayIndexOutOfBoundsException
	 *             if this points number of dimensions < 2
	 */
	public int getY() {
		return point[1];
	}

	/**
	 * Gets the z coordinate.
	 * 
	 * @return the z coordinate.
	 * @throws java.lang.ArrayIndexOutOfBoundsException
	 *             if this points number of dimensions < 3
	 */
	public int getZ() {
		return point[2];
	}

	/**
	 * Gets the coordinate at the specified index. The x coordinate is at index 0, y at 1, z at 2
	 * and so on.
	 * 
	 * @param index
	 *            the index of the coordinate
	 * @return the coordinate at the specified index.
	 * @throws java.lang.ArrayIndexOutOfBoundsException
	 *             if this points number of dimensions is less than the specified index.
	 */
	public int getCoord(int index) {
		return point[index];
	}

	/**
	 * Gets the number of dimensions of this point.
	 * 
	 * @return the number of dimensions of this point.
	 */
	public int dimensionCount() {
		return point.length;
	}

	/**
	 * Copies the point coordinates into the specified int array. If the array is null, a new one is
	 * created and returned.
	 * 
	 * @param array
	 *            the array to put the point coorindates in. If the array is null, a new one is
	 *            created and returned.
	 * @return an array containing point coordinates sizes.
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the passed in array is not the correct length.
	 */
	public int[] toIntArray(int[] array) {
		if (array == null) {
			array = new int[point.length];
		}
		System.arraycopy(point, 0, array, 0, point.length);
		return array;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(point);
	}

	@Override
	public int hashCode() {
		int hashCode = 17;
		if (point == null) {
			return hashCode;
		}
		for (int val : point) {
			hashCode = 37 * hashCode + val;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof GridPoint))
			return false;
		GridPoint other = (GridPoint) obj;
		if (other.point.length != this.point.length)
			return false;
		for (int i = 0; i < this.point.length; i++) {
			if (point[i] != other.point[i])
				return false;
		}

		return true;
	}
}
