package principal;

import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;

import interfazGrafica.VentanaInicioSesion;

public class Principal {

	public static void main(String[] args) {
			try {
				VentanaInicioSesion vis= new VentanaInicioSesion();
				vis.start();
			}catch(SWTError | SWTException e) {
				
			}
			
		
	}

}