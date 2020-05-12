import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.math.BigInteger;
public class Server{
	public static void main(String args[]) throws IOException{
		String server_pri_key="",server_n="",client_pub_key="",client_n="";
		//Get Key
		try{
		
		FileReader file = new FileReader("KeyGerner.txt");
		BufferedReader reader = new BufferedReader(file);
		int line_num =0;
		//read key, N from file KeyGerner.txt
		while(reader.ready()){
			String keyLine = reader.readLine();
			line_num++;
			String[] rsArr = keyLine.split(",");
			if(line_num ==2){
				server_pri_key = rsArr[1];//server priavate key
				server_n = rsArr[2];//server 'N' number
			}else{
				client_pub_key = rsArr[0];//client public key
				client_n = rsArr[2];//client 'N' number
			}
			
		}
		reader.close();
		file.close();
		}catch(Exception get_key_err){
			get_key_err.printStackTrace();
		}
		//Read socket and print message from client
		try{ 
			ServerSocket serv = new ServerSocket(4999);//port number = 4999
			Socket socket = serv.accept();//accept a socket that pushed by client
			System.out.println("*** Client Connected ! ***");         
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  
			BigInteger big_neg_1 = new BigInteger("-1");
			System.out.print("Client: ");
			while ( true){ 
				BigInteger bigInt = (BigInteger) ois.readObject();
				if (bigInt.equals(big_neg_1))                
					break;
				//First Decode by server private key       
				BigInteger rs = bigInt.modPow(new BigInteger(server_pri_key.trim()), new BigInteger(server_n.trim()));
				//After that Decode by Client public key
				BigInteger rs_final = rs.modPow(new BigInteger(client_pub_key.trim()), new BigInteger(client_n.trim()));
				
				System.out.print((char)rs_final.intValue());
			} 
			socket.close();            
			serv.close(); 
		}catch(Exception socket_err){
			socket_err.printStackTrace();
		} 
	}


}