package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBConnector {

	private static DBConnector single_instance = null;
	private static Connection connection;
	private static String password;
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/book_store";
	private static final String DATABASE_USERNAME = "root";
	private static final String DATABASE_PASSWORD = "root";

	public static DBConnector getInstance() {
		if (single_instance == null) {
			// PassValues.setUserName("emanrafik");
			single_instance = new DBConnector();
		}
		if (connection == null) {
			try {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				printSQLException(e);
			}
		}
		return single_instance;
	}

	/// CHECK IF USER NAME IS ALREADY EXIST OR NOT BEFORE INSERTION.
	public boolean existusername(String username) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "select username from user where username=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();
			/// CHANGE CONDITION TO COUNT IF NOT 0 THEN TRUE ELSE THEN FALSE.
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return false;
	}

	/// INSERT DATA OF THE NEW USER
	public void insertRecord(String userName, String password, String firstName, String lastName, String email,
			String phone, String shippingadd) throws SQLException {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "insert into user values(?,?,?,?,?,?,?,'user')";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, firstName);
			preparedStatement.setString(4, lastName);
			preparedStatement.setString(5, email);
			preparedStatement.setString(6, phone);
			preparedStatement.setString(7, shippingadd);
			

			preparedStatement.executeUpdate();

			PassValues.setUserName(userName);
			PassValues.setFirstName(firstName);
			PassValues.setSecondName(lastName);
			PassValues.setPhone(phone);
			PassValues.setAddress(shippingadd);
			PassValues.setEmail(email);

			DBConnector.password = password;
		} catch (SQLException e) {
			// print SQL exception information
			printSQLException(e);
		}
	}

	/// CHECK IF USERNAME AND PASSWORD FOR THE USER ARE VALID OR NOT.
	public boolean validate(String username, String password) throws SQLException {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "select * from user where username=? and password=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				PassValues.setUserName(resultSet.getString("username"));
				PassValues.setAddress(resultSet.getString("ship_address"));
				PassValues.setFirstName(resultSet.getString("first_name"));
				PassValues.setSecondName(resultSet.getString("last_name"));
				PassValues.setPhone(resultSet.getString("phone"));
				PassValues.setEmail(resultSet.getString("email"));
				PassValues.setPrivilage(resultSet.getString("privilege"));
				DBConnector.password = password;
				return true;
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return false;
	}

	public void editPassword(String password) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "update user set password = ? where username=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, password);
			preparedStatement.setString(2, PassValues.getUserName());
			preparedStatement.executeUpdate();
			DBConnector.password = password;
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public void editData(String firstname, String lastname, String email, String phone, String shippingadd) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "update user set first_name=?, last_name=?, phone=?, ship_address=?, "
					+ "email=? where username=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, firstname);
			preparedStatement.setString(2, lastname);
			preparedStatement.setString(3, phone);
			preparedStatement.setString(4, shippingadd);
			preparedStatement.setString(5, email);
			preparedStatement.setString(6, PassValues.getUserName());
			preparedStatement.executeUpdate();
			PassValues.setFirstName(firstname);
			PassValues.setSecondName(lastname);
			PassValues.setPhone(phone);
			PassValues.setAddress(shippingadd);
			PassValues.setEmail(email);

		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public boolean publisherExists(String name) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "select * from publisher where publisher_name=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			ResultSet resultSet = preparedStatement.executeQuery();
			/// CHANGE CONDITION TO COUNT IF NOT 0 THEN TRUE ELSE THEN FALSE.
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return false;
	}

	
	public void addPublisher(String name, ArrayList<String> phones, ArrayList<String> addresses) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			connection.setAutoCommit(false);
			String query = "insert into publisher values(?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, addresses.get(0));
			preparedStatement.setString(3, phones.get(0));
			preparedStatement.executeUpdate();
