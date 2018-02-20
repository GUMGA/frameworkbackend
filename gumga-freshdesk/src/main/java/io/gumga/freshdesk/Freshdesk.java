package io.gumga.freshdesk;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Freshdesk implements Serializable {

    /**
     * Name of the requester
     */
    private String name;
    /**
     * User ID of the requester. For existing contacts,
     * the requester_id can be passed instead of the requester's email.
     */
    private Long requester_id;
    /**
     * 	Email address of the requester.
     * 	If no contact exists with this email address in Freshdesk,
     * 	it will be added as a new contact.
     */
    private String email;
    /**
     * Facebook ID of the requester.
     * If no contact exists with this facebook_id, then a new contact will be created.
     */
    private String facebook_id;
    /**
     * Phone number of the requester.
     * If no contact exists with this phone number in Freshdesk,
     * it will be added as a new contact.
     * If the phone number is set and the email address is not,
     * then the name attribute is mandatory.
     */
    private String phone;
    /**
     * Twitter handle of the requester.
     * If no contact exists with this handle in Freshdesk,
     * it will be added as a new contact.
     */
    private String twitter_id;
    /**
     * Subject of the ticket. The default Value is null.
     */
    private String subject;
    /**
     * Helps categorize the ticket according to the different kinds of issues your support team deals with.
     * The default Value is null.
     */
    private String type;
    /**
     * Status of the ticket. The default Value is 2.
     * 2 - Open
     * 3 - Pending
     * 4 - Resolved
     * 5 - Closed
     */
    private Long status = 2l;
    /**
     * Priority of the ticket. The default value is 1.
     * 1 - Low
     * 2 - Medium
     * 3 - High
     * 4 - Urgent
     */
    private Long priority = 1l;
    /**
     * HTML content of the ticket.
     */
    private String description;
    /**
     * ID of the agent to whom the ticket has been assigned

     */
    private Long responder_id;
    /**
     * Email address added in the 'cc' field of the incoming ticket email
     */
    private List<String> cc_emails;
    /**
     * Timestamp that denotes when the ticket is due to be resolved
     */
    private Date due_by;
    /**
     * ID of email config which is used for this ticket. (i.e., support@yourcompany.com/sales@yourcompany.com)
     * If product_id is given and email_config_id is not given, product's primary email_config_id will be set
     */
    private Long email_config_id;
    /**
     * Timestamp that denotes when the first response is due
     */
    private Date fr_due_by;
    /**
     * ID of the group to which the ticket has been assigned.
     * The default value is the ID of the group that is associated with the given email_config_id
     */
    private Long group_id;
    /**
     * ID of the product to which the ticket is associated.
     * It will be ignored if the email_config_id attribute is set in the request.
     */
    private Long product_id;
    /**
     * The channel through which the ticket was created. The default value is 2.
     */
    private Long source;
    /**
     * Tags that have been associated with the ticket
     */
    private List<String> tags;
    /**
     * Company ID of the requester.
     * This attribute can only be set if the Multiple Companies feature is enabled (Estate plan and above)
     */
    private Long company_id;

    protected Freshdesk() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRequester_id() {
        return requester_id;
    }

    public void setRequester_id(Long requester_id) {
        this.requester_id = requester_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getResponder_id() {
        return responder_id;
    }

    public void setResponder_id(Long responder_id) {
        this.responder_id = responder_id;
    }

    public List<String> getCc_emails() {
        return cc_emails;
    }

    public void setCc_emails(List<String> cc_emails) {
        this.cc_emails = cc_emails;
    }

    public Date getDue_by() {
        return due_by;
    }

    public void setDue_by(Date due_by) {
        this.due_by = due_by;
    }

    public Long getEmail_config_id() {
        return email_config_id;
    }

    public void setEmail_config_id(Long email_config_id) {
        this.email_config_id = email_config_id;
    }

    public Date getFr_due_by() {
        return fr_due_by;
    }

    public void setFr_due_by(Date fr_due_by) {
        this.fr_due_by = fr_due_by;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public static Builder build() {
        return new Builder(new Freshdesk());
    }

    public static class Builder {
        private Freshdesk freshdesk;

        public Builder(Freshdesk freshdesk) {
            this.freshdesk = freshdesk;
        }

        public Builder name(String name) {
            this.freshdesk.name = name;
            return this;
        }

        public Builder email(String email) {
            this.freshdesk.email = email;
            return this;
        }

        public Builder subject(String subject) {
            this.freshdesk.subject = subject;
            return this;
        }

        public Builder description(String description) {
            this.freshdesk.description = description;
            return this;
        }

        public Builder group(Long group){
            this.freshdesk.group_id = group;
            return this;
        }

        public Builder type(String type) {
            this.freshdesk.type = type;
            return this;
        }

        public Builder status(Long status) {
            this.freshdesk.status = status;
            return this;
        }

        public Builder priority(Long priority) {
            this.freshdesk.priority = priority;
            return this;
        }

        public Freshdesk builder() {
            Objects.requireNonNull(this.freshdesk.email);
            Objects.requireNonNull(this.freshdesk.subject);
            Objects.requireNonNull(this.freshdesk.description);
            Objects.requireNonNull(this.freshdesk.group_id);
            Objects.requireNonNull(this.freshdesk.type);
            return this.freshdesk;
        }
    }
}
