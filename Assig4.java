import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.lang.*;

public class Assig4{
	static Scanner sc=new Scanner(System.in);
	static ArrayList<Ballot> ballots=new ArrayList<Ballot>(); 
	static JButton logIn= new JButton("Log In");
	static JButton castVote= new JButton("Cast your vote");
	static JButton newCandi= new JButton("Administrator");
	static JFrame window = new JFrame("Vote");
	static JPanel panel=new JPanel();
	static Voter voter=new Voter();

	private static final int HEIGHT = 400;
    private static final int WIDTH = 800;
        private static final String password="TK is smart";


    //init read
	public static void init(String str){
		File file=new File(str);
		try{//find the main loading file
			if (!file.exists()){
				System.out.println("File does not exist, please double check!");
				System.exit(0);
			}
		//read main file
		Scanner input = new Scanner(file);
		//num of ballots
		int num=input.nextInt();
		String s=input.nextLine();
			//read ballot
			for(int i=0;i<num;i++){
				 s=input.nextLine();
				//System.out.println(s);
    			
				Ballot ballot=new Ballot(s);
				//create arraylist for ballots
				ballots.add(ballot);
				//System.out.println(ballots.size());
				//read each questions	
			}
		}catch(Exception exc){
			System.out.println(exc);
		} 
	}	

	//log in part
	public static void login(){
		String inputValue = JOptionPane.showInputDialog("Please enter your voter ID: "); 
		String s=inputValue;
		if (s!=""){
			//whether have right to vote
			if (voter.check_id(s)>= 0){
				int index=voter.check_id(s);
				//whether voted
				boolean b=voter.voted(index);
				//System.out.println(b);
				if (!b){//haven't vote
					String str= "Welcome, "+voter.name(index);
					JOptionPane.showMessageDialog(window, str ,"Log in successfully", JOptionPane.INFORMATION_MESSAGE);
					for (Ballot ballot:ballots){
						ballot.able();
					}
					logIn.setEnabled(false);
					castVote.setEnabled(true);
					newCandi.setEnabled(false);
				}
				else{
					//have voted
					String str= voter.name(index)+", you have already voted!";
					JOptionPane.showMessageDialog(window,str,"You cannot voted twice", JOptionPane.INFORMATION_MESSAGE);
					logIn.setEnabled(true);
					castVote.setEnabled(false);
					newCandi.setEnabled(true);
				}
			}else {
				//does not exist
				JOptionPane.showMessageDialog(window, "Voter haven't registered!","Invilid input", JOptionPane.WARNING_MESSAGE);
				logIn.setEnabled(true);
				castVote.setEnabled(false);
				newCandi.setEnabled(true);
			}
			
		}	
	}

