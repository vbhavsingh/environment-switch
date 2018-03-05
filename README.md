# environment-switch


Many a time we feel that there should be a way by which we can invoke different functionality in a flow depending on the environment.

How great it would be if in development environment one can just mock the database or any other external call and return dummy data for debugging purpose. However. , there are existing frameworks for testing like Mockito which let one simulate external objects by mocking return objects. Spring profile is also helpful to an extent by creating different profiles. All these existing frameworks are good for either testing or running code in any live environment, but for the desktop IDE based environment it is too much work to set them up.

I wanted a simple approach that there should be some library which I can include and annotate my code to use a different method instead of real one in "test" and "development" environment. So I created one which is easy to use with no code pollution for myself. Since I find it good to use, I am sharing it with all.


Below is an example of using the library, to annotate code. Note the example has "devMethod", but one can also use "testMethod", this there are two alternates that are allowed.

@EnvironmentalControl(devMethod="com.log.server.util.DevEnvironmentMocker.getClients()")
public List<AgentModel> getClients(DaoSearchModel model) {
    String query = createQuery(model);
    Log.info("query to fetch matching clients : {}", query);
    return jdbcTemplate.query(query, new ClientModelRowMapper());
}


Below is the equivalent code that will get executed instead of the original method.

public class DevEnvironmentMocker {
......
public Object getClients() {
        AgentModel a1 = new AgentModel();
        a1.setClientConnectPort(1234);
        a1.setClientLabels(Arrays.asList("test"));
        a1.setClientName("test-1.rm.net");
        a1.setClientNode("test-1.rm.net");
        
        AgentModel a2 = new AgentModel();
        a1.setClientConnectPort(1234);
        a1.setClientLabels(Arrays.asList("test"));
        a1.setClientName("test-2.rm.net");
        a1.setClientNode("test-2.rm.net");
        
        System.out.println("returning mocked values");
        return Arrays.asList(a1, a2);
    }
}


At the time one needs to reuse the arguments passed to the original method to emulate behavior more dynamically. I have taken care of that situation by allowing access to arguments as java.lang.Object array. You can guide annotation to use this as :

@EnvironmentalControl(devMethod="com.log.server.util.DevEnvironmentMocker.getClients(args)")


Once you use this, make sure your target method has the following syntax. You can use the arguments in the order they are passed in the original method.

public class DevEnvironmentMocker {
public Object getClients(Object[] args) {
    DaoSearchModel dm = (DaoSearchModel) args[0];
    ..........
    ..........
    return Arrays.asList(a1, a2);
}

}


One may also need to access local variables of the parent class itself. To achieve this use annotation like following.

@EnvironmentalControl(devMethod="com.log.server.util.DevEnvironmentMocker.getClients(this)")


Now you can access object of which the original object is member of, like following.

public class DevEnvironmentMocker {
public Object getClients(Object object) {
    MyDao md = (MyDao) object;
    ..........
    ..........
    return Arrays.asList(a1, a2);
}

}


How to include ?
This utility is based upon AspectJ. For the convenience of users, There are two ways one can use this library on their pom.xml  as a dependency.

Use full jar with packaged AspectJ support as follows

<dependency>
  <groupId>net.rationalminds</groupId>
  <artifactId>environment-switch</artifactId>
  <version>0.0.1</version>
  <classifier>jar-with-dependencies</classifier>
</dependency>


Use AspectJ library separate, in case you have your own version dependency on Aspect.

<dependency>
  <groupId>net.rationalminds</groupId>
  <artifactId>environment-switch</artifactId>
  <version>0.0.1</version>
</dependency>


Once the dependency is on your path, you need to include these two parameters in your startup VM options.

-Denv.switch=dev -javaagent:{full-path}/environment-switch-jar-with-dependencies.jar


Do not use these options in production environment. The allowed values for "env.switch" are "dev", "development", "test" and "testing".
