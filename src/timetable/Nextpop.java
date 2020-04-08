/*recently just remove this mark
package timetable;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Nextpop {
	//private static List<Indvi> preAndCurrentPop=new ArrayList<>();	

private static List<Indvi> nextpop=new ArrayList<>();	
public static List<Indvi> generate(List<Indvi> pop, GA ga) throws FileNotFoundException, UnsupportedEncodingException
{ //preAndCurrentPop.clear();
nextpop.clear();
	//elitism(pop,ga);
	//crossover(pop,ga);
      uniCrossover(pop,ga);
	mutate(pop,ga);
	rest(pop,ga);
	//System.out.println("NextPopSize="+nextpop.size());
	for(int i=0;i<nextpop.size();i++)
	Fitness.setFit(nextpop.get(i));

	
	Fitness.setRatio(nextpop);
	Fitness.setInterval(nextpop);	
	
	//return generateNextPop(ga.getPopSize(),ga.getMaxIdentical());
	return nextpop;
}

public static void elitism(List<Indvi>pop, GA ga)
{ 
 // List <Indvi> elitism=new ArrayList<>();
	int ElitismSize=0;
	ElitismSize=(int) (ga.getPopSize()*ga.getElitismRatio());
	// today System.out.println(ElitismSize);
  for(int i=0;i< ga.getPopSize();i++)
	  for(int j=0;j<ElitismSize;j++)
	if(pop.get(i).getfitValue()<ga.getElitismList(j).getfitValue() && !isEqualElitism(ga.getAllElitismList(), pop.get(i), 2))	
		{ga.removeElitismList(j);
		 ga.addElitismList(new Indvi(pop.get(i).getPro(),pop.get(i).getfitValue(),pop.get(i).getInverseFitValue(),pop.get(i).getfitRatio(),pop.get(i).getInterval()));
		}
 
 for(int i=0;i<ElitismSize;i++)
	  {nextpop.add(new Indvi(ga.getElitismList(i).getPro(),ga.getElitismList(i).getfitValue(),ga.getElitismList(i).getInverseFitValue(),ga.getElitismList(i).getfitRatio(),ga.getElitismList(i).getInterval()));
	  
	  }}

public static void uniCrossover(List<Indvi> pop,GA ga) throws FileNotFoundException
{
	int crossoverSize=0;
	crossoverSize=(int) (pop.size()*ga.getCrossoverRatio());
	Indvi Indvi1;
	Indvi Indvi2;
	for(int cross=0;cross<crossoverSize;cross++)
	{
		while(true) {
	 Indvi1=Selection.selectRoulette(pop);
	 
	 Indvi2=Selection.selectRoulette(pop);
	if(!isEqual(Indvi1,Indvi2))
	break;	
	
	
		}
	Indvi CrossedSols=UniformCossover.crossover(Indvi1, Indvi2, ga);
	
}
}
public static void crossover(List<Indvi> pop,GA ga) throws FileNotFoundException
{int crossoverSize=0;
crossoverSize=(int) (pop.size()*ga.getCrossoverRatio());
Indvi Indvi1;
Indvi Indvi2;
for(int cross=0;cross<crossoverSize;cross++)
{
	while(true) {
 Indvi1=Selection.selectRoulette(pop);
 
 Indvi2=Selection.selectRoulette(pop);
if(!isEqual(Indvi1,Indvi2))
break;	
	}
List<Indvi> CrossedSols=Crossover.crossover(Indvi1, Indvi2, ga);
//System.out.println("CrossedSols"+CrossedSols.size());
for(int j=0;j<CrossedSols.size();j++)
	Fitness.setFit(CrossedSols.get(j));	
boolean large=false;
int index=0;
{for(int i=1;i<CrossedSols.size();i++)		
	{index=i;
			for(int j=i-1;j>=0;j--)
	{if(CrossedSols.get(j).getfitValue()>CrossedSols.get(i).getfitValue())
				{
		large=true;
				index=j;
				}

	else
	{	break;
	}

			
	}
	if(large==true)
			{CrossedSols.add(index,CrossedSols.get(i));
			CrossedSols.remove(i+1);
			large=false;
	}}}

if(CrossedSols.size()>=1)
	nextpop.add(CrossedSols.get(0));

//testing

for(int i=0;i<CrossedSols.size();i++)	
	System.out.println("Cross"+i+"="+CrossedSols.get(i).getfitValue());

}
}
public static void mutate(List <Indvi> pop, GA ga) throws FileNotFoundException, UnsupportedEncodingException
{ 
	
	int NoSolsMutated=(int)(ga.getPopSize()*ga.getMutationRatio());
	
for(int i=0;i<NoSolsMutated;i++)
	{ 
	  boolean mutated=false;

	  while(mutated==false)
		  
	  {Indvi indviMutated;
	  indviMutated=Selection.selectRoulette(pop);
		  mutated=Mutation.mutation(indviMutated,ga);
	   if(mutated==true)
	     {indviMutated.getChrom().clear();
	     indviMutated.buildChrom();
		   nextpop.add(indviMutated)	;
	     }
	  // else
		//  System.out.println("************  Mutation Field  ************");

      }
 
    }
}

public static void rest(List<Indvi> pop, GA ga)
{
int restSize=0;
restSize=pop.size()-nextpop.size();
Indvi indvi;
int size=0;
while(size!=restSize)
{
indvi=Selection.selectRoulette(pop);
if(!isEqual(nextpop,indvi,ga.getMaxIdentical()))
{nextpop.add(indvi);
size++;
}
}
}
	
	/*
public static List<Indvi> generateNextPop(int sizePop,int max)
{
	//orderingIncresinglyPop(preAndCurrentPop);
//binary selection***********************************
	/*
	List<Indvi> binaryList=new ArrayList<>();
	int size=0;
	while(size!=sizePop)
	{binaryList.clear();
	 for(int j=0;j<2;j++)
		 binaryList.add(Selection.selectRandomly(preAndCurrentPop));
	 orderingIncresinglyPop(binaryList);
		if(!isEqual(nextpop,binaryList.get(0)))
		{nextpop.add(new Indvi(binaryList.get(0).getPro(),binaryList.get(0).getfitValue(),binaryList.get(0).getInverseFitValue(),binaryList.get(0).getfitRatio(),binaryList.get(0).getInterval()));
		size++;

		}}
	
	
	
	*/

	
