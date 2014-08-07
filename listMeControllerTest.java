@isTest
public class listMeControllerTest {

    static Id setup() {
        ListMe_Event__c event = new ListMe_Event__c(Name = 'First Event');
        event.Show_Wait_Time__c = true;
        insert event;
        ListMe_Customer__c customer = new ListMe_Customer__c(Name = 'David', Event__c = event.Id, Active__c = true);
        ListMe_Customer__c customer2 = new ListMe_Customer__c(Name = 'David2', Event__c = event.Id, Active__c = false);
        insert customer;
        insert customer2;
        return event.Id;
    }

    static testmethod void testGetEvents() {
        setup();
        ListMe_Event__c[] events = listMeController.getEvents();
        System.assertEquals(1, events.size());
        System.assertEquals('First Event', events[0].Name);
    }

    static testmethod void testGetCustomers() {
        Id eventId = setup();
        ListMe_Customer__c[] customers = listMeController.getActiveCustomers(eventId);
        System.assertEquals(1, customers.size());
    }

    static testmethod void testCreateCustomer() {
        Id eventId = setup();
        Contact cont = new Contact(FirstName='Henry', LastName='Lee');
        listMeController.createCustomer(cont, eventId);
        ListMe_Customer__c[] customers = listMeController.getActiveCustomers(eventId);
        System.assertEquals(2, customers.size());
    }

    static testmethod void testRemoveCustomer() {
        Id eventId = setup();
        ListMe_Customer__c[] customers = listMeController.getActiveCustomers(eventId);
        System.assertEquals(1, customers.size());
        listMeController.removeCustomer(customers[0].Id, false);
        customers = listMeController.getActiveCustomers(eventId);
        System.assertEquals(0, customers.size());
    }
}