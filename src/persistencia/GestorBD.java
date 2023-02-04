package persistencia;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import model.Fichero;
import model.Usuario;

public class GestorBD{
	private static final String URL = getPropiedad("url");
	private static final String USR = getPropiedad("user");
	private static final String PWD = getPropiedad("password");
			
	public static String getPropiedad(String clave) {
		String valor = null;
		try {
		    Properties props = new Properties();
		    InputStream prIS = GestorBD.class.getResourceAsStream("/conexion.properties");
		    props.load(prIS);
		    valor = props.getProperty(clave);
		}catch (IOException ex) { 
			ex.printStackTrace();
		}
		
		return valor;
	}
	
	public Usuario buscarUsuario(String arg0) throws Exception {
		Usuario u=null;
	    Connection con = null;
	    try {
	      con = DriverManager.getConnection(URL,USR,PWD);
	      con.setAutoCommit(false);
	      String sql = "SELECT * FROM Usuario WHERE ID= '" + arg0 + "'";
	      PreparedStatement stm = con.prepareStatement(sql);

	      ResultSet res = stm.executeQuery();
	      
	      if(res.next()) {
	    	  
	    	  u= new Usuario(res.getString(1),res.getString(2),res.getString(3),res.getString(4));
	    	  
	      }

	      stm.close();
	      con.commit();	
	    } catch (SQLException e) {
	      e.printStackTrace();
	      try {
			if (con != null) con.rollback();
	      } catch (SQLException e2) {
			e2.printStackTrace();
			
			throw new Exception(e2);
	      }
	      throw new Exception(e);
	    } finally {
	      if (con != null) {
	        try {
	        con.close();
	        } catch (SQLException ex) {
	          ex.printStackTrace();
	        throw new Exception(ex);
	        }
	      }
	    }
	    return u;
	}
	
	public Fichero buscaFichero(String arg0) throws Exception{
		
		Fichero f=null;
	    Connection con = null;
	    try {
	      con = DriverManager.getConnection(URL,USR,PWD);
	      
	      con.setAutoCommit(false);
	      
	      String sql = "SELECT * FROM Fichero WHERE ID_Fichero= '" + arg0 + "'";
	      
	      PreparedStatement stm = con.prepareStatement(sql);

	      ResultSet res = stm.executeQuery();
	      
	      if(res.next()) {
	    	  
	    	  
	    	  f= new Fichero(res.getString(1),res.getString(2),buscarUsuario(res.getString(3)),buscarUsuario(res.getString(4)),res.getString(5),res.getString(6));
	    	  
	      }

	      stm.close();
	      con.commit();	
	    } catch (SQLException e) {
	      e.printStackTrace();
	      try {
			if (con != null) con.rollback();
	      } catch (SQLException e2) {
			e2.printStackTrace();
			
			throw new Exception(e);
	      }
	      throw new Exception(e);
	    } finally {
	      if (con != null) {
	        try {
	        con.close();
	        } catch (SQLException ex) {
	          ex.printStackTrace();
	        throw new Exception(ex);
	        }
	      }
	    }
	    return f;		
	}
	
