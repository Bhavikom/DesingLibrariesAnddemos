package com.yasoka.eazyscreenrecord.utils.taptargetview;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toolbar;
import java.util.ArrayList;
import java.util.Stack;

class ToolbarTapTarget extends ViewTapTarget {

    @TargetApi(21)
    private static class StandardToolbarProxy implements ToolbarProxy {
        private final Toolbar toolbar;

        StandardToolbarProxy(Toolbar toolbar2) {
            this.toolbar = toolbar2;
        }

        public CharSequence getNavigationContentDescription() {
            return this.toolbar.getNavigationContentDescription();
        }

        public void setNavigationContentDescription(CharSequence charSequence) {
            this.toolbar.setNavigationContentDescription(charSequence);
        }

        public void findViewsWithText(ArrayList<View> arrayList, CharSequence charSequence, int i) {
            this.toolbar.findViewsWithText(arrayList, charSequence, i);
        }

        public Drawable getNavigationIcon() {
            return this.toolbar.getNavigationIcon();
        }

        @Nullable
        public Drawable getOverflowIcon() {
            if (VERSION.SDK_INT >= 23) {
                return this.toolbar.getOverflowIcon();
            }
            return null;
        }

        public int getChildCount() {
            return this.toolbar.getChildCount();
        }

        public View getChildAt(int i) {
            return this.toolbar.getChildAt(i);
        }

        public Object internalToolbar() {
            return this.toolbar;
        }
    }

    private static class SupportToolbarProxy implements ToolbarProxy {
        private final android.support.v7.widget.Toolbar toolbar;

        SupportToolbarProxy(android.support.v7.widget.Toolbar toolbar2) {
            this.toolbar = toolbar2;
        }

        public CharSequence getNavigationContentDescription() {
            return this.toolbar.getNavigationContentDescription();
        }

        public void setNavigationContentDescription(CharSequence charSequence) {
            this.toolbar.setNavigationContentDescription(charSequence);
        }

        public void findViewsWithText(ArrayList<View> arrayList, CharSequence charSequence, int i) {
            this.toolbar.findViewsWithText(arrayList, charSequence, i);
        }

        public Drawable getNavigationIcon() {
            return this.toolbar.getNavigationIcon();
        }

        public Drawable getOverflowIcon() {
            return this.toolbar.getOverflowIcon();
        }

        public int getChildCount() {
            return this.toolbar.getChildCount();
        }

        public View getChildAt(int i) {
            return this.toolbar.getChildAt(i);
        }

        public Object internalToolbar() {
            return this.toolbar;
        }
    }

    private interface ToolbarProxy {
        void findViewsWithText(ArrayList<View> arrayList, CharSequence charSequence, int i);

        View getChildAt(int i);

        int getChildCount();

        CharSequence getNavigationContentDescription();

        Drawable getNavigationIcon();

        @Nullable
        Drawable getOverflowIcon();

        Object internalToolbar();

        void setNavigationContentDescription(CharSequence charSequence);
    }

    ToolbarTapTarget(android.support.v7.widget.Toolbar toolbar, @IdRes int i, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        super(toolbar.findViewById(i), charSequence, charSequence2);
    }

    ToolbarTapTarget(Toolbar toolbar, @IdRes int i, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        super(toolbar.findViewById(i), charSequence, charSequence2);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    ToolbarTapTarget(android.support.v7.widget.Toolbar toolbar, boolean z, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        super(z ? findNavView(toolbar) : findOverflowView(toolbar), charSequence, charSequence2);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    ToolbarTapTarget(Toolbar toolbar, boolean z, CharSequence charSequence, @Nullable CharSequence charSequence2) {
        super(z ? findNavView(toolbar) : findOverflowView(toolbar), charSequence, charSequence2);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static ToolbarProxy proxyOf(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Given null instance");
        } else if (obj instanceof android.support.v7.widget.Toolbar) {
            return new SupportToolbarProxy((android.support.v7.widget.Toolbar) obj);
        } else {
            if (obj instanceof Toolbar) {
                return new StandardToolbarProxy((Toolbar) obj);
            }
            throw new IllegalStateException("Couldn't provide proper toolbar proxy instance");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static View findNavView(Object obj) {
        ToolbarProxy proxyOf = proxyOf(obj);
        CharSequence navigationContentDescription = proxyOf.getNavigationContentDescription();
        boolean z = !TextUtils.isEmpty(navigationContentDescription);
        if (!z) {
            navigationContentDescription = "taptarget-findme";
        }
        proxyOf.setNavigationContentDescription(navigationContentDescription);
        ArrayList arrayList = new ArrayList(1);
        proxyOf.findViewsWithText(arrayList, navigationContentDescription, 2);
        if (!z) {
            proxyOf.setNavigationContentDescription(null);
        }
        if (arrayList.size() > 0) {
            return (View) arrayList.get(0);
        }
        Drawable navigationIcon = proxyOf.getNavigationIcon();
        if (navigationIcon != null) {
            int childCount = proxyOf.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = proxyOf.getChildAt(i);
                if ((childAt instanceof ImageButton) && ((ImageButton) childAt).getDrawable() == navigationIcon) {
                    return childAt;
                }
            }
        }
        try {
            return (View) ReflectUtil.getPrivateField(proxyOf.internalToolbar(), "mNavButtonView");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Could not find navigation view for Toolbar!", e);
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Unable to access navigation view for Toolbar!", e2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static View findOverflowView(Object obj) {
        ToolbarProxy proxyOf = proxyOf(obj);
        Drawable overflowIcon = proxyOf.getOverflowIcon();
        if (overflowIcon != null) {
            Stack stack = new Stack();
            stack.push((ViewGroup) proxyOf.internalToolbar());
            while (!stack.empty()) {
                ViewGroup viewGroup = (ViewGroup) stack.pop();
                int childCount = viewGroup.getChildCount();
                int i = 0;
                while (true) {
                    if (i < childCount) {
                        View childAt = viewGroup.getChildAt(i);
                        if (childAt instanceof ViewGroup) {
                            stack.push((ViewGroup) childAt);
                        } else if ((childAt instanceof ImageView) && ((ImageView) childAt).getDrawable() == overflowIcon) {
                            return childAt;
                        }
                        i++;
                    }
                }
            }
        }
        try {
            return (View) ReflectUtil.getPrivateField(ReflectUtil.getPrivateField(ReflectUtil.getPrivateField(proxyOf.internalToolbar(), "mMenuView"), "mPresenter"), "mOverflowButton");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Could not find overflow view for Toolbar!", e);
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Unable to access overflow view for Toolbar!", e2);
        }
    }
}
