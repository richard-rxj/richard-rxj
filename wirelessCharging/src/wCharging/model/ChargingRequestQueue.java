/**
 * 
 */
package wCharging.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Logger;

import wCharging.test.SimulationSetting;
import wCharging.util.TravelTimeComparator;

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
   
   public ChargingRequestQueue getSubQueueByReleaseTime(double time)
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
   
   public ChargingRequestQueue getSubQueueByTimeLimit(double time)
   {
	   ChargingRequestQueue subQueue=new ChargingRequestQueue();
	   for(int index=0;index<this.size();index++)
	   {
		   ChargingRequest current=this.get(index);
		   if(current.getTravelPlusBackTime()>time)
		   {
			   break;
		   }
		   subQueue.add(current);
	   }
	   return subQueue;
   }
   
   public ChargingRequestQueue getSubQueueByRequestList(ArrayList<ChargingRequest> rList)
   {
	   ChargingRequestQueue subQueue=(ChargingRequestQueue) this.clone();
	   
	   if(rList!=null)
	   {
		   for(int index=0;index<rList.size();index++)
		   {
			   subQueue.removeById(rList.get(index).getId());
		   }
	   }
	   return subQueue;
   }
   
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Logger log=SimulationSetting.gLog;
		
		ChargingRequestQueue q=new ChargingRequestQueue();
		
		ChargingRequest[] cSet=new ChargingRequest[10];
		
		for(int i=0;i<10;i++)
		{
			cSet[i]=new ChargingRequest();
			cSet[i].setId(i);
			cSet[i].setReleaseTime(i*10);
			cSet[i].setTravelTime(i*100);
		}
			
		cSet[3].setReleaseTime(72);
		cSet[9].setReleaseTime(5);
		
		cSet[1].setTravelTime(750);
		cSet[7].setTravelTime(150);
		
		for(ChargingRequest c:cSet)
		{
			q.add(c);
		}
		
		ChargingRequestQueue sub=q.getSubQueueByReleaseTime(71);
		
		q.removeById(cSet[5].getId());
		
		
		for(ChargingRequest c:sub)
		{
			log.info(c.toString());
		}
		
		log.info("***************");
		
	    
		ArrayList<ChargingRequest> rList=new ArrayList<ChargingRequest>();
		rList.add(cSet[2]);
		rList.add(cSet[6]);
		
		ChargingRequestQueue sub2=q.getSubQueueByRequestList(rList);
		
		for(ChargingRequest c:sub2)
		{
			log.info(c.toString());
		}
		
		log.info("~~~~~~~~~~~~~~~~~~~~");
		
		for(ChargingRequest c:q)
		{
			log.info(c.toString());
		}
	    
//	    Collections.sort(q,new TravelTimeComparator(true));
//	    
//       System.out.println("-----------------");
//		
//	    for(ChargingRequest c:q)
//		{
//			System.out.println(c);
//		}
	}

}
