package io.github.guiritter.image_lister_side_by_side;

public class ImageCouple {
	public final String imageLeft;
	public final String imageRight;

	public ImageCouple(String _imageLeft, String _imageRight) {
		imageLeft = _imageLeft;
		imageRight = _imageRight;
	}

	@Override
	public String toString() {
		return String.format("ImageCouple [imageLeft=%s, imageRight=%s]", imageLeft, imageRight);
	}
}
