package com.apap.director.client.presentation.ui.message.di.module;

import com.apap.director.client.domain.repository.MessageRepository;
import com.apap.director.client.presentation.di.scope.Activity;
import com.apap.director.client.presentation.ui.message.contract.NewMsgContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adam on 2017-07-04.
 */

@Module
public class NewMsgContractModule {

    private NewMsgContract.View view;
    private MessageRepository messageRepository;
    private Long id;

    public NewMsgContractModule(NewMsgContract.View view, MessageRepository messageRepository, Long id) {
        this.view = view;
        this.messageRepository = messageRepository;
        this.id = id;
    }

    @Activity
    @Provides
    public NewMsgContract.View provideView() {
        return view;
    }

    @Activity
    @Provides
    public MessageRepository provideMessageRepository() {
        return messageRepository;
    }

    @Activity
    @Provides
    public Long provideLong() { return id; }
}
