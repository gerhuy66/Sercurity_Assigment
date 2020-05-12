
import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
public class Client{
	public static void main(String[] args) throws IOException,ArrayIndexOutOfBoundsException{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter user full name:");
		String fname = sc.nextLine();
		List<Integer> asii_arr = new ArrayList<Integer>();
		int i = 0;
		//convert each char into number type 'int' and put into asii_arr
		for(Character c : fname.toCharArray()){
			asii_arr.add((int)c);
			i++;
		}
		sc.close();
		//Get Key
		String client_pri_key="",client_n="",server_pub_key="",server_n="";
		FileReader file = new FileReader("KeyGerner.txt");
		BufferedReader reader = new BufferedReader(file);
		int line_num =0;
		while(reader.ready()){
			String keyLine = reader.readLine();
			line_num++;//Client line = 1, server line  = 2 (see in KeyGerner.txt)
			String[] rsArr = keyLine.split(",");//spit by ","
			if(line_num ==1){
				client_pri_key = rsArr[1];//client private key
				client_n = rsArr[2];//client 'N' number
			}else{
				server_pub_key = rsArr[0];//server public key
				server_n = rsArr[2];//server 'N' number
			}
			
		}
		reader.close();
		file.close();
		//Send socket to client with Encryted Message
		try{
		Socket clientSocket = new Socket("localhost", 4999);
		ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
		for (i = 0; i < asii_arr.size(); i++) {
			BigInteger B_asii_val = new BigInteger(asii_arr.get(i).toString().trim());
			//First encode with client priavate key
			BigInteger cypher_val = B_asii_val.modPow(new BigInteger(client_pri_key.trim()),
					new BigInteger(client_n.trim()));
			//After that encode with server public key
			BigInteger cypher_val_final = cypher_val.modPow(new BigInteger(server_pub_key.trim()),
					new BigInteger(server_n.trim()));

			oos.writeObject(cypher_val_final);
			oos.flush();//push socket
		}
		oos.writeObject(new BigInteger("-1"));
		oos.flush();

		clientSocket.close();
		}catch(Exception err){
			err.printStackTrace();
		}
	}
}