//*************************************************************BAHAA****************************************************************
//			String publisherAddressesQuery = "insert into Publisher_Address values(?,?)";
//			for (String a : addresses) {
//				preparedStatement = connection.prepareStatement(publisherAddressesQuery);
//				preparedStatement.setString(1, name);
//				preparedStatement.setString(2, a);
//				preparedStatement.executeUpdate();
//			}
//			String publisherPhonesQuery = "insert into Publisher_Address values(?,?)";
//			for (String ph : phones) {
//				preparedStatement = connection.prepareStatement(publisherPhonesQuery);
//				preparedStatement.setString(1, name);
//				preparedStatement.setString(2, ph);
//				preparedStatement.executeUpdate();
//			}
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				if (connection != null) {
					connection.rollback();
					connection.setAutoCommit(true);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			printSQLException(e);
		}
	}

	
	public void addBook(String isbn, String title, String pname, String pyear, String category, String price,
			String threashold, String availableCopies, String orderQuantity, List<String> authors) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			connection.setAutoCommit(false);
			String query = "insert into book values(?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, isbn);
			preparedStatement.setString(2, title);
			preparedStatement.setString(3, pname);
//			preparedStatement.setString(4, pyear);   //BAHAA **************************************************
			preparedStatement.setDouble(4, Double.valueOf(price));
			preparedStatement.setString(5, category);
			preparedStatement.setInt(6, Integer.valueOf(availableCopies));
			preparedStatement.setInt(7, Integer.valueOf(threashold));
			preparedStatement.setInt(8, Integer.valueOf(orderQuantity));
			preparedStatement.executeUpdate();

			String authorsQuery = "insert into author values(?,?)";

			for (String a : authors) {
				preparedStatement = connection.prepareStatement(authorsQuery);
				preparedStatement.setString(1, a.trim());
				preparedStatement.setString(2, isbn);
				
				preparedStatement.executeUpdate();
			}
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				if (connection != null) {
					connection.rollback();
					connection.setAutoCommit(true);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			printSQLException(e);
		}
	}

	public boolean bookexist(String isbn) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "select * from book where ISBN=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, isbn);
			ResultSet result = preparedStatement.executeQuery();
			if (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
		return false;
	}
	
	public void getbookdata(String isbn) {
		String selectFromBookQuery = "select * from book where ISBN=?";
		String selectFromBookAuthorsQuery = "select * from author where ISBN=?";
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			/** get book data from book relation **/
			PreparedStatement preparedStatement = connection.prepareStatement(selectFromBookQuery);
			preparedStatement.setString(1, isbn);
			ResultSet result = preparedStatement.executeQuery();
			List<String> dataToModify = new ArrayList<String>();
			if (result.next()) {
				dataToModify.add(result.getString("title"));
				dataToModify.add(result.getString("publisher_name"));
//				Date date = result.getDate("publication_year");   //BAHAA ****************************************************************************
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyy");	//BAHAA
//				dataToModify.add(formatter.format(date));					//BAHAA
				dataToModify.add(String.valueOf(result.getDouble("price")));
				dataToModify.add(result.getString("category"));
				dataToModify.add(String.valueOf(result.getInt("copies")));
				dataToModify.add(String.valueOf(result.getInt("threshold")));
				dataToModify.add(String.valueOf(result.getInt("order_quantity")));
			}
			/** get book authors from book authors relation **/
			preparedStatement = connection.prepareStatement(selectFromBookAuthorsQuery);
			preparedStatement.setString(1, isbn);
			result = preparedStatement.executeQuery();
			ArrayList<String> authors = new ArrayList<>();
			while (result.next()) {
				authors.add(result.getString("author"));
			}
			String listString = "";
			for (int i = 0; i < authors.size(); i++) {
				listString += authors.get(i);
				if (i < authors.size() - 1) {
					listString += ",";
				}
			}
			dataToModify.add(listString);
			PassValues.setDataToModify(dataToModify);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
	}
	
	public void modifybook(String isbn, String title, String pname, String pyear, String category, String price,
			String threashold, String orderQuantity, List<String> authors) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			connection.setAutoCommit(false);
			String query = "update book set title=?, publisher_name=?, price=?,"
					+ "category=?, threshold=?, order_quantity=? where ISBN=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, title);
			preparedStatement.setString(2, pname);