	//when user click yes
	public static void update(){
		//update voter condition
		voter.save();
		//update candidate condition
		for (int i=0;i<ballots.size();i++){
			Ballot ballot=ballots.get(i);
			String s=ballot.get_id()+".txt";
			File f=new File(s);
			//ballot file not exist create new file
			if (!f.exists()){
				ballot.initFile();//init the ballot file but nothing in it
			}
			//create backupfile for ballots
			ballot.file(ballot.get_id());			
			////safe save
			safesave(s,ballot.get_id());
			//change button status
			ballot.disable();
			
			ballot.clear_selection();
		}
		castVote.setEnabled(false);
		logIn.setEnabled(true);
		newCandi.setEnabled(true);	
	}
	//save safe
	public static void safesave(String str1,String str2){
		try{
			File a=new File(str1);
			File b=new File(str2);
			Files.copy(b.toPath(), a.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    Files.delete(b.toPath());
		}catch(IOException ioex) {
			//System.out.println(ioex);
		}
	}

	public static void cast_vote(){

		Object[] options = {"Yes","No","Cancel"};
		int n = JOptionPane.showOptionDialog(window, "Confirm Vote? ","Vote Confirmation",
		JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,
	    options,options[0]);

		//create backup file
		
		voter.update();
	    //confirm yes
	    if (n==0) {
	    	update();
	    	JOptionPane.showMessageDialog(window,"Thank you for your vote!","Vote successfully", JOptionPane.INFORMATION_MESSAGE);
	    	
	    	JButton test= ballots.get(0).selected_candidate();
	    	//System.out.println(test.getText());
	    	castVote.setEnabled(false);
			logIn.setEnabled(true);	
			newCandi.setEnabled(true);
	    } else{

	    }
	    //if cancel do not need to work
	  //    else if(n==2){
	  //   	cancellation();
	  //   	JOptionPane.showMessageDialog(window,"Vote Canceled!","cancellation", JOptionPane.INFORMATION_MESSAGE);
	  //   	castVote.setEnabled(false);
			// logIn.setEnabled(true);	
	  //   }
	    
	}

	// cancel button do not need to do anaything
	// public static void cancellation(){
	// 	voter.neversave();
	// 	for (int i=0;i<ballots.size();i++){
	// 		ballots.get(i).clear_selection(); 
	// 		ballots.get(i).disable();
	// 	}
		
		
	// } 

    public static void visualize(){
   		
    	window.setLayout(new BorderLayout());
    	panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    	window.setSize(WIDTH, HEIGHT);
    	//add ballots to window

    	for (int i=0;i<ballots.size();i++){
    		Ballot ballot=ballots.get(i);
    		panel.add(ballot);
    		ballot.setLayout(new BoxLayout(ballot, BoxLayout.Y_AXIS));
    		
    		//System.out.println(ballot.name);
    	}
    	//add log button
    	panel.add(logIn);
    	ActionListener log = new Log();
		logIn.addActionListener(log);
		//add cast button
    	panel.add(castVote);
    	castVote.setEnabled(false);
    	ActionListener cast = new Cast();
    	castVote.addActionListener(cast);
    	//add new Candi button
    	panel.add(newCandi);
    	ActionListener admin= new Admin();
    	newCandi.addActionListener(admin);
    	window.add(panel,BorderLayout.CENTER);

    	window.setVisible(true);

    }
    //action for login button
    static class Log implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==logIn){
				login();
			}
					
   		}
	}

	//action for cast button
	static class Cast implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==castVote){
				cast_vote();
			}
				
   		}
	}		

	//action for add new voters
	static class Admin implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==newCandi){
				authrization();
			}
					
   		}
	}

	static void authrization(){
		String inputValue = JOptionPane.showInputDialog("Please enter password: ");
		String s=inputValue;
		String name;
		if (s!=""){
			if (!s.equals(password)){
				JOptionPane.showMessageDialog(window, "password incorrect","Authrization failed", JOptionPane.WARNING_MESSAGE);
			}else {
				Object[] options = {"Add candidate","Add Voter","Cancel"};
				int n = JOptionPane.showOptionDialog(window, "What do you want? ","Vote Confirmation",
				JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,
			    options,options[2]);
				if (n==0){
					Candi();
				} else if (n==1){
					vot();
				}
			}
		} 	
	}

	public static boolean ballot_exist(String str){
		for (Ballot ballot:ballots) {
			if (ballot.get_id().equals(str)){
				return true;
			}
		}
		return false;
	}

	public static void showDialog() {

    }
    //add voter
    public static void vot(){
		
		JPanel p = new JPanel(new BorderLayout(100,100));

        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        //labels.add(new JLabel("candidate id", SwingConstants.RIGHT));
        labels.add(new JLabel("Voter id", SwingConstants.RIGHT));
        labels.add(new JLabel("vote name", SwingConstants.RIGHT));
        p.add(labels, BorderLayout.WEST);

        JPanel controls = new JPanel(new GridLayout(0,1,2,2));
        JTextField id = new JTextField(10);
        controls.add(id);
        JTextField name = new JTextField();
        controls.add(name);
        //JTextField vote = new JTextField();
        //controls.add(vote);
        p.add(controls, BorderLayout.CENTER);
        
       
        JOptionPane.showMessageDialog(
            window, p, "Add Voter", JOptionPane.QUESTION_MESSAGE);
        String[] str=new String[3];
		str[0]=id.getText();
		str[1]=name.getText();
		str[2]="false";
		voter.add_voter(str);
		//s3=vote.getText();
	}
	// pos in list
	public static int ballots_id(String str){
		for (int i=0;i<ballots.size();i++ ) {
			if (str.equals(ballots.get(i).get_id())){
				return i;
			}
		}return -1;
	}

	public static void Candi(){
		
			JPanel p = new JPanel(new BorderLayout(100,100));

        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        labels.add(new JLabel("ballot id", SwingConstants.RIGHT));
        labels.add(new JLabel("candidate name", SwingConstants.RIGHT));
       // labels.add(new JLabel("vote earned", SwingConstants.RIGHT));
        p.add(labels, BorderLayout.WEST);

        JPanel controls = new JPanel(new GridLayout(0,1,2,2));
        JTextField id = new JTextField(10);
        controls.add(id);
        JTextField name = new JTextField();
        controls.add(name);
        //JTextField vote = new JTextField();
       // controls.add(vote);
        p.add(controls, BorderLayout.CENTER);
        
       
        JOptionPane.showMessageDialog(
            window, p, "Add candidate", JOptionPane.QUESTION_MESSAGE);
        String s1,s2,s3;
		s1=id.getText();
		s2=name.getText();
		//s3=vote.getText();
		if (ballot_exist(s1)){
			int index=ballots_id(s1);
			ballots.get(index).add_candy(s2);
			JOptionPane.showMessageDialog(window, "candidate added","Add candidates successful", JOptionPane.WARNING_MESSAGE);

		} else{
			JOptionPane.showMessageDialog(window, "Ballot number incorrect","Add candidates failed", JOptionPane.WARNING_MESSAGE);
	
		}
		
	}

	public static void main(String[] args) {
		//check input
		String str;
		if (args.length==0){
			System.out.println("please enter the name of the file: ");
			str=sc.next();
		} else{
			 str=args[0];
		}
		//read file
		init(str);
		//create window
		visualize();
				
	}
}