package com.apap.director.db;

import com.apap.director.network.rest.Paths;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;

import info.guardianproject.netcipher.NetCipher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Adam on 2017-01-08.
 */

public class OkHttpClientTest {
    private Retrofit retrofit;

    @Test
    public void testSocksFunctionality() throws Exception {
        final String baseUrl = "http://"+ Paths.SERVER_IP+":"+Paths.SERVER_PORT;

        final OkHttpClient client = new OkHttpClient.Builder()
                .proxy(NetCipher.ORBOT_HTTP_PROXY)
                .build();

            this.retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(client)
                    .build();


        final Request request = new Request.Builder()
                .url("https://www.google.com/")
                .build();
        assertTrue(client.newCall(request).execute().isSuccessful());
    }
}
