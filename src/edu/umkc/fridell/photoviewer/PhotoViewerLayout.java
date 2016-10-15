package edu.umkc.fridell.photoviewer;

import java.awt.*;
import java.util.ResourceBundle;

import javax.swing.*;

public class PhotoViewerLayout extends JFrame {

  private PhotoViewerModel model;
  JButton nextButton;
  JButton prevButton;
  JTextField pictureNumberTextField;
  ImageIcon imageIcon;
  JLabel imageLabel;
  JLabel descriptionText;
  JTextArea descriptionTextArea;
  JButton addButton;
  JButton saveButton;
  JButton deleteButton;
  JLabel pictureCountLabel;
  JLabel dateLabel;
  JTextField dateTextField;

  public PhotoViewerLayout() {
    model = new PhotoViewerModel(this);
    Container contentPane = getContentPane();


    imageLabel = new JLabel("", SwingConstants.CENTER);
    JScrollPane scrollPane = new JScrollPane(imageLabel);

    contentPane.add(scrollPane, BorderLayout.CENTER);

    JPanel controlPane = new JPanel();
    controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.PAGE_AXIS));

    JPanel descriptionPane = new JPanel();
    descriptionPane.setLayout(new FlowLayout(FlowLayout.LEFT));

    descriptionText = new JLabel(ResourceBundle.getBundle("PhotoAlbumStrings").getString("DescriptionLabel"));
    descriptionTextArea = new JTextArea(4, 20);
    descriptionPane.add(descriptionText);
    descriptionPane.add(descriptionTextArea);
    descriptionTextArea.setText(model.getDescription());

    JPanel datePane = new JPanel();

    dateLabel = new JLabel(ResourceBundle.getBundle("PhotoAlbumStrings").getString("DateLabel"));
    dateLabel.setPreferredSize(new Dimension(descriptionText.getPreferredSize().width, dateLabel.getPreferredSize().height));
    dateTextField = new JTextField(ResourceBundle.getBundle("PhotoAlbumStrings").getString("DateFormat"));
    datePane.add(dateLabel);
    datePane.add(dateTextField);

    JPanel buttonPane = new JPanel();

    deleteButton = new JButton(ResourceBundle.getBundle("PhotoAlbumStrings").getString("Delete"));
    deleteButton.addActionListener(e -> model.delete());

    saveButton = new JButton(ResourceBundle.getBundle("PhotoAlbumStrings").getString("SaveChanges"));
    saveButton.addActionListener(e -> model.save());

    addButton = new JButton(ResourceBundle.getBundle("PhotoAlbumStrings").getString("AddPhoto"));
    addButton.addActionListener(e -> model.add());

    buttonPane.add(deleteButton);
    buttonPane.add(saveButton);
    buttonPane.add(addButton);

    JPanel leftRightPane = new JPanel();
    leftRightPane.setLayout(new BorderLayout());
    leftRightPane.add(datePane, BorderLayout.WEST);
    leftRightPane.add(buttonPane, BorderLayout.EAST);


    JPanel southButtonPanel = new JPanel();
    pictureNumberTextField = new JTextField(model.getCurrentPhotoNumber(), 4);
    pictureNumberTextField.addActionListener(e -> model.search());
    pictureCountLabel = new JLabel(
        ResourceBundle.getBundle("PhotoAlbumStrings").getString(" of ") + model.getPhotoCount());
    prevButton = new JButton(ResourceBundle.getBundle("PhotoAlbumStrings").getString("Previous"));
    prevButton.addActionListener(e -> model.prevButton());
    prevButton.setEnabled(false);
    nextButton = new JButton(ResourceBundle.getBundle("PhotoAlbumStrings").getString("Next"));
    nextButton.addActionListener(e -> model.nextButton());
    nextButton.setEnabled(false);

    southButtonPanel.add(pictureNumberTextField);
    southButtonPanel.add(pictureCountLabel);
    southButtonPanel.add(prevButton);
    southButtonPanel.add(nextButton);
    FlowLayout flowLayout = (FlowLayout) southButtonPanel.getLayout();
    flowLayout.setAlignment(FlowLayout.LEFT);


    controlPane.add(descriptionPane);
    controlPane.add(leftRightPane);
    controlPane.add(southButtonPanel);

    contentPane.add(controlPane, BorderLayout.SOUTH); // Or PAGE_END
  }

  public static void createAndShowGUI() {
    JFrame frame = new PhotoViewerLayout();
    frame.pack();
    frame.setSize(500, 500);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }
}