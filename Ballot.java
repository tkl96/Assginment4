import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class Ballot extends JPanel{
	private String id;
	private String name;//name of the ballot
	private ArrayList<JButton> candidates=new ArrayList<JButton>();
	private JButton selected_candidate=new JButton();
	private String[] str1;
	//public JPanel ballot=new JPanel();

	public String  get_id(){
		return id;
	}

	public String get_name(){
		return name;
	}

	public Ballot(String str){
		String[] s = str.split(":");
		id= s[0]; //get ballot id
		
		name=s[1]; //get name of ballot
		
		JLabel label=new JLabel(name);
		add(label,BorderLayout.CENTER);
		
		//add all candidates
		String[] str1= s[2].split(",");
		for (String str2 : str1){
			JButton candidate = new JButton(str2);
			ActionListener choose = new Choose();
			candidate.addActionListener(choose);
		 	candidate.setEnabled(false);
			candidates.add(candidate);
		 	add(candidate,BorderLayout.CENTER);
			setLayout(new FlowLayout());
		}
	}

	public void able(){
		for(JButton button:candidates){
			button.setEnabled(true);
		}
	}
	public void clear_selection(){
		selected_candidate.setForeground(Color.BLACK);
		//System.out.println("cancel "+selected_candidate.getText());
		selected_candidate=new JButton();
	}
	public void disable(){
		for(JButton button:candidates){
			button.setEnabled(false);
		}
	}

	public void file(String str){
		File f=new File(str+".txt");
		ArrayList<String> elements=new ArrayList<String>();// string array to store name and number  
		try{
		//find the main loading file	
		Scanner input = new Scanner(f);
		
		while (input.hasNext()){
				String s=input.nextLine();
				String[] s1=s.split(":");
				if (selected_candidate.getText().equals(s1[0])) {
					s1[1]=Integer.toString(Integer.parseInt(s1[1])+1);
					selected_candidate.setForeground(Color.BLACK);
					selected_candidate=new JButton();
				}
				s=s1[0]+":"+s1[1];
				elements.add(s);
			}		
		}catch(Exception exc){
		//	System.out.println(exc);
		} 

		try{//create backup file
			FileWriter fwriter = new FileWriter(str);
			PrintWriter outputFile = new PrintWriter(fwriter);
			//print out file
			for (String st:elements){
				outputFile.println(st);
			}
			outputFile.close();

		}catch(Exception exc){
		//	System.out.println(exc);
		} 
	
	}
	public void initFile(){// if no file exists, then create it
		File f=new File(id+".txt");
		if (!f.exists()){
			try{
				FileWriter fwriter = new FileWriter(id+".txt");
				PrintWriter outputFile = new PrintWriter(fwriter);
				for (int j=0;j<candidates.size();j++){
					outputFile.println(candidates.get(j).getText()+":0");
				}
				outputFile.close();
			}catch(Exception exc){
			//	System.out.println(exc);
			} 
		}
	}

	public ArrayList<JButton> get_candidates(){
		return candidates;
	}

	public void add_candy(String name){
		JButton candidate=new JButton(name);
		ActionListener choose = new Choose();
		candidate.addActionListener(choose);
	 	candidate.setEnabled(false);
		candidates.add(candidate);
	}

	public JButton selected_candidate(){
		return selected_candidate;
	}

	class Choose implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			selected_candidate.setForeground(Color.BLACK);
			if (selected_candidate !=(JButton)e.getSource()){
			selected_candidate=(JButton)e.getSource();
			selected_candidate.setForeground(Color.RED);
			//System.out.println(selected_candidate.getText());
		}else{
			
			//System.out.println("cancel "+selected_candidate.getText());
			selected_candidate=new JButton();
		}
			
   		}
	}
}