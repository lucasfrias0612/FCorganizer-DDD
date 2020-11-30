package ar.com.unpaz.organizerddd.infrastructure.db.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ar.com.unpaz.organizerddd.domain.entitys.User;
import ar.com.unpaz.organizerddd.domain.repositorycontracts.IRepository;
import ar.com.unpaz.organizerddd.domain.specifications.Specification;
import ar.com.unpaz.organizerddd.infrastructure.db.DbConection;

public class PostgreslqUserRepository implements IRepository<User> {

	@Override
	public List<User> get() {
		// TODO Auto-generated method stub
		String userList = "SELECT u.dni,u.firstname,u.lastname,u.username,u.password FROM users as u";
		List<User> userlist = new ArrayList<>();
		Connection con = DbConection.getConection();
		try {
			PreparedStatement ps = con.prepareStatement(userList);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User(rs.getInt("dni"), rs.getString("firstname"), rs.getString("lastname"),
						rs.getString("username"), rs.getString("password"));
				userlist.add(user);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return userlist;

	}

	@Override
	public void insert(User user) {
		// TODO Auto-generated method stub
		String insertUser = "INSERT INTO users (dni,firstname,lastname,username,password) values(?,?,?,?,?)";
		Connection con = DbConection.getConection();
		try {
			PreparedStatement ps = con.prepareStatement(insertUser);
			ps.setInt(1, user.getDni());
			ps.setString(2, user.getName());
			ps.setString(3, user.getLastName());
			ps.setString(4, user.getUser());
			ps.setString(5, user.getPass());
			ps.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		String updateUser = "UPDATE users SET firstname=?,lastname=?,username=?,password=? WHERE dni=?";
		Connection con = DbConection.getConection();
		try {
			PreparedStatement ps = con.prepareStatement(updateUser);
			ps.setString(1, user.getName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getUser());
			ps.setString(4, user.getPass());
			ps.setInt(5, user.getDni());
			ps.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void delete(User user) {
		// TODO Auto-generated method stub
		String deleteUser = "DELETE FROM users WHERE dni=?";
		Connection con = DbConection.getConection();
		try (PreparedStatement st = con.prepareStatement(deleteUser)) {
			st.setInt(1, user.getDni());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<User> query(Specification<User> spec) {
		// TODO Auto-generated method stub
		return null;
	}

}
