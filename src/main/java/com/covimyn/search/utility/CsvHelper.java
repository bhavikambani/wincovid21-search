/**
 * Created by Avishek Gurung on 2021-05-05
 */

package com.covimyn.search.utility;

import com.covimyn.search.model.ResourceModel;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Component
public class CsvHelper {

    @Autowired
    private DateUtil dateUtil;
    private final static CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    private String configFile;

    private Sheets service;
    private static final String ROWS = "ROWS";
    private static final String WRITE_RANGE_SUFFIX = "!A1:Z";
    private static final String RAW = "RAW";


    @Autowired
    public CsvHelper(@Value("${google.sheet.config}") String configFile) throws Exception {
        this.configFile = configFile;
        service = getService();
    }

    private Sheets getService() throws IOException, GeneralSecurityException {
        InputStream in = new FileInputStream(configFile);
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);
        Credential credential = GoogleCredential.fromStream(in)
                .createScoped(scopes);
        Sheets sheets = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("avishek-gsurung").build();
        return sheets;
    }

    public UpdateValuesResponse upload(String sheetName, String sheetId, List<ResourceModel> resourceModels) throws IOException {
        List<List<Object>> data = new ArrayList<>();
        List<Object> headers = getHeaders();
        data.add(headers);
        int count = 1;

        for(ResourceModel resourceModel : resourceModels) {
            List<Object> row = getRow(resourceModel, count++);
            data.add(row);
        }

        //Logic to make remaining rows empty. Has a better way to do.
        int remainRowsToEmpty = 10000;
        for(int i=0; i < remainRowsToEmpty; i++) {
            List<Object> row = new ArrayList<>();
            for(int j=0; j < headers.size(); j++) {
                row.add("");
            }
            data.add(row);
        }
        //ends here.

        String writeRange = sheetName + WRITE_RANGE_SUFFIX;
        ValueRange vr = new ValueRange().setValues(data).setMajorDimension(ROWS);
        UpdateValuesResponse response = service.spreadsheets().values()
                .update(sheetId, writeRange, vr)
                .setValueInputOption(RAW)
                .execute();
        return response;
    }


    public ByteArrayInputStream download(List<ResourceModel> resourceModels) throws IOException {
        int count = 1;
        List<Object> headers = getHeaders();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
                csvPrinter.printRecord(headers);
                for (ResourceModel resourceModel : resourceModels) {
                    List<Object> row = getRow(resourceModel, count++);
                    csvPrinter.printRecord(row);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to import data to CSV file: " + e.getMessage());
        }
    }

    private List<Object> getHeaders() {
        List<Object> headers = new ArrayList<Object>(){
            {
                add("Sl no.");
                add(Constant.NAME);
                add(Constant.STATE);
                add(Constant.CITY);
                add(Constant.CATEGORY);
                add(Constant.SUBCATEGORY);
                add(Constant.VERIFIED);
                add(Constant.AVAILABLE);
                add(Constant.UPDATED_AT);
                add(Constant.PHONE1);
                add(Constant.PHONE2);
                add(Constant.ADDRESS);
                add(Constant.PINCODE);
                add(Constant.EMAIL);
                add(Constant.CATEGORYID);
                add(Constant.SUBCATEGORYID);
                add(Constant.CITYID);
                add(Constant.STATEID);
                add(Constant.CREATED_BY);
                add(Constant.UPDATED_BY);
                add(Constant.CREATED_AT);
                add(Constant.VALID);
                add(Constant.ID);
            }
        };
        return headers;
    }

    private List<Object> getRow(ResourceModel resourceModel, int rowNum) {
        List<Object> row = new ArrayList<>();
        String name = resourceModel.getName() == null ? "" : resourceModel.getName();
        String state = resourceModel.getState() == null ? "" : resourceModel.getState();
        String city = resourceModel.getCity() == null ? "" : resourceModel.getCity();
        String category = resourceModel.getCategory() == null ? "" : resourceModel.getCategory();
        String subCategory = resourceModel.getSubcategory() == null ? "" : resourceModel.getSubcategory();
        String phone1 = resourceModel.getPhone1() == null ? "" : resourceModel.getPhone1();
        String phone2 = resourceModel.getPhone2() == null ? "" : resourceModel.getPhone2();
        String address = resourceModel.getAddress() == null ? "" : resourceModel.getAddress();
        String pincode = resourceModel.getPincode() == null ? "" : resourceModel.getPincode();
        String email = resourceModel.getEmail() == null ? "" : resourceModel.getEmail();
        String createdBy = resourceModel.getCreatedBy() == null ? "" : resourceModel.getCreatedBy();
        String updatedBy = resourceModel.getUpdatedBy() == null ? "" : resourceModel.getUpdatedBy();
        String id = resourceModel.getId() == null ? "" : resourceModel.getId();

        row.add(String.valueOf(rowNum));
        row.add(name);
        row.add(state);
        row.add(city);
        row.add(category);
        row.add(subCategory);
        row.add(String.valueOf(resourceModel.isVerified()));
        row.add(String.valueOf(resourceModel.isAvailable()));
        row.add(dateUtil.epochToDate(Long.parseLong(resourceModel.getUpdatedAt())));
        row.add(phone1);
        row.add(phone2);
        row.add(address);
        row.add(pincode);
        row.add(email);
        row.add(String.valueOf(resourceModel.getCategoryId()));
        row.add(String.valueOf(resourceModel.getSubcategoryId()));
        row.add(String.valueOf(resourceModel.getCityId()));
        row.add(String.valueOf(resourceModel.getStateId()));
        row.add(createdBy);
        row.add(updatedBy);
        row.add(dateUtil.epochToDate(Long.parseLong(resourceModel.getCreatedAt())));
        row.add(String.valueOf(resourceModel.isValid()));
        row.add(id);
        return row;
    }
}
