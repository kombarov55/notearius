package configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("repositories")
public class RootConfig {
	
	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("org.postgresql.Driver");
		ds.setUsername("postgres");
		ds.setPassword("root");
		ds.setUrl("jdbc:postgresql://127.0.0.1:5432/notearius");
		return ds;
	}

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
		sf.setDataSource(getDataSource());
		Properties props = new Properties();
//		props.setProperty("dialect", "org.hibernate.dialect.PostgreSQL94Dialect");
//		props.setProperty("connection.pool_size", "3");
//		props.setProperty("show_sql", "true");
		props.setProperty("dialect", "org.hibernate.dialect.PostgreSQLDialect");
		props.setProperty("show_sql", "true");
		sf.setHibernateProperties(props);
		sf.setAnnotatedClasses(beans.Note.class, beans.User.class);
		return sf;
		
	}
	
}
