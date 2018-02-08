package simbigraph.grid.pseudogrid;
/*package simbiggraph.quasigrid;


import java.util.Arrays;
import java.util.List;

*//**
 * Represents an n-dimensional point in a space.
 * 
 * @author Nick Collier
 * @author Eugine Eudine
 *//*
public class QuasiGridPoint {

	// this is package protected so we can use it directly
	//int[] point;
	List point;
	float loc[];
	
	
	*//**
	 * Creates a NdPoint from the passed in array of points.
	 * 
	 * @param point
	 *//*
	public QuasiGridPoint(List point) {
		this.point = point;
	}

	
		 * Gets the square of the euclidian distance from this point to the
		 * specified point. If the points do not have the same dimension
		 * then this returns Double.NaN
		 *
		 * @param point
		 * @return the square of the euclidian distance from this point to the
		 * specified point. If the points do not have the same dimension
		 * then this returns Double.NaN
		 
		public double getDistanceSq(QuasiGridPoint point) {
			if (point.dimensionCount() != dimensionCount()) return Double.NaN;
			double sum = 0;
			for (int i = 0, n = this.point.length; i < n; i++) {
				double diff = this.point[i] - point.point[i];
				sum += diff * diff;
			}

			return sum;

		}

		
		 * Gets the the euclidian distance from this point to the
		 * specified point. If the points do not have the same dimension
		 * then this returns Double.NaN
		 *
		 * @param point
		 * @return the euclidian distance from this point to the
		 * specified point. If the points do not have the same dimension
		 * then this returns Double.NaN
		 
		public double getDistance(QuasiGridPoint point) {
			double distanceSq = getDistanceSq(point);
			return Double.isNaN(distanceSq) ? distanceSq : Math.sqrt(distanceSq);
		}
		 

	*//**
	 * Gets the x coordinate.
	 * 
	 * @return the x coordinate.
	 *//*
	public float getX() {
		return loc[0];
	}

	*//**
	 * Gets the y coordinate.
	 * 
	 * @return the y coordinate.
	 * @throws java.lang.ArrayIndexOutOfBoundsException
	 *             if this points number of dimensions < 2
	 *//*
	public float getY() {
		return loc[1];
	}

	*//**
	 * Gets the z coordinate.
	 * 
	 * @return the z coordinate.
	 * @throws java.lang.ArrayIndexOutOfBoundsException
	 *             if this points number of dimensions < 3
	 *//*
	public float getZ() {
		return loc[2];
	}

	
	 * Gets the coordinate at the specified index. The x coordinate is at index 0, y at 1, z at 2
	 * and so on.
	 * 
	 * @param index
	 *            the index of the coordinate
	 * @return the coordinate at the specified index.
	 * @throws java.lang.ArrayIndexOutOfBoundsException
	 *             if this points number of dimensions is less than the specified index.
	 
	public int getCoord(int index) {
		return point[index];
	}

	*//**
	 * Gets the number of dimensions of this point.
	 * 
	 * @return the number of dimensions of this point.
	 *//*
	public int dimensionCount() {
		return loc.length;
	}

	
	 * Copies the point coordinates into the specified int array. If the array is null, a new one is
	 * created and returned.
	 * 
	 * @param array
	 *            the array to put the point coorindates in. If the array is null, a new one is
	 *            created and returned.
	 * @return an array containing point coordinates sizes.
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the passed in array is not the correct length.
	
	public int[] toIntArray(int[] array) {
		if (array == null) {
			array = new int[point.length];
		}
		System.arraycopy(point, 0, array, 0, point.length);
		return array;
	}
	 
	@Override
	public String toString() {
		return Arrays.toString(loc);
	}

	@Override
	public int hashCode() {
		int hashCode = 17;
		if (loc == null) {
			return hashCode;
		}
		for (float val : loc) {
			hashCode = 37 * hashCode + val;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof QuasiGridPoint))
			return false;
		QuasiGridPoint other = (QuasiGridPoint) obj;
		if (other.point.size() != this.point.size())
			return false;
		for (int i = 0; i < this.point.size(); i++) {
			if (point.get(i) != other.point[i])
				return false;
		}

		return true;
	}
}
*/