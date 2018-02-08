package simbigraph.gui;
import javax.swing.*;
/**
 * 
 * @version $Revision$ $Date$
 * 
 * Class of progress dialog frame
 */ 
class ProgressDialog extends JDialog
{
	private static final long serialVersionUID = 1L;

	public ProgressDialog(String header)
	{
		setTitle("Progress Information");
		setAlwaysOnTop(true);
    	setResizable(false);
    	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    	/*setLayout(new java.awt.GridLayout(0, 1));
    	
    	JLabel jLabel = new JLabel();
    	jLabel.setText(header);
    	jLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
    	add(jLabel);
    	
		JProgressBar jProgressBar = new JProgressBar();
		jProgressBar.setIndeterminate(true);
        jProgressBar.setToolTipText("Progress Information");
        
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new java.awt.GridLayout(0, 1));
        jPanel.setBorder(new EmptyBorder(5, 15, 15, 15));
        jPanel.add(jProgressBar);
        
        add(jPanel);*/
    	
    	JProgressBar jProgressBar = new JProgressBar();
		jProgressBar.setIndeterminate(true);
        jProgressBar.setToolTipText("Progress Information");
    	
    	JOptionPane jOptionPane1 = new JOptionPane();
    	Object[] mes = {header, " ", jProgressBar};
    	jOptionPane1.setMessage(mes);
    	jOptionPane1.setMessageType(JOptionPane.INFORMATION_MESSAGE);
    	Object[] opt = {new JLabel()};
        jOptionPane1.setOptions(opt);
        
        add(jOptionPane1);
        //pack();
        setSize(400, 160);
        setLocationRelativeTo(null);
        setVisible(true);
	}
}
