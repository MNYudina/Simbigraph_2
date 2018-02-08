package texts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.ImageIcon;

import simbigraph.gui.MainFrame;

public class GetText {
public static String getText(String filename){
    String st="";
    InputStream is= null;
	try {
		Class cl = MainFrame.class;
		URL url =cl.getResource("/texts/"+filename+".txt");
		
		is = cl.getResourceAsStream("/texts/"+filename+".txt");
		
        BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(is));
        String str;
        while ((str = in.readLine()) != null) {
            st=st+str+"\r\n";
        }
        in.close();
    } catch (IOException e) {
    	System.out.println("не считалось");
    }
	return st;
}
}
