import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.GenerateSequence;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

public class GenerateSequenceToGCS {

    public interface Options extends PipelineOptions, DataflowPipelineOptions {

        @Description("Output GCS Path")
        String getOutputGCSPath();
        void setOutputGCSPath(String value);
    }

    public static class FormatFn extends DoFn<Long, String> {

        @ProcessElement
        public void processElement(@Element Long number, OutputReceiver<String> out) {

            out.output(number.toString());
        }
    }

    public static void main(String[] args) {

        Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);

        Pipeline pipeline = Pipeline.create(options);

        PCollection<Long> input = pipeline.apply("GenerateSequence", GenerateSequence.from(0L).to(100L));

        PCollection<String> formatted = input.apply("Format", ParDo.of(new FormatFn()));

        formatted.apply("Write To GCS", TextIO.write().to(options.getOutputGCSPath()));

        pipeline.run();
    }
}