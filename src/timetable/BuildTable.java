
package timetable;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public class BuildTable {

public static List<Exam>  FrontListExam=new ArrayList<>();
public static List<Exam>  BackListExam=new ArrayList<>();
public static List<Exam> UnscheduledExams=new ArrayList<>();
public static Problem sol;
public static List <Period> firstPS_Periods=new ArrayList<>();
public static List <Period> lastPS_Periods=new ArrayList<>();
public static List <Period> mid_Periods=new ArrayList<>();
public static List <Exam> finalSol=new ArrayList<>();
public static int interval;
static List<freePathObject> freeConflict=new ArrayList<>();
 //static short PS=Problem.getPeriodSpread();

public static boolean Build(Problem individual,Short PS) throws FileNotFoundException, UnsupportedEncodingException
{
	FrontListExam.clear();
	BackListExam.clear();
	UnscheduledExams.clear();
	firstPS_Periods.clear();
	lastPS_Periods.clear();
	mid_Periods.clear();
	finalSol.clear();
	sol=individual;
 for(byte i=0;i<sol.Periods.size();i++)
	 for( byte j=0;j<sol.Periods.get(i).get_allperiodRoomS().size();j++)
		 sol.Periods.get(i).get_allperiodRoomS().get(j).getRoomExams().clear();

 boolean isScheduled=true;
 List <Exam> Exams=sol.Exams;
 
 arrangeRooms();
 //order exam increasing by its ConflictWithSize****************************************************************
 boolean large=false;
 short index=0;
 for(short i=1;i<Exams.size();i++)
	{ index=i;
	  for(short j=(short)(i-1);j>=0;j--)
		 { if(Exams.get(i).ConflictWithSize>Exams.get(j).ConflictWithSize)
			 { large=true;
			   index=j; }
           else
	           {break;}
    	 }
	  if(large==true)
		{Exams.add(index,Exams.get(i));
		 Exams.remove(i+1);
		 large=false;}
	}
 
 
 //recently added
 /*
 Scanner nn=new Scanner (System.in);
 Problem pro= new Problem();
 pro.Exams.addAll(Exams);
 pro.print_allExamsInfo();
 nn.nextLine();
 */
//end of ordering***********************************************************************************************
 
FrontListExam=OrderingExams.OrderingFrontList(Exams);
//print_allExamsInfoExceptPeriodAndRoom(FrontListExam);
//System.out.print("Size FrontLitExam"+FrontListExam.size());
	
BackListExam=OrderingExams.OrderingBackList(Exams);
//print_allExamsInfoExceptPeriodAndRoom(BackListExam);
//System.out.print(BackListExam.size());// Note: consider that the FrontLoad Exams will not be in this List
		


UnscheduledExams=OrderingExams.OrderingUnscheduledExamsRandomly(Exams);
//print_allExamsInfoExceptPeriodAndRoom(UnscheduledExams);
//System.out.print("size of Mid List Exams "+UnscheduledExams.size());




//orderExamsByID(Exams);



/*determine first periods = sol.Periods.size()*0.25, because some Datasets their period spread larger than 
the total period such as Exam6 and to schedule the rest exams in the rest periods that will equal 50% at least out of the total period*/

byte FrontCount=0;
for(byte i=0;i<=(sol.Periods.size()-1)*0.25 && FrontCount<PS ;i++)
   {if(sol.Periods.get(i).getPenalty()==0)
      { firstPS_Periods.add(sol.Periods.get(i));
		FrontCount++; }}

if(firstPS_Periods.size()==0)//this means all periods in the data set have penalty as Exam6
	for(byte i=0;i<=(sol.Periods.size()-1)*0.25 && FrontCount<PS ;i++)
	   { firstPS_Periods.add(sol.Periods.get(i));
			FrontCount++; }	
//for(int i=0;i<firstPS_Periods.size();i++)
//System.out.println("firstPeriods_ID="+firstPS_Periods.get(i).getID());
	
//determine last Periods..as the same manner with first periods 
byte BackCount=0;
for(byte i=(byte)(sol.Periods.size()-1);i>=(sol.Periods.size()-1)*0.75 && BackCount<PS;i--)
   {if(sol.Periods.get(i).getPenalty()==0)
	  { lastPS_Periods.add(0,sol.Periods.get(i));
		BackCount++;}}

if(lastPS_Periods.size()==0)//this means all periods in the data set have penalty as Exam6
	for(byte i=(byte)(sol.Periods.size()-1);i>=(sol.Periods.size()-1)*0.75&& BackCount<PS;i--)
	   {
		   lastPS_Periods.add(0,sol.Periods.get(i));
			BackCount++;}	

//for(int i=0;i<lastPS_Periods.size();i++)
//System.out.print("lastPeriods_ID"+lastPS_Periods.get(i).getID());


/*
//determine midPeriods
for(byte i=(byte)(firstPS_Periods.get(firstPS_Periods.size()-1).getID()+1);i<lastPS_Periods.get(0).getID();i++)
	mid_Periods.add(sol.Periods.get(i));
*/


for(int i=0;i<sol.Periods.size();i++)
	mid_Periods.add(sol.Periods.get(i));


//System.out.println("mid_Periods Size="+mid_Periods.size());
//System.out.println("ID of First Period in mid Periods"+mid_Periods.get(0).getID());
//System.out.println("ID of Last Period in mid Periods"+mid_Periods.get(mid_Periods.size()-1).getID());	
//--------------------------------------------------------------------------------------------------------------
//Build_FirstPeriods********************************************************************************************
boolean ScheduledFront=false;
Byte firstIndexPeriod=0;
Byte backIndexPeriod=(byte)(lastPS_Periods.size()-1);
int Nofirst=0;
int NoBack=0;
while(!FrontListExam.isEmpty() && !BackListExam.isEmpty() )
{ int firstIndexExam=0;
int backIndexExam=0;
	
if(backIndexPeriod>=0)
	while(!BackListExam.isEmpty())
	{
		
		
		
		if(Build_LastPeriods(BackListExam.get(backIndexExam),backIndexPeriod )==true)
			{ NoBack++;
			//System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh");
			if(BackListExam.size()==0)
				break;}
		else if(backIndexExam!=BackListExam.size())
			backIndexExam++;	
		 if(backIndexExam==BackListExam.size())
		{backIndexPeriod--;
			break;}
	}



if(firstIndexPeriod<firstPS_Periods.size())
	
	while(!FrontListExam.isEmpty())
	{
		
		
		
		if(Build_FirstPeriods(FrontListExam.get(firstIndexExam),firstIndexPeriod )==true)
			{Nofirst++;
			if(FrontListExam.size()==0)
				break;}
		else if(firstIndexExam!=FrontListExam.size())
			firstIndexExam++;	
		 if(firstIndexExam==FrontListExam.size())
		{firstIndexPeriod++;
			break;}
	}

else
	break;


}

//System.out.println("inFirst="+Nofirst);
//System.out.println("inBack="+NoBack);

/*
while(!FrontListExam.isEmpty())
{
	for(byte j=0; j<firstPS_Periods.size();j++)
		{if(Build_FirstPeriods(FrontListExam.get(0), j)==true)
			{//System.out.println(FrontListExam.get(0).getID());
			ScheduledFront=true;
			break;
			}
	 if(FrontListExam.size()==0)
			break;
		}
	if(ScheduledFront==false)
		{
		FrontListExam.remove(0);
		}
	ScheduledFront=false;

}


boolean ScheduledBack=false;
while(!BackListExam.isEmpty()){
	ScheduledBack=false;
	for(byte j=(byte)(lastPS_Periods.size()-1);j>=0;j--)	
		{if(Build_LastPeriods(BackListExam.get(0), j)==true)
			{ScheduledBack=true;
			break;
			}			
		 if(BackListExam.size()==0)
				break;
		}
	if(ScheduledBack==false)
	{
	BackListExam.remove(0);
		
	}
	
	
	
	//ConflictRefresh.setCM(BackListExam);



	//nn.nextLine();
	//OrderingExams.OrderingbyConflict(BackListExam);
	//BackListExam=OrderingExams.OrderingBackList(BackListExam);
	
}

	*/   
		
//for(byte i=0;i<lastPS_Periods.size();i++)
//{System.out.println("last Period No="+lastPS_Periods.get(i).getID());
//Print_allInformationOFSpecificPeriod(lastPS_Periods.get(i));
//}
//System.out.println(FrontListExam.size());
//System.out.println(BackListExam.size());
//System.out.println(UnscheduledExams.size());
//print_allExamsInfoExceptPeriodAndRoom(UnscheduledExams);

//--------------------------------------------------------------------------------------------------------------
//Build_midPeriods********************************************************************************************
//System.out.println("Size of uncheled exams is-----------"+ UnscheduledExams.size());
List<Byte> pp=new ArrayList<>();
List<Byte> track=new ArrayList<>();
pp.clear();
track.clear();
Scanner nn=new Scanner (System.in);
for(Byte i=0; i<mid_Periods.size();i++)
pp.add(i);
//orderUnscheduledExamsbyFreePeriods(UnscheduledExams,pp,track);
while(!UnscheduledExams.isEmpty())
{//  in ordre to run by freepath 
	//setConflictAfterForEachExam();
//OrderingExams.OrderingUnscheduledExamsRandomly2(UnscheduledExams);
//ConflictRefresh.setCM(UnscheduledExams);
	orderUnscheduledExamsbyFreePeriods(UnscheduledExams,pp,track);
	//for(int kk=0;kk<UnscheduledExams.size();kk++)
		//System.out.print(UnscheduledExams.get(kk).getID()+", ");
//	System.out.println();
	//System.out.println("nooooooooooooon");
	UnscheduledExams=OrderingExams.refreshOrderingFrontList(UnscheduledExams);	
	//for(int kk=0;kk<UnscheduledExams.size();kk++)
		//System.out.print("qwerftghjk"+UnscheduledExams.get(kk).getID()+", ");
	//nn.nextLine();
	//UnscheduledExams=OrderingExams.OrderingFrontList(UnscheduledExams);
	//for(int sd=UnscheduledExams.size()-1;sd>=0;sd--)
		//System.out.println("Size free periods for Exam N0("+UnscheduledExams.get(sd).getID()+")="+UnscheduledExams.get(sd).freePeriods);
	//nn.nextLine();

//150, EXAM_COINCIDENCE, 151
//nn.nextLine();
//OrderingExams.OrderingbyConflict(UnscheduledExams);
	//for(int sd=0;sd<UnscheduledExams.size();sd++)
		//System.out.println("Sise for Exam N0("+UnscheduledExams.get(sd).getID()+")="+UnscheduledExams.get(sd).ConflictWithSize);
	//nn.nextLine();
	
	//Exam e=UnscheduledExams.get(0);

//
	//151, AFTER, 3
	//33, AFTER, 5
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	if(dd(UnscheduledExams.get(0),pp,track)==false)
		
		
	{
		//System.out.println("cannot scheduled all exams");
		break;
	}
	
}


/*recently added comments marks
boolean finishing=false;
while(!UnscheduledExams.isEmpty())
{	isScheduled=false;
	for(byte i=0;i<mid_Periods.size();i++)	
		{if(Build_midPeriods(UnscheduledExams.get(0), i)==true)
		{if(UnscheduledExams.isEmpty())
		{
			System.out.print("UnscheduledExams is Empty");
			isScheduled=true;
		    finishing=true;
			
			
		}
			//System.out.println("cooooooooooner="+UnscheduledExams.get(0).getID());
			isScheduled=true;
			break;
			
		}
		
	
		}
if(isScheduled==false)
{System.out.print("please try agian");
break;	
}
}
*/
/*
for(byte i=0;i<mid_Periods.size();i++)
{System.out.println("mid Periods No="+mid_Periods.get(i).getID());
Print_allInformationOFSpecificPeriod(mid_Periods.get(i));
}	  

*/

 
/*
for(byte i=0;i<lastPS_Periods.size();i++)
{System.out.println("last Period No="+lastPS_Periods.get(i).getID());
Print_allInformationOFSpecificPeriod(lastPS_Periods.get(i));
}
*/
// to be print solution
/*List<Exam> tableSol=new ArrayList<>();
for(byte i=0;i<firstPS_Periods.size();i++)
	for(byte j=0;j<firstPS_Periods.get(i).get_allperiodRoomS().size();j++)
		for(int k=0;k<firstPS_Periods.get(i).get_allperiodRoomS().get(j).getRoomExams().size();k++)
			tableSol.add(firstPS_Periods.get(i).get_allperiodRoomS().get(j).getSpecificExamInRoomExamS(k));

for(int i=0;i<tableSol.size();i++)
	System.out.println(tableSol.get(i).getPeriod().getID());
*/


orderExamsByID(finalSol);
sol.Exams.clear();
for(short i=0; i<finalSol.size();i++)
	sol.Exams.add(finalSol.get(i));
if(finalSol.size()!=Problem.isComplete)
	
{System.out.println("**************************************************************************************************");
	System.out.println("The solution is uncompleted!");
//System.out.println("Number of exams Scheduled is: "+sol.Exams.size()+" out of "+Problem.isComplete);
System.out.println("**************************************************************************************************");}
else
	System.out.println("number of exams Scheduled is: "+sol.Exams.size());
//nn.nextLine();
//System.out.println(sol.Exams.get(5).getPeriod().getID());
if(UnscheduledExams.isEmpty()==true)
{System.out.println("All Exams have been scheduled");
if(CheckHardConstraints.checkAllHC(sol)==true)
	{System.out.println("***feasible solution***");
	//System.out.println("-------------------------------------------------------------------------------------------------------------");
	PrintSolution.printing(sol);}
else System.out.println("***Infeasible solution***");
return true;
}
else
	return false;
}// End the primary method "Build"
//===============================================================================================================
	public static boolean dd(Exam e,List<Byte> pp, List<Byte> track)
	{
		List<Byte> ppc=new ArrayList<>();
	
		  ppc.addAll(pp);
		  Random p=new Random();
		  
		  for( short t=0;t<track.size();t++)
				if(Build_midPeriods(e,track.get(t) )==true)
				{   //System.out.println("the period in track has been selected =="+track.get(t));
					return true;
									
				}
			
		  while(!ppc.isEmpty())
		  {
		   int index_ppc= p.nextInt(ppc.size());
		   byte period_no=(byte)ppc.get(index_ppc);
		 // System.out.println("the period has been selected =="+period_no);
		       if(Build_midPeriods(e,period_no )==true)
		         { 
		           track.add(period_no);
		           pp.remove(Byte.valueOf(period_no));
		    	   ppc.clear();
		           return true;       
		//for(short dd=0;dd<track.size();dd++)
			// System.out.println("track has this period number="+track.get(dd));
			
		         }	
		  
		         else
		         {ppc.remove(Byte.valueOf(period_no));
			  
		         }
		  }
		return false;
		
	}
	public static boolean Build_FirstPeriods(Exam e, byte Pi){
	if(e.coin.size()!=0)
		{if(firstPs_checkCoincidence(e,Pi)==true)
		return true;
		else
			return false;
		}
	else if( firstPs_checkDuration(e,Pi)==true & firstPs_checkConflict(e,Pi)==false 
			& firstPs_checkExclusion(e,Pi)==false & firstPs_checkAfter(e,Pi)==true & firstPs_checkSpread(e,Pi)==true )
		{if(firstPs_scheduleInRoom(e,Pi)==true)
		return true;
		}
		return false;
	}
//************************for firstPeriods --- checking Period duration**************************************************************

public static boolean firstPs_checkDuration(Exam e, byte Pi)
{
if(e.getduration()<=firstPS_Periods.get(Pi).getDuration())
	return true;// means this period can be used to schedule this exam
else
	return false;
}
//************************for firstPeriods --- checkExamsConflict************************************************************************

public static boolean firstPs_checkConflict(Exam e, byte Pi)
{
for(short i=0;i<e.ConflictWith.size();i++)
	{
	if(firstPS_Periods.get(Pi).get_allPeriodExamS().contains(e.ConflictWith.get(i)))
		{
		//System.out.println("conflict with"+e.getID()+" with "+e.ConflictWith.get(i));
return true;}// means there is conflict
	}	return false;// means no conflict
}
//************************for firstPeriods --- checkExclusion************************************************************************

public static boolean firstPs_checkExclusion(Exam e, byte Pi)
{
for(byte i=0; i<e.exclusion.size();i++)
	if(firstPS_Periods.get(Pi).get_allPeriodExamS().contains(e.exclusion.get(i).getID()))
		return true;
return false;
}
//***************************************************************************************************************

//*******if exam afterList has exams-->check if all before exams are scheduled***********************************************************
public static boolean firstPs_checkAfter(Exam e, byte Pi){
boolean scheduled=false;
	for(byte i=0;i<e.after.size();i++)
		{for(byte j=0;j<Pi;j++)
			{if(firstPS_Periods.get(j).get_allPeriodExamS().contains(e.after.get(i).getID()))
			{
				scheduled=true;
				break;
			}
			
			}
		if(scheduled==false)
			return false;
		else
			scheduled=false;
		}
	
return true;
}
//******if exam coincidence has exams--> check if all its all coincidence exams can be scheduled in this period**************************************************************************
public static boolean firstPs_checkCoincidence(Exam e, byte Pi){
if(firstPs_checkDuration(e,Pi)==false | firstPs_checkConflict(e,Pi)==true
| firstPs_checkExclusion(e,Pi)==true | firstPs_checkAfter(e,Pi)==false 
| firstPs_checkSpread(e,Pi)==false)
		return false;
for(byte i=0;i<e.coin.size();i++)
if(firstPs_checkDuration(e.coin.get(i),Pi)==false | firstPs_checkConflict(e.coin.get(i),Pi)==true
   | firstPs_checkExclusion(e.coin.get(i),Pi)==true | firstPs_checkAfter(e.coin.get(i),Pi)==false 
   | firstPs_checkSpread(e.coin.get(i),Pi)==false)
return false;			

List<ItcRoom>AlltempRooms=new ArrayList<>();
AlltempRooms.clear();
for(byte i=0;i<firstPS_Periods.get(Pi).get_allperiodRoomS().size();i++)
{	ItcRoom tempRoom=new ItcRoom();
	tempRoom.setRoomID(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getID());
	
	tempRoom.setRoomCapacity(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getCapacity());
	tempRoom.setRoomPenalty(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getPenalty());
	tempRoom.setRoomFreeSize(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize());
	tempRoom.setRoomExclusive(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExclusive());

for(short j=0;j<firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().size();j++)
		tempRoom.add_roomExamS(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().get(j));

AlltempRooms.add(tempRoom);
}

if(CheckProomsForCoinExams(AlltempRooms,e)==false)//first of all, check the the Exam before its Coin Exams
	return false;

//means the Exam can be scheduled in this period rooms and we will check its Coin Exams



for(byte i=0;i<e.coin.size();i++)// check its Coin Exams
{
	if(CheckProomsForCoinExams(AlltempRooms,e.coin.get(i))==false)
		{
	     return false;}

}

//real scheduling 
firstPs_scheduleInRoom(e,Pi);

for(byte i=0;i<e.coin.size();i++)
	{ for(short j=0;j<FrontListExam.size();j++)
		if(e.coin.get(i).getID()==FrontListExam.get(j).getID())
	firstPs_scheduleInRoom(FrontListExam.get(j),Pi);
		
	}
return true;
}

//check if there is any room can contain this exam, if any, then schedule this exam in this room for this Period*******************************************************
public static boolean firstPs_scheduleInRoom(Exam e, byte Pi)
{ 
	for(byte i=0;i<firstPS_Periods.get(Pi).get_allperiodRoomS().size();i++)
	{ if(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getPenalty()==0)
	     { if(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().size()==0)
	         { if(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize()>=e.getSize()) 
	               { firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).add_roomExamS(e);
	                 firstPS_Periods.get(Pi).add_periodExamS(e.getID());
	                 if(e.getRoomExclusive()==true)
	                    firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).setRoomExclusive(true);
	                 e.setPeroid(firstPS_Periods.get(Pi));
	    	         e.setRoom(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i));
	    	         finalSol.add(e);
	    	         deleteExam(e.getID());
	    	         return true;}}
	             
	        else if(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().get(0).getduration()==e.getduration()
	        	    & e.getRoomExclusive()==false 
	        	    & firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize()>=e.getSize()
	        	    & firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExclusive()==false)
	        		{ firstPS_Periods.get(Pi).get_allperiodRoomS().get(i).add_roomExamS(e);
	                  firstPS_Periods.get(Pi).add_periodExamS(e.getID());
	                  e.setPeroid(firstPS_Periods.get(Pi));
	     	          e.setRoom(firstPS_Periods.get(Pi).get_allperiodRoomS().get(i));
	     	          finalSol.add(e);
	     	          deleteExam(e.getID());
	     	          return true;
	        		}
		  }
		 
	}
		
	return false;
		}

