//package com.apap.director.client.component;
//
//import com.apap.director.client.activity.AddContactActivity;
//import com.apap.director.client.activity.LoginActivity;
//import com.apap.director.client.activity.NewMsgActivity;
//import com.apap.director.client.activity.SingleContactActivity;
//import com.apap.director.client.fragment.ContactsFragment;
//import com.apap.director.client.fragment.InboxFragment;
//import com.apap.director.db.dao.module.DaoModule;
//import com.apap.director.network.rest.service.UserService;
//
//import javax.inject.Singleton;
//
//import dagger.Component;
//import io.realm.Realm;
//
//@Singleton
//@Component(modules = {DaoModule.class})
//public interface DaoComponent {
//    //DatabaseManager databaseManager();
//    Realm realm();
//    void inject(AddContactActivity addContactActivity);
//    void inject(ContactsFragment contactsFragment);
//    void inject(SingleContactActivity singleContactActivity);
//    void inject(InboxFragment inboxFragment);
//    void inject(NewMsgActivity newMsgActivity);
//    void inject(LoginActivity loginActivity);
//}
