package io.github.guiritter.image_lister_side_by_side;

import static java.awt.GridBagConstraints.EAST;
import static java.awt.GridBagConstraints.WEST;
import static java.lang.System.exit;
import static java.lang.System.out;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import io.github.guiritter.image_component.ImageComponent;

public class ImageListerSideBySide {

	private static final JFrame frame;

	private static final int HALF_PADDING = 5;

	// private static final int FULL_PADDING = 2 * HALF_PADDING;

	private static final List<ImageCouple> imageCoupleList = new LinkedList<>();

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

		textArea = new JTextArea();
		frame.getContentPane().add(textArea);

		var readListButton = new JButton("read list");
		ActionListener onReadListPressed = e -> onInitPressed();
		readListButton.addActionListener(onReadListPressed);
		readListButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
		readListButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, readListButton.getPreferredSize().height));
		frame.getContentPane().add(readListButton);
	}

	private static final GridBagConstraints buildGBC(int x, int y, int topPadding, int bottomPadding, int anchor) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.fill = GridBagConstraints.NONE;
		// gbc.fill = GridBagConstraints.BOTH;
		// gbc.weightx = 1.0;
		gbc.insets = new Insets(topPadding, 0, bottomPadding, 0);
		gbc.anchor = anchor;
		return gbc;
	}

	private static final void buildRow(ImageCouple imageCouple) {
		var couplePanel = new JPanel();
		panel.add(couplePanel);

		couplePanel.setLayout(new GridBagLayout());

		BufferedImage imageLeft;
		BufferedImage imageRight;

		try {
			imageLeft = ImageIO.read(new File(imageCouple.imageLeft));
			imageRight = ImageIO.read(new File(imageCouple.imageRight));
		} catch (IOException e) {
			e.printStackTrace();
			showMessageDialog(frame, "error reading image", "error", ERROR_MESSAGE);
			exit(0);
			return;
		}

		var sizeLeft = new JTextField(imageLeft.getWidth() + " x " + imageLeft.getHeight());
		var sizeRight = new JTextField(imageRight.getWidth() + " x " + imageRight.getHeight());

		imageLeft = resizeImage(imageLeft, imagePreviewWidthMax, imagePreviewHeightMax);
		imageRight = resizeImage(imageRight, imagePreviewWidthMax, imagePreviewHeightMax);

		var max = new Dimension(imagePreviewWidthMax, imagePreviewHeightMax);

		var imageComponentLeft = new ImageComponent(imageLeft, max, max, max, max);
		var imageComponentRight = new ImageComponent(imageRight, max, max, max, max);

		var labelLeft = new JTextField(imageCouple.imageLeft);
		var labelRight = new JTextField(imageCouple.imageRight);

		couplePanel.add(labelLeft, buildGBC(0, 0, HALF_PADDING, 0, EAST));
		couplePanel.add(labelRight, buildGBC(1, 0, HALF_PADDING, 0, WEST));
		couplePanel.add(sizeLeft, buildGBC(0, 1, 0, 0, EAST));
		couplePanel.add(sizeRight, buildGBC(1, 1, 0, 0, WEST));
		couplePanel.add(imageComponentLeft, buildGBC(0, 2, 0, HALF_PADDING, EAST));
		couplePanel.add(imageComponentRight, buildGBC(1, 2, 0, HALF_PADDING, WEST));

		frame.revalidate();
	}

	private static final void onInitPressed() {

		imagePreviewWidthMax = textArea.getWidth() / 3;
		imagePreviewHeightMax = textArea.getHeight() / 3;
	
		var inputText = textArea.getText();
		var lineList = inputText.split("\n");
		var imageList = new LinkedList<String>();
		imageList.addAll(List.of(lineList));

		if ((imageList.size() % 2) != 0) {
			showMessageDialog(frame, "image list must contain an even number of images", "notice", WARNING_MESSAGE);
			exit(0);
		}

		var imageCoupleSize = imageList.size() / 2;

		ImageCouple imageCouple;

		for (int index = 0; index < imageCoupleSize; index++) {
			imageCouple = new ImageCouple(imageList.get(2 * index), imageList.get((2 * index) + 1));
			imageCoupleList.add(imageCouple);
		}

		frame.getContentPane().removeAll();

		pane = new JScrollPane();
		frame.getContentPane().add(pane);

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		pane.setViewportView(panel);

		imageCoupleList.forEach(ImageListerSideBySide::buildRow);
	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();

		if ((originalWidth <= maxWidth) && (originalHeight <= maxHeight)) {
			return originalImage;
		}

		double aspectRatio = (double) originalWidth / originalHeight;

		int newWidth = maxWidth;
		int newHeight = (int) (newWidth / aspectRatio);

		if (newHeight > maxHeight) {
			newHeight = maxHeight;
			newWidth = (int) (newHeight * aspectRatio);
		}

		out.format("ImageListerSideBySide.resizeImage; originalWidth: %d; originalHeight: %d; maxWidth: %d; maxHeight: %d; newWidth: %d; newHeight: %d\n", originalWidth, originalHeight, maxWidth, maxHeight, newWidth, newHeight);
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
		newImage.createGraphics().drawImage(originalImage, 0, 0, newWidth, newHeight, null);
		return newImage;
	}

	public static void main(String args[]) throws IOException {
		out.println("ImageListerSideBySide");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
