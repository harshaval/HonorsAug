package co.harshaval.skeletontest;


import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * A {@link TextureView} that can be adjusted to a specified aspect ratio.
 */
public class AutoFitTextureView extends TextureView {
    private int widthRatio = 0;
    private int heightRatio = 0;

    public AutoFitTextureView(final Context context) {
        this(context, null);
    }

    public AutoFitTextureView(final Context context, final AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public AutoFitTextureView(final Context context, final AttributeSet attributes, final int defStyle) {
        super(context, attributes, defStyle);
    }

    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters.
     */
    public void setAspectRatio(final int width, final int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        widthRatio = width;
        heightRatio = height;
        requestLayout();
    }
}
