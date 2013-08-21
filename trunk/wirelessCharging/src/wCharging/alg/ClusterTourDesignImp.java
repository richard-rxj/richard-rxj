/**
 * 
 */
package wCharging.alg;

import java.util.ArrayList;
import java.util.Stack;

import wCharging.model.ChargingRequest;
import wCharging.model.ChargingRequestQueue;

/**
 * @author u4964526
 *
 */
public class ClusterTourDesignImp extends BaseTourDesign {

	private int kValue=0;
	
	
	
	/* (non-Javadoc)
	 * @see wCharging.alg.BaseTourDesign#design()
	 */
	public int getkValue() {
		return kValue;
	}


	public void setkValue(int kValue) {
		this.kValue = kValue;
	}


	@Override
	public ArrayList<ChargingRequest> design() {
		// TODO Auto-generated method stub
		ArrayList<ChargingRequest> result=new ArrayList<ChargingRequest>();
		
		double currentX=this.startX;
		double currentY=this.startY;
		
		while(this.currentTime<=this.timeLimit)
		{
			ArrayList<ChargingRequest> tList=this.subClusterDesign(currentX, currentY, this.timeLimit-this.currentTime, this.requestQueue.getSubQueueByReleaseTime(currentTime));
			if(tList.size()==0)
			{
				break;
			}
			else
			{
				ChargingRequest c=null;
				for(int i=0;i<tList.size();i++)
				{
					c=tList.get(i);
					c.setProcessTime(c.getTravelTime());
					result.add(c);		
					this.requestQueue.removeById(c.getId());     //update the queue
					this.currentTime=this.currentTime+c.getProcessTime();  //update the time
				}
				currentX=c.getxAxis();
				currentY=c.getyAxis();
			}
		}
		
		// for the residual requests, apply nearest rule
		{
			NearestTourDesignImp nImp=new NearestTourDesignImp();
			nImp.setRequestQueue(this.requestQueue);
			nImp.setTimeLimit(this.timeLimit);
			nImp.setCurrentTime(this.currentTime);
			nImp.setStartX(currentX);
			nImp.setStartY(currentY);
			ArrayList<ChargingRequest> tList=nImp.design();
			for(ChargingRequest c:tList)
			{
				result.add(c);
			}
		}
		
		return result;
		
		
	}

	
	private ArrayList<ChargingRequest>[] kCluster(ChargingRequestQueue currentQueue)
	//  divide into K cluster
	{
		ArrayList<ChargingRequest>[] result=new ArrayList[this.kValue];
		//coding here
		return result;
	}
	
	private double clusterEvaluation(ArrayList<ChargingRequest> in, ArrayList<ChargingRequest> out, double leftTimeLimit, double currentX, double currentY, double endX, double endY)
	// construct a MST-tour and evaluate it;
	{
		double result=0;

		ChargingRequest[] total=new ChargingRequest[in.size()+2];
		
		ChargingRequest start=new ChargingRequest();
		start.setxAxis(currentX);
		start.setyAxis(currentY);
		start.setId(-1);
		total[0]=start;
		
		ChargingRequest end=new ChargingRequest();
		end.setxAxis(endX);
		end.setyAxis(endY);
		end.setId(-1);
		total[1]=end;

		for(int i=0;i<in.size();i++)
		{
			total[2+i]=in.get(i);
		}
	
		double[][] originEdge=new double[total.length][total.length];
		double[][] mstEdge=new double[total.length][total.length];
		
		for(int i=0;i<total.length;i++)
		{
			for(int j=0;j<total.length;j++)
			{
				originEdge[i][j]=ChargingRequest.distance(total[i], total[j]);
			}
		}
		
		int[] tMark=new int[total.length];   //1 means in tree
		
		
		tMark[0]=1;        //first node
		int tSelect=1;     //how many in tree
		
		while(tSelect<=total.length)
		{
			int tChooseI=0;    //in tree
			int tChooseJ=0;    //not in tree
			double tMin=Double.MAX_VALUE;    //min Edge
			for(int i=0;i<tMark.length;i++)
			{
				if(tMark[i]>0)
				{
					for(int j=0;j<total.length;j++)
					{
						if(tMark[j]==0)
						{
							if(originEdge[i][j]<tMin)
							{
								tMin=originEdge[i][j];
								tChooseI=i;
								tChooseJ=j;
							}
						}
					}
				}
			}
			tSelect++;
			tMark[tChooseJ]=1;
			mstEdge[tChooseI][tChooseJ]=originEdge[tChooseI][tChooseJ];
			mstEdge[tChooseJ][tChooseI]=originEdge[tChooseJ][tChooseI];
			System.out.println(total[tChooseJ]);
		}
		
		Stack<Integer> gStack=new Stack<Integer>();
		gStack.push(0);
		
		while(!gStack.empty())
		{
			int tChoose=gStack.pop();
			out.add(total[tChoose]);
			for(int i=0; i<total.length;i++)
			{
				if(mstEdge[tChoose][i]>0)
				{
					if(!out.contains(total[i]))
					{
						gStack.push(i);
					}
				}
			}
		}
		
		
		for(ChargingRequest c:out)
		{
			System.out.println(c);  //for test
		}
		

		return result;
	}
	

	private ArrayList<ChargingRequest> subClusterDesign(double currentX, double currentY, double leftTimeLimit, ChargingRequestQueue currentQueue)
	{
		
		//divide currentQueue to K clusters
		ArrayList<ChargingRequest>[] kList=this.kCluster(currentQueue);
		
		//evaluate every cluster and choose the largest cluster
		ArrayList<ChargingRequest>  gMax=null;
		double gMaxValue=0;
		for(ArrayList<ChargingRequest> in:kList)
		{   
			ArrayList<ChargingRequest> out=new ArrayList<ChargingRequest>();
			double tMaxValue=this.clusterEvaluation(in, out, leftTimeLimit, currentX, currentY, this.startX, this.startY);
			if(tMaxValue>gMaxValue)
			{
				gMax=out;
			}
		}
		
		return gMax;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
