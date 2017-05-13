package sk.styk.martin.bakalarka.statistics.processors.helpers;

import java.math.BigDecimal;

/**
 * Created by Martin Styk on 18.01.2016.
 */
public class PercentageHelper {
    public static BigDecimal getPercentage(double part, double whole) {
        final double res = (part * 100 / whole);
        if (!Double.isNaN(res)) {
            return BigDecimal.valueOf(res);
        } else {
        	return BigDecimal.valueOf(0);
        }
    }
}
