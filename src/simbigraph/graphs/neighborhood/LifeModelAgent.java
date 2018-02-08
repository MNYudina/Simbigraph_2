package simbigraph.graphs.neighborhood;


import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import simbigraph.grid.model.Grid;
import simbigraph.math.RandomHelper;

import edu.uci.ics.jung.graph.util.Pair;




public class LifeModelAgent extends Agent {
	private boolean border =false;
	private boolean seed =false;
	
	private String name;
	private Color node_color;					//  ����� ����� ���� ���� �������	
	LifeModelAgent lifeModelAgent;				//  ������ ��  �����
	private double strength;	
	//  ���� �����		 				

	// ----------------------------------------------------------------
	public LifeModelAgent(int x, int y, double _strength) {
		
		this.x = x;		this.y = y;				// �� ����������� � ���� ���������
												// �������� � �������� �������
		this.lifeModelAgent = this;				//  ����� ��������� �� ����
		node_color = new Color(
		RandomHelper.nextIntFromTo(50, 200), 	//  ���� ����� 
		RandomHelper.nextIntFromTo(50, 200),		//  ������� ��������
		RandomHelper.nextIntFromTo(50, 200));
		seed =true;
		//strength =0.5;
		strength =_strength;
		//if(0.1>RandomHelper.nextDoubleFromTo(0.01, 0.99)) strength=0.9;
	}

	// ----------------------------------------------------------------
	public LifeModelAgent(int x2, int y2, LifeModelAgent lifeModelAgent) {
		
		this.x = x2; this.y = y2;		

		this.lifeModelAgent = lifeModelAgent;
		node_color = this.lifeModelAgent.getColor();// ���� � ���� ��� � 
		strength = lifeModelAgent.getStrendth();	// �����-��������
	}

	// ----------------------------------------------------------------------------------
	public void step() {
		Grid grid = MainNeiGraph.grid;
		int xdim =grid.getDimensions().getWidth();
		int ydim = grid.getDimensions().getHeight();
			for (int j = y - 1; j <= y + 1; j++) {
				for (int i = x - 1; i <= x + 1; i++) {
					if (!(j == y && i == x)) {
						//���� �������, �� �������� ������� �� ����, � ���� �������� ��� ���������
						if(!((i==-1)||(j==-1)||(i==xdim)||(j==ydim))){
							Object obj = grid.getObjectAt(i, j);
							if (obj == null) {
											
											// ���� ������ ������ ����� ������� ��������					
								DumpAgent newDumpAgent = new DumpAgent(this, i, j);
								grid.moveTo(newDumpAgent, newDumpAgent.getX(),	newDumpAgent.getY());
								SimulationNei.currentDumpsAgents.add(newDumpAgent);

							} else if (obj instanceof DumpAgent) {

											//���� ������ �������� �� � �����
											//�������� ������ ��� ����� ��� ++�������
							
								((DumpAgent) obj).addToMap(this);
							
							} else if (obj instanceof LifeModelAgent) {
											//���� �� ������ ������ �������
											//�� ������ �����, ���� ��� �� �������
								if (((LifeModelAgent) obj).getPar() != lifeModelAgent) {
									MainNeiGraph.graph.addEdge(MainNeiGraph.edgeFactory.create(), lifeModelAgent, ((LifeModelAgent) obj).getPar());
								}
							}
						} else {
							//System.out.println("���, ����������!!");
							getPar().setBorder(true);
						}
					}
			}
		}
			
	}

	public void setBorder(boolean b) {
		border=b;
	}
	public boolean isBorder()
	{
		return false;
		//return border; 
	}

	// ----------------------------------------------------------------
	public LifeModelAgent getPar() {
		return lifeModelAgent;
	}

	// ----------------------------------------------------------------
	public int getX() {
		return x;
	}

	// ----------------------------------------------------------------
	public int getY() {
		return y;
	}

	// ----------------------------------------------------------------
	public Color getColor() {
		if(!isBorder())
		return node_color; else return Color.BLACK;
		
	}

	// ------------------------------------------------------------------
	public double getStrendth() {
		//if(!b)
		return strength;
	//	else
		//	return 1-strength;
	}

	public void setPar(LifeModelAgent par) {
		lifeModelAgent = par;
	}

	public boolean isSeed() {
		return seed;
	}
}