//			preparedStatement.setString(3, pyear);    //BAHAA  *********************************************************************
			preparedStatement.setDouble(3, Double.valueOf(price));
			preparedStatement.setString(4, category);
			preparedStatement.setInt(5, Integer.valueOf(threashold));
			preparedStatement.setInt(6, Integer.valueOf(orderQuantity));
			preparedStatement.setString(7, isbn);
			preparedStatement.executeUpdate();

			String deleteAuthorsQuery = "delete from author where ISBN=?";
			preparedStatement = connection.prepareStatement(deleteAuthorsQuery);
			preparedStatement.setString(1, isbn);
			preparedStatement.executeUpdate();

			String authorsQuery = "insert into author values(?,?)";
			for (String a : authors) {
				preparedStatement = connection.prepareStatement(authorsQuery);
				preparedStatement.setString(1, a.trim());
				preparedStatement.setString(2, isbn);
				
				preparedStatement.executeUpdate();
			}
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				if (connection != null) {
					connection.rollback();
					connection.setAutoCommit(true);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			printSQLException(e);
		}
	}

	public void addorder(String isbn, String quantity) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "call add_order(?,?)";
			CallableStatement statement = connection.prepareCall(query);
			statement.setString(1, isbn);
			statement.setInt(2, Integer.valueOf(quantity));
			statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
	}

	public void getAllOrders() {
		PassValues.clearAllOrder();
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "select * from book_store.order";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				PassValues.setOrderID(result.getInt("id"));
				PassValues.setOrderISBN(result.getString("ISBN"));
				PassValues.setOrderDate(result.getTimestamp("date")); // YYYY-MM-DD HH:MM:SS
				PassValues.setOrderQuantity(result.getInt("num_of_books"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
	}

	public void confirmOrder(int id) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "delete from book_store.order where id=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
	}

	
	public String getuserdata(String username) {
		String res = "";
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "select * from user where username=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet result = preparedStatement.executeQuery();
			List<String> userdata = new ArrayList<String>();
			if (result.next()) {
				String firstName = result.getString("first_name");
				res += "First Name: " + firstName + "\n";
				userdata.add(firstName);
				String secondName = result.getString("last_name");
				res += "last Name: " + secondName + "\n";
				userdata.add(secondName);
				String phone = result.getString("phone");
				res += "Phone: " + phone + "\n";
				userdata.add(phone);
				String email = result.getString("email");
				res += "E-Mail: " + email + "\n";
				userdata.add(email);
				String address = result.getString("ship_address");
				res += "Address: " + address + "\n";
				userdata.add(address);
				String privelege = result.getString("privilege");
				res += "Privelege: " + privelege + "\n";
				userdata.add(privelege);
				PassValues.setUserdata(userdata);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
		return res;
	}

	
	public void promote(String username) {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			String query = "update user set privilege='manager' where username=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
	}
	
	public void bookSearch(String title, String publisher, String category, int publicationYear, double sellingPrice,
			String auther) {
		boolean first = true;
		String query;
		// search by author only
		if (!auther.equals("") && title.equals("") && publisher.equals("") && category.equals("")
				&& publicationYear == 0 && sellingPrice == 0) {
			query = "select ISBN from author where author like '%" + auther + "%'";
			try {
				if (connection == null) {
					connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
				}
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet isbnResult = preparedStatement.executeQuery();
				while (isbnResult.next()) {
					String ISBN = isbnResult.getString("ISBN");
					// get book data by ISBN
					String dataQuery = "select * from book where ISBN=?";
					preparedStatement = connection.prepareStatement(dataQuery);
					preparedStatement.setString(1, ISBN);
					ResultSet result = preparedStatement.executeQuery();
					while (result.next()) {
						PassValues.setTitle(result.getString("title"));
						PassValues.setPublisher(result.getString("publisher_name"));
//						Date date = result.getDate("publication_year");      //************************BAHAAAAAAAA************
//						SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//						PassValues.setPublicationYear(Integer.valueOf(formatter.format(date)));   //***********************************BAAAAAAAAHHHHHHHHAAAAAAAAAAAAAA********
						PassValues.setCategory(result.getString("category"));
						PassValues.setSellingPrice(result.getDouble("price"));
						PassValues.setAvailableCopies(result.getInt("copies"));
						PassValues.setISBN(result.getString("ISBN"));
					}
					// get book authors by ISBN
					String authorsQuery = "select * from author where ISBN=?";
					preparedStatement = connection.prepareStatement(authorsQuery);
					preparedStatement.setString(1, ISBN);
					ResultSet authors = preparedStatement.executeQuery();
					ArrayList<String> Authers = new ArrayList<String>();
					while (authors.next()) {
						Authers.add(authors.getString("author"));
					}
					PassValues.setAuthers(Authers);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				printSQLException(e);
			}
		} else {
			if (!auther.equals("")) {
				query = "select * from book, author where author.ISBN and ";          //it could be wrong from before
			} else {
				query = "select * from book where ";
			}
			if (!title.equals("")) {
				query += "title like '%" + title + "%'";
				first = false;
			}
			if (!publisher.equals("")) {
				if (!first)
					query += " and ";
				query += "publisher_name like '%" + publisher + "%'";
				first = false;
			}
			if (!category.equals("")) {
				if (!first)
					query += " and ";
				query += "category='" + category + "'";
				first = false;
			}
			String publicationYearString = String.valueOf(publicationYear);
			if (publicationYear != 0 && publicationYearString.length() == 4) {
//				if (!first)    //BAHAA 
//					query += " and ";                                              //*************************************************BBAHAAAAAAAAAAAA
//				query += "publication_year='" + publicationYearString + "'";    //******bahaa*****
//				first = false;
			}
			if (sellingPrice != 0) {
				if (!first)
					query += " and ";
				query += "price=" + String.valueOf(sellingPrice);
				first = false;
			}
			if (!auther.equals("")) {
				if (!first)
					query += " and ";
				query += "author like '%" + auther + "%'";
			}
			try {
				if (connection == null) {
					connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
				}
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet result = preparedStatement.executeQuery();
				while (result.next()) {
					String ISBN = result.getString("ISBN");
					// get book authors by ISBN
					String authorsQuery = "select * from author where ISBN=?";
					preparedStatement = connection.prepareStatement(authorsQuery);
					preparedStatement.setString(1, ISBN);
					ResultSet authors = preparedStatement.executeQuery();
					ArrayList<String> Authers = new ArrayList<String>();
					while (authors.next()) {
						Authers.add(authors.getString("author"));
					}
					PassValues.setAuthers(Authers);
					PassValues.setTitle(result.getString("title"));
					PassValues.setPublisher(result.getString("publisher_name"));
//					Date date = result.getDate("publication_year");         //BAHAAAAAAAAAA*************************************************
//					SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//					PassValues.setPublicationYear(Integer.valueOf(formatter.format(date))); //BAHAA********************************
					PassValues.setCategory(result.getString("category"));
					PassValues.setSellingPrice(result.getDouble("price"));
					PassValues.setAvailableCopies(result.getInt("copies"));
					PassValues.setISBN(result.getString("ISBN"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				printSQLException(e);
			}
		}

	}
	//BAHAA all prev DONE
	public void checkOut(List<String> ISBN) {
		String updateBookQuery = "update book set copies = copies - 1 where ISBN=?";
		String addToSalesProcedureCall = "call add_to_cart(?,?)";
		try {
			if (connection == null) {
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			}
			connection.setAutoCommit(false);
			for (String s : ISBN) {
				PreparedStatement preparedStatement = connection.prepareStatement(updateBookQuery);
				preparedStatement.setString(1, s);
				preparedStatement.executeUpdate();
				CallableStatement callableStatement = connection.prepareCall(addToSalesProcedureCall);
				callableStatement.setString(1, PassValues.getUserName());
				callableStatement.setString(2, s);
				callableStatement.executeQuery();
			}
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
			if (connection != null) {
				try {
					connection.rollback();
					connection.setAutoCommit(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					printSQLException(e1);
				}
			}
		}
	}

	public static void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

	public String getpass() {
		return password;
	}

	public String getcurrentusername() {
		return PassValues.getUserName();
	}
}