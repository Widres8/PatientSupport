package patientsupport.patientsupport.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import patientsupport.patientsupport.helpers.PatientByZones;
import patientsupport.patientsupport.helpers.Selects;
import patientsupport.patientsupport.models.parameters.City;
import patientsupport.patientsupport.models.parameters.StatusReason;
import patientsupport.patientsupport.models.parameters.Zone;

@Controller
@RequestMapping("generics")
public class GenericController {

	private Selects selects;
	private EntityManager em;

    @Autowired
    public GenericController(Selects selects, EntityManager em) {
        this.selects = selects;
        this.em = em;
	}
	
	@RequestMapping(value = "/patientbyzones", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody List<PatientByZones> getPatientByZones(){
        StoredProcedureQuery sp = em.createStoredProcedureQuery("patientbyzones");
        boolean result = sp.execute();
        if (result == true) {
            return sp.getResultList();
        } else {
            // Handle the false for no result set returned, e.g.
            throw new RuntimeException("No result set(s) returned from the stored procedure"); 
        }
	}

	@RequestMapping(value = "/patientbyzonespdfreport", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPatientByZonesReport() {

        StoredProcedureQuery sp = em.createStoredProcedureQuery("patientbyzones");
        boolean result = sp.execute();
        List<Object[]> data = new ArrayList<Object[]>();
        if (result == true) {
			data = sp.getResultList();
        } else {
            // Handle the false for no result set returned, e.g.
            throw new RuntimeException("No result set(s) returned from the stored procedure"); 
        }

        ByteArrayInputStream bis = generatePdf1(data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
    }

	@RequestMapping(value = "/patientsbytherapypdfreport", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPatientByTheraphyReport() {

        StoredProcedureQuery sp = em.createStoredProcedureQuery("patientsbytherapy");
        boolean result = sp.execute();
        List<Object[]> data = new ArrayList<Object[]>();
        if (result == true) {
			data = sp.getResultList();
        } else {
            // Handle the false for no result set returned, e.g.
            throw new RuntimeException("No result set(s) returned from the stored procedure"); 
        }

        ByteArrayInputStream bis = generatePdf2(data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
    }
	
	@RequestMapping(value = "/patientsbytherapy", method = RequestMethod.GET, produces = "application/json")
    public  @ResponseBody List<Object> getPatientsByTherapy(){
        StoredProcedureQuery sp = em.createStoredProcedureQuery("patientsbytherapy");
        boolean result = sp.execute();
        if (result == true) {
            return sp.getResultList();
        } else {
            // Handle the false for no result set returned, e.g.
            throw new RuntimeException("No result set(s) returned from the stored procedure"); 
        }
	}

    @RequestMapping(value = "/getZonesByCountryId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody List<Zone> getZonesByCountryId(@RequestParam Integer countryId) {
		try {
			return selects.getZones(countryId);

		} catch (Exception ex) {
			return null;
		}
    }
    
    @RequestMapping(value = "/getCitiesByDepartmentId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody List<City> getCitiesByDepartmentId(@RequestParam Integer departmentId) {
		try {
			return selects.getCities(departmentId);

		} catch (Exception ex) {
			return null;
		}
    }
    
    @RequestMapping(value = "/getStatusReasonByStatusId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody List<StatusReason> getStatusReasonByStatusId(@RequestParam Integer statusId) {
		try {
			return selects.getStatusReasons(statusId);

		} catch (Exception ex) {
			return null;
		}
    }
    
    public static ByteArrayInputStream generatePdf1(List<Object[]> data) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(60);
            float[] columnWidths = {1f, 1f, 1f};
            table.setWidths(columnWidths);

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            
            PdfPCell hcell1 = new PdfPCell(new Phrase("Country", headFont));
            hcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell1);

            PdfPCell hcell2 = new PdfPCell(new Phrase("Zone", headFont));
            hcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell2);

            PdfPCell hcell3 = new PdfPCell(new Phrase("Patients", headFont));
            hcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell3);

            ArrayList<String[]> datapdf = new ArrayList<>();

            for (Object[] item : data) {
                datapdf.add(new String[]{item[0].toString(),item[1].toString(),item[2].toString()} );
            }


            for (String[] item : datapdf) {

                PdfPCell cell1 = new PdfPCell(new Phrase(item[0].toString()));
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell1);

                PdfPCell cell2 = new PdfPCell(new Phrase(item[1].toString()));
                cell2.setPaddingLeft(5);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell2);

                PdfPCell cell3 = new PdfPCell(new Phrase(String.valueOf(item[2].toString())));
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell3.setPaddingRight(5);
                table.addCell(cell3);
            }

            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();
            writer.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (DocumentException ex) {
            return null;
        }
        
    }

    public static ByteArrayInputStream generatePdf2(List<Object[]> data) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(60);
            float[] columnWidths = {1f, 1f};
            table.setWidths(columnWidths);

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            
            PdfPCell hcell1 = new PdfPCell(new Phrase("Theraphy", headFont));
            hcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell1);

            PdfPCell hcell3 = new PdfPCell(new Phrase("Patients", headFont));
            hcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell3);

            ArrayList<String[]> datapdf = new ArrayList<>();

            for (Object[] item : data) {
                datapdf.add(new String[]{item[0].toString(),item[1].toString()} );
            }


            for (String[] item : datapdf) {

                PdfPCell cell1 = new PdfPCell(new Phrase(item[0].toString()));
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell1);

                PdfPCell cell3 = new PdfPCell(new Phrase(String.valueOf(item[1].toString())));
                cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell3.setPaddingRight(5);
                table.addCell(cell3);
            }

            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();
            writer.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (DocumentException ex) {
            return null;
        }
        
    }
	
}