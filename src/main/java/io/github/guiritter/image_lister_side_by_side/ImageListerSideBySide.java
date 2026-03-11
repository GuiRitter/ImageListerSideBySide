package io.github.guiritter.image_lister_side_by_side;

import static java.lang.System.out;


import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Desktop.Action;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ImageListerSideBySide {

	private static final JFrame frame;

	private static final int HALF_PADDING = 5;

	private static final int FULL_PADDING = 2 * HALF_PADDING;

	private static final List<String> imageList = new LinkedList<>();

	private static int imagePreviewHeightMax;
	private static int imagePreviewWidthMax;

	private static JScrollPane pane;
	
	private static JPanel panel;

	private static final JTextArea textArea;

	static {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		frame = new JFrame();
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), Y_AXIS));
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// pane = new JScrollPane();
		// frame.getContentPane().add(pane);

		// panel = new JPanel();
		// pane.setViewportView(panel);

		// var initButton = new JButton("init");
		// ActionListener onInitPressed = e -> onInitPressed();
		// initButton.addActionListener(onInitPressed);
		// panel.add(initButton);
		
		// panel = new JPanel();
		// frame.getContentPane().add(panel);

		textArea = new JTextArea();
		frame.getContentPane().add(textArea);

		var readListButton = new JButton("read list");
		ActionListener onReadListPressed = e -> onInitPressed();
		readListButton.addActionListener(onReadListPressed);
		readListButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		readListButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, readListButton.getPreferredSize().height));
		frame.getContentPane().add(readListButton);
	} 

	private static final GridBagConstraints buildGBC(int y, int topPadding, int bottomPadding) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = y;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(topPadding, FULL_PADDING, bottomPadding, FULL_PADDING);
		return gbc;
	}

	// private static final void onInitPressed() {
	// 	// JOptionPane.show

	// 	imagePreviewWidthMax = panel.getWidth() / 3;
	// 	imagePreviewHeightMax = panel.getHeight() / 3;
	// }

	private static final void onInitPressed() {
		var inputText = textArea.getText();
		var lineList = inputText.split("\n");
		imageList.addAll(List.of(lineList));

		frame.getContentPane().removeAll();

		pane = new JScrollPane();
		frame.getContentPane().add(pane);

		panel = new JPanel();
		pane.setViewportView(panel);
	}

	public static void main(String args[]) throws IOException {
		out.println("ImageListerSideBySide");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
