package edu.umkc.fridell;

import javax.swing.*;

import edu.umkc.fridell.photoviewer.PhotoViewerLayout;

public class Main {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      PhotoViewerLayout mainLayout = new PhotoViewerLayout();
      mainLayout.createAndShowGui();
    });

  }
}
