package simbigraph.gui;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
/**
 * 
 * @author Eugene Ershov
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 * 
 */

class ButtonTabComponent extends JPanel
{
	private static final long serialVersionUID = 1L;
	private final JTabbedPane pane;

    public ButtonTabComponent(final JTabbedPane pane, ImageIcon imageIcon, boolean showCloseButton)
    {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null)
        {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);
        
        //make JLabel read titles from JTabbedPane
        JLabel label = new JLabel()
        {
        	private static final long serialVersionUID = 1L;
        	public String getText()
            {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1)
                {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        
        add(label);
        //add more space between the label and the button
        //label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        label.setIcon(imageIcon);
        //tab button
        if (showCloseButton)
        {
        	label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        	JButton button = new TabButton();
            add(button);
            
            //JButton button1 = new TabButton();
            //add(button1);
        }
        //add more space to the top of the component
        //setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabButton extends JButton implements ActionListener
    {
    	private static final long serialVersionUID = 1L;
    	public TabButton()
        {
            int size = 15;
            setPreferredSize(new java.awt.Dimension(size, size));
            setToolTipText("Close");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            //setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e)
        {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1)
            {
                pane.remove(i);
            }
        }

        //we don't want to update UI for this button
        public void updateUI()
        {
        }

        //paint the cross
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            //g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            //shift the image for pressed buttons
            if (getModel().isPressed())
            {
                g2.translate(2, 2);
            }
            else
            {
                g2.translate(1, 1);
            }
            	
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover())
            {
                g2.setColor(new Color(192, 0, 0));
            }
            int delta = 4;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    private final MouseListener buttonMouseListener = new MouseAdapter()
    {
        public void mouseEntered(MouseEvent e)
        {
            Component component = e.getComponent();
            if (component instanceof AbstractButton)
            {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e)
        {
            Component component = e.getComponent();
            if (component instanceof AbstractButton)
            {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}