package com.deange.gimgur.misc;

import android.animation.LayoutTransition;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public final class Utils {

    public static byte[] read(final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

    public static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {
                // Shhhhh... be still.
            }
        }
    }

    public static void setLayoutTransition(final ViewGroup view) {
        if (view != null) {
            final LayoutTransition transition = new LayoutTransition();
            view.setLayoutTransition(transition);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                transition.enableTransitionType(LayoutTransition.CHANGING);
            }
        }
    }

    private Utils() {
        // Uninstantiable
    }
}
