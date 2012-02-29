/* -------------------------
 * NetworkGenerator.java
 * -------------------------
 *
 * Original Author:  Zichuan Xu.
 *
 * The node class V need to have four parameters: id, x location, y location, rest energy and current energy.
 *
 * Changes
 * -------
 * 2-Dec-2011 : Initial revision (GB);
 *
 */

package generate.dr.alg.anu.au;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.GraphGenerator;



public class NetworkGenerator<V, E> implements GraphGenerator<V, E, V> {
	// ~ Instance fields
	// --------------------------------------------------------

	private double transmissionRange;
	private int nSize;

	// ~ Constructors
	// -----------------------------------------------------------

	/**
	 * Construct a new NetworkGenerator.
	 * 
	 * @param nSize
	 *            number of vertices to be generated
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified size is negative.
	 */
	public NetworkGenerator(int nSize, int gSize, double transmissionRange) {
		if (nSize < 0) {
			throw new IllegalArgumentException("must be non-negative");
		}

		this.nSize = nSize;
		this.transmissionRange=transmissionRange;
	}

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public void generateGraph(Graph<V, E> target,
			VertexFactory<V> vertexFactory, Map<String, V> resultMap) {
		if (nSize < 1) {
			return;
		}

		// Add all the vertices to the set
		for (int i = 0; i < nSize; i++) {
			V newVertex = vertexFactory.createVertex();
			target.addVertex(newVertex);
		}

		/*
		 * We want two iterators over the vertex set, one fast and one slow. The
		 * slow one will move through the set once. For each vertex, the fast
		 * iterator moves through the set, adding an edge to all vertices we
		 * haven't connected to yet.
		 * 
		 * If we have an undirected graph, the second addEdge call will return
		 * nothing; it will not add a second edge.
		 */
		Iterator<V> slowI = target.vertexSet().iterator();
		Iterator<V> fastI;
		while (slowI.hasNext()) { // While there are more vertices in the
									// set

			V latestVertex = slowI.next();
			fastI = target.vertexSet().iterator();

			// Jump to the first vertex *past* latestVertex
			while (fastI.next() != latestVertex) {
				;
			}

			// And, add edges to all remaining vertices
			V temp;
			while (fastI.hasNext()) {
				temp = fastI.next();
				double distX, distY, dist;
				try {
					distX = (Double) temp.getClass().getMethod("getX")
							.invoke(temp, null)
							- (Double) latestVertex.getClass()
									.getMethod("getX")
									.invoke(latestVertex, null);
					distY = (Double) temp.getClass().getMethod("getY")
							.invoke(temp, null)
							- (Double) latestVertex.getClass()
									.getMethod("getY")
									.invoke(latestVertex, null);
					dist = Math.sqrt(distX * distX + distY * distY);
					if (dist <= this.transmissionRange) {
						target.addEdge(latestVertex, temp);
						target.addEdge(temp, latestVertex);
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void generateGraphFromNodeArray(Graph<V, E> target, V nodes[]) {

		// Add all the vertices to the set
		for (int i = 0; i < nodes.length; i++) {
			V newVertex = nodes[i];
			target.addVertex(newVertex);
		}
		/*
		 * We want two iterators over the vertex set, one fast and one slow. The
		 * slow one will move through the set once. For each vertex, the fast
		 * iterator moves through the set, adding an edge to all vertices we
		 * haven't connected to yet.
		 * 
		 * If we have an undirected graph, the second addEdge call will return
		 * nothing; it will not add a second edge.
		 */
		Iterator<V> slowI = target.vertexSet().iterator();
		Iterator<V> fastI;

		while (slowI.hasNext()) { // While there are more vertices in the set

			V latestVertex = slowI.next();
			fastI = target.vertexSet().iterator();

			// Jump to the first vertex *past* latestVertex
			while (fastI.next() != latestVertex) {
				;
			}

			// And, add edges to all remaining vertices
			V temp;
			while (fastI.hasNext()) {
				temp = fastI.next();
				double distX, distY, dist;
				try {
					distX = (Double) temp.getClass().getMethod("getX")
							.invoke(temp, null)
							- (Double) latestVertex.getClass()
									.getMethod("getX")
									.invoke(latestVertex, null);
					distY = (Double) temp.getClass().getMethod("getY")
							.invoke(temp, null)
							- (Double) latestVertex.getClass()
									.getMethod("getY")
									.invoke(latestVertex, null);
					dist = Math.sqrt(distX * distX + distY * distY);
					if (dist <= this.transmissionRange) {
						target.addEdge(latestVertex, temp);
						target.addEdge(temp, latestVertex);
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
