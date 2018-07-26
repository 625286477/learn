## spring-boot 启动源码分析（1.4.3.RELEASE）
### spring-boot 启动
启动入口
```
@SpringBootApplication
public class Starter extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
```
SpringApplication 中的 run 方法
```
public static ConfigurableApplicationContext run(Object source, String... args) {
	return run(new Object[] { source }, args);
}

public static ConfigurableApplicationContext run(Object[] sources, String[] args) {
    // 这里 new 出来一个 SpringApplication 并执行了 run 方法
	return new SpringApplication(sources).run(args);
}

public SpringApplication(Object... sources) {
	initialize(sources);
}

private void initialize(Object[] sources) {
	if (sources != null && sources.length > 0) {
		this.sources.addAll(Arrays.asList(sources));
	}
	// 判断是否是 web 环境
	this.webEnvironment = deduceWebEnvironment();
	// 获取 ApplicationContextInitializer
	setInitializers((Collection) getSpringFactoriesInstances(
			ApplicationContextInitializer.class));
	// 获取 ApplicationListener
	setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
	// 获取 MainApplicationClass
	this.mainApplicationClass = deduceMainApplicationClass();
}

private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type) {
	return getSpringFactoriesInstances(type, new Class<?>[] {});
}

private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type,
		Class<?>[] parameterTypes, Object... args) {
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	// Use names and ensure unique to protect against duplicates
	// 这里根据 type 来获取 spring.factories 文件中对应的类,参见 SpringFactoriesLoader
	Set<String> names = new LinkedHashSet<String>(
			SpringFactoriesLoader.loadFactoryNames(type, classLoader));
	// 创建实例 names 对应的实例
	List<T> instances = createSpringFactoriesInstances(type, parameterTypes,
			classLoader, args, names);
	// 对 instances 执行排序
	AnnotationAwareOrderComparator.sort(instances);
	return instances;
}

public ConfigurableApplicationContext run(String... args) {
	StopWatch stopWatch = new StopWatch();
	stopWatch.start();
	ConfigurableApplicationContext context = null;
	FailureAnalyzers analyzers = null;
	configureHeadlessProperty();
	// 获取 spring.factories 中的 SpringApplicationRunListener
	SpringApplicationRunListeners listeners = getRunListeners(args);
	listeners.started();
	try {
		ApplicationArguments applicationArguments = new DefaultApplicationArguments(
				args);
		ConfigurableEnvironment environment = prepareEnvironment(listeners,
				applicationArguments);
		Banner printedBanner = printBanner(environment);
		context = createApplicationContext();
		analyzers = new FailureAnalyzers(context);
		prepareContext(context, environment, listeners, applicationArguments,
				printedBanner);
		refreshContext(context);
		afterRefresh(context, applicationArguments);
		listeners.finished(context, null);
		stopWatch.stop();
		if (this.logStartupInfo) {
			new StartupInfoLogger(this.mainApplicationClass)
					.logStarted(getApplicationLog(), stopWatch);
		}
		return context;
	}
	catch (Throwable ex) {
		handleRunFailure(context, listeners, analyzers, ex);
		throw new IllegalStateException(ex);
	}
}

private SpringApplicationRunListeners getRunListeners(String[] args) {
	Class<?>[] types = new Class<?>[] { SpringApplication.class, String[].class };
	return new SpringApplicationRunListeners(logger, getSpringFactoriesInstances(
			SpringApplicationRunListener.class, types, this, args));
}


```

### SpringFactoriesLoader

```
public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) {
    String factoryClassName = factoryClass.getName();
    try {
        Enumeration ex = classLoader != null?classLoader.getResources("META-INF/spring.factories"):ClassLoader.getSystemResources("META-INF/spring.factories");
        ArrayList result = new ArrayList();
        while(ex.hasMoreElements()) {
            URL url = (URL)ex.nextElement();
            Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
            String factoryClassNames = properties.getProperty(factoryClassName);
            result.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(factoryClassNames)));
        }
        return result;
    } catch (IOException var8) {
        throw new IllegalArgumentException("Unable to load [" + factoryClass.getName() + "] factories from location [" + "META-INF/spring.factories" + "]", var8);
    }
}
```

### spring-boot META-INF/spring.factories 文件内容
```
# PropertySource Loaders
org.springframework.boot.env.PropertySourceLoader=\
org.springframework.boot.env.PropertiesPropertySourceLoader,\
org.springframework.boot.env.YamlPropertySourceLoader

# Run Listeners
org.springframework.boot.SpringApplicationRunListener=\
org.springframework.boot.context.event.EventPublishingRunListener

# Application Context Initializers
org.springframework.context.ApplicationContextInitializer=\
org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer,\
org.springframework.boot.context.ContextIdApplicationContextInitializer,\
org.springframework.boot.context.config.DelegatingApplicationContextInitializer,\
org.springframework.boot.context.web.ServerPortInfoApplicationContextInitializer

# Application Listeners
org.springframework.context.ApplicationListener=\
org.springframework.boot.ClearCachesApplicationListener,\
org.springframework.boot.builder.ParentContextCloserApplicationListener,\
org.springframework.boot.context.FileEncodingApplicationListener,\
org.springframework.boot.context.config.AnsiOutputApplicationListener,\
org.springframework.boot.context.config.ConfigFileApplicationListener,\
org.springframework.boot.context.config.DelegatingApplicationListener,\
org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener,\
org.springframework.boot.logging.ClasspathLoggingApplicationListener,\
org.springframework.boot.logging.LoggingApplicationListener

# Environment Post Processors
org.springframework.boot.env.EnvironmentPostProcessor=\
org.springframework.boot.cloud.CloudFoundryVcapEnvironmentPostProcessor,\
org.springframework.boot.env.SpringApplicationJsonEnvironmentPostProcessor

# Failure Analyzers
org.springframework.boot.diagnostics.FailureAnalyzer=\
org.springframework.boot.diagnostics.analyzer.BeanCurrentlyInCreationFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.BeanNotOfRequiredTypeFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.BindFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.ConnectorStartFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.NoUniqueBeanDefinitionFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.PortInUseFailureAnalyzer,\
org.springframework.boot.diagnostics.analyzer.ValidationExceptionFailureAnalyzer

# FailureAnalysisReporters
org.springframework.boot.diagnostics.FailureAnalysisReporter=\
org.springframework.boot.diagnostics.LoggingFailureAnalysisReporter

```

### @SpringBootApplication 
该注解结合了 @SpringBootConfiguration @EnableAutoConfiguration @ComponentScan
```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
    Class<?>[] exclude() default {};

    String[] excludeName() default {};

    @AliasFor(
        annotation = ComponentScan.class,
        attribute = "basePackages"
    )
    String[] scanBasePackages() default {};

    @AliasFor(
        annotation = ComponentScan.class,
        attribute = "basePackageClasses"
    )
    Class<?>[] scanBasePackageClasses() default {};
}
```