//*********************checkPeriodSpreadForFirstPeriods************************************************************************
public static boolean firstPs_checkSpread(Exam e, byte Pi){

for(byte i=0;i<firstPS_Periods.size();i++)
	for(short j=0; j<e.ConflictWith.size();j++)
	if(firstPS_Periods.get(i).get_allPeriodExamS().contains(e.ConflictWith.get(j)))
	return false;

return true;
	
}

//***********check if all Coincidence Exams of this Exam can be scheduled in this Period Rooms******************
public static boolean CheckProomsForCoinExams(List<ItcRoom> rooms, Exam e)
{

	for(byte i=0;i<rooms.size();i++)
	{ if(rooms.get(i).getPenalty()==0)
	     { if(rooms.get(i).getRoomExams().size()==0)
	         { if(rooms.get(i).getFreeSize()>=e.getSize()) 
	               { rooms.get(i).add_roomExamS(e);
	                 
	                 if(e.getRoomExclusive()==true)
	                	 rooms.get(i).setRoomExclusive(true);
	                 
	    	         return true;}}
	             
	        else if(rooms.get(i).getRoomExams().get(0).getduration()==e.getduration()
	        	    & e.getRoomExclusive()==false 
	        	    & rooms.get(i).getFreeSize()>=e.getSize()
	        	    & rooms.get(i).getRoomExclusive()==false)
	        		{ rooms.get(i).add_roomExamS(e);
	                  return true;
	        		}
		  }
		 
	}
		
	return false;
	
}
//*************************************************************************************************************
//-------------------------------------------------------------------------------------------------------------
//*************************************Build Last Periods*******************************************************


