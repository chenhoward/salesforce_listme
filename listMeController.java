/** Controller for ListMe App. */
global class ListMeController {

    /** Returns all events. */
    @RemoteAction
    global static ListMe_Event__c[] getEvents() {
        ListMe_Event__c[] events= [SELECT Name, Id, Waiting_Customers__c, Average_Wait_Time__c, Show_Wait_Time__c FROM ListMe_Event__c];
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
        ListMe_Customer__c customer;
        if (Schema.SObjectType.ListMe_Customer__c.isAccessible()) {
            customer = [SELECT Name, CreatedDate, Wait_Time__c FROM ListMe_Customer__c WHERE Id =: customerId];
            customer.Wait_Time__c = (System.now().getTime() - customer.CreatedDate.getTime())/ 60000;
            customer.Active__c = false;
            customer.Dropped__c = drop;
            update customer;
        }
        return customer;
    }

    /** Gets the signuptimes for the event with Id EVENTID. */
    @RemoteAction
    global static DateTime[] getSignUpTimes(Id eventId) {
        ListMe_Customer__c[] customers = [SELECT CreatedDate FROM ListMe_Customer__c WHERE Event__c =: eventId ORDER BY CreatedDate ASC];
        DateTime[] times = new List<DateTime>();
        for (ListMe_Customer__c customer: customers) {
            times.add(customer.CreatedDate);
        }
        return times;
    }

    /** Gets the off times for the event with Id EVENTID. */
    @RemoteAction
    global static ListMe_Customer__c[] getOffTimes(Id eventId) {
        ListMe_Customer__c[] customers = [SELECT CreatedDate, Wait_Time__c FROM ListMe_Customer__c WHERE Event__c =: eventId AND Active__c = false AND Dropped__c = false ORDER BY CreatedDate ASC];
        return customers;
    }
}