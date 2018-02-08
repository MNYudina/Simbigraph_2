package temp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import simbigraph.util.Statistic;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

class XNode {
	public XNode() {
	}

}

public class TestBA_R2 {

	public static void main(String[] args) {
		int m = 2;
		int N = 100000;
		int sumE=0;
		
		List<XNode> virtexes = new ArrayList();
		// разыгрываем ki и формируем список входящих полурёбер - list
		List<XNode> list = new ArrayList();
		List<XNode> virgin = new ArrayList();
		Graph<XNode, Integer> graph = new UndirectedSparseMultigraph<XNode, Integer>();
		int distr[] = new int[10];
		for (int i = 0; i < N; i++) {
			int k_i = getVirtexK(m);
			if (k_i < distr.length)
				distr[k_i] = distr[k_i] + 1;
			sumE=sumE+k_i;

			XNode virt = new XNode();
			graph.addVertex(virt);
			virtexes.add(virt);
			for (int j = 0; j < k_i - m; j++) {
				list.add(virt);
			}
			if (k_i == m)
				virgin.add(virt);
		}
		System.out.println("sumE:"+sumE);
		System.out.println("-------2-------------");
		for (int i = 0; i < distr.length; i++) {
			System.out.println(distr[i]);
		}
		// ===========

		Random rand = new Random();
		// работаем с матрицей смежности mass
		int edgeCount = 0;

		for (XNode a : virtexes) {
			for (int s = 0; s < m; s++) {
				XNode b;
				do {
					int index = rand.nextInt(list.size());
					b = list.get(index);
				} while (a == b);
				if (virgin.contains(b))
					System.out.println("косяк");
				if (!list.contains(b))
					System.out.println("косяк2");

				boolean b1 = graph.addEdge(new Integer(edgeCount++), a, b);
				if (!b1)
					System.out.println("false");
			}
		}
		System.out.println("|E|" + graph.getEdgeCount());
		// System.out.println("число кластеров"+Statistic.getClusters(graph).size());
		writeNodesDegrees(graph, 10);
	}

	private static int getVirtexK(int m) {
		int l = m;
		int k = m;
		double rand = Math.random();
		double tr = 0;
		while (true) {
			tr = tr + (2.0 * l * (l + 1)) / (1.0 * k * (k + 1) * (k + 2));
			if (rand < tr) {
				break;
			}
			k++;
		}
		return k;
	}

	public static void writeNodesDegrees(Graph<XNode, Integer> graph, int length) {
		System.out.println("выводим степени связности");

		Iterator<XNode> it = graph.getVertices().iterator();
		long c1 = 1;

		int[] distr0 = new int[length];
		while (it.hasNext()) {
			XNode node = it.next();
			{
				if (graph.degree(node) < length)
					distr0[graph.degree(node)] = distr0[graph.degree(node)] + 1;
			}
		}
		for (int i = 0; i < distr0.length; i++)
			System.out.println(distr0[i]);
	}
}
