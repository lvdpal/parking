package nl.finalist.parking.backend.database;

import org.apache.commons.lang.StringUtils;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Parameter;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Contains utilities useful for all dao classes.
 */
public final class UniqueResultEnforcer {
    private UniqueResultEnforcer() {
        // Intentionally left blank for this is a static class.
    }

    /**
     * Method that verifies if a parameter for an in-clause is not an empty list.
     *
     * @param query  The query that is adapted.
     * @param name   The name of the column.
     * @param values The possible values of the column.
     */
    public static void setInClauseParameter(Query query, String name, List<?> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("In clause values cannot be an empty list");
        }

        query.setParameter(name, values);
    }

    /**
     * Get at most X rows to make sure that an (implicit) unique constraint holds true.
     *
     * @param query          The query that is to be executed.
     * @param requiredAmount The required number of rows.
     * @param <T>            The type of the resulting rows.
     * @return The resulting rows.
     */
    public static <T> List<T> getAtMostXResults(Query query, Integer requiredAmount) {
        List<T> results = query.getResultList();

        if (results.size() > requiredAmount) {
            throw new NonUniqueResultException(
                    "Multiple instances found: " + results.size() + " instead of " + requiredAmount + " , queryParams: "
                            + describeParameters(query));
        }

        return results;
    }

    private static <T> Optional<T> getAtMostOnlyResult(List<T> results, Query query) {
        if (results.size() > 1) {
            throw new NonUniqueResultException(
                    "Multiple instances found: " + results.size() + " , queryParams: " + describeParameters(query));
        }

        if (results.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(results.get(0));
    }

    /**
     * Check that the given list contains only one result and return that result.
     * It will throw a NoResultException if there were no results and a NonUniqueResultException if there was more than
     * 1 result.
     *
     * @param results List of results
     * @param <T>     The type of the resulting rows.
     * @param query   Query used to obtain the result. Used here for logging purposes.
     * @return
     */
    public static <T> T getOnlyResult(List<T> results, Query query) {
        Optional<T> result = getAtMostOnlyResult(results, query);

        return result.orElseThrow(() -> new NoResultException("Not found, queryParams: " + describeParameters(query)));
    }

    /**
     * Check that the given query results in only one result and return that result.
     * It will throw a NoResultException if there were no results and a NonUniqueResultException if there was more than
     * 1 result.
     *
     * @param query The query that is to be executed.
     * @param <T>   The type of the resulting row.
     * @return The row
     */
    public static <T> T getOnlyResult(Query query) {
        List<T> results = query.getResultList();

        return getOnlyResult(results, query);
    }

    /**
     * Get either null or the only result. If there are more than one rows in the result, throw an exception.
     *
     * @param query The query that is to be executed
     * @param <T>   The type of the resulting row.
     * @return The row or null.
     */
    public static <T> Optional<T> getAtMostOnlyResult(Query query) {
        List<T> results = query.getResultList();

        return getAtMostOnlyResult(results, query);
    }

    /**
     * Delete exactly a single instance.
     *
     * @param query The query that should delete exactly one row.
     */
    public static void deleteExactlyOne(Query query) {
        int numAffected = query.executeUpdate();

        if (numAffected == 0) {
            throw new NoResultException(
                    "Deleted none instead of a single one, queryParams: " + describeParameters(query));
        } else if (numAffected != 1) {
            throw new NonUniqueResultException(
                    "Deleted " + numAffected + " instances instead of a single one, queryParams: " + describeParameters(
                            query));
        }
    }

    private static String describeParameters(Query query) {
        List<String> parameterDescriptions = new ArrayList<>();

        for (Parameter parameter : query.getParameters()) {
            parameterDescriptions.add(parameter.getName() + ": " + query.getParameterValue(parameter));
        }

        return StringUtils.join(parameterDescriptions, ", ");
    }
}
