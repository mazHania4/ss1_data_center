package ss1.ong.datacenter.common.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ss1.ong.datacenter.common.config.AppProperties;

/**
 *
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-08-30
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final AppProperties appProperties;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    private static final String THYMELEAF_VARIABLE_CODE = "code";
    private static final String THYMELEAF_VARIABLE_NAME_USER = "name";

    private static final String THYMELEAF_TEMPLATE_EMAIL_VERIFICATION = "EmailVerification";

    public void sendVerifyEmail(String participantEmail, String userFullName, Integer userId, String code) {
        Context context = new Context();

        // establece las variables que se usarán en la plantilla de correo
        context.setVariable(THYMELEAF_VARIABLE_CODE, code);
        context.setVariable(THYMELEAF_VARIABLE_NAME_USER, userFullName);

        // genera el contenido html del correo usando la plantilla thymeleaf
        String html = templateEngine.process(THYMELEAF_TEMPLATE_EMAIL_VERIFICATION, context);

        try {
            // construye el mensaje mime con el asunto y contenido html
            MimeMessage mimeMessage = buildMimeMessage(participantEmail, "Valida tu correo electrónico", html);

            // envía el correo
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // registra un error si falla la construcción o envío del mensaje
            log.error("Error al enviar el correo de registro aprobado a {}", participantEmail, e);
        } catch (MailException e) {
            // captura excepciones relacionadas con el envío del correo, pero no hace nada
            log.error("Error al enviar el correo de registro aprobado a {}", participantEmail, e);
        }
    }

    /**
     * Construye un mensaje MIME listo para ser enviado por correo electrónico.
     * 
     * Este método configura un mensaje con contenido HTML, destinatario, asunto
     * y remitente personalizado utilizando el nombre de la aplicación.
     *
     */
    private MimeMessage buildMimeMessage(String to, String subject, String htmlContent) throws MessagingException {
        // crea un nuevo mensaje mime vacío
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // envuelve el mensaje con un helper para configurar su contenido
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        // establece el contenido html del mensaje
        helper.setText(htmlContent, true);

        // define el destinatario del correo
        helper.setTo(to);

        // establece el asunto del correo
        helper.setSubject(subject);

        // define el remitente usando el nombre de la aplicación y el correo configurado
        helper.setFrom("SIE <" + appProperties.getMailFrom() + ">");

        // retorna el mensaje construido
        return mimeMessage;
    }

}
