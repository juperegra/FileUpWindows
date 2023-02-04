package interfazGrafica;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.rmi.activation.Activator;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;

import model.Fichero;
import model.Usuario;

public class GrupoArchivos extends Composite{
	private Group g;
	public GrupoArchivos(Composite c, int style) {
		super(c,style);
			
			
			Group g= new Group(this, SWT.FILL);
			this.g=g;
			this.setLayout(new FillLayout());
			
			GridLayout gl= new GridLayout();
			gl.numColumns=5;
			g.setLayout(gl);
			
			GridData gdB= new GridData();
			gdB.horizontalSpan=5;
			gdB.horizontalAlignment = GridData.FILL;
			
			Label l= new Label(g, SWT.CENTER);
			l.setText("Archivos compartidos conmigo");
			l.setLayoutData(gdB);
			g.pack();		

	}
	public void prueba() {
		System.out.println("gola");
		Button b= new Button(g,SWT.NONE);
		b.setText("seguimos probando");
		g.layout();
	}

	
	public static List<Fichero> fichsUsr(VentanaPrincipal vp ) {
			List<Fichero> fichs=new ArrayList<>();
		try (Socket s = new Socket("localhost",40400);
			DataInputStream in= new DataInputStream(s.getInputStream());
			DataOutputStream out= new DataOutputStream(s.getOutputStream());){
			
			String pet="GET ALL: ";
			//System.out.println("se esta enviando esto");
			
			Usuario u=vp.getUsuario();
			//System.out.println("se esta enviando esto2");
			
			pet +="Ficheros "+u.getId()+"\n";
			//System.out.println("se esta enviando esto3");
			
			out.write(pet.getBytes());
			//System.out.println("se esta enviando esto4");
			
			String conf=in.readLine();
			
			System.out.println(conf);
			
			if(conf.equals("ya")) {
				Socket s1= new Socket("localhost",40401);
				ObjectInputStream oin= new ObjectInputStream(s.getInputStream());
				System.out.println("001");
				
				Fichero fich= (Fichero) oin.readObject();
				System.out.println("002");
				while(fich!=null) {
					fichs.add(fich);
					System.out.println(fich.toString());
					fich= (Fichero) oin.readObject();
				}
				oin.close();
				s1.close();
			}
			
		}catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace(); para evitar problemas, este catch debe estar vacio
		}
		System.out.println("numFicheros:"+fichs.size());
		
		return fichs;
	}
	public Group getGroup() {
		return this.g;
	}
}