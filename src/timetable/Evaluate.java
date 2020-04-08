package timetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Evaluate {
	//Population pop=new Population();
	//static Scanner  show=new Scanner(System.in);

	
	
	public static Problem Evaluate_Sol;
	private static short conflict=0;	
	private static long overAll_Penalty;

	
	
//**************************************************************************************************************
	public static long overAll_Penalty(){
	overAll_Penalty=SumPeriodSpreadofALLstudentS();
	
		return overAll_Penalty;
	}

//**************************************************************************************************************	

	public static long SumPeriodSpreadofALLstudentS(){
	

	 long allStudents_Penalty=0; 
	for(short i=0;i<Evaluate_Sol.Students.size();i++)
		 allStudents_Penalty+=	sumPenaltiesOfOnetudents(i);
	//System.out.println("The number of occurrences of conflict Exams= "+conflict);
	
	//System.out.println("The number of occurrences of Two Exams in A Row= "+(No2R*W2R));
	//System.out.println("The number of occurrences of Two Exams in A Day= "+(No2D*W2D));
	//System.out.println("The number of occurrences of Periods Spread= "+NoPS);
	
	return allStudents_Penalty;
	}
//**************************************************************************************************************	
	
	    public static long sumPenaltiesOfOnetudents(short s){
	  
	    short Ps=5;
	    
	  
	    short penaltyPS_forThisStudent=0;
	  
	    // order the Exams of this student by periods of exam
	    boolean smaller=false;
	    short index=0;
	    	for(short i=1;i<Evaluate_Sol.Students.get(s).get_EaxmList().size();i++)
	    {
	    		
	    		index=i;
	    	
	    		for(short j=(short)(i-1);j>=0;j--)
	    {if(Evaluate_Sol.Students.get(s).get_EaxmList().get(i).getPeriod().getID()<Evaluate_Sol.Students.get(s).get_EaxmList().get(j).getPeriod().getID())
	    			{
	    	smaller=true;
	    			index=j;
	    			}

	    else
	    {	break;
	    }

	    		
	    }
	    if(smaller==true)
	    		{Evaluate_Sol.Students.get(s).get_EaxmList().add(index,Evaluate_Sol.Students.get(s).get_EaxmList().get(i));
	    		Evaluate_Sol.Students.get(s).get_EaxmList().remove(i+1);
	    		smaller=false;
	    }}

	 // testing ordering
	    	
	    	// 10/21 for(int i=0;i<Evaluate_Sol.Students.get(s).get_EaxmList().size();i++)
	    		//10/ 21 System.out.println("Student number("+Evaluate_Sol.Students.get(s).getID()+")= Period ID for exam NO("+i+")="+Evaluate_Sol.Students.get(s).get_EaxmList().get(i).getPeriod().getID());
	    	//if(Evaluate_Sol.Students.get(s).getID()==50)
	    		//for(short l=0;l<Evaluate_Sol.Students.get(s).get_EaxmList().size();l++)
	    //	System.out.println("exam ID for student after ordering"+Evaluate_Sol.Students.get(s).get_EaxmList().get(l).getID())	;
	    int No_Exams=Evaluate_Sol.Students.get(s).get_EaxmList().size();
	    int gaps=0;
	    //end of ordering 
	    
	    
	    
	    for(int i=0;i<No_Exams-1;i++)
	    { 
	      for(int j=i+1;j<No_Exams;j++)
	      {gaps=Evaluate_Sol.Students.get(s).get_EaxmList().get(j).getPeriod().getID()-Evaluate_Sol.Students.get(s).get_EaxmList().get(i).getPeriod().getID();
	      if(gaps==0)
	    	  {System.out.println("There is a conflict !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    	  penaltyPS_forThisStudent+=10000000;
	    	  }
	    	  
	      if(gaps<=5)
	        	  penaltyPS_forThisStudent+=Math.pow(2, (5-gaps));  
	    
	    }
	    }
	    return penaltyPS_forThisStudent;
	    }
	    
	    
//**************************************************************************************************************	    


}//end class