package github.fabianterhorst.drawer_sliding_pane_layout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.model.BaseDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.util.PressedEffectStateListDrawable;
import com.mikepenz.materialdrawer.util.UIUtils;

public class IconDrawerItem extends BaseDrawerItem<IconDrawerItem> implements Badgeable<IconDrawerItem> {
    private String badge;

    public IconDrawerItem withBadge(String badge) {
        this.badge = badge;
        return this;
    }

    public String getBadge() {
        return badge;
    }

    @Override
    public void setBadge(String badge) {
        this.badge = badge;
    }


    @Override
    public String getType() {
        return "PRIMARY_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_icon;
    }

    @Override
    public View convertView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        Context ctx = parent.getContext();

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(getLayoutRes(), parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int selected_color = getSelectedColor();
        if (selected_color == 0 && getSelectedColorRes() != -1) {
            selected_color = ctx.getResources().getColor(getSelectedColorRes());
        } else if (selected_color == 0) {
            selected_color = UIUtils.getThemeColorFromAttrOrRes(ctx, com.mikepenz.materialdrawer.R.attr.material_drawer_selected, com.mikepenz.materialdrawer.R.color.material_drawer_selected);
        }
        UIUtils.setBackground(viewHolder.view, UIUtils.getDrawerItemBackground(selected_color));

        //get the correct color for the icon
        int iconColor;
        int selected_icon = getSelectedIconColor();
        if (selected_icon == 0 && getSelectedIconColorRes() != -1) {
            selected_icon = ctx.getResources().getColor(getSelectedIconColorRes());
        } else if (selected_icon == 0) {
            selected_icon = UIUtils.getThemeColorFromAttrOrRes(ctx, com.mikepenz.materialdrawer.R.attr.material_drawer_selected_text, com.mikepenz.materialdrawer.R.color.material_drawer_selected_text);
        }
        if (this.isEnabled()) {
            iconColor = getIconColor();
            if (iconColor == 0 && getIconColorRes() != -1) {
                iconColor = ctx.getResources().getColor(getIconColorRes());
            } else if (iconColor == 0) {
                iconColor = UIUtils.getThemeColorFromAttrOrRes(ctx, com.mikepenz.materialdrawer.R.attr.material_drawer_primary_icon, com.mikepenz.materialdrawer.R.color.material_drawer_primary_icon);
            }
        } else {
            iconColor = getDisabledIconColor();
            if (iconColor == 0 && getDisabledIconColorRes() != -1) {
                iconColor = ctx.getResources().getColor(getDisabledIconColorRes());
            } else if (iconColor == 0) {
                iconColor = UIUtils.getThemeColorFromAttrOrRes(ctx, com.mikepenz.materialdrawer.R.attr.material_drawer_hint_text, com.mikepenz.materialdrawer.R.color.material_drawer_hint_text);
            }
        }

        Drawable icon = null;
        Drawable selectedIcon = null;
        if (this.getIcon() != null) {
            icon = this.getIcon();

            if (this.getSelectedIcon() != null) {
                selectedIcon = this.getSelectedIcon();
            } else if (this.isSelectedIconTinted()) {
                icon = new PressedEffectStateListDrawable(icon, selected_icon);
            }
        } else if (this.getIIcon() != null) {
            icon = new IconicsDrawable(ctx, this.getIIcon()).color(iconColor).actionBarSize().paddingDp(1);
            selectedIcon = new IconicsDrawable(ctx, this.getIIcon()).color(selected_icon).actionBarSize().paddingDp(1);
        } else if (this.getIconRes() > -1) {
            icon = UIUtils.getCompatDrawable(ctx, getIconRes());

            if (this.getSelectedIconRes() > -1) {
                selectedIcon = UIUtils.getCompatDrawable(ctx, getSelectedIconRes());
            } else if (this.isSelectedIconTinted()) {
                icon = new PressedEffectStateListDrawable(icon, selected_icon);
            }
        }

        if (icon != null) {
            if (selectedIcon != null) {
                viewHolder.icon.setImageDrawable(UIUtils.getIconColor(icon, selectedIcon));
            } else {
                viewHolder.icon.setImageDrawable(icon);
            }

            viewHolder.icon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        private View view;
        private ImageView icon;

        private ViewHolder(View view) {
            this.view = view;
            this.icon = (ImageView) view.findViewById(com.mikepenz.materialdrawer.R.id.icon);
        }
    }
}
