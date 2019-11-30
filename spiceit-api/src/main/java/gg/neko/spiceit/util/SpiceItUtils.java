package gg.neko.spiceit.util;

import gg.neko.spiceit.annotation.FallbackIt;
import gg.neko.spiceit.annotation.LogIt;
import gg.neko.spiceit.annotation.TimeIt;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * Common utility methods for SpiceIt.
 */
public class SpiceItUtils {

    private SpiceItUtils() {
        throw new UnsupportedOperationException("do not instantiate this class");
    }

    /**
     * Returns what annotations are currently exposed by this version of SpiceIt.
     *
     * @return a {@link List} of available SpiceIt annotations
     */
    public static List<Class<? extends Annotation>> spiceItAnnotations() {
        return Arrays.asList(FallbackIt.class,
                             TimeIt.class,
                             LogIt.class);
    }

}
