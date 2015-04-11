package github.fabianterhorst.drawer_sliding_pane_layout;

import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class DrawerFadeSlidingPaneLayout extends SlidingPaneLayout {
    private View partialView = null;
    private View fullView = null;

    private boolean wasOpened = false;

    public DrawerFadeSlidingPaneLayout(Context context) {
        super(context);
    }

    public DrawerFadeSlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerFadeSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        super.setPanelSlideListener(crossFadeListener);
    }

    public void setFullView(View fullView) {
        this.fullView = fullView;
    }

    public void setPartialView(View partialView) {
        this.partialView = partialView;
    }

    @Override
    public void setPanelSlideListener(final PanelSlideListener listener) {
        if (listener == null) {
            super.setPanelSlideListener(crossFadeListener);
            return;
        }

        super.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                crossFadeListener.onPanelSlide(panel, slideOffset);
                listener.onPanelSlide(panel, slideOffset);
            }

            @Override
            public void onPanelOpened(View panel) {
                listener.onPanelOpened(panel);
            }

            @Override
            public void onPanelClosed(View panel) {
                listener.onPanelClosed(panel);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (partialView != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                updatePartialViewVisibilityPreHoneycomb(isOpen());
            } else {
                partialView.setVisibility(isOpen() ? View.GONE : VISIBLE);
            }
        }
    }

    private SimplePanelSlideListener crossFadeListener = new SimplePanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            super.onPanelSlide(panel, slideOffset);
            if (partialView == null || fullView == null) {
                return;
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                if (slideOffset == 1 && !wasOpened) {
                    updatePartialViewVisibilityPreHoneycomb(true);
                    wasOpened = true;
                } else if (slideOffset < 1 && wasOpened) {
                    updatePartialViewVisibilityPreHoneycomb(false);
                    wasOpened = false;
                }
            } else {
                partialView.setVisibility(isOpen() ? View.GONE : VISIBLE);
            }

            ViewHelper.setAlpha(partialView, 1 - slideOffset);
            ViewHelper.setAlpha(fullView, slideOffset);
        }
    };

    private void updatePartialViewVisibilityPreHoneycomb(boolean slidingPaneOpened) {
        if (slidingPaneOpened) {
            partialView.layout(-partialView.getWidth(), 0, 0, partialView.getHeight());
        } else {
            partialView.layout(0, 0, partialView.getWidth(), partialView.getHeight());
        }
    }
}
