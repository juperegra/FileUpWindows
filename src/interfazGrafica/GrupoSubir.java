package interfazGrafica;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import model.Fichero;
import model.Usuario;

public class GrupoSubir extends Composite{

	private String ruta;
	private VentanaPrincipal vp;
	
	public GrupoSubir(Composite c, int style, Shell s, VentanaPrincipal vp) {
		super(c,style);
		this.vp=vp;
		Group g= new Group(this, SWT.FILL);		
		GridLayout gl= new GridLayout();
		gl.numColumns=5;
		g.setLayout(gl);
		GridData gd= new GridData();
		gd.horizontalSpan=5;
		
		GridData gd2= new GridData();
		gd2.horizontalSpan=2;
		
		GridData gd3= new GridData();
		gd3.horizontalSpan=3;
		
		Label l = new Label(g,SWT.NONE);
		l.setText("Seleccionar el archivo");
		l.setLayoutData(gd);
		
		Text text = new Text(g,SWT.SINGLE |SWT.BORDER | SWT.READ_ONLY);
		text.setLayoutData(gd3);
		
		Button b= new Button(g,SWT.NONE);
		b.setText("Select");
		b.setLayoutData(gd2);
		
		Label l1 = new Label(g,SWT.NONE);
		l1.setText("Introduce el id de usuario del receptor del archivo");
		l1.setLayoutData(gd);
		
		Text text1 = new Text(g,SWT.SINGLE |SWT.BORDER);
		text1.setLayoutData(gd3);
		
		Button bSub= new Button(g,SWT.NONE);
		bSub.setText("subir");
		bSub.setLayoutData(gd2);
		
		Label l2 = new Label(g,SWT.NONE);
		l2.setText("(Para subir el archivo de forma p�blica, deja la casilla vacia)");
		l2.setLayoutData(gd);
		
		
		
		b.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){

				FileDialog dlg = new FileDialog(s, SWT.MULTI);
				System.out.println("esto ejecuta3");
			    ruta = dlg.open();
			    System.out.println(ruta);
			    text.setText(ruta);
				}
			});
		
		bSub.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){

				try(Socket s= new Socket("localhost", 40400);
					DataOutputStream out= new DataOutputStream(s.getOutputStream());
					DataInputStream in= new DataInputStream(s.getInputStream());){
					String pet="INSERT: Fichero "+"\n";
					out.write(pet.getBytes());
					//TODO
					// leer confirmacion
					String resp=in.readLine();
					if(resp.equals("OK")) {
						Socket s1= new Socket("localhost", 40401);
						ObjectOutputStream oout= new ObjectOutputStream(s1.getOutputStream());
						String [] trozosRuta=ruta.split("\\\\");
						Fichero f= new Fichero("",trozosRuta[(trozosRuta.length-1)],vp.getUsuario(),new Usuario(text1.getText(),"","",""),"","");
						//abrir otro socket con objectOutputStrean
						oout.writeObject(f);
						//enviar el fichero serializado
						oout.close();
						s1.close();
						
						//cerrar el segundo socket
						File fil= new File(ruta);
						FileInputStream fin= new FileInputStream(fil);
						int byteLeido=fin.read();
						while(byteLeido!=-1) {
							System.out.println(byteLeido);
							out.write(byteLeido);
							byteLeido=fin.read();
						}
						//System.out.println("esto termina al subir el fichero");
						out.close();
						fin.close();
						in.close();
						s.close();
						// mandar el fichero completo a traves del dataoutputstream
					}else {
						//mostrar error por pantalla
					}
					
				}catch(IOException ex) {
					ex.printStackTrace();
				}
				
				}
			});
		
		g.pack();
	}

}