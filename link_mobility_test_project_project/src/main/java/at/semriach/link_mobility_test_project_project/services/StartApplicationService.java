/*
    Name of the project: link_mobility_test_project_project
    Name of the package: at.semriach.link_mobility_test_project_project.services
    Name of the file: StartApplicationService
    Date of the creation: 12.09.24
    Time of the creation: 12:35
    Name of the creator: manuelbodlos
*/

package at.semriach.link_mobility_test_project_project.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StartApplicationService
{
    private final ProducerService producerService;
    private final ConsumerService consumerService;

    @PostConstruct
    public void init()
    {
        // producerService.startProducingMessages(0L);
        consumerService.startConsumingMessages();
    }
}
