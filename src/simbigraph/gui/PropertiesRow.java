package simbigraph.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel; //import javax.swing.text.*;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection; 

import simbigraph.core.Context;
import simbigraph.graphs.decomposition.SimbigraphNode;
import simbigraph.graphs.prefAttachment.DecompEditGraphPanel;
import simbigraph.graphs.views.NetGrhFilter;
import simbigraph.projections.PAttachDetermGraphProjection;
import simbigraph.projections.PAttachStohasticGraphProjection;
import simbigraph.projections.ProjGraphPA;
import simbigraph.projections.Projection;
import simbigraph.util.Statistic;
import temp.CubeInteractionSimulation;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.io.PajekNetWriter;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;


public class PropertiesRow extends JPanel {
	
	private JCheckBox checkbox;
	static boolean modeStohR=true;//определяет режим калибровки
	private static final long serialVersionUID = 1L;
	private JLabel jLabelPropertyName;
	private JTextField jTextFieldPropertyValue;
	private JTextPane jTextPanePropertyValue;
	private JButton jButtonPropertyValue;
	private JTextPane jTextPanePropertyValueML;
	private JCheckBox jCheckBoxPropertyValue;
	private JComboBox jComboBoxPropertyValue;
	private JSpinner jSpinnerPropertyValue;
	private JSpinner.NumberEditor jSpinnerNumberEditor;

	private PropertiesPanel propertiesPanel = null;
	private String propertyName = "";
	private String propertyValue = "";
	private Object selectedObject = null;
	private String name = "";

	private JTable jTablePropertyValue;
	private JSpinner jSpinnerX;
	private JSpinner jSpinnerY;
	boolean b = false;

