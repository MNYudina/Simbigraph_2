package simbigraph.gui;
import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

import simbigraph.projections.Projection;


public class PropertiesPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	JPanel jPanelHeader;
	JPanel jPanelProperties;
	JPanel jPanelEvents;
	
	public void removeAll()
    {
		if (jPanelHeader != null)
		{
			remove(jPanelHeader);
		}
		jPanelProperties.removeAll();
		jPanelEvents.removeAll();
		validate();
	}
	
	//String name;
	String type;
	JLabel jLabelHeaderName;
	public void setHeaderName(String name)
    {
		jLabelHeaderName.setText("Properties: <" + type + "> ");
		//this.name = name;
    }

	public void addHeader( String type)
    {
		//this.name = name;
		this.type = type;
		jLabelHeaderName = new JLabel();
		jLabelHeaderName.setText("Properties: <" + type + "> " );
		jLabelHeaderName.setFont(new Font("Arial", Font.BOLD, 14));
		jLabelHeaderName.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		jLabelHeaderName.setIcon(new ImageIcon(getClass().getResource("/images/IconProperty.png")));
		jLabelHeaderName.setForeground(Color.white);
		
		jPanelHeader = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g)
			{
				//super.paintComponent(g);
				Graphics2D g2d = (Graphics2D)g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				//g2d.setPaint(new GradientPaint(new Point(getLocation().x, getLocation().y), new Color(255, 255, 255), new Point(getLocation().x, getLocation().y + getHeight()), new Color(79, 129, 189), true));
				Point2D start = new Point2D.Float(getLocation().x, getLocation().y);
				Point2D end = new Point2D.Float(getLocation().x, getLocation().y + getHeight());
				//float[] dist = {0.0f, 0.65f, 1.0f};
				//Color[] colors = {new Color(229, 238, 255), new Color(191, 213, 255), new Color(163, 196, 255)};
				float[] dist = {0.0f, 0.49f, 0.50f, 0.95f, 1.0f};
				Color[] colors = {new Color(80, 80, 80), new Color(89, 89, 89), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)};
				g2d.setPaint(new LinearGradientPaint(start, end, dist, colors));
				g2d.fillRect(getLocation().x, getLocation().y, getWidth(), getHeight());
			}
		};
		jPanelHeader.setLayout(new GridLayout(0, 1));
		//jPanel_.setBorder(BorderFactory.createMatteBorder(2, 10, 2, 2, new Color(31, 73, 125)));
		jPanelHeader.setBackground(Color.white);
		jPanelHeader.add(jLabelHeaderName);
		
		//jPanelProperties.add(jPanel_);
		//jPanelProperties.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		jPanelHeader.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		add("North", jPanelHeader);
		
        validate();
    }
	
	public void addRows(Object selectedObject)
	{
		try
		{
        	removeAll();
        	if (selectedObject instanceof Projection)
        	{
        		Projection selectedNode = (Projection)selectedObject;
        		addHeader(selectedNode.getType());
        		
        		JPanel j = new JPanel();
        		j.setLayout(new BoxLayout(j, BoxLayout.PAGE_AXIS));
        		jPanelProperties.add(j);
        		
        		for (int ii = 0; ii < selectedNode.getProperties().size(); ii++)
                {
        			if (!selectedNode.getProperties().get(ii)[4].equalsIgnoreCase(""))
        			{
        				if (selectedNode.getProperties().get(ii).length == 6)
            			{
        					int bor = 0;
        					int band = 0;
        					int bandc = 0;
        					String[] st = selectedNode.getProperties().get(ii)[5].split(",");
        					for (int a = 0; a < st.length; a++)
        					{
        						if (st[a].contains("@"))
        						{
        							bandc++;
        							String[] st2 = st[a].split("@");
            						if (selectedNode.getProperty(st2[0]).equalsIgnoreCase(st2[1]))
            						{
            							band++;
            						}
        						}
        						
        						if (st[a].contains("="))
        						{
        							String[] st2 = st[a].split("=");
            						if (selectedNode.getProperty(st2[0]).equalsIgnoreCase(st2[1]))
            						{
            							bor++;
            						}
        						}
        					}
        					if ((bandc > 0 && band == bandc && bor > 0) || (bandc == 0 && bor > 0))
        					{
        						j.add(new PropertiesRow(this, selectedNode, selectedNode.getProperties().get(ii)[1], selectedNode.getProperties().get(ii)[3], selectedNode.getProperties().get(ii)[2], selectedNode.getProperties().get(ii)[0], selectedNode.getProperties().get(ii)[4]));
        					}
            			}
        				else
        				{
        					j.add(new PropertiesRow(this, selectedNode, selectedNode.getProperties().get(ii)[1], selectedNode.getProperties().get(ii)[3], selectedNode.getProperties().get(ii)[2], selectedNode.getProperties().get(ii)[0], selectedNode.getProperties().get(ii)[4]));
        				}
        			}
        		}
        		j = new JPanel();
        		j.setLayout(new BoxLayout(j, BoxLayout.PAGE_AXIS));
        		jPanelEvents.add(j);
        		
        		for (int ii = 0; ii < selectedNode.getEvents().size(); ii++)
                {
            		j.add(new PropertiesRow(this, selectedNode, selectedNode.getEvents().get(ii)[0], selectedNode.getEvents().get(ii)[0], selectedNode.getEvents().get(ii)[1], "", "textpane"));
        		}
        		validate();
        	}
		}
        catch (Exception e)
        {
        	System.out.println("error");
        }
	}
	
	public PropertiesPanel()
	{
		super();
		setLayout(new BorderLayout());
		
		JTabbedPane jTabbedPane = new JTabbedPane();
		jTabbedPane.setTabPlacement(JTabbedPane.LEFT);
		//jTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add("Center", jTabbedPane);

		
		jPanelProperties = new JPanel();
		jPanelProperties.setLayout(new GridLayout(0, 1));
		jPanelProperties.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BorderLayout());
		jPanel.add("North", jPanelProperties);
		
		JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(jPanel);
		jTabbedPane.addTab("", new ImageIcon(getClass().getResource("/images/IconProperties.png")), jScrollPane);

		jPanelEvents = new JPanel();
		jPanelEvents.setLayout(new GridLayout(0, 1));
		jPanelEvents.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

		
		JPanel jPanel_ = new JPanel();
		jPanel_.setLayout(new BorderLayout());
		jPanel_.add("North", jPanelEvents);
		
		JScrollPane jScrollPane_ = new JScrollPane();
        jScrollPane_.setViewportView(jPanel_);
		
		jTabbedPane.addTab("", new ImageIcon(getClass().getResource("/images/IconEvent.png")), jScrollPane_);
	}
}
