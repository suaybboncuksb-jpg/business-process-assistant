package com.suayb.bpa.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.suayb.bpa.model.ProcessCase;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class CsvImportService {

    public List<ProcessCase> parseCsv(MultipartFile file) throws Exception {
        InputStreamReader reader = new InputStreamReader(file.getInputStream());
        return new CsvToBeanBuilder<ProcessCase>(reader)
                .withType(ProcessCase.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();
    }
}