	public PropertiesRow(final PropertiesPanel propertiesPanel,
			final Object selectedObject, final String propertyName,
			String propertyName_, String propertyValue, String propertyType,
			String controlType) {
		this.propertiesPanel = propertiesPanel;
		this.selectedObject = selectedObject;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;

		int lse = 10;
		int lsp = 10;

		if (selectedObject instanceof Projection) {
			Projection selectedNode = (Projection) selectedObject;
			for (int i = 0; i < selectedNode.getProperties().size(); i++) {
				JLabel jl = new JLabel(selectedNode.getProperties().get(i)[1]);
				if (jl.getPreferredSize().width > lsp) {
					lsp = jl.getPreferredSize().width;
				}
			}
			for (int i = 0; i < selectedNode.getEvents().size(); i++) {
				JLabel jl = new JLabel(selectedNode.getEvents().get(i)[0]);
				if (jl.getPreferredSize().width > lse) {
					lse = jl.getPreferredSize().width;
				}
			}
		}
		setLayout(new BorderLayout());

		setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

		jLabelPropertyName = new JLabel();
		jLabelPropertyName.setText(propertyName_);
		jLabelPropertyName.setVerticalAlignment(SwingConstants.TOP);
		String s = propertyName.substring(0, 1);
		if (s.equals(s.toLowerCase())) {
			jLabelPropertyName.setIcon(new ImageIcon(getClass().getResource(
					"/images/IconEvent.png")));
			jLabelPropertyName.setPreferredSize(new Dimension(lse + 40, 22));
		} else {
			jLabelPropertyName.setIcon(new ImageIcon(getClass().getResource(
					"/images/IconProperty.png")));
			jLabelPropertyName.setPreferredSize(new Dimension(lsp + 40, 22));
		}
		if (propertyType != "") {
			jLabelPropertyName.setToolTipText(propertyType);
		}
		jLabelPropertyName.setBorder(BorderFactory.createEmptyBorder(6, 0, 0,
				10));

		JPanel j = new JPanel();
		j.setLayout(new BorderLayout());
		j.add("North", jLabelPropertyName);

		add("West", j);

		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.equalsIgnoreCase("textfield")) {

			jTextPanePropertyValue = new JTextPane();

			// jTextPanePropertyValue.setMaximumSize(new Dimension(400,
			// jTextPanePropertyValue.getPreferredSize().height));

			if (propertyType != "") {
				jTextPanePropertyValue.setToolTipText(propertyType);
			}
			JavaDocument jd = new JavaDocument();
			jd.setSingleLine(true);
			jTextPanePropertyValue.setDocument(jd);
			jTextPanePropertyValue.setFont(new Font(Font.MONOSPACED,
					Font.PLAIN, 14));
			jTextPanePropertyValue.setMargin(new Insets(-3, -2, -3, -2));
			jTextPanePropertyValue
					.addKeyListener(new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							jTextPanePropertyValueKeyReleased(evt);
						}
					});
			jTextPanePropertyValue.setText(propertyValue);

			JScrollPane jScrollPane = new JScrollPane();
			// jScrollPane.setMaximumSize(new Dimension(400,
			// jTextPanePropertyValue.getPreferredSize().height));
			jScrollPane.setViewportView(jTextPanePropertyValue);
			jScrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			jScrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
			add("Center", jPanel);

			jPanel.add(jScrollPane);
		}

		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.equalsIgnoreCase("button")) {

			JButton jButton = new JButton();
			jButton.setText("...");
			/*jButton.setIcon(new ImageIcon(getClass().getResource(
			"/images/IconAdd.jpg")));*/

			jButton.setVerticalAlignment(SwingConstants.TOP);
			jButton.setToolTipText("Add new item...");
			jButton.setPreferredSize(new Dimension(25, 25));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				int countItem = 0;

				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Projection selectedNode = (Projection) selectedObject;
					String[] butt = selectedNode.getProperties().get(
							selectedNode.getProperties().size() - 1);
					selectedNode.getProperties().remove(butt);

					String[] panelDecompItems = { "dec",
							"DecompItems" + countItem++, "", "Item", "itemlist" };
					selectedNode.getProperties().add(panelDecompItems);

					selectedNode.getProperties().add(butt);
					update();
					propertiesPanel.addRows((Projection) selectedObject);
				}
			});
			Box box = Box.createHorizontalBox();
			box.add(jButton);
			add(box);
		}
		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.equalsIgnoreCase("button2")) {
			JButton jButton = new JButton();
			jButton.setText("...");
			jButton.setVerticalAlignment(SwingConstants.TOP);
			jButton.setToolTipText("Open java code editor...");
			jButton.setPreferredSize(new Dimension(25, 25));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				int countItem = 0;

				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Projection selectedNode = (Projection) selectedObject;
					String[] butt = selectedNode.getProperties().get(
							selectedNode.getProperties().size() - 1);
					selectedNode.getProperties().remove(butt);

					String[] panelDecompItems = { "seed",
							"SeedItem" + countItem++, "0.1,0.1", "Item",
							"seedlist:0.01,0.99" };
					selectedNode.getProperties().add(panelDecompItems);

					selectedNode.getProperties().add(butt);
					update();
					propertiesPanel.addRows((Projection) selectedObject);
				}
			});
			Box box = Box.createHorizontalBox();
			box.add(jButton);
			add(box);
		}

		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.equalsIgnoreCase("calibrate")) {
			final ProjGraphPA pr = ((ProjGraphPA) selectedObject);
			// /-------------------------------------------------------------------------
			jTextPanePropertyValue = new JTextPane();
			if (propertyType != "") {
				jTextPanePropertyValue.setToolTipText(propertyType);
			}
			jTextPanePropertyValue.setFont(new Font(Font.MONOSPACED,
					Font.PLAIN, 14));
			jTextPanePropertyValue.setMargin(new Insets(-3, -2, -3, -2));
			jTextPanePropertyValue
					.addKeyListener(new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							jTextPanePropertyValueKeyReleased(evt);
						}
					});
			jTextPanePropertyValue.setText(propertyValue);

			jButtonPropertyValue = new JButton();
			jButtonPropertyValue.setText("...");
			jButtonPropertyValue.setVerticalAlignment(SwingConstants.TOP);
			jButtonPropertyValue.setToolTipText("Open file chooser...");
			jButtonPropertyValue.setPreferredSize(new Dimension(25, 25));
			jButtonPropertyValue
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							jFileChooserValueActionPerformed(evt);
							pr.f = null;
							System.gc();
							//jTextPanePropertyValueKeyReleased(null);
							//update();

							doUpd(pr);
							
						

						}
					});

			j = new JPanel();
			j.setLayout(new BorderLayout());
			j.add("North", jButtonPropertyValue);

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add("Center", jTextPanePropertyValue);
			jPanel.add("East", j);
			add("Center", jPanel);

			Box box2 = Box.createVerticalBox();
			box2.add(jPanel);
			JPanel pan = new JPanel();
			pan.setLayout(new BorderLayout());
			JButton btn = new JButton("  Calibrate  ");
			final JButton btn2 = new JButton("Using regression");

			if (pr.f != null) {
				btn2.setEnabled(true);
			} else {
				btn2.setEnabled(false);
			}
			btn2.validate();
			btn2.addActionListener(new ActionListener() {
				private void heightConstrain(Component component) {
					Dimension d = new Dimension(
							component.getMaximumSize().width, component
									.getMinimumSize().height);
					component.setMaximumSize(d);
				}

				@Override
				public void actionPerformed(ActionEvent e) {
					final JFrame frame = new JFrame("Approximation of PA function");
					final ProjGraphPA pr = ((ProjGraphPA) selectedObject);
					final double f[] = pr.f;

					final XYSeries series1 = new XYSeries("Real");
					for (int i = 1; i < f.length; i++)
						series1.add((double) i, (double) f[i]);

					final XYSeriesCollection dataset = new XYSeriesCollection();
					dataset.addSeries(series1);

					JFreeChart chart = ChartFactory.createXYLineChart("", "k",
							"f(k)", dataset, PlotOrientation.VERTICAL, true,
							true, false);
					final XYPlot plot = chart.getXYPlot();
					final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
					renderer.setSeriesLinesVisible(0, false);
					plot.setRenderer(renderer);

					final NumberAxis rangeAxis = (NumberAxis) plot
							.getRangeAxis();
					rangeAxis.setStandardTickUnits(NumberAxis
							.createIntegerTickUnits());

					final ChartPanel chartPanel = new ChartPanel(chart);

					String[] items = { "A*k+B", "exp(A*k+b)", "A*k^B",
							"A+Ln(k)" };
					final JComboBox cb = new JComboBox(items);
					final DefaultTableModel dm = new DefaultTableModel(0, 0) {
						@Override
						public void setValueAt(Object value, int row, int column) {
							if (column == 1) {
								try {
									super.setValueAt(Double.valueOf(value
											.toString()), row, column);
									super.setValueAt(value, row, column);
								} catch (Exception re) {
									// super.setValueAt(new Double(0.1), row,
									// column);
								} finally {
								}
							}
						}
					};
					final DefaultTableModel dm2 = new DefaultTableModel(0, 0) {
						@Override
						public void setValueAt(Object value, int row, int column) {
							if (column == 1) {
								try {
									super.setValueAt(Double.valueOf(value
											.toString()), row, column);
									super.setValueAt(value, row, column);
								} catch (Exception re) {
									// super.setValueAt(new Double(0.1), row,
									// column);
								} finally {
								}
							}
						}
					};
					dm.addColumn(" k ");
					dm.addColumn(" f(k)");
					dm2.addColumn(" k ");
					dm2.addColumn(" f(k)");

					for (int k = 0; k < f.length; k++) {
						dm.addRow(new Object[] { Integer.valueOf(k),
								Double.valueOf(f[k]) });
					}

					final JTable jTable = new JTable(dm);
					jTable
							.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

					jTable.getTableHeader().setReorderingAllowed(false);
					JScrollPane jScrollPane = new JScrollPane(jTable);
					jScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, jTable
							.getTableHeader());
					jTable.setPreferredScrollableViewportSize(new Dimension(
							150, 50));

					final JTable jTable2 = new JTable(dm2);
					jTable2.getTableHeader().setReorderingAllowed(false);
					jScrollPane.setBorder(BorderFactory
							.createTitledBorder("use in trend"));

					JScrollPane jScrollPane2 = new JScrollPane(jTable2);
					jScrollPane2.setCorner(JScrollPane.UPPER_LEFT_CORNER,
							jTable.getTableHeader());
					jTable2.setPreferredScrollableViewportSize(new Dimension(
							150, 50));
					jScrollPane2.setBorder(BorderFactory
							.createTitledBorder("use as static value"));

					Box controls = Box.createVerticalBox();
					Box box4 = Box.createHorizontalBox();
					box4.add(jScrollPane);
					box4.add(Box.createHorizontalStrut(4));
					Box box5 = Box.createVerticalBox();
					JButton restore = new JButton("   Restore   All  ");
					restore.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							DefaultTableModel model2 = ((DefaultTableModel) jTable2
									.getModel());
							int m2 = model2.getRowCount();
							for (int i = m2 - 1; i >= 0; i--)
								model2.removeRow(i);

							DefaultTableModel model = ((DefaultTableModel) jTable
									.getModel());
							int m = model.getRowCount();
							for (int i = m - 1; i >= 0; i--)
								dm.removeRow(i);
							for (int k = 0; k < f.length; k++) {
								dm.addRow(new Object[] { Integer.valueOf(k),
										Double.valueOf(f[k]) });
							}
							series1.clear();
							for (int i = 1; i < f.length; i++)
								series1.add((double) i, (double) f[i]);
						}
					});
					JButton removeFirst = new JButton("To Static value");
					removeFirst.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							DefaultTableModel dm = ((DefaultTableModel) jTable
									.getModel());
							DefaultTableModel dm2 = ((DefaultTableModel) jTable2
									.getModel());
							int index = jTable.getSelectedRow();
							if (index == -1)
								index = 0;
							Object obj1 = dm.getValueAt(index, 0);
							Object obj2 = dm.getValueAt(index, 1);
							dm.removeRow(index);
							dm2.addRow(new Object[] { obj1, obj2 });
							series1.clear();
							for (int i = 0; i < dm.getRowCount(); i++)
								series1.add(Double.valueOf(dm.getValueAt(i, 0)
										.toString()), Double.valueOf(dm
										.getValueAt(i, 1).toString()));

						}
					});
					box5.add(Box.createVerticalStrut(10));
					box5.add(removeFirst);
					box5.add(Box.createVerticalStrut(5));
					box5.add(restore);
					box5.add(Box.createVerticalGlue());

					box4.add(box5);
					box4.add(Box.createHorizontalStrut(4));
					box4.add(jScrollPane2);
					box4.add(Box.createGlue());
					controls.add(box4);
					Dimension space = new Dimension(20, 20);
					controls.add(Box.createRigidArea(space));

					controls.add(Box.createRigidArea(space));
					JLabel l = new JLabel("Type of trend");
					JPanel pa = new JPanel(new BorderLayout());
					pa.add(l, BorderLayout.WEST);
					heightConstrain(pa);
					controls.add(pa);
					controls.add(Box.createVerticalStrut(5));

					heightConstrain(cb);
					controls.add(cb);

					controls.add(Box.createRigidArea(space));

					JPanel operation2 = new JPanel(new BorderLayout());
					JButton ok1 = new JButton(" Create Trend   ");
					final JButton ok2 = new JButton("   Save Trend   ");
					final JLabel lastTrend = new JLabel("Not created trend now");
					ok1.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							int count = dm.getRowCount();
							double[][] mass = new double[count][2];
							for (int i = 0; i < count; i++) {
								mass[i][0] = Double.valueOf(dm.getValueAt(i, 0)
										.toString());
								mass[i][1] = Double.valueOf(dm.getValueAt(i, 1)
										.toString());
							}

							double[] d_trand = null;
							if (cb.getSelectedItem().toString().equals("A*k+B")) {
								d_trand = getLinearTrendCoef(mass);
								String znak = "+";
								if (d_trand[1] < 0.)
									znak = "";
								name = "" + d_trand[0] + "*k" + znak
										+ d_trand[1];
								lastTrend.setText("Current trend: f=" + name);

								final XYSeries series2 = new XYSeries(name);
								for (int i = 0; i < count; i++)
									series2.add((double) i, d_trand[0] * i
											+ d_trand[1]);
								dataset.addSeries(series2);

							} else if (cb.getSelectedItem().toString().equals(
									"exp(A*k+b)")) {
								double[][] temp_m = new double[mass.length][2];
								for (int i = 0; i < count; i++) {
									if (mass[i][1] > 0) {

										temp_m[i][1] = Math.log(mass[i][1]);
										temp_m[i][0] = (mass[i][0]);

									} else {
										JOptionPane.showMessageDialog(null,
												"The f(k) must be possitive!",
												"Error!",
												JOptionPane.ERROR_MESSAGE);
										return;
									}
								}
								d_trand = getLinearTrendCoef(temp_m);
								String znak = "+";
								if (d_trand[1] < 0.)
									znak = "";
								name = "Math.exp(" + d_trand[0] + "*k+"
										+ d_trand[1] + ") ";
								lastTrend.setText("Current trend: " + "f="
										+ name);

								final XYSeries series2 = new XYSeries(name);
								for (int i = 0; i < count; i++)
									series2.add((double) i, Math.exp(d_trand[0]
											* i + d_trand[1]));
								dataset.addSeries(series2);
							} else if (cb.getSelectedItem().toString().equals(
									"A*k^B")) {
								double[][] temp_m = new double[mass.length][2];
								for (int i = 0; i < count; i++) {
									if (mass[i][1] > 0 && mass[i][0] > 0) {
										temp_m[i][1] = Math.log(mass[i][1]);
										temp_m[i][0] = Math.log(mass[i][0]);
									} else {
										JOptionPane
												.showMessageDialog(
														null,
														"Values of k and  f(k) must be possitive!",
														"Error!",
														JOptionPane.ERROR_MESSAGE);
										return;
									}
								}
								d_trand = getLinearTrendCoef(temp_m);
								String znak = "+";
								if (d_trand[1] < 0.)
									znak = "";

								name = "" + Math.exp(d_trand[1])
										+ "*Math.pow( k," + d_trand[0] + ")";
								lastTrend.setText("Current trend: " + "f="
										+ name);

								final XYSeries series2 = new XYSeries(name);

								double max = Math.exp(d_trand[0] * 2
										+ d_trand[1]);
								for (double i = 0; i < Math
										.log(temp_m.length + 1); i = i
										+ Math.log(temp_m.length)
										/ ((double) temp_m.length))
									series2.add(Math.exp((double) i), Math
											.exp(d_trand[0] * i + d_trand[1]));
								dataset.addSeries(series2);
								// renderer.setSeriesLinesVisible(dataset.getSeriesCount()-1,
								// false);
							}
						}

						private double[] getLinearTrendCoef(double[][] mass) {

							double[] koef = new double[2];
							double XY = 0., X1 = 0., Y1 = 0., X2 = 0.;
							for (int i = 0; i < mass.length; i++) {
								XY = XY + mass[i][0] * mass[i][1];
								X1 = mass[i][0] + X1;
								Y1 = mass[i][1] + Y1;
								X2 = mass[i][0] * mass[i][0] + X2;
							}
							koef[0] = (X1 * Y1 - mass.length * XY)
									/ (X1 * X1 - mass.length * X2);
							koef[1] = Y1 * 1.0 / ((double) mass.length) - X1
									* koef[0] / ((double) mass.length);

							ok2.setEnabled(true);
							return koef;
						}

					});
					ok2.setEnabled(false);
					ok2.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {

							List<String[]> list = pr.getProperties();

						/*	String str = ""
								+ "import simbigraph.graphs.prefAttachment.*;"
								+ "\r\n"
								+ "import simbigraph.gui.*;"
								+ "\r\n"
								+ "import simbigraph.core.*;"
								+ "\r\n"
								+ "public class prefRule extends PrefAttechRule{"
								+ "\r\n"
								+ "public double f(int k) {if(k<f.length)return f[k]; else return "+name+";}"
								+ "\r\n" + "public int getM() {return " +Integer.MAX_VALUE + ";}" + "\r\n"
								+ "double f[]= new double[]{" + "\r\n";*/
							
							String str = ""
								+ "import simbigraph.graphs.prefAttachment.*;"
								+ "\r\n"
								+ "import simbigraph.gui.*;"
								+ "\r\n"
								+ "import simbigraph.core.*;"
								+ "\r\n"

								+ "import java.util.Map;"
								+ "\r\n"
								+ "import java.util.HashMap;"
								+ "\r\n"
							
								+ "public class prefRule extends PrefAttechRule{"
								+ "\r\n"
								+ "Map<Integer,Double> map = new HashMap<Integer,Double>();"
								+ "\r\n" + "public  prefRule(){" + "\r\n";
							TableModel tm = jTable2.getModel();
							int rc = tm.getRowCount();
							for (int j = 0; j < rc; j++)
								str = str + "map.put(Integer.valueOf("
										+ tm.getValueAt(j, 0)
										+ "),Double.valueOf("
										+ tm.getValueAt(j, 1) + "));\r\n";

							str = str
									+ "}\r\n"
									+ "public double f(int k) {if(map.containsKey(k))return map.get(k); else return "
									+ name + ";}\r\n"
									+ "public int getM() {return "
									+ Integer.MAX_VALUE + ";}}" + "\r\n";

							// Math.pow(k, )

							String[] s = null;
							for (String[] strings : list)
								if (strings[1].equals("Code"))
									s = strings;

							int index = pr.getProperties().indexOf(s);
							pr.getProperties().remove(index);
							String[] fileNamed = { "String", "Code", str,
									"RuleClass", "textpane" };
							pr.getProperties().add(index, fileNamed);
							pr.setF(f);
							update();
							if (selectedObject instanceof Projection) {
								propertiesPanel
										.addRows((Projection) selectedObject);
							}
							frame.dispose();

						}

					});
					controls.add(Box.createVerticalStrut(5));
					Box box6 = Box.createVerticalBox();
					box6.add(Box.createVerticalStrut(10));
					box6.add(ok1);
					box6.add(Box.createVerticalStrut(5));
					box6.add(ok2);

					operation2.add(box6, BorderLayout.EAST);
					operation2.add(lastTrend, BorderLayout.NORTH);
					heightConstrain(operation2);
					controls.add(operation2);
					controls.add(Box.createRigidArea(space));

					controls.add(Box.createVerticalGlue());
					frame.add(controls, BorderLayout.EAST);

					frame.add(chartPanel);
					frame.setVisible(true);
					frame.pack();
				}
			});
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//if (pr instanceof PAttachDetermGraphProjection)
						doCalibrate2(pr);
					//if (pr instanceof PAttachStohasticGraphProjection)
					//	calibStoh(pr);
				}
			});
			Box box0 = Box.createVerticalBox();
			box0.add(Box.createVerticalStrut(8));
			Box box3 = Box.createHorizontalBox();
			box3.add(btn);
			box3.add(Box.createHorizontalStrut(5));
			box3.add(btn2);
			box0.add(box3);
			pan.add(BorderLayout.EAST, box0);
			box2.add(pan);
			add(box2);
			if (pr instanceof PAttachStohasticGraphProjection)
				loadData(pr);
		}

		// ------------------------------------------------------------------
		if (controlType.equalsIgnoreCase("table")) {
			final ProjGraphPA pr = ((ProjGraphPA) selectedObject);
			final DefaultTableModel dm = new DefaultTableModel(0, 0) {
				@Override
				public void setValueAt(Object value, int row, int column) {
					try {
						if (pr.q_ != null) {
							double sum_Q = 0.;
							double sum_r = 0.0;
							for (int i = 0; i < row; i++) {
								sum_Q = sum_Q + pr.q_[i];
								sum_r = sum_r
										+ Double.valueOf(this.getValueAt(i, 0)
												.toString());
							}
							if ((sum_r - sum_Q) <= 0.000000000001) {
								super.setValueAt(value, row, column);
								// System.out.println("===== "+(sum_r-sum_Q));
							} else {
								JOptionPane
										.showMessageDialog(
												null,
												"The probability "
														+ value
														+ " for compliance with the condition of existence must be more then "
														+ pr.q_[row], "Error!",
												JOptionPane.ERROR_MESSAGE);
								super.setValueAt(value, row, column);
							}
						}else 	super.setValueAt(value, row, column);


					} catch (Exception re) {
						JOptionPane.showMessageDialog(null,
								"The probabilities must be possitive",
								"Error!", JOptionPane.ERROR_MESSAGE);
						super.setValueAt(new Double(0.0), row, column);
					} finally {
						updateProb();

					}
				}
			};
			dm.addColumn(propertyName_ + " values");
			
			String[] doub = propertyValue.split("/n");
			double[] d = new double[doub.length];
			double sum = 0.;
			for (int i = 0; i < doub.length; i++) {
				d[i]= Double.valueOf(doub[i]);
				dm.addRow(new Double[] {d[i] });
			}
			pr.r=d;
			if(pr.q_!=null)
				if(modeStohR)
					{setStohValuesR(dm,pr);if(checkbox!=null)checkbox.setSelected(true);}
				else
					{setStohValuesR2(dm,pr);if(checkbox!=null)checkbox.setSelected(false);}

			jTablePropertyValue = new JTable(dm);

			jLabelPropertyName = new JLabel(" Num of " + propertyName_);
			jTablePropertyValue
					.setPreferredScrollableViewportSize(new Dimension(150, 50));
			jTablePropertyValue.getColumnModel().getColumn(0)
					.setPreferredWidth(50);
			jTablePropertyValue.getTableHeader().setReorderingAllowed(false);
			JScrollPane jScrollPane = new JScrollPane(jTablePropertyValue);
			JTable rowTable = new RowNumberTable2(jTablePropertyValue);
			jScrollPane.setRowHeaderView(rowTable);
			jScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowTable
					.getTableHeader());

			final JSpinner jSpinner = new JSpinner();
			int val=1;
			if(pr.r!=null)val =pr.r.length;
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
			spinnerNumberModel.setMinimum(val);
			spinnerNumberModel.setMaximum(10000);
			spinnerNumberModel.setStepSize(1);
			spinnerNumberModel.setValue(Integer.valueOf(val));
			jSpinner.setValue(Integer.valueOf(val));
			jSpinner.setMaximumSize(new Dimension(400, jSpinner
					.getPreferredSize().height));

			jSpinner.setModel(spinnerNumberModel);
			
			jSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent evt) {
					// логика следующая. если стало больше чем строк, то
					// заполняю по умолчанию
					// если стало меньше, то удаляю последние так чтобы осталось
					// сколько нужно
					if (dm.getRowCount() < (Integer) jSpinner.getValue()) {
						int num = (Integer) jSpinner.getValue()
								- dm.getRowCount();
						for (int i = 0; i < num; i++)
							dm.addRow(new Double[] { 0.0 });
					}
					if (dm.getRowCount() > (Integer) jSpinner.getValue()) {
						int num = dm.getRowCount()
								- (Integer) jSpinner.getValue();
						for (int i = 0; i < num; i++)
							dm.removeRow(dm.getRowCount() - 1);
					}
					//recalcTableValues2();
					modeStohR=false;
					setStohValuesR2( dm,   pr);
					updateProb();


				}

				private void recalcTableValues() {
				// нужно определить сколько должно быть мин количество r
					pr.r = new double[pr.getM()];
					
				// m
					double av_k=0.;//,sum_q=0.;
					for(int i=0; i<pr.q_.length;i++)
						av_k=av_k+pr.q_[i]*i;
					//m=<k>/2
					double m=av_k/2.0;
					
				// 	min_index
					int j = 0; int min_index=0;
					for(int i=0; i<pr.q_.length;i++)
					{
						if(pr.q_[i]>0){min_index =i; break;}
						
					}
					
				// 
					double sumQ=0.;
					double d=min_index;
					for(int i=0; i<pr.q_.length;i++)
					{
						sumQ=sumQ+pr.q_[i];
						if(pr.q_[i]>0)d=d+1-sumQ;
						if(d>=m){
							j=i;
							break;
						}

					}
					double delta=d-m;
					j=j+2;
					//один j за счёт нулевого последнего а другой за счёт того что отсчёт с нуля начинается
					pr.r = new double[j];
					double w=0.;
					for (int i = 0; i < pr.r.length-1; i++)
						{
						pr.r[i] = pr.q_[i];
						w=w+pr.r[i];
						}
					
					pr.r[pr.r.length-2]=pr.r[pr.r.length-2]+delta;
					pr.r[pr.r.length-1] = 1-w-delta;
					
					int row=dm.getRowCount();
					for (int i =  row-1; i >=0; i--) {
						dm.removeRow(i);
						
					}
					for (int i = 0; i < pr.r.length; i++) {
						dm.addRow(new Double[]{pr.r[i]});
					}

					
					
				/*	double sum = 0.;
					pr.r = new double[(Integer) jSpinner.getValue()];
					for (int i = 0; i < -1 + (Integer) jSpinner.getValue(); i++) {
						dm.setValueAt(pr.q_[i], i, 0);
						sum += pr.q_[i];
						pr.r[i] = pr.q_[i];
					}
					dm.setValueAt(1 - sum, -1 + (Integer) jSpinner.getValue(),0);
					pr.r[(Integer) jSpinner.getValue() - 1] = 1 - sum;*/
				}
				private void recalcTableValues2() {
					for (int i = 0; i < -1 + (Integer) jSpinner.getValue(); i++) {
						dm.setValueAt(0., i, 0);
					}
					dm.setValueAt(1., -1 + (Integer) jSpinner.getValue(),0);
				}

			});

			JSpinner.NumberEditor jSpinnerNumberEditor = new JSpinner.NumberEditor(
					jSpinner, "0");
			jSpinnerNumberEditor.getTextField().setHorizontalAlignment(
					JTextField.LEFT);
			jSpinnerNumberEditor.getTextField().addKeyListener(
					new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							// System.out.println("keyReleased");
						}

						public void keyTyped(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
							// System.out.println("keyTyped");
						}
					});
			jSpinner.setEditor(jSpinnerNumberEditor);

			Box box1 = Box.createHorizontalBox();
			Box box2 = Box.createVerticalBox();

			JPanel pan1 = new JPanel();
			pan1.setLayout(new BorderLayout());
			box1.add(jSpinner);
			box1.add(jLabelPropertyName);
			updateProb();

			pan1.add(BorderLayout.WEST, box1);
			pan1.add(BorderLayout.CENTER, new JPanel());

			JPanel pan2 = new JPanel();
			pan2.setLayout(new BorderLayout());
			pan2.add(BorderLayout.WEST, jScrollPane);
			checkbox=new JCheckBox("High clustering");
			checkbox.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					//---------------------------------------------------------------------------------------
					modeStohR=!modeStohR;
					//if(modeStohR)checkbox.setSelected(true);
					//else checkbox.setSelected(false);

					if(modeStohR)setStohValuesR(dm,pr);
					else setStohValuesR2(dm,pr);;

					//---------------------------------------------------------------------------------------
				}
				
			});
			
			
			
			pan2.add(BorderLayout.NORTH,checkbox);

			pan2.add(BorderLayout.CENTER, new JPanel());
			box2.add(pan1);
			box2.add(Box.createVerticalStrut(5));
			box2.add(pan2);
			add("Center", box2);
		}
		// //////////////////////////////////////////////////////////////////x./////////
		if (controlType.equalsIgnoreCase("itemlist")) {
			final JButton jButton1 = new JButton();
			jButton1.setText("...");
			jButton1.setVerticalAlignment(SwingConstants.TOP);
			jButton1.setToolTipText("Edit Item...");
			jButton1.setIcon(new ImageIcon(getClass().getResource(
			"/images/IconError.png")));
			jButton1.setPreferredSize(new Dimension(45, 25));
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				private void heightConstrain(Component component) {
					Dimension d = new Dimension(
							component.getMaximumSize().width, component
									.getMinimumSize().height);
					component.setMaximumSize(d);
				}

				public void actionPerformed(java.awt.event.ActionEvent evt) {
					final JFrame frame = new JFrame("Editing graph");
					final DecompEditGraphPanel degp = new DecompEditGraphPanel();
					if (!getPropertyValue().equals("")) {
						StringReader sr = new StringReader(getPropertyValue());
						degp.loadGraph(sr);
					}
					final JLabel lab = new JLabel("Undirected");
					JCheckBox chDirected = new JCheckBox();
					chDirected.addItemListener(new ItemListener() {
						@Override
						public void itemStateChanged(ItemEvent e) {
							if (e.getStateChange() == ItemEvent.SELECTED) {
								lab.setText("Directed");
								degp.setTypeEdge(EdgeType.DIRECTED);
							} else {
								lab.setText("Undirected");
								degp.setTypeEdge(EdgeType.UNDIRECTED);
							}

						}

					});
					JComboBox cb = (JComboBox) degp.getComboMouse();
					JButton beg = new JButton("Begin");

					JButton end = new JButton("End");
					JButton normal = new JButton("Normal");
					JButton ok = new JButton("Save");
					beg.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							degp.gm.setMode(Mode.PICKING);
							degp.doBeginer(e);
							degp.gm.setMode(Mode.PICKING);
						}
					});
					normal.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							degp.doNormal(e);
						}
					});
					end.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							degp.doEnd(e);
						}
					});
					ok.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							boolean b = checkGraph();
							if (!b) {
								JOptionPane.showMessageDialog(null,
										"Put graph decomposition properly!",
										"Error!", JOptionPane.ERROR_MESSAGE);
								// WARNING_MESSAGE - уведомление

								return;
							}
							int i = 0;
							StringWriter sw = new StringWriter();
							Graph<SimbigraphNode, String> gra = null;
							gra = degp.getGraph();
							Transformer<SimbigraphNode, String> vertex_transformer = new Transformer<SimbigraphNode, String>() {

								public String transform(SimbigraphNode link) {
									return new Integer(link.state).toString();
								}
							};

							Transformer<SimbigraphNode, String> vertex_transformer2 = new Transformer<SimbigraphNode, String>() {

								public String transform(SimbigraphNode a) {
									return new Integer(a.getX()).toString();
								}
							};
							Transformer<SimbigraphNode, String> vertex_transformer3 = new Transformer<SimbigraphNode, String>() {

								public String transform(SimbigraphNode a) {
									return new Integer(a.getY()).toString();
								}
							};
							Transformer<SimbigraphNode, String> vertex_transformer4 = new Transformer<SimbigraphNode, String>() {
								public String transform(SimbigraphNode a) {
									String str = "bla";
									if (a.state == -5)
										str = "beg";
									if (a.state == 5)
										str = "end";
									if (a.state == 1)
										str = "nor";
									return str;
								}
							};
							Transformer<Hypergraph<SimbigraphNode, String>, String> graph_desc = new Transformer<Hypergraph<SimbigraphNode, String>, String>() {

								public String transform(
										Hypergraph<SimbigraphNode, String> a) {
									return new Double(0.2).toString();
								}
							};
							GraphMLWriter<SimbigraphNode, String> gm = new GraphMLWriter();
							try {
								gm.addVertexData("state", null, null,
										vertex_transformer4);
								gm.save(gra, sw);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							frame.dispose();
							jButton1
									.setIcon(new ImageIcon(
											getClass()
													.getResource(
															"/images/IconTaskList.png")));

							// System.out.println(sw.getBuffer().toString());
							setPrVal(sw.getBuffer().toString(),
									jSpinnerPropertyValue.getValue().toString());
							;
						}

						private boolean checkGraph() {
							// проверить компонент связности n>3
							// проверить нет ли циклов у входной и выходной

							Graph<SimbigraphNode, String> gra = degp.getGraph();
							int first = 0;
							int second = 0;
							for (SimbigraphNode node : gra.getVertices())
								if (((node.state == 5) || (node.state == -5))) {
									if (node.state == 5)
										second++;
									else
										first++;
									for (String edge : gra
											.getIncidentEdges(node))
										if (gra.getOpposite(node, edge).equals(
												node))
											return false;

								}
							if (first != 1 || second != 1)
								return false;
							return true;
						}

					});
					final ScalingControl scaler = new CrossoverScalingControl();
					JButton plus = new JButton("+");
					plus.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							scaler.scale(degp.vv, 1.1f, degp.vv.getCenter());
						}
					});
					JButton minus = new JButton("-");
					minus.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							scaler
									.scale(degp.vv, 1 / 1.1f, degp.vv
											.getCenter());
						}
					});

					Box controls = Box.createVerticalBox();
					Dimension space = new Dimension(20, 20);
					controls.add(Box.createRigidArea(space));

					JPanel zoomControls = new JPanel(new GridLayout(1, 2));
					zoomControls.setBorder(BorderFactory
							.createTitledBorder("Zoom"));
					zoomControls.add(plus);
					zoomControls.add(minus);
					heightConstrain(zoomControls);
					controls.add(zoomControls);

					JPanel modePanel = new JPanel(new GridLayout(1, 1));
					modePanel.setBorder(BorderFactory
							.createTitledBorder("Mouse Mode"));
					modePanel.add(cb);
					heightConstrain(modePanel);
					controls.add(modePanel);

					JPanel nodeType = new JPanel(new GridLayout(1, 3));
					nodeType.setBorder(BorderFactory
							.createTitledBorder("Vertex Type"));
					nodeType.add(beg);
					nodeType.add(normal);
					nodeType.add(end);
					heightConstrain(nodeType);
					controls.add(nodeType);
					controls.add(Box.createRigidArea(space));

					JPanel modePanel2 = new JPanel(new GridLayout(1, 1));
					modePanel2.setBorder(BorderFactory
							.createTitledBorder("Edge Type"));
					modePanel2.add(chDirected);
					modePanel2.add(lab);
					heightConstrain(modePanel2);
					controls.add(modePanel2);

					JPanel okey = new JPanel(new GridLayout(1, 2));
					okey.add(new JPanel());
					okey.add(ok);
					JPanel p = new JPanel();
					p.add(BorderLayout.NORTH, okey);
					controls.add(p);

					// JPanel pan=new JPanel(new BorderLayout());
					frame.add(controls, BorderLayout.EAST);
					frame.add(degp, BorderLayout.CENTER);

					frame.pack();
					frame.setVisible(true);
				}

			});
			JButton jButton2 = new JButton();
			//jButton2.setText("X");
			jButton2.setIcon(new ImageIcon(getClass().getResource(
			"/images/IconDelete.png")));
			jButton2.setVerticalAlignment(SwingConstants.TOP);
			jButton2.setToolTipText("Remove Item...");
			//jButton2.setPreferredSize(new Dimension(45, 25));
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// удаляю текущий элемент
					Projection selectedNode = (Projection) selectedObject;

					// если нахожу элементы по имени, то удаляю
					String[] delElem = null;
					for (String[] iterable_element : selectedNode
							.getProperties()) {
						if (propertyName.equals(iterable_element[1]))
							delElem = iterable_element;
					}
					selectedNode.getProperties().remove(delElem);
					update();
					propertiesPanel.addRows((Projection) selectedObject);

				}
			});
			jSpinnerPropertyValue = new JSpinner();
			jSpinnerPropertyValue.setPreferredSize(new Dimension(45, 25));
			SpinnerModel sm = new SpinnerNumberModel(0.5, 0.1, 10.0, 0.1);

			jSpinnerPropertyValue.setModel(sm);
			jSpinnerPropertyValue
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(
								javax.swing.event.ChangeEvent evt) {
							setNewVer();

						}

					});
			Box box = Box.createHorizontalBox();

			if (propertyValue == null || propertyValue.equals(""))
				jButton1.setIcon(new ImageIcon(getClass().getResource(
						"/images/IconError.png")));
			else
				jButton1.setIcon(new ImageIcon(getClass().getResource(
						"/images/IconTaskList.png")));

			box.add(Box.createHorizontalStrut(5));
			box.add(jButton1);
			box.add(Box.createHorizontalStrut(5));
			box.add(jSpinnerPropertyValue);
			box.add(Box.createHorizontalStrut(5));
			box.add(jButton2);
			box.add(Box.createGlue());

			JPanel aa = new JPanel();
			aa.setLayout(new BorderLayout());
			aa.add("North", box);
			add("Center", box);
			validate();
		}

		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.equalsIgnoreCase("textpane")) {
			jTextPanePropertyValue = new JTextPane();

			if (propertyType != "") {
				jTextPanePropertyValue.setToolTipText(propertyType);
			}
			JavaDocument jd = new JavaDocument();
			// jd.setSingleLine(true);
			jTextPanePropertyValue.setDocument(jd);
			jTextPanePropertyValue.setFont(new Font(Font.MONOSPACED,
					Font.PLAIN, 14));
			jTextPanePropertyValue.setMargin(new Insets(-3, -2, -3, -2));
			jTextPanePropertyValue
					.addKeyListener(new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							jTextPanePropertyValueKeyReleased(evt);
						}
					});
			jTextPanePropertyValue.setText(propertyValue);

			JScrollPane jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(jTextPanePropertyValue);
			jScrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			jScrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			jButtonPropertyValue = new JButton();
			jButtonPropertyValue.setText("...");
			jButtonPropertyValue.setVerticalAlignment(SwingConstants.TOP);
			jButtonPropertyValue.setToolTipText("Open java code editor...");
			jButtonPropertyValue.setPreferredSize(new Dimension(25, 25));
			jButtonPropertyValue
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							jButtonPropertyValueActionPerformed(evt);
						}
					});

			j = new JPanel();
			j.setLayout(new BorderLayout());
			j.add("North", jButtonPropertyValue);

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add("Center", jScrollPane);
			jPanel.add("East", j);
			add("Center", jPanel);
		}
		// //////////////////////////////////////////////////////////////////x./////////
		if (controlType.split(":")[0].equalsIgnoreCase("seedlist")) {

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
			add("Center", jPanel);

			jSpinnerX = new JSpinner();
			jSpinnerY = new JSpinner();
			SpinnerNumberModel spinnerNumberModelX = new SpinnerNumberModel();
			spinnerNumberModelX.setMinimum(0.0);
			spinnerNumberModelX.setMaximum(1.0);
			spinnerNumberModelX.setStepSize(0.01);

			spinnerNumberModelX.setValue(Double.valueOf(propertyValue
					.split(",")[0]));
			jSpinnerX.setValue(Double.valueOf(propertyValue.split(",")[0]));
			jSpinnerX.setMaximumSize(new Dimension(400, jSpinnerX
					.getPreferredSize().height));

			jSpinnerX.setModel(spinnerNumberModelX);
			jSpinnerX.addChangeListener(new javax.swing.event.ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					changeItemProp();

				}
			});

			JSpinner.NumberEditor jSpinnerNumberEditorX = new JSpinner.NumberEditor(
					jSpinnerX, "0.00");
			jSpinnerNumberEditorX.getTextField().setHorizontalAlignment(
					JTextField.LEFT);
			jSpinnerNumberEditorX.getTextField().addKeyListener(
					new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
						}

						public void keyTyped(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
						}
					});
			jSpinnerX.setEditor(jSpinnerNumberEditorX);

			SpinnerNumberModel spinnerNumberModelY = new SpinnerNumberModel();
			spinnerNumberModelY.setMinimum(0.0);
			spinnerNumberModelY.setMaximum(1.0);
			spinnerNumberModelY.setStepSize(0.01);

			spinnerNumberModelY.setValue(Double.valueOf(propertyValue
					.split(",")[1]));
			jSpinnerY.setValue(Double.valueOf(propertyValue.split(",")[1]));
			jSpinnerY.setMaximumSize(new Dimension(400, jSpinnerY
					.getPreferredSize().height));

			jSpinnerY.setModel(spinnerNumberModelY);
			JSpinner.NumberEditor jSpinnerNumberEditorY = new JSpinner.NumberEditor(
					jSpinnerY, "0.00");

			jSpinnerY.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent evt) {
					changeItemProp();

				}
			});

			jSpinnerNumberEditorY.getTextField().setHorizontalAlignment(
					JTextField.LEFT);
			jSpinnerNumberEditorY.getTextField().addKeyListener(
					new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
						}

						public void keyTyped(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
						}
					});
			jSpinnerY.setEditor(jSpinnerNumberEditorY);

			JLabel jLabelX = new JLabel("Probability:");
			jPanel.add(jLabelX);
			jPanel.add(jSpinnerX);

			jPanel.add(Box.createHorizontalStrut(10));

			JLabel jLabelY = new JLabel("Straight:");
			jPanel.add(jLabelY);
			jPanel.add(jSpinnerY);

			JButton jButton2 = new JButton();
			jButton2.setText("X");
			jButton2.setVerticalAlignment(SwingConstants.TOP);
			jButton2.setToolTipText("Remove Item...");
			jButton2.setPreferredSize(new Dimension(45, 25));
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// удаляю текущий элемент
					Projection selectedNode = (Projection) selectedObject;

					// если нахожу элементы по имени, то удаляю
					String[] delElem = null;
					for (String[] iterable_element : selectedNode
							.getProperties()) {
						if (propertyName.equals(iterable_element[1]))
							delElem = iterable_element;
					}
					selectedNode.getProperties().remove(delElem);
					update();
					propertiesPanel.addRows((Projection) selectedObject);

				}
			});
			jPanel.add(Box.createHorizontalStrut(20));
			jPanel.add(jButton2);

			jPanel.add(Box.createHorizontalGlue());

			validate();
		}

		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.equalsIgnoreCase("filechooser")) {
			jTextPanePropertyValue = new JTextPane();

			if (propertyType != "") {
				jTextPanePropertyValue.setToolTipText(propertyType);
			}
			// JavaDocument jd = new JavaDocument();
			// jd.setSingleLine(true);
			// jTextPanePropertyValue.setDocument(jd);
			jTextPanePropertyValue.setFont(new Font(Font.MONOSPACED,
					Font.PLAIN, 14));
			jTextPanePropertyValue.setMargin(new Insets(-3, -2, -3, -2));
			jTextPanePropertyValue
					.addKeyListener(new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							jTextPanePropertyValueKeyReleased(evt);
						}
					});
			jTextPanePropertyValue.setText(propertyValue);

			JScrollPane jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(jTextPanePropertyValue);
			jScrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			jScrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			jButtonPropertyValue = new JButton();
			jButtonPropertyValue.setText("...");
			jButtonPropertyValue.setVerticalAlignment(SwingConstants.TOP);
			jButtonPropertyValue.setToolTipText("Open file chooser...");
			jButtonPropertyValue.setPreferredSize(new Dimension(25, 25));
			jButtonPropertyValue
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							jFileChooserValueActionPerformed(evt);
						}
					});

			j = new JPanel();
			j.setLayout(new BorderLayout());
			j.add("North", jButtonPropertyValue);
			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add("Center", jScrollPane);
			jPanel.add("East", j);
			add("Center", jPanel);
		}
		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.equalsIgnoreCase("checkbox")) {
			jCheckBoxPropertyValue = new JCheckBox();
			jCheckBoxPropertyValue.setText(propertyValue);
			jCheckBoxPropertyValue.setSelected(Boolean.valueOf(propertyValue));
			jCheckBoxPropertyValue.setBorder(BorderFactory.createEmptyBorder(4, 0, 6, 0));
			jCheckBoxPropertyValue
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							jCheckBoxPropertyValueActionPerformed(evt);
						}
					});
			if (propertyType != "") {
				jCheckBoxPropertyValue.setToolTipText(propertyType);
			}

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
			add("Center", jPanel);

			jPanel.add(jCheckBoxPropertyValue);
			jPanel.add(Box.createHorizontalGlue());
		}

		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.split(":")[0].equalsIgnoreCase("combobox")) {
			jComboBoxPropertyValue = new JComboBox();
			jComboBoxPropertyValue.setMaximumSize(new Dimension(400,
					jComboBoxPropertyValue.getPreferredSize().height));
			for (int i = 0; i < controlType.split(":")[1].split(",").length; i++) {
				jComboBoxPropertyValue.addItem(controlType.split(":")[1]
						.split(",")[i]);
			}
			jComboBoxPropertyValue.setSelectedItem(propertyValue);
			jComboBoxPropertyValue
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							jComboBoxPropertyValueActionPerformed(evt);
						}
					});
			if (propertyType != "") {
				jComboBoxPropertyValue.setToolTipText(propertyType);
			}

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
			add("Center", jPanel);

			jPanel.add(jComboBoxPropertyValue);

			jPanel.add(Box.createHorizontalGlue());
		}
		// /////////////////////////////////////////////////////////////////////
		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.split(":")[0].equalsIgnoreCase("spinnerProb")) {
			jSpinnerPropertyValue = new JSpinner();
			double min=Double.valueOf(controlType.split(":")[1].split(",")[0]);
			double val=Double.valueOf(propertyValue);
			double max=Double.valueOf(controlType.split(":")[1].split(",")[1]);
			
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(val,	min, max, 0.001);
			spinnerNumberModel.setMinimum(min);
			spinnerNumberModel.setMaximum(max);
			//spinnerNumberModel.setStepSize(0.001);
			spinnerNumberModel.setValue(val);
			jSpinnerPropertyValue.setModel(spinnerNumberModel);
			jSpinnerPropertyValue.setValue(Double.valueOf(propertyValue));
		/*	jSpinnerPropertyValue.setMaximumSize(new Dimension(800,
					jSpinnerPropertyValue.getPreferredSize().height));*/
			jSpinnerPropertyValue
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(
								javax.swing.event.ChangeEvent evt) {
							jSpinnerPropertyValueStateChanged(evt);
						}
					});

			/*jSpinnerNumberEditor = new JSpinner.NumberEditor(
					jSpinnerPropertyValue, "0.001");
			jSpinnerNumberEditor.getTextField().setHorizontalAlignment(
					JTextField.LEFT);
			jSpinnerNumberEditor.getTextField().addKeyListener(
					new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							jSpinnerPropertyValueKeyReleased(evt);
						}

						public void keyTyped(java.awt.event.KeyEvent evt) {
							jSpinnerPropertyValueKeyReleased(evt);
						}
					});
			jSpinnerPropertyValue.setEditor(jSpinnerNumberEditor);

			if (propertyType != "") {
				jSpinnerPropertyValue.setToolTipText(propertyType);
			}*/

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
			add("Center", jPanel);

			jPanel.add(jSpinnerPropertyValue);

			jPanel.add(Box.createHorizontalGlue());
		}
		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.split(":")[0].equalsIgnoreCase("spinner")) {
			jSpinnerPropertyValue = new JSpinner();
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
			spinnerNumberModel.setMinimum(Integer.valueOf(controlType
					.split(":")[1].split(",")[0]));
			spinnerNumberModel.setMaximum(Integer.valueOf(controlType
					.split(":")[1].split(",")[1]));
			spinnerNumberModel.setStepSize(1);
			spinnerNumberModel.setValue(Integer.valueOf(propertyValue));
			jSpinnerPropertyValue.setModel(spinnerNumberModel);
			jSpinnerPropertyValue.setValue(Integer.valueOf(propertyValue));
			jSpinnerPropertyValue.setMaximumSize(new Dimension(400,
					jSpinnerPropertyValue.getPreferredSize().height));
			jSpinnerPropertyValue
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(
								javax.swing.event.ChangeEvent evt) {
							jSpinnerPropertyValueStateChanged(evt);
						}
					});

			jSpinnerNumberEditor = new JSpinner.NumberEditor(
					jSpinnerPropertyValue, "0");
			jSpinnerNumberEditor.getTextField().setHorizontalAlignment(
					JTextField.LEFT);
			jSpinnerNumberEditor.getTextField().addKeyListener(
					new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							jSpinnerPropertyValueKeyReleased(evt);
						}

						public void keyTyped(java.awt.event.KeyEvent evt) {
							jSpinnerPropertyValueKeyReleased(evt);
						}
					});
			jSpinnerPropertyValue.setEditor(jSpinnerNumberEditor);

			if (propertyType != "") {
				jSpinnerPropertyValue.setToolTipText(propertyType);
			}

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
			add("Center", jPanel);

			jPanel.add(jSpinnerPropertyValue);

			jPanel.add(Box.createHorizontalGlue());
		}

		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.split(":")[0].equalsIgnoreCase("colorchooser")) {
			jButtonPropertyValue = new JButton();
			jButtonPropertyValue.setText(propertyValue);
			jButtonPropertyValue.setVerticalAlignment(SwingConstants.TOP);
			jButtonPropertyValue.setToolTipText("Open color chooser...");
			jButtonPropertyValue.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jColorChooserValueActionPerformed(e);
				}
			});

			if (propertyType != "") {
				jButtonPropertyValue.setToolTipText(propertyType);
			}

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
			add("Center", jPanel);

			JButton jButtonSetNullValue = new JButton();
			jButtonSetNullValue.setText("Set null value");
			jButtonSetNullValue.setVerticalAlignment(SwingConstants.TOP);
			jButtonSetNullValue.setToolTipText("Set null value");
			jButtonSetNullValue.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonSetNullValueActionPerformed(e);
				}
			});
			JSpinner jSpinnerTransparencyValue = new JSpinner();
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
			spinnerNumberModel.setMinimum(0);
			spinnerNumberModel.setMaximum(255);
			spinnerNumberModel.setStepSize(1);
			if (!propertyValue.equalsIgnoreCase("null")) {
				spinnerNumberModel.setValue(Integer.valueOf(propertyValue
						.split(",")[3]));
				jSpinnerTransparencyValue.setValue(Integer
						.valueOf(propertyValue.split(",")[3]));
			} else {
				spinnerNumberModel.setValue(255);
				jSpinnerTransparencyValue.setValue(255);
			}
			jSpinnerTransparencyValue.setModel(spinnerNumberModel);
			jSpinnerTransparencyValue
					.addChangeListener(new javax.swing.event.ChangeListener() {
						public void stateChanged(
								javax.swing.event.ChangeEvent evt) {
							// jSpinnerTransparencyValueStateChanged(evt);
						}
					});

			JSpinner.NumberEditor jSpinnerNumberEditor = new JSpinner.NumberEditor(
					jSpinnerTransparencyValue, "0");
			jSpinnerNumberEditor.getTextField().setHorizontalAlignment(
					JTextField.LEFT);
			jSpinnerNumberEditor.getTextField().addKeyListener(
					new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
						}

						public void keyTyped(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
						}
					});
			jSpinnerTransparencyValue.setEditor(jSpinnerNumberEditor);

			jPanel.add(jButtonPropertyValue);

			jPanel.add(Box.createHorizontalStrut(10));

			JLabel jLabelTransparency = new JLabel("Transparency:");
			jPanel.add(jLabelTransparency);

			jSpinnerTransparencyValue.setMaximumSize(new Dimension(400,
					jSpinnerTransparencyValue.getPreferredSize().height));
			jPanel.add(jSpinnerTransparencyValue);

			jPanel.add(Box.createHorizontalStrut(10));

			jPanel.add(jButtonSetNullValue);
			jPanel.add(Box.createHorizontalGlue());
		}

		// ///////////////////////////////////////////////////////////////////////////
		if (controlType.split(":")[0].equalsIgnoreCase("location")) {
			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
			add("Center", jPanel);

			JSpinner jSpinnerX = new JSpinner();
			SpinnerNumberModel spinnerNumberModelX = new SpinnerNumberModel();
			spinnerNumberModelX.setMinimum(0);
			spinnerNumberModelX.setMaximum(10000);
			spinnerNumberModelX.setStepSize(1);

			spinnerNumberModelX.setValue(Integer.valueOf(propertyValue
					.split(",")[0]));
			jSpinnerX.setValue(Integer.valueOf(propertyValue.split(",")[0]));
			jSpinnerX.setMaximumSize(new Dimension(400, jSpinnerX
					.getPreferredSize().height));

			jSpinnerX.setModel(spinnerNumberModelX);
			jSpinnerX.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent evt) {
					// jSpinnerTransparencyValueStateChanged(evt);
				}
			});

			JSpinner.NumberEditor jSpinnerNumberEditorX = new JSpinner.NumberEditor(
					jSpinnerX, "0");
			jSpinnerNumberEditorX.getTextField().setHorizontalAlignment(
					JTextField.LEFT);
			jSpinnerNumberEditorX.getTextField().addKeyListener(
					new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
						}

						public void keyTyped(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
						}
					});
			jSpinnerX.setEditor(jSpinnerNumberEditorX);

			JSpinner jSpinnerY = new JSpinner();
			SpinnerNumberModel spinnerNumberModelY = new SpinnerNumberModel();
			spinnerNumberModelY.setMinimum(0);
			spinnerNumberModelY.setMaximum(10000);
			spinnerNumberModelY.setStepSize(1);

			spinnerNumberModelY.setValue(Integer.valueOf(propertyValue
					.split(",")[1]));
			jSpinnerY.setValue(Integer.valueOf(propertyValue.split(",")[1]));
			jSpinnerY.setMaximumSize(new Dimension(400, jSpinnerY
					.getPreferredSize().height));

			jSpinnerY.setModel(spinnerNumberModelY);
			jSpinnerY.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent evt) {
					// jSpinnerTransparencyValueStateChanged(evt);
				}
			});

			JSpinner.NumberEditor jSpinnerNumberEditorY = new JSpinner.NumberEditor(
					jSpinnerY, "0");
			jSpinnerNumberEditorY.getTextField().setHorizontalAlignment(
					JTextField.LEFT);
			jSpinnerNumberEditorY.getTextField().addKeyListener(
					new java.awt.event.KeyAdapter() {
						public void keyReleased(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
						}

						public void keyTyped(java.awt.event.KeyEvent evt) {
							// jSpinnerPropertyValueKeyReleased(evt);
						}
					});
			jSpinnerY.setEditor(jSpinnerNumberEditorY);

			JLabel jLabelX = new JLabel("X:");
			jPanel.add(jLabelX);
			jPanel.add(jSpinnerX);

			jPanel.add(Box.createHorizontalStrut(10));

			JLabel jLabelY = new JLabel("Y:");
			jPanel.add(jLabelY);
			jPanel.add(jSpinnerY);

			jPanel.add(Box.createHorizontalGlue());
		}
		// ///////////////////////////////////////////////////////////////////////////

		if (controlType.split(":")[0].equalsIgnoreCase("size")) {
			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
			add("Center", jPanel);
			
			jSpinnerX = new JSpinner();
			SpinnerNumberModel spinnerNumberModelX = new SpinnerNumberModel();
			spinnerNumberModelX.setMinimum(0);
			spinnerNumberModelX.setMaximum(10000);
			spinnerNumberModelX.setStepSize(1);
			
			spinnerNumberModelX.setValue(Integer.valueOf(propertyValue.split(",")[0]));
			jSpinnerX.setValue(Integer.valueOf(propertyValue.split(",")[0]));
			jSpinnerX.setMaximumSize(new Dimension(400, jSpinnerX.getPreferredSize().height));
			
			jSpinnerX.setModel(spinnerNumberModelX);
			jSpinnerX.addChangeListener(new javax.swing.event.ChangeListener()
			{
	            public void stateChanged(javax.swing.event.ChangeEvent evt)
	            {
	            	jSpinnerPropertyValueStateChanged2(evt);
	            }
	        });
			
			JSpinner.NumberEditor jSpinnerNumberEditorX = new JSpinner.NumberEditor(jSpinnerX, "0");
			jSpinnerNumberEditorX.getTextField().setHorizontalAlignment(JTextField.LEFT);
			jSpinnerX.setEditor(jSpinnerNumberEditorX);
			
			jSpinnerY = new JSpinner();
			SpinnerNumberModel spinnerNumberModelY = new SpinnerNumberModel();
			spinnerNumberModelY.setMinimum(0);
			spinnerNumberModelY.setMaximum(10000);
			spinnerNumberModelY.setStepSize(1);
			
			spinnerNumberModelY.setValue(Integer.valueOf(propertyValue.split(",")[1]));
			jSpinnerY.setValue(Integer.valueOf(propertyValue.split(",")[1]));
			jSpinnerY.setMaximumSize(new Dimension(400, jSpinnerY.getPreferredSize().height));
			
			jSpinnerY.setModel(spinnerNumberModelY);
			jSpinnerY.addChangeListener(new javax.swing.event.ChangeListener()
			{
	            public void stateChanged(javax.swing.event.ChangeEvent evt)
	            {
	            	jSpinnerPropertyValueStateChanged2(evt);
	            }
	        });
			
			JSpinner.NumberEditor jSpinnerNumberEditorY = new JSpinner.NumberEditor(jSpinnerY, "0");
			jSpinnerNumberEditorY.getTextField().setHorizontalAlignment(JTextField.LEFT);
			jSpinnerY.setEditor(jSpinnerNumberEditorY);
			
			JLabel jLabelX = new JLabel("Width:");
			jPanel.add(jLabelX);
			jPanel.add(jSpinnerX);
			
			jPanel.add(Box.createHorizontalStrut(10));
			
			
			JLabel jLabelY = new JLabel("Height:");
			jPanel.add(jLabelY);
			jPanel.add(jSpinnerY);

			
			jPanel.add(Box.createHorizontalGlue());
		}

		// ///////////////////////////////////////////////////////////////////////////

		if (controlType.equalsIgnoreCase("probabilities")) {
			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.LINE_AXIS));
			add("Center", jPanel);

			for (int i = 0; i < 6; i++) {
				jTextPanePropertyValue = new JTextPane();

				// jTextPanePropertyValue.setMaximumSize(new Dimension(400,
				// jTextPanePropertyValue.getPreferredSize().height));

				if (propertyType != "") {
					jTextPanePropertyValue.setToolTipText(propertyType);
				}
				JavaDocument jd = new JavaDocument();
				jd.setSingleLine(true);
				jTextPanePropertyValue.setDocument(jd);

				jTextPanePropertyValue.setFont(new Font(Font.MONOSPACED,
						Font.PLAIN, 14));
				jTextPanePropertyValue.setMargin(new Insets(-3, -2, -3, -2));
				jTextPanePropertyValue
						.addKeyListener(new java.awt.event.KeyAdapter() {
							public void keyReleased(java.awt.event.KeyEvent evt) {
								jTextPanePropertyValueKeyReleased(evt);
							}
						});
				jTextPanePropertyValue.setText(propertyValue.split(",")[i]);

				JScrollPane jScrollPane = new JScrollPane();
				// jScrollPane.setMaximumSize(new Dimension(400,
				// jTextPanePropertyValue.getPreferredSize().height));
				jScrollPane.setViewportView(jTextPanePropertyValue);
				jScrollPane
						.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				jScrollPane
						.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				JLabel jLabelP = new JLabel("  " + (i + 1) + ":");
				jPanel.add(jLabelP);
				jPanel.add(jScrollPane);
			}
		}
	}
	/**
	 * 
	 * Устанавливают правильное значение в таблице приращений
	 * @param dm модель таблицы приращения
	 * @param pr соответствующая проекция
	 * 
	 */

		private void setStohValuesR(DefaultTableModel dm,  ProjGraphPA pr) {

			pr.r = new double[pr.getM()];
			double[] newQ=new double[pr.getM()];
		
			
			/*System.out.println("вывожу Q");
			for(int i=0;i<6;i++)
			System.out.println(pr.q_[i]);*/
			
			//запоминаем среднюю связность и сумму вероятностей до первого нуля
			double m=0;
			for(int i=0; i<pr.q_.length;i++)
				m=m+pr.q_[i]*i;
			//получаем среднее значение m, нормированное по связностям до первого нуля
			m=m/2.0;
			System.out.println("m="+m);
			
			//получаем g
			int g=0;
			for(int i=0; i<pr.q_.length;i++)
				if(pr.q_[i]>0){g =i; break;}
			
			// ищем h
			int h = 0;
			double Fq=0.;		double sumQ=0.;
			for(int i=0; i<pr.q_.length;i++)
			{
				sumQ=sumQ+pr.q_[i];
				Fq=Fq+(1-sumQ);
				if((m-Fq)<0){
					// во первых на одну больше, во вторых начинается с 0
					h=i+2;
					break;
				}
			}
		
			// устанавливаем значения r
			pr.r = new double[h];
			sumQ=0.;		double Sum_iQ=0.;
			for (int i = 0; i < pr.r.length-1; i++)
				{
					pr.r[i] = pr.q_[i];
					Sum_iQ=Sum_iQ+i*pr.q_[i];
					sumQ=sumQ+pr.q_[i];
				}
			//Sum_iQ=Sum_iQ+(h-1)*(1-sumQ);
			
			pr.r[h-1] = 1-sumQ;
			//double m_= (Sum_iQ);
			pr.r[h-2]=pr.r[h-2] +(Fq-m); //(m_-m);
			pr.r[h-1] = pr.r[h-1]-(Fq-m);//(m_-m);
			//System.out.println("del="+(Fq-m));
			
			double su = 0;
			for (int i = 0; i < pr.r.length; i++) {
				su=su+i*pr.r[i];
			}
			//System.out.println("среднее:"+su);
			
			int row=dm.getRowCount();
			for (int i =  row-1; i >=0; i--) {
				dm.removeRow(i);
			}
			double a1=0.,a2=0.;
			for (int i = 0; i < pr.r.length; i++) {
				dm.addRow(new Double[]{pr.r[i]});
				a1+=pr.r[i];
			}
			
			for (int i = 0; i < pr.q_.length; i++) {
				a2+=pr.q_[i];
			}

			
			//System.out.println("r:"+a1);
			//System.out.println("q_:"+a2);
		}

		/**
		 * 
		 * Устанавливают правильное значение в таблице приращений
		 * @param dm модель таблицы приращения
		 * @param pr соответствующая проекция
		 * 
		 */

			private void setStohValuesR2(DefaultTableModel dm,  ProjGraphPA pr) {
				System.out.println("в");
				pr.r = new double[pr.getM()];
				double[] newQ=new double[pr.getM()];
				
				
				//запоминаем среднюю связность и сумму вероятностей до первого нуля
				double m=0;
				for(int i=0; i<pr.q_.length;i++)
					m=m+pr.q_[i]*i;
				//получаем среднее значение m, нормированное по связностям до первого нуля
				m=m/2.0;
				//System.out.println("m="+m);
				
				//получаем g
				int g=0;
				for(int i=0; i<pr.q_.length;i++)
					if(pr.q_[i]>0){g =i; break;}
				
				// ищем h
				int h = 0;
				double Fq=0.;		double sumQ=0.;
				for(int i=0; i<pr.q_.length;i++)
				{
					sumQ=sumQ+pr.q_[i];
					Fq=Fq+(1-sumQ);
					if((m-Fq)<0){
						// нумерация не с нуля, а с единицы
						h=i+2;
						break;
					}
				}
				if(dm.getRowCount()+1>h)h=dm.getRowCount()+1;
				//System.out.println("dm.getRowCount()"+dm.getRowCount());
				//System.out.println("h"+h);
				// устанавливаем значения r
				pr.r = new double[h];
				sumQ=0.;		double Sum_iQ=0.;
				for (int i = 0; i < pr.r.length-1; i++)
					{
						pr.r[i] = pr.q_[i];
						sumQ=sumQ+pr.q_[i];
					}
				pr.r[h-1] = 1-sumQ;//(m_-m);

				
				double delta= (Fq-m);

				System.out.println("del2:"+delta);
				int j = 1;
                for(int i=g;i<h-1;i++)
                {
                	if(j*(pr.r[i+1])>delta)
                	{
                		pr.r[g]=pr.r[g]+delta/(double)j;
                		pr.r[i+1]=pr.r[i+1]-delta/(double)j;
                		break;
                	}else{
                		delta=delta-j*(pr.r[i+1]);
                		pr.r[g]=pr.r[g]+pr.r[i+1]; pr.r[i+1]=0.;
                		j++;
                	}
                	
                	
                }
				double su = 0;
				for (int i = 0; i < pr.r.length; i++) {
					su=su+pr.r[i];
				}
				System.out.println("ffffffffffffffff"+su);
				
				int row=dm.getRowCount();
				for (int i =  row-1; i >=0; i--) {
					dm.removeRow(i);
				}
				double a1=0.,a2=0.;
				for (int i = 0; i < pr.r.length; i++) {
					dm.addRow(new Double[]{pr.r[i]});
					a1+=pr.r[i];
				}
				
				for (int i = 0; i < pr.q_.length; i++) {
					a2+=pr.q_[i];
				}

				
				su=0;
				for (int i = 0; i < pr.r.length; i++) {
					su=su+i*pr.r[i];
				}
				System.out.println("среднее:"+su);
				System.out.println("r:"+a1);
				System.out.println("q_:"+a2);
			}

