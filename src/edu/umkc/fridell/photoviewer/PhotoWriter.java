package edu.umkc.fridell.photoviewer;


import java.sql.PreparedStatement;
import java.sql.SQLException;

import edu.umkc.fridell.database.DbController;

public class PhotoWriter {
  private DbController db;

  public PhotoWriter(DbController db) {
    this.db = db;
  }

  public void write(Photo photo) throws SQLException {
    PreparedStatement pstmt = db.prepareStatement("INSERT INTO PHOTO " +
        "(guid, description, name, date, photo_order, content)" +
        "VALUES (?,?,?,?,?,?)");

    pstmt.setString(1, photo.getGuid());
    pstmt.setString(2, photo.getDescription());
    pstmt.setString(3, photo.getName());
    if (photo.getDate() == null){
      pstmt.setDate(4, null);
    } else {
      pstmt.setDate(4, java.sql.Date.valueOf(photo.getDate()));
    }
    pstmt.setInt(5, photo.getOrder());
    pstmt.setBytes(6, photo.getContent());

    pstmt.executeUpdate();
    pstmt.close();
  }

  public void update(Photo photo) throws SQLException {
    PreparedStatement pstmt = db.prepareStatement("UPDATE PHOTO SET description = ?, date = ?, photo_order = ? " +
        "WHERE guid = ?");

    pstmt.setString(1, photo.getDescription());
    if (photo.getDate() == null){
      pstmt.setDate(2, null);
    } else {
      pstmt.setDate(2, java.sql.Date.valueOf(photo.getDate()));
    }
    pstmt.setInt(3, photo.getOrder());
    pstmt.setString(4, photo.getGuid());

    pstmt.executeUpdate();
    pstmt.close();
  }
}
