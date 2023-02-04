package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import model.Fichero;
import model.Usuario;
import persistencia.GestorBD;

public class AtenderPeticion extends Thread{

	private Socket s;
	private static GestorBD gb= new GestorBD();
	
	public AtenderPeticion(Socket s){
		this.s=s;
	}
	
	public void run() {
		try {
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out= new DataOutputStream(s.getOutputStream());
			
			System.out.println("mas ejecuciones");
			
			String pet= in.readLine();
			System.out.println(pet);
			if(pet!=null) {
				leerPeticion(pet, out, in);
			}
		
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void leerPeticion(String pet, DataOutputStream out, DataInputStream in) {
		if(pet.startsWith("POST ")) {
			String resp=""+"\n";
			try {
	 			String[] trozos=pet.split(" ");
				System.out.println(trozos[1]);
				Usuario u=gb.buscarUsuario(trozos[1]);
				if(u!=null) {
					if(u.getContrasegna().equals(trozos[2])) {
						 resp="OK "+"\n";
						 System.out.println("correcto");
					}else{
						System.out.println("this");
						 resp="ERROR: Usuario y/o contrasegna no validos"+"\n";
					}
				}else {
					System.out.println("or maybe this");
					resp="ERROR: Usuario y/o contrasegna no validos"+"\n";
				}
				System.out.println(resp);
				out.write(resp.getBytes());
				System.out.println("justo despues deenviar");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}if(pet.startsWith("INSERT: ")) {
			String resp=""+"\n";
			try {
	 			String[] trozos=pet.split(" ");
				System.out.println(trozos[1]);
				if(trozos[1].equals("Usuario")) {
					Usuario u=gb.buscarUsuario(trozos[2]);
					if(u==null) {
						System.out.println("por aqui se inserta un usuario");
						String apellidos=null;
						System.out.println(trozos.length);
						if(trozos.length==6) {
							apellidos=trozos[5];
						}
						Usuario us= new Usuario(trozos[2],trozos[4],apellidos,trozos[3]);
						
						boolean agnadido=gb.agnadirPersona(us);
						if(agnadido) {
							resp="OK "+"\n";
							out.write(resp.getBytes());
						}else {
							resp="ERROR: no se ha podido añadir el usuario"+"\n";
							out.write(resp.getBytes());
						}
					}else {
						resp="ERROR: Este usuario ya existe"+"\n";
						out.write(resp.getBytes());
						System.out.println("error por aqui");
					}
				}if(trozos[1].equals("Fichero")) {
					String respu="OK"+"\n";
					out.write(respu.getBytes());
					ServerSocket ss1= new ServerSocket(40401);
					Socket s1= ss1.accept();
					ObjectInputStream oin= new ObjectInputStream(s1.getInputStream());
					Fichero f= (Fichero)oin.readObject();
					f.setId(String.valueOf((Fichero.numficheros+1)));
					String ruta=f.getNombre();
					f.setRuta(ruta);//ver donde guardo esta mierda
					gb.agnadirFichero(f);
					
					FileOutputStream fout= new FileOutputStream(ruta);
					
					int byteLeido=in.read();
					while(byteLeido!=-1) {
						System.out.println(byteLeido);
						fout.write(byteLeido);
						byteLeido=in.read();
					}	
					System.out.println("esto termina al subir el fichero");
					Fichero.numficheros++;
					fout.close();
					s1.close();
					ss1.close();
				}
				

				System.out.println(resp);
				
				System.out.println("justo despues deenviar");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}if(pet.startsWith("GET: ")) {
			String resp=""+"\n";
			try {
	 			String[] trozos=pet.split(" ");
				System.out.println(trozos[1]);
				System.out.println("ey");
				if(trozos[1].equals("Usuario")) {
					System.out.println(trozos[2]);
					Usuario us= gb.buscarUsuario(trozos[2]);
					resp=us.getId()+" "+us.getContrasegna()+" "+us.getNombre()+" "+us.getApellidos()+"\n";
					out.write(resp.getBytes());
				}
				if(trozos[1].equals("Fichero")) {
					System.out.println("quiero un fichero bro");
					Fichero fEnv=gb.buscaFichero(trozos[2]);
					String ruta=fEnv.getRuta();
					System.out.println(ruta);
					File fEnvi= new File(ruta); 
					FileInputStream fin= new FileInputStream(fEnvi);
					int byteLeido=fin.read();
					while(byteLeido!=-1) {
						System.out.println(byteLeido);
						out.write(byteLeido);
						byteLeido=fin.read();
					}
					out.close();
					fin.close();
				}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}if(pet.startsWith("GET ALL: Ficheros ")) {
			
			try {
				
				String conf="ya"+"\n";
				
				System.out.println(conf);
				
				out.write(conf.getBytes());
				
				ServerSocket ss= new ServerSocket(40401);
				
				Socket s1= ss.accept();
				
				ObjectOutputStream oout= new ObjectOutputStream(s.getOutputStream());
				
				String[] trozos=pet.split(" ");
				List<Fichero> li = gb.todosFicherosUsuario(trozos[3]);
	 			
				for(Fichero f: li) {
					oout.writeObject(f);
				}
				oout.close();
				s1.close();				
				ss.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}