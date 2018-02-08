package simbigraph.graphs.prefAttachment;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.GraphMLReader;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.DefaultVertexIconTransformer;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import edu.uci.ics.jung.visualization.util.PredicatedParallelEdgeIndexFunction;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.xml.sax.SAXException;

import simbigraph.graphs.decomposition.SimbigraphNode;


/**
 *
 * @author Yudin
 */

public class DecompEditGraphPanel extends javax.swing.JPanel {

    
    Graph<SimbigraphNode, String> g;
    public void doBeginer(java.awt.event.ActionEvent evt)
    {
                   Collection picked = new HashSet(vv.getPickedVertexState().getPicked());
                    if(picked.size() == 1) {
                	 SimbigraphNode first = (SimbigraphNode) picked.iterator().next();
                	 first.state=-5;
                     Graph inGraph = layout.getGraph();

                     vv.getRenderContext().getParallelEdgeIndexFunction().reset();
                     layout.setGraph(inGraph);
                     vv.getPickedVertexState().clear();
                     vv.repaint();
                 }
    }
    ///========================
    public void doEnd(java.awt.event.ActionEvent evt)
    {
          Collection picked = new HashSet(vv.getPickedVertexState().getPicked());
                 if(picked.size() == 1) {
                	 SimbigraphNode first = (SimbigraphNode) picked.iterator().next();
                	 first.state=5;
                     Graph inGraph = layout.getGraph();

                     vv.getRenderContext().getParallelEdgeIndexFunction().reset();
                     layout.setGraph(inGraph);
                     vv.getPickedVertexState().clear();
                     vv.repaint();
                     
                 }
    }
    ///========================
    public void doNormal(java.awt.event.ActionEvent evt)
    {
          Collection picked = new HashSet(vv.getPickedVertexState().getPicked());
                 if(picked.size() == 1) {
                	 SimbigraphNode first = (SimbigraphNode) picked.iterator().next();
                	 first.state=0;
                     Graph inGraph = layout.getGraph();
                     vv.getRenderContext().getParallelEdgeIndexFunction().reset();
                     layout.setGraph(inGraph);
                     vv.getPickedVertexState().clear();
                     vv.repaint();
                 }
    }
    ///=======================
    public void doOrdinary(java.awt.event.ActionEvent evt)
    {
      Collection picked = new HashSet(vv.getPickedVertexState().getPicked());
                 if(picked.size() == 1) {
                	 SimbigraphNode first = (SimbigraphNode) picked.iterator().next();
                	 first.state=0;
                     Graph inGraph = layout.getGraph();

                     vv.getRenderContext().getParallelEdgeIndexFunction().reset();
                     layout.setGraph(inGraph);
                     vv.getPickedVertexState().clear();
                     vv.repaint();
                 }
    }
    ///========================
    public  void onLoad(DirectedSparseGraph gr) {
	
        //layout.setGraph(gr);
        g=gr;
        vv.repaint();
      }
    //============================
    public  JComponent getComboMouse() {

        return (JComponent)gm.getModeComboBox();
       }
    public  JComponent getComboView() {

    	     return (JComponent)layoutTypeComboBox;
    }
    //============================
    public  JMenuItem getMode(int mode) {
        //( JMenuItem)gm.getModeMenu().;
        System.out.println("ff"+gm.getModeMenu().getComponentCount());
        JMenuItem ct = (JMenuItem)(JComponent)modeMenu.getMenuComponent(mode);
        return ct;
    }
    GraphCollapser collapser;
    int mode;
    int nodeCount, edgeCount;
    JComboBox layoutTypeComboBox;
    public Factory<SimbigraphNode> vertexFactory;
    public Factory<String> edgeFactory;
    public EditingModalGraphMouse2 gm;
    FRLayout<SimbigraphNode, String> layout;
    public VisualizationViewer<SimbigraphNode, String> vv;
    final JMenu modeMenu;
	//Class[] layoutClasses = new Class[]{CircleLayout.class,SpringLayout.class,FRLayout.class,KKLayout.class};
    public Graph<SimbigraphNode, String> getGraph() {
        return g;
    }
    public GraphCollapser getCollapser() {
        return collapser;
    }
    //Класс подменяет стандартное изображение вершины, нашим

    public  class VertexIconTransformer<Object> extends DefaultVertexIconTransformer<Object> implements Transformer<Object, Icon> {