public static boolean Build_LastPeriods(Exam e,byte Pi){
if(e.coin.size()!=0)
	{if(lastPs_checkCoincidence(e,Pi)==true)
	return true;
	else
		return false;
	}
else if(lastPs_checkDuration(e,Pi)==true & lastPs_checkConflict(e,Pi)==false & lastPs_checkExclusion(e,Pi)==false
        & lastPs_checkBefore(e,Pi)==true  
        & lastPs_checkSpread(e,Pi)==true )
	{if(lastPs_scheduleInRoom(e,Pi)==true)
	return true;
	else
		return false;
	}
else
	return false;
}

//************************for lastPeriods --- checking Period duration**************************************************************

public static boolean lastPs_checkDuration(Exam e, byte Pi)
{
if(e.getduration()<=lastPS_Periods.get(Pi).getDuration())
	return true;// means this period can be used to schedule this exam
else
	return false;
}
//************************for last Periods --- checkExamsConflict************************************************************************

public static boolean lastPs_checkConflict(Exam e, byte Pi)
{
for(short i=0;i<e.ConflictWith.size();i++)
	{
	if(lastPS_Periods.get(Pi).get_allPeriodExamS().contains(e.ConflictWith.get(i)))
		{
		//System.out.println("conflict with"+e.getID()+" with "+e.ConflictWith.get(i));
return true;}// means there is conflict
	}	return false;// means no conflict
}
//************************for lastPeriods --- checkExclusion************************************************************************

