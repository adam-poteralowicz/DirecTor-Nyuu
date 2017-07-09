package com.apap.director.client.domain.interactor.message;

import com.apap.director.client.data.net.service.WebSocketService;
import com.apap.director.client.data.net.to.MessageTO;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.util.EncryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.whispersystems.libsignal.DuplicateMessageException;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.InvalidVersionException;
import org.whispersystems.libsignal.LegacyMessageException;
import org.whispersystems.libsignal.NoSessionException;
import org.whispersystems.libsignal.UntrustedIdentityException;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * Created by Alicja Michniewicz
 */

public class GetMessageStreamInteractor extends BaseInteractor<MessageModel, AccountModel> {

    private WebSocketService webSocketService;
    private EncryptionService encryptionService;
    private CreateMessageInteractor createMessageInteractor;

    @Inject
    public GetMessageStreamInteractor(WebSocketService webSocketService, EncryptionService encryptionService, CreateMessageInteractor createMessageInteractor) {
        this.webSocketService = webSocketService;
        this.encryptionService = encryptionService;
        this.createMessageInteractor = createMessageInteractor;
    }

    @Override
    protected Observable<MessageModel> buildObservable(AccountModel account) {

        return webSocketService.getMessages(account.getCookie())
                .flatMap(this::mapToMessage);
    }

    private Observable<MessageModel> mapToMessage(StompMessage stompMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MessageTO messageTO = mapper.readValue(stompMessage.getPayload(), MessageTO.class);

        return createMessageInteractor.execute(messageTO)
            .map(message -> decryptMessage(message, messageTO.getType()));
    }

    private MessageModel decryptMessage(MessageModel message, int messageType) throws InvalidKeyIdException, InvalidKeyException, LegacyMessageException, InvalidVersionException, InvalidMessageException, DuplicateMessageException, NoSessionException, UntrustedIdentityException {
        return encryptionService.decryptMessage(message.getConversation().getContact().getContactKey(), message, messageType);
    }
}

