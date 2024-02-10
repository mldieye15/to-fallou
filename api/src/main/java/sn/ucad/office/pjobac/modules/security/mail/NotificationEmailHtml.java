package sn.ucad.office.pjobac.modules.security.mail;

import lombok.*;

import java.util.Map;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEmailHtml {
    private String subject;
    private String recipient;
    private String body;
    private Map<String, Object> emailVariables;
    private String templateName;
}