/*	private void setStohValuesR(DefaultTableModel dm,  ProjGraphPA pr) {
		double sum_r = 0.; double sum_q =0.;double s =0.;
		for (int i = 0; i < dm.getRowCount(); i++) {
			double r = Double.valueOf(dm.getValueAt(i, 0).toString());
			sum_r=r+sum_r;
			sum_q = sum_q+pr.q_[i];
		}
		// нужно определить сколько должно быть мин количество r
		pr.r = new double[pr.getM()];
	
		double m=0;
		for(int i=0; i<pr.q_.length;i++)
		{
			m=m+pr.q_[i]*i;
			s=s+pr.q_[i];
		}
		m=m/2.0;
		int j = 0; int first=0;
		for(int i=0; i<pr.q_.length;i++)
		{
			if(pr.q_[i]>0){first =i; break;}
			
		}
		double pl=(double)first;double sumQ=0.;
		for(int i=0; i<pr.q_.length;i++)
		{
			sumQ=sumQ+pr.q_[i];
			if(pr.q_[i]>0)pl=pl+1-sumQ;
			if(pl>=m){
				j=i;
				break;
			}

		}
		double delta=pl-m;
		j=j+2;//один j за счёт нулевого последнего а другой за счёт того что отсчёт с нуля начинается
		pr.r = new double[j];
		double w=0.;
		for (int i = 0; i < pr.r.length-1; i++)
			{
			pr.r[i] = pr.q_[i];
			w=w+pr.r[i];
			}
		
		pr.r[pr.r.length-2]=pr.r[pr.r.length-2]+delta;
		pr.r[pr.r.length-1] = 1-w-delta;
		
		int row=dm.getRowCount();
		for (int i =  row-1; i >=0; i--) {
			dm.removeRow(i);
			
		}
		for (int i = 0; i < pr.r.length; i++) {
			dm.addRow(new Double[]{pr.r[i]});
		}
	}*/
	private void loadData(ProjGraphPA pr) {
		double[] Q_;
		double[] Q;
		Factory<Integer> agentFactory = new Factory<Integer>() {
			int n = 0;
			public Integer create() {
				return n++;
			}
		};
		Factory<Integer> edgeFactory = new Factory<Integer>() {
			int n = 0;
			public Integer create() {
				return n++;
			}
		};
		String fileName = jTextPanePropertyValue.getText();
		Graph graph = new SparseGraph<Integer, Integer>();
		PajekNetReader<Graph<Integer, Integer>, Integer, Integer> pnr;
		try {
			pnr = new PajekNetReader<Graph<Integer, Integer>, Integer, Integer>(
					agentFactory, edgeFactory);
			File file = new File(fileName);
			pnr.load(fileName, graph);

		} catch (IOException e5) {
			new IOException("error Simbigraph xml format");
		}
		Iterator<Integer> it = graph.getVertices().iterator();
		int length = 0;
		while (it.hasNext()) {
			int deg = graph.degree(it.next());
			if (deg > length)
				length = deg;
		}
		it = graph.getVertices().iterator();
		Q_ = new double[length + 1];
		while (it.hasNext()) {
			Integer node = it.next();
			int m = graph.degree(node);
			Q_[m] = Q_[m] + 1.0 / ((double) graph.getVertexCount());
		}
		pr.q_ = Q_;
	}

	protected void calibStoh(ProjGraphPA pr) {
		
		double[] g = new double[pr.q_.length];
		int min_nZero = Integer.MAX_VALUE;
		for (int i = 0; i < pr.q_.length; i++) {
			if (pr.q_[i] > 0) {
				min_nZero = i;
				break;
			}
		}
		g[min_nZero] = pr.r[min_nZero] / pr.q_[min_nZero];

		int i = min_nZero + 1;
		for (; i < g.length; i++) {
			if (pr.q_[i] > 0) {
				double r_ = 0.;
				if (i < pr.r.length)
					r_ = pr.r[i];
				g[i] = pr.q_[i - 1] / pr.q_[i] * g[i - 1] + r_ / pr.q_[i] - 1;
			} else
				break;
		}
		List<String[]> list = pr.getProperties();
		String str = ""
				+ "import simbigraph.graphs.prefAttachment.*;"
				+ "\r\n"
				+ "import simbigraph.gui.*;"
				+ "\r\n"
				+ "import simbigraph.core.*;"
				+ "\r\n"
				+ "public class prefRule extends PrefAttechRule{"
				+ "\r\n"
				+ "public double f(int k) {if(k<f.length)return f[k]; else return 0;}"
				+ "\r\n" + "public int getM() {return " + i + ";}" + "\r\n"
				+ "double f[]= new double[]{" + "\r\n";
		double f[] = new double[i];
		for (int j = 0; j < i; j++) {
			str = str + "" + g[j] + "," + "\r\n";
			f[j] = g[j];
		}
		str = str + "};}";
		//System.out.println(str);
		
		String[] s = null;
		for (String[] strings : list)
			if (strings[1].equals("Code"))
				s = strings;

		int index = pr.getProperties().indexOf(s);
		pr.getProperties().remove(pr.getProperties().size()-1);
		String[] fileNamed = { "String", "Code", str, "RuleClass", "textpane" };
		pr.getProperties().add(fileNamed);
		pr.setF(f);
		update();
		pr.calibFlag = true;
		propertiesPanel.addRows(pr);
	}


	private void doUpd(ProjGraphPA pr) {
		{
			double[] Q_;
			double[] Q;
			Factory<Integer> agentFactory = new Factory<Integer>() {
				int n = 0;

				public Integer create() {
					return n++;
				}
			};
			Factory<Integer> edgeFactory = new Factory<Integer>() {
				int n = 0;

				public Integer create() {
					return n++;
				}
			};
			String fileName = jTextPanePropertyValue.getText();
			Graph graph = new SparseGraph<Integer, Integer>();
			PajekNetReader<Graph<Integer, Integer>, Integer, Integer> pnr;
			try {
				pnr = new PajekNetReader<Graph<Integer, Integer>, Integer, Integer>(
						agentFactory, edgeFactory);
				File file = new File(fileName);
				pnr.load(fileName, graph);

			} catch (IOException e5) {
				System.out.println("error format");
			}
			Iterator<Integer> it = graph.getVertices().iterator();
			int length = 0;
			while (it.hasNext()) {
				int deg = graph.degree(it.next());
				if (deg > length)
					length = deg;
			}
			it = graph.getVertices().iterator();
			Q_ = new double[length + 1];
			boolean isDirected = false;
			while (it.hasNext()) {
				Integer node = it.next();

				int m = graph.degree(node);
				if(m!=graph.outDegree(node))isDirected=true;
				Q_[m] = Q_[m] + 1.0 / (2*((double) graph.getEdgeCount()));
			}
			pr.q_ = Q_;
			update();
			
			
			
			
			
			System.out.println("File:"+fileName);
			System.out.println("Directed:"+isDirected);
			System.out.println("Num of nodes:"+graph.getVertexCount());
			System.out.println("Num of edges:"+graph.getEdgeCount());
			System.out.println("количество кластеров:"+ Statistic.getClusters(graph).size());
			propertiesPanel.addRows(pr);
		}
	}

	private void doCalibrate2(ProjGraphPA pr) {
		{
			double[] Q_;
			Factory<Integer> agentFactory = new Factory<Integer>() {
				int n = 0;

				public Integer create() {
					return n++;
				}
			};
			Factory<Integer> edgeFactory = new Factory<Integer>() {
				int n = 0;

				public Integer create() {
					return n++;
				}
			};
			String fileName = jTextPanePropertyValue.getText();
			Graph graph = new SparseGraph<Integer, Integer>();
			PajekNetReader<Graph<Integer, Integer>, Integer, Integer> pnr;
			try {
				pnr = new PajekNetReader<Graph<Integer, Integer>, Integer, Integer>(
						agentFactory, edgeFactory);
				File file = new File(fileName);
				pnr.load(fileName, graph);

			} catch (IOException e5) {
				System.out.println("error format");
			}

			Iterator<Integer> it = graph.getVertices().iterator();
			int max = 0;	//max degree
			
			
			
			
			while (it.hasNext()) {
				int deg = graph.degree(it.next());
				if (deg > max)
					max = deg;
			}
			//calc 	max non zero and min non-zero degree
			int maxZ = 0;	
			int min = 0;	

			boolean[] deg = new boolean[max]; 
			it = graph.getVertices().iterator();
			while (it.hasNext()) {
				int d = graph.degree(it.next());
		
				if(d<deg.length)deg[d]=true;
			}
			boolean flag1=true;boolean flag2=false;
			
			for (int i = 0; i < deg.length; i++) {
				if (flag1&&deg[i])
					{min = i;flag1=false;flag2=true;}
				if(flag2&&!deg[i]){
					maxZ=i; flag2= false;
				}
			}
			
			double edgeCount =0.;
			it = graph.getVertices().iterator();
			
			int m = min;// (int) Math.round((graph.getEdgeCount()/(1.*graph.getVertexCount())));

			while (it.hasNext()) {
				int d = graph.degree(it.next());
				
				if((d>=m))//чтобы не считать входящие вероятности меньше m
							//ограничение на maxZ не делаю потому что получается неправдоподобный ряд
							//cходящийся к 0, который уже нет смысла аппроксимировать
					edgeCount=edgeCount+1.0;
			}
			
			//calc probabilities
			Q_ = new double[maxZ];
			
			it = graph.getVertices().iterator();
			while (it.hasNext()) {
				Integer node = it.next();
				int mi = graph.degree(node);
				if((mi<maxZ)&&(mi>=m))Q_[mi] = Q_[mi] + 1.0 / ((double) edgeCount);
				//if((mi<maxZ))Q_[mi] = Q_[mi] + 1.0 / ((double) graph.getVertexCount());
			}
			
			
			
			
			double sq=0.;
			for (int i = 0; i < Q_.length; i++) {
				sq=sq+Q_[i];
			}
			
			
			
			//System.out.println("Sq="+sq);
			//m=1;
			pr.q_ = Q_;
				double[] g = new double[Q_.length];
				 g = new double[Q_.length];
				 int i;
				if (pr instanceof PAttachStohasticGraphProjection) {
					 g[m] = pr.r[m] / Q_[m]-1;
					 i = m + 1;
					 for (; i < g.length; i++) {
						 double fi=0;
						 if(pr.r.length>i)fi=pr.r[i];
							 if (Q_[i] > 0)
								 	g[i] = Q_[i - 1] / Q_[i]* g[i - 1]+fi/Q_[i] - 1; 
							 else break; 
							 }
				}else{
					 g[m] = 1.0 / Q_[m]-1;
					 i = m + 1;
					 for (; i < g.length; i++) {
							 if (Q_[i] > 0)
								 	g[i] = Q_[i - 1] / Q_[i]* g[i - 1] - 1; 
							 else break; 
							 }
				}	 
				List<String[]> list = pr.getProperties();
				String str = ""
						+ "import simbigraph.graphs.prefAttachment.*;"
						+ "\r\n"
						+ "import simbigraph.gui.*;"
						+ "\r\n"
						+ "import simbigraph.core.*;"
						+ "\r\n"
						+ "public class prefRule extends PrefAttechRule{"
						+ "\r\n"
						+ "public double f(int k) {if(k<f.length)return f[k]; else return 0;}"
						+ "\r\n" + "public int getM() {return " + i + ";}"
						+ "\r\n" + "double f[]= new double[]{" + "\r\n";
				double f[] = new double[i];
				for (int j = 0; j < i; j++) {
					str = str + "" + g[j] + "," + "\r\n";
					f[j] = g[j];
				}
				str = str + "};}";

				String[] s = null;
				for (String[] strings : list)
					if (strings[1].equals("Code"))
						s = strings;

				int index = pr.getProperties().indexOf(s);
				pr.getProperties().remove(index);
				String[] fileNamed = { "String", "Code", str, "RuleClass",
						"textpane" };
				pr.getProperties().add(index, fileNamed);
				pr.setF(f);
				update();
				propertiesPanel.addRows(pr);
		}
	}

	protected void updateProb() {
		double sum = 0.;
		propertyValue = "";
		double av = 0;
		for (int i = 0; i < jTablePropertyValue.getModel().getRowCount(); i++) {
			sum = sum + Double.valueOf(jTablePropertyValue.getModel().getValueAt(
							i, 0).toString());
			av = av	+ i	* Double.valueOf(jTablePropertyValue.getModel().getValueAt(	i, 0).toString());
			propertyValue = propertyValue
					+ jTablePropertyValue.getModel().getValueAt(i, 0)
							.toString() + "/n";
		}
		DecimalFormat f = new DecimalFormat();
		f.setMaximumIntegerDigits(3);
		f.setMaximumFractionDigits(5);
		String str = f.format(sum).toString();
		jLabelPropertyName.setText(" Sum of Probability = " + str + "; Average connectivity = " + f.format(av).toString());
		update();

	}

	protected void changeItemProp() {
		propertyValue = "" + String.valueOf(jSpinnerX.getValue()) + ","
				+ String.valueOf(jSpinnerY.getValue());
		update();

	}

	protected void setNewVer() {
		// TODO Auto-generated method stub
		if (propertyValue != "") {
			int i = propertyValue.indexOf('\n');
			propertyValue = "" + jSpinnerPropertyValue.getValue().toString()
					+ propertyValue.substring(i);
			update();

		}
	}

	private void update() {
		if (selectedObject instanceof Projection) {
			Projection selectedNode = (Projection) selectedObject;

			for (int ii = 0; ii < selectedNode.getProperties().size(); ii++) {
				if (selectedNode.getProperties().get(ii)[1]
						.equalsIgnoreCase(propertyName)) {
					selectedNode.getProperties().get(ii)[2] = propertyValue;
				}
			}
			for (int ii = 0; ii < selectedNode.getEvents().size(); ii++) {
				if (selectedNode.getEvents().get(ii)[0]
						.equalsIgnoreCase(propertyName)) {
					selectedNode.getEvents().get(ii)[1] = propertyValue;
				}
			}

			if (propertyName.equalsIgnoreCase("Name")) {
				propertiesPanel.setHeaderName(selectedNode.getType());
			}
		}
		validate();
		propertiesPanel.validate();
	}

	private void jTextFieldPropertyValueKeyReleased(java.awt.event.KeyEvent evt) {
		propertyValue = jTextFieldPropertyValue.getText();
		update();
	}

	private void jTextPanePropertyValueKeyReleased(java.awt.event.KeyEvent evt) {
		propertyValue = jTextPanePropertyValue.getText();
		update();
	}

	private void jCheckBoxPropertyValueActionPerformed(
			java.awt.event.ActionEvent evt) {
		propertyValue = String.valueOf(jCheckBoxPropertyValue.isSelected());
		jCheckBoxPropertyValue.setText(propertyValue);
		update();
	}

	private void jComboBoxPropertyValueActionPerformed(
			java.awt.event.ActionEvent evt) {
		propertyValue = String
				.valueOf(jComboBoxPropertyValue.getSelectedItem());
		if (selectedObject instanceof PAttachStohasticGraphProjection) {
			((PAttachStohasticGraphProjection) selectedObject).setEventOrProperty(
					"VerTable", "0.0/n");
			((PAttachStohasticGraphProjection) selectedObject).q_ = null;
			((PAttachStohasticGraphProjection) selectedObject).f = null;

		}
		update();
		if (selectedObject instanceof Projection) {
			propertiesPanel.addRows((Projection) selectedObject);
			// ((Projection)selectedObject).setSelected(true);
		}
		// jComboBoxPropertyValue.setSelectedItem(propertyValue);
	}

	private void jSpinnerPropertyValueStateChanged(
			javax.swing.event.ChangeEvent evt) {
		// int stringLength =
		// jSpinnerNumberEditor.getTextField().getText().length();
		// jSpinnerNumberEditor.getTextField().setColumns(stringLength + 4);
		propertyValue=String.valueOf(jSpinnerPropertyValue.getValue());
		update();
	}
	protected void jSpinnerPropertyValueStateChanged2(ChangeEvent evt) {
		propertyValue = String.valueOf(jSpinnerX.getValue())+","+String.valueOf(jSpinnerY.getValue());
		update();
	}

	private void jSpinnerPropertyValueKeyReleased(java.awt.event.KeyEvent evt) {
		if (evt.getKeyChar() == '0' || evt.getKeyChar() == '1'
				|| evt.getKeyChar() == '2' || evt.getKeyChar() == '3'
				|| evt.getKeyChar() == '4' || evt.getKeyChar() == '5'
				|| evt.getKeyChar() == '6' || evt.getKeyChar() == '7'
				|| evt.getKeyChar() == '8' || evt.getKeyChar() == '9') {
			propertyValue = jSpinnerNumberEditor.getTextField().getText();
			update();
		} else {
			evt.setKeyChar(java.awt.event.KeyEvent.CHAR_UNDEFINED);
		}
	}

	private void jColorChooserValueActionPerformed(ActionEvent e) {
		Color c = null;
		if ((c = JColorChooser.showDialog(null, "Choose color", Color.white)) != null) {
			jButtonPropertyValue.setText(c.getRed() + "," + c.getGreen() + ","
					+ c.getBlue() + "," + c.getAlpha());

			propertyValue = jButtonPropertyValue.getText();
			update();
		}
	}

	private void jButtonSetNullValueActionPerformed(ActionEvent e) {
		jButtonPropertyValue.setText("null");
		propertyValue = "null";
		update();
	}

	private void jFileChooserValueActionPerformed(ActionEvent evt) {
		JFileChooser fc = null;
		fc = new JFileChooser();

		fc.setFileFilter(new NetGrhFilter());
		fc.setAcceptAllFileFilterUsed(false);

		try {

			fc.setCurrentDirectory(new File(new File("./graphs")
					.getCanonicalPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// fc.setFileFilter(new SlpFilter());
		fc.setAcceptAllFileFilterUsed(false);
		// if (lastDir != null)
		// fc.setCurrentDirectory(new File(lastDir));
		if (fc.showOpenDialog(null) == 0) {
			File selectedFile = fc.getSelectedFile();
			if (selectedFile != null) {
				jTextPanePropertyValue.setText(selectedFile.getAbsolutePath());
				propertyValue = selectedFile.getAbsolutePath();
				update();
			}
		}
	}

	private void jButtonPropertyValueActionPerformed(
			java.awt.event.ActionEvent evt) {
		JFrame jFrame = new JFrame("Java code editor");
		jFrame.setSize(800, 600);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		jFrame.setLayout(new BorderLayout());

		JScrollPane jScrollPane = new JScrollPane();
		// RTextScrollPane jScrollPane = new RTextScrollPane();
		// jScrollPane.setLineNumbersEnabled(true);
		jFrame.add("Center", jScrollPane);
		jTextPanePropertyValueML = new JTextPane();
		jTextPanePropertyValueML.setDocument(new JavaDocument());
		jTextPanePropertyValueML.setFont(new Font(Font.MONOSPACED, Font.PLAIN,
				14));
		jTextPanePropertyValueML.setText(propertyValue);
		jScrollPane.setViewportView(jTextPanePropertyValueML);
		jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				formWindowClosed(evt);
			}

			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosed(evt);
			}
		});
	}

	void formWindowClosed(java.awt.event.WindowEvent evt) {
		propertyValue = jTextPanePropertyValueML.getText();
		jTextPanePropertyValue.setText(propertyValue);
		update();
	}

	private void setPrVal(String string, String ver) {
		propertyValue = "" + ver + "\r\n" + string;
		update();
	}

	private String getPropertyValue() {
		return propertyValue;
	}

}
