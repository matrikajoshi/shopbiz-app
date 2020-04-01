package self.edu.shopbiz.util;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ServicesUtil
{
	public final static String RATING_FROM = "ratingFrom";
	public final static String RATING_TO = "ratingTo";

	private static void validateParameterNotNull(Object parameter, String nullMessage)
	{
		Preconditions.checkArgument(parameter != null, nullMessage);
	}

	public static void validateParameterNotNullStandardMessage(String param, Object value)
	{
		validateParameterNotNull(value, "Parameter " + param + " cannot be null");
	}

	/**
	 * Returns a map to provide range of from and to values
	 * @param ratingFrom from range - optional
	 * @param ratingTo to range - optional
	 * @return map containing from and to, if either value is nullable, then highest / lowest possible values
	 * are returned
	 */
	public static Map<String, Double> getRatingsLimit(Optional<Double> ratingFrom, Optional<Double> ratingTo) {
		Map<String,Double> ratingsLimitMap = new HashMap<>();
		Double ratingStart = ratingFrom.isPresent()? ratingFrom.get() : Double.valueOf(0);
		Double ratingEnd = ratingTo.isPresent()? ratingTo.get() : Double.MAX_VALUE;
		// handling inverse order
		ratingsLimitMap.put(RATING_FROM, Double.min(ratingStart, ratingEnd));
		ratingsLimitMap.put(RATING_TO, Double.max(ratingStart,ratingEnd));
		return ratingsLimitMap;
	}
}
