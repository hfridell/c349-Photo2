package edu.umkc.fridell.photoviewer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.UUID;

public class Photo {

  private String guid;
  private String description;
  private String name;
  private LocalDate date = null;
  private String location;
  private int order = -1;
  private byte[] content;

  public Photo(File file) throws IOException {
    location = file.getCanonicalPath();
    name = file.getName();
    if (file.canRead()){
      content = new byte[(int) file.length()];
      content = Files.readAllBytes(file.toPath());
      guid = UUID.nameUUIDFromBytes(content).toString();
    }
  }

  public Photo(String guid, String description, String name, LocalDate date, String location, int order, byte[] content) {
    this.guid = guid;
    this.description = description;
    this.name = name;
    this.date = date;
    this.location = location;
    this.order = order;
    this.content = content;
  }

  public Photo(File file, int currentPhotoNumber) throws IOException {
    this(file);
    order = currentPhotoNumber;
  }

  public String getDescription() {
    return description;
  }

  @Deprecated
  public String getLocation() {
    return location;
  }


  public byte[] getContent() {
    return content;
  }

  public String getName() {
    return name;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getGuid() {
    return guid;
  }

  public int getOrder(){
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public static class PhotoBuilder {
    private String guid;
    private String description;
    private String name;
    private LocalDate date = null;
    private String location;
    private int order;
    private byte[] content;

    public Photo build() throws IOException {
      return new Photo(guid, description, name, date, location, order, content);
    }

    public PhotoBuilder content(byte[] content) {
      this.content = content;
      return this;
    }

    public PhotoBuilder location(String location) {
      this.location = location;
      return this;
    }

    public PhotoBuilder date(java.sql.Date date) {
      if (date != null) {
        this.date = date.toLocalDate();
      }
      return this;
    }

    public PhotoBuilder name(String name) {
      this.name = name;
      return this;
    }

    public PhotoBuilder description(String description) {
      this.description = description;
      return this;
    }

    public PhotoBuilder guid(String guid) {
      this.guid = guid;
      return this;
    }

    public PhotoBuilder order(int order) {
      this.order = order;
      return this;
    }
  }
}
