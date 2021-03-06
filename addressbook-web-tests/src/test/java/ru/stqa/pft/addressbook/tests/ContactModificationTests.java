package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class ContactModificationTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        Groups groups = app.db().groups();
        if (groups.size() == 0) {
            GroupData group = new GroupData().withName("new");
            app.goTo().groupPage();
            app.group().create(group);
            groups = app.db().groups();
        }
        if (app.db().contacts().size() == 0) {
            if (app.contact().all().size() == 0) {
                app.contact().create(new ContactData().withFirstname("smit").withLastname("grey").withMobilePhone("89296173544")
                        .withEmail("mamba@mail.ru").withPhoto(new File("src/test/resources/stru.png")).inGroup(groups.iterator().next()));
            }
        }
    }

    @Test
    public void testContactModification() {
        Contacts before = app.db().contacts();
        ContactData modifiedContact = before.iterator().next();
        ContactData contact = new ContactData().withId(modifiedContact.getId()).withFirstname("smit").withLastname("grey")
                .withMobilePhone("89296173544").withEmail("mamba@mail.ru").inGroup(app.db().groups().iterator().next())
                .withPhoto(new File("src/test/resources/stru.png"));
        app.contact().modify(contact);
        Contacts after = app.db().contacts();
        assertEquals(after.size(), before.size());
        assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
        verifyContactListInUi();
    }
}