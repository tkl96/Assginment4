import java.io.*;
import java.util.*;
import java.nio.file.*;

public class Voter{
	private int current_user;
	private ArrayList<String[]> names=new ArrayList<String[]>();
	public Voter(){
		File file=new File("voters.txt");
		try{
			if (!file.exists()){
				System.out.println("Cannot find any voter!");
				System.exit(0);
			}
			Scanner input = new Scanner(file);
			while (input.hasNext()){
				String s=input.nextLine();
				String[] s1=s.split(":");
				names.add(s1);
			//	System.out.println(s1[2]);
			}
		}catch(Exception exc){
			//System.out.println(exc);
		} 
	}
	//check id -1 does not exist index number
	public int check_id(String str){
		for (int i=0;i<names.size();i++){
			String[] s1=names.get(i);
			if(s1[0].equals(str)){
				return(i);
			}
		}
		return -1;
	}
	//check voted
	public boolean voted(int index){
		current_user=index;
		if ((boolean)names.get(index)[2].equals("true")){
			return true;
		}
		
		return false;
	}

	public String name(int index){
		return names.get(index)[1];
	}
	//create backup file
	public void update(){
		try{names.get(current_user)[2]="true";
			FileWriter fwriter = new FileWriter("voters");
			PrintWriter outputFile = new PrintWriter(fwriter);
			for (int i=0;i<names.size();i++){
				outputFile.println(names.get(i)[0]+":"+names.get(i)[1]+":"+names.get(i)[2]);
			}
			outputFile.close();
		}catch(Exception exc){
			//System.out.println(exc);
		} 
	}
	//save file
	public void save(){
		try{
			names.get(current_user)[2]="true";
			String fileToWrite="voters.txt";
			String backupFile="voters";
			File a = new File(fileToWrite);
		    File b = new File(backupFile);
		    Files.copy(b.toPath(), a.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    Files.delete(b.toPath());
	    }catch(IOException ioex) {
			//System.out.println(ioex);
		}
	}

	public void add_voter(String[] s){
		names.add(s);
	}

	//cancel do not need to work
	// public void neversave(){
	// 	try{
	// 		names.get(current_user)[2]="false";			
	// 		String backupFile="voters";
	// 	    File b = new File(backupFile);
	// 	    Files.delete(b.toPath());
	//     }catch(IOException ioex) {
	// 		//System.out.println(ioex);
	// 	}
	// }
}