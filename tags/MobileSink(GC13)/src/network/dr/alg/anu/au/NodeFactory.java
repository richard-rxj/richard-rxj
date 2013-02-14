/* -------------------------
 * NodeFactory.java
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

package network.dr.alg.anu.au;

import java.lang.reflect.Constructor;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;

public class NodeFactory<V> implements VertexFactory<V> {
	// ~ Instance fields
	// --------------------------------------------------------

	public static int nodesProduced = 0;
	private Random ran = new Random();
	private int rangeX = 100;
	private int rangeY = 100;
	private final Class<? extends V> vertexClass;

	/*
	 * In mode 0(g == null), nodes are generated one by one. In mode 1(g !=
	 * null), the vertices are generated from a existing graph.
	 */

	// ~ Constructors
	// -----------------------------------------------------------

	public NodeFactory(Class<? extends V> vertexClass, int rangeX, int rangeY) {
		this.vertexClass = vertexClass;
		this.rangeX = rangeX;
		this.rangeY = rangeY;
	}

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * @see VertexFactory#createVertex()
	 */
	public V createVertex() {
		try {
			Constructor con = this.vertexClass
					.getConstructor(new Class[] { NodeInitialParameters.class });
			if (null != con) {
				double energy = ran.nextDouble();
				NodeInitialParameters ni = new NodeInitialParameters();
				ni.id = nodesProduced++;
				ni.x = ran.nextDouble() * this.rangeX;
				ni.y = ran.nextDouble() * this.rangeY;
				ni.ce = energy;
				ni.re = energy;
				return (V) con.newInstance(ni);
			} else {
				return this.vertexClass.newInstance();
			}

		} catch (Exception e) {
			throw new RuntimeException("Vertex factory failed", e);
		}

	}
}