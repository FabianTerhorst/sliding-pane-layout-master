package github.fabianterhorst.drawer_sliding_pane_layout;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout crossFadePanViews = (FrameLayout) findViewById(R.id.cross_fade_pane_views);
        LinearLayout rightLayout = (LinearLayout) findViewById(R.id.right_layout);

        Drawer.Result result = new Drawer()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.app_name).withIcon(FontAwesome.Icon.faw_align_justify),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.app_name).withIcon(FontAwesome.Icon.faw_angellist)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                    }
                })
                .buildView();

        crossFadePanViews.addView(result.getSlider());

        Drawer.Result result2 = new Drawer()
                .withActivity(this)
                .withDrawerWidthRes(R.dimen.partial_pane_width)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        new IconDrawerItem().withIcon(FontAwesome.Icon.faw_align_justify),
                        new IconDrawerItem().withIcon(FontAwesome.Icon.faw_angellist)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                    }
                })
                .buildView();

        Configuration configuration = this.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;
        int normalWidth = Math.round(pxFromDp(this,64));
        int screenWidth = Math.round(pxFromDp(this, screenWidthDp));
        int responseWidth = screenWidth - normalWidth;

        RelativeLayout slider = result2.getSlider();
        slider.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        slider.getLayoutParams().width = normalWidth;
        crossFadePanViews.addView(slider);

        DrawerFadeSlidingPaneLayout crossFadeSlidingPaneLayout = (DrawerFadeSlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
        crossFadeSlidingPaneLayout.setFullView(crossFadePanViews.getChildAt(0));
        crossFadeSlidingPaneLayout.setSliderFadeColor(Color.TRANSPARENT);
        crossFadeSlidingPaneLayout.setPartialView(crossFadePanViews.getChildAt(1));
        rightLayout.getLayoutParams().width = responseWidth;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}