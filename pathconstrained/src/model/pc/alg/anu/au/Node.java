package model.pc.alg.anu.au;

import util.pc.alg.anu.au.CommonFacility;

public class Node {

	private double x;
	private double y;
	private int id;
	
	
	public Node() {
		super();
		// TODO Auto-generated constructor stub
		this.id=CommonFacility.getID();
	}


	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public int getId() {
		return id;
	}


	
	
	@Override
	public String toString() {
		return "Node [id=" + id + ", x=" + x + ", y=" + y + "]";
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
