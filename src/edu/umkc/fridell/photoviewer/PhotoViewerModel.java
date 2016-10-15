package edu.umkc.fridell.photoviewer;

import javax.swing.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PhotoViewerModel {

  private PhotoViewerLayout layout;
  private int photoCount = 0;
  private int currentPhotoNumber = 0;
  private ArrayList<Photo> photos = new ArrayList<>();
  private JFileChooser fileChooser = new JFileChooser();

  public PhotoViewerModel(PhotoViewerLayout layout) {
    this.layout = layout;
  }

  public void delete() {
    photos.remove(currentPhotoNumber - 1);
    photoCount--;
    prevButton();
  }

  public void add() {
    int returnVal = fileChooser.showOpenDialog(layout.addButton);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      try {
        photos.add(new Photo(file));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    photoCount++;
    nextButton();
  }

  public void save() {
    photos.get(currentPhotoNumber - 1).setDate(layout.dateTextField.getText());
    photos.get(currentPhotoNumber - 1).setDescription(layout.descriptionTextArea.getText());
    ObjectOutputStream oos;
    try {
      oos = new ObjectOutputStream(new FileOutputStream(
          photos.get(currentPhotoNumber - 1).getName() + ".txt"));
      oos.writeObject(photos.get(currentPhotoNumber - 1));
      oos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void prevButton() {
    currentPhotoNumber--;
    if (currentPhotoNumber < photoCount) {
      // Show previous photo
      layout.nextButton.setEnabled(true);
    }
    if (currentPhotoNumber <= 1) {
      layout.prevButton.setEnabled(false);
    }
    if (photoCount <= 1) {
      layout.nextButton.setEnabled(false);
    }
    updateUI();
  }

  public void nextButton() {
    if (currentPhotoNumber < photoCount) {
      // Show next photo
      currentPhotoNumber++;
    }
    if (currentPhotoNumber > 1) {
      layout.prevButton.setEnabled(true);
    }
    if (currentPhotoNumber == photoCount) {
      layout.nextButton.setEnabled(false);
    }
    updateUI();
  }

  public String getPhotoCount() {
    return Integer.toString(photoCount);
  }

  public String getCurrentPhotoNumber() {
    return Integer.toString(currentPhotoNumber);
  }

  public String getDescription() {
    if (photoCount <= 0) {
      return "Add a Photo to View";
    } else {
      return photos.get(currentPhotoNumber - 1).getDescription();
    }
  }

  public String getPhotoLocation() {
    if (photoCount <= 0) {
      return "";
    } else {
      return photos.get(currentPhotoNumber - 1).getLocation();
    }
  }

  public String getDate() {
    return photos.get(currentPhotoNumber - 1).getDate();
  }

  private void updateUI() {
    if (photoCount == 0) {
      layout.imageIcon = new ImageIcon("");
      layout.pictureCountLabel.setText(" of " + getPhotoCount());
      layout.pictureNumberTextField.setText(Integer.toString(currentPhotoNumber));
      layout.descriptionTextArea.setText(getDescription());
      layout.dateTextField.setText("mm/dd/yyyy");
    } else {
      layout.imageIcon = photos.get(currentPhotoNumber - 1).getPhoto();
      layout.imageLabel.setIcon(layout.imageIcon);
      layout.pictureCountLabel.setText(" of " + getPhotoCount());
      layout.pictureNumberTextField.setText(Integer.toString(currentPhotoNumber));
      if (photos.get(currentPhotoNumber - 1).getDate() == null) {
        layout.dateTextField.setText("mm/dd/yyyy");
      } else {
        layout.dateTextField.setText(photos.get(currentPhotoNumber - 1).getDate());
      }
      layout.descriptionTextArea.setText(photos.get(currentPhotoNumber - 1).getDescription());
    }
  }

  public void search() {
    int search = Integer.parseInt(layout.pictureNumberTextField.getText());
    if (search <= photoCount && search > 0) {
      currentPhotoNumber = search;
    }
    updateUI();
  }
}
