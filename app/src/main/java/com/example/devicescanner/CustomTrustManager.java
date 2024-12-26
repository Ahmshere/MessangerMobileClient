package com.example.devicescanner;

import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class CustomTrustManager {

    public static SSLContext getSSLContext(Context context) throws Exception {
        // Создаем объект для обработки сертификатов
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        // Загружаем сертификат из ресурсов
        InputStream caInput = context.getResources().openRawResource(R.raw.server_cert); // Файл server_cert.crt
        Certificate ca;
        try {
            // Генерируем сертификат
            ca = cf.generateCertificate(caInput);
        } finally {
            caInput.close();
        }

        // Создаем KeyStore для хранения сертификатов
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null); // Инициализируем пустое хранилище

        // Добавляем серверный сертификат в хранилище с алиасом "selfsigned"
        keyStore.setCertificateEntry("selfsigned", ca);  // Используем алиас "selfsigned"

        // Создаем TrustManagerFactory для создания менеджера доверия
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        // Создаем SSLContext с нашим доверенным сертификатом
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Возвращаем настроенный SSLContext
        return sslContext;
    }
}
