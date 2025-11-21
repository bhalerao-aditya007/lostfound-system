// ============================================================================
// FILE: src/com/lostfound/notification/NotificationService.java
// LOCATION: Copy this to src/com/lostfound/notification/NotificationService.java
// ============================================================================

package com.lostfound.notification;

import com.lostfound.model.Match;
import com.lostfound.model.LostItem;
import com.lostfound.model.FoundItem;

/**
 * Interface for notification services.
 *
 * This interface defines the contract for notification implementations.
 * Current implementation: ConsoleNotificationService (prints to console)
 *
 * FUTURE IMPLEMENTATIONS:
 * -----------------------
 * - EmailNotificationService: Send email notifications using JavaMail API
 * - SmsNotificationService: Send SMS notifications using Twilio/similar
 * - PushNotificationService: Send push notifications to mobile apps
 * - SlackNotificationService: Post notifications to Slack channels
 *
 * USAGE:
 * ------
 * NotificationService notifier = new ConsoleNotificationService();
 * notifier.notifyMatch(match, lostItem, foundItem);
 *
 * TO IMPLEMENT EMAIL NOTIFICATIONS:
 * ----------------------------------
 * 1. Create EmailNotificationService.java
 * 2. Implement this interface
 * 3. Use JavaMail API or SMTP to send emails
 * 4. Add SMTP configuration to app.properties
 * 5. Change Main.java to use EmailNotificationService instead
 */
public interface NotificationService {

    /**
     * Send notification about a match between a lost item and a found item.
     *
     * This method is called whenever the system finds a potential match
     * between a reported lost item and a reported found item.
     *
     * @param match The Match object containing match details (ID, score, status)
     * @param lostItem The LostItem that was reported as lost
     * @param foundItem The FoundItem that was reported as found
     */
    void notifyMatch(Match match, LostItem lostItem, FoundItem foundItem);

    /**
     * OPTIONAL: Send notification when an item status changes
     * Uncomment and implement if needed
     */
    // void notifyStatusChange(int itemId, String itemType, String newStatus);

    /**
     * OPTIONAL: Send reminder notification for pending matches
     * Uncomment and implement if needed
     */
    // void sendReminder(Match match, LostItem lostItem, FoundItem foundItem);
}

// ============================================================================
// EXAMPLE: EmailNotificationService Implementation (FUTURE)
// ============================================================================
/*
package com.lostfound.notification;

import com.lostfound.model.Match;
import com.lostfound.model.LostItem;
import com.lostfound.model.FoundItem;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailNotificationService implements NotificationService {

    private String smtpHost;
    private String smtpPort;
    private String smtpUser;
    private String smtpPassword;
    private String fromEmail;

    public EmailNotificationService() {
        // Load SMTP configuration from app.properties
        this.smtpHost = DbConnection.getProperty("smtp.host");
        this.smtpPort = DbConnection.getProperty("smtp.port");
        this.smtpUser = DbConnection.getProperty("smtp.user");
        this.smtpPassword = DbConnection.getProperty("smtp.password");
        this.fromEmail = DbConnection.getProperty("smtp.from");
    }

    @Override
    public void notifyMatch(Match match, LostItem lostItem, FoundItem foundItem) {
        // Send email to person who lost the item
        sendEmail(
            lostItem.getContactInfo(),
            "Match Found - Your Lost Item",
            createEmailBody(match, lostItem, foundItem, true)
        );

        // Send email to person who found the item
        sendEmail(
            foundItem.getFinderInfo(),
            "Match Found - Item You Found",
            createEmailBody(match, lostItem, foundItem, false)
        );
    }

    private void sendEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            SimpleLogger.log("Email sent to: " + toEmail);
        } catch (MessagingException e) {
            SimpleLogger.error("Failed to send email: " + e.getMessage());
        }
    }

    private String createEmailBody(Match match, LostItem lostItem, FoundItem foundItem, boolean isLostOwner) {
        StringBuilder body = new StringBuilder();
        body.append("Dear User,\n\n");
        body.append("A potential match has been found!\n\n");
        body.append("Match Score: ").append(match.getScore()).append("/100\n");
        body.append("Status: ").append(match.getStatus()).append("\n\n");

        if (isLostOwner) {
            body.append("Your Lost Item:\n");
            body.append("  - Item: ").append(lostItem.getItemName()).append("\n");
            body.append("  - Color: ").append(lostItem.getColor()).append("\n");
            body.append("  - Location: ").append(lostItem.getLocation()).append("\n\n");
            body.append("Found Item Details:\n");
            body.append("  - Item: ").append(foundItem.getItemName()).append("\n");
            body.append("  - Color: ").append(foundItem.getColor()).append("\n");
            body.append("  - Location: ").append(foundItem.getLocation()).append("\n");
            body.append("  - Finder Contact: ").append(foundItem.getFinderInfo()).append("\n\n");
        } else {
            body.append("Item You Found:\n");
            body.append("  - Item: ").append(foundItem.getItemName()).append("\n");
            body.append("  - Color: ").append(foundItem.getColor()).append("\n");
            body.append("  - Location: ").append(foundItem.getLocation()).append("\n\n");
            body.append("Lost Item Details:\n");
            body.append("  - Item: ").append(lostItem.getItemName()).append("\n");
            body.append("  - Color: ").append(lostItem.getColor()).append("\n");
            body.append("  - Location: ").append(lostItem.getLocation()).append("\n");
            body.append("  - Owner Contact: ").append(lostItem.getContactInfo()).append("\n\n");
        }

        body.append("Please contact each other to verify the match.\n\n");
        body.append("Thank you,\n");
        body.append("Lost & Found System");

        return body.toString();
    }
}
*/
