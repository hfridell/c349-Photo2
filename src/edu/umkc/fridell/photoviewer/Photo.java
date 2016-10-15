package edu.umkc.fridell.photoviewer;

import javax.swing.*;

import java.io.File;
import java.io.IOException;

public class Photo {

  private String description;
  private String name;
  private String date = null;
  private String location;
  private ImageIcon photo;

  public Photo(File file) throws IOException {
    location = file.getCanonicalPath();
    name = file.getName();
    photo = new ImageIcon(location);
  }

  public String getDescription() {
    return description;
  }

  public String getLocation() {
    return location;
  }


  public ImageIcon getPhoto() {
    return photo;
  }

  public String getName() {
    return name;
  }

  public String getDate() {
    return date;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
