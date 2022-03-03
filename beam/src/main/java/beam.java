import org.apache.beam.sdk.io.kafka.*;
import org.apache.beam.sdk.*;
import org.apache.beam.sdk.io.kafka.KafkaIO.Read;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Values;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class beam {
	public static void main(String[] args) {
		Pipeline pipeline = Pipeline.create();
		Read<Long, String> kafkaReader = KafkaIO.<Long, String>read()
				.withBootstrapServers("localhost:9092")
				.withTopic("movielog20")
				.withKeyDeserializer(LongDeserializer.class)
				.withValueDeserializer(StringDeserializer.class);
		kafkaReader.withoutMetadata();
		pipeline.apply("Kafka Reader", kafkaReader)
				.apply("Extract Words", ParDo.of(new DoFn<KafkaRecord<Long, String>, String>() {
					@ProcessElement
					public void processElement(ProcessContext c){
						System.out.println(c.element().getKV().getValue());
					}
				}));
		pipeline.run();
	}
}
