/*
    Name of the project: link_mobility_test_project_project
    Name of the package: at.semriach.link_mobility_test_project_project.pojos
    Name of the file: Message
    Date of the creation: 12.09.24
    Time of the creation: 12:17
    Name of the creator: manuelbodlos
*/

package at.semriach.link_mobility_test_project_project.pojos;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {})
@ToString(exclude = {})
public class Message implements Serializable
{
    private UUID id;
    private String sender;
    private String recipient;
    private String message;
}
