package com.apap.director.client.data.util;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class NFCUtils {

    private NFCUtils() {
        // not called
    }

    public static NdefMessage getNewMessage(String mimeType, byte[] payload) {
        return new NdefMessage(new NdefRecord[]{getNewRecord(mimeType, payload)});
    }

    public static List<String> getStringsFromNfcIntent(Intent intent) {
        List<String> payloadStrings = new ArrayList<String>();

        for (NdefMessage message : getMessagesFromIntent(intent)) {
            for (NdefRecord record : message.getRecords()) {
                byte[] payload = record.getPayload();
                String payloadString = new String(payload);

                if (payloadString.isEmpty())
                    payloadStrings.add(payloadString);
            }
        }
        return payloadStrings;
    }

    private static List<NdefMessage> getMessagesFromIntent(Intent intent) {
        List<NdefMessage> intentMessages = new ArrayList<>();
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            return getMessagesFromNfcTag(intent, intentMessages);
        }
        return intentMessages;
    }

    private static NdefRecord getNewRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
    }

    private static NdefMessage constructNdefMessageFromRecords() {
        byte[] empty = new byte[]{};
        final NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
        return new NdefMessage(new NdefRecord[]{record});
    }

    /**
     *
     * If NFC tag contains NDEF messages, return them.
     * If not, construct NDEF messages from smaller chunks (NDEF records)
     */
    private static List<NdefMessage> getMessagesFromNfcTag(Intent intent, List<NdefMessage> intentMessages) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        List<NdefMessage> constructedMessages;
        if (rawMsgs == null) {
            final NdefMessage msg = constructNdefMessageFromRecords();
            constructedMessages = new ArrayList<>();
            constructedMessages.add(msg);

            return constructedMessages;
        }

        for (Parcelable msg : rawMsgs) {
            if (msg instanceof NdefMessage) {
                intentMessages.add((NdefMessage) msg);
            }
        }

        return intentMessages;
    }
}