	public void agnadirFichero(Fichero f) throws Exception{
		
		Connection con = null;
		try {
			con = DriverManager.getConnection(URL, USR, PWD);

			con.setAutoCommit(false);

			String sql = "INSERT INTO Fichero VALUES(?,?,?,?,?,?)";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, f.getId());
			
			ps.setString(2, f.getNombre());
			
			ps.setString(3, f.getEmisor().getId());
			
			ps.setString(4, f.getReceptor().getId());
			
			ps.setString(5, f.getRuta());
			
			ps.setString(6, f.getCodificacion());

			ps.executeUpdate();
			

			ps.close();
			
			
			con.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			throw new Exception(ex);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
					throw new Exception(ex);
				}
			}
		}

		
	}
	
	public boolean agnadirPersona(Usuario u) throws Exception{
		Usuario u2=buscarUsuario(u.getId());
		if(u2!=null) {
			return false;
		}else {
			Connection con = null;
			try {
				con = DriverManager.getConnection(URL, USR, PWD);

				con.setAutoCommit(false);

				String sql = "INSERT INTO Usuario VALUES(?,?,?,?)";

				PreparedStatement ps = con.prepareStatement(sql);

				ps.setString(1, u.getId());
				
				ps.setString(2, u.getNombre());
				
				ps.setString(3, u.getApellidos());
				
				ps.setString(4, u.getContrasegna());


				ps.executeUpdate();
				

				ps.close();
				
				
				con.commit();
			} catch (SQLException ex) {
				ex.printStackTrace();
				try {
					if (con != null)
						con.rollback();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				throw new Exception(ex);
			} finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
						throw new Exception(ex);
					}
				}
			}
			return true;
		}
	}
	
	public boolean eliminarFichero(String id) throws Exception{
		boolean deleted=false;
		Connection con = null;
		try {
			con = DriverManager.getConnection(URL, USR, PWD);

			con.setAutoCommit(false);

			String sql = "DELETE FROM Fichero WHERE ID_Fichero=?";

			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setString(1, id);
			
			if(ps.executeUpdate()!=0){
				deleted=true;
			}

			ps.close();
			
			
			con.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			throw new Exception(ex);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
					throw new Exception(ex);
				}
			}
		}
		return deleted;
	}
	
	public boolean eliminarPersona(String id) throws Exception{
		
		boolean deleted=false;
		Connection con = null;
		try {
			con = DriverManager.getConnection(URL, USR, PWD);

			con.setAutoCommit(false);

			String sql = "DELETE FROM Usuario WHERE ID=?";

			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setString(1, id);
			
			if(ps.executeUpdate()!=0){
				deleted=true;
			}

			ps.close();
			
			
			con.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			throw new Exception(ex);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
					throw new Exception(ex);
				}
			}
		}
		return deleted;
		
	}
	
	public List<Fichero> todosFicherosUsuario(String arg0) throws Exception{
		
		List<Fichero> lf=new ArrayList<>();
		Connection con = null;
		
		try {
			con = DriverManager.getConnection(URL, USR, PWD);

			con.setAutoCommit(false);
			
			System.out.println(arg0);
			
			String sql = "SELECT * FROM Fichero WHERE ID_Receptor= '" + arg0 + "'";

			PreparedStatement ps = con.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				lf.add(buscaFichero(rs.getString(1), con));
			}
			
			ps.close();
			
			
			con.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			throw new Exception(ex);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
					throw new Exception(ex);
				}
			}
		}
		return lf;
	}
	
	public List<Usuario> todosUsuarios() throws Exception{// esto ns si deberia estar, pero bueno, yo lo pongo
		
		return null;
		
	}
	
	private Fichero buscaFichero(String arg0, Connection con) throws Exception{
		
		Fichero deleted=null;
	    
	    try {
	      con = DriverManager.getConnection(URL,USR,PWD);
	      
	      con.setAutoCommit(false);
	      
	      String sql = "SELECT * FROM Fichero WHERE ID_Fichero= '" + arg0 + "'";
	      
	      PreparedStatement stm = con.prepareStatement(sql);

	      ResultSet res = stm.executeQuery();
	      
	      if(res.next()) {
	    	  
	    	  
	    	  deleted= new Fichero(res.getString(1),res.getString(2),buscarUsuario(res.getString(3)),buscarUsuario(res.getString(4)),res.getString(5),res.getString(6));
	    	  
	      }

	      stm.close();
	      con.commit();	
	    } catch (SQLException e) {
	      e.printStackTrace();
	      try {
			if (con != null) con.rollback();
	      } catch (SQLException e2) {
			e2.printStackTrace();
			
			throw new Exception(e);
	      }
	      throw new Exception(e);
	    } 
	    return deleted;		
	}
}
