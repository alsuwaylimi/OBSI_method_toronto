package timetable;

import java.util.ArrayList;

public class Indvi {
private long fitValue;
private double inverseFitValue;
private double fitRatio;
private long interval;
private Problem Pro;
private ArrayList<Gene> Chrom=new ArrayList<>();


// constructors =================================================================================================
Indvi(){}

Indvi (Problem Pro)
{
	this.Pro=Pro.Copy();
}

Indvi (Problem Pro, long fitValue,double inverseFitValue, double fitRatio, long interval)
{
	this.Pro=Pro.Copy();
	this.fitValue=fitValue;
	this.inverseFitValue=inverseFitValue;
	this.fitRatio=fitRatio;
	this.interval=interval;
}

// set methods ==================================================================================================
public void setFitValue(long fitValue)
{
	this.fitValue=fitValue;
	if(fitValue!=0)
	this.inverseFitValue=Math.round((1.0/fitValue)*1000000);
	
	
}

public void setInterval(long interval)
{
	this.interval=interval;
}

public void setInverseFitValue(double inverseFitValue)
{
	this.inverseFitValue=inverseFitValue;
}

public void setFitRatio(double fitRatio)
{
	this.fitRatio=fitRatio;
}

public void set(Problem Pro)
{
	this.Pro=Pro.Copy();
}

public void buildChrom()
{
for(int i=0;i<Pro.getExams().size();i++)
	Chrom.add(new Gene(Pro.getExams().get(i).getPeriod().getID(),Pro.getExams().get(i).getRoom().getID()));
}


public void addExam(Gene gene)
{
	Chrom.add(gene);
}

public Gene getGene(int index)
{
	return Chrom.get(index);
}


// get methods ==================================================================================================
public Long getfitValue()
{
    return fitValue;
}

public double getInverseFitValue()
{
	return inverseFitValue;
}


public double getfitRatio()
{     return fitRatio;
    
    
}


public long getInterval()
{
	return interval;
}

public Problem getPro()
{
    return Pro;
}


public ArrayList<Gene> getChrom()
{
    return Chrom;
}
public void printChrom()
{
	for(int i=0;i<Chrom.size();i++)
		System.out.println("Exam NO("+i+")="+Chrom.get(i).getPeriodID()+", "+Chrom.get(i).getRoomID());
}

}
