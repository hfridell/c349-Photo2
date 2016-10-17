package edu.umkc.fridell.photoviewer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

import edu.umkc.fridell.database.DbController;

import static java.util.ResourceBundle.getBundle;

public class PhotoViewerModel {

  private Photo currentPhoto;
  private PhotoViewerLayout layout;
  private int photoCount = 0;

  private int countPhotos() {
    int count = DbController.getInstance().count("Photo");
    if (count != -1) {
      return count;
    }
    else {
      return 0;
    }
  }

  private int currentPhotoNumber = 0;
  private ArrayList<Photo> photos = new ArrayList<>();
  private JFileChooser fileChooser = new JFileChooser();

  public PhotoViewerModel(PhotoViewerLayout layout) {
    this.layout = layout;
    photoCount = countPhotos();
    if (photoCount != 0) {
      currentPhotoNumber = 1;
    }
    updateUi();
  }

  public void delete() {
    photoCount--;
    prevButton();
  }

  public void add() {
    int returnVal = fileChooser.showOpenDialog(layout.addButton);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      try {
        Photo newPhoto = new Photo(file, currentPhotoNumber);
        save();
        currentPhoto = newPhoto;

        photoCount++;
        nextButton();
        PhotoWriter writer = new PhotoWriter(DbController.getInstance());
        writer.write(newPhoto);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  updateUi();
  }

  public void save() {
    savePhotoInfo();
    PhotoWriter writer = new PhotoWriter(DbController.getInstance());
    try {
      if (currentPhotoNumber > 0) {
        writer.update(currentPhoto);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void savePhotoInfo() {
    if (currentPhoto != null) {
      if (!layout.descriptionTextArea.getText().equals(getBundle("PhotoAlbumStrings").getString("DefaultDescription"))) {
        currentPhoto.setDescription(layout.descriptionTextArea.getText());
      }
      if (!layout.dateTextField.getText().equals(getBundle("PhotoAlbumStrings").getString("DateFormat"))) {
        currentPhoto.setDate(LocalDate.parse(layout.dateTextField.getText()));
      }
      currentPhoto.setOrder(currentPhotoNumber);
    }
  }

  public void prevButton() {
    savePhotoInfo();
    if (currentPhotoNumber > 1){
      currentPhotoNumber--;
    }
    updateUi();
  }

  public void nextButton() {
    savePhotoInfo();
    if (currentPhotoNumber < photoCount) {
      currentPhotoNumber++;
    }
    updateUi();
  }

  private void updateButtons() {
    layout.nextButton.setEnabled(currentPhotoNumber != photoCount);
    layout.prevButton.setEnabled(currentPhotoNumber > 1);
  }

  public String getPhotoCount() {
    return Integer.toString(photoCount);
  }

  public String getCurrentPhotoNumber() {
    return Integer.toString(currentPhotoNumber);
  }


  private void updateUi() {
    updateButtons();
    if (photoCount == 0 && currentPhoto == null) {
      layout.imageIcon = new ImageIcon("");
      layout.descriptionTextArea.setText(getBundle("PhotoAlbumStrings").getString("DefaultDescription"));
      layout.dateTextField = new JTextField(getBundle("PhotoAlbumStrings").getString("DateFormat"));
    } else {
      if (getPhoto(currentPhotoNumber) == null){
        layout.imageIcon = new ImageIcon(currentPhoto.getContent());
        layout.imageLabel = new JLabel(currentPhoto.getName());
      } else {
        currentPhoto = getPhoto(currentPhotoNumber);
        layout.imageIcon = new ImageIcon(currentPhoto.getContent());
        layout.imageLabel.setIcon(layout.imageIcon);
      }

      if (currentPhoto.getDate() == null) {
        layout.dateTextField.setText(getBundle("PhotoAlbumStrings").getString("DateFormat"));
      } else {
        layout.dateTextField.setText(currentPhoto.getDate().toString());
      }

      layout.descriptionTextArea.setText(currentPhoto.getDescription());
    }

    layout.pictureNumberTextField.setText(Integer.toString(currentPhotoNumber));
    layout.pictureCountLabel.setText(getBundle("PhotoAlbumStrings").getString(" of ") + getPhotoCount());
  }

  public void search() {
    int search = Integer.parseInt(layout.pictureNumberTextField.getText());
    if (search <= photoCount && search > 0) {
      currentPhotoNumber = search;
    }
    updateUi();
  }

  private Photo getPhoto(int x) {
    PhotoReader reader = new PhotoReader(DbController.getInstance());
    try {
      return reader.read(x);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
