package net.rationalminds.es;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import net.rationalminds.es.util.Helper;

	

/**
 * AcpectJ based implementation of {@link EnvironmentalControl}. This class contains logic to invoke desired methods as 
 * environment switch using reflection.
 * @author Vaibhav Pratap SIngh
 *
 */
@Aspect
public class EnvironmentControlImpl {
	
	private static final List<String> ALLOWED_ARGS= Arrays.asList("args","this","");
	
	/**
	 * Returns execution result of switched method.
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(EnvironmentalControl)")
	public Object switchEnrvironment(ProceedingJoinPoint jp) throws Throwable {
		if(Helper.isEnvSwitchEnabled() == false) {
			return jp.proceed();
		}
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		EnvironmentalControl annotation=method.getAnnotation(EnvironmentalControl.class);

		String value = null;
		if(Helper.isdevEnvironment()) {
			value = annotation.devMethod();
		}
		if(Helper.isTestEnvironment()) {
			value = annotation.testMethod();
		}
		
		if(value == null) {
			System.out.println("no alternate methods defined, using regular method");
			return jp.proceed();
		}
		
		value= annotation.devMethod();
		String className = value.substring(0, value.lastIndexOf('.'));
		String methodName = value.substring(value.lastIndexOf('.')+1,value.indexOf("("));
		
		int startBrace = value.indexOf("(");
		int endBrace = value.indexOf(")");
		
		String arg="";
		
		if(endBrace - startBrace > 1) {
			arg = value.substring(startBrace+1, endBrace);
		}
		
		if(ALLOWED_ARGS.contains(arg)) {
			Class clazz = jp.getTarget().getClass().getClassLoader().loadClass(className);
			Object object = clazz.newInstance();
			if(arg.equals("this")) {
				Method m = clazz.getDeclaredMethod(methodName,new Class[]{Object.class});
				m.setAccessible(true);
				return m.invoke(object, new Object[]{jp.getTarget()});
			}
			if(arg.equals("args")) {
				Object args[]=jp.getArgs();
				Class c[] = new Class[args.length];
				int i=0;
				for(Object a:args) {
					c[i++]=a.getClass();
				}
				Method m = clazz.getDeclaredMethod(methodName, c);
				m.setAccessible(true);
				return m.invoke(object, args);
			}
			Method m = clazz.getDeclaredMethod(methodName, (Class[])null);
			m.setAccessible(true);
			return m.invoke(object, (Object[]) null);
		}
		else {
			throw new Exception("supported arguments are "+ALLOWED_ARGS.toString());
		}	
	}
	
}
