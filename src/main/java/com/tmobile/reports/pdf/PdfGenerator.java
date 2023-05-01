package com.tmobile.reports.pdf;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.tmobile.reports.model.AppInfo;
import com.tmobile.reports.model.DisplayItem;
import com.tmobile.reports.model.TableFields;
import com.tmobile.reports.service.TableHeaderEventHandler;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PdfGenerator {

    protected final Color GRAY = new DeviceRgb(245, 245, 245);
    protected final Color GRAY_LINE = new DeviceRgb(212, 212, 212);
    protected final Color WHITE = new DeviceRgb(255, 255, 255);
    public static final String DEST1 = "c:/results/sample/samplePdf.pdf";

    public void generatePdf(Map<Integer, Map<String,List<AppInfo>>> reportData) throws FileNotFoundException {
        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(new PdfWriter(DEST1));

        // Initialize document
        Document document = new Document(pdf);
        TableHeaderEventHandler handler = new TableHeaderEventHandler(document, "Test Customer Document");
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
        List<DisplayItem> requesterData = new ArrayList<>();
        requesterData.add(new DisplayItem("Request Reference ID", "ABCD12345"));
        requesterData.add(new DisplayItem("Request Date","2023-MARCH-17"));
        requesterData.add(new DisplayItem("Subject Type","FORMER CUSTOMER"));
        requesterData.add(new DisplayItem("Request Type","ACCESS PERSONAL DATA"));
        writeData(document, requesterData,null);
        List<DisplayItem> billingAccountInfo = new ArrayList<>();
        billingAccountInfo.add(new DisplayItem("Billing Account Number", "98765432"));
        billingAccountInfo.add(new DisplayItem("Mobile Number","(902)9893033"));
        writeData(document, billingAccountInfo,null);
        for (Map.Entry<Integer, Map<String, List<AppInfo>>> entry : reportData.entrySet()) {
            System.out.println("Parent Key:  "+entry.getKey()+",  Parent Value: "+ entry.getKey());

            List<DisplayItem> regularIdentifierData = new ArrayList<>();
            for (Map.Entry<String, List<AppInfo>> subEntry : entry.getValue().entrySet()) {
                for(AppInfo appInfo: subEntry.getValue()) {
                    System.out.println("    AppID: "+appInfo.getAppId()+"   Child Key : " + subEntry.getKey() + ",  Child Value : " + appInfo.getBusinessTerm());
                    regularIdentifierData.add(new DisplayItem(appInfo.getBusinessTerm(), ""));
                }
                writeData(document,regularIdentifierData,subEntry.getKey());
            }
        }
       document.close();
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
