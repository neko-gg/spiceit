package gg.neko.spiceit.util;

import gg.neko.spiceit.annotation.BenchIt;
import gg.neko.spiceit.annotation.BreakIt;
import gg.neko.spiceit.annotation.BulkIt;
import gg.neko.spiceit.annotation.CacheIt;
import gg.neko.spiceit.annotation.FallbackIt;
import gg.neko.spiceit.annotation.LimitIt;
import gg.neko.spiceit.annotation.LogIt;
import gg.neko.spiceit.annotation.RetryIt;
import gg.neko.spiceit.annotation.TimeIt;
import gg.neko.spiceit.annotation.TimeoutIt;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class SpiceItUtils {

    private SpiceItUtils() { throw new UnsupportedOperationException(); }

    public static List<Class<? extends Annotation>> spiceItAnnotations() {
        return Arrays.asList(BenchIt.class,
                             RetryIt.class,
                             BreakIt.class,
                             BulkIt.class,
                             LimitIt.class,
                             TimeoutIt.class,
                             CacheIt.class,
                             FallbackIt.class,
                             TimeIt.class,
                             LogIt.class);
    }

}
