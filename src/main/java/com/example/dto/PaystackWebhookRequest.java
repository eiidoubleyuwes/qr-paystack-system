package com.example.dto;

public class PaystackWebhookRequest {
    private String event;
    private WebhookData data;

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }
    public WebhookData getData() { return data; }
    public void setData(WebhookData data) { this.data = data; }

    public static class WebhookData {
        private String reference;
        private int amount;
        private String paid_at;
        private String channel;
        private WebhookCustomer customer;
        private Object metadata;
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public int getAmount() { return amount; }
        public void setAmount(int amount) { this.amount = amount; }
        public String getPaid_at() { return paid_at; }
        public void setPaid_at(String paid_at) { this.paid_at = paid_at; }
        public String getChannel() { return channel; }
        public void setChannel(String channel) { this.channel = channel; }
        public WebhookCustomer getCustomer() { return customer; }
        public void setCustomer(WebhookCustomer customer) { this.customer = customer; }
        public Object getMetadata() { return metadata; }
        public void setMetadata(Object metadata) { this.metadata = metadata; }
    }
    public static class WebhookCustomer {
        private String first_name;
        private String last_name;
        private String email;
        private String phone;
        public String getFirst_name() { return first_name; }
        public void setFirst_name(String first_name) { this.first_name = first_name; }
        public String getLast_name() { return last_name; }
        public void setLast_name(String last_name) { this.last_name = last_name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
} 