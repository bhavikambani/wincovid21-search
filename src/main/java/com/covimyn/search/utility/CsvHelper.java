/**
 * Created by Avishek Gurung on 2021-05-05
 */

package com.covimyn.search.utility;

import com.covimyn.search.model.ResourceModel;
import com.covimyn.search.services.DateUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class CsvHelper {

    private DateUtil dateUtil;
    private final static CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    public ByteArrayInputStream convertResourceModelToCSV(List<ResourceModel> resourceModels) throws IOException {
        int count = 1;
        List<String> headers = new ArrayList<String>(){
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
                add(Constant.ID);
            }
        };

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
                csvPrinter.printRecord(headers);
                for (ResourceModel resourceModel : resourceModels) {
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(count));
                    row.add(resourceModel.getName());
                    row.add(resourceModel.getState());
                    row.add(resourceModel.getCity());
                    row.add(resourceModel.getCategory());
                    row.add(resourceModel.getSubcategory());
                    row.add(String.valueOf(resourceModel.isVerified()));
                    row.add(String.valueOf(resourceModel.isAvailable()));
                    row.add(dateUtil.epochToDate(Long.parseLong(resourceModel.getUpdatedAt())));
                    row.add(resourceModel.getPhone1());
                    row.add(resourceModel.getPhone2());
                    row.add(resourceModel.getAddress());
                    row.add(resourceModel.getPincode());
                    row.add(resourceModel.getEmail());
                    row.add(String.valueOf(resourceModel.getCategoryId()));
                    row.add(String.valueOf(resourceModel.getSubcategoryId()));
                    row.add(String.valueOf(resourceModel.getCityId()));
                    row.add(String.valueOf(resourceModel.getStateId()));
                    row.add(resourceModel.getCreatedBy());
                    row.add(resourceModel.getUpdatedBy());
                    row.add(resourceModel.getCreatedAt());
                    csvPrinter.printRecord(row);
                    row.add(resourceModel.getId());
                    count++;
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to import data to CSV file: " + e.getMessage());
        }
    }
}
