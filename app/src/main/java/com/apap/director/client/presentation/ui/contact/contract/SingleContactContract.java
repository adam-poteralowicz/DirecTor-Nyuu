package com.apap.director.client.presentation.ui.contact.contract;

import android.database.Cursor;
import android.graphics.BitmapFactory;

import com.apap.director.client.presentation.ui.base.contract.presenter.BasePresenter;
import com.apap.director.client.presentation.ui.base.contract.view.BaseView;

import java.util.List;

/**
 * Created by Adam on 2017-07-03.
 */

public interface SingleContactContract {

    interface View extends BaseView {
        void showAvatar(String imagePath, BitmapFactory.Options options);
    }

    interface Presenter extends BasePresenter {
        void checkAvatar(BitmapFactory.Options options, Long contactId);
        void initOptions(List<String> options);
        void getAvatar(Cursor cursor, String[] filePath, String contactName, BitmapFactory.Options options);
    }
}
