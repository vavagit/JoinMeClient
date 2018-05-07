package vava.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientApplication extends Application{
	@Override
	public void start(final Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vava/app/views/Login.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
        stage.setTitle("JoinMe");
        stage.setResizable(false);
        Image image = new Image(getClass().getResourceAsStream("/img/titleIco.jpg"));
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
