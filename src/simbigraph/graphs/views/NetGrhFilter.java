package simbigraph.graphs.views;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class NetGrhFilter extends FileFilter {
	File f = null;
	@Override
	public boolean accept(File f)
	{
		this.f = f;
		return f.getName().toLowerCase().endsWith(".net") ||
				f.getName().toLowerCase().endsWith(".grh") ||
				f.getName().toLowerCase().endsWith(".shpx") ||
				f.isDirectory();
	}
	@Override
	public String getDescription()
	{
		return "based simbigraph graph (*.net,.*.grh,*.xml,*.shpx)";
	}

}
