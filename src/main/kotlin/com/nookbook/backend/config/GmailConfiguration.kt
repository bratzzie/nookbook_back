package com.nookbook.backend.config

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader


@Configuration
open class GmailConfiguration {
    val APPLICATION_NAME: String = "Nookbook"
    val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
    val TOKENS_DIRECTORY_PATH: String = "tokens"
    val SCOPES: List<String> = listOf(GmailScopes.GMAIL_SEND)
    val CREDENTIALS_FILE: String = "credentials.json"

    fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
        // Load client secrets.
        val inputStream: InputStream = this.javaClass.classLoader.getResource(CREDENTIALS_FILE)?.openStream()
            ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE")

        val clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(inputStream))

        val flow = GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()

        val receiver = LocalServerReceiver.Builder().setPort(8888).build()

        val credential = AuthorizationCodeInstalledApp(flow, receiver).authorize("user")

        inputStream.close()
        return credential
    }

    @Bean
    open fun getService(): Gmail {
        val HTTP_TRANSPORT: HttpTransport

        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        return Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build()

    }

}