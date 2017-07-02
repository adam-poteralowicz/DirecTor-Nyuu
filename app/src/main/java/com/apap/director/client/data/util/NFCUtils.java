package com.apap.director.client.data.util;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
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

    public static boolean writeMessageToTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Log.e(NFCUtils.class.toString(), "Error : Tag is not writable");
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Log.e(NFCUtils.class.toString(), "Error : Message exceeds the max tag size " + ndef.getMaxSize());
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;
            } else {
                return provideNdefOperations(tag, message);
            }
        } catch (Exception e) {
            Log.getStackTraceString(e);
            return false;
        }
    }

    public static List<String> getStringsFromNfcIntent(Intent intent) {
        List<String> payloadStrings = new ArrayList<String>();

        for (NdefMessage message : getMessagesFromIntent(intent)) {
            for (NdefRecord record : message.getRecords()) {
                byte[] payload = record.getPayload();
                String payloadString = new String(payload);

                if (!TextUtils.isEmpty(payloadString))
                    payloadStrings.add(payloadString);
            }
        }
        return payloadStrings;
    }

    private static List<NdefMessage> getMessagesFromIntent(Intent intent) {
        List<NdefMessage> intentMessages = new ArrayList<>();
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs == null) {
                byte[] empty = new byte[]{};
                final NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                final NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                intentMessages = new ArrayList<>();
                intentMessages.add(msg);

                return intentMessages;
            }

            for (Parcelable msg : rawMsgs) {
                if (msg instanceof NdefMessage) {
                    intentMessages.add((NdefMessage) msg);
                }
            }
        }
        return intentMessages;
    }

    private static NdefRecord getNewRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
    }

    private static boolean provideNdefOperations(Tag tag, NdefMessage message) {
        NdefFormatable format = NdefFormatable.get(tag);
        if (format != null) {
            try {
                format.connect();
                format.format(message);
                return true;
            } catch (IOException | FormatException e) {
                Log.getStackTraceString(e);
                return false;
            }
        } else {
            Log.e(NFCUtils.class.toString(), "Error : Undefined format");
            return false;
        }
    }
}
