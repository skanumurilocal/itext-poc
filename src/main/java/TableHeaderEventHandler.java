import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

class TableHeaderEventHandler implements IEventHandler {
    private Table table;
    private float tableHeight;
    private Document doc;

    public TableHeaderEventHandler(Document doc, String documentTitle) {
        this.doc = doc;
        // Calculate top margin to be sure that the table will fit the margin.
        initTable(documentTitle);

        TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
        renderer.setParent(new DocumentRenderer(doc));

        // Simulate the positioning of the renderer to find out how much space the header table will occupy.
        LayoutResult result = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
        tableHeight = result.getOccupiedArea().getBBox().getHeight();

        // set top margin
        float topMargin = 36 + getTableHeight();
        doc.setMargins(topMargin, 36, 36, 36);
    }

    @Override
    public void handleEvent(Event currentEvent) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        PageSize pageSize = pdfDoc.getDefaultPageSize();
        float coordX = pageSize.getX() + doc.getLeftMargin();
        float coordY = pageSize.getTop() - doc.getTopMargin();
        float width = pageSize.getWidth() - doc.getRightMargin() - doc.getLeftMargin();
        float height = getTableHeight();
        Rectangle rect = new Rectangle(coordX, coordY, width, height);

        new Canvas(canvas, rect)
                .add(table)
                .close();
    }

    public float getTableHeight() {
        return tableHeight;
    }

    private void initTable(String documentTitle) {
        table = new Table(new float[]{320F, 200F});
        table.useAllAvailableWidth();
        Cell title = new Cell();
        title.setBorder(Border.NO_BORDER);
        Paragraph movement_report = new Paragraph(documentTitle).setFontSize(17);
        title.add(movement_report);
        table.addCell(title);
        table.setMarginBottom(20);

    }
}
