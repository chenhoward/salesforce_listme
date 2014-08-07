@isTest
public class listMeControllerTest {

    static Id setup() {
        ListMe_Event__c event = new ListMe_Event__c(Name = 'First Event');
        event.Show_Wait_Time__c = true;
        insert event;
        return event.Id;
    }

    static testmethod void testGetEvents() {
        setup();
        ListMe_Event__c[] events = listMeController.getEvents();
        System.assertEquals(1, events.size());
        System.assertEquals('First Event', events[0].Name);
    }
}