package simbigraph.gui;
import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

class TreeIconRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;
	ImageIcon defaultIcon;
	ImageIcon specialIcon;

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		Object nodeObj = ((CustomTreeNode) value).getUserObject();
		return this;
	}
}