public static boolean lastPs_checkExclusion(Exam e, byte Pi)
{
for(byte i=0; i<e.exclusion.size();i++)
	if(lastPS_Periods.get(Pi).get_allPeriodExamS().contains(e.exclusion.get(i).getID()))
		return true;
return false;
}
//***************************************************************************************************************




//*******if exam BeforeList has exams-->check if all after exams are scheduled***********************************************************
public static boolean lastPs_checkBefore(Exam e, byte Pi){

boolean scheduled=false;
	for(byte i=0;i<e.before.size();i++)
		{for(byte j=(byte)(Pi+1);j<lastPS_Periods.size();j++)
			{if(lastPS_Periods.get(j).get_allPeriodExamS().contains(e.before.get(i).getID()))
			{
				scheduled=true;
				break;
			}
			
			}
		if(scheduled==false)
			return false;
		else
			scheduled=false;
		}
	//if(e.getID()==522)
	  //	  System.out.println("Heeeer522 522rrrrrrrrrrrrrrLastLastrrrrrrrrrrrrrrreeeeee");
		//if(e.getID()==523)
		  //	  System.out.println("523523523523 in last");	

return true;
}

//***********************checkPeriodSpreadForLastPeriods*****************************************************
public static boolean lastPs_checkSpread(Exam e, byte Pi){

for(byte i=0;i<lastPS_Periods.size();i++)
	for(short j=0; j<e.ConflictWith.size();j++)
	if(lastPS_Periods.get(i).get_allPeriodExamS().contains(e.ConflictWith.get(j)))
	return false;

return true;
	
}

