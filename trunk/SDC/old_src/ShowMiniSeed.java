import java.io.*;
import java.util.*;
import javax.swing.*;

public class ShowMiniSeed extends JFrame{

    public ShowMiniSeed(String filePath){

	SpecialMiniSeedReader smr = new SpecialMiniSeedReader();
	smr.readFile(filePath);
	System.out.println("DISPLAYING");
	getContentPane().add(smr.DisplayData());	

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	this.setSize(800,450);
	// this.pack();
	this.setVisible(true);
    }

    public static void main(String argv[]){
	int argc = argv.length;
	
	if (argc < 0 ) {
	    System.out.println("No files to display");
	}

        for (int i=0; i<argc; i++){

	    String file = argv[i];
	    File f = new File(file);

   	    if (f.exists()==false){
		System.out.println("ERROR: File not exist. " + file);
		continue;
	    }	    
	    ShowMiniSeed show = new ShowMiniSeed(file);
	}	
    }

}
