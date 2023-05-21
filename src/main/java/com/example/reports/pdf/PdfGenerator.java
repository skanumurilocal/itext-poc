package com.example.reports.pdf;

import com.example.reports.exception.ReportsException;
import com.example.reports.model.DisplayItem;
import com.example.reports.model.TableFields;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.example.reports.model.AppInfo;
import com.example.reports.service.TableHeaderEventHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class PdfGenerator {

    public static final String REQUEST_REFERENCE_ID = "Request Reference ID";
    public static final String REQUEST_DATE = "Request Date";
    public static final String SUBJECT_TYPE = "Subject Type";
    public static final String REQUEST_TYPE = "Request Type";
    public static final String BILLING_ACCOUNT_NUMBER = "Billing Account Number";
    public static final String MOBILE_NUMBER = "Mobile Number";
    protected final Color GRAY = new DeviceRgb(245, 245, 245);
    protected final Color GRAY_LINE = new DeviceRgb(212, 212, 212);
    protected final Color WHITE = new DeviceRgb(255, 255, 255);
    public static final String DEST1 = "c:/results/sample/sample10pages.pdf";
    public static final String DEST2 = "c:/results/sample/samplePdf.pdf";

    public void generatePdf(Map<Integer, Map<String,List<AppInfo>>> reportData,
                            Map<String,String> resultData, HttpServletResponse response) throws IOException {

        generatePdf(reportData, resultData);
        mergePdf(response);

    }

    private void mergePdf(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-disposition", "attachment; filename=" + "testFile.pdf");
        PdfDocument pdf = new PdfDocument(new PdfWriter(response.getOutputStream()));
        PdfMerger merger = new PdfMerger(pdf);
        PdfDocument firstSourcePdf = null;
        PdfDocument secondSourcePdf = null;
        try {
            firstSourcePdf = new PdfDocument(new PdfReader(DEST1));
            merger.merge(firstSourcePdf, 1, firstSourcePdf.getNumberOfPages());
            secondSourcePdf = new PdfDocument(new PdfReader(DEST2));
            merger.merge(secondSourcePdf, 1, secondSourcePdf.getNumberOfPages());
        } finally {
            merger.close();
            if(firstSourcePdf!=null)
                firstSourcePdf.close();
            if(secondSourcePdf!=null)
                secondSourcePdf.close();
            pdf.close();
        }

    }

    private void generatePdf(Map<Integer, Map<String, List<AppInfo>>> reportData, Map<String, String> resultData) throws FileNotFoundException {
        PdfDocument secondPdf = new PdfDocument(new PdfWriter(DEST2));
        // Initialize document
        Document document = new Document(secondPdf);
        try {
            TableHeaderEventHandler handler = new TableHeaderEventHandler(document, "Test Customer Document");
            secondPdf.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
            List<DisplayItem> requesterData = new ArrayList<>();
            requesterData.add(new DisplayItem(REQUEST_REFERENCE_ID, resultData.containsValue(REQUEST_REFERENCE_ID) ? resultData.get(REQUEST_REFERENCE_ID):""));
            requesterData.add(new DisplayItem(REQUEST_DATE, resultData.containsValue(REQUEST_DATE) ? resultData.get(REQUEST_DATE):""));
            requesterData.add(new DisplayItem(SUBJECT_TYPE, resultData.containsValue(SUBJECT_TYPE) ? resultData.get(SUBJECT_TYPE):""));
            requesterData.add(new DisplayItem(REQUEST_TYPE, resultData.containsValue(REQUEST_TYPE) ? resultData.get(REQUEST_TYPE):""));
            writeData(document, requesterData,null);
            List<DisplayItem> billingAccountInfo = new ArrayList<>();
            billingAccountInfo.add(new DisplayItem(BILLING_ACCOUNT_NUMBER, resultData.containsValue(BILLING_ACCOUNT_NUMBER) ? resultData.get(BILLING_ACCOUNT_NUMBER):""));
            billingAccountInfo.add(new DisplayItem(MOBILE_NUMBER, resultData.containsValue(MOBILE_NUMBER) ? resultData.get(MOBILE_NUMBER):""));
            writeData(document, billingAccountInfo,null);
            for (Map.Entry<Integer, Map<String, List<AppInfo>>> entry : reportData.entrySet()) {
                System.out.println("Parent Key:  "+entry.getKey()+",  Parent Value: "+ entry.getKey());

                List<DisplayItem> regularIdentifierData = new ArrayList<>();
                for (Map.Entry<String, List<AppInfo>> subEntry : entry.getValue().entrySet()) {
                    List<AppInfo> distinctValues = subEntry.getValue().stream().filter(distinctByKey(appInfo -> appInfo.getBusinessTerm()))
                            .sorted(Comparator.comparing(AppInfo::getBusinessTerm))
                            .collect(Collectors.toList());
                    for(AppInfo appInfo: distinctValues) {
                        System.out.println("    AppID: "+appInfo.getAppId()+"   Child Key : " + subEntry.getKey() + ",  Child Value : " + appInfo.getBusinessTerm());
                        if(resultData.containsKey(appInfo.getBusinessTerm()))
                            regularIdentifierData.add(new DisplayItem(appInfo.getBusinessTerm(), resultData.containsKey(appInfo.getBusinessTerm())? resultData.get(appInfo.getBusinessTerm()):""));
                    }
                    if(regularIdentifierData.size()>0)
                    writeData(document,regularIdentifierData,subEntry.getKey());
                }
            }
        } catch (Exception e) {
            throw new ReportsException(e);
        } finally {
            secondPdf.close();
            document.close();
        }

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        final Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public void writeData(Document document, List<DisplayItem> requesterData, String o) {
        addItemFields(document,requesterData,o);
        document.add(new Paragraph("\n"));

    }

    public void addItemFields(Document document, List<DisplayItem> displayValues, String headerName){
        if(headerName!=null) {
            document.add(getBlockTitle(headerName).setMarginTop(0));
            document.add(new Paragraph("\n"));
        }

        TableFields movementFields = new TableFields();
        displayValues.forEach(item ->{
            movementFields.add(item.getDisplayName(),item.getDisplayValue());

        });
        Table movementDetails = createTable(movementFields);
        document.add(movementDetails);
    }

    public Paragraph getBlockTitle(String title) {
        return new Paragraph(title)
                .setFontSize(13)
                .setBorderBottom(new SolidBorder(GRAY_LINE, 1))
                .setMarginTop(35);
    }

    public Table createTable(TableFields tableFields) {
        Table table = new Table(new float[]{220F, 300F});
        AtomicInteger rowCounter = new AtomicInteger(0);

        tableFields.getFieldList().forEach(field ->
                insertIfNotNull(field.getDisplayName(), field.getDisplayValue(), table, rowCounter));
        return table;
    }

    public void insertIfNotNull(String displayName, Object value, Table table, AtomicInteger rowCounter) {
        if (value != null) {
            Color color = rowCounter
                    .getAndIncrement() % 2 == 0 ?
                    GRAY :
                    WHITE;

            table.addCell(new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setBackgroundColor(color)
                    .add(new Paragraph(displayName)));

            table.addCell(new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setBackgroundColor(color)
                    .add(new Paragraph(String.valueOf(value))));
        }
    }
}