//check also the any exam will be scheduled in last periods must any of its after list exam not scheduled after this exam
/*public static boolean lastPs_checkAfter(Exam e, byte Pi){

	for(byte i=0;i<e.after.size();i++)
		for(byte j=Pi;j<lastPS_Periods.size();j++)
			if(lastPS_Periods.get(j).get_allPeriodExamS().contains(e.after.get(i).getID()))
				return true;
			
		
			
return false;
}
*/
//******if exam coincidence has exams--> check if all its all coincidence exams can be scheduled in this period**************************************************************************
public static boolean lastPs_checkCoincidence(Exam e, byte Pi){
	if(lastPs_checkDuration(e,Pi)==false | lastPs_checkConflict(e,Pi)==true |
			   lastPs_checkExclusion(e,Pi)==true | lastPs_checkBefore(e,Pi)==false 
			   // |lastPs_checkAfter(e,Pi)==true 
			   |lastPs_checkSpread(e,Pi)==false)
		return false;
	for(byte i=0;i<e.coin.size();i++)
if(lastPs_checkDuration(e.coin.get(i),Pi)==false | lastPs_checkConflict(e.coin.get(i),Pi)==true |
   lastPs_checkExclusion(e.coin.get(i),Pi)==true | lastPs_checkBefore(e.coin.get(i),Pi)==false |lastPs_checkSpread(e.coin.get(i),Pi)==false)
return false;			

List<ItcRoom>AlltempRooms=new ArrayList<>();
AlltempRooms.clear();
for(byte i=0;i<lastPS_Periods.get(Pi).get_allperiodRoomS().size();i++)
{	ItcRoom tempRoom=new ItcRoom();
	tempRoom.setRoomID(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getID());
	tempRoom.setRoomCapacity(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getCapacity());
	tempRoom.setRoomPenalty(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getPenalty());
	tempRoom.setRoomFreeSize(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize());
	tempRoom.setRoomExclusive(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExclusive());

for(short j=0;j<lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().size();j++)
		tempRoom.add_roomExamS(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().get(j));

AlltempRooms.add(tempRoom);
}

if(CheckProomsForCoinExams(AlltempRooms,e)==false)//first of all, check the the Exam before its Coin Exams
	return false;

//means the Exam can be scheduled in this period rooms and we will check its Coin Exams

for(byte i=0;i<e.coin.size();i++)// check its Coin Exams
{
	if(CheckProomsForCoinExams(AlltempRooms,e.coin.get(i))==false)
		{
	     return false;}
}


//real scheduling 
lastPs_scheduleInRoom(e,Pi);

for(byte i=0;i<e.coin.size();i++)
	{for(short j=0;j<BackListExam.size();j++)
		if(e.coin.get(i).getID()==BackListExam.get(j).getID())
			lastPs_scheduleInRoom(BackListExam.get(j),Pi);

		
	}
return true;
}


//check if there is any room can contain this exam, if any, then schedule this exam in this room for this Period*******************************************************
public static boolean lastPs_scheduleInRoom(Exam e, byte Pi)
{ 
for(byte i=0;i<lastPS_Periods.get(Pi).get_allperiodRoomS().size();i++)
{ if(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getPenalty()==0)
     { if(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().size()==0)
         { if(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize()>=e.getSize()) 
               { lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).add_roomExamS(e);
                 lastPS_Periods.get(Pi).add_periodExamS(e.getID());
                 if(e.getRoomExclusive()==true)
                    lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).setRoomExclusive(true);
                 e.setPeroid(lastPS_Periods.get(Pi));
    	         e.setRoom(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i));
    	         finalSol.add(e);
    	         deleteExam(e.getID());
    	         return true;}}
             
        else if(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().get(0).getduration()==e.getduration()
        	    & e.getRoomExclusive()==false 
        	    & lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize()>=e.getSize()
        	    & lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExclusive()==false)
        		{ lastPS_Periods.get(Pi).get_allperiodRoomS().get(i).add_roomExamS(e);
                  lastPS_Periods.get(Pi).add_periodExamS(e.getID());
                  e.setPeroid(lastPS_Periods.get(Pi));
     	          e.setRoom(lastPS_Periods.get(Pi).get_allperiodRoomS().get(i));
     	          finalSol.add(e);
     	          deleteExam(e.getID());
     	          return true;
        		}
	  }
	 
}
	
return false;
	
	//********************************************
}



//************************************End of building last periods *********************************************

//*************************************************************************************************************
//------------------------------Build mid periods -------------------------------------------------------------
//*************************************************************************************************************
//===============================================================================================================

	public static boolean Build_midPeriods(Exam e, byte Pi){
	if(e.coin.size()!=0)
		{if(midPs_checkCoincidence(e,Pi)==true)
		return true;
		else
			return false;
		}
	else if( midPs_checkDuration(e,Pi)==true & midPs_checkConflict(e,Pi)==false 
			& midPs_checkExclusion(e,Pi)==false & midPs_checkAfter(e,Pi)==true & midPs_checkBefore(e,Pi)==true )
		{if(midPs_scheduleInRoom(e,Pi)==true)
		{return true;}
		}
	
		return false;
	}
//************************for mid Periods --- checking Period duration**************************************************************

public static boolean midPs_checkDuration(Exam e, byte Pi)
{
if(e.getduration()<=mid_Periods.get(Pi).getDuration())
	return true;// means this period can be used to schedule this exam
else
	return false;
}
//************************for midPeriods --- checkExamsConflict************************************************************************

public static boolean midPs_checkConflict(Exam e, byte Pi)
{
for(short i=0;i<e.ConflictWith.size();i++)
	{
	if(mid_Periods.get(Pi).get_allPeriodExamS().contains(e.ConflictWith.get(i)))
		{
		//System.out.println("conflict with"+e.getID()+" with "+e.ConflictWith.get(i));
return true;}// means there is conflict
	}	return false;// means no conflict
	
}
//************************for midPeriods --- checkExclusion************************************************************************

public static boolean midPs_checkExclusion(Exam e, byte Pi)
{
for(byte i=0; i<e.exclusion.size();i++)
	if(mid_Periods.get(Pi).get_allPeriodExamS().contains(e.exclusion.get(i).getID()))
		return true;
		
return false;
}
//***************************************************************************************************************

public static boolean midPs_checkBefore(Exam e, int Pi){
	
	for(int i=0;i<e.before.size();i++)
		{
	
	
		{for(int j=0;j<=Pi;j++)
			{if(mid_Periods.get(j).get_allPeriodExamS().contains(e.before.get(i).getID()))
				{//System.out.println("DDDDDDDDDDDDDDDDDDsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
				return false;}
			}
			
			}}
//	System.out.println("DDDDDDDDDDDDDDDDDDsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
return true;
}

//***********






