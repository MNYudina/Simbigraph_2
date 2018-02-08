package simbigraph.gui;

import javax.swing.*;
import javax.swing.tree.*;
/**
 * 
 * @author Eugene Ershov
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 * Defines the view and type elements (network structures) witch
 * located in the tree at one of splin pane of the main frame)
 * 
 */
public class CustomTreeNode extends DefaultMutableTreeNode {

	private ImageIcon imageIcon = null;
    public void setImageIcon(ImageIcon value)
    {
    	imageIcon = value;
    }
    public ImageIcon getImageIcon()
    {
    	return imageIcon;
    }
    
    private String nodeType = "";
    public void setNodeType(String value)
    {
    	nodeType = value;
    }
    public String getNodeType()
    {
    	return nodeType;
    }
	
	public CustomTreeNode() {
	}

	public CustomTreeNode(Object arg0) {
		super(arg0);
	}
	
	public CustomTreeNode(Object arg0, String nodeType) {
		super(arg0);
		this.nodeType = nodeType;
		try
		{
			this.imageIcon = new ImageIcon(getClass().getResource("../../images/Icon" + nodeType.replace(" ", "") + ".png"));
		}
		catch (Exception e)
		{}
	}
	
	public CustomTreeNode(Object arg0, String nodeType, ImageIcon imageIcon) {
		super(arg0);
		this.nodeType = nodeType;
		this.imageIcon = imageIcon;
	}
	
	public CustomTreeNode(Object arg0, ImageIcon imageIcon) {
		super(arg0);
		this.imageIcon = imageIcon;
	}

	public CustomTreeNode(Object arg0, boolean arg1) {
		super(arg0, arg1);
	}

}