//end of binary selection ***********************************
	
	
	
	//best selection**********************************
	/*int size=0;
	while(size!=sizePop)
	{
	
	for(int i=0;i<sizePop;i++)
		if(!isEqual(nextpop,preAndCurrentPop.get(i),max))
		{nextpop.add(preAndCurrentPop.get(i));
		size++;
		*/
		//}
	//end of best selection**************************
	/*}
	Fitness.setRatio(nextpop);
	Fitness.setInterval(nextpop);
	return nextpop;
	
	
}
*/

/*recently just remove this mark
public static List<Indvi>  orderingIncresinglyPop(List<Indvi> pop)
{
	boolean smaller=false;
	int index=0;
		for(int i=1;i<pop.size();i++)
	{
			
			index=i;
		
			for(int j=i-1;j>=0;j--)
	{if(pop.get(i).getfitValue()<pop.get(j).getfitValue())
				{
		smaller=true;
				index=j;
				}

	else
	{	break;
	}

			
	}
	if(smaller==true)
			{pop.add(index,pop.get(i));
			pop.remove(i+1);
			smaller=false;
	}}
	
return pop;
}

public static boolean isEqual(List<Indvi> listPop, Indvi indvi, int max)
{int maxIdentical=0;
int maxdiff=3;
int diff=0;
	for(int i=0;i<listPop.size();i++)
	{	boolean equal=true;
	diff=0;
		for(int j=0;j<listPop.get(i).getPro().getExams().size();j++)
		{
			if(listPop.get(i).getPro().getExams().get(j).getPeriod().getID()!=indvi.getPro().getExams().get(j).getPeriod().getID())
			//|| listPop.get(i).getPro().getExams().get(j).getRoom().getID()!=indvi.getPro().getExams().get(j).getRoom().getID()	)
			{diff++;
			if(diff==maxdiff)
				
		
				equal= false;
			}}
	if(equal==true)
		maxIdentical++;
		if(equal==true && maxIdentical==max)
		return true;
	}
return false;
}


//for Elitism
public static boolean isEqualElitism(List<Indvi> listPop, Indvi indvi, int max)
{int maxIdentical=0;

	for(int i=0;i<listPop.size();i++)
	{	boolean equal=true;
	
		for(int j=0;j<listPop.get(i).getPro().getExams().size();j++)
		{
			if(listPop.get(i).getPro().getExams().get(j).getPeriod().getID()!=indvi.getPro().getExams().get(j).getPeriod().getID())
			//|| listPop.get(i).getPro().getExams().get(j).getRoom().getID()!=indvi.getPro().getExams().get(j).getRoom().getID()	)
			
		
				equal= false;
			}
	if(equal==true)
		maxIdentical++;
		if(equal==true && maxIdentical==max)
		return true;
	}
return false;
}

//for Crossover
public static boolean isEqual(Indvi indvi1, Indvi indvi2)
{
int diff=0;
int MaxNodiff=3;
	

		for(int j=0;j<indvi2.getPro().getExams().size();j++)
		
			if(indvi2.getPro().getExams().get(j).getPeriod().getID()!=indvi1.getPro().getExams().get(j).getPeriod().getID())
			{//|| listPop.get(i).getPro().getExams().get(j).getRoom().getID()!=indvi.getPro().getExams().get(j).getRoom().getID()	)
		diff++;
		if(diff==MaxNodiff)
				return false;
			}
return true;
}



}
*/ //recently just remove this marks