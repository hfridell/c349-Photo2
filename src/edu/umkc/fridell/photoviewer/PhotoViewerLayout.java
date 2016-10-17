package edu.umkc.fridell.photoviewer;

import java.awt.*;
import java.util.ResourceBundle;

import javax.swing.*;

import static java.util.ResourceBundle.*;

public class PhotoViewerLayout extends JFrame {

  private PhotoViewerModel model;
  JButton nextButton;
  JButton prevButton;
  JTextField pictureNumberTextField = new JTextField("", 4);
  ImageIcon imageIcon;
  JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
  JLabel descriptionText;
  JTextArea descriptionTextArea = new JTextArea(4, 20);
  JButton addButton;
  JButton saveButton;
  JButton deleteButton;
  JLabel pictureCountLabel = new JLabel(
      getBundle("PhotoAlbumStrings").getString(" of ") + 0);
  JLabel dateLabel;
  JTextField dateTextField = new JTextField(getBundle("PhotoAlbumStrings").getString("DateFormat"));

  public PhotoViewerLayout() {

    createButtons();


    model = new PhotoViewerModel(this);


    Container contentPane = getContentPane();
    JScrollPane scrollPane = new JScrollPane(imageLabel);
    contentPane.add(scrollPane, BorderLayout.CENTER);
    JPanel controlPane = new JPanel();
    controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.PAGE_AXIS));


    JPanel descriptionPane = new JPanel();
    descriptionPane.setLayout(new FlowLayout(FlowLayout.LEFT));
    descriptionText = new JLabel(getBundle("PhotoAlbumStrings").getString("DescriptionLabel"));
    descriptionPane.add(descriptionText);
    descriptionPane.add(descriptionTextArea);


    JPanel datePane = new JPanel();
    dateLabel = new JLabel(getBundle("PhotoAlbumStrings").getString("DateLabel"));
    dateLabel.setPreferredSize(new Dimension(descriptionText.getPreferredSize().width, dateLabel.getPreferredSize().height));
    datePane.add(dateLabel);
    datePane.add(dateTextField);


    JPanel buttonPane = new JPanel();
    deleteButton.addActionListener(e -> model.delete());
    saveButton.addActionListener(e -> model.save());
    addButton.addActionListener(e -> model.add());
    buttonPane.add(deleteButton);
    buttonPane.add(saveButton);
    buttonPane.add(addButton);


    JPanel leftRightPane = new JPanel();
    leftRightPane.setLayout(new BorderLayout());
    leftRightPane.add(datePane, BorderLayout.WEST);
    leftRightPane.add(buttonPane, BorderLayout.EAST);

    JPanel southButtonPanel = new JPanel();
    pictureNumberTextField.addActionListener(e -> model.search());


    prevButton.addActionListener(e -> model.prevButton());
    nextButton.addActionListener(e -> model.nextButton());


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

  private void createButtons() {
    prevButton = new JButton(getBundle("PhotoAlbumStrings").getString("Previous"));
    nextButton = new JButton(getBundle("PhotoAlbumStrings").getString("Next"));
    deleteButton = new JButton(getBundle("PhotoAlbumStrings").getString("Delete"));
    saveButton = new JButton(getBundle("PhotoAlbumStrings").getString("SaveChanges"));
    addButton = new JButton(getBundle("PhotoAlbumStrings").getString("AddPhoto"));
  }

  public void createAndShowGui() {
    JFrame frame = new PhotoViewerLayout();
    frame.pack();
    frame.setSize(500, 500);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }
}