//*******if exam afterList has exams-->check if all before exams are scheduled***********************************************************
public static boolean midPs_checkAfter(Exam e, byte Pi){

boolean scheduled=false;
for(byte i=0;i<e.after.size();i++)
    {for(byte j=0;j<firstPS_Periods.size()  & j<Pi;j++)
      {if(firstPS_Periods.get(j).get_allPeriodExamS().contains(e.after.get(i).getID()))
         { scheduled=true;
  //       if(e.getID()==522)
    //   	  System.out.println("Heeeerrrrrrrrrrrfirstmidrrrrrrrrrrrrrrrrrrreeeeee");
		  break;
	     }} 
	 if(scheduled==false)
       {{for(byte j=0;j<Pi;j++)
          {if(mid_Periods.get(j).get_allPeriodExamS().contains(e.after.get(i).getID()))
		  {//if(e.getID()==522)
        	 // System.out.println("Heeeerrrrrrrrrrrmidmidrrrrrrrrrrrrrrrrrrreeeeee");
        	  
        	  scheduled=true;
		  break;}
			
		}}}
		if(scheduled==false)
			return false;
		else
			scheduled=false;
		}
    
return true;
}
//******if exam coincidence has exams--> check if all its all coincidence exams can be scheduled in this period**************************************************************************
public static boolean midPs_checkCoincidence(Exam e, byte Pi){
	if(midPs_checkDuration(e,Pi)==false | midPs_checkConflict(e,Pi)==true
			   | midPs_checkExclusion(e,Pi)==true | midPs_checkAfter(e,Pi)==false | midPs_checkBefore(e,Pi)==false)
				return false;	
for(byte i=0;i<e.coin.size();i++)
if(midPs_checkDuration(e.coin.get(i),Pi)==false | midPs_checkConflict(e.coin.get(i),Pi)==true
   | midPs_checkExclusion(e.coin.get(i),Pi)==true | midPs_checkAfter(e.coin.get(i),Pi)==false |  midPs_checkBefore(e.coin.get(i),Pi)==false)
	return false;			

List<ItcRoom>AlltempRooms=new ArrayList<>();
AlltempRooms.clear();
for(byte i=0;i<mid_Periods.get(Pi).get_allperiodRoomS().size();i++)
{	ItcRoom tempRoom=new ItcRoom();
	tempRoom.setRoomID(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getID());
	tempRoom.setRoomCapacity(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getCapacity());
	tempRoom.setRoomPenalty(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getPenalty());
	tempRoom.setRoomFreeSize(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize());
	tempRoom.setRoomExclusive(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExclusive());

for(short j=0;j<mid_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().size();j++)
		tempRoom.add_roomExamS(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().get(j));

AlltempRooms.add(tempRoom);
}



if(mid_CheckProomsForCoinExams(AlltempRooms,e)==false)//first of all, check the the Exam before its Coin Exams
	{
	return false;
	}
	
//means the Exam can be scheduled in this period rooms and we will check its Coin Exams

for(byte i=0;i<e.coin.size();i++)// check its Coin Exams
{
	if(mid_CheckProomsForCoinExams(AlltempRooms,e.coin.get(i))==false)
		{return false;}
}


//real scheduling 
midPs_scheduleInRoom(e,Pi);

for(byte i=0;i<e.coin.size();i++)
	{for(short j=0;j<UnscheduledExams.size();j++)
		if(e.coin.get(i).getID()==UnscheduledExams.get(j).getID())
			midPs_scheduleInRoom(UnscheduledExams.get(j),Pi);
	
		
	}
return true;
}

//check if there is any room can contain this exam, if any, then schedule this exam in this room for this Period*******************************************************
public static boolean midPs_scheduleInRoom(Exam e, byte Pi)
{ 
	for(byte i=0;i<mid_Periods.get(Pi).get_allperiodRoomS().size();i++)
	{ 
	      if(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().size()==0)
	         { if(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize()>=e.getSize()) 
	               { mid_Periods.get(Pi).get_allperiodRoomS().get(i).add_roomExamS(e);
	                 mid_Periods.get(Pi).add_periodExamS(e.getID());
	                 if(e.getRoomExclusive()==true)
	                    mid_Periods.get(Pi).get_allperiodRoomS().get(i).setRoomExclusive(true);
	                 e.setPeroid(mid_Periods.get(Pi));
	    	         e.setRoom(mid_Periods.get(Pi).get_allperiodRoomS().get(i));
	    	         finalSol.add(e);
	    	         deleteExam(e.getID());
	    	         return true;}}
	             
	        else if(e.getRoomExclusive()==false 
	        	    & mid_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize()>=e.getSize()
	        	    & mid_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExclusive()==false)
	        		{ mid_Periods.get(Pi).get_allperiodRoomS().get(i).add_roomExamS(e);
	                  mid_Periods.get(Pi).add_periodExamS(e.getID());
	                  e.setPeroid(mid_Periods.get(Pi));
	     	          e.setRoom(mid_Periods.get(Pi).get_allperiodRoomS().get(i));
	     	          finalSol.add(e);
	     	          deleteExam(e.getID());
	     	          return true;
	        		}
		  
		 
	}
		
	return false;
		
}


//***********check if all Coincidence Exams of this Exam can be scheduled in this Period Rooms******************
public static boolean mid_CheckProomsForCoinExams(List<ItcRoom> rooms, Exam e)
{
	for(byte i=0;i<rooms.size();i++)
	{ 
	      if(rooms.get(i).getRoomExams().size()==0)
	         { if(rooms.get(i).getFreeSize()>=e.getSize()) 
	               { rooms.get(i).add_roomExamS(e);
	                
	                 if(e.getRoomExclusive()==true)
	                   rooms.get(i).setRoomExclusive(true);
	                   return true;}}
	             
	        else if(e.getRoomExclusive()==false 
	        	    & rooms.get(i).getFreeSize()>=e.getSize()
	        	    & rooms.get(i).getRoomExclusive()==false)
	        		{ rooms.get(i).add_roomExamS(e);
	                  
	     	          return true;
	        		}
		  
		 
	}
		
	return false;}
















//***************arrange all rooms for all Periods increasingly by their capacities*****************************
public static void arrangeRooms(){
boolean large=false;
byte index=0;
for(byte k=0;k<sol.Periods.size();k++)
{for(byte i=1;i<sol.Periods.get(k).get_allperiodRoomS().size();i++)		
	{index=i;
		
			for(byte j=(byte)(i-1);j>=0;j--)
	{if(sol.Periods.get(k).get_allperiodRoomS().get(i).getCapacity()<sol.Periods.get(k).get_allperiodRoomS().get(j).getCapacity())
				{
		large=true;
				index=j;
				}

	else
	{	break;
	}

			
	}
	if(large==true)
			{sol.Periods.get(k).get_allperiodRoomS().add(index,sol.Periods.get(k).get_allperiodRoomS().get(i));
			sol.Periods.get(k).get_allperiodRoomS().remove(i+1);
			large=false;
	}}}

	
//for(int test=0;test<sol.Periods.get(4).get_allperiodRoomS().size();test++)
	//System.out.println("roomID"+sol.Periods.get(0).get_allperiodRoomS().get(test).getID());
	
}


