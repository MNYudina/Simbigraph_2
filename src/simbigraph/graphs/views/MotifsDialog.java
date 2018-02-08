package simbigraph.graphs.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JProgressBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Font;

public class MotifsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSpinner spinner;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MotifsDialog dialog = new MotifsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MotifsDialog() {
		setAlwaysOnTop(true);
		setResizable(false);
		setModal(true);
		setTitle("Motifs statistics");
		setBounds(100, 100, 706, 441);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Algotithms options", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(29, 22, 658, 114);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JRadioButton rdbtnEnumeration = new JRadioButton("Full Enumiration");
		rdbtnEnumeration.setSelected(true);
		rdbtnEnumeration.setBounds(82, 43, 156, 23);
		panel.add(rdbtnEnumeration);
		
		JRadioButton rdbtnSamplingCount = new JRadioButton("Sampling Count");
		rdbtnSamplingCount.setBounds(82, 69, 156, 23);
		panel.add(rdbtnSamplingCount);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(4, 3, 4, 1));
		spinner.setBounds(124, 16, 38, 20);
		panel.add(spinner);
		
		JCheckBox AccuracyCheck = new JCheckBox("AccuracyCheck");
		AccuracyCheck.setEnabled(false);
		AccuracyCheck.setBounds(368, 43, 196, 23);
		panel.add(AccuracyCheck);
		
		JCheckBox chckbxDirGraph = new JCheckBox("Directed Graph");
		chckbxDirGraph.setSelected(true);
		chckbxDirGraph.setBounds(368, 15, 169, 23);
		panel.add(chckbxDirGraph);
		
		JLabel lblSubgraphSize = new JLabel("Subgraph Size");
		lblSubgraphSize.setBounds(20, 19, 94, 14);
		panel.add(lblSubgraphSize);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Algotithm", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(29, 133, 660, 91);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(100);
		progressBar.setStringPainted(true);
		progressBar.setBounds(172, 24, 199, 14);
		panel_1.add(progressBar);
		
		JLabel lblTotalProgress = new JLabel("Total Progress");
		lblTotalProgress.setBounds(85, 22, 87, 14);
		panel_1.add(lblTotalProgress);
		{
			JButton runButton = new JButton("RUN");
			runButton.setBounds(527, 57, 89, 23);
			panel_1.add(runButton);
			runButton.setActionCommand("Cancel");
		}
		
		JLabel lblNumberOfThreads = new JLabel("Number of threads");
		lblNumberOfThreads.setBounds(54, 61, 118, 14);
		panel_1.add(lblNumberOfThreads);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(8, 1, 16, 1));
		spinner_1.setBounds(172, 61, 43, 14);
		panel_1.add(spinner_1);
		
		JCheckBox chckbxAbsolute = new JCheckBox("Absolute values");
		chckbxAbsolute.setSelected(true);
		chckbxAbsolute.setBounds(322, 57, 174, 23);
		panel_1.add(chckbxAbsolute);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Results", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(29, 221, 658, 184);
		contentPanel.add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setBounds(10, 45, 89, 23);
		panel_2.add(btnLoad);
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(10, 79, 89, 23);
		panel_2.add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(131, 11, 499, 151);
		panel_2.add(scrollPane);
		
		JTextPane txtpnNetworkNameCemailemailnet = new JTextPane();
		txtpnNetworkNameCemailemailnet.setFont(new Font("Tahoma", Font.PLAIN, 9));
		txtpnNetworkNameCemailemailnet.setText("Network name: C:\\\\email\\Email.net\t\t\r\nNumber of nodes: 265215,\tNumber of edges: 364481\t\t\r\nNumber of single edges: 310006, Number of mutual edges: 54475\t\t\r\nSubgraphs:\r\nNA\r\nNA\r\nNA\r\n1,93466E+11\r\nNA\r\nNA\r\nNA\r\n6456245202\r\n13418424770\r\nNA\r\nNA\r\nNA\r\n572077970\r\n687418593\r\n17530533\r\nNA\r\n889540098\r\n14410349\r\n49462929\r\n2223607\r\n785055\r\n1078335\r\nNA\r\nNA\r\n776368511\r\n2299330646\r\n2531165883\r\nNA\r\nNA\r\n69159001");
		scrollPane.setViewportView(txtpnNetworkNameCemailemailnet);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}
}
