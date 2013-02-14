package network.dr.alg.anu.au;

import org.jgrapht.graph.DefaultEdge;

public class UnDirectedLink extends DefaultEdge {
	// ~ Static fields/initializers
	// ---------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 89549103738832235L;
	private double linkReliability = 0.6;// initial link reliability

	// ~ Methods
	// ----------------------------------------------------------------

	public UnDirectedLink() {
		super();
		this.linkReliability = 0.6;
	}

	public UnDirectedLink(double lr) {
		super();
		this.linkReliability = lr;
	}

	/**
	 * Retrieves the source of this edge. This is protected, for use by
	 * subclasses only (e.g. for implementing toString).
	 * 
	 * @return source of this edge
	 */
	public Object getSource() {
		return super.getSource();
	}

	/**
	 * Retrieves the target of this edge. This is protected, for use by
	 * subclasses only (e.g. for implementing toString).
	 * 
	 * @return target of this edge
	 */
	public Object getTarget() {
		return super.getTarget();
	}

	public String toString() {
		return super.toString();
	}

	public double getLinkReliability() {
		return linkReliability;
	}

	public void setLinkReliability(double linkReliability) {
		this.linkReliability = linkReliability;
	}

	@Override
	public boolean equals(Object another) {

		// Check for self-comparison
		if (this == another)
			return true;

		// Use instanceof instead of getClass here for two reasons
		// 1. if need be, it can match any supertype, and not just one class;
		// 2. it renders an explict check for "that == null" redundant, since
		// it does the check for null already - "null instanceof [type]" always
		// returns false.
		if (!(another instanceof UnDirectedLink))
			return false;

		Object thisS = this.getSource();
		Object anotherS = ((UnDirectedLink) another).getSource();

		Object thisT = this.getTarget();
		Object anotherT = ((UnDirectedLink) another).getTarget();

		// The algorithm only accepts comparison between identical V types.
		if ((!(thisS instanceof Node)) && (!(anotherS instanceof Node))
				&& (!(thisT instanceof Node)) && (!(anotherT instanceof Node)))
			return false;
		
		if((((Node)thisS).getId() == ((Node)anotherS).getId())&&(((Node)thisT).getId() == ((Node)anotherT).getId()))
			return true;
		if((((Node)thisS).getId() == ((Node)anotherT).getId())&&(((Node)thisT).getId() == ((Node)anotherS).getId()))
			return true;
		
		return false;
	}
}
