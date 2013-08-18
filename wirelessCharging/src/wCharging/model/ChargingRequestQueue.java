/**
 * 
 */
package wCharging.model;

import java.util.LinkedList;

/**
 * @author user
 *
 */
public class ChargingRequestQueue extends LinkedList<ChargingRequest>{

   @Override	
   public boolean add(ChargingRequest c)
   {
	   int index=0;
	   for(;index<this.size();index++)
	   {
		   ChargingRequest current=this.get(index);
		   if(current.compareTo(c)>0)
		   {
			   break;
		   }
	   }
	   this.add(index,c);
	   return true;
   }
	
   public boolean removeById(int id)
   {
	   int index=0;
	   for(;index<this.size();index++)
	   {
		   ChargingRequest current=this.get(index);
		   if(current.getId()==id)
		   {
			   this.remove(current);
			   return true;
		   }
	   }
	   
	   return false;
   }
   
   public ChargingRequestQueue getSubQueueByTime(double time)
   {
	   ChargingRequestQueue subQueue=new ChargingRequestQueue();
	   for(int index=0;index<this.size();index++)
	   {
		   ChargingRequest current=this.get(index);
		   if(current.getReleaseTime()>time)
		   {
			   break;
		   }
		   subQueue.add(current);
	   }
	   return subQueue;
   }
   
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChargingRequestQueue q=new ChargingRequestQueue();
		
		ChargingRequest[] cSet=new ChargingRequest[10];
		
		for(int i=0;i<10;i++)
		{
			cSet[i]=new ChargingRequest();
			cSet[i].setId(i);
			cSet[i].setReleaseTime(i*10);
		}
			
		cSet[3].setReleaseTime(72);
		cSet[9].setReleaseTime(5);
		
		for(ChargingRequest c:cSet)
		{
			q.add(c);
		}
		
		ChargingRequestQueue sub=q.getSubQueueByTime(71);
		
		q.removeById(cSet[5].getId());
		
		
		for(ChargingRequest c:sub)
		{
			System.out.println(c);
		}
		
		System.out.println("***************");
		
	    for(ChargingRequest c:q)
		{
			System.out.println(c);
		}
	}

}
