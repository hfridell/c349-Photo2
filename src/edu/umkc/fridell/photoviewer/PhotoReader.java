package edu.umkc.fridell.photoviewer;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.umkc.fridell.database.DbController;

public class PhotoReader {

  private DbController db;

  public PhotoReader(DbController db) {
    this.db = db;
  }

  public List<Photo> read() throws SQLException {
    ResultSet rs = db.prepareStatement("SELECT guid, description, name, date, content " +
        "FROM PHOTO;").executeQuery();

    List<Photo> album = new ArrayList<>();
    while (rs.next()){
      Photo.PhotoBuilder builder = new Photo.PhotoBuilder();
      builder
          .guid(rs.getString(1))
          .description(rs.getString(2))
          .name(rs.getString(3))
          .date(rs.getDate(4))
          .content(rs.getBytes(5));
      try {
        album.add(builder.build());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return album;
  }

  public Photo read(int order) throws SQLException {
    ResultSet rs = db.prepareStatement("SELECT guid, description, name, date, photo_order, content " +
        "FROM PHOTO WHERE photo_order = " + order).executeQuery();

    while (rs.next()){
      Photo.PhotoBuilder builder = new Photo.PhotoBuilder();
      builder
          .guid(rs.getString(1))
          .description(rs.getString(2))
          .name(rs.getString(3))
          .date(rs.getDate(4))
          .order(rs.getInt(5))
          .content(rs.getBytes(6));
      try {
        return builder.build();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
