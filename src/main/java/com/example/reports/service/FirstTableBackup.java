package com.example.reports.service;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class FirstTableBackup {
    public static final String DEST = "c:/results/chapter05/my_first_table.pdf";
    public static final String DEST1 = "c:/results/sample/samplePdf.pdf";
    protected final int MAX_IMAGE_NUM = 4;
    protected final Color GRAY = new DeviceRgb(245, 245, 245);
    protected final Color GRAY_LINE = new DeviceRgb(212, 212, 212);
    protected final Color WHITE = new DeviceRgb(255, 255, 255);

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FirstTableBackup().createPdf(DEST);
        File file1= new File(DEST1);
        file1.getParentFile().mkdirs();
        new FirstTableBackup().generatePdf(DEST1);
    }

    public void createPdf(String dest) throws IOException {
        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));

        // Initialize document
        Document document = new Document(pdf);
        Table table = new Table(new float[] {1, 1, 1});
        table.addCell(new Cell(1, 3).add(new Paragraph("Cell with colspan 3")));
        table.addCell(new Cell(2, 1).add(new Paragraph("Cell with rowspan 2")));
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        document.add(table);
        document.close();
    }

    protected Table createTable(TableFields tableFields) {
        Table table = new Table(new float[]{220F, 300F});
        AtomicInteger rowCounter = new AtomicInteger(0);

        tableFields.fieldList.forEach(field ->
                insertIfNotNull(field.displayName, field.value, table, rowCounter));
        return table;
    }

    protected void insertIfNotNull(String displayName, Object value, Table table, AtomicInteger rowCounter) {
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

    protected Paragraph getBlockTitle(String title) {
        return new Paragraph(title)
                .setFontSize(13)
                .setBorderBottom(new SolidBorder(GRAY_LINE, 1))
                .setMarginTop(35);
    }

    protected void writeData(Document document, List <Item> items) {
        items.forEach(item -> {
            addItemFields(document, item);
            document.add(new Paragraph("\n"));
        });

    }

    protected  void generatePdf(String dest1) throws FileNotFoundException {
        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest1));

        // Initialize document
        Document document = new Document(pdf);
        TableHeaderEventHandler handler = new TableHeaderEventHandler(document, "Sample Document");
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
        List<DisplayItem> requesterData = new ArrayList<>();
        requesterData.add(new DisplayItem("Request Reference ID", "ABCD12345"));
        requesterData.add(new DisplayItem("Request Date","2023-MARCH-17"));
        requesterData.add(new DisplayItem("Subject Type","FORMER CUSTOMER"));
        requesterData.add(new DisplayItem("Request Type","ACCESS PERSONAL DATA"));
        writeData(document, requesterData,null);
        requesterData.add(new DisplayItem("Request Reference ID", "ABCD12345"));
        requesterData.add(new DisplayItem("Request Date","2023-MARCH-17"));
        requesterData.add(new DisplayItem("Subject Type","FORMER CUSTOMER"));
        requesterData.add(new DisplayItem("Request Type","ACCESS PERSONAL DATA"));
        writeData(document, requesterData,null);
        requesterData.add(new DisplayItem("Request Reference ID", "ABCD12345"));
        requesterData.add(new DisplayItem("Request Date","2023-MARCH-17"));
        requesterData.add(new DisplayItem("Subject Type","FORMER CUSTOMER"));
        requesterData.add(new DisplayItem("Request Type","ACCESS PERSONAL DATA"));
        writeData(document, requesterData,null);

        requesterData.add(new DisplayItem("Request Reference ID", "ABCD12345"));
        requesterData.add(new DisplayItem("Request Date","2023-MARCH-17"));
        requesterData.add(new DisplayItem("Subject Type","FORMER CUSTOMER"));
        requesterData.add(new DisplayItem("Request Type","ACCESS PERSONAL DATA"));
        writeData(document, requesterData,null);

        document.close();
    }

    private void writeData(Document document, List<DisplayItem> requesterData, String o) {
        addItemFields(document,requesterData,o);
        document.add(new Paragraph("\n"));

    }

    private void addItemFields(Document document, Item item) {
        document.add(getBlockTitle("Report: " + item.getId()).setMarginTop(0));

        TableFields movementFields = new TableFields();
        movementFields.add("Report Id", item.getId());
        movementFields.add("Item Name", item.getName());
        movementFields.add("Title", item.getTitle());
        movementFields.add("Description", item.getDescription());
        movementFields.add("Area", item.getArea());
        movementFields.add("Location", item.getLocation());
        Table movementDetails = createTable(movementFields);

        document.add(movementDetails);


    }

    private void addItemFields(Document document, List<DisplayItem> displayValues,String headerName){
        if(headerName!=null)
        document.add(getBlockTitle(headerName).setMarginTop(0));

        TableFields movementFields = new TableFields();
        displayValues.forEach(item ->{
            movementFields.add(item.getDisplayName(),item.getDisplayValue());

        });
        Table movementDetails = createTable(movementFields);
        document.add(movementDetails);
    }


    protected static class DisplayItem {
        private String displayName;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayValue() {
            return displayValue;
        }

        public void setDisplayValue(String displayValue) {
            this.displayValue = displayValue;
        }

        private String displayValue;

        protected DisplayItem(String displayName,String displayValue){
            this.displayName = displayName;
            this.displayValue=displayValue;

        }
    }
    protected  static class Item {
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getArea() {
            return area;
        }

        public void setArea(double area) {
            this.area = area;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public long id;
        public String name;
        public String title;
        public String description;
        public double area;
        public String location;

    }

    protected static class TableField {
        public String displayName;
        public Object value;

        protected TableField(String displayName, Object value) {
            this.displayName = displayName;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TableField)) return false;
            TableField that = (TableField) o;
            return Objects.equals(displayName, that.displayName) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(displayName, value);
        }
    }

    protected static class TableFields {
        private List<TableField> fieldList = new ArrayList<>();

        protected void add(String displayName, Object value) {
            fieldList.add(new TableField(displayName, value));
        }

        protected void add(TableField field) {
            fieldList.add(field);
        }
    }
}
