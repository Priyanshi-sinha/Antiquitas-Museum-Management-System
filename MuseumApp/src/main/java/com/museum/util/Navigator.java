package com.museum.util;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public final class Navigator {
    private static StackPane root;
    private static Runnable navRefresh;

    public static void init(StackPane contentRoot, Runnable onNavigate) {
        root = contentRoot;
        navRefresh = onNavigate;
    }

    public static void go(Node page) {
        if (root != null) {
            root.getChildren().setAll(page);
            if (navRefresh != null) navRefresh.run();
        }
    }
}
