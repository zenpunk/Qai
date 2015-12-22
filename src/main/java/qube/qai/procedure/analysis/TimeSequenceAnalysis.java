package qube.qai.procedure.analysis;

import qube.qai.data.Arguments;
import qube.qai.data.Metrics;
import qube.qai.data.analysis.Statistics;
import qube.qai.data.TimeSequence;
import qube.qai.procedure.ProcedureChain;

/**
 * Created by rainbird on 11/28/15.
 */
public class TimeSequenceAnalysis extends ProcedureChain {

    public static String NAME = "Time-Sequence Analysis";

    private static String DESCRIPTION = "This is a procedure to analyze a given time series.";
    /**
     * this is a procedure to analyze a given time series
     * statistical:
     *          average value
     *          result value variance etc.
     * top 10/bottom 10/average entities- prepare those results as charts
     */
    public TimeSequenceAnalysis() {
        super(NAME);
    }

    @Override
    public void buildArguments() {
        description = DESCRIPTION;
        arguments = new Arguments(INPUT_TIME_SEQUENCE);
        arguments.putResultNames(TIME_SEQUENCE_METRICS);
    }

    @Override
    public void execute() {

        if (!arguments.isSatisfied()) {
            throw new RuntimeException("Process: " + name + " has not been initialized properly- missing argument");
        }

        // first get the selector
        TimeSequence timeSequence = (TimeSequence) arguments.getSelector(INPUT_TIME_SEQUENCE).getData();
        if (timeSequence == null) {
            logger.error("Input time-series has not been initialized properly: null value");
        }

        Number[] data = timeSequence.toArray();
        Statistics stats = new Statistics(data);
        Metrics metrics = stats.buildMetrics();
        log("adding '" + TIME_SEQUENCE_METRICS + "' to return values");
        arguments.addResult(TIME_SEQUENCE_METRICS, metrics);
    }
}