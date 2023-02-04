package interfazGrafica;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class VentanaRegistrar {
	
	public void startReg() {
		Display d= new Display();
		
		Shell s = new Shell(d);
		GridLayout gl= new GridLayout();
		
		s.setBounds(750, 400, 450, 300);
		
		s.setFullScreen(false);
		
		gl.numColumns=3;
		
		s.setLayout(gl);
		
		Label l= new Label(s, SWT.CENTER);
		
		l.setText("Registro");
		
		GridData gd= new GridData();
		gd.horizontalSpan=3;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		
		GridData gd1= new GridData();
		gd1.horizontalSpan=2;
		gd1.horizontalAlignment = GridData.FILL;
		gd1.grabExcessHorizontalSpace = true;
		
		l.setLayoutData(gd);
		
		Label l1 = new Label(s, SWT.LEFT);
		l1.setText("Nombre:");
		Text text1 = new Text(s,SWT.SINGLE |SWT.BORDER);
		text1.setLayoutData(gd1);
		
		Label l2 = new Label(s, SWT.LEFT);
		l2.setText("Apellidos(opcional):");
		Text text2 = new Text(s,SWT.SINGLE |SWT.BORDER);
		text2.setLayoutData(gd1);
		
		Label l3= new Label(s, SWT.LEFT);
		l3.setText("Usuario:");
		Text text3 = new Text(s,SWT.SINGLE |SWT.BORDER);
		text3.setLayoutData(gd1);
		
		Label l4 = new Label(s, SWT.LEFT);
		l4.setText("Contrasegna:");
		Text text4 = new Text(s,SWT.SINGLE |SWT.BORDER| SWT.PASSWORD);

		text4.setLayoutData(gd1);
		
		GridData gdB= new GridData();
		gdB.horizontalSpan=3;
		gdB.horizontalAlignment = GridData.FILL;
		
		Button bis = new Button(s, SWT.CENTER);
		bis.setText("Registrate!");
		bis.setLayoutData(gdB);
		
		Label lerr = new Label(s, SWT.LEFT);
		lerr.setText(" ");
		lerr.setLayoutData(gd);
		
		bis.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){	
					if(!text1.getText().equals("") && !text3.getText().equals("")&& !text4.getText().equals("")){
						//System.out.println("entra");
						registro(text1.getText(), text2.getText(),text3.getText(),text4.getText(),lerr, d, s);
					}
				}
			});
		
		s.open();
		
		while(!s.isDisposed()) {
			if(d.readAndDispatch()) {
				d.sleep();
			}
		}
		d.dispose();
	}
	public void registro(String nombre, String apellidos, String usuario, String contrasegna, Label lerr, Display de, Shell sh) {
		try {
			Socket s= new Socket("localhost", 40400);
			DataOutputStream out= new DataOutputStream(s.getOutputStream());
			DataInputStream in= new DataInputStream(s.getInputStream());
			
			String pet="INSERT: Usuario ";
			pet+= usuario+" "+contrasegna+" "+nombre+" "+apellidos+"\n";
			
			out.write(pet.getBytes());
			
			String respuesta=in.readLine();
			
			if(respuesta.startsWith("OK ")) {
				sh.close();
				//System.out.println("esto");
			}else if(respuesta.startsWith("ERROR: ")){
				String[] trozos=respuesta.split(":");
				lerr.setText(trozos[1]);
				final Color color = new Color(de, 255, 0, 0);
				lerr.setForeground(color);
			}
			
			System.out.println(pet);
			s.close();
			in.close();
			out.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}