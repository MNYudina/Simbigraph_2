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

public class MetricsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MetricsDialog dialog = new MetricsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MetricsDialog() {
		setAlwaysOnTop(true);
		setResizable(false);
		setModal(true);
		setTitle("Motifs statistics");
		setBounds(100, 100, 706, 329);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Algotithm", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 207, 660, 91);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(100);
		progressBar.setStringPainted(true);
		progressBar.setBounds(172, 24, 199, 14);
		panel_1.add(progressBar);
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
		
		JLabel lblTotalProgress = new JLabel("Total Progress");
		lblTotalProgress.setBounds(75, 22, 87, 14);
		panel_1.add(lblTotalProgress);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Results", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(10, 11, 658, 184);
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
		txtpnNetworkNameCemailemailnet.setText("Network name: p2p-Gnutella31.net\r\nNumber of verticies: 62586\r\nNumber of edges: 147892\r\nDiameter: 11\r\nCalculation time: 3 min 2 sec\r\n");
		txtpnNetworkNameCemailemailnet.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(txtpnNetworkNameCemailemailnet);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}
}
