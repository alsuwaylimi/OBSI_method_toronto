package timetable;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Comment;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;


import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;

import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;


public  class TestProject {

	public static List<PrintResults>  experiment=new ArrayList<>();

	/**
	 * @param args
	 * @throws IOException 
	 * @throws WriteException 
	 * @throws TransformerException 
	 * @throws ParserConfigurationException 
	 * @throws CloneNotSupportedException 
	 */
	
	
	public static void main(String[] args) throws WriteException, IOException, ParserConfigurationException, TransformerException {
	//System.out.println("helooooooooooooo");
	
		Scanner No=new Scanner (System.in);
		GA ga = new GA();
		//Constraint constt = new Constraint();
		
		String instanceNo ="YOR83";
		String Alg1="OBSI";
//		String Alg2 ="LD";
//		String Alg3 ="LWD";
//		String Alg4 ="LE";
//		String Alg5 ="SD";
//		String Alg6 ="RD";
		
		String ExpSol="YOR83_Run";

		System.out.print("Number of runs: ");
		
		
		Problem time = new Problem ((byte)8);
		
		int No_run = No.nextByte();
		
		for(int i=0; i< No_run ; i++){

	//long startTime = System.currentTimeMillis();
	//  OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	   //  System.out.println("dfvgbhnm,"+operatingSystemMXBean.getSystemLoadAverage()); 
	     long startCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();  
	     
	     ga.run();

	     long totalUsedCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()-startCPUTime;
	     
	     //long end = System.currentTimeMillis();

	//long diff = (end - startTime)/1000;
	     
	long ExecutionTime= (long) (totalUsedCPUTime/1E6);
	
	//comment it for EXP ???????? ******
	//System.out.println("Run "+"("+i+ ") "+"  Execution time (milliseconds): " + ExecutionTime);
	
	//System.out.println("Execution time (seconds) " + diff);
	//System.out.println("Execution time (miunts) " + diff/60);
	double a = ga.population.get(0).getfitValue()/Problem.no_students;

	//System.out.println("My cost value: " + a);
	
	String timee = Long.toString(ExecutionTime);
	String aa = Double.toString(a);
	experiment.add(new PrintResults(instanceNo, Alg1, aa, timee));
	PrintXlss(experiment);

	ExpSol+=i+1;
	printing(ga.population.get(0).getPro(), ExpSol);
//	createXML(ga.population.get(0).getPro(),time);
	
	//time.print_allstudentsIDs();
	//StAXCreateXML(ga.population.get(0).getPro(),time);
	//StAXCreateXML2(ga.population.get(0).getPro(),time);
	//ExpSol+=i+1;
	//printing(ga.population.get(0).getPro(), ExpSol);
		ga.population.remove(0);
		ExpSol ="YOR83_Run";
		}
		
		
		printValues(experiment);
		
		//PrintXlss(experiment);
		}
	
	
	
	public static void printValues(List<PrintResults> experiment2){
	  
	  for(int i=0; i<experiment.size();i++){
	  
	  System.out.println("Result " +
	  "Name of instance: "+experiment.get(i).getInstanceName() +
	  " Approach: "+experiment.get(i).getApproahc() +
	  " Cost Value: "+experiment.get(i).getCostValue()+" Time: "
	  +experiment.get(i).getTime());} }
	  
	  
	  
	  public static void printing(Problem sol, String ExpSol) throws
	  FileNotFoundException, UnsupportedEncodingException{
	  
	  
	  File file=new File("SolStore/"+ExpSol+".sln"); 
	  PrintWriter p=new PrintWriter(file,"UTF-8");
	  p.print(sol.Exams.get(0).getPeriod().getID()+", "+sol.Exams.get(0).getRoom().
	  getID()+"\r\n"); 
	  for(int i=1;i<sol.Exams.size();i++)
	  p.append(sol.Exams.get(i).getPeriod().getID()+", "+sol.Exams.get(i).getRoom().getID()+"\r\n"); 
	  p.close(); 
	  }
//	  
//	 
//
public static void PrintXlss(List<PrintResults> experiment) {
	

	File file=new File("Results.xls");

	if(!file.exists())
	{
		try {
			createExcel();
			writingExcel();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}
	try {
		writingExcel();
	} catch (WriteException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} 
}
    	
     	
public static void createExcel() throws WriteException, IOException, BiffException {

	try{
		WritableWorkbook workbook = Workbook.createWorkbook(new File("Results.xls"));
		workbook.createSheet("Results.xls",0);
		workbook.write();
		workbook.close();
	} 
	catch (Exception e){
		e.printStackTrace();
	}

}

public static void writingExcel()  throws IOException, WriteException{
	try{

		Workbook aWorkBook = Workbook.getWorkbook(new File("Results.xls"));
		WritableWorkbook aCopy = Workbook.createWorkbook(new File("Results.xls"), aWorkBook);
		WritableSheet aCopySheet = aCopy.getSheet(0);

		Label label0 =  new Label(0, 0, experiment.get(0).getInstanceName());
		Label label1 =  new Label(1, 0, "Approach");
		Label label2 =  new Label(2, 0, "Cost_value");
		Label label3 =  new Label(3, 0, "Time");

		aCopySheet.addCell((WritableCell) label0);
		aCopySheet.addCell((WritableCell) label1);
		aCopySheet.addCell((WritableCell) label2);
		aCopySheet.addCell((WritableCell) label3);

		for(int i=1; i<=experiment.size();i++){

			aCopySheet.addCell((WritableCell) new Label(0, i, experiment.get(i-1).getInstanceName()));
		}

		
		for(int i=1; i<=experiment.size();i++){

			aCopySheet.addCell((WritableCell) new Label(1, i, experiment.get(i-1).getApproahc()));

		}

		
		for(int i=1; i<=experiment.size();i++){

			aCopySheet.addCell((WritableCell) new Label(2, i, experiment.get(i-1).getCostValue()));

		}

		
		for(int i=1; i<=experiment.size();i++){

			aCopySheet.addCell((WritableCell) new Label(3, i, experiment.get(i-1).getTime()));

		}

		aCopy.write();
		aCopy.close();

	} catch (Exception e){
		e.printStackTrace();
	}


	 
	


}


 

}