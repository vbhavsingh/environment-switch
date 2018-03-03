package net.rationalminds.es;

import java.lang.instrument.Instrumentation;
import org.aspectj.weaver.loadtime.Agent;
import net.rationalminds.es.util.Helper;

/**
 * This class is wrapper for {@link org.aspectj.weaver.loadtime.Agent}. To activate use -javaagent:{path}/
 * @author Vaibhav Pratap Singh
 *
 */
public class EnvSwitch extends Agent {
	public EnvSwitch() {
		super();
	}

	public static void premain(String options, Instrumentation instrumentation) {
		Helper.print();
		Agent.premain(options, instrumentation);
	}
	

}

