/** Controller for ListMe App. */
global class ListMeController {

    /** Returns all events. */
    @RemoteAction
    global static ListMe_Event__c[] getEvents() {
        ListMe_Event__c[] events= [SELECT Name, Id FROM ListMe_Event__c];
        return events;
    }

    /** Get customers that are in an event with Id EVENTID. */
    @RemoteAction
    global static ListMe_Customer__c[] getCustomers(Id eventId) {
        ListMe_Customer__c[] customers = [SELECT Name, Active__c, Id, Contact__c FROM ListMe_Customer__c WHERE Event__c =: eventId ORDER BY Order__c ASC];
        return customers;
    }

    @RemoteAction
    global static ListMe_Customer__c createCustomer(String firstName, String lastName, Id eventId) {
        Contact customerContact = new Contact(firstName = firstName, lastName = lastName);
        if (Schema.SObjectType.Contact.isCreateable()) {
            insert customerContact;
        }
        ListMe_Customer__c customer = new ListMe_Customer__c(Name = customerContact.Name);
        if (Schema.SObjectType.ListMe_Customer__c.isAccessible()) {
            ListMe_Customer__c[] customerList = [SELECT Id FROM ListMe_Customer__c WHERE Event__c =: eventId];
            customer.Order__c = customerList.size();
        }
        if (Schema.SObjectType.ListMe_Customer__c.isCreateable()) {
            insert customer;
        }
        return customer;
    }
    
}