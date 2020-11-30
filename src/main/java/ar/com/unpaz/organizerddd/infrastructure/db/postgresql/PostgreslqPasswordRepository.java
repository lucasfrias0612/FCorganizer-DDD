package ar.com.unpaz.organizerddd.infrastructure.db.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ar.com.unpaz.organizerddd.domain.entitys.Password;
import ar.com.unpaz.organizerddd.domain.repositorycontracts.IRepository;
import ar.com.unpaz.organizerddd.domain.specifications.Specification;
import ar.com.unpaz.organizerddd.infrastructure.db.DbConection;

public class PostgreslqPasswordRepository implements IRepository<Password> {

	@Override
	public List<Password> get() {
		String pslist = "SELECT p.id,p.site,p.site_user,p.site_password,p.user_dni FROM passwords as p";
		List<Password> password = new ArrayList<>();
		Connection con = DbConection.getConection();
		try {
			PreparedStatement ps = con.prepareStatement(pslist);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Password pass = new Password(rs.getInt("id"),
						                     rs.getString("site"), 
						                     rs.getString("site_user"),
						                     rs.getString("site_password"),
						                     rs.getInt("user_dni"));
				password.add(pass);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return password;
	}

	@Override
	public void insert(Password psw) {
		String insertpass = "INSERT INTO passwords (site,site_user,site_password,user_dni) values(?,?,?,?)";
		Connection con = DbConection.getConection();
		try {
			PreparedStatement ps = con.prepareStatement(insertpass);
			ps.setString(1, psw.getSite());
			ps.setString(2, psw.getUser());
			ps.setString(3, psw.getPass());
			ps.setInt(4, psw.getDni());
			ps.executeUpdate();
		
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void delete(Password pswId) {
		String deletepass = "DELETE FROM passwords WHERE id=?";
		Connection con = DbConection.getConection();
		try (PreparedStatement st = con.prepareStatement(deletepass)) {
			st.setInt(1,pswId.getPassId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Password> query(Specification<Password> spec) {
		 List<Password> current = get();
		 List<Password> result = new ArrayList<Password>(); 
		    for (Password e : current) {
		      if (spec.specified(e)) {
		        result.add(e);
		      }
		    }
		 return result;
	}

	@Override
	public void update(Password entity) {
		// TODO Auto-generated method stub
		return;
	}

}
