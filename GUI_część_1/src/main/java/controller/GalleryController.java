package controller;


import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Gallery;
import model.Photo;
import util.PhotoDownloader;

public class GalleryController {

    private Gallery galleryModel;

    @FXML
    private TextField imageNameField;

    @FXML
    private ImageView imageView;

    @FXML
    private ListView<Photo> imagesListView;

    @FXML
    private TextField searchTextField;

    @FXML
    public void initialize() {
        imagesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Photo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView photoIcon = new ImageView(item.getPhotoData());
                    photoIcon.setPreserveRatio(true);
                    photoIcon.setFitHeight(50);
                    setGraphic(photoIcon);
                }
            }
        });
        imagesListView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
                if(oldValue != null)
                    imageNameField.textProperty().unbindBidirectional(oldValue.nameProperty());
                if(newValue != null)
                    bindSelectedPhoto(newValue);
            }
        );
    }

    public void setModel(Gallery gallery) {
        this.galleryModel = gallery;
        imagesListView.setItems(gallery.getPhotos());
        imagesListView.getSelectionModel().select(0);
    }

    private void bindSelectedPhoto(Photo selectedPhoto) {
        this.imageNameField.textProperty().bindBidirectional(selectedPhoto.nameProperty());
        this.imageView.imageProperty().bind(selectedPhoto.photoDataProperty());
    }

    public void searchButtonClicked(ActionEvent event) {
        PhotoDownloader photoDownloader = new PhotoDownloader();
        this.galleryModel.clear();
        photoDownloader.searchForPhotos(this.searchTextField.getText())
                .take(10)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(photo -> this.galleryModel.addPhoto(photo));
    }
}

