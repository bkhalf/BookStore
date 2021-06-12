package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Users extends Pane {

	private Pane root;

	public Users() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Users.fxml"));
		loader.setController(this);
		try {
			root = loader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public void show(Manager mananger) {
		mananger.mangpane.getChildren().add(root);
	}

	@FXML
	private TextArea userdata;

	@FXML
	private TextField username;

	@FXML
	private Text errormsg;

	@FXML
	void getdata(ActionEvent event) {
		userdata.clear();
		DBConnector db = DBConnector.getInstance();
		if (username.getText().isEmpty()) {
			errormsg.setVisible(true);
			errormsg.setText("Please Enter The UserName");
			errormsg.setVisible(true);
		} else if (db.existusername(username.getText())) {
			errormsg.setVisible(false);
			userdata.setText(db.getuserdata(username.getText()));
		} else {
			errormsg.setVisible(true);
			errormsg.setText("There is No User With This UserName");
		}
	}

	@FXML
	void promot(ActionEvent event) {
		if (username.getText().isEmpty()) {
			errormsg.setText("Please Enter The UserName");
			errormsg.setVisible(true);
		} else {
			errormsg.setVisible(false);
			DBConnector db = DBConnector.getInstance();
			db.promote(username.getText());
		}
	}

}
