/**
 * Created by Avishek Gurung on 2021-05-06
 */

package com.covimyn.search;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleSheetIntegrationPoc {
    private static final String CREDENTIALS_FILE_PATH = "/google_sheet_config.json";

    public static void main(String[] args) throws Exception{

        File file =new File("/tmp/credentials.json");
        //InputStream in = GoogleSheetIntegrationPoc.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        InputStream in = new FileInputStream(file);
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS,SheetsScopes.DRIVE);
        Credential credential = GoogleCredential.fromStream(in)
                .createScoped(scopes);
        Sheets sheets = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("avishek-gsurung").build();
        /*ValueRange readResult = sheets.spreadsheets().values()
                .get("1o1N2D1J4VZPU4sA5MRMjFchJZA_AMghp21snuBJQuB8","A1:E")
                .execute();

        List<List<Object>> values = readResult.getValues();
        System.out.println(values);*/


        //Inserting
        String writeRange = "Sheet1!A1:E";
        List<List<Object>> writeData = new ArrayList<>();

        List<Object> list1 = new ArrayList<>();
        list1.add("My");
        list1.add("Way");
        list1.add("Or");
        writeData.add(list1);

        list1 = new ArrayList<>();
        list1.add("I");
        list1.add("4");
        list1.add("001");
        writeData.add(list1);

        ValueRange vr = new ValueRange().setValues(writeData).setMajorDimension("ROWS");
        UpdateValuesResponse x = sheets.spreadsheets().values()
                .update("1o1N2D1J4VZPU4sA5MRMjFchJZA_AMghp21snuBJQuB8", writeRange, vr)
                .setValueInputOption("RAW")
                .execute();


        System.out.println("DONE");

    }


}
