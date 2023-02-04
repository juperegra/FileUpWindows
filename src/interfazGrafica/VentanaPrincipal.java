package interfazGrafica;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import model.Fichero;
import model.Usuario;



public class VentanaPrincipal {
	
	private Usuario u;
	private List<Fichero> listFiles= new ArrayList<>();
	
	public VentanaPrincipal() {
		
	}
	
	public VentanaPrincipal(Usuario u) {
		this.u=u;
	}
	
	public void inicio() {
		Display d= new Display();
		
		Shell s = new Shell(d);
		
		s.setBounds(100, 100, 700, 500);
		s.setLayout(new FillLayout());
		TabFolder t = new TabFolder(s,SWT.TOP);
		t.setLayout(new FillLayout());
		
		VentanaPrincipal pri= this;

		TabItem t1=new TabItem(t,SWT.FILL);
		t1.setText("Subir archivo");
		t1.setControl(new GrupoSubir(t,SWT.NONE, s, this));
		
		TabItem t2=new TabItem(t,SWT.FILL);
		t2.setText("Mis archivos");
		GrupoArchivos g=new GrupoArchivos(t,SWT.NONE);
		t2.setControl(g);
		t.setLayout(new FillLayout());
		Group gr=g.getGroup();
	
		TabItem t3=new TabItem(t,SWT.FILL);
		t3.setText("Archivos publicos");
		GrupoPublicos gp= new GrupoPublicos(t,SWT.NONE);
		t3.setControl(gp);
		Group grp=gp.getGroup();
		
		t.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		        if(t.getSelection()[0].getText().equals("Mis archivos")) {
		        	listFiles.clear();
		        	listFiles=GrupoArchivos.fichsUsr(pri);
		        	pri.crearBotonesFich(gr,s);
		        }
		        if(t.getSelection()[0].getText().equals("Archivos publicos")) {
		        	listFiles.clear();
		        	listFiles=GrupoPublicos.fichsPubl(pri);
		        	pri.crearBotonesFich(grp,s);
		        }
		      }
		    });
		
		t.pack();
		
		t.pack();
		s.setText("FileUP!");
		s.open();
		
		while(!s.isDisposed()) {
			if(d.readAndDispatch()) {
				d.sleep();
			}
		}
		d.dispose();
		
	}
	
	public Usuario getUsuario() {
		return this.u;
	}
	
	public void crearBotonesFich(Group g, Shell s) {
		Control[] clist=g.getTabList();
		for(Control cr: clist) {
			//System.out.println(cr.toString());
			if(cr.toString().startsWith("Button ")) {
				cr.dispose();
				//System.out.println(cr.toString()+" ha sido disposeado");
			}
			
		}
		//System.out.println("numcontoles= "+clist.length);
		
		//System.out.println("crearbotonesnumbotones"+listFiles.size());
		for (Fichero f: listFiles){
			
			Button b= new Button(g,SWT.CENTER);
			System.out.println(f.getNombre());
			b.setText(f.getNombre());
			b.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e){
						descargar(f, s);
					}
				});
			g.layout();
			
		}		
	}
	
	public void descargar(Fichero f,Shell sh) {
		try(Socket s= new Socket("localhost",40400);
			DataOutputStream out= new DataOutputStream(s.getOutputStream());
			DataInputStream in= new DataInputStream(s.getInputStream());){
			//System.out.println("esto ejecuta1");
			String pet="GET: Fichero ";
			
			pet+=f.getId()+"\n";
			//System.out.println("esto ejecuta2");
			out.write(pet.getBytes());
			
			FileDialog dlg = new FileDialog(sh, SWT.SAVE);
			dlg.setFileName(f.getNombre());
			//System.out.println("esto ejecuta3");
		    String ruta = dlg.open();
		    if (ruta != null) {
		    	//System.out.println("aqui entra");
		    	System.out.println(ruta);
		      FileOutputStream fout= new FileOutputStream(ruta);
				int byteLeido=in.read();
				while(byteLeido!=-1) {
					//System.out.println(byteLeido);
					fout.write(byteLeido);
					byteLeido=in.read();
				}	
				//System.out.println("esto ejecuta4");
				fout.close();
		    }
		    
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}