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


public class TestBA_R3 {

	public static void main(String[] args) {
		int m = 2;
		int N = 1000;
		int sumE=0;
		int[][] mass= new int[N][N];
		List<Integer> virtexes = new ArrayList();
		// разыгрываем ki и формируем список входящих полурёбер - list
		List<Integer> list = new ArrayList();
		List virgin = new ArrayList();
		int distr[] = new int[10];
		for (int i = 0; i < N; i++) {
			int k_i = getVirtexK(m);
			if (k_i < distr.length)
				distr[k_i] = distr[k_i] + 1;
			sumE=sumE+k_i;

			Integer virt = new Integer(i);
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
		Random rand = new Random();
		// работаем с матрицей смежности mass

		//for (Integer a : virtexes) {
			for (int i = 0; i < mass.length; i++) {
			Integer a= virtexes.get(i);	
			
			for (int s = 0; s < m; s++) {
				Integer b;
				do {
					int index = rand.nextInt(list.size());
					b = list.get(index);
				} while (a == b);

				mass[a][b]=mass[a][b]+1;
				mass[b][a]=mass[b][a]+1;
				if(virgin.contains(b))System.out.println("косяк");
			}
		}
		
		
		System.out.println("-----------------------------");
		int distr2[] = new int[10];
		int sumE2=0;
		for (int i = 0; i < mass.length; i++) {
			int sum =0;
			for (int j = 0; j < mass[i].length; j++) {
				sum=mass[i][j]+sum;
				
			}
			sumE2=sumE2+sum;
			if(distr2.length>sum)distr2[sum] = distr2[sum] + 1;
		}
		System.out.println("sumE2:"+sumE2);

		for (int i = 0; i < distr2.length; i++) {
			System.out.println(distr2[i]);
		}
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

	public static void writeNodesDegrees(Graph<Integer, Integer> graph, int length) {
		System.out.println("выводим степени связности");

		Iterator<Integer> it = graph.getVertices().iterator();
		long c1 = 1;

		int[] distr0 = new int[length];
		while (it.hasNext()) {
			Integer node = it.next();
			{
				if (graph.degree(node) < length)
					distr0[graph.degree(node)] = distr0[graph.degree(node)] + 1;
			}
		}
		for (int i = 0; i < distr0.length; i++)
			System.out.println(distr0[i]);
	}
}
