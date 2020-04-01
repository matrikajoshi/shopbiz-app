package self.edu.shopbiz.util;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ServicesUtilTest {

    @Test
    public void testGetRatingsRange() {
        Optional<Double> ratingFrom = Optional.ofNullable(7d);
        Optional<Double> ratingTo  = Optional.ofNullable(null);
        Map<String, Double> ratingsLimit = ServicesUtil.getRatingsLimit(ratingFrom, ratingTo);
        assertEquals(Double.valueOf(7), ratingsLimit.get(ServicesUtil.RATING_FROM), "ratings From min 0");
        assertEquals(Double.MAX_VALUE, ratingsLimit.get(ServicesUtil.RATING_TO), "ratingsTo ");
    }

    @Test
    void getRatingsLimit() {
        Optional<Double> ratingFrom = Optional.ofNullable(7d);
        Optional<Double> ratingTo  = Optional.ofNullable(4.5d);
        Map<String, Double> ratingsLimit = ServicesUtil.getRatingsLimit(ratingFrom, ratingTo);
        assertEquals(Double.valueOf(4.5), ratingsLimit.get(ServicesUtil.RATING_FROM), "ratings From min 0");
        assertEquals(Double.valueOf(7), ratingsLimit.get(ServicesUtil.RATING_TO), "ratingsTo ");
    }
}