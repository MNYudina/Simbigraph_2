package simbigraph.grid.triangle;
/**
 * Projection descriptor for grids.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class GridProjectionDescriptor extends AbstractProjectionDescriptor {

  public GridProjectionDescriptor(ProjectionData proj) {
    super(proj);
    if (proj.getType() != ProjectionType.GRID )
      throw new IllegalArgumentException("Projection is not a grid");
  }

  /**
   * Gets the implied 3d layout of the projection, if any.
   * 
   * @return the implied layout of the projection, if any.
   */
  public String getImpliedLayout3D() {
    return Grid3DLayout.class.getName();
  }

  /**
   * Gets the implied 2d layout of the projection, if any.
   * 
   * @return the implied layout of the projection, if any.
   */
  public String getImpliedLayout2D() {
    return Grid2DLayout.class.getName();
  }

}
