package math;

public class test {
	public static void main(String[] args) {
		Vertex u = new Vertex("u");
		Vertex v = new Vertex("v");
		Vertex x = new Vertex("x");
		Vertex y = new Vertex("y");
		Vertex z = new Vertex("z");
		Vertex[] V = {u, v, x, y, z};
		Edge[] E = {new Edge(u, v, 1), new Edge(v, u, 1), new Edge(u, x, 2), new Edge(x, u, 2), new Edge(x, y, 1),
				new Edge(y, x, 1), new Edge(y, v, 15), new Edge(v, y, 15), new Edge(v, z, 5), new Edge(v, z, 5),
				new Edge(z, x, 2), new Edge(x, z, 2), new Edge(z, y, 10), new Edge(y, z, 10)};		
		for (Vertex k : V) {
			if (k != z) k.d = Integer.MAX_VALUE;			
		}
		for (int i = 0; i < 2; i++) {
			for (Edge k : E) {
				k.b.d = Math.min(k.a.d + k.c, k.b.d);
			}
		}
		for (Vertex k : V) {
			for (Vertex l : V) {
				System.out.println();
			}
		}
	}
	
	static class Vertex {
		public int d;
		public String name;
		public Vertex(String name) {
			this.name=name;
		}
	}
	static class Edge {
		public Vertex a, b;
		public int c;
		public Edge(Vertex a, Vertex b, int c) {
			this.a = a;
			this.b=b;
			this.c = c;
		}
	}
}