//*******************delete exam from all Lists by Sent Exam Id *************************************************
public static void deleteExam(short IdExam){

	for(short i=0;i<FrontListExam.size();i++)
	{
		if(FrontListExam.get(i).getID()==IdExam)
			FrontListExam.remove(i);
	}

	
	for(short i=0;i<BackListExam.size();i++)
	{
		if(BackListExam.get(i).getID()==IdExam)
			BackListExam.remove(i);
	}
	
	for(short i=0;i<UnscheduledExams.size();i++)
	{
		if(UnscheduledExams.get(i).getID()==IdExam)
			UnscheduledExams.remove(i);
	}
	
}


//*************************print all info for all Exams except Period and Room***********************************
public static void print_allExamsInfoExceptPeriodAndRoom(List<Exam> Exams)
{
 for(short i=0; i<Exams.size(); i++)
    {System.out.println("index("+i+")=> ID="+Exams.get(i).getID()+", Duration="+Exams.get(i).getduration()+
	           ", Size="+Exams.get(i).getSize()+", ConflictWithSize="+Exams.get(i).ConflictWithSize+
	           
	          ", Exclusive="+Exams.get(i).getRoomExclusive()+
	           ", FrontLoad="+Exams.get(i).getFrontLoad());          
    System.out.print("coincedence with=");
    for(byte c=0;c<Exams.get(i).get_CoinList().size();c++)
    	System.out.print(" "+Exams.get(i).get_CoinList().get(c).getID());
         
    System.out.println();
    System.out.print("Exclusion with=");
    for(byte e=0;e<Exams.get(i).get_exclusionList().size();e++)
    System.out.print(" "+Exams.get(i).get_exclusionList().get(e).getID());
    System.out.println();
    System.out.print("After=");
    for(byte a=0;a<Exams.get(i).get_afterList().size();a++)
    	System.out.print(" "+Exams.get(i).get_afterList().get(a).getID());
    System.out.println();	
    System.out.print("Before=");
    for(byte b=0;b<Exams.get(i).get_beforeList().size();b++)
    	System.out.print(" "+Exams.get(i).get_beforeList().get(b).getID());
    System.out.println();
    System.out.println( "************************************************************************************************");
	}	
}

//*******************orderExamsByID******************************************************************************
public static void orderExamsByID(List<Exam> Exams)
{
	boolean large=false;
	short index=0;
		for(short i=1;i<Exams.size();i++)
	{index=i;
		
			for(short j=(short)(i-1);j>=0;j--)
	{if(Exams.get(i).getID()<Exams.get(j).getID())
				{
		large=true;
				index=j;
				}

	else
	{	break;
	}

			
	}
	if(large==true)
			{Exams.add(index,Exams.get(i));
			Exams.remove(i+1);
			large=false;
	}}

	
}
//*******************EndMethod***********************************************************************************
public static void Print_allInformationOFSpecificPeriod(Period P)
{
	List<ItcRoom> rooms=new ArrayList<>();
rooms=P.get_allperiodRoomS();
//System.out.println(rooms.size());
for(byte i=0;i<rooms.size();i++)	
{	System.out.println("room ID("+rooms.get(i).getID()+"):");
	for(short j=0;j<rooms.get(i).getRoomExams().size();j++)
		System.out.println("ExamID="+rooms.get(i).getRoomExams().get(j).getID());
}}

//*********************************************************
//*********************************************************
public static void setConflictAfterForEachExam()
{
for(short i=0;i<UnscheduledExams.size();i++)
	{freeConflict.clear();
	//ConflictAfter=0;
	computeConflictAfter(UnscheduledExams.get(i));
	UnscheduledExams.get(i).conflictAfter=(short)UnscheduledExams.get(i).FreePathWith.size();}

   // for(short y=0;y<Exams.get(2).FreePathWith.size();y++)
    //		System.out.println("fg="+Exams.get(2).FreePathWith.get(y));

     //    Exams.get(i).conflictAfter=ConflictAfter;
     
	//}
   //  for(short i=0;i<Exams.get(0).ConflictWith.size();i++)
    //	 System.out.println(Exams.get(0).ConflictWith.get(i));
     
}

// not use yet
public static void computeConflictAfter(Exam e){
	e.FreePathWith.clear();
for(short j=0;j<UnscheduledExams.size();j++)
{	if(j!=e.getID())
	if(!e.ConflictWith.contains(j))
		freeConflict.add(new freePathObject(j));
}

if(freeConflict.size()>0){
//    ConflictAfter++;
//	maxConflictAfter(e);}
	
	short count=0;
short max=0;
short maxExam=0;

for(short n=0;n<freeConflict.size();n++)
	{count=0;
	for(short k=0;k<freeConflict.size();k++)
	{	if( ConflictMatrix.conflictMatrix[freeConflict.get(n).getExamID()][freeConflict.get(k).getExamID()]==0)
			{
			freeConflict.get(n).incrementNoFreeConflict();
			}
	}
	}
// decreasing order freeConflict by NoFreeWith**********************************************************

boolean large=false;
int index=0;
	for(short i=1;i<freeConflict.size();i++)
{index=i;
	
		for(int j=i-1;j>=0;j--)
{if(freeConflict.get(i).getNoFreeWith()>freeConflict.get(j).getNoFreeWith())
{
	large=true;
			index=j;
			}

else
{	break;
}

		
}
if(large==true)
		{freeConflict.add(index,freeConflict.get(i));
		freeConflict.remove(i+1);
		large=false;
}}

	while(freeConflict.size()>0)
{maxExam=(short)freeConflict.get(0).getExamID();
e.FreePathWith.add(maxExam);

	short k=1;
	while(k<freeConflict.size())
	{
	

		if(ConflictMatrix.conflictMatrix[maxExam][freeConflict.get(k).getExamID()]!=0)
			{
			freeConflict.remove(k);k--;
			}
	k++;	
	}


	
	freeConflict.remove(0);   
	if(freeConflict.size()>0)
	maxExam=(short)freeConflict.get(0).getExamID();


   



}}}

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


public static boolean TestBuild_midPeriods(Exam e, byte Pi){
if(e.coin.size()!=0)
	{if(TestmidPs_checkCoincidence(e,Pi)==true)
	return true;
	else
		return false;
	}
else if( TestmidPs_checkDuration(e,Pi)==true & TestmidPs_checkConflict(e,Pi)==false 
		& TestmidPs_checkExclusion(e,Pi)==false & TestmidPs_checkAfter(e,Pi)==true )
	{if(TestmidPs_scheduleInRoom(e,Pi)==true)
	{return true;}
	}

	return false;
}
//************************for mid Periods --- checking Period duration**************************************************************

public static boolean TestmidPs_checkDuration(Exam e, byte Pi)
{
if(e.getduration()<=mid_Periods.get(Pi).getDuration())
return true;// means this period can be used to schedule this exam
else
return false;
}
//************************for midPeriods --- checkExamsConflict************************************************************************

