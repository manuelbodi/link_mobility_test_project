/*
    Name of the project: link_mobility_test_project_project
    Name of the package: at.semriach.link_mobility_test_project_project.configurations
    Name of the file: Configurations
    Date of the creation: 12.09.24
    Time of the creation: 12:18
    Name of the creator: manuelbodlos
*/

package at.semriach.link_mobility_test_project_project.configurations;

import at.semriach.link_mobility_test_project_project.pojos.OutputLock;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configurations
{
    @Bean
    public Queue messageQueue()
    {
        return new Queue("messageQueue", false);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin()
    {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public MessageConverter jsonMessageConverter()
    {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("at.semriach.link_mobility_test_project_project.pojos");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public OutputLock outputLock()
    {
        return new OutputLock();
    }
}