        public Icon transform(Object v) {
            ImageIcon a;
            if (((SimbigraphNode) v).state == -5) {
                a = new ImageIcon(getClass().getResource("/images/IconSource.png"));//new ImageIcon("./img/big.gif");
            } else if (((SimbigraphNode) v).state == 5) {
                a = new ImageIcon(getClass().getResource("/images/IconSink.png"));//new ImageIcon("./img/end.gif");
            } else {
                a = new ImageIcon(getClass().getResource("/images/IconStop.png"));//new ImageIcon("./img/red.gif");
            }
            Icon icon = new LayeredIcon(a.getImage());
            return icon;
        }
    }

    public DecompEditGraphPanel() {
        initComponents();
        g = new  SparseMultigraph<SimbigraphNode, String>();
        collapser = new GraphCollapser(g);
        edgeCount = 0;
        vertexFactory = new Factory<SimbigraphNode>() { // Фабрика вершин
            public SimbigraphNode create() {
                return new SimbigraphNode(nodeCount++);
            }
        };
        edgeFactory = new Factory<String>() { // Фабрика ребер
            public String create() {
                return "E" + edgeCount++;
            }
        };
         layout =new FRLayout<SimbigraphNode, String>(getGraph());

        vv = new VisualizationViewer<SimbigraphNode, String>(layout);
        gm = new EditingModalGraphMouse2(vv.getRenderContext(), vertexFactory, edgeFactory);
        final PredicatedParallelEdgeIndexFunction eif = PredicatedParallelEdgeIndexFunction.getInstance();
        final Set exclusions = new HashSet();
        eif.setPredicate(new Predicate() {
            public boolean evaluate(Object e) {
                return exclusions.contains(e);
            }
        });
        vv.getRenderContext().setParallelEdgeIndexFunction(eif);
        // Эти строки настраивают выводимый текст при вершинах и ребрах
        vv.getRenderContext().setVertexLabelTransformer(
                new Transformer<SimbigraphNode, String>() {
                    public String transform(SimbigraphNode input) {
                        String l = "";
//                        if (input instanceof Graph) {
//                            Collection c = ((Graph) input).getVertices();
//                            for (Object v : c) {
//                                l = l + ((MyNode) v).count;
//                            }
//                        } else {
//                            l = "" + input;
//                        }
                        if(input.count==1111) l="начало";
                        if(input.count==3333) l="конец";
                        return l;
                    }
                });
        //  vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        //Этот класс заменяет изображение вершины нашим
        final VertexIconTransformer<SimbigraphNode> vertexIconTransformer = new VertexIconTransformer<SimbigraphNode>();
        vv.getRenderContext().setVertexIconTransformer(vertexIconTransformer);
        //Управляет курсором` мыши
        vv.setBackground(Color.WHITE);
        vv.setGraphMouse(gm);
        vv.setVisible(true);
        vv.repaint();
        GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
        this.add(gzsp);
        setVisible(true);
        modeMenu= gm.getModeMenu();
        gm.setMode(Mode.EDITING);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }
	public void loadGraph(StringReader sr) {

        GraphMLReader<Graph<SimbigraphNode, String>, SimbigraphNode, String> gmlr = null;
        try {
            gmlr = new GraphMLReader<Graph<SimbigraphNode, String>, SimbigraphNode, String>(
                    vertexFactory, edgeFactory);
           char f;
           do{ 
        	   f =(char) sr.read();
        	   System.out.print(""+f);
           }while(f!='\n');
            Graph<SimbigraphNode, String> graph = new   SparseMultigraph<SimbigraphNode, String>();
           ;
            gmlr.load(sr, graph);
            g=graph;

        } catch (SAXException e2) {
            System.out.println("#");
        } catch (ParserConfigurationException e6) {
            System.out.println("##");
        } catch (IOException e5) {
            System.out.println("###"+e5);
        }
        System.out.println("граф=" + g.toString());
        System.out.println("Узлов=" + g.getVertexCount());
        System.out.println("Узлов=" + g.getEdgeCount());

        Transformer<SimbigraphNode, String> t_n = gmlr.getVertexMetadata().get("state").transformer;
        for (SimbigraphNode me : g.getVertices()) 
        {
            me.setState(t_n.transform(me));
        }
        vv.getRenderContext().getParallelEdgeIndexFunction().reset();
        layout.setGraph(g);
        vv.getPickedVertexState().clear();
        vv.repaint();

	}
	EdgeType defaultEdgeType;
	public void setTypeEdge(EdgeType directed) {
		defaultEdgeType=directed;
		gm.setDirected(directed);
	}        
}