public static boolean TestmidPs_checkConflict(Exam e, byte Pi)
{
for(short i=0;i<e.ConflictWith.size();i++)
{
if(mid_Periods.get(Pi).get_allPeriodExamS().contains(e.ConflictWith.get(i)))
	{
	//System.out.println("conflict with"+e.getID()+" with "+e.ConflictWith.get(i));
return true;}// means there is conflict
}	return false;// means no conflict
}
//************************for midPeriods --- checkExclusion************************************************************************

public static boolean TestmidPs_checkExclusion(Exam e, byte Pi)
{
for(byte i=0; i<e.exclusion.size();i++)
if(mid_Periods.get(Pi).get_allPeriodExamS().contains(e.exclusion.get(i).getID()))
	return true;
return false;
}
//***************************************************************************************************************

//*******if exam afterList has exams-->check if all before exams are scheduled***********************************************************
public static boolean TestmidPs_checkAfter(Exam e, byte Pi){

boolean scheduled=false;
for(byte i=0;i<e.after.size();i++)
{for(byte j=0;j<firstPS_Periods.size();j++)
  {if(firstPS_Periods.get(j).get_allPeriodExamS().contains(e.after.get(i).getID()))
     { scheduled=true;
	  break;
     }} 
 if(scheduled==false)
   {{for(byte j=0;j<Pi;j++)
      {if(mid_Periods.get(j).get_allPeriodExamS().contains(e.after.get(i).getID()))
	  {scheduled=true;
	  break;}
		
	}}}
	if(scheduled==false)
		return false;
	else
		scheduled=false;
	}

return true;
}


//******if exam coincidence has exams--> check if all its all coincidence exams can be scheduled in this period**************************************************************************
public static boolean TestmidPs_checkCoincidence(Exam e, byte Pi){
if(TestmidPs_checkDuration(e,Pi)==false | TestmidPs_checkConflict(e,Pi)==true
		   | TestmidPs_checkExclusion(e,Pi)==true | TestmidPs_checkAfter(e,Pi)==false )
			return false;	
for(byte i=0;i<e.coin.size();i++)
if(TestmidPs_checkDuration(e.coin.get(i),Pi)==false | TestmidPs_checkConflict(e.coin.get(i),Pi)==true
| TestmidPs_checkExclusion(e.coin.get(i),Pi)==true | TestmidPs_checkAfter(e.coin.get(i),Pi)==false )
return false;			

List<ItcRoom>AlltempRooms=new ArrayList<>();
AlltempRooms.clear();
for(byte i=0;i<mid_Periods.get(Pi).get_allperiodRoomS().size();i++)
{	ItcRoom tempRoom=new ItcRoom();
tempRoom.setRoomID(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getID());
tempRoom.setRoomCapacity(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getCapacity());
tempRoom.setRoomPenalty(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getPenalty());
tempRoom.setRoomFreeSize(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize());
tempRoom.setRoomExclusive(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExclusive());

for(short j=0;j<mid_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().size();j++)
	tempRoom.add_roomExamS(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().get(j));

AlltempRooms.add(tempRoom);
}



if(Testmid_CheckProomsForCoinExams(AlltempRooms,e)==false)//first of all, check the the Exam before its Coin Exams
{
return false;
}

//means the Exam can be scheduled in this period rooms and we will check its Coin Exams

for(byte i=0;i<e.coin.size();i++)// check its Coin Exams
{
if(Testmid_CheckProomsForCoinExams(AlltempRooms,e.coin.get(i))==false)
	{return false;}
}


//real scheduling 
TestmidPs_scheduleInRoom(e,Pi);

for(byte i=0;i<e.coin.size();i++)
{for(short j=0;j<UnscheduledExams.size();j++)
	if(e.coin.get(i).getID()==UnscheduledExams.get(j).getID())
		TestmidPs_scheduleInRoom(UnscheduledExams.get(j),Pi);

	
}
return true;
}

//check if there is any room can contain this exam, if any, then schedule this exam in this room for this Period*******************************************************
public static boolean TestmidPs_scheduleInRoom(Exam e, byte Pi)
{ 
for(byte i=0;i<mid_Periods.get(Pi).get_allperiodRoomS().size();i++)
{ 
      if(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExams().size()==0)
         { if(mid_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize()>=e.getSize()) 
               { 
                 
    	         return true;}}
             
        else if(e.getRoomExclusive()==false 
        	    & mid_Periods.get(Pi).get_allperiodRoomS().get(i).getFreeSize()>=e.getSize()
        	    & mid_Periods.get(Pi).get_allperiodRoomS().get(i).getRoomExclusive()==false)
        		{ 
                  return true;
        		}
	  
	 
}
	
return false;
	
}


//***********check if all Coincidence Exams of this Exam can be scheduled in this Period Rooms******************
public static boolean Testmid_CheckProomsForCoinExams(List<ItcRoom> rooms, Exam e)
{
for(byte i=0;i<rooms.size();i++)
{ 
      if(rooms.get(i).getRoomExams().size()==0)
         { if(rooms.get(i).getFreeSize()>=e.getSize()) 
               { rooms.get(i).add_roomExamS(e);
                
                 if(e.getRoomExclusive()==true)
                   rooms.get(i).setRoomExclusive(true);
                   return true;}}
             
        else if(e.getRoomExclusive()==false 
        	    & rooms.get(i).getFreeSize()>=e.getSize()
        	    & rooms.get(i).getRoomExclusive()==false)
        		{ rooms.get(i).add_roomExamS(e);
                  
     	          return true;
        		}
	  
	 
}
	
return false;}
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
public static void orderUnscheduledExamsbyFreePeriods(List<Exam> examss,List<Byte> pp,List<Byte> track)
{
	
	for(int i=0;i<examss.size();i++)
	examss.get(i).freePeriods=0;
	for(int i=0;i<examss.size();i++)
		{ for(byte j=0;j<track.size();j++)
			if(TestBuild_midPeriods(examss.get(i),track.get(j))==true)
				examss.get(i).freePeriods++;
		//System.out.println("Helloooooooooooooooooo");
		for(byte j=0;j<pp.size();j++)
			if(TestBuild_midPeriods(examss.get(i),pp.get(j))==true)
				examss.get(i).freePeriods++;
		}
	
	
	boolean large=false;
	 short index=0;
	 for(short i=1;i<examss.size();i++)
		{ index=i;
		  for(short j=(short)(i-1);j>=0;j--)
			 { if(examss.get(i).freePeriods<examss.get(j).freePeriods)
				 { large=true;
				   index=j; }
	           else
		           {break;}
	    	 }
		  if(large==true)
			{examss.add(index,examss.get(i));
			 examss.remove(i+1);
			 large=false;}
		}

	// examss=OrderingListbyAfter(examss);
}







}
