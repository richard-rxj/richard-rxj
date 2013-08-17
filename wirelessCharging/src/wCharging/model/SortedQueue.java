/**
 * 
 */
package wCharging.model;

import java.util.LinkedList;

/**
 * @author user
 *
 */
public class SortedQueue<E> extends LinkedList<E>{

	private Link<E> first;
	private Link<E> last;
	
	
	
/**
	 * 
	 */
	public SortedQueue() {
		//super();
		first=null;
		last=null;
	}

private class Link<E>
{
	public Comparable<E> data;
	public Link next;
}

   @Override
   public boolean add(E obj)
   {
	   Link newLink =new Link();
	   newLink.data=(Comparable<E>)obj;
	   
	   //first node
	   if(first==null)
	   {
		   first=newLink;
		   last=newLink;
		   return true;
	   }
	   
	   //less than first
	   if(newLink.data.compareTo(first.data)<0)
	   {
		   newLink.next=first;
		   first=newLink;
		   return true;
	   }
	   
	   //greater than last
	   if(newLink.data.compareTo(last.data)>0)
	   {
		   last.next=newLink;
		   last=newLink;
	   }
	   
	   
	   //general case
	   if(newLink.data.compareTo(first.data)>=0 && newLink.data.compareTo(last.data)<=0)
	   {
		   Link current=first.next;
		   Link previous=first;
		   while(newLink.data.compareTo(current.data)>=0)
		   {
			   previous=current;
			   current=current.next;
		   }
		   previous.next=newLink;
		   newLink.next=current;
	   }
	   return true;
   }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
