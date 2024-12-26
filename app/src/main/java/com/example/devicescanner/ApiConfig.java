package com.example.devicescanner;

import android.content.Context;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfig {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit(Context context) {

        if (retrofit == null) {
            try {
                // Получаем сертификат из res/raw
                InputStream certificateInputStream = context.getResources().openRawResource(R.raw.server_cert);  // server.crt

                // Создаем объект KeyStore и загружаем в него сертификат
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null); // Инициализируем пустой хранилище

                // Добавляем сертификат в хранилище
                keyStore.setCertificateEntry("server", CertificateFactory.getInstance("X.509").generateCertificate(certificateInputStream));

                // Создаем TrustManagerFactory с этим хранилищем
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);

                // Создаем SSLContext с нашим доверенным сертификатом
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

                // Настроим OkHttpClient с созданным SSLContext
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .sslSocketFactory(sslContext.getSocketFactory())
                        .build();

                // Создаем Retrofit с этим клиентом
                retrofit = new Retrofit.Builder()
                        .baseUrl("https://10.0.0.12:8443/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return retrofit;
    }
}
