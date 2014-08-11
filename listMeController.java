/** Controller for ListMe App. */
global with Sharing class ListMeController {

    /** Returns all events. */
    @RemoteAction
    global static ListMe_Event__c[] getEvents() {
        ListMe_Event__c[] events;
        if (Schema.SObjectType.ListMe_Event__c.isAccessible()) {
            events= [SELECT Name, Id, Waiting_Customers__c, Average_Wait_Time__c, Show_Wait_Time__c, Email_Position__c, Send_Email__c FROM ListMe_Event__c];
        }
        return events;
    }

    /** Get customers that are in an event with Id EVENTID. */
    @RemoteAction
    global static ListMe_Customer__c[] getActiveCustomers(Id eventId) {
        ListMe_Customer__c[] customers;
        if (Schema.SObjectType.ListMe_Customer__c.isAccessible()) {
            customers = [SELECT Name, Active__c, Id, Contact__c FROM ListMe_Customer__c WHERE Event__c =: eventId AND Active__c =: True ORDER BY Order__c ASC];
        }
        return customers;
    }

    /** Creates the CUSTOMERCONTACT for an event with Id EVENTID. */
    @RemoteAction
    global static ListMe_Customer__c createCustomer(Contact customerContact, Id eventId) {
        ListMe_Customer__c customer;
        if (Schema.SObjectType.Contact.isCreateable()) {
            insert customerContact;
        }
        if (Schema.SObjectType.Contact.isAccessible()) {
            customerContact = [SELECT Name FROM Contact WHERE Id =: customerContact.Id];
            customer = new ListMe_Customer__c(Name = customerContact.Name, Event__c = eventId, Contact__c = customerContact.Id);
        }
        if (Schema.SObjectType.ListMe_Customer__c.isAccessible()) {
            ListMe_Customer__c[] customerList = [SELECT Id FROM ListMe_Customer__c WHERE Event__c =: eventId];
            customer.Order__c = customerList.size();
        }
        if (Schema.SObjectType.ListMe_Customer__c.isCreateable()) {
            insert customer;
        }
        return customer;
    }

    /** Removes the customer with Id CUSTOMERID and determines if the DROPped. */
    @RemoteAction
    global static ListMe_Customer__c removeCustomer(Id customerId, Boolean drop) {
        ListMe_Event__c event;
        ListMe_Customer__c customer;
        ListMe_Customer__c[] customers;
        if (Schema.SObjectType.ListMe_Customer__c.isAccessible() && Schema.SObjectType.ListMe_Customer__c.isUpdateable()) {
            customer = [SELECT Name, CreatedDate, Wait_Time__c, Event__c, Contact__r.Email FROM ListMe_Customer__c WHERE Id =: customerId];
            customer.Wait_Time__c = (System.now().getTime() - customer.CreatedDate.getTime())/ 60000;
            customer.Active__c = false;
            customer.Dropped__c = drop;
            update customer;
        }
        if (Schema.SObjectType.ListMe_Event__c.isAccessible()) {
            event = [SELECT Id, Email_Position__c, Send_Email__c FROM ListMe_Event__c WHERE Id =: customer.Event__c];
        }
        customers = getActiveCustomers(event.Id);
        sendEmail(event, customers);
        return customer;
    }

    /** Determines whether or not to send an email. */ 
    private static void sendEmail(ListMe_Event__c event, ListMe_Customer__c[] customers) {
        Boolean send = false;
        Contact cont;
        if (event.Send_Email__c && customers.size() >= event.Email_Position__c && Schema.SObjectType.Contact.isAccessible()) {
            cont = [SELECT Email FROM Contact WHERE Id =: customers[(event.Email_Position__c - 1).intValue()].Contact__c];
            send = cont.Email != null;
        }
        if (send) {
            Messaging.reserveSingleEmailCapacity(1);
            Messaging.SingleEmailMessage mail = new Messaging.SingleEmailMessage();
            String[] toAddresses = new String[] {cont.Email};
            mail.setToAddresses(toAddresses);
            mail.setSenderDisplayName('Support');
            mail.setSubject('Reminder : Waitlist Position');
            mail.setBccSender(false);
            mail.setUseSignature(false);
            mail.setPlainTextBody('You are now position ' + event.Email_Position__c + ' on the waitlist.');
            Messaging.sendEmail(new Messaging.SingleEmailMessage[] { mail });
        }
    }

    /** Gets the signuptimes for the event with Id EVENTID. */
    @RemoteAction
    global static DateTime[] getSignUpTimes(Id eventId) {
        ListMe_Customer__c[] customers;
        if (Schema.SObjectType.ListMe_Customer__c.isAccessible()) {
            customers = [SELECT CreatedDate FROM ListMe_Customer__c WHERE Event__c =: eventId ORDER BY CreatedDate ASC];
        }
        DateTime[] times = new List<DateTime>();
        for (ListMe_Customer__c customer: customers) {
            times.add(customer.CreatedDate);
        }
        return times;
    }

    /** Gets the off times for the event with Id EVENTID. */
    @RemoteAction
    global static ListMe_Customer__c[] getOffTimes(Id eventId) {
        ListMe_Customer__c[] customers;
        if (Schema.SObjectType.ListMe_Customer__c.isAccessible()) {
            customers = [SELECT CreatedDate, Wait_Time__c FROM ListMe_Customer__c WHERE Event__c =: eventId AND Active__c = false AND Dropped__c = false ORDER BY CreatedDate ASC];
        }
        return customers;
    }

    /** Saves the setting on the event. */
    @RemoteAction
    global static void saveSetting(ListMe_Event__c event) {
        if (Schema.SObjectType.ListMe_Event__c.isUpdateable()) {
            update event;
        }
    }

    /** Returns the average wait time for event with Id EVENTID. */
    @RemoteAction
    global static Decimal getUpdatedTime(Id eventId) {
        ListMe_Event__c event;
        if (Schema.SObjectType.ListMe_Event__c.isAccessible()) {
            event = [SELECT Average_Wait_Time__c FROM ListMe_Event__c WHERE Id =: eventId];
        }
        return event.Average_Wait_Time__c;
    }
}