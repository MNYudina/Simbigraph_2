package simbigraph.gui;
import java.io.File;
import javax.swing.filechooser.*;
/**
 * 
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 * Defines the extension of save file project (File Filter)
 */
public class SbdFilter extends FileFilter
{
	File f = null;;
	public boolean accept(File f)
	{
		this.f = f;
		return f.getName().toLowerCase().endsWith(".sbg") ||
				f.getName().toLowerCase().endsWith(".sbg") ||
				f.isDirectory();
	}
	public String getDescription()
	{
		return "simbigraph project files (*.sbg)";
	}
}
