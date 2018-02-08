package simbigraph.delaune;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

public class DelauneyPanel extends JPanel {
	DelaunaeGraphPanel panelGraphView;
	public Triangle[] triangulateDelaunay(int[] x, int[] y) {
		int n = x.length;
		int[] z = new int[n];
		for (int i = 0; i < n; i++)
			z[i] = x[i] * x[i] + y[i] * y[i];
		List<Triangle> res = new ArrayList<Triangle>();
		for (int i = 0; i < n - 2; i++) {
			for (int j = i + 1; j < n; j++) {
				m1: for (int k = i + 1; k < n; k++) {
					if (j == k)
						continue;
					long nx = (y[j] - y[i]) * (z[k] - z[i]) - (y[k] - y[i])
							* (z[j] - z[i]);
					long ny = (x[k] - x[i]) * (z[j] - z[i]) - (x[j] - x[i])
							* (z[k] - z[i]);
					long nz = (x[j] - x[i]) * (y[k] - y[i]) - (x[k] - x[i])
							* (y[j] - y[i]);
					if (nz >= 0)
						continue;
					for (int m = 0; m < n; m++) {
						long dot = (x[m] - x[i]) * nx + (y[m] - y[i]) * ny
								+ (z[m] - z[i]) * nz;
						if (dot > 0)
							continue m1;
					}
					int[] s1 = { i, j, k, i };
					for (Triangle t : res) {
						int[] s2 = { t.a, t.b, t.c, t.a };
						for (int u = 0; u < 3; u++)
							for (int v = 0; v < 3; v++)
								if (proverka(x[s1[u]], y[s1[u]], x[s1[u + 1]],
										y[s1[u + 1]], x[s2[v]], y[s2[v]],
										x[s2[v + 1]], y[s2[v + 1]]))
									continue m1;
					}
					res.add(new Triangle(i, j, k));
				}
			}
		}
		return res.toArray(new Triangle[0]);
	}

	boolean proverka(long x1, long y1, long x2, long y2, long x3, long y3,
			long x4, long y4) {
		long z1 = (x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1);
		long z2 = (x4 - x1) * (y2 - y1) - (y4 - y1) * (x2 - x1);
		if (z1 < 0 && z2 < 0 || z1 > 0 && z2 > 0 || z1 == 0 || z2 == 0)
			return false;
		long z3 = (x1 - x3) * (y4 - y3) - (y1 - y3) * (x4 - x3);
		long z4 = (x2 - x3) * (y4 - y3) - (y2 - y3) * (x4 - x3);
		if (z3 < 0 && z4 < 0 || z3 > 0 && z4 > 0 || z3 == 0 || z4 == 0)
			return false;
		return true;
	}

	JPanel ob;

	public DelauneyPanel() {
		super();
	
		
	
	}

	public void init() {
		ob = new JPanel();
		ob.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		final JTextField chiclo = new JTextField("100");
		JPanel vv = new JPanel();
		Box vb = Box.createVerticalBox();
		JButton real = new JButton("Сгенерировать");
		JLabel lbl = new JLabel("Введите количество вершин графа:");
		// setLayout(new BorderLayout());
		vb.add(lbl);
		vb.add(Box.createVerticalStrut(5));
		vb.add(chiclo);
		vb.add(Box.createVerticalStrut(7));
		vb.add(real);
		vv.add(vb);
		ob.add(vv, BorderLayout.EAST);

		real.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int n;
				try {
					n = Integer.parseInt(chiclo.getText());
				} catch (Exception exception) {
					JFrame err = new JFrame("Сообщение об ошибке");
					err.setPreferredSize(new Dimension(300, 250));
					JLabel och = new JLabel(
							"Исходные значения введены не правильно!!!");
					err.add(och);
					err.pack();
					err.setVisible(true);
					chiclo.setText("");
				}
				n = Integer.parseInt(chiclo.getText());
				final int[] x = new int[n];
				final int[] y = new int[n];
				Random rnd = new Random();
				for (int i = 0; i < n; i++) {
					x[i] = rnd.nextInt(500);
					y[i] = rnd.nextInt(500);
				}
				final Triangle[] ts = triangulateDelaunay(x, y);
				final JPanel panel = new JPanel() {
					protected void paintComponent(Graphics g) {
						// System.out.println("cdfv");
						super.paintComponent(g);
						((Graphics2D) g).setStroke(new BasicStroke(2));
						g.setColor(Color.BLUE);

						for (Triangle t : ts) {
							g.drawLine((int) x[t.a], (int) y[t.a],
									(int) x[t.b], (int) y[t.b]);
							g.drawLine((int) x[t.a], (int) y[t.a],
									(int) x[t.c], (int) y[t.c]);
							g.drawLine((int) x[t.c], (int) y[t.c],
									(int) x[t.b], (int) y[t.b]);
						}
						g.setColor(Color.black);
						for (int i = 0; i < x.length; i++) {
							g.drawOval((int) x[i] - 1, (int) y[i] - 1, 3, 3);
						}
					}
				};
				Graph<DelanuneyAgent,Integer> gr = new SparseGraph();

				DelanuneyAgent[] massVirt=new DelanuneyAgent[n];
				for (int i = 0; i < n; i++) {
					massVirt[i]=new DelanuneyAgent(x[i],y[i]);
					gr.addVertex(massVirt[i]);
				}
				Factory<Integer> edgeFactory = new Factory<Integer>() {
					int n = 0;
					public Integer create() {
						return n++;
					}
				};

				for (Triangle t : ts) {
					//Collection<DelanuneyAgent> colVirt=gr.getVertices();
					if(!gr.isNeighbor(massVirt[t.a], massVirt[t.b]))gr.addEdge(edgeFactory.create(), massVirt[t.a],massVirt[t.b]); 
					if(!gr.isNeighbor(massVirt[t.b], massVirt[t.c]))gr.addEdge(edgeFactory.create(), massVirt[t.b],massVirt[t.c]); 
					if(!gr.isNeighbor(massVirt[t.c], massVirt[t.a]))gr.addEdge(edgeFactory.create(), massVirt[t.c],massVirt[t.a]); 

					/*g.drawLine((int) x[t.a], (int) y[t.a],
							(int) x[t.b], (int) y[t.b]);
					g.drawLine((int) x[t.a], (int) y[t.a],
							(int) x[t.c], (int) y[t.c]);
					g.drawLine((int) x[t.c], (int) y[t.c],
							(int) x[t.b], (int) y[t.b]);*/
				}

				
				
				panelGraphView.updateGraph(gr);
				panel.setSize(new Dimension(500, 500));
				ob.add(panel);
				panel.repaint();

			}
		});
		ob.setPreferredSize(new Dimension(200,300));
		add(ob);

		invalidate();
		validate();
		setVisible(true);
	}

	public void setGraphView(DelaunaeGraphPanel p) {
		panelGraphView=p;
	}
}
