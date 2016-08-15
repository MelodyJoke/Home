package com.teamsolo.home.structure.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;

import java.util.List;

/**
 * description: main activity fab and bottom navigation behavior
 * author: Melody
 * date: 2016/6/23
 * version: 0.0.0.1
 */
@SuppressWarnings("unused")
public class MainFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    public MainFabBehavior() {

    }

    public MainFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child,
                                   View dependency) {
        return dependency instanceof BottomNavigationBar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child,
                                          View dependency) {
        float translationY = getFabTranslationYForBottomNavBar(parent, child);
        child.setTranslationY(translationY);

        return false;
    }

    private float getFabTranslationYForBottomNavBar(CoordinatorLayout parent,
                                                    FloatingActionButton fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);

        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof BottomNavigationBar && parent.doViewsOverlap(fab, view))
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
        }

        return minOffset;
    }
}
