/** Controller for ListMe App. */
global class ListMeController {

    /** Returns all events. */
    @RemoteAction
    global static ListMe_Event__c[] getEvents() {
    	ListMe_Event__c[] events= [SELECT Name, Id FROM ListMe_Event__c];
    	return events;
    }

    @RemoteAction
    /** Get customers that are in an event with Id EVENTID. */
    global static ListMe_Customer__c[] getCustomers(Id eventId) {
        ListMe_Customer__c[] customers = [SELECT Name, Status__c, Id, Lead__c FROM ListMe_Customer__c WHERE Event__c =: eventId ORDER BY Order__c ASC];
        return customers;
    